package com.light.basegert.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Slf4j
public class JdbcUtils {

    public static <T> T jdbcRunning(String name,Function<JdbcTemplate, T> apply)  {
        Object bean = SpringContextHolder.getBean(name);
        if (bean instanceof JdbcTemplate){
            return apply.apply((JdbcTemplate)bean);
        }
        log.error("没有名称为"+name+"的数据源");
        throw  new NoSuchBeanDefinitionException("name");
    }
    public static <T> T jdbcRunning(Function<JdbcTemplate, T> apply)  {
        return jdbcRunning("master",apply);
    }
    public static List<Map<String,Object>> jdbcQuerySql(String dataName, String sql) throws SQLSyntaxErrorException {
        if (StrUtil.isEmpty(sql)){
            throw new SQLSyntaxErrorException("sql空语句");
        }
        if (!sql.trim().startsWith("select")){
            throw new SQLSyntaxErrorException("非查询语句");
        }
        log.info("运行sql语句:"+sql);
        return jdbcRunning(dataName,jdbcTemplate -> jdbcTemplate.queryForList(sql));
    }

    public static List<Map<String,Object>> jdbcQuerySql(String dataName, String sql,String ...args) throws SQLSyntaxErrorException {
        if (StrUtil.isEmpty(sql)){
            throw new SQLSyntaxErrorException("sql空语句");
        }
        if (!sql.trim().startsWith("select")){
            throw new SQLSyntaxErrorException("非查询语句");
        }
        log.info("运行sql语句:"+sql);
        return jdbcRunning(dataName,jdbcTemplate -> jdbcTemplate.queryForList(sql, (Object[]) args));

    }

    public static List<Map<String,Object>> jdbcQuerySql(String sql) throws SQLSyntaxErrorException {
        return jdbcQuerySql("master",sql);
    }


}
