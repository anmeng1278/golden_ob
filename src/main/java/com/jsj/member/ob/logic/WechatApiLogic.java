package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.wechatApi.*;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class WechatApiLogic {

    public static WechatApiLogic wechatApiLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        wechatApiLogic = this;
        wechatApiLogic.webconfig = this.webconfig;
    }

    /**
     * 获取AccessToken
     * @param requ
     * @return
     */
    public static GetAccessTokenResp GetAccessToken(GetAccessTokenRequ requ) {

        GetAccessTokenResp resp = new GetAccessTokenResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());

            String sign = WechatApiLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            //设置签名
            String result = HttpUtils.json("http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetAccessToken", JSON.toJSONString(requ));
            resp = JSON.parseObject(result, GetAccessTokenResp.class);

        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }

    /**
     * 获取公众号的 jsapi_ticket
     * @param requ
     * @return
     */
    public static GetJsApiTicketResp GetJsApiTicket(GetJsApiTicketRequ requ) {

        GetJsApiTicketResp resp = new GetJsApiTicketResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("AccessToken",requ.getRequestBody().getAccessToken());

            String sign = WechatApiLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            //设置签名
            String result = HttpUtils.json(wechatApiLogic.webconfig.getJsApiTicketUrl(), JSON.toJSONString(requ));
            resp = JSON.parseObject(result, GetJsApiTicketResp.class);

        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }


    /**
     * 通过 code 换取网页授权 access_token
     * @param requ
     * @return
     */
    public static GetWeChatAccessTokenResp GetWeChatAccessToken(GetWeChatAccessTokenRequ requ) {

        GetWeChatAccessTokenResp resp = new GetWeChatAccessTokenResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("Code",requ.getRequestBody().getCode());

            String sign = WechatApiLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            //设置签名
            String result = HttpUtils.json(wechatApiLogic.webconfig.getWeChatAccessTokenUrl(), JSON.toJSONString(requ));
            resp = JSON.parseObject(result, GetWeChatAccessTokenResp.class);

        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }


    /**
     * 微信公众号下单接口，使用返回参数调起微信客户端支付
     * @param requ
     * @return
     */
    public static GetPayTradeResp GetPayTrade(GetPayTradeRequ requ) {

        GetPayTradeResp resp = new GetPayTradeResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("PlatformAppId",wechatApiLogic.webconfig.getPlatformAppId());
            map.put("PlatformToken", wechatApiLogic.webconfig.getPlatformToken());
            map.put("SourceWay", requ.getRequestBody().getSourceWay());
            map.put("SourceApp", requ.getRequestBody().getSourceApp());
            map.put("PayMethod", requ.getRequestBody().getPayAmount());
            map.put("OutTradeId",requ.getRequestBody().getOutTradeId());
            map.put("PayAmount", requ.getRequestBody().getPayAmount());
            map.put("OpenId", requ.getRequestBody().getOpenId());
            map.put("OrderTimeOut", requ.getRequestBody().getOrderTimeOu());

            String sign = WechatApiLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            //设置签名
            String result = HttpUtils.json(wechatApiLogic.webconfig.getWeChatAccessTokenUrl(), JSON.toJSONString(requ));
            resp = JSON.parseObject(result, GetPayTradeResp.class);

        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }


    /**
     * 签名字符串处理
     * @param map
     * @param secret
     * @return
     */
    private static String GetMd5str(LinkedMap<String, String> map, String secret) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : map.keySet()) {
                sb.append(key + "=").append(map.get(key)).append("&");
            }
            sb.append(secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
