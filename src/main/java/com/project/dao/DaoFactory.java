package com.project.dao;

import com.project.bean.system.PaySettingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用mapper的总仓库
 */
@Component
public class DaoFactory {
	@Autowired
	public SettingDao settingDao;//系统设置
	@Autowired
	public SystemModuleDao systemModuleDao;//系统模块
	@Autowired
	public CodeDao codeDao;//验证码
	@Autowired
	public OthersDao othersDao;
	@Autowired
	public SystemAccountDao systemAccountDao;//系统账号
	@Autowired
	public InstitutionDao institutionDao;//机构
	@Autowired
	public ExamDao examDao;//考试科目
	@Autowired
	public StudentDao studentDao;
	@Autowired
	public StudentHistoryDao studentHistoryDao;//历史考生信息
	@Autowired
	public PayHistoryDao payHistoryDao;//缴费记录
	@Autowired
	public ExamRoomDao examRoomDao;//考场
	@Autowired
	public InstitutionLevelDao institutionLevelDao;//机构级别
	@Autowired
	public AreaDao areaDao;//区域
	@Autowired
	public SignUpApplyDao signUpApplyDao;//报名申请
	@Autowired
	public PaySettingDao paySettingDao;//支付设置
}
