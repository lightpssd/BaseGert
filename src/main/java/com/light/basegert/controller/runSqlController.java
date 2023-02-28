package com.light.basegert.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.light.basegert.utils.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLSyntaxErrorException;
import java.util.Map;

/**
 * ClassName: runSqlController
 * Author: 光子
 * Date:  2023/2/28
 * Project baseGert
 **/
@RestController
@RequestMapping("openapi")
public class runSqlController {

    @GetMapping("/{address}")
    public Object run(@PathVariable("address") String address) throws SQLSyntaxErrorException {
        Map<String, Object> result = JdbcUtils.jdbcRunning(jdbcTemplate -> {
            try {
                return jdbcTemplate.queryForMap("select * from sql_data where address=?", address);
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        });
        if (result==null||result.isEmpty()){
            return SaResult.error("没有对应的sql语句");
        }
        Object sql=result.get("sql_string");
        String sqlStr=null;
        if (!(sql instanceof String)){
            return SaResult.error("没有对应的sql语句");
        }
        sqlStr=(String)sql;
        if (StrUtil.isEmpty(sqlStr)){
            return SaResult.error("sql语句无效");
        }

        String dataName=(String)result.get("dataName");
        if (StrUtil.isEmpty(dataName)){
            return SaResult.error("数据库名无效");
        }
        return JdbcUtils.jdbcQuerySql(dataName,sqlStr);
    }
}
