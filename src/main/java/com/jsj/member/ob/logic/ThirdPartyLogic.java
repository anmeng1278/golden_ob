package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.thirdParty.*;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class ThirdPartyLogic extends BaseLogic {


    private final Logger logger = LoggerFactory.getLogger(ThirdPartyLogic.class);


    public static ThirdPartyLogic thirdPartyLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        thirdPartyLogic = this;
    }

    //region (public) 获取AccessToken GetAccessToken

    /**
     * 获取AccessToken
     *
     * @param requ
     * @return
     */
    public static GetAccessTokenResp GetAccessToken(GetAccessTokenRequ requ) {

        GetAccessTokenResp resp = new GetAccessTokenResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());

            String sign = thirdPartyLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            String json = JSON.toJSONString(requ);
            String url = thirdPartyLogic.webconfig.getAccessTokenUrl();

            //设置签名
            String result = HttpUtils.json(url, json);
            resp = JSON.parseObject(result, GetAccessTokenResp.class);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            if (!resp.getResponseHead().getCode().equals("0000")) {
                throw new TipException(resp.getResponseHead().getMessage());
            }

        } catch (TipException ex) {
            throw ex;
        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 获取开发环境token GetAccessTokenDev

    /**
     * 获取开发环境token
     *
     * @param requ
     * @return
     */
    public static GetAccessTokenResp GetAccessTokenDev(GetAccessTokenRequ requ) {

        GetAccessTokenResp resp = new GetAccessTokenResp();

        GetAccessTokenResp.ResponseBody responseBody = resp.new ResponseBody();
        resp.setResponseBody(responseBody);

        String url = "http://172.16.5.63:9999/api/accesstoken?timestamp=%d&sign=%s";
        int timestamp = DateUtils.getCurrentUnixTime();
        String signKey = "jsjwechat*$(@^^^^)";

        String sign = Md5Utils.MD5(timestamp + "" + signKey);
        sign = sign.toLowerCase();

        url = String.format(url, timestamp, sign);

        try {

            String s = HttpUtils.get(url);
            JSONObject jsonObject = JSON.parseObject(s);

            Object access_token = jsonObject.getInnerMap().get("access_token");
            resp.getResponseBody().setAccessToken(access_token.toString());

            ResponseHead responseHead = new ResponseHead();
            responseHead.setCode("0000");
            responseHead.setMessage("请求成功");

            resp.setResponseHead(responseHead);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }
    //endregion

    //region (public) 获取公众号的 GetJsApiTicket

    /**
     * 获取公众号的 jsapi_ticket
     *
     * @param requ
     * @return
     */
    public static GetJsApiTicketResp GetJsApiTicket(GetJsApiTicketRequ requ) {

        GetJsApiTicketResp resp = new GetJsApiTicketResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("AccessToken", requ.getRequestBody().getAccessToken());

            String sign = thirdPartyLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            String url = thirdPartyLogic.webconfig.getJsApiTicketUrl();
            String json = JSON.toJSONString(requ);

            //设置签名
            String result = HttpUtils.json(url, json);
            resp = JSON.parseObject(result, GetJsApiTicketResp.class);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            if (!resp.getResponseHead().getCode().equals("0000")) {
                throw new TipException(resp.getResponseHead().getMessage());
            }

        } catch (TipException ex) {
            throw ex;
        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 通过 code 换取网页授权 GetWeChatAccessToken

    /**
     * 通过 code 换取网页授权 access_token
     *
     * @param requ
     * @return
     */
    public static GetWeChatAccessTokenResp GetWeChatAccessToken(GetWeChatAccessTokenRequ requ) {

        GetWeChatAccessTokenResp resp = new GetWeChatAccessTokenResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("Code", requ.getRequestBody().getCode());

            String sign = thirdPartyLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            String url = thirdPartyLogic.webconfig.getWeChatAccessTokenUrl();
            String json = JSON.toJSONString(requ);

            //设置签名
            String result = HttpUtils.json(url, json);
            resp = JSON.parseObject(result, GetWeChatAccessTokenResp.class);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            if (!resp.getResponseHead().getCode().equals("0000")) {
                throw new TipException(resp.getResponseHead().getMessage());
            }

        } catch (TipException ex) {
            throw ex;
        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 开卡 CreateGoldenCard

    /**
     * 开卡
     *
     * @param requ
     * @return
     */
    public static CreateGoldenCardResp CreateGoldenCard(CreateGoldenCardRequ requ) {

        CreateGoldenCardResp resp = new CreateGoldenCardResp();

        try {

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("CardType", requ.getRequestBody().getCardType() + "");
            map.put("MemberMobile", requ.getRequestBody().getMemberMobile());
            map.put("MemberName", requ.getRequestBody().getMemberName());
            map.put("MemberIdNumber", requ.getRequestBody().getMemberIdNumber());
            map.put("MemberIDType", requ.getRequestBody().getMemberIDType());
            map.put("SalePrice", requ.getRequestBody().getSalePrice());

            String sign = thirdPartyLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            System.out.println("签名前：" + sign);
            sign = Md5Utils.MD5(sign);
            System.out.println("签名后：" + sign);
            requ.getRequestHead().setSign(sign);

            String url = thirdPartyLogic.webconfig.getOpenCardUrl();
            String json = JSON.toJSONString(requ);

            //设置签名
            String result = HttpUtils.json(url, json);
            resp = JSON.parseObject(result, CreateGoldenCardResp.class);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            if (!resp.getResponseHead().getCode().equals("0000")) {
                throw new TipException(resp.getResponseHead().getMessage());
            }

        } catch (TipException ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 微信公众号下单接口，使用返回参数调起微信客户端支付 GetPayTrade

    /**
     * 微信公众号下单接口，使用返回参数调起微信客户端支付
     *
     * @param requ
     * @return
     */
    public static GetPayTradeResp GetPayTrade(GetPayTradeRequ requ) {

        GetPayTradeResp resp = new GetPayTradeResp();

        try {

            requ.getRequestHead().setTimeStamp(DateUtils.getCurrentUnixTime() + "");
            requ.getRequestHead().setSourceFrom("KTGJ");
            requ.getRequestBody().setSourceWay("20");
            requ.getRequestBody().setSourceApp("600");
            requ.getRequestBody().setPayMethod("22");

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());
            map.put("PlatformAppId", thirdPartyLogic.webconfig.getPlatformAppId());
            map.put("PlatformToken", thirdPartyLogic.webconfig.getPlatformToken());
            map.put("SourceWay", requ.getRequestBody().getSourceWay());
            map.put("SourceApp", requ.getRequestBody().getSourceApp());
            map.put("PayMethod", requ.getRequestBody().getPayMethod());
            map.put("OutTradeId", requ.getRequestBody().getOutTradeId());
            map.put("PayAmount", requ.getRequestBody().getPayAmount());
            map.put("OpenId", requ.getRequestBody().getOpenId());
            map.put("OrderTimeOut", requ.getRequestBody().getOrderTimeOut());

            requ.getRequestBody().setPlatformAppId(thirdPartyLogic.webconfig.getPlatformAppId());
            requ.getRequestBody().setPlatformToken(thirdPartyLogic.webconfig.getPlatformToken());

            String sign = thirdPartyLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            String url = thirdPartyLogic.webconfig.getPayTradeUrl();
            String json = JSON.toJSONString(requ);

            //设置签名
            String result = HttpUtils.json(url, json);
            resp = JSON.parseObject(result, GetPayTradeResp.class);


            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            if (!resp.getResponseHead().getCode().equals("0000")) {
                throw new TipException(resp.getResponseHead().getMessage());
            }

            resp.getResponseBody().Parse();

            /*
            {
                "ResponseBody": {
                    "ResponseText": "{\"appId\":\"wx555b169abb4d62ce\",\"timeStamp\":\"1544802746\",\"nonceStr\":\"1m40kqqdjzrbtx7yl4vxymroeerxkj84\",\"package\":\"prepay_id=wx141552270715285f9854d2e10606969281\",\"signType\":\"MD5\",\"paySign\":\"13CA39013606BA1062BE1E1517B6EEC1\"}"
                },
                "ResponseHead": {
                    "Code": "0000",
                    "Message": "SUCCESS"
                }
            }
             */

        } catch (TipException ex) {
            throw ex;
        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 发送短信 SendSms

    /**
     * 发送短信
     *
     * @param smsDto
     */
    public static void SendSms(SmsDto smsDto) {

        if (smsDto == null) return;
        if (StringUtils.isBlank(smsDto.getMobile())) {
            return;
        }
        if (StringUtils.isBlank(smsDto.getContents())) {
            return;
        }
        try {

            //空铁管家
            smsDto.setSendCompany(2);
            smsDto.setSmsSouce(10);

            String url = "http://sms.jsjinfo.cn/Sms/SendSms.aspx";
            //String url = "http://localhost/sms/Sms/SendSms.aspx";
            Map<String, Object> map = new HashMap<>();
            map.put("SmsDto", smsDto);

            String json = JSON.toJSONString(map);
            //System.out.println(body);
            String result = HttpUtils.json(url, json);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

            //System.out.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region (public) 发送微信模板消息 SendWxTemplate

    /**
     * 发送微信模板消息
     */
    public static void SendWxTemplate(TemplateDto templateDto) {

        if (templateDto == null) return;
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

        try {

            GetAccessTokenRequ requ = new GetAccessTokenRequ();
            GetAccessTokenResp resp;

            if (SpringContextUtils.getActiveProfile().equals("dev")) {
                resp = ThirdPartyLogic.GetAccessTokenDev(null);
            } else {
                resp = ThirdPartyLogic.GetAccessToken(requ);
            }

            String accessToken = resp.getResponseBody().getAccessToken();
            //accessToken = "15_4CEnte3ggbs9c7ofUjlUaBgn5dtPdRuT_nPV_ATLALAwjKWlUF9h_TXG0JJcc-n_MO_gh3NIwCd7fskxfQg5Ru5xUoMVadhBc5rfTFtOFVpWISIHgUCwzeOEFGaK1-hUy2QK44YMWBQFRBSDBLOfAEARBE";

            url = String.format(url, accessToken);

            String json = JSON.toJSONString(templateDto);
            String result = HttpUtils.json(url, json);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
    //endregion

    //region (public) 发送客服消息 SendWxMessage

    //{
    //    "touser":"OPENID",
    //        "msgtype":"text",
    //        "text":
    //    {
    //        "content":"Hello World"
    //    }
    //}

    /**
     * 发送客服消息
     *
     * @param openId
     * @param content
     */
    public static void SendWxMessage(String openId, String content) {

        if (StringUtils.isEmpty(openId)) {
            return;
        }
        if (StringUtils.isEmpty(content)) {
            return;
        }

        HashMap<String, Object> body = new HashMap<>();
        body.put("touser", openId);
        body.put("msgtype", "text");
        HashMap<String, Object> txt = new HashMap<>();
        txt.put("content", content);
        body.put("text", txt);

        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

        try {

            GetAccessTokenRequ requ = new GetAccessTokenRequ();
            GetAccessTokenResp resp;

            if (SpringContextUtils.getActiveProfile().equals("dev")) {
                resp = ThirdPartyLogic.GetAccessTokenDev(null);
            } else {
                resp = ThirdPartyLogic.GetAccessToken(requ);
            }

            String accessToken = resp.getResponseBody().getAccessToken();
            //accessToken = "15_4CEnte3ggbs9c7ofUjlUaBgn5dtPdRuT_nPV_ATLALAwjKWlUF9h_TXG0JJcc-n_MO_gh3NIwCd7fskxfQg5Ru5xUoMVadhBc5rfTFtOFVpWISIHgUCwzeOEFGaK1-hUy2QK44YMWBQFRBSDBLOfAEARBE";

            url = String.format(url, accessToken);

            String json = JSON.toJSONString(body);
            String result = HttpUtils.json(url, json);

            thirdPartyLogic.logger.info(String.format("%s %s %s", url, json, result));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region (private) 签名字符串处理 GetMd5str

    /**
     * 签名字符串处理
     *
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
    //endregion

    //region (public) 获取空铁活动码 GetActivityCodes

    /**
     * 获取空铁活动码
     *
     * @param requ
     * @return
     */
    public static GetActivityCodesResp GetActivityCodes(GetActivityCodesRequ requ) {

        GetActivityCodesResp resp = new GetActivityCodesResp();

        if (requ == null) {
            requ = new GetActivityCodesRequ();
        }
        if (requ.getCount() <= 0) {
            requ.setCount(1);
        }
        requ.setPartnerLoginName(thirdPartyLogic.webconfig.getPartnerLoginName());
        requ.setPartnerLoginPassword(thirdPartyLogic.webconfig.getPartnerLoginPassword());

        String url = thirdPartyLogic.webconfig.getActivityUrl();
        String json = JSON.toJSONString(requ);
        String result = HttpUtils.json(url, json, "_GetActivityCode");

        thirdPartyLogic.logger.info(String.format("%s %s %s %s", url, json, result, "_GetActivityCode"));

        if (!StringUtils.isEmpty(result)) {
            resp = JSON.parseObject(result, GetActivityCodesResp.class);
        }

        return resp;
    }
    //endregion

}
