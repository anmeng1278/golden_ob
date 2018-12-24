package com.jsj.member.ob.logic;

import com.jsj.member.ob.config.Webconfig;
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

}
