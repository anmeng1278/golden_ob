package com.jsj.member.ob.logic;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.enums.WechatRelationType;
import com.jsj.member.ob.exception.TipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigLogic extends BaseLogic {

    public static ConfigLogic configLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        configLogic = this;
    }

    @Autowired
    Webconfig webconfig;

    public static Webconfig GetWebConfig() {
        return configLogic.webconfig;
    }

    //region (public) 获取支付的AppId GetPlatformAppId

    /**
     * 获取支付的AppId
     *
     * @param wechatRelationType
     * @return
     */
    public static String GetPlatformAppId(WechatRelationType wechatRelationType) {
        String platformAppId = configLogic.webconfig.getPlatformAppId();
        if (wechatRelationType == null) {
            return platformAppId;
        }
        switch (wechatRelationType) {
            case AWKMINI:
                return String.format("%s-mini", platformAppId);
            default:
                throw new TipException("未知的枚举类型");
        }
    }
    //endregion

    //region (public) 获取支付的AppToken GetPlatformToken

    /**
     * 获取支付的AppToken
     *
     * @param wechatRelationType
     * @return
     */
    public static String GetPlatformToken(WechatRelationType wechatRelationType) {
        String platformToken = configLogic.webconfig.getPlatformToken();
        if (wechatRelationType == null) {
            return platformToken;
        }
        switch (wechatRelationType) {
            case AWKMINI:
                return String.format("%s-mini", platformToken);
            default:
                throw new TipException("未知的枚举类型");
        }
    }
    //endregion

}
