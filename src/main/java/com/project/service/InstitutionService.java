package com.project.service;

import com.project.bean.institution.*;
import com.project.bean.others.CodeBean;
import com.project.bean.others.ExcelBean;
import com.project.bean.others.ZipFile;
import com.project.bean.pay.PayHistoryBean;
import com.project.bean.system.SystemAccountBean;
import com.project.dao.DaoFactory;
import com.project.others.PageBean;
import com.project.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 机构
 */
@Service
public class InstitutionService {
    @Autowired
    DaoFactory daoFactory;
    @Autowired
    SystemService systemService;

    /**
     * 添加机构
     */
    public int insertInstitution(InstitutionBean institutionBean, SystemAccountBean systemAccountBean) {
        int num = systemService.insertSystemAccount(systemAccountBean.setSystem_type("institution"));
        if (num == 0) {
            throw new RuntimeException("账号分配失败");
        }
        num = daoFactory.institutionDao.insertSelective(institutionBean.setInstitution_no(OthersUtils.createRandom(6)).setAccount_id(systemAccountBean.getAccount_id()));
        if (num == 0) {
            throw new RuntimeException("机构添加失败");
        }
        num = daoFactory.systemAccountDao.updateByPrimaryKeySelective(new SystemAccountBean().setAccount_id(systemAccountBean.getAccount_id()).setInstitution_id(institutionBean.getInstitution_id()));
        if (num == 0) {
            throw new RuntimeException("机构账号信息关联失败");
        }
        return num;
    }

    /**
     * 删除机构
     */
    public int deleteInstitution(InstitutionBean institutionBean) {
        return daoFactory.institutionDao.deleteByPrimaryKey(institutionBean);
    }

    /**
     * 修改机构
     */
    public int updateInstitution(InstitutionBean institutionBean) {
        return daoFactory.institutionDao.updateByPrimaryKeySelective(institutionBean);
    }

    /**
     * 机构详情
     */
    public InstitutionBean getInstitutionDetail(InstitutionBean institutionBean) {
        return daoFactory.institutionDao.selectByPrimaryKey(institutionBean);
    }

    /**
     * 机构列表
     */
    public List<InstitutionBean> getInstitutionList(InstitutionBean institutionBean, PageBean pageBean) {
        return daoFactory.othersDao.getInstitutionList(institutionBean, pageBean);
    }

    /**
     * 考试科目列表
     */
    public List<ExamBean> getExamList(ExamBean examBean, PageBean pageBean) {
        Example example = new Example(ExamBean.class);
        example.setOrderByClause("create_time asc");
        if (!OthersUtils.isEmpty(examBean.getExam_name())) {
            example.createCriteria().andLike("exam_name", "%" + examBean.getExam_name() + "%");
        }
        return daoFactory.examDao.selectByExampleAndRowBounds(example, pageBean);
    }

    /**
     * 考试科目列表(去重)
     */
    public List<ExamBean> getExamListDistrict() {
        return daoFactory.othersDao.getExamListDistrict();
    }

    /**
     * 考试科目详情
     */
    public ExamBean getExamDetail(ExamBean examBean) {
        return daoFactory.examDao.selectByPrimaryKey(examBean);
    }

    /**
     * 修改考试科目
     */
    public int updateExam(ExamBean examBean) {
        return daoFactory.examDao.updateByPrimaryKeySelective(examBean);
    }

    /**
     * 删除考试科目
     */
    public int deleteExam(ExamBean examBean) {
        return daoFactory.examDao.deleteByPrimaryKey(examBean);
    }

    /**
     * 添加考试科目
     */
    public int insertExam(ExamBean examBean) {
        return daoFactory.examDao.insertSelective(examBean);
    }

    /**
     * 考生信息列表
     */
    public List<StudentBean> getStudentList(StudentBean studentBean, PageBean pageBean) {
        Example example = new Example(StudentBean.class);
        example.setOrderByClause("create_time desc");
        if (!OthersUtils.isEmpty(studentBean.getName())) {
            example.createCriteria().andLike("name", "%" + studentBean.getName() + "%");
        }
        if (!OthersUtils.isEmpty(studentBean.getId_card())) {
            example.createCriteria().andLike("id_card", "%" + studentBean.getId_card() + "%");
        }
        if (studentBean.getInstitution_id() != null) {
            example.createCriteria().andEqualTo("institution_id", studentBean.getInstitution_id());
        }
        if (!OthersUtils.isEmpty(studentBean.getPhone())) {
            example.createCriteria().andLike("phone", "%" + studentBean.getPhone() + "%");
        }
        return daoFactory.othersDao.getStudentList(studentBean, pageBean);
    }

    /**
     * 考生信息详情
     */
    public StudentBean getStudentDetail(StudentBean studentBean) {
        return daoFactory.othersDao.getStudentDetail(studentBean);
    }

    /**
     * 修改考生信息
     */
    public int updateStudent(StudentBean studentBean) {
        if ("0".equals(studentBean.getIs_validate())) {
            StudentBean studentBean1 = daoFactory.studentDao.selectByPrimaryKey(studentBean);
            if (!studentBean.getName().equals(studentBean1.getName()) || !studentBean.getId_card().equals(studentBean1.getId_card())) {
                studentBean.setPhoto(null);
            }
            this.validateStudentInfo(studentBean);
        } else {
            studentBean.setState("0");
        }
        return daoFactory.studentDao.updateByPrimaryKeySelective(studentBean);
    }

    /**
     * 删除考生信息
     */
    public int deleteStudent(StudentBean studentBean) {
        return daoFactory.othersDao.deleteStudent(studentBean);
    }

    /**
     * 添加考生信息
     */
    public int insertStudent(StudentBean studentBean) {
        this.validateStudentInfo(studentBean);
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(studentBean.getInstitution_id());
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        studentBean.setTicket_no(TimeUtils.getCurrentTime("yyyyMMdd") + OthersUtils.createRandom(5))
                .setBatch_no(institutionBean.getBatch_no());
        return daoFactory.studentDao.insertSelective(studentBean);
    }

    /**
     * 修改历史考生信息
     */
    public int updateStudentHistory(StudentHistoryBean studentHistoryBean) {
        String id_card = studentHistoryBean.getId_card();
        if (id_card.length() != 18) {
            throw new RuntimeException("身份证号长度18位");
        }
        studentHistoryBean.setId_card(id_card);
        //生日
        String birthday = id_card.substring(6, 14);
        studentHistoryBean.setBirthday(birthday);
        //年龄
        Integer year = Integer.valueOf(birthday.substring(0, 4));
        studentHistoryBean.setAge(Calendar.getInstance().get(Calendar.YEAR) - year);
        //性别
        if ((int) id_card.charAt(16) % 2 == 0) {
            studentHistoryBean.setSex("女");
        } else {
            studentHistoryBean.setSex("男");
        }
        return daoFactory.studentHistoryDao.updateByPrimaryKeySelective(studentHistoryBean);
    }

