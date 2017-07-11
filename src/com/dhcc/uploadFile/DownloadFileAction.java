package com.dhcc.uploadFile;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class DownloadFileAction extends ActionSupport implements ServletRequestAware {

	private String fileName;

	private String filePath;
	
	HttpServletRequest request;

	public InputStream getDownloadFile() throws Exception {
		this.fileName = new String(this.fileName.getBytes("GBK"),"ISO-8859-1");
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.lastIndexOf("\\"));
		return new FileInputStream(path+filePath);
	}

	public String execute() {
		return SUCCESS;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

}
