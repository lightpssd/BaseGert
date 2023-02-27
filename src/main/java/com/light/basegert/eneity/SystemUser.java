package com.light.basegert.eneity;

/**
 * ClassName: SystemUser
 * Author: 光子
 * Date:  2023/2/27
 * Project baseGert
 **/
public class SystemUser {
    private String username;
    private String password;

    public SystemUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
