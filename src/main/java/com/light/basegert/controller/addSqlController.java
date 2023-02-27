package com.light.basegert.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.RandomUtil;
import com.light.basegert.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/sql")
@Slf4j
public class addSqlController {
    @SaIgnore
    @GetMapping("addsql")
    public String addSql(){

        return "system/addsql";
    }

    @SaIgnore
    @GetMapping("addsql")
    public String showSql(){

        return "system/showSqlList";
    }
    @SaIgnore
    @GetMapping("runsql")
    @ResponseBody
    public SaResult runSql(@RequestParam(defaultValue = "master") String dataName, String sql){

        try {
            List<Map<String, Object>> maps = JdbcUtils.jdbcQuerySql(dataName,sql);
            return new SaResult(SaResult.CODE_SUCCESS,"查找成功",maps);
        }catch (SQLException e){
           return SaResult.error(e.getMessage());
        }

    }

    @SaIgnore
    @PostMapping("savesql")
    @ResponseBody
    public SaResult saveSql(String address,String remark,String sql,String dataName) {

        String regex = "^[a-zA-Z0-9]{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        // 判断是否匹配成功
        if (!matcher.matches()) {
            return SaResult.error("地址不合法");
        }
        Boolean hasAddress = JdbcUtils.jdbcRunning(jdbcTemplate -> {
            Map<String, Object> count = jdbcTemplate.queryForMap("select count(*) as c from sql_data where address=?", address);
            return (Integer) count.get("c") > 0;
        });
        if (hasAddress){
            return SaResult.error("地址重复");
        }
        JdbcUtils.jdbcRunning(jdbcTemplate -> jdbcTemplate.update("insert into sql_data(dataName,sql_string,address,remark) values (?,?,?,?)", dataName, sql, address, remark));
        return SaResult.ok();
    }

    @SaIgnore
    @GetMapping("randomString")
    @ResponseBody
    public SaResult randomString(){
        String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return SaResult.data(NanoId.randomNanoId(RandomUtil.getRandom(),s.toCharArray(),RandomUtil.randomInt(6,18)));
    }

}
