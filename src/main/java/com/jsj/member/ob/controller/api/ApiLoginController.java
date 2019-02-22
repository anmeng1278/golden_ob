package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.mini.RegisterRequ;
import com.jsj.member.ob.dto.mini.RegisterResp;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.WechatLogic;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weixin.popular.bean.user.User;
import weixin.popular.bean.wxa.WxaDUserInfo;
import weixin.popular.util.WxaUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiLoginController {


    private final Logger logger = LoggerFactory.getLogger(ApiLoginController.class);

    //region (public) 微信小程序数据初始化 register

    /**
     * 解析用户信息
     *
     * @param requ
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信小程序数据初始化，用户授权后调用")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response<RegisterResp> register(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<RegisterRequ> requ, HttpServletRequest request) throws Exception {

        RegisterResp resp = new RegisterResp();
        Object attribute = request.getSession().getAttribute(Constant.wxapp_session_key);

        if (attribute == null) {
            throw new TipException("登录信息丢失，请重新登录");
        }

        JSONObject jsonObject = (JSONObject) attribute;
        String sessionKey = jsonObject.getString("sessionKey");

        logger.info("获取登录数据：{}", JSON.toJSONString(attribute));
        logger.info("{} {} {}", sessionKey, requ.getRequestBody().getEncryptedData(), requ.getRequestBody().getIv());

        WxaDUserInfo wxaDUserInfo = WxaUtil.decryptUserInfo(sessionKey, requ.getRequestBody().getEncryptedData(), requ.getRequestBody().getIv());
        if (wxaDUserInfo == null) {
            //解密失败后清空Session
            request.getSession().removeAttribute(Constant.wxapp_session_key);
            throw new TipException("解密用户信息失败，请重试。");
        }

        //数据插入数据库
        User user = new User();

        user.setUnionid(wxaDUserInfo.getUnionId());
        user.setNickname(wxaDUserInfo.getNickName());
        user.setOpenid(wxaDUserInfo.getOpenId());
        user.setCity(wxaDUserInfo.getCity());
        user.setCountry(wxaDUserInfo.getCountry());
        user.setHeadimgurl(wxaDUserInfo.getAvatarUrl());

        user.setLanguage(wxaDUserInfo.getLanguage());
        user.setProvince(wxaDUserInfo.getProvince());
        user.setSex(Integer.parseInt(wxaDUserInfo.getGender()));

        WechatLogic.Init(user);

        if (requ.getRequestBody().getJsjId() > 0) {
            WechatLogic.BindJSJId(wxaDUserInfo.getOpenId(), requ.getRequestBody().getJsjId());
        }

        resp.setOpenId(wxaDUserInfo.getOpenId());
        resp.setUnionId(wxaDUserInfo.getUnionId());

        return Response.ok(resp);


    }
    //endregion

}
