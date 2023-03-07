package com.light.basegert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ClassName: SqlConfig
 * Author: 光子
 * Date:  2023/3/7
 * Project baseGert
 **/
@Configuration
@ConfigurationProperties(prefix = "sql-file")
public class SqlConfig {
    public static Path path;

    public void setPath(String path) throws IOException {
        SqlConfig.path= Paths.get(path);
        if (!SqlConfig.path.toFile().exists()){
            Files.createDirectories(SqlConfig.path);
        }
    }
}
