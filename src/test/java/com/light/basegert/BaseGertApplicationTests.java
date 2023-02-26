package com.light.basegert;

import com.light.basegert.utils.SpringContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLSyntaxErrorException;

@SpringBootTest
class BaseGertApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    String sql="select * from qwed";
    @Test
    void contextLoads() throws SQLSyntaxErrorException {
        JdbcTemplate bean = SpringContextHolder.getBean(JdbcTemplate.class);
        bean.queryForList(sql);
    }

}
