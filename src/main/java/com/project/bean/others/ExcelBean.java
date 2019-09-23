package com.project.bean.others;

/**
 * excel列信息
 * @author 彭方林
 */
public class ExcelBean {
	private String name;//标题
	private String type;//类型

	public ExcelBean(String name,String type){
		this.name=name;
		this.type=type;
	}
	public String getName() {
		return name;
	}
	public ExcelBean setName(String name) {
		this.name = name;
		return this;
	}
	public String getType() {
		return type;
	}
	public ExcelBean setType(String type) {
		this.type = type;
		return this;
	}
	
	
}
