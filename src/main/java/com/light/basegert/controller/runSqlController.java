package com.light.basegert.controller;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.light.basegert.utils.JdbcUtils;
import com.light.basegert.utils.SqlReaderUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: runSqlController
 * Author: 光子
 * Date:  2023/2/28
 * Project baseGert
 **/
@RestController
@RequestMapping("openapi")
public class runSqlController {

    private static final LoadingCache<String, String> sqlcache =
            CacheBuilder.newBuilder()
                    // 初始化容量
                    .initialCapacity(4)
                    // 缓存池大小，在缓存数量到达该大小时， Guava开始回收旧的数据
                    .maximumSize(100)
                    // 设置时间对象没有被读/写访问则对象从内存中删除(在另外的线程里面不定期维护)
                    .expireAfterAccess(5, TimeUnit.MINUTES)
                    // 设置缓存在写入之后 设定时间 后失效
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    // 数据被移除时的监听器, 缓存项被移除时会触发执行
                    .removalListener((RemovalListener<String, String>) rn -> {
                        System.out.println(String.format("数据key:%s value:%s 因为%s被移除了", rn.getKey(), rn.getValue(),
                                rn.getCause().name()));
                    })
                    // 开启Guava Cache的统计功能
                    .recordStats()
                    // 数据写入后被多久刷新一次
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    // 数据并发级别
                    .concurrencyLevel(16)
                    // 当缓存中没有数据时的数据加载器
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) {
                            return "";
                        }
                    });
    @GetMapping("{address}")
    public Object run(@PathVariable("address") String address, @RequestParam Map<String, String> args) throws SQLSyntaxErrorException {

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
        String param = (String) result.get("param");
        if (StrUtil.isEmpty(param)&&args.isEmpty())
            return JdbcUtils.jdbcQuerySql(dataName,sqlStr);


        HashSet<String> split = CollUtil.set(true, (param.split("\\?")));

        if (!args.keySet().containsAll(split)||split.size()!=args.size()){
            return SaResult.error("参数不匹配!");
        }
        String[] as = split.stream().map(args::get).toArray(String[]::new);
        System.out.println(Arrays.toString(as));
        return JdbcUtils.jdbcQuerySql(dataName,sqlStr,as);
    }
    @GetMapping("/{dataName}/{address}")
    public Object runSql(@PathVariable("address") String address,@PathVariable("dataName") String dataName) throws SQLSyntaxErrorException, ExecutionException {

        String sql = sqlcache.get(address);
        if (StrUtil.isEmpty(sql)){
            sql = SqlReaderUtil.readSql(address);
            if (StrUtil.isEmpty(sql)){
                return SaResult.error("sql语句无效");
            }
            sqlcache.put(address,sql);
        }
        if (StrUtil.isEmpty(dataName)){
            return SaResult.error("数据库名无效");
        }
        return JdbcUtils.jdbcQuerySql(dataName,sql);
    }
}
