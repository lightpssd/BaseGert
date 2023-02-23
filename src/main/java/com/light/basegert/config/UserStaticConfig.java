package com.light.basegert.config;

import com.light.basegert.eneity.SystemUser;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserStaticConfig {
    public static List<SystemUser> users = Arrays.asList(new SystemUser("admin","DUOLAAMENG"), new SystemUser("root", "XXXXXXXX"));

}
