package com.light.basegert.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.light.basegert.config.UserStaticConfig;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorld {


    @GetMapping("/login")
    public String hello() {
        return "system/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public SaResult success(String username, String password) {

        boolean b = UserStaticConfig.INSTANCE.getUsers().stream().anyMatch(r -> r.getUsername().equals(username) && r.getPassword().equals(password));
        if (b){
            StpUtil.login(username);
            return SaResult.ok("登录成功!");
        }

        return SaResult.error("登录失败");
    }

    // 查询登录状态
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
