package com.light.basegert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

@SpringBootTest
class BaseGertApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    String sql="select * from qwed";
    @Test
    void contextLoads() throws SQLSyntaxErrorException {
        try {
            try {
                Class.forName("com.ddtek.jdbc.openedge.OpenEdgeDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //conn = DriverManager.getConnection("jdbc:datadirect:openedge://192.9.200.157:6091;databaseName=mesprod", "mfg",null);
            //MES测试库
            //conn = DriverManager.getConnection("jdbc:datadirect:openedge://192.9.200.157:7097;databaseName=mestest", "mfg",null);
            //MES影子库
            Connection mfg = DriverManager.getConnection("jdbc:datadirect:openedge://192.9.200.74:6093;databaseName=mesprod", "mfg", null);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {



    }

}