    /**
     * 验证考生信息是否合法
     */
    public boolean validateStudentInfo(StudentBean studentBean) {
        boolean validate = true;
        StringBuffer sb = new StringBuffer();
        String id_card = studentBean.getId_card();
        if (OthersUtils.isEmpty(id_card)) {
            validate = false;
            sb.append("身份证号不能为空/");
        } else {
            if (id_card.length() != 18) {
                validate = false;
                sb.append("身份证号长度必须18位/");
            }
            if (!IdCardUtils.isPass(id_card)) {
                validate = false;
                sb.append("身份证号不合法/");
            } else {
                studentBean.setId_card(id_card);
                //生日
                String birthday = id_card.substring(6, 14);
                studentBean.setBirthday(birthday);
                //年龄
                Integer year = Integer.valueOf(birthday.substring(0, 4));
                Integer age = Calendar.getInstance().get(Calendar.YEAR) - year;
                studentBean.setAge(age);
                //性别
                if ((int) id_card.charAt(16) % 2 == 0) {
                    studentBean.setSex("女");
                } else {
                    studentBean.setSex("男");
                }
            }
        }
        if (OthersUtils.isEmpty(studentBean.getProfession())) {
            validate = false;
            sb.append("报考专业不能为空/");
        } else {
            boolean have = false;
            List<ExamBean> examBeans = daoFactory.examDao.selectAll();
            for (ExamBean examBean : examBeans) {
                if (examBean.getExam_name().equals(studentBean.getProfession())) {
                    have = true;
                    break;
                }
            }
            if (!have) {
                validate = false;
                sb.append("报考专业不存在/");
            }
        }
        if (OthersUtils.isEmpty(studentBean.getPinyin())) {
            validate = false;
            sb.append("拼音不能为空/");
        }
        if (OthersUtils.isEmpty(studentBean.getNation())) {
            validate = false;
            sb.append("民族不能为空/");
        } else {
            if (!NationalUtils.validate(studentBean.getNation())) {
                validate = false;
                sb.append("民族输入有误/");
            }
        }
        if (OthersUtils.isEmpty(studentBean.getPhoto())) {
            validate = false;
            sb.append("照片不能为空/");
        }
        if (OthersUtils.isEmpty(studentBean.getLevel())) {
            validate = false;
            sb.append("报考级别不能为空/");
        }
        if (OthersUtils.isEmpty(studentBean.getName())) {
            validate = false;
            sb.append("姓名不能为空/");
        }
        if (!OthersUtils.isEmpty(studentBean.getId_card()) && !OthersUtils.isEmpty(studentBean.getLevel()) && !OthersUtils.isEmpty(studentBean.getProfession()) && studentBean.getAge() != null) {
            Integer maxLevel = daoFactory.othersDao.getStudentMaxLevel(new StudentBean().setId_card(studentBean.getId_card()).setProfession(studentBean.getProfession()));
            if (studentBean.getAge() < 15) {
                if ( "儿童画,速写".contains(studentBean.getProfession())) {
                    if(studentBean.getLevel()>2){
                        validate = false;
                        sb.append("首次报考").append(studentBean.getProfession()).append("最高报考2级/");
                    }
                }else {
                    if(studentBean.getLevel()>5){
                        validate = false;
                        sb.append("首次报考").append(studentBean.getProfession()).append("最高报考5级/");
                    }
                }
            } else {
                if (maxLevel != null && maxLevel != 0) {
                    if (studentBean.getLevel() <= maxLevel) {
                        validate = false;
                        sb.append("已通过").append(studentBean.getProfession()).append(maxLevel).append("级/");
                    }
                    if (studentBean.getLevel() - maxLevel > 1) {
                        validate = false;
                        sb.append("已通过").append(studentBean.getProfession()).append(maxLevel).append("级，只能报考").append(maxLevel + 1).append("级/");
                    }
                }
            }
        }
        try {
            if (!OthersUtils.isEmpty(studentBean.getId_card()) && !OthersUtils.isEmpty(studentBean.getProfession())) {
                //是否已报考该科目
                List<StudentBean> studentBeans = daoFactory.studentDao.select(new StudentBean().setId_card(studentBean.getId_card()).setProfession(studentBean.getProfession()));
                if (studentBean.getStudent_id() == null) {
                    if (studentBeans.size() > 0) {
                        validate = false;
                        sb.append("已报考").append(studentBean.getProfession()).append("/");
                    }
                } else {
                    if (studentBeans.size() > 0) {
                        for (StudentBean studentBean1 : studentBeans) {
                            if (!studentBean1.getStudent_id().equals(studentBean.getStudent_id())) {
                                validate = false;
                                sb.append("已报考").append(studentBean.getProfession()).append("/");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            validate = false;
            sb.append("已报考").append(studentBean.getProfession()).append("/");
            e.printStackTrace();
        }
        studentBean.setIs_validate(validate ? "1" : "0").setState(validate ? "0" : studentBean.getState());
        studentBean.setError_info(validate ? "" : sb.toString());
        return validate;
    }

    /**
     * 导入考生信息
     */
    @Transactional
    public int importStudent(String file_path, Integer institution_id) {
        int num = 0;
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(institution_id);
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        List<Map<String, String>> mapList = ExcelUtils.importExcel(OthersUtils.getFileSaveParentPath() + file_path);
        if (mapList == null || mapList.size() == 0) {
            return 0;
        } else {
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, String> map = mapList.get(i);
                StudentBean studentBean = new StudentBean();
                studentBean.setId_card(map.get("身份证号码"))
                        .setName(map.get("姓名"))
                        .setPinyin(map.get("拼音"))
                        .setNation(map.get("民族"))
                        .setProfession(map.get("报考专业"))
                        .setLevel(OthersUtils.isEmpty(map.get("报考级别")) ? null : Integer.valueOf(map.get("报考级别")))
                        .setPhone(map.get("电话"))
                        .setAddress(map.get("考场地点"))
                        .setEmail(map.get("邮箱号码"))
                        .setExam_no(map.get("考试代码"))
                        .setCountry(map.get("国籍"))
                        .setZip_code(map.get("邮编"))
                        .setInstitution_id(institution_id)
                        .setBatch_no(institutionBean.getBatch_no())
                        .setNote(map.get("备注"));
                if (OthersUtils.isEmpty(studentBean.getId_card()) && OthersUtils.isEmpty(studentBean.getName())) {
                    continue;
                }
                List<StudentBean> studentBeans = daoFactory.studentDao.select(new StudentBean().setId_card(studentBean.getId_card())
                        .setName(studentBean.getName())
                        .setProfession(studentBean.getProfession())
                        .setLevel(studentBean.getLevel())
                        .setInstitution_id(studentBean.getInstitution_id()));
                if (studentBeans != null && studentBeans.size() > 0) {
                    for (StudentBean studentBean1 : studentBeans) {
                        daoFactory.studentDao.deleteByPrimaryKey(studentBean1);
                        studentBean.setStudent_id(studentBean1.getStudent_id()).setPhoto(studentBean1.getPhoto());
                    }
                }
                this.validateStudentInfo(studentBean);
                daoFactory.studentDao.insertSelective(studentBean);
            }
        }
        return 1;
    }

    /**
     * 批量修改考生图片
     */
    public boolean updateStudentPhotoMany(HttpServletRequest request) {
        //存放表单类型数据
        HashMap<String, String> mapString = new HashMap<>();
        List<StudentBean> studentBeans = new LinkedList<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        //获取表单的name值列表
        Enumeration<String> parameterNames = params.getParameterNames();
        //遍历name值列表，取出对应的值放到表单map中
        for (; parameterNames.hasMoreElements(); ) {
            String key = parameterNames.nextElement();
            mapString.put(key, params.getParameter(key));
        }
        //存放文件列表
        List<MultipartFile> files = new LinkedList<>();
        //获取文件的name值列表
        Iterator<String> fileNames = params.getFileNames();
        //遍历列表，取出name对应的文件列表合并到总文件列表
        for (; fileNames.hasNext(); ) {
            String key = fileNames.next();
            List<MultipartFile> files1 = ((MultipartHttpServletRequest) request).getFiles(key);
            if (files1.size() > 0) {
                files.addAll(files1);
            }
        }
        //文件保存默认位置
        String path = OthersUtils.setDefaultPath(params);
        path += "/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/";
        MultipartFile file;
        BufferedOutputStream stream = null;
        //遍历文件总表，将文件储存到本地
        for (MultipartFile file1 : files) {
            file = file1;
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String fileName = String.valueOf(System.currentTimeMillis())
                            + String.valueOf(new Random().nextInt(Integer.MAX_VALUE))
                            + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    String basePath = OthersUtils.getFileSaveParentPath();
                    File f = new File(basePath + path);
                    if (!f.exists()) {
                        boolean success = f.mkdirs();
                        if (!success) {
                            throw new RuntimeException("创建文件或目录失败");
                        }
                    }
                    stream = new BufferedOutputStream(new FileOutputStream(new File(basePath + path + fileName)));
                    stream.write(bytes);
                    stream.flush();
                    String file_name = file.getOriginalFilename().replace("＋", "+");
                    String name = file_name.substring(file_name.lastIndexOf("\\") + 1, file_name.indexOf("+"));
                    String id_card = file_name.substring(file_name.indexOf("+") + 1, file_name.indexOf("."));
                    StudentBean studentBean = new StudentBean().setName(name).setId_card(id_card).setPhoto(path + fileName);
                    studentBeans.add(studentBean);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
        //更新数据库考生图片信息
        for (StudentBean studentBean : studentBeans) {
            List<StudentBean> studentBeans1 = daoFactory.studentDao.select(new StudentBean().setId_card(studentBean.getId_card()));
            if (studentBeans1.size() > 0) {
                for (StudentBean studentBean1 : studentBeans1) {
                    if (OthersUtils.isEmpty(studentBean.getName()) || OthersUtils.isEmpty(studentBean1.getName())) {
                        continue;
                    }
                    if (studentBean.getName().equals(studentBean1.getName())) {
                        this.validateStudentInfo(studentBean1.setPhoto(studentBean.getPhoto()));
                        daoFactory.studentDao.updateByPrimaryKeySelective(new StudentBean().setStudent_id(studentBean1.getStudent_id()).setPhoto(studentBean.getPhoto()).setIs_validate(studentBean1.getIs_validate()).setError_info(studentBean1.getError_info()));
                    } else {
                        String error_info = (studentBean1.getError_info() == null ? "" : studentBean1.getError_info()) + "照片姓名不匹配/";
                        daoFactory.studentDao.updateByPrimaryKeySelective(new StudentBean().setStudent_id(studentBean1.getStudent_id()).setError_info(error_info).setIs_validate("0"));
                    }
                }
            }
        }
        return true;
    }

    /**
     * 删除历史考生信息
     */
    public int deleteStudentHistory(String student_ids) {
        return daoFactory.othersDao.deleteStudentHistory(student_ids);
    }

    /**
     * 历史考生信息列表
     */
    public List<StudentHistoryBean> getStudentHistoryList(StudentHistoryBean studentHistoryBean, PageBean pageBean) {
        return daoFactory.othersDao.getStudentHistoryList(studentHistoryBean, pageBean);
    }

    /**
     * 导出考生信息
     */
    public String exportStudent(StudentBean studentBean) {
        List<ExcelBean> excelBeans = new LinkedList<>();
        excelBeans.add(new ExcelBean("ID", "student_id"));
        excelBeans.add(new ExcelBean("姓名", "name"));
        excelBeans.add(new ExcelBean("拼音", "pinyin"));
        excelBeans.add(new ExcelBean("身份证号", "id_card"));
        excelBeans.add(new ExcelBean("性别", "sex"));
        excelBeans.add(new ExcelBean("出生日期", "birthday"));
        excelBeans.add(new ExcelBean("专业", "profession"));
        excelBeans.add(new ExcelBean("级别", "level"));
        excelBeans.add(new ExcelBean("民族", "nation"));
        excelBeans.add(new ExcelBean("年龄", "age"));
        excelBeans.add(new ExcelBean("机构名称", "institution_name"));
        excelBeans.add(new ExcelBean("电话", "phone"));
        excelBeans.add(new ExcelBean("报考时间", "create_time"));
        excelBeans.add(new ExcelBean("国籍", "country"));
        excelBeans.add(new ExcelBean("是否分配考场", "is_allot_show"));
        excelBeans.add(new ExcelBean("是否缴费", "is_pay_show"));
        excelBeans.add(new ExcelBean("准考证号码", "ticket_no"));
        excelBeans.add(new ExcelBean("考场名称", "room_name"));
        excelBeans.add(new ExcelBean("考试时间", "start_time"));
        excelBeans.add(new ExcelBean("考试地点", "exam_address"));
        excelBeans.add(new ExcelBean("地址", "address"));
        excelBeans.add(new ExcelBean("邮箱", "email"));
        excelBeans.add(new ExcelBean("邮编", "zip_code"));
        excelBeans.add(new ExcelBean("考试代码", "exam_no"));
        excelBeans.add(new ExcelBean("备注", "note"));
        List<StudentBean> studentBeans = daoFactory.othersDao.getStudentList(studentBean, new PageBean(1, 100000000));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat chineseStartFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        SimpleDateFormat chineseEndFormat = new SimpleDateFormat("HH:mm");
        try {
            for (StudentBean studentBean1 : studentBeans) {
                if (!OthersUtils.isEmpty(studentBean1.getStart_time()) && !OthersUtils.isEmpty(studentBean1.getEnd_time())) {
                    studentBean1.setStart_time(chineseStartFormat.format(format.parse(studentBean1.getStart_time())) + "-" + chineseEndFormat.format(format.parse(studentBean1.getEnd_time())));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String path = "/excel/student/export/";
        String file_name = System.currentTimeMillis() + ".xls";
        if (ExcelUtils.exportExcel(path, file_name, excelBeans, studentBeans)) {
            return path + file_name;
        } else {
            return null;
        }
    }

    /**
     * 导出验证失败考生信息
     */
    public String exportErrorStudent(StudentBean studentBean) {
        List<ExcelBean> excelBeans = new LinkedList<>();
        excelBeans.add(new ExcelBean("ID", "student_id"));
        excelBeans.add(new ExcelBean("姓名", "name"));
        excelBeans.add(new ExcelBean("拼音", "pinyin"));
        excelBeans.add(new ExcelBean("身份证号码", "id_card"));
        excelBeans.add(new ExcelBean("电话", "phone"));
        excelBeans.add(new ExcelBean("考场地点", "address"));
        excelBeans.add(new ExcelBean("性别", "sex"));
        excelBeans.add(new ExcelBean("年龄", "age"));
        excelBeans.add(new ExcelBean("邮箱号码", "email"));
        excelBeans.add(new ExcelBean("出生日期", "birthday"));
        excelBeans.add(new ExcelBean("报考专业", "profession"));
        excelBeans.add(new ExcelBean("邮编", "zip_code"));
        excelBeans.add(new ExcelBean("报考级别", "level"));
        excelBeans.add(new ExcelBean("民族", "nation"));
        excelBeans.add(new ExcelBean("机构", "institution_name"));
        excelBeans.add(new ExcelBean("考试代码", "exam_no"));
        excelBeans.add(new ExcelBean("报考时间", "create_time"));
        excelBeans.add(new ExcelBean("国籍", "country"));
        excelBeans.add(new ExcelBean("状态", "state_show"));
        excelBeans.add(new ExcelBean("是否分配考场", "is_allot_show"));
        excelBeans.add(new ExcelBean("是否缴费", "is_pay_show"));
        excelBeans.add(new ExcelBean("错误信息", "error_info"));
        studentBean.setIs_validate("0");
        List<StudentBean> studentBeans = daoFactory.othersDao.getStudentList(studentBean, new PageBean(1, 100000000));
        String path = "/excel/student/export/";
        String file_name = System.currentTimeMillis() + ".xls";
        if (ExcelUtils.exportExcel(path, file_name, excelBeans, studentBeans)) {
            return path + file_name;
        } else {
            return null;
        }
    }

    /**
     * 考生缴费
     */
    public int payStudent(InstitutionBean institutionBean) {
        InstitutionBean institutionBean1 = daoFactory.institutionDao.selectByPrimaryKey(institutionBean);
        if (institutionBean1 == null) {
            throw new RuntimeException("机构不存在");
        }
        SystemAccountBean systemAccountBean = daoFactory.systemAccountDao.selectByPrimaryKey(institutionBean1.getAccount_id());
        if (systemAccountBean == null) {
            throw new RuntimeException("账号不存在");
        }
        if (systemAccountBean.getIs_apply_pay().equals("1")) {
            throw new RuntimeException("已申请缴费，请等待后台审核");
        }
        List<StudentBean> notPassStudentBeans = daoFactory.studentDao.select(new StudentBean().setInstitution_id(institutionBean.getInstitution_id())
                .setIs_pay("0").setIs_validate("0"));
        if (notPassStudentBeans.size() > 0) {
            throw new RuntimeException("考生信息未校验通过，请修改考生信息，或者删除该考生信息");
        }
        if (daoFactory.othersDao.getNoPhotoStudentCount(institutionBean.getInstitution_id()) > 0) {
            throw new RuntimeException("考生照片未全部上传，请补全照片，或者删除该考生信息");
        }
        List<StudentBean> studentBeans = daoFactory.othersDao.getWaitPayStudentList(institutionBean);
        if (studentBeans.size() == 0) {
            throw new RuntimeException("没有可缴费学生");
        }
        Map<String, Map<Integer, Float>> examPrice = this.getExamPriceTree();
        StringBuilder final_ids = new StringBuilder();
        Float sum_price = 0f;
        for (StudentBean studentBean : studentBeans) {
            Map<Integer, Float> priceMap = examPrice.get(studentBean.getProfession());
            if (priceMap == null) {
                throw new RuntimeException("学生【" + studentBean.getName() + "】科目【" + studentBean.getProfession() + "】价格未配置");
            } else {
                if (priceMap.get(studentBean.getLevel()) == null) {
                    throw new RuntimeException("学生【" + studentBean.getName() + "】科目【" + studentBean.getProfession() + "】级别【" + studentBean.getLevel() + "】价格未配置");
                }
            }
            Float student_price = priceMap.get(studentBean.getLevel());
            sum_price += student_price;
            final_ids.append(studentBean.getStudent_id()).append(",");
        }
        if (final_ids.length() > 0) {
            final_ids.deleteCharAt(final_ids.length() - 1);
        }
        PayHistoryBean payHistoryBean = new PayHistoryBean()
                .setPay_amount(sum_price)
                .setInstitution_id(institutionBean1.getInstitution_id())
                .setOrder_no(OthersUtils.createRandom(10))
                .setExtra_data(final_ids.toString())
                .setPay_state("wait_audit")
                .setPay_type("pay_student");
        int num = daoFactory.payHistoryDao.insertSelective(payHistoryBean);
        if (num == 0) {
            throw new RuntimeException("付款申请失败");
        }
        num = daoFactory.systemAccountDao.updateByPrimaryKeySelective(new SystemAccountBean().setAccount_id(institutionBean1.getAccount_id()).setIs_apply_pay("1"));
        if (num == 0) {
            throw new RuntimeException("修改缴费状态失败");
        }
        num = daoFactory.othersDao.updateStudentPayStateMany(new StudentBean().setStudent_ids(final_ids.toString()).setIs_pay("-1"));
        return num;
    }

    /**
     * 科目价格
     */
    public Map<String, Map<Integer, Float>> getExamPriceTree() {
        List<ExamBean> examBeans = daoFactory.examDao.selectAll();
        Map<String, Map<Integer, Float>> nameMap = new HashMap<>();
        for (ExamBean examBean : examBeans) {
            if (nameMap.get(examBean.getExam_name()) == null) {
                Map<Integer, Float> priceMap = new HashMap<>();
                priceMap.put(examBean.getLevel(), examBean.getExam_price());
                nameMap.put(examBean.getExam_name(), priceMap);
            } else {
                Map<Integer, Float> priceMap = nameMap.get(examBean.getExam_name());
                priceMap.put(examBean.getLevel(), examBean.getExam_price());
            }
        }
        return nameMap;
    }

    /**
     * 考场列表
     */
    public List<ExamRoomBean> getExamRoomList(ExamRoomBean examRoomBean, PageBean pageBean) {
        return daoFactory.othersDao.getExamRoomList(examRoomBean, pageBean);
    }

    /**
     * 修改考场
     */
    public int updateExamRoom(ExamRoomBean examRoomBean) {
        return daoFactory.examRoomDao.updateByPrimaryKeySelective(examRoomBean);
    }

    /**
     * 删除考场
     */
    public int deleteExamRoom(ExamRoomBean examRoomBean) {
        return daoFactory.examRoomDao.deleteByPrimaryKey(examRoomBean);
    }

    /**
     * 添加考场
     */
    public int insertExamRoom(ExamRoomBean examRoomBean) {
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(new InstitutionBean().setInstitution_id(examRoomBean.getInstitution_id()));
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        examRoomBean.setRoom_no(TimeUtils.getCurrentTime("yyyyMMdd") + OthersUtils.createRandom(5)).setBatch_no(institutionBean.getBatch_no());
        return daoFactory.examRoomDao.insertSelective(examRoomBean);
    }

    /**
     * 分配考场
     */
    @Transactional
    public int allotExamRoom(ExamRoomBean examRoomBean) {
        Map<String, String> result = new LinkedHashMap<>();
        String institution_student_ids = daoFactory.othersDao.getInstitutionStudentIds(new StudentBean().setStudent_ids(examRoomBean.getStudent_ids()).setInstitution_id(examRoomBean.getInstitution_id()));
        if (OthersUtils.isEmpty(institution_student_ids)) {
            throw new RuntimeException("请先缴费，缴费后再进行排考安排！");
        }
        int num = daoFactory.examRoomDao.updateByPrimaryKeySelective(examRoomBean);
        if (num == 0) {
            throw new RuntimeException("修改考场信息失败");
        }
        ExamRoomBean examRoomBean1 = daoFactory.examRoomDao.selectByPrimaryKey(examRoomBean);
        if (examRoomBean1 == null) {
            throw new RuntimeException("考场不存在");
        }
        String date = examRoomBean1.getStart_time().replace("-", "").substring(0, 8);
        for (String student_id : institution_student_ids.split(",")) {
            String maxNo = daoFactory.othersDao.getMaxTicketNoByDate(date);
            if (OthersUtils.isEmpty(maxNo)) {
                maxNo = date + "00001";
            } else {
                String nowNo = String.valueOf(Integer.valueOf(maxNo.substring(8)) + 1);
                while (nowNo.length() < 5) {
                    nowNo = "0" + nowNo;
                }
                maxNo = date + nowNo;
            }
            num = daoFactory.studentDao.updateByPrimaryKeySelective(new StudentBean().setStudent_id(OthersUtils.toInteger(student_id)).setIs_allot("1").setRoom_id(examRoomBean.getRoom_id()).setTicket_no(maxNo));
            if (num == 0) {
                throw new RuntimeException("修改考生排考状态失败");
            }
        }
        return num;
    }

    /**
     * 机构级别列表
     */
    public List<InstitutionLevelBean> getInstitutionLevelList(PageBean pageBean) {
        return daoFactory.institutionLevelDao.selectByRowBounds(null, pageBean);
    }

    /**
     * 修改机构级别
     */
    public int updateInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        InstitutionLevelBean institutionLevelBean1 = daoFactory.institutionLevelDao.selectOne(new InstitutionLevelBean().setLevel_name(institutionLevelBean.getLevel_name()));
        if (institutionLevelBean1 != null && institutionLevelBean.getLevel_id() != institutionLevelBean1.getLevel_id()) {
            throw new RuntimeException("该级别已存在");
        }
        return daoFactory.institutionLevelDao.updateByPrimaryKeySelective(institutionLevelBean);
    }

    /**
     * 删除机构级别
     */
    public int deleteInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        int count = daoFactory.institutionDao.selectCount(new InstitutionBean().setLevel_id(institutionLevelBean.getLevel_id()));
        if (count > 0) {
            throw new RuntimeException("该级别正在使用中，无法删除");
        }
        return daoFactory.institutionLevelDao.deleteByPrimaryKey(institutionLevelBean);
    }

    /**
     * 添加机构级别
     */
    public int insertInstitutionLevel(InstitutionLevelBean institutionLevelBean) {
        List<InstitutionLevelBean> institutionLevelBeans = daoFactory.institutionLevelDao.select(new InstitutionLevelBean().setLevel_name(institutionLevelBean.getLevel_name()));
        if (institutionLevelBeans.size() > 0) {
            throw new RuntimeException("该级别已存在");
        }
        return daoFactory.institutionLevelDao.insertSelective(institutionLevelBean);
    }

    /**
     * 添加或修改区域
     */
    public int insertOrUpdateArea(AreaBean areaBean) {
        if (areaBean.getArea_id() == null) {
            return daoFactory.areaDao.insertSelective(areaBean);
        } else {
            return daoFactory.areaDao.updateByPrimaryKeySelective(areaBean);
        }
    }

    /**
     * 删除区域
     */
    public int deleteArea(AreaBean areaBean) {
        return daoFactory.areaDao.deleteByPrimaryKey(areaBean);
    }

    /**
     * 区域列表
     */
    public List<AreaBean> getAreaList(AreaBean areaBean, PageBean pageBean) {
        return daoFactory.areaDao.selectByRowBounds(areaBean, pageBean);
    }

    /**
     * 区域树
     */
    public List<AreaBean> getAreaTree() {
        return daoFactory.othersDao.getAreaTree();
    }

    /**
     * 机构待分配考场人数
     */
    public Integer getWaitAllotStudentCount(Integer institution_id) {
        return daoFactory.othersDao.getWaitAllotStudentCount(institution_id);
    }

    /**
     * 删除报名申请
     */
    public int deleteSignUpApplyMany(SignUpApplyBean signUpApplyBean) {
        return daoFactory.othersDao.deleteSignUpApplyMany(signUpApplyBean);
    }

    /**
     * 添加报名申请
     */
    public int insertSignUpApply(SignUpApplyBean signUpApplyBean) {
        SignUpApplyBean signUpApplyBean1 = daoFactory.othersDao.institutionIsSignUp(signUpApplyBean.getInstitution_id());
        if (signUpApplyBean1 != null) {
            if (signUpApplyBean1.getState().equals("wait_audit")) {
                throw new RuntimeException("已经提交过申请，请等待平台审核");
            } else {
                throw new RuntimeException("本次报名未结束，不能重复申请");
            }
        }
        String nowNo = daoFactory.othersDao.getMaxBatchNo();
        String date = TimeUtils.getCurrentTime("yyyyMMdd");
        if (OthersUtils.isEmpty(nowNo)) {
            nowNo = date + "001";
        } else {
            String start = nowNo.substring(0, 8);
            String end = nowNo.substring(8);
            if (start.equals(date)) {
                end = String.valueOf(Integer.valueOf(end) + 1);
            } else {
                end = String.valueOf("001");
            }
            while (end.length() < 3) {
                end = "0" + end;
            }
            nowNo = date + end;
        }
        int num = daoFactory.institutionDao.updateByPrimaryKeySelective(new InstitutionBean().setInstitution_id(signUpApplyBean.getInstitution_id()).setBatch_no(nowNo));
        if (num == 0) {
            throw new RuntimeException("分配报考批次编号失败");
        }
        return daoFactory.signUpApplyDao.insertSelective(signUpApplyBean.setBatch_no(nowNo));
    }

    /**
     * 审核报名申请
     */
    public int auditSignUpApply(SignUpApplyBean signUpApplyBean) {
        int num = 0;
        if (OthersUtils.isEmpty(signUpApplyBean.getIds())) {
            throw new RuntimeException("未选择申请记录");
        }
        for (String id : signUpApplyBean.getIds().split(",")) {
            SignUpApplyBean signUpApplyBean1 = daoFactory.signUpApplyDao.selectByPrimaryKey(id);
            if (signUpApplyBean1 == null) {
                throw new RuntimeException("申请记录不存在");
            }
            if (signUpApplyBean1.getState().equals("accept")) {
                continue;
            }
            InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(signUpApplyBean1.getInstitution_id());
            if (institutionBean == null) {
                throw new RuntimeException("机构不存在");
            }
            num = daoFactory.systemAccountDao.updateByPrimaryKeySelective(new SystemAccountBean().setAccount_id(institutionBean.getAccount_id()).setIs_apply_pay("0"));
            if (num == 0) {
                throw new RuntimeException("修改机构报名申请状态失败");
            }
            num = daoFactory.othersDao.auditSignUpApply(signUpApplyBean.setUpdate_time(TimeUtils.getCurrentTime()));
            if (num == 0) {
                throw new RuntimeException("审核报名申请失败");
            }
            if ("accept".equals(signUpApplyBean.getState())) {
                String content = "【中美贵州考级办】尊敬的" +
                        institutionBean.getInstitution_name() +
                        "，您的开考申请已经通过审核，开考报名时间为：" +
                        signUpApplyBean1.getStart_time() +
                        "-" + signUpApplyBean1.getEnd_time() + "，请及时报名！";
                SmsUtils.sms(institutionBean.getPhone(), content);
            }
        }
        return num;
    }

    /**
     * 报名申请列表
     */
    public List<SignUpApplyBean> getSignUpApplyList(SignUpApplyBean signUpApplyBean, PageBean pageBean) {
        return daoFactory.othersDao.getSignUpApplyList(signUpApplyBean, pageBean);
    }

    /**
     * 缴费记录
     */
    public List<PayHistoryBean> getPayHistoryList(PayHistoryBean payHistoryBean, PageBean pageBean) {
        return daoFactory.othersDao.getPayHistoryList(payHistoryBean, pageBean);
    }

    /**
     * 导入错误学生信息
     */
    public int importErrorStudent(String file_path, Integer institution_id) {
        int num = 0;
        Map<String, Map<Integer, Float>> examTree = this.getExamPriceTree();
        List<Map<String, String>> mapList = ExcelUtils.importExcel(OthersUtils.getFileSaveParentPath() + file_path);
        if (mapList == null || mapList.size() == 0) {
            return 0;
        } else {
            Integer nowYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, String> map = mapList.get(i);
                if (OthersUtils.isEmpty(map.get("ID"))) {
                    continue;
                }
                Example example = new Example(StudentBean.class);
                StudentBean studentBean = new StudentBean();
                studentBean.setId_card(map.get("身份证号码"))
                        .setStudent_id(Integer.valueOf(map.get("ID")))
                        .setName(map.get("姓名"))
                        .setPinyin(map.get("拼音"))
                        .setNation(map.get("民族"))
                        .setProfession(map.get("报考专业"))
                        .setLevel(OthersUtils.isEmpty(map.get("报考级别")) ? null : Integer.valueOf(map.get("报考级别")))
                        .setPhone(map.get("电话"))
                        .setAddress(map.get("考场地点"))
                        .setEmail(map.get("邮箱号码"))
                        .setExam_no(map.get("考试代码"))
                        .setCountry(map.get("国籍"))
                        .setZip_code(map.get("邮编"))
                        .setInstitution_id(institution_id);
                if (OthersUtils.isEmpty(studentBean.getId_card()) && OthersUtils.isEmpty(studentBean.getName())) {
                    continue;
                }
                this.validateStudentInfo(studentBean);
                studentBean.setPhoto(null);
                daoFactory.studentDao.updateByPrimaryKeySelective(studentBean);
            }
            return 1;
        }
    }

    /**
     * 学生准考证
     */
    public String getStudentPdf(StudentBean studentBean, HttpServletResponse response) {
        List<StudentBean> studentBeans = daoFactory.othersDao.getPdfStudentList(studentBean);
        if (studentBeans.size() == 0) {
            throw new RuntimeException("没有可打印PDF的学生");
        }
        for (StudentBean studentBean1 : studentBeans) {
            if (!OthersUtils.isEmpty(studentBean1.getAddress()) && studentBean1.getAddress().length() > 15) {
                studentBean1.setAddress(studentBean1.getAddress().substring(0, 15));
            }
            if (!OthersUtils.isEmpty(studentBean1.getRoom_no()) && studentBean1.getRoom_no().length() > 15) {
                studentBean1.setRoom_no(studentBean1.getRoom_no().substring(0, 15));
            }
        }
        String fileName = "/pdf/student/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/" + OthersUtils.createRandom(10) + ".pdf";
        PdfUtils.createStudentPdfMany("/pdf/student_template.pdf", studentBeans, response);
        return fileName;
    }

    /**
     * 审核缴费申请
     */
    public int auditPayApply(PayHistoryBean payHistoryBean) {
        PayHistoryBean payHistoryBean1 = daoFactory.payHistoryDao.selectByPrimaryKey(payHistoryBean);
        if (payHistoryBean1 == null) {
            throw new RuntimeException("缴费申请不存在");
        }
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(payHistoryBean1.getInstitution_id());
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        int num = payHistoryBean1.getExtra_data().split(",").length;
        payHistoryBean.setSum_less_price(payHistoryBean.getLess_price() * num)
                .setOld_price(payHistoryBean1.getPay_amount())
                .setPay_state("pending");
        Float final_price = payHistoryBean1.getPay_amount() - payHistoryBean.getSum_less_price();
        payHistoryBean.setPay_amount(final_price < 0 ? 0 : final_price);
        num = daoFactory.payHistoryDao.updateByPrimaryKeySelective(payHistoryBean);
        if (num == 0) {
            throw new RuntimeException("审核缴费申请失败");
        }
        String content = "【中美贵州考级办】尊敬的" +
                institutionBean.getInstitution_name() +
                "，您的缴费申请已经审核，共需缴费" + payHistoryBean.getPay_amount() + "元，请于3日内缴费，谢谢！";
        SmsUtils.sms(institutionBean.getPhone(), content);
        return num;
    }

    /**
     * 手动设置缴费成功
     */
    public int setPaySuccess(PayHistoryBean payHistoryBean) {
        PayHistoryBean payHistoryBean1 = daoFactory.payHistoryDao.selectByPrimaryKey(payHistoryBean);
        if (payHistoryBean1 == null) {
            throw new RuntimeException("缴费申请不存在");
        }
        int num = daoFactory.payHistoryDao.updateByPrimaryKeySelective(payHistoryBean.setPay_state("ok").setSuccess_time(TimeUtils.getCurrentTime()));
        if (num == 0) {
            throw new RuntimeException("修改缴费状态失败");
        }
        num = daoFactory.othersDao.updateStudentPayStateMany(new StudentBean().setStudent_ids(payHistoryBean1.getExtra_data()).setIs_pay("1"));
        if (num == 0) {
            throw new RuntimeException("修改学生支付状态失败");
        }
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(payHistoryBean1.getInstitution_id());
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        String content = "【中美贵州考级办】尊敬的" + institutionBean.getInstitution_name() + "，您的缴费已经收到，请及时打印准考证安排考试！";
        SmsUtils.sms(institutionBean.getPhone(), content);
        return num;
    }

    /**
     * 导出学生照片
     */
    public String exportStudentPhoto(InstitutionBean institutionBean) {
        List<StudentBean> studentBeans = daoFactory.studentDao.select(new StudentBean().setBatch_no(institutionBean.getBatch_no()));
        List<ZipFile> zipFiles = new LinkedList<>();
        for (StudentBean studentBean : studentBeans) {
            if (!OthersUtils.isEmpty(studentBean.getPhoto())) {
                String file_type = studentBean.getPhoto().substring(studentBean.getPhoto().lastIndexOf("."));
                zipFiles.add(new ZipFile().setName(studentBean.getName() + "+" + studentBean.getId_card() + file_type).setPath(studentBean.getPhoto()));
            }
        }
        String zipFile = "/zip/student/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/" + institutionBean.getBatch_no() + ".zip";
        OthersUtils.zipFiles(zipFiles, zipFile);
        return zipFile;
    }

    /**
     * 可排考考场列表
     */
    public List<ExamRoomBean> getAllotExamRoomList(ExamRoomBean examRoomBean) {
        InstitutionBean institutionBean = daoFactory.institutionDao.selectByPrimaryKey(examRoomBean.getInstitution_id());
        if (institutionBean == null) {
            throw new RuntimeException("机构不存在");
        }
        Example example = new Example(ExamRoomBean.class);
        example.createCriteria()
                .andGreaterThanOrEqualTo("end_time", TimeUtils.getCurrentTime())
                .andEqualTo("institution_id", examRoomBean.getInstitution_id())
                .andEqualTo("batch_no", institutionBean.getBatch_no());
        return daoFactory.examRoomDao.selectByExample(example);
    }

    /**
     * 考场已分配考生信息
     */
    public List<StudentBean> getExamRoomStudentList(ExamRoomBean examRoomBean) {
        return daoFactory.othersDao.getExamRoomStudentList(examRoomBean);
    }

    /**
     * 修改缴费支付方式
     */
    public int changePayApplyPayWay(PayHistoryBean payHistoryBean) {
        return daoFactory.payHistoryDao.updateByPrimaryKeySelective(payHistoryBean);
    }

    /**
     * 批次号列表
     */
    public List<Map<String, String>> getStudentBatchNoList(Map<String, String> params) {
        return daoFactory.othersDao.getStudentBatchNoList(params);
    }

    /**
     * 批量导入考上成绩
     */
    public int importStudentScore(String file_path) {
        List<StudentHistoryBean> studentHistoryBeans = new LinkedList<>();
        List<Map<String, String>> mapList = ExcelUtils.importExcel(OthersUtils.getFileSaveParentPath() + file_path);
        if (mapList == null || mapList.size() == 0) {
            return 0;
        } else {
            for (Map<String, String> map : mapList) {
                StudentHistoryBean studentHistoryBean = new StudentHistoryBean();
                studentHistoryBean.setTicket_no(map.get("准考证号")).setPass_level(Integer.valueOf(map.get("通过级别")))
                        .setCertificate_no(map.get("证书编号"));
                studentHistoryBeans.add(studentHistoryBean);
            }
            if (studentHistoryBeans.size() == 0) {
                return 1;
            } else {
                daoFactory.othersDao.importStudentScore(studentHistoryBeans);
            }
        }
        return studentHistoryBeans.size();
    }

    /**
     * 科目列表
     */
    public String getDistinctProfessionList(HashMap<String, Object> params) {
        return daoFactory.othersDao.getDistinctProfessionList(params);
    }

    /**
     * 民族列表
     */
    public String getDistinctNationList(HashMap<String, Object> params) {
        return daoFactory.othersDao.getDistinctNationList(params);
    }

    /**
     * 通过机构账号获取绑定手机号
     */
    public Map<String, String> getInstitutionPhoneByUsername(String username) {
        return daoFactory.othersDao.getInstitutionPhoneByUsername(username);
    }

    /**
     * 机构修改密码
     */
    public int updateInstitutionPassword(InstitutionBean institutionBean, String code) {
        List<CodeBean> codeBeans = daoFactory.codeDao.select(new CodeBean().setMobile(institutionBean.getPhone()).setCode_type("institution_forget_password").setCode(code));
        if (codeBeans.size() == 0) {
            throw new RuntimeException("验证码错误");
        }
        int num = daoFactory.systemAccountDao.updateByPrimaryKeySelective(new SystemAccountBean().setAccount_id(institutionBean.getAccount_id()).setPassword(EncodeUtils.MD5Encode(institutionBean.getPassword())));
        if (num == 0) {
            throw new RuntimeException("密码修改失败");
        }
        num = daoFactory.codeDao.deleteByPrimaryKey(codeBeans.get(0));
        if (num == 0) {
            throw new RuntimeException("验证码删除失败");
        }
        return num;
    }

    public int importStudentHistory(String file_path) {
        int num = 0;
        List<StudentHistoryBean> studentHistoryBeans = new LinkedList<>();
        List<Map<String, String>> mapList = ExcelUtils.importExcel(OthersUtils.getFileSaveParentPath() + file_path);
        if (mapList == null || mapList.size() == 0) {
            return 0;
        } else {
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, String> map = mapList.get(i);
                StudentHistoryBean studentHistoryBean = new StudentHistoryBean();
                studentHistoryBean.setId_card(map.get("身份证号"))
                        .setName(map.get("姓名"))
                        .setPinyin(map.get("拼音"))
                        .setSex(map.get("性别"))
                        .setCountry(map.get("国籍"))
                        .setNation(map.get("民族"))
                        .setInstitution_id(75)
                        .setProfession(map.get("专业"))
                        .setLevel(OthersUtils.isEmpty(map.get("级别")) ? null : Integer.valueOf(map.get("级别")))
                        .setPass_level(OthersUtils.isEmpty(map.get("证书级别")) ? null : Integer.valueOf(map.get("证书级别")))
                        .setCertificate_no(map.get("证书编号"))
                        .setTicket_no(map.get("准考证号"))
                        .setPhone(map.get("移动电话"));
                String id_card = studentHistoryBean.getId_card();
                if (!OthersUtils.isEmpty(id_card) && id_card.length() == 18) {
                    //生日
                    String birthday = id_card.substring(6, 14);
                    studentHistoryBean.setBirthday(birthday);
                    //年龄
                    Integer year = Integer.valueOf(birthday.substring(0, 4));
                    Integer age = Calendar.getInstance().get(Calendar.YEAR) - year;
                    studentHistoryBean.setAge(age);
                    //性别
//                    if ((int) id_card.charAt(16) % 2 == 0) {
//                        studentHistoryBean.setSex("女");
//                    } else {
//                        studentHistoryBean.setSex("男");
//                    }
                }
                if (OthersUtils.isEmpty(studentHistoryBean.getId_card()) && OthersUtils.isEmpty(studentHistoryBean.getName())) {
                    continue;
                }
                studentHistoryBeans.add(studentHistoryBean);
            }
            if (studentHistoryBeans.size() > 0) {
                num = daoFactory.othersDao.insertStudentHistoryMany(studentHistoryBeans);
            }
        }
        return num;
    }

    /**
     * 取消分配考场
     */
    public int cancelAllotExamRoom(ExamRoomBean examRoomBean, Integer student_id) {
        int num = daoFactory.examRoomDao.updateByPrimaryKeySelective(examRoomBean);
        if (num == 0) {
            throw new RuntimeException("修改考场信息失败");
        }
        num = daoFactory.studentDao.updateByPrimaryKeySelective(new StudentBean().setStudent_id(student_id).setIs_allot("0"));
        if (num == 0) {
            throw new RuntimeException("修改考生信息失败");
        }
        return num;
    }

    /**
     * 考生查询成绩
     */
    public StudentHistoryBean queryResults(String search) {
        if (OthersUtils.isEmpty(search)) {
            throw new RuntimeException("请输入准考证号/证书编号/身份证号");
        }
        return daoFactory.othersDao.queryResults(search);
    }

    /**
     * 修改考生信息（不校验信息）
     */
    public int updateStudentNotValidate(StudentBean studentBean) {
        return daoFactory.studentDao.updateByPrimaryKeySelective(studentBean);
    }

    /**
     * 批量通过校验考生
     */
    public int studentPassAuditMany(String student_ids) {
        return daoFactory.othersDao.studentPassAuditMany(student_ids);
    }

    /**
     * 机构未导入成绩批次查询
     */
    public String getNotImportScoreBatchNoList(InstitutionBean institutionBean) {
        return daoFactory.othersDao.getNotImportScoreBatchNoList(institutionBean);
    }

    /**
     * 个人报名
     */
    public void studentSignUp(StudentBean studentBean, String code) {
        if (OthersUtils.isEmpty(code)) {
            throw new RuntimeException("请输入验证码");
        }
        CodeBean codeBean = daoFactory.codeDao.selectOne(new CodeBean().setCode(code).setMobile(studentBean.getPhone()));
        if (codeBean == null) {
            throw new RuntimeException("验证码不存在");
        }
        daoFactory.codeDao.deleteByPrimaryKey(codeBean.getCode_id());
        studentBean.setPinyin(ChineseToPinYinUtils.toPinyin(studentBean.getName()));
        this.insertStudent(studentBean);
    }
}
