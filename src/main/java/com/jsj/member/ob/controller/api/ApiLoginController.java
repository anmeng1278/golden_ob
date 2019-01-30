package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.mini.MiniUserInfo;
import com.jsj.member.ob.dto.mini.RegisterRequ;
import com.jsj.member.ob.dto.mini.RegisterResp;
import com.jsj.member.ob.logic.WechatLogic;
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
        MiniUserInfo userInfo = JSON.parseObject(requ.getRequestBody().getRawData(), MiniUserInfo.class);

        //数据插入数据库
        User user = new User();

        user.setNickname(userInfo.getNickName());
        user.setOpenid(requ.getRequestBody().getOpenId());
        user.setCity(userInfo.getCity());
        user.setCountry(userInfo.getCountry());
        user.setHeadimgurl(userInfo.getAvatarUrl());

        user.setLanguage(userInfo.getLanguage());
        user.setProvince(userInfo.getProvince());
        user.setSex(userInfo.getGender());

        WechatLogic.Init(user);

        if (requ.getRequestBody().getJsjId() > 0) {
            WechatLogic.BindJSJId(requ.getRequestBody().getOpenId(), requ.getRequestBody().getJsjId());
        }

        resp.setOpenId(requ.getRequestBody().getOpenId());
        return Response.ok(resp);


    }
    //endregion

}
