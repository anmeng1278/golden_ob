package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.proto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

        StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse resp = MemberLogic.StrictChoiceConsume(requ.build());

        System.out.println(resp.toString());

    }

    @Test
    public void strictChoiceSearch() {

        StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.Builder requ = StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.newBuilder();
        requ.setJSJID(20612968);

        StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse resp = MemberLogic.StrictChoiceSearch(requ.build());
        System.out.println(resp.toBuilder().toString());
        System.out.println(resp.toString());
        System.out.println(resp.getCurrentStrictChoice());


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