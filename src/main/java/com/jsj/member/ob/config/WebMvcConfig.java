package com.jsj.member.ob.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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
            registry.addResourceHandler(String.format("%s/static/**", webconfig.getVirtualPath())).addResourceLocations("classpath:/static/");
        }
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        registry.addResourceHandler(String.format("%s/swagger/**", webconfig.getVirtualPath())).addResourceLocations("classpath:/META-INF/resources/");

        super.addResourceHandlers(registry);

        //registry.addResourceHandler("**/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(String.format("%s/swagger/v2/api-docs", webconfig.getVirtualPath()), "/v2/api-docs");
        registry.addRedirectViewController(String.format("%s/swagger/swagger-resources/configuration/ui", webconfig.getVirtualPath()), "/swagger-resources/configuration/ui");
        registry.addRedirectViewController(String.format("%s/swagger/swagger-resources/configuration/security", webconfig.getVirtualPath()), "/swagger-resources/configuration/security");
        registry.addRedirectViewController(String.format("%s/swagger/swagger-resources", webconfig.getVirtualPath()), "/swagger-resources");
        //registry.addRedirectViewController("/ot", "/ot/swagger-ui.html");
        //registry.addRedirectViewController("/ot/", "/ot/swagger-ui.html");
    }


}