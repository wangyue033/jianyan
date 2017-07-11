package com.dhcc.popedom.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.UserLog;
import com.dhcc.popedom.impl.UserLogImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @author WYH
 *
 */
public class UserLogAction extends ActionSupport
		implements ModelDriven<UserLog>,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	UserLog bean = new UserLog();
	List<UserLog> dataRows = new ArrayList<UserLog>();
	HttpServletRequest request;
	HttpServletResponse response;
	UserLogImpl impl = new UserLogImpl();

	public String UserLog_list() {
		if (bean.getOper() == 0) {
			return "UserLog_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getUserLogList(
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

	public UserLog getModel() {
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

	public List<UserLog> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<UserLog> dataRows) {
		this.dataRows = dataRows;
	}

}
