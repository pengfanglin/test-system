package com.project.controller;

import com.project.bean.institution.*;
import com.project.bean.others.Ajax;
import com.project.bean.pay.PayHistoryBean;
import com.project.bean.system.SystemAccountBean;
import com.project.others.PageBean;
import com.project.service.InstitutionService;
import com.project.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 机构
 */
@RestController
@RequestMapping("/api/institutionController")
public class InstitutionController {
    @Autowired
    InstitutionService institutionService;

    /**
     * 批次号列表
     */
    @PostMapping("getStudentBatchNoList")
    public Ajax getStudentBatchNoList(@RequestParam Map<String, String> params) {
        return Ajax.ok(institutionService.getStudentBatchNoList(params));
    }

    /**
     * 修改缴费支付方式
     */
    @PostMapping("changePayApplyPayWay")
    public Ajax changePayApplyPayWay(PayHistoryBean payHistoryBean) {
        return Ajax.ok(institutionService.changePayApplyPayWay(payHistoryBean));
    }

    /**
     * 考场已分配考生信息
     */
    @PostMapping("getExamRoomStudentList")
    public Ajax getExamRoomStudentList(ExamRoomBean examRoomBean) {
        return Ajax.ok(institutionService.getExamRoomStudentList(examRoomBean));
    }

    /**
     * 可排考考场列表
     */
    @PostMapping("getAllotExamRoomList")
    public Ajax getAllotExamRoomList(ExamRoomBean examRoomBean) {
        return Ajax.ok(institutionService.getAllotExamRoomList(examRoomBean));
    }

    /**
     * 导出考生照片
     */
    @PostMapping("exportStudentPhoto")
    public Ajax exportStudentPhoto(InstitutionBean institutionBean) {
        return Ajax.ok(institutionService.exportStudentPhoto(institutionBean));
    }

