package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.googlecode.protobuf.format.JsonFormat;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.proto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class MemberLogicTest {

    @Test
    public void strictChoiceBack() {

        StrictChoiceBackRequestOuterClass.StrictChoiceBackRequest.Builder requ = StrictChoiceBackRequestOuterClass.StrictChoiceBackRequest.newBuilder();
        requ.setJSJID(1);

        StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse resp = MemberLogic.StrictChoiceBack(requ.build());
        System.out.println(JSON.toJSONString(resp));

    }

    @Test
    public void getCustomerIdByPhone() {

        GetCustomerIdByPhoneRequOuterClass.GetCustomerIdByPhoneRequ.Builder requ = GetCustomerIdByPhoneRequOuterClass.GetCustomerIdByPhoneRequ.newBuilder();
        requ.setPhone("15210860133");

        GetCustomerIdByPhoneRespOuterClass.GetCustomerIdByPhoneResp resp = MemberLogic.GetCustomerIdByPhone(requ.build());
        System.out.println(resp.toBuilder().toString());
        System.out.println(resp.toString());

    }

    @Test
    public void strictChoiceCancel() {
    }

    @Test
    public void strictChoiceLock() {
    }

    @Test
    public void strictChoiceUnLock() {
    }

    @Test
    public void strictChoiceConsume() {

        StrictChoiceConsumeRequestOuterClass.StrictChoiceConsumeRequest.Builder requ = StrictChoiceConsumeRequestOuterClass.StrictChoiceConsumeRequest.newBuilder();

        requ.setJSJID(1);
        requ.setMoney(0.01);
        requ.setOrderType(StrictChoiceOrderTypeOuterClass.StrictChoiceOrderType.StrictChoice);
        requ.setOrderID(1000);
        requ.setRemark("严选商品兑换");
        requ.setProjectID(13006);

        //Map<Descriptors.FieldDescriptor, Object> allFields = requ.build().getAllFields();
        //
        //for (Descriptors.FieldDescriptor key : allFields.keySet()) {
        //    System.out.println("Key = " + key.getName());
        //    System.out.println(allFields.get(key));
        //}
        //
        //System.out.println(requ.build().toString().replaceAll("\n", " "));

        String jsonFormat = JsonFormat.printToString(requ.build());
        System.out.println(jsonFormat);
        //StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse resp = MemberLogic.StrictChoiceConsume(requ.build());
        //
        //System.out.println(resp.toString());

    }

    private static String hexStr2Str(String hexStr) {
        String str = "0123456789abcdef";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Test
    public void strictChoiceSearch() {

        StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.Builder requ = StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.newBuilder();
        //requ.setJSJID(20612968);
        requ.setJSJID(20613259);

        StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse resp = MemberLogic.StrictChoiceSearch(requ.build());
        System.out.println(resp.toBuilder().toString());
        System.out.println(resp.toString());
        System.out.println("余额" + resp.getCurrentStrictChoice());


        System.out.println("***************");
        StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse resp2 = StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse.getDefaultInstance();
        System.out.println(resp2.getBaseResponse());
        System.out.println(resp2.getBaseResponse().getIsSuccess());
        System.out.println(resp2.toBuilder().toString());

        BaseResponseOuterClass.BaseResponse.Builder builder = BaseResponseOuterClass.BaseResponse.getDefaultInstance().toBuilder();
        builder.setErrorMessage("出错了");

        System.out.println(resp2.getBaseResponse());
        System.out.println(resp2.getBaseResponse().getIsSuccess());
        System.out.println(resp2.getBaseResponse().getErrorMessage());
        System.out.println(resp2.toBuilder().toString());
    }


}