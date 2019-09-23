package com.project.bean.others;

/**
 * ajax返回结果
 * @author 彭方林
 * @date 2018年4月2日
 */
public class Ajax {
	private Integer status;//状态码  200:成功  500:失败 202:等待中 401:未授权 403:权限不足
	private String error;//错误信息
	private Object data;//结果集
	private Long total=0L;//分页总条数

	public static Ajax ok() {
		Ajax ajax = new Ajax();
		ajax.setStatus(200);
		return ajax;
	}
	public static Ajax ok(Object object) {
		Ajax ajax = new Ajax();
		ajax.setStatus(200);
		ajax.setData(object);
		return ajax;
	}
	public static Ajax ok(Object object, Long total) {
		Ajax ajax = new Ajax();
		ajax.setStatus(200);
		ajax.setData(object);
		ajax.setTotal(total);
		return ajax;
	}
	public static Ajax error(String error) {
		Ajax ajax = new Ajax();
		ajax.setStatus(500).setError(error);
		return ajax;
	}
	public static Ajax status(Integer status) {
		Ajax ajax = new Ajax();
		ajax.setStatus(status);
		return ajax;
	}
	public static Ajax status(Integer status,String error) {
		Ajax ajax = new Ajax();
		ajax.setStatus(status).setError(error);
		return ajax;
	}
	public static Ajax pending(String error) {
		Ajax ajax = new Ajax();
		ajax.setStatus(202).setError(error);
		return ajax;
	}
	public Integer getStatus() {
		return status;
	}
	public Ajax setStatus(Integer status) {
		this.status = status;
		return this;
	}
	public String getError() {
		return error;
	}
	public Ajax setError(String error) {
		this.error = error;
		return this;
	}
	public Object getData() {
		return data;
	}
	public Ajax setData(Object data) {
		this.data = data;
		return this;
	}
	public Long getTotal() {
		return total;
	}
	public Ajax setTotal(Long total) {
		this.total = total;
		return this;
	}
}
