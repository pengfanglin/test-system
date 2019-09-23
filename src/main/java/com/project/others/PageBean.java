package com.project.others;


import com.github.pagehelper.PageRowBounds;

/**
 * 分页
 */
public class PageBean extends PageRowBounds {
	public PageBean(Integer page, Integer limit) {
		super(((page==null?1:page)-1)*(limit==null?10:limit), limit==null?10:limit);
	}
}
