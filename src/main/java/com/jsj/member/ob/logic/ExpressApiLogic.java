package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.express.ExpressTypeResp;
import com.jsj.member.ob.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class ExpressApiLogic {

    private ExpressApiLogic expressApiLogic;

    @PostConstruct
    public void init() {
        expressApiLogic = this;
    }

    /**
     * 获取是哪家快递
     * @param text
     * @return
     */
    public static String GetText(String text){

        ExpressTypeResp resp = new ExpressTypeResp();
        String type = null;
        if(StringUtils.isBlank(text)){
            return null;
        }
        //通过快递单号获得是哪家快递
        String url  = String.format("http://www.kuaidi100.com/autonumber/autoComNum?text="+text);
        try {
            String resultType = HttpUtils.get(url);
            JSONObject typeJsonObject = (JSONObject) JSON.parse(resultType);
            JSONArray typeDataArray = (JSONArray) typeJsonObject.get("auto");
            if(typeDataArray.isEmpty()){
                return null;
            }
            JSONObject typeObject = (JSONObject)typeDataArray.get(0);
            type = typeObject.getString("comCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;

    }

    /**
     * 获取快递物流信息
     * @param requ
     * @return
     * @throws IOException
     */
    public static ExpressResp GetExpress(ExpressRequ requ) throws IOException {
        ExpressResp resp = new ExpressResp();
        String getText = GetText(requ.getText());
        // 通过快递公司及快递单号获取物流信息。
        //Double temp = new Random().nextDouble();
        String url  = String.format("http://www.kuaidi100.com/query?type="+getText+"&postid="+requ.getText()+"&temp="+new Random().nextDouble());
        String expressResult = HttpUtils.get(url);
        resp = JSON.parseObject(expressResult, ExpressResp.class);
        return resp;
    }

}
