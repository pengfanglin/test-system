package com.project.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtils {
  private  static Logger logger=LoggerFactory.getLogger(LogUtils.class);
  /**
   * 打印普通日志
   */
  public static void info(Object log){
    logger.info(log.toString());
  }
  /**
   * 打印错误日志
   */
  public static void error(Object log){
    logger.error(log.toString());
  }
  /**
   * 打印debug日志
   */
  public static void debug(Object log){
    logger.debug(log.toString());
  }
  /**
   * 打印警告日志
   */
  public static void warn(Object log){
    logger.warn(log.toString());
  }
}
