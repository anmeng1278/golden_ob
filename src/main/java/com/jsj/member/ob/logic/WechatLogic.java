package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.wechat.Jscode2SessionRequest;
import com.jsj.member.ob.dto.api.wechat.Jscode2SessionResponse;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.entity.WechatRelation;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.WechatRelationService;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;
import weixin.popular.bean.user.User;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class WechatLogic extends BaseLogic {

    private final Logger logger = LoggerFactory.getLogger(WechatLogic.class);

    public static WechatLogic wechatLogic;

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatRelationService wechatRelationService;

    @PostConstruct
    public void init() {
        wechatLogic = this;
    }


    //region (public) 获取微信用户信息 GetWechant

    /**
     * 获取微信用户信息
     *
     * @param openId
     * @return
     */
    public static WechatDto GetWechat(String openId) {

        WechatDto dto = new WechatDto();
        dto.setOpenId(openId);

        Wechat wechat = wechatLogic.wechatService.selectById(openId);
        if (wechat == null) {
            dto.setNickname("匿名");
            return dto;
        }


        dto.setCity(wechat.getCity());
        dto.setCountry(wechat.getCountry());
        dto.setCreateTime(wechat.getCreateTime());
        dto.setDeleteTime(wechat.getDeleteTime());
        dto.setHeadimgurl(wechat.getHeadimgurl());

        dto.setJsjid(wechat.getJsjid());
        dto.setLanguage(wechat.getLanguage());
        dto.setNickname(wechat.getNickname());
        dto.setOfficialAccounts(wechat.getOfficialAccounts());
        dto.setOpenId(wechat.getOpenId());
        dto.setProvince(wechat.getProvince());

        dto.setRemarks(wechat.getRemarks());
        dto.setSex(wechat.getSex());
        dto.setSource(wechat.getSource());
        dto.setSubscribe(wechat.getSubscribe());
        dto.setSubscribeTime(wechat.getSubscribeTime());

        dto.setUnionId(wechat.getUnionId());
        dto.setUnsubscribeTime(wechat.getUnsubscribeTime());
        dto.setUpdateTime(wechat.getUpdateTime());

        return dto;

    }
    //endregion

    //region (public) 获取新增用户 GetNewUsers

    /**
     * 获取新增用户
     *
     * @return
     */
    public static Integer GetNewUsers() {
        EntityWrapper<Wechat> wechatWrapper = new EntityWrapper<>();

        wechatWrapper.where("delete_time is null and unsubscribe_time is null and create_time={0}", DateUtils.getCurrentUnixTime());

        return wechatLogic.wechatService.selectCount(wechatWrapper);
    }
    //endregion

    //region (public) 初始化会员数据 Init

    /**
     * 初始化会员数据
     *
     * @param user
     * @return
     */
    public static void Init(User user) {

        Wechat wechat = wechatLogic.wechatService.selectById(user.getOpenid());

        if (wechat == null) {

            wechat = new Wechat();

            wechat.setCity(user.getCity());
            wechat.setCountry(user.getCountry());
            wechat.setCreateTime(DateUtils.getCurrentUnixTime());
            wechat.setHeadimgurl(user.getHeadimgurl());

            wechat.setLanguage(user.getLanguage());
            wechat.setNickname(user.getNickname());
            wechat.setOfficialAccounts(1);
            wechat.setOpenId(user.getOpenid());
            wechat.setProvince(user.getProvince());

            wechat.setRemarks(user.getRemark());
            wechat.setSex(user.getSex());
            wechat.setSource(0);
            wechat.setSubscribe(user.getSubscribe());
            wechat.setSubscribeTime(user.getSubscribe_time());

            wechat.setUnionId(user.getUnionid());
            wechat.setUnsubscribeTime(0);
            wechat.setUpdateTime(DateUtils.getCurrentUnixTime());

            wechatLogic.wechatService.insert(wechat);
        } else {

            wechat.setCity(user.getCity());
            wechat.setCountry(user.getCountry());
            wechat.setHeadimgurl(user.getHeadimgurl());
            wechat.setLanguage(user.getLanguage());
            wechat.setNickname(user.getNickname());

            wechat.setOpenId(user.getOpenid());
            wechat.setProvince(user.getProvince());
            wechat.setRemarks(user.getRemark());
            wechat.setSex(user.getSex());
            wechat.setSubscribe(user.getSubscribe());

            wechat.setSubscribeTime(user.getSubscribe_time());
            wechat.setUpdateTime(DateUtils.getCurrentUnixTime());

            wechatLogic.wechatService.updateById(wechat);

        }
    }

    //endregion

    //region (public) 绑定会员关系 BindRelation

    /**
     * 绑定会员关系
     *
     * @param openId
     * @param relationOpenId
     * @param sourceType
     */
    public static void BindRelation(String openId, String relationOpenId, SourceType sourceType) {

        if (StringUtils.isEmpty(openId)) {
            return;
        }
        if (StringUtils.isEmpty(relationOpenId)) {
            return;
        }

        Wrapper<WechatRelation> wrapper = new EntityWrapper<>();
        wrapper.where("open_id = {0}", openId);
        wrapper.where("relation_open_id = {0}", relationOpenId);
        wrapper.where("type_id = {0}", sourceType.getValue());

        WechatRelation wechatRelation = wechatLogic.wechatRelationService.selectOne(wrapper);
        //已绑定
        if (wechatRelation != null) {
            return;
        }

        wechatRelation = new WechatRelation();
        wechatRelation.setOpenId(openId);
        wechatRelation.setRelationOpenId(relationOpenId);
        wechatRelation.setTypeId(sourceType.getValue());
        wechatRelation.setCreateTime(DateUtils.getCurrentUnixTime());
        wechatRelation.setUpdateTime(DateUtils.getCurrentUnixTime());

        wechatLogic.wechatRelationService.insert(wechatRelation);

    }
    //endregion

    //region (public) 绑定会员编号 BindJSJId

    /**
     * 绑定会员编号
     *
     * @param openId
     * @param jsjId
     */
    public static Integer BindJSJId(String openId, int jsjId) {

        Wechat wechat = wechatLogic.wechatService.selectById(openId);
        if (wechat.getJsjid() == null || wechat.getJsjid() == 0) {
            wechat.setJsjid(jsjId);
            wechatLogic.wechatService.updateById(wechat);
        }

        return wechat.getJsjid();
    }
    //endregion

    //region (public) 根据openId获取绑定关系 GetWechatRelation

    /**
     * 根据openId获取绑定关系
     *
     * @param openId
     * @param sourceType
     * @return
     */
    public static WechatRelation GetWechatRelation(String openId, SourceType sourceType) {

        Wrapper<WechatRelation> wrapper = new EntityWrapper<>();
        wrapper.where("open_id = {0}", openId);
        wrapper.where("type_id = {0}", sourceType.getValue());

        WechatRelation wechatRelation = wechatLogic.wechatRelationService.selectOne(wrapper);
        return wechatRelation;

    }
    //endregion

    //region (public) 小程序jscode换取openId JsCode2Session

    /**
     * 小程序jscode换取openId
     * 登录凭证校验。通过 wx.login() 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。
     * https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code
     *
     * @param request
     * @return
     */
    public static Jscode2SessionResponse JsCode2Session(Jscode2SessionRequest request) {

        Jscode2SessionResponse resp = new Jscode2SessionResponse();

        try {

            String appId = "wx9ccdd5f8678c78d0";
            String appSecret = "b15b8332ace80da296226dde7666cc3e";

            Jscode2sessionResult jscode2session = SnsAPI.jscode2session(appId, appSecret, request.getJsCode());

            wechatLogic.logger.info(String.format("%s %s", JSON.toJSON(request), JSON.toJSONString(jscode2session)));

            if (!jscode2session.isSuccess()) {
                throw new TipException(jscode2session.getErrmsg());
            }
            resp.setOpenId(jscode2session.getOpenid());
            resp.setSessionKey(jscode2session.getSession_key());
            resp.setUnionId(jscode2session.getUnionid());
            resp.getBaseResp().setSuccess(true);

        } catch (Exception ex) {
            resp.getBaseResp().setSuccess(false);
            resp.getBaseResp().setMessage(ex.getMessage());

        }

        return resp;

    }
    //endregion

    /**
     * 活动可以接受消息推送的用户信息
     * @return
     */
    public static  List<Wechat> GetNotifyWechat(){

        EntityWrapper<Wechat> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("ifnotify = 1");
        List<Wechat> wechats = wechatLogic.wechatService.selectList(wrapper);

        return wechats;
    }
}
