package com.project.service;

import com.project.bean.institution.InstitutionBean;
import com.project.bean.institution.SignUpApplyBean;
import com.project.bean.others.HighChartsBean;
import com.project.bean.others.ShiroCacheInfo;
import com.project.bean.system.PaySettingBean;
import com.project.bean.system.SettingBean;
import com.project.bean.system.SystemAccountBean;
import com.project.bean.system.SystemModuleBean;
import com.project.dao.DaoFactory;
import com.project.others.PageBean;
import com.project.utils.EncodeUtils;
import com.project.utils.OthersUtils;
import com.project.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class SystemService {
	@Autowired
    DaoFactory daoFactory;

	/**
	 * 获取系统模块列表
	 */
	public List<SystemModuleBean> getSystemModuleList(SystemModuleBean systemModuleBean,PageBean pageBean) {
		Example example=new Example(SystemModuleBean.class);
		example.setOrderByClause("sort asc,create_time desc");
		example.createCriteria().andEqualTo("parent_id",systemModuleBean.getParent_id());
		return daoFactory.systemModuleDao.selectByExampleAndRowBounds(example,pageBean);
	}
	/**
	 * 添加系统模块
	 */
	public int insertSystemModule(SystemModuleBean systemModuleBean) {
		if(systemModuleBean.getParent_id()==null){
			systemModuleBean.setParent_id(0);
		}
		return daoFactory.systemModuleDao.insertSelective(systemModuleBean);
	}
	/**
	 * 删除系统模块
	 */
	public int deleteSystemModule(SystemModuleBean systemModuleBean) {
		return daoFactory.systemModuleDao.deleteByPrimaryKey(systemModuleBean);
	}
	/**
	 * 修改系统模块
	 */
	public int updateSystemModule(SystemModuleBean systemModuleBean) {
		return daoFactory.systemModuleDao.updateByPrimaryKeySelective(systemModuleBean);
	}
	/**
	 * 系统模块详情
	 */
	public SystemModuleBean getSystemModuleDetail(SystemModuleBean systemModuleBean) {
		return daoFactory.systemModuleDao.selectByPrimaryKey(systemModuleBean);
	}
	/**
	 * 通过账号获取账号信息包含权限信息
	 */
	public ShiroCacheInfo getAccountWithRoleByUsername(String account) {
		return daoFactory.othersDao.getAccountWithRoleByUsername(account);
	}
	/**
	 * 账号登录
	 */
	@Transactional
	public SystemAccountBean systemLogin(SystemAccountBean systemAccountBean) {
		if(OthersUtils.isEmpty(systemAccountBean.getUsername())){
			throw new RuntimeException("请输入用户名");
		}
		if(OthersUtils.isEmpty(systemAccountBean.getPassword())){
			throw new RuntimeException("请输入密码");
		}
		SystemAccountBean systemAccountBean1=daoFactory.systemAccountDao.selectOne(new SystemAccountBean().setUsername(systemAccountBean.getUsername()));
		if(systemAccountBean1==null){
			throw new RuntimeException("用户不存在");
		}
		if(!EncodeUtils.MD5Encode(systemAccountBean.getPassword()).equals(systemAccountBean1.getPassword())){
			throw new RuntimeException("密码错误");
		}
		systemAccountBean1.setIs_sign_up("0");
		if("institution".equals(systemAccountBean1.getSystem_type())){
			List<SignUpApplyBean> signUpApplyBeans=daoFactory.othersDao.getInstitutionSignUpNow(systemAccountBean1.getInstitution_id());
			if(signUpApplyBeans.size()>0){
				systemAccountBean1.setIs_sign_up("1");
			}
			//判断是否可以缴费
			SignUpApplyBean signUpApplyBean=daoFactory.othersDao.getInstitutionSignUpNew(systemAccountBean1.getInstitution_id());
			systemAccountBean1.setIs_pay("0");
			if(signUpApplyBean!=null){
				int date=TimeUtils.getDayCompareDate(signUpApplyBean.getEnd_time(),TimeUtils.getCurrentTime(),"yyyy-MM-dd");
				if(date<=0&&date>=-5){
					systemAccountBean1.setIs_pay("0");
				}
			}
		}
		if(systemAccountBean1.getInstitution_id()!=0){
			InstitutionBean institutionBean=daoFactory.institutionDao.selectByPrimaryKey(systemAccountBean1.getInstitution_id());
			if(institutionBean!=null){
				systemAccountBean1.setInstitution_name(institutionBean.getInstitution_name());
			}
		}
		return systemAccountBean1.setPassword(null);
	}
	/**
	 * 获取模块树
	 */
	public List<SystemModuleBean> getSystemModuleTree(SystemAccountBean systemAccountBean) {
		return daoFactory.othersDao.getSystemModuleTree(systemAccountBean);
	}
	/**
	 * 系统账号列表
	 */
	public List<SystemAccountBean> getSystemAccountList(SystemAccountBean systemAccountBean, PageBean pageBean) {
		return daoFactory.othersDao.getSystemAccountList(systemAccountBean,pageBean);
	}
	/**
	 * 删除系统账号
	 */
	public int deleteSystemAccount(SystemAccountBean systemAccountBean) {
		return daoFactory.systemAccountDao.deleteByPrimaryKey(systemAccountBean);
	}
	/**
	 * 系统账号详情
	 */
	public SystemAccountBean getSystemAccountDetail(SystemAccountBean systemAccountBean) {
		return daoFactory.othersDao.getSystemAccountDetail(systemAccountBean);
	}
	/**
	 * 添加系统账号
	 */
	public int insertSystemAccount(SystemAccountBean systemAccountBean) {
		SystemAccountBean systemAccountBean1=daoFactory.systemAccountDao.selectOne(new SystemAccountBean().setUsername(systemAccountBean.getUsername()));
		if(systemAccountBean1!=null){
			throw new RuntimeException("账号已存在");
		}
		//对密码进行加密
		String password= EncodeUtils.MD5Encode(systemAccountBean.getPassword());
		return daoFactory.systemAccountDao.insertSelective(systemAccountBean.setPassword(password));
	}
	/**
	 * 修改系统账号
	 */
	public int updateSystemAccount(SystemAccountBean systemAccountBean) {
		if(!OthersUtils.isEmpty(systemAccountBean.getPassword())){
			String password=EncodeUtils.MD5Encode(systemAccountBean.getPassword());
			systemAccountBean.setPassword(password);
		}else{
			systemAccountBean.setPassword(null);
		}
		return daoFactory.systemAccountDao.updateByPrimaryKeySelective(systemAccountBean);
	}
	/**
	 * 财务统计
	 */
	public List<HighChartsBean> getFinancialStatistics() {
		return daoFactory.othersDao.getFinancialStatistics();
	}
	/**
	 * 修改设置
	 */
	public int updateSetting(SettingBean settingBean) {
		return daoFactory.settingDao.updateByPrimaryKeySelective(settingBean);
	}
	/**
	 * 设置列表
	 */
	public List<SettingBean> getSettingList(PageBean pageBean) {
		return daoFactory.settingDao.selectByRowBounds(new SettingBean(),pageBean);
	}
	/**
	 * 修改个人支付二维码
	 */
	public int updatePayQr(PaySettingBean paySettingBean) {
		return daoFactory.paySettingDao.updateByPrimaryKeySelective(paySettingBean);
	}
	/**
	 * 个人付款二维码
	 */
	public PaySettingBean getPayQr() {
		return daoFactory.paySettingDao.selectOne(null);
	}
	/**
	 * 机构状态
	 */
	public Map<String,String> institutionState(Integer institution_id) {
		Map<String,String> result=new HashMap<>();
		String is_sign_up="0";
		String is_pay="0";
		List<SignUpApplyBean> signUpApplyBeans=daoFactory.othersDao.getInstitutionSignUpNow(institution_id);
		if(signUpApplyBeans.size()>0){
			is_sign_up="1";
		}
		//判断是否可以缴费
		SignUpApplyBean signUpApplyBean=daoFactory.othersDao.getInstitutionSignUpNew(institution_id);
		if(signUpApplyBean!=null){
			int date=TimeUtils.getDayCompareDate(signUpApplyBean.getEnd_time(),TimeUtils.getCurrentTime(),"yyyy-MM-dd");
			if(date<=0&&date>=-5){
				is_pay="0";
			}
		}
		SystemAccountBean systemAccountBean=daoFactory.systemAccountDao.selectOne(new SystemAccountBean().setInstitution_id(institution_id));
		if(systemAccountBean==null){
			throw new RuntimeException("账号不存在");
		}
		if("1".equals(systemAccountBean.getIs_apply_pay())){
			is_pay="1";
		}
		result.put("is_sign_up",is_sign_up);
		result.put("is_pay",is_pay);
		return result;
	}
}
