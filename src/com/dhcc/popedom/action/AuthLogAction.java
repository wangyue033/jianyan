package com.dhcc.popedom.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.AuthLog;
import com.dhcc.popedom.impl.AuthLogImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class AuthLogAction extends ActionSupport
		implements ModelDriven<AuthLog>,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	AuthLog bean = new AuthLog();
	List<AuthLog> dataRows = new ArrayList<AuthLog>();
	HttpServletRequest request;
	HttpServletResponse response;
	AuthLogImpl impl = new AuthLogImpl();

	public String AuthLog_list() {
		if (bean.getOper() == 0) {
			return "AuthLog_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getAuthLogList(
						bean.getCurPage(),
						bean.getPerNumber(),
						bean.getOrderByField(),
						bean.getOrderByType(),
						bean.getSearchField(),
						bean.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public AuthLog getModel() {
		return bean;
	}

	public void setServletRequest(
			HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(
			HttpServletResponse res) {
		this.response = res;
	}

	public List<AuthLog> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<AuthLog> dataRows) {
		this.dataRows = dataRows;
	}

}
