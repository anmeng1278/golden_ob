package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.api.express.ExpressBirdRequ;
import com.jsj.member.ob.dto.api.express.ExpressBirdResp;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Random;

@Component
public class ExpressApiLogic extends BaseLogic {

    private static ExpressApiLogic expressApiLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        expressApiLogic = this;
    }


    /**
     * 快递100获取快递物流信息
     *
     * @param requ
     * @return
     * @throws IOException
     */
    public static ExpressResp GetExpressHundred(ExpressRequ requ) throws IOException {
        if (StringUtils.isBlank(requ.getText())) {
            throw new TipException("快递单号不能为空！");
        }

        String getText = GetText(requ.getText());

        // 通过快递公司及快递单号获取物流信息。
        //TODO 格式化这里需要修改
        String url = String.format("http://www.kuaidi100.com/query?type=" + getText + "&postid=" + requ.getText() + "&temp=" + new Random().nextDouble());

        String expressResult = HttpUtils.get(url);
        ExpressResp resp = JSON.parseObject(expressResult, ExpressResp.class);
        return resp;
    }


    /**
     * 快递鸟获取快递物流信息
     *
     * @param
     * @return
     * @throws IOException
     */
    public static ExpressBirdResp GetExpressBird(ExpressBirdRequ requ) throws Exception {

        String requestData = requ.toString();

        LinkedMap<String, String> map = new LinkedMap<>();

        map.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        map.put("EBusinessID", expressApiLogic.webconfig.getEBusinessID());
        map.put("RequestType", "1002");

        String str = Md5Utils.MD5(requestData + expressApiLogic.webconfig.getAppKey());
        String dataSign = Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));

        map.put("DataSign", URLEncoder.encode((dataSign), "UTF-8"));

        map.put("DataType", "2");

        String form = HttpUtils.form(expressApiLogic.webconfig.getReqURL(), map);

        ExpressBirdResp resp = JSON.parseObject(form, ExpressBirdResp.class);

        return resp;
    }

    /**
     * 快递100获取是哪家快递
     *
     * @param text
     * @return
     */
    public static String GetText(String text) throws IOException {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //通过快递单号获得是哪家快递
        String url = String.format("http://www.kuaidi100.com/autonumber/autoComNum?text=" + text);

        String resultType = HttpUtils.get(url);

        JSONObject typeJsonObject = (JSONObject) JSON.parse(resultType);
        JSONArray typeDataArray = (JSONArray) typeJsonObject.get("auto");
        if (typeDataArray.isEmpty()) {
            return null;
        }
        JSONObject typeObject = (JSONObject) typeDataArray.get(0);
        String type = typeObject.getString("comCode");
        return type;
    }

}
