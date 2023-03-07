package com.light.basegert.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.light.basegert.config.SqlConfig;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * ClassName: SqlRedeUtil
 * Author: 光子
 * Date:  2023/3/7
 * Project baseGert
 **/
public class SqlReaderUtil {

    public static String readSql(String filename){
        try {
            Path resolve = SqlConfig.path.resolve(filename + ".sql");
            return FileUtil.readString(resolve.toFile(), Charset.defaultCharset());
        }catch (IORuntimeException e){
            System.out.println(e);
            return "";
        }
    }

}
