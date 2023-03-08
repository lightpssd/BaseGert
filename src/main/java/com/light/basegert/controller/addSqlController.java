package com.light.basegert.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.light.basegert.utils.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sql")
@Slf4j
public class addSqlController {

    @GetMapping("addsql")
    public String addSql() {

        return "system/addsql";
    }


    @GetMapping("updatesql")
    public String updateSqlHtml(String id, Model model) {

        Map<String, Object> maps = JdbcUtils.jdbcRunning(jdbcTemplate -> jdbcTemplate.queryForMap("select * from sql_data where id=?", id));

        model.addAttribute("result", maps);
        return "system/updatesql";
    }


    @PostMapping("updatesql")
    @ResponseBody
    public SaResult updateSql(String id, String address, String remark, String sql, String dataName,String param) {
        String regex = "^[a-zA-Z0-9]{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        // 判断是否匹配成功
        if (!matcher.matches()) {
            return SaResult.error("地址不合法");
        }

        Boolean hasAddress = JdbcUtils.jdbcRunning(jdbcTemplate -> {
            Map<String, Object> count = jdbcTemplate.queryForMap("select count(*) as c from sql_data where address=? and id!=?", address,id);
            return (Integer) count.get("c") > 0;
        });
        if (hasAddress) {
            return SaResult.error("地址重复");
        }

        Integer integer = JdbcUtils.jdbcRunning(jdbcTemplate ->
                jdbcTemplate.update(
                        "update  sql_data set dataName=?,sql_string=?,address=?,remark=?,param=? where id=?"
                        , dataName, sql, address, remark,param, id));
        if (integer > 0)
            return SaResult.ok();
        else
            return SaResult.error("更新出错，请稍后再试");
    }


    @GetMapping("showsql")
    public String showSql(Model model) {

        try {
            List<Map<String, Object>> sqls = JdbcUtils.jdbcQuerySql("select * from sql_data");
            sqls.forEach(r->{
                String param = (String) r.getOrDefault("param","");
                if (StrUtil.isEmpty(param))
                    r.put("param","");
                else
                    r.put("param","?"+StrUtil.join("&", Arrays.stream(param.split("\\?")).map(s->s+"={}").collect(Collectors.toList())));
            });
            model.addAttribute("sqls", sqls);
        } catch (SQLSyntaxErrorException e) {
            throw new RuntimeException(e);
        }

        return "system/showSqlList";
    }


    @GetMapping("deletesql")
    public String deleteSql(String id) {

        JdbcUtils.jdbcRunning(jdbcTemplate -> jdbcTemplate.update("delete from sql_data where id=?", id));

        return "redirect:showsql";
    }

    @GetMapping("runsql")
    @ResponseBody
    public SaResult runSql(@RequestParam(defaultValue = "master") String dataName, String sql,String paramdata) {
        String[] split = StrUtil.isEmpty(paramdata)? new String[0] :paramdata.split("\\?");
        if (StrUtil.count(sql,'?')!=split.length){
            return SaResult.error("参数数量错误");
        }
        try {
            List<Map<String, Object>> maps = JdbcUtils.jdbcQuerySql(dataName, sql,split);
            return new SaResult(SaResult.CODE_SUCCESS, "查找成功", maps);
        } catch (SQLException e) {
            return SaResult.error(e.getMessage());
        }
    }


    @PostMapping("savesql")
    @ResponseBody
    public SaResult saveSql(String address, String remark, String sql, String dataName,String param) {

        String regex = "^[a-zA-Z0-9]{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        // 判断是否匹配成功
        if (!matcher.matches()) {
            return SaResult.error("地址不合法");
        }
        if (StrUtil.isEmpty(sql)){
            return SaResult.error("不存在sql语句");
        }
        Boolean hasAddress = JdbcUtils.jdbcRunning(jdbcTemplate -> {
            Map<String, Object> count = jdbcTemplate.queryForMap("select count(*) as c from sql_data where address=?", address);
            return (Integer) count.get("c") > 0;
        });
        if (hasAddress) {
            return SaResult.error("地址重复");
        }
        JdbcUtils.jdbcRunning(jdbcTemplate -> jdbcTemplate.update("insert into sql_data(dataName,sql_string,address,remark,param) values (?,?,?,?,?)", dataName, sql, address, remark,param));
        return SaResult.ok();
    }


    @GetMapping("randomString")
    @ResponseBody
    public SaResult randomString() {
        String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return SaResult.data(NanoId.randomNanoId(RandomUtil.getRandom(), s.toCharArray(), RandomUtil.randomInt(6, 18)));
    }

}
