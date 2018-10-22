package com.jsj.member.ob.config;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@MapperScan("com.jsj.member.ob.dao*")
<<<<<<< HEAD
@ImportResource(locations={"classpath*:applicationContext.xml"})
=======
@ImportResource(locations={"classpath:applicationContext.xml"})
>>>>>>> fcc265340f5094d58d2d861cd745ec9311d6b2d7
@EnableTransactionManagement
public class MybatisPlusConfig {


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 在代码中配置MybatisPlus替换掉application.yml中的配置
     * @return
     */
    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        //主键类型 0:数据库ID自增, 1:用户输入ID,2:全局唯一ID (数字类型唯一ID), 3:全局唯一ID UUID
        conf.setIdType(0);
        //字段策略(拼接sql时用于判断属性值是否拼接) 0:忽略判断,1:非NULL判断,2:非空判断
        conf.setFieldStrategy(2);
        //驼峰下划线转换含查询column及返回column(column下划线命名create_time，返回java实体是驼峰命名createTime，开启后自动转换否则保留原样)
        conf.setDbColumnUnderline(true);
        //是否动态刷新mapper
        conf.setRefresh(true);
        return conf;
    }

}
