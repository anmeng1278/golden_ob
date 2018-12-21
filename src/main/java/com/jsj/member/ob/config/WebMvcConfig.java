package com.jsj.member.ob.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源映射
 *
 * @author sam
 * @since 2017/7/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    Webconfig webconfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if (!StringUtils.isEmpty(webconfig.getVirtualPath())) {
            registry.addResourceHandler(String.format("{0}/**", webconfig.getVirtualPath())).addResourceLocations("classpath:/static/");
        }

        registry.addResourceHandler(String.format("/**", webconfig.getVirtualPath())).addResourceLocations("classpath:/static/");
    }
}