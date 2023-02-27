package com.light.basegert.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * ClassName: dataSourceAndJdbcTemplateConfig
 * Author: 光子
 * Date:  2023/2/27
 * Project baseGert
 **/
@Configuration
public class dataSourceAndJdbcTemplateConfig {

    @Autowired
    DataSource dataSource;


    @Bean
    @Primary
    public JdbcTemplate master(){
        return new  JdbcTemplate(dataSource);
    }
    @Bean("edga")
    public JdbcTemplate edge(){
        return new  JdbcTemplate(((DynamicRoutingDataSource)dataSource).getDataSource("edge"));
    }


}
