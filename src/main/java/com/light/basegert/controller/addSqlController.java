package com.light.basegert.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.light.basegert.utils.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sql")
public class addSqlController {
    @SaIgnore
    @GetMapping("addsql")
    public String addSql(){

        return "system/addsql";
    }
    @SaIgnore
    @GetMapping("runsql")
    @ResponseBody
    public SaResult addSql(String sql){
        try {
            List<Map<String, Object>> maps = JdbcUtils.jdbcQuerySql(sql);
            return new SaResult(SaResult.CODE_SUCCESS,"查找成功",maps);
        }catch (SQLException e){
           return SaResult.error(e.getMessage());
        }

    }
}
