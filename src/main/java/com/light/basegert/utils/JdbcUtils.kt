package com.light.basegert.utils

import com.alibaba.druid.stat.JdbcStatManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.thymeleaf.spring6.context.SpringContextUtils
import javax.sql.DataSource

class JdbcUtils {
    val log by getLogger()
    fun <T> JdbcRunning(block:JdbcTemplate.()->T): T? {
        val jdbcTemplate:JdbcTemplate=SpringContextHolder.getBean(JdbcTemplate::class.java)
        return kotlin.runCatching {
            jdbcTemplate.block()
        }.onFailure {

        }.getOrNull()
    }
}
