package com.project.others;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.project.bean.others.Ajax;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常捕获
 */
@ControllerAdvice
public class DefaultExceptionHandler {
  @ExceptionHandler
  @ResponseBody
  public Ajax handleException(Exception e) {
    //打印堆栈信息
    e.printStackTrace();
    Ajax ajax = new Ajax();
    //提取错误信息
    String error;
    System.out.println(e.getClass());
    if(e instanceof DataIntegrityViolationException&&e.getCause().getCause() instanceof MysqlDataTruncation){
      //sql异常处理
      DataIntegrityViolationException exception= (DataIntegrityViolationException) e;
      MysqlDataTruncation mysqlDataTruncation= (MysqlDataTruncation) exception.getCause();
      //获取sql状态码
      String sqlState=mysqlDataTruncation.getSQLState();
      if("22001".equals(sqlState)){
        error="长度超出";
      }else{
        error="参数非法";
      }
    }else if (e instanceof BindException) {
      //前端参数传递有误的异常处理(比如Bean的字段类型是Integer前端传的却是非Integer类型)
      BindException bindException = (BindException) e;
      //获取异常内容
      FieldError fieldError = bindException.getFieldError();
      if (fieldError != null) {
        //发生异常的字段的类信息
        Class clazz = bindException.getFieldType(fieldError.getField());
        String hopeType = "合法类型";
        if (clazz != null) {
          switch (clazz.getSimpleName()) {
            case "Integer":
            case "int":
            case "Long":
            case "long":
              hopeType = "整数";
              break;
            case "Boolean":
            case "boolean":
              hopeType = "布尔类型";
              break;
            case "Double":
            case "double":
            case "Float":
            case "float":
              hopeType = "小数";
              break;
            case "Date":
              hopeType="日期";
              break;
            default:
              hopeType = clazz.getSimpleName();
              break;
          }
        }
        //用户输入的内容
        Object object = fieldError.getRejectedValue();
        if (object != null) {
          error = "【" + object.toString() + "】格式非法,请输入【" + hopeType + "】";
        } else {
          error = "参数格式非法";
        }
      } else {
        error = "参数格式非法";
      }
    } else if (e.getCause() != null) {
      error = e.getCause().getMessage() == null ? "空指针异常" : e.getCause().getMessage();
    } else {
      error = e.getMessage() == null ? "空指针异常" : e.getMessage();
    }
    return Ajax.error(error);
  }
}