    /**
     * 手动设置缴费成功
     */
    @PostMapping("setPaySuccess")
    public Ajax setPaySuccess(PayHistoryBean payHistoryBean) {
        if (institutionService.setPaySuccess(payHistoryBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 审核缴费申请
     */
    @PostMapping("auditPayApply")
    public Ajax auditPayApply(PayHistoryBean payHistoryBean) {
        if (institutionService.auditPayApply(payHistoryBean) > 0) {
            return Ajax.ok("审核成功");
        } else {
            return Ajax.error("审核失败");
        }
    }

    /**
     * 添加机构
     */
    @PostMapping("insertInstitution")
    public Ajax insertInstitution(InstitutionBean institutionBean, SystemAccountBean systemAccountBean) {
        if (institutionService.insertInstitution(institutionBean, systemAccountBean) > 0) {
            return Ajax.ok("添加成功");
        } else {
            return Ajax.error("添加失败");
        }
    }

    /**
     * 删除机构
     */
    @PostMapping("deleteInstitution")
    public Ajax deleteInstitution(InstitutionBean institutionBean) {
        if (institutionService.deleteInstitution(institutionBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 修改机构
     */
    @PostMapping("updateInstitution")
    public Ajax updateInstitution(InstitutionBean institutionBean) {
        if (institutionService.updateInstitution(institutionBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 机构详情
     */
    @PostMapping("getInstitutionDetail")
    public Ajax getInstitutionDetail(InstitutionBean institutionBean) {
        return Ajax.ok(institutionService.getInstitutionDetail(institutionBean));
    }

    /**
     * 机构列表
     */
    @PostMapping("getInstitutionList")
    public Ajax getInstitutionList(InstitutionBean institutionBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getInstitutionList(institutionBean, pageBean), pageBean.getTotal());
    }

    /**
     * 添加考试科目
     */
    @PostMapping("insertExam")
    public Ajax insertExam(ExamBean examBean) {
        if (institutionService.insertExam(examBean) > 0) {
            return Ajax.ok("添加成功");
        } else {
            return Ajax.error("添加失败");
        }
    }

    /**
     * 删除考试科目
     */
    @PostMapping("deleteExam")
    public Ajax deleteExam(ExamBean examBean) {
        if (institutionService.deleteExam(examBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 修改考试科目
     */
    @PostMapping("updateExam")
    public Ajax updateExam(ExamBean examBean) {
        if (institutionService.updateExam(examBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 考试科目详情
     */
    @PostMapping("getExamDetail")
    public Ajax getExamDetail(ExamBean examBean) {
        return Ajax.ok(institutionService.getExamDetail(examBean));
    }

    /**
     * 考试科目列表
     */
    @PostMapping("getExamList")
    public Ajax getExamList(ExamBean examBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getExamList(examBean, pageBean), pageBean.getTotal());
    }

    /**
     * 科目列表
     */
    @PostMapping("getDistinctProfessionList")
    public Ajax getDistinctProfessionList(@RequestParam HashMap<String, Object> params) {
        return Ajax.ok(institutionService.getDistinctProfessionList(params));
    }

    /**
     * 科目列表
     */
    @PostMapping("getDistinctNationList")
    public Ajax getDistinctNationList(@RequestParam HashMap<String, Object> params) {
        return Ajax.ok(institutionService.getDistinctNationList(params));
    }

    /**
     * 考试科目列表(去重)
     */
    @PostMapping("getExamListDistrict")
    public Ajax getExamListDistrict() {
        return Ajax.ok(institutionService.getExamListDistrict());
    }

    /**
     * 添加考生信息
     */
    @PostMapping("insertStudent")
    public Ajax insertStudent(StudentBean studentBean) {
        if (institutionService.insertStudent(studentBean) > 0) {
            return Ajax.ok("添加成功");
        } else {
            return Ajax.error("添加失败");
        }
    }

    /**
     * 批量通过校验考生
     */
    @PostMapping("studentPassAuditMany")
    public Ajax studentPassAuditMany(String student_ids) {
        institutionService.studentPassAuditMany(student_ids);
        return Ajax.ok("审核成功");
    }

    /**
     * 删除考生信息
     */
    @PostMapping("deleteStudent")
    public Ajax deleteStudent(StudentBean studentBean) {
        if (institutionService.deleteStudent(studentBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 修改考生信息
     */
    @PostMapping("updateStudent")
    public Ajax updateStudent(StudentBean studentBean) {
        if (institutionService.updateStudent(studentBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 取消考上的考场分配
     */
    @PostMapping("cancelAllotExamRoom")
    public Ajax cancelAllotExamRoom(ExamRoomBean examRoomBean, Integer student_id) {
        if (institutionService.cancelAllotExamRoom(examRoomBean, student_id) > 0) {
            return Ajax.ok("取消成功");
        } else {
            return Ajax.error("取消失败");
        }
    }

    /**
     * 考生信息详情
     */
    @PostMapping("getStudentDetail")
    public Ajax getStudentDetail(StudentBean studentBean) {
        return Ajax.ok(institutionService.getStudentDetail(studentBean));
    }

    /**
     * 考生信息列表
     */
    @PostMapping("getStudentList")
    public Ajax getStudentList(StudentBean studentBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getStudentList(studentBean, pageBean), pageBean.getTotal());
    }

    /**
     * 导入考生信息
     */
    @PostMapping("importStudent")
    public Ajax importStudent(String file_path, Integer institution_id) {
        int num = institutionService.importStudent(file_path, institution_id);
        if (num > 0) {
            return Ajax.ok(num);
        } else {
            return Ajax.error("考生信息入库失败");
        }
    }

    /**
     * 导入考生信息
     */
    @PostMapping("importStudentHistory")
    public Ajax importStudentHistory(String file_path) {
        int num = institutionService.importStudentHistory(file_path);
        if (num > 0) {
            return Ajax.ok(num);
        } else {
            return Ajax.error("考生信息入库失败");
        }
    }

    /**
     * 批量导入考生成绩
     */
    @PostMapping("importStudentScore")
    public Ajax importStudentScore(String file_path) {
        return Ajax.ok(institutionService.importStudentScore(file_path));
    }

    /**
     * 重新导入已出错学生信息
     */
    @PostMapping("importErrorStudent")
    public Ajax importErrorStudent(String file_path, Integer institution_id) {
        int num = institutionService.importErrorStudent(file_path, institution_id);
        if (num > 0) {
            return Ajax.ok(num);
        } else {
            return Ajax.error("考生信息入库失败");
        }
    }

    /**
     * 批量上传考生图片
     */
    @PostMapping("updateStudentPhotoMany")
    public Ajax updateStudentPhotoMany(HttpServletRequest request) {
        institutionService.updateStudentPhotoMany(request);
        return Ajax.ok("上传成功");
    }

    /**
     * 删除历史考生信息
     */
    @PostMapping("deleteStudentHistory")
    public Ajax deleteStudentHistory(String student_ids) {
        if (institutionService.deleteStudentHistory(student_ids) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 历史考生信息列表
     */
    @PostMapping("getStudentHistoryList")
    public Ajax getStudentHistoryList(StudentHistoryBean studentHistoryBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getStudentHistoryList(studentHistoryBean, pageBean), pageBean.getTotal());
    }

    /**
     * 修改历史考生信息
     */
    @PostMapping("updateStudentHistory")
    public Ajax updateStudentHistory(StudentHistoryBean studentHistoryBean) {
        if (institutionService.updateStudentHistory(studentHistoryBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 导出考生信息
     */
    @PostMapping("exportStudent")
    public Ajax exportStudent(StudentBean studentBean) {
        String file = institutionService.exportStudent(studentBean);
        if (file == null) {
            return Ajax.error("导出失败");
        } else {
            return Ajax.ok(file);
        }
    }

    /**
     * 导出错误考生信息
     */
    @PostMapping("exportErrorStudent")
    public Ajax exportErrorStudent(StudentBean studentBean) {
        String file = institutionService.exportErrorStudent(studentBean);
        if (file == null) {
            return Ajax.error("导出失败");
        } else {
            return Ajax.ok(file);
        }
    }

    /**
     * 考生缴费
     */
    @PostMapping("payStudent")
    public Ajax payStudent(InstitutionBean institutionBean) {
        if (institutionService.payStudent(institutionBean) > 0) {
            return Ajax.ok("付款申请提交成功");
        } else {
            return Ajax.error("支付创建失败");
        }
    }

    /**
     * 添加考场信息
     */
    @PostMapping("insertExamRoom")
    public Ajax insertExamRoom(ExamRoomBean examRoomBean) {
        if (institutionService.insertExamRoom(examRoomBean) > 0) {
            return Ajax.ok("添加成功");
        } else {
            return Ajax.error("添加失败");
        }
    }

    /**
     * 删除考场信息
     */
    @PostMapping("deleteExamRoom")
    public Ajax deleteExamRoom(ExamRoomBean examRoomBean) {
        if (institutionService.deleteExamRoom(examRoomBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 修改考场信息
     */
    @PostMapping("updateExamRoom")
    public Ajax updateExamRoom(ExamRoomBean examRoomBean) {
        if (institutionService.updateExamRoom(examRoomBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 考场信息列表
     */
    @PostMapping("getExamRoomList")
    public Ajax getExamRoomList(ExamRoomBean examRoomBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getExamRoomList(examRoomBean, pageBean), pageBean.getTotal());
    }

    /**
     * 分配考场
     */
    @PostMapping("allotExamRoom")
    public Ajax allotExamRoom(ExamRoomBean examRoomBean) {
        return Ajax.ok(institutionService.allotExamRoom(examRoomBean));
    }

    /**
     * 添加机构级别
     */
    @PostMapping("insertInstitutionLevel")
    public Ajax insertInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        if (institutionService.insertInstitutionLevel(institutionLevelBean) > 0) {
            return Ajax.ok("添加成功");
        } else {
            return Ajax.error("添加失败");
        }
    }

    /**
     * 删除机构级别
     */
    @PostMapping("deleteInstitutionLevel")
    public Ajax deleteInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        if (institutionService.deleteInstitutionLevel(institutionLevelBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 修改机构级别
     */
    @PostMapping("updateInstitutionLevel")
    public Ajax updateInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        if (institutionService.updateInstitutionLevel(institutionLevelBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }

    /**
     * 机构级别列表
     */
    @PostMapping("getInstitutionLevelList")
    public Ajax getInstitutionLevelList(PageBean pageBean) {
        return Ajax.ok(institutionService.getInstitutionLevelList(pageBean), pageBean.getTotal());
    }

    /**
     * 添加/修改区域
     */
    @PostMapping("insertOrUpdateArea")
    public Ajax insertOrUpdateArea(AreaBean areaBean) {
        if (institutionService.insertOrUpdateArea(areaBean) > 0) {
            return Ajax.ok("操作成功");
        } else {
            return Ajax.error("操作失败");
        }
    }

    /**
     * 删除区域
     */
    @PostMapping("deleteArea")
    public Ajax deleteArea(AreaBean areaBean) {
        if (institutionService.deleteArea(areaBean) > 0) {
            return Ajax.ok("删除成功");
        } else {
            return Ajax.error("删除失败");
        }
    }

    /**
     * 区域列表
     */
    @PostMapping("getAreaList")
    public Ajax getAreaList(AreaBean areaBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getAreaList(areaBean, pageBean), pageBean.getTotal());
    }

    /**
     * 区域树
     */
    @PostMapping("getAreaTree")
    public Ajax getAreaTree() {
        return Ajax.ok(institutionService.getAreaTree());
    }

    /**
     * 机构待分配考场人数
     */
    @PostMapping("getWaitAllotStudentCount")
    public Ajax getWaitAllotStudentCount(Integer institution_id) {
        return Ajax.ok(institutionService.getWaitAllotStudentCount(institution_id));
    }

    /**
     * 批量删除报名申请
     */
    @PostMapping("deleteSignUpApplyMany")
    public Ajax deleteSignUpApplyMany(SignUpApplyBean signUpApplyBean) {
        institutionService.deleteSignUpApplyMany(signUpApplyBean);
        return Ajax.ok("删除成功");
    }

    /**
     * 添加报名申请
     */
    @PostMapping("insertSignUpApply")
    public Ajax insertSignUpApply(SignUpApplyBean signUpApplyBean) {
        if (institutionService.insertSignUpApply(signUpApplyBean) > 0) {
            return Ajax.ok("申请成功，等待平台审核");
        } else {
            return Ajax.error("申请失败");
        }
    }

    /**
     * 机构未导入成绩批次查询
     */
    @PostMapping("getNotImportScoreBatchNoList")
    public Ajax getNotImportScoreBatchNoList(InstitutionBean institutionBean) {
        return Ajax.ok(institutionService.getNotImportScoreBatchNoList(institutionBean));
    }

    /**
     * 审核报名申请
     */
    @PostMapping("auditSignUpApply")
    public Ajax auditSignUpApply(SignUpApplyBean signUpApplyBean) {
        if (institutionService.auditSignUpApply(signUpApplyBean) > 0) {
            return Ajax.ok("审核成功");
        } else {
            return Ajax.error("审核失败");
        }
    }

    /**
     * 报名申请列表
     */
    @PostMapping("getSignUpApplyList")
    public Ajax getSignUpApplyList(SignUpApplyBean signUpApplyBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getSignUpApplyList(signUpApplyBean, pageBean), pageBean.getTotal());
    }

    /**
     * 缴费记录
     */
    @PostMapping("getPayHistoryList")
    public Ajax getPayHistoryList(PayHistoryBean payHistoryBean, PageBean pageBean) {
        return Ajax.ok(institutionService.getPayHistoryList(payHistoryBean, pageBean), pageBean.getTotal());
    }

    /**
     * 学生准考证打印
     */
    @RequestMapping("getStudentPdf")
    public void getStudentPdf(StudentBean studentBean, HttpServletResponse response) {
        institutionService.getStudentPdf(studentBean, response);
    }

    /**
     * 通过机构账号获取绑定手机号
     */
    @PostMapping("getInstitutionPhoneByUsername")
    public Ajax getInstitutionPhoneByUsername(String username) {
        return Ajax.ok(institutionService.getInstitutionPhoneByUsername(username));
    }

    /**
     * 通过机构账号获取绑定手机号
     */
    @PostMapping("updateInstitutionPassword")
    public Ajax updateInstitutionPassword(InstitutionBean institutionBean, String code) {
        return Ajax.ok(institutionService.updateInstitutionPassword(institutionBean, code));
    }

    /**
     * 考生查询成绩
     */
    @PostMapping("queryResults")
    public Ajax queryResults(String search) {
        return Ajax.ok(institutionService.queryResults(search));
    }

    /**
     * 修改考生信息（不校验信息）
     */
    @PostMapping("updateStudentNotValidate")
    public Ajax updateStudentNotValidate(StudentBean studentBean) {
        if (institutionService.updateStudentNotValidate(studentBean) > 0) {
            return Ajax.ok("修改成功");
        } else {
            return Ajax.error("修改失败");
        }
    }
    /**
     * 考生个人报名
     */
    @PostMapping("studentSignUp")
    public Ajax studentSignUp(StudentBean studentBean,String code) {
        institutionService.studentSignUp(studentBean,code);
        return Ajax.ok("报名成功");
    }
}
