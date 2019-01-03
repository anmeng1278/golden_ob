package com.jsj.member.ob.logic;

import com.jsj.member.ob.dto.proto.StrictChoiceBackRequestOuterClass;
import com.jsj.member.ob.dto.proto.StrictChoiceBackResponseOuterClass;
import com.jsj.member.ob.dto.proto.ZRequestOuterClass;
import com.jsj.member.ob.dto.proto.ZResponseOuterClass;
import com.jsj.member.ob.utils.HttpUtils;

/**
 * 会员组接口
 */
public class MemberLogic {

    /**
     * 严选资产取消
     *
     * @return
     */
    private static StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse StrictChoiceBack(StrictChoiceBackRequestOuterClass.StrictChoiceBackRequest requ) {

        StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse resp = StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse.getDefaultInstance();

        try {

            String url = "";

            ZRequestOuterClass.ZRequest.Builder zRequest = ZRequestOuterClass.ZRequest.newBuilder();
            zRequest.setMethodName("StrictChoiceBack");

            ZResponseOuterClass.ZResponse.Builder zResponse = HttpUtils.protobuf(zRequest, url);
            if (zResponse.getIsSuccess()) {
                resp = StrictChoiceBackResponseOuterClass.StrictChoiceBackResponse.parseFrom(zResponse.getZPack());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;

    }


}

