package com.project.controller;

import com.project.bean.institution.InstitutionBean;
import com.project.bean.others.Ajax;
import com.project.bean.system.PaySettingBean;
import com.project.bean.system.SettingBean;
import com.project.bean.system.SystemAccountBean;
import com.project.bean.system.SystemModuleBean;
import com.project.others.PageBean;
import com.project.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统权限控制器
 * @author 方林
 *
 */
@RestController
@RequestMapping("/api/systemController/")
public class SystemController  {
	@Autowired
	SystemService systemService;
	/**
	 * 修改个人二维码
	 */
	@PostMapping("updatePayQr")
	public Ajax updatePayQr(PaySettingBean paySettingBean){
		if(systemService.updatePayQr(paySettingBean)>0){
			return Ajax.ok("修改成功");
		}else{
			return Ajax.error("修改失败");
		}
	}
	/**
	 * 个人付款二维码
	 */
	@PostMapping("getPayQr")
	public Ajax getPayQr(){
		return Ajax.ok(systemService.getPayQr());
	}
	/**
	 * 员工登录
	 */
	@PostMapping("systemLogin")
	public Ajax systemLogin(SystemAccountBean systemAccountBean) {
		return Ajax.ok(systemService.systemLogin(systemAccountBean));
	}
	/**
	 * 机构状态
	 */
	@PostMapping("institutionState")
	public Ajax institutionState(Integer institution_id) {
		return Ajax.ok(systemService.institutionState(institution_id));
	}
	/**
	 * 模块树
	 */
	@PostMapping("getSystemModuleTree")
	public Ajax getSystemModuleTree(SystemAccountBean systemAccountBean){
		return Ajax.ok(systemService.getSystemModuleTree(systemAccountBean));
	}

	/**
	 * 获取系统模块列表
	 */
	@PostMapping("getSystemModuleList")
	public Ajax getSystemModuleList(PageBean pageBean, SystemModuleBean systemModuleBean){
		return Ajax.ok(systemService.getSystemModuleList(systemModuleBean,pageBean),pageBean.getTotal());
	}
	/**
	 * 添加系统模块
	 */
	@PostMapping("insertSystemModule")
	public Ajax insertSystemModule(SystemModuleBean systemModuleBean){
		int num=systemService.insertSystemModule(systemModuleBean);
		if(num>0) {
			return Ajax.ok("添加成功");
		}else {
			return Ajax.error("添加失败");
		}
	}
	/**
	 * 删除系统模块
	 */
	@PostMapping("deleteSystemModule")
	public Ajax deleteSystemModule(SystemModuleBean systemModuleBean){
		int num=systemService.deleteSystemModule(systemModuleBean);
		if(num>0) {
			return Ajax.ok("删除成功");
		}else {
			return Ajax.error("删除失败");
		}
	}
	/**
	 * 修改系统模块
	 */
	@PostMapping("updateSystemModule")
	public Ajax updateSystemModule(SystemModuleBean systemModuleBean){
		int num=systemService.updateSystemModule(systemModuleBean);
		if(num>0) {
			return Ajax.ok("修改成功");
		}else {
			return Ajax.error("修改失败");
		}
	}
	/**
	 * 单个系统模块详情
	 */
	@PostMapping("getSystemModuleDetail")
	public Ajax getSystemModuleDetail(SystemModuleBean systemModuleBean){
		return Ajax.ok(systemService.getSystemModuleDetail(systemModuleBean));
	}
	/**
	 * 系统账号列表
	 */
	@PostMapping("getSystemAccountList")
	public Ajax getSystemAccountList(SystemAccountBean systemAccountBean,PageBean pageBean){
		return Ajax.ok(systemService.getSystemAccountList(systemAccountBean,pageBean),pageBean.getTotal());
	}
	/**
	 * 删除系统账号
	 */
	@PostMapping("deleteSystemAccount")
	public Ajax deleteSystemAccount(SystemAccountBean systemAccountBean){
		if(systemService.deleteSystemAccount(systemAccountBean)>0){
			return Ajax.ok("删除成功");
		}else{
			return Ajax.error("删除失败");
		}
	}
	/**
	 * 系统账号详情
	 */
	@PostMapping("getSystemAccountDetail")
	public Ajax getSystemAccountDetail(SystemAccountBean systemAccountBean){
		return Ajax.ok(systemService.getSystemAccountDetail(systemAccountBean));
	}
	/**
	 * 删除系统账号
	 */
	@PostMapping("insertSystemAccount")
	public Ajax insertSystemAccount(SystemAccountBean systemAccountBean){
		if(systemService.insertSystemAccount(systemAccountBean)>0){
			return Ajax.ok("添加成功");
		}else{
			return Ajax.error("添加失败");
		}
	}
	/**
	 * 删除系统账号
	 */
	@PostMapping("updateSystemAccount")
	public Ajax updateSystemAccount(SystemAccountBean systemAccountBean){
		if(systemService.updateSystemAccount(systemAccountBean)>0){
			return Ajax.ok("修改成功");
		}else{
			return Ajax.error("修改失败");
		}
	}
	/**
	 * 财务统计
	 */
	@PostMapping("getFinancialStatistics")
	public Ajax getFinancialStatistics(){
		return Ajax.ok(systemService.getFinancialStatistics());
	}
	/**
	 * 修改设置
	 */
	@PostMapping("updateSetting")
	public Ajax updateSetting(SettingBean settingBean){
		if(systemService.updateSetting(settingBean)>0){
			return Ajax.ok("修改成功");
		}else{
			return Ajax.error("修改失败");
		}
	}
	/**
	 * 设置列表
	 */
	@PostMapping("getSettingList")
	public Ajax getSettingList(PageBean pageBean){
		return Ajax.ok(systemService.getSettingList(pageBean));
	}
}
