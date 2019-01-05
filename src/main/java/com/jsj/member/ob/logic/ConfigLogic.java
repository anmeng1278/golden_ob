package com.jsj.member.ob.logic;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.enums.SourceType;
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
     * @param sourceType
     * @return
     */
    public static String GetPlatformAppId(SourceType sourceType) {
        String platformAppId = configLogic.webconfig.getPlatformAppId();
        if (sourceType == null) {
            return platformAppId;
        }
        switch (sourceType) {
            case AWKTC:
                return platformAppId;
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
     * @param sourceType
     * @return
     */
    public static String GetPlatformToken(SourceType sourceType) {
        String platformToken = configLogic.webconfig.getPlatformToken();
        if (sourceType == null) {
            return platformToken;
        }
        switch (sourceType) {
            case AWKTC:
                return platformToken;
            case AWKMINI:
                return String.format("%s-mini", platformToken);
            default:
                throw new TipException("未知的枚举类型");
        }
    }
    //endregion

}
