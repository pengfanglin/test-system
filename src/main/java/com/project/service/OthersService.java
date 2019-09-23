package com.project.service;

import com.project.bean.others.CodeBean;
import com.project.dao.DaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OthersService {

  @Autowired
  DaoFactory daoFactory;

  /**
   * 添加新的验证码
   */
  public int insertCode(CodeBean codeBean) {
    return daoFactory.codeDao.insertSelective(codeBean);
  }
}
