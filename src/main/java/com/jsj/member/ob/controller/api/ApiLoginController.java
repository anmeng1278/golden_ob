package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.mini.MiniUserDecodeRequ;
import com.jsj.member.ob.dto.mini.MiniUserDecodeResp;
import com.jsj.member.ob.dto.mini.MiniWechatDto;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiLoginController {


    //region (public) 解析用户信息 userDecode

    /**
     * 解析用户信息
     *
     * @param requ
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "微信小程序用户信息解析")
    @RequestMapping(value = "/userDecode", method = RequestMethod.POST)
    public Response<MiniUserDecodeResp> userDecode(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<MiniUserDecodeRequ> requ, HttpServletRequest request) throws Exception {

        MiniUserDecodeResp resp = new MiniUserDecodeResp();

        Object attribute = request.getSession().getAttribute(Constant.wxapp_session_key);
        if (attribute == null) {
            throw new TipException("缓存已失效，请重新授权登录", 1404);
        }

        JSONObject jsonObject = (JSONObject) attribute;
        String session_key = jsonObject.getString("sessionKey");

        byte[] encrypData = org.apache.commons.codec.binary.Base64.decodeBase64(requ.getRequestBody().getEncryptedData());
        byte[] ivData = org.apache.commons.codec.binary.Base64.decodeBase64(requ.getRequestBody().getIv());
        byte[] sessionKey = org.apache.commons.codec.binary.Base64.decodeBase64(session_key);

        String str = SecurityUtils.decrypt2(sessionKey, ivData, encrypData);
        MiniWechatDto userInfo = JSON.parseObject(str, MiniWechatDto.class);

        if (userInfo != null) {
            //数据插入数据库

            User user = new User();

            user.setNickname(userInfo.getNickName());
            user.setOpenid(userInfo.getOpenId());
            user.setCity(userInfo.getCity());
            user.setCountry(userInfo.getCountry());
            user.setHeadimgurl(userInfo.getAvatarUrl());

            user.setLanguage(userInfo.getLanguage());
            user.setProvince(userInfo.getProvince());
            user.setSex(userInfo.getGender());
            user.setUnionid(userInfo.getUnionId());

            WechatLogic.Init(user);

            return Response.ok(resp);
        }

        throw new TipException("解析失败");

    }
    //endregion

}
