package com.dhcc.popedom.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.PopedomLog;
import com.dhcc.popedom.impl.PopedomLogImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @author WYH
 *
 */
public class PopedomLogAction extends ActionSupport
		implements ModelDriven<PopedomLog>,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	PopedomLog bean = new PopedomLog();
	List<PopedomLog> dataRows = new ArrayList<PopedomLog>();
	HttpServletRequest request;
	HttpServletResponse response;
	PopedomLogImpl impl = new PopedomLogImpl();

	public String PopedomLog_list() {
		if (bean.getOper() == 0) {
			return "PopedomLog_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getPopedomLogList(
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

	public PopedomLog getModel() {
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

	public List<PopedomLog> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<PopedomLog> dataRows) {
		this.dataRows = dataRows;
	}

}
