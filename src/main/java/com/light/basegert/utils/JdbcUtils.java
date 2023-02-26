package com.light.basegert.utils;

import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JdbcUtils {

    public static <T> T jdbcRunning(Function<JdbcTemplate, T> apply) {

        JdbcTemplate jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
        return apply.apply(jdbcTemplate);

    }


    public static List<Map<String,Object>> jdbcQuerySql(String sql) throws SQLSyntaxErrorException {
        if (StrUtil.isEmpty(sql)){
            throw new SQLSyntaxErrorException("sql空语句");
        }
        if (!sql.trim().startsWith("select")){
            throw new SQLSyntaxErrorException("非查询语句");
        }
        return jdbcRunning(jdbcTemplate -> jdbcTemplate.queryForList(sql));
    }


}
