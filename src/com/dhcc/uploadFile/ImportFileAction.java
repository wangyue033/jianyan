package com.dhcc.uploadFile;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;


public class ImportFileAction extends ActionSupport implements
											ServletRequestAware {

	HttpServletRequest request;
	
	private String fileNames;
	private String modulePath;
	private int oper;
	private String listNum;
	
	public String execute(){
		if(fileNames.contains(";;")){
			System.out.println(fileNames);
			String[] filePaths = fileNames.split(";;");
			for(int i=0;i<filePaths.length/2;i++){
				String path = request.getSession().getServletContext().getRealPath("")+filePaths[i*2+1];
				String ktrPath = request.getSession().getServletContext().getRealPath("")+"\\ktr\\";
				System.out.println(path);
				
				//etl数据导入操作
				Map<String,String> params = new HashMap<String,String>();
				params.put("fileName", path);
			}
		}
		return null;
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}



	public String getFileNames() {
		return fileNames;
	}



	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}


	public String getModulePath() {
		return modulePath;
	}


	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}


	public int getOper() {
		return oper;
	}


	public void setOper(int oper) {
		this.oper = oper;
	}


	public String getListNum() {
		return listNum;
	}


	public void setListNum(String listNum) {
		this.listNum = listNum;
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	

}
