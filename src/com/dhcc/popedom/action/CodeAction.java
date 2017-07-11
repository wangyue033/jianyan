package com.dhcc.popedom.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.Code;
import com.dhcc.popedom.domain.ColumnInfo;
import com.dhcc.popedom.impl.CodeImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CodeAction extends ActionSupport implements ModelDriven<Code>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5867842342485106108L;

	HttpServletRequest request;

	HttpServletResponse response;

	Code bean = new Code();

	CodeImpl impl = new CodeImpl();

	List<ColumnInfo> codeList = new ArrayList<ColumnInfo>();

	public String Code_init() {
		String result = "00";
		if (bean.getOper() == 0) {
			bean.reset();
			addActionMessage("");
		} else if (bean.getOper() == 1) {
			try {
				codeList = impl.getColumnInfoList(bean.getSchemaName(), bean
						.getTableName());
				if (codeList.size() > 0) {
					result = "10";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (bean.getOper() == 2) {
			try{
				result = "20";
				LinkedHashMap<String, String> selectMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> addMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> editMap = new LinkedHashMap<String, String>();
				for (int i = 0; i < codeList.size(); i++) {
					ColumnInfo info = codeList.get(i);
					if(info.getIsSelect().equals("true")){
						selectMap.put(info.getColumnName(), info.getColumnZh());// + "-" + info.getIsPri()
					}
					if(info.getIsInsert() != null && info.getIsInsert().equals("true")){
						addMap.put(info.getColumnName(), info.getColumnZh());
					}
					if(info.getIsUpdate() != null && info.getIsUpdate().equals("true")){
						editMap.put(info.getColumnName(), info.getColumnZh());
					}
				}
				
				String srcRes = impl.srcFile(bean, codeList);
				String webRes = impl.webFile(bean, codeList, selectMap, addMap, editMap);
				if(srcRes.equals("99") || webRes.equals("99")){
					result = "99";
				}else if(srcRes.equals("90") || webRes.equals("90")){
					result = "30";
				}
				
			}catch(Exception e){
				e.printStackTrace();
				result = "30";
			}
			
		}
		request.setAttribute("result", result);
		return "Code_init";
	}

	public List<ColumnInfo> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<ColumnInfo> codeList) {
		this.codeList = codeList;
	}

	public Code getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

}
