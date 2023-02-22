package com.light.basegert.utils

import com.alibaba.druid.util.FnvHash.Constants.T
import org.slf4j.LoggerFactory


inline fun <reified T:Any> T.getLogger() = lazy {  LoggerFactory.getLogger(T::class.java)}
