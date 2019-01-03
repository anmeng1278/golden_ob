package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.proto.GetCustomerIdByPhoneRequOuterClass;
import com.jsj.member.ob.dto.proto.GetCustomerIdByPhoneRespOuterClass;
import com.jsj.member.ob.dto.proto.StrictChoiceBackRequestOuterClass;
import com.jsj.member.ob.dto.proto.StrictChoiceBackResponseOuterClass;
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
}