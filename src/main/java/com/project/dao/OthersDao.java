package com.project.dao;

import com.project.bean.institution.*;
import com.project.bean.others.*;
import com.project.bean.pay.PayHistoryBean;
import com.project.bean.system.SystemAccountBean;
import com.project.bean.system.SystemModuleBean;
import com.project.others.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OthersDao {
	/**
	 * 获取模块列表
	 */
	List<SystemModuleBean> getSystemModuleTree(SystemAccountBean systemAccountBean);
	/**
	 * 通过用户名获取用户基本信息和角色信息
	 */
	ShiroCacheInfo getAccountWithRoleByUsername(String account);
	/**
	 * 系统账号列表
	 */
	List<SystemAccountBean> getSystemAccountList(SystemAccountBean systemAccountBean, PageBean pageBean);
	/**
	 * 系统账号详情
	 */
	SystemAccountBean getSystemAccountDetail(SystemAccountBean systemAccountBean);
	/**
	 * 考上信息详情
	 */
  StudentBean getStudentDetail(StudentBean studentBean);
	/**
	 * 考生信息列表
	 */
	List<StudentBean> getStudentList(StudentBean studentBean, PageBean pageBean);
  /**
   * 批量插入考生信息
   */
  int insertStudentMany(List<StudentBean> studentBeans);
	/**
	 * 历史考生信息列表
	 */
	List<StudentHistoryBean> getStudentHistoryList(StudentHistoryBean studentHistoryBean, PageBean pageBean);
	/**
	 * 待支付学生列表
	 */
	List<StudentBean> getWaitPayStudentList(InstitutionBean institutionBean);
	/**
	 * 考上支付成功之后修改支付状态
	 */
	int payStudentSuccess(@Param("ids") String ids);
	/**
	 * 考场列表
	 */
	List<ExamRoomBean> getExamRoomList(ExamRoomBean examRoomBean, PageBean pageBean);
	/**
	 * 财务统计
	 */
	List<HighChartsBean> getFinancialStatistics();
	/**
	 * 修改考生考场分配状态
	 */
	int updateStudentAllotState(StudentBean studentBean);
	/**
	 * 机构列表
	 */
	List<InstitutionBean> getInstitutionList(InstitutionBean institutionBean, PageBean pageBean);
	/**
	 * 区域树
	 */
	List<AreaBean> getAreaTree();
	/**
	 * 获取机构的所有学生id
	 */
	String getInstitutionStudentIds(StudentBean studentBean);
	/**
	 * 考试科目列表(去重)
	 */
	List<ExamBean> getExamListDistrict();
	/**
	 * 机构待分配考场人数
	 */
	Integer getWaitAllotStudentCount(Integer institution_id);
	/**
	 * 用户当前时间已通过报名申请列表
	 */
	List<SignUpApplyBean> getInstitutionSignUpNow(@Param("institution_id") Integer institution_id);
	/**
	 * 报名申请列表
	 */
	List<SignUpApplyBean> getSignUpApplyList(SignUpApplyBean signUpApplyBean, PageBean pageBean);
	/**
	 * 审核报名申请
	 */
	int auditSignUpApply(SignUpApplyBean signUpApplyBean);
	/**
	 * 批量删除报名申请
	 */
	int deleteSignUpApplyMany(SignUpApplyBean signUpApplyBean);
	/**
	 * 缴费记录
	 */
	List<PayHistoryBean> getPayHistoryList(PayHistoryBean payHistoryBean,PageBean pageBean);
	/**
	 * 获取可以打印pdf的学生列表
	 */
	List<StudentBean> getPdfStudentList(StudentBean studentBean);
	/**
	 * 获取最新的报考批次编号
	 */
	String getMaxBatchNo();
	/**
	 * 获取机构最新的报名申请
	 */
	SignUpApplyBean getInstitutionSignUpNew(@Param("institution_id") Integer institution_id);
	/**
	 * 批量修改学生缴费状态
	 */
	int updateStudentPayStateMany(StudentBean studentBean);
	/**
	 * 学生历史最大报考级别
	 */
	Integer getStudentMaxLevel(StudentBean studentBean);
	/**
	 * 考场已分配考生信息
	 */
	List<StudentBean> getExamRoomStudentList(ExamRoomBean examRoomBean);
	/**
	 * 批次号列表
	 */
	List<Map<String,String>> getStudentBatchNoList(Map<String,String> params);
	/**
	 * 删除学生
	 */
	int deleteStudent(StudentBean studentBean);
	/**
	 * 删除历史考生数据
	 */
	int deleteStudentHistory(@Param("student_ids") String student_ids);
	/**
	 * 机构是否已经提交过申请
	 */
	SignUpApplyBean institutionIsSignUp(@Param("institution_id") Integer institution_id);
	/**
	 * 该开考时间最大准考证号
	 */
  String getMaxTicketNoByDate(@Param("date") String date);
	/**
	 * 批量导入考上成绩
	 */
	int importStudentScore(List<StudentHistoryBean> studentHistoryBeans);
	/**
	 * 科目列表
	 */
	String getDistinctProfessionList(HashMap<String,Object> params);
	/**
	 * 民族列表
	 */
	String getDistinctNationList(HashMap<String,Object> params);
	/**
	 * 未上传照片考生数量
	 */
	int getNoPhotoStudentCount(@Param("institution_id") Integer institution_id);
	/**
	 * 通过机构账号获取绑定手机号
	 */
	Map<String,String> getInstitutionPhoneByUsername(@Param("username") String username);
	/**
	 * 批量插入考生信息
	 */
	int insertStudentHistoryMany(List<StudentHistoryBean> studentHistoryBeans);
	/**
	 * 考生查询成绩
	 */
	StudentHistoryBean queryResults(String search);
  /**
   * 批量通过校验考生
   */
  int studentPassAuditMany(@Param("student_ids") String student_ids);

	String getNotImportScoreBatchNoList(InstitutionBean institutionBean);
}
