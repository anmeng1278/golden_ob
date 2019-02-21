package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.proto.*;
import com.jsj.member.ob.utils.DateUtils;
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
    public void createPlus() {

        AddServiceRequestOuterClass.AddServiceRequest.Builder requ = AddServiceRequestOuterClass.AddServiceRequest.newBuilder();

        requ.setCustomerId(20613248);
        requ.setOperapersonId(1);
        requ.setOperaTime(DateUtils.getCurrentUnixTime());
        requ.setServiceId(UpgradeServiceOuterClass.UpgradeService.Plus500);

        AddServiceResponseOuterClass.AddServiceResponse resp = MemberLogic.CreatePlus(requ.build());
        System.out.println(resp);
        System.out.println(resp.getMessage());

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


    @Test
    public void customerInformation() {

        CustomerInformationRequestOuterClass.CustomerInformationRequest.Builder requ = CustomerInformationRequestOuterClass.CustomerInformationRequest.newBuilder();
        requ.setJSJID("20613354");


        CustomerInformationResponseOuterClass.CustomerInformationResponse resp = MemberLogic.CustomerInformation(requ.build());
        System.out.println(JsonFormat.printToString(resp));

        //    {
        //        "BaseResponse": {
        //        "IsSuccess": true
        //    },
        //        "list": [
        //        {
        //            "CustomerID": 20613259,
        //                "CustomerName": "ceshi",
        //                "CustomerClass": "分享会员",
        //                "CustomerRegiestTime": "2018-10-09 14:19:33",
        //                "CardID": "8100000001110",
        //                "CardTypeID": 6898,
        //                "CardTypeName": "逸站通",
        //                "CardInvalidDate": "2020-01-07 14:48:48",
        //                "CustPoint": 2000,
        //                "CustVoucher": 130,
        //                "CustTravelFundNum": 2000,
        //                "Contactmeans": "15303368095",
        //                "Certificate": "A88888888",
        //                "CertificateType": "护照",
        //                "CustomerSex": 1,
        //                "CustomerBirth": "1998-10-09 00:00:00",
        //                "GuaranteeNo": "保险待开通",
        //                "CustType": 3,
        //                "CustomerQRCode": "F9GgzK99KhtObCUuiKsEZcYdxNWa8qyduYWp4A6R%2bybVVSs1hEILBsqWJCVzpOrh",
        //                "EnterLowestPoint": "8000",
        //                "EnterLowestVoucher": "150",
        //                "EnterLowestMoney": "150",
        //                "CertificateTypeID": 4,
        //                "IsCustomerPrice": true,
        //                "Token": "m6qMEJSONZDHCcrzbG0ASdc6B-M8Phg7oKIBrSJAqlI=",
        //                "UserMealNum": 12,
        //                "StarType": 4,
        //                "EnterNum": 12,
        //                "CheckeInNum": 12,
        //                "ServerList": [
        //            {
        //                "ServerID": 41,
        //                    "ServerName": "Plus权益包\n",
        //                    "Remark": "该升级产品已将您本人的权益，升级至全国通贵宾厅权益。在权益有效期内，您本人可不限次使用金色世纪自营与合作的机场/高铁站贵宾厅。此权益不包含随行人员进入合作厅的休息权益。"
        //            }
        //        ],
        //            "CardTypeShortName": "逸站通会员",
        //                "VIPOtherName": "",
        //                "StrictChoiceNum": 1279.79
        //        }
        //]
        //    }

    }

    @Test
    public void strictChoiceDetail() {

        StrictChoiceDetailRequestOuterClass.StrictChoiceDetailRequest.Builder requ = StrictChoiceDetailRequestOuterClass.StrictChoiceDetailRequest.newBuilder();
        //requ.setJSJID(20612968);
        requ.setJSJID(20613259);

        StrictChoiceDetailResponseOuterClass.StrictChoiceDetailResponse resp = MemberLogic.StrictChoiceDetail(requ.build());

        System.out.println(JsonFormat.printToString(resp));


    }

    @Test
    public void strictChoiceDetail2() throws Exception {

        JSONObject js = new JSONObject();

        js.put("JSJID", 20613259);

        JSONArray objects = MemberLogic.StrictChoiceDetail(js);
        System.out.println(JSON.toJSONString(objects));


    }

    @Test
    public void getCustAsset() throws Exception {

        JSONObject js = new JSONObject();
        js.put("JSJID", 20613259);

        JSONObject jsonObject = MemberLogic.GetCustAsset(js);
        System.out.println(JSON.toJSONString(jsonObject));

    }

    @Test
    public void getPlusDetail() throws Exception {

        JSONObject js = new JSONObject();
        js.put("JSJID", 20613259);

        JSONObject jsonObject = MemberLogic.GetPlusDetail(js);
        System.out.println(JSON.toJSONString(jsonObject));

    }
}