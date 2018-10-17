package com.jsj.member.ob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "webconfig")
@Component
public class Webconfig {

    private boolean imgServerSwitch;

    private String imgServerURL;

    public boolean isImgServerSwitch() {
        return imgServerSwitch;
    }

    public void setImgServerSwitch(boolean imgServerSwitch) {
        this.imgServerSwitch = imgServerSwitch;
    }

    public String getImgServerURL() {
        return imgServerURL;
    }

    public void setImgServerURL(String imgServerURL) {
        this.imgServerURL = imgServerURL;
    }
}
