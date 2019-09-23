package com.project.bean.others;


import java.io.Serializable;
/**
 * shiro授权后redis中存放的信息
 */
public class ShiroCacheInfo implements Serializable {
  private Integer account_id;//用户主键
  private String username;//用户名
  private String password;//密码
  private String is_disable;//是否禁用
  private String system_type;//用户的角色

  public Integer getAccount_id() {
    return account_id;
  }

  public ShiroCacheInfo setAccount_id(Integer account_id) {
    this.account_id = account_id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public ShiroCacheInfo setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public ShiroCacheInfo setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getIs_disable() {
    return is_disable;
  }

  public ShiroCacheInfo setIs_disable(String is_disable) {
    this.is_disable = is_disable;
    return this;
  }

	public String getSystem_type() {
		return system_type;
	}

	public ShiroCacheInfo setSystem_type(String system_type) {
		this.system_type = system_type;
		return this;
	}
}
