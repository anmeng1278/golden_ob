package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.proto.*;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 会员组接口
 */
@Component
public class MemberLogic {

    private static MemberLogic memberLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        memberLogic = this;
    }

    private final Logger logger = LoggerFactory.getLogger(MemberLogic.class);

    //region (public) 严选资产退还 StrictChoiceBack

    /**
     * 严选资产退还
     *
     * @return
     */
    public static StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse StrictChoiceBack(StrictChoiceBackRequestOuterClass.StrictChoiceBackRequest requ) {

        StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse resp = StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceBack");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }

            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产取消 StrictChoiceCancel

    /**
     * 严选资产取消
     *
     * @return
     */
    public static StrictChoiceCancelResponseOuterClass.StrictChoiceCancelResponse StrictChoiceCancel(StrictChoiceCancelRequestOuterClass.StrictChoiceCancelRequest requ) {

        StrictChoiceCancelResponseOuterClass.StrictChoiceCancelResponse resp = StrictChoiceCancelResponseOuterClass.StrictChoiceCancelResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceCancel");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceCancelResponseOuterClass.StrictChoiceCancelResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产锁定 StrictChoiceLock

    /**
     * 严选资产锁定
     *
     * @return
     */
    public static StrictChoiceLockResponseOuterClass.StrictChoiceLockResponse StrictChoiceLock(StrictChoiceLockRequestOuterClass.StrictChoiceLockRequest requ) {

        StrictChoiceLockResponseOuterClass.StrictChoiceLockResponse resp = StrictChoiceLockResponseOuterClass.StrictChoiceLockResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceLock");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceLockResponseOuterClass.StrictChoiceLockResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产解锁 StrictChoiceUnLock

    /**
     * 严选资产解锁
     *
     * @return
     */
    public static StrictChoiceUnLockResponseOuterClass.StrictChoiceUnLockResponse StrictChoiceUnLock(StrictChoiceUnLockRequestOuterClass.StrictChoiceUnLockRequest requ) {

        StrictChoiceUnLockResponseOuterClass.StrictChoiceUnLockResponse resp = StrictChoiceUnLockResponseOuterClass.StrictChoiceUnLockResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceUnLock");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceUnLockResponseOuterClass.StrictChoiceUnLockResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产消费 StrictChoiceConsume

    /**
     * 严选资产消费
     *
     * @return
     */
    public static StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse StrictChoiceConsume(StrictChoiceConsumeRequestOuterClass.StrictChoiceConsumeRequest requ) {

        StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse resp = StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceConsume");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产余额 StrictChoiceSearch

    /**
     * 严选资产余额
     *
     * @return
     */
    public static StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse StrictChoiceSearch(StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest requ) {

        StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse resp = StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceSearch");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion


    //region (public) 开通Plus权益 CreatePlus

    /**
     * 开通Plus权益
     *
     * @return
     */
    public static AddServiceResponseOuterClass.AddServiceResponse CreatePlus(AddServiceRequestOuterClass.AddServiceRequest requ) {

        AddServiceResponseOuterClass.AddServiceResponse resp = AddServiceResponseOuterClass.AddServiceResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("AddService");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = AddServiceResponseOuterClass.AddServiceResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion


    //region (public) 查询会员信息 CustomerInformation

    /**
     * 查询会员信息
     *
     * @return
     */
    public static CustomerInformationResponseOuterClass.CustomerInformationResponse CustomerInformation(CustomerInformationRequestOuterClass.CustomerInformationRequest requ) {

        CustomerInformationResponseOuterClass.CustomerInformationResponse resp = CustomerInformationResponseOuterClass.CustomerInformationResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("GetCustomerInfo");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = CustomerInformationResponseOuterClass.CustomerInformationResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

    //region (public) 严选资产余额 StrictChoiceSearch

    /**
     * 严选资产余额
     *
     * @param jsjId
     * @return
     */
    public static double StrictChoiceSearch(int jsjId) {

        double balance = 0d;
        try {
            StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.Builder requ = StrictChoiceSearchRequestOuterClass.StrictChoiceSearchRequest.newBuilder();
            requ.setJSJID(jsjId);

            StrictChoiceSearchResponseOuterClass.StrictChoiceSearchResponse resp = MemberLogic.StrictChoiceSearch(requ.build());
            if (resp.getBaseResponse().getIsSuccess()) {
                balance = resp.getCurrentStrictChoice();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return balance;

    }
    //endregion

    //region (public) 根据手机号获取会员编号 GetCustomerIdByPhone

    /**
     * 根据手机号获取会员编号
     *
     * @return
     */
    public static GetCustomerIdByPhoneRespOuterClass.GetCustomerIdByPhoneResp GetCustomerIdByPhone(GetCustomerIdByPhoneRequOuterClass.GetCustomerIdByPhoneRequ requ) {

        GetCustomerIdByPhoneRespOuterClass.GetCustomerIdByPhoneResp resp = GetCustomerIdByPhoneRespOuterClass.GetCustomerIdByPhoneResp.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("GetCustomerIdByPhone");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = GetCustomerIdByPhoneRespOuterClass.GetCustomerIdByPhoneResp.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion


    //region (public) 获取礼品券使用明细 StrictChoiceDetail

    /**
     * 获取礼品券使用明细
     *
     * @return
     */
    public static StrictChoiceDetailResponseOuterClass.StrictChoiceDetailResponse StrictChoiceDetail(StrictChoiceDetailRequestOuterClass.StrictChoiceDetailRequest requ) {

        StrictChoiceDetailResponseOuterClass.StrictChoiceDetailResponse resp = StrictChoiceDetailResponseOuterClass.StrictChoiceDetailResponse.getDefaultInstance();

        try {

            String url = memberLogic.webconfig.getMemberApiUrl();
            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("GetStrictChoiceDetail");
            zRequest.setZPack(requ.toByteString());

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceDetailResponseOuterClass.StrictChoiceDetailResponse.parseFrom(zResponse.getZPack());
            } else {
                throw new Exception(zResponse.getExceptionMessage());
            }
            memberLogic.logger.info(String.format("%s %s %s", url, JsonFormat.printToString(requ), JsonFormat.printToString(resp)));

        } catch (Exception ex) {
            memberLogic.logger.error(String.format("会员组接口失败：%s %s", JsonFormat.printToString(requ), JSON.toJSONString(ex)));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion


    //region (public) 获取礼品券使用明细 StrictChoiceDetail

    /**
     * 获取礼品券使用明细
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONArray StrictChoiceDetail(Map<String, Object> map) throws Exception {

        String url = memberLogic.webconfig.getMemberApiUrl();
        String json = HttpUtils.json(url,
                JSON.toJSONString(map),
                "GetStrictChoiceDetail", "4D7D8DEB-5212-4758-808A-32D60F01D689");

        if (!StringUtils.isEmpty(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject baseResponse = jsonObject.getJSONObject("BaseResponse");
            if (!baseResponse.getBoolean("IsSuccess")) {
                String errorMsg = baseResponse.getString("ErrorMessage");
                throw new TipException(errorMsg);
            } else {
                return jsonObject.getJSONArray("StrictChoiceList");
            }
        } else {
            throw new Exception("返回信息为空");
        }


    }
    //endregion


    //region (public) 获取会员资产 GetCustAsset

    /**
     * 获取会员资产
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONObject GetCustAsset(Map<String, Object> map) throws Exception {

        String url = memberLogic.webconfig.getMemberApiUrl2();
        return MemberLogic.GetJMember(url, map, "GetCustAsset");

    }
    //endregion


    /**
     * Plus使用明细
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONObject GetPlusDetail(Map<String, Object> map) throws Exception {

        String url = memberLogic.webconfig.getMemberApiUrl();
        return MemberLogic.GetJMember(url, map, "GetPlusDetail");

    }

    /**
     * 请求会员组接口
     *
     * @param url
     * @param map
     * @param method
     * @return
     * @throws Exception
     */
    private static JSONObject GetJMember(String url, Map<String, Object> map, String method) throws Exception {

        String json = HttpUtils.json(url,
                JSON.toJSONString(map),
                method, "4D7D8DEB-5212-4758-808A-32D60F01D689");

        if (!StringUtils.isEmpty(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject baseResponse = jsonObject.getJSONObject("BaseResponse");
            if(baseResponse == null){
                baseResponse = jsonObject.getJSONObject("baseresponse");
            }
            if (!baseResponse.getBoolean("IsSuccess")) {
                String errorMsg = baseResponse.getString("ErrorMessage");
                throw new TipException(errorMsg);
            } else {
                return jsonObject;
            }
        } else {
            throw new Exception("返回信息为空");
        }


    }

}

