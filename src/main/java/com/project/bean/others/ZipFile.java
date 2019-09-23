package com.project.bean.others;

/**
 * 压缩文件子文件
 */
public class ZipFile {
	private String name;
	private String path;

	public String getName() {
		return name;
	}

	public ZipFile setName(String name) {
		this.name = name;
		return this;
	}

	public String getPath() {
		return path;
	}

	public ZipFile setPath(String path) {
		this.path = path;
		return this;
	}
}
