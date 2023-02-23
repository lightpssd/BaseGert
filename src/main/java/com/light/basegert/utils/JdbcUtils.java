package com.light.basegert.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.Introspector;
import java.util.function.Function;

public class JdbcUtils {

    public static <T> T jdbcRunning(Function<JdbcTemplate, T> apply, Boolean autoCommit) {
        JdbcTemplate jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
        jdbcTemplate.query()
        return apply.apply(jdbcTemplate);

    }
}
