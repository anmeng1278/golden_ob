package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.proto.*;
import com.jsj.member.ob.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));

        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));

        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));

        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));
        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));
        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));
        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
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
            memberLogic.logger.info(String.format("%s %s %s", url, requ.toString(), resp.toString()));

        } catch (Exception ex) {
            memberLogic.logger.error("会员组接口失败：" + JSON.toJSONString(ex));
            ex.printStackTrace();
        }
        return resp;

    }
    //endregion

}

