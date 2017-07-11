package com.dhcc.popedom.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.OptLog;
import com.dhcc.popedom.impl.OptLogImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @author WYH
 *
 */
public class OptLogAction extends ActionSupport implements ModelDriven<OptLog>,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	OptLog bean = new OptLog();
	List<OptLog> dataRows = new ArrayList<OptLog>();
	HttpServletRequest request;
	HttpServletResponse response;
	OptLogImpl impl = new OptLogImpl();

	public String OptLog_list() {
		if (bean.getOper() == 0) {
			return "OptLog_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getOptLogList(bean.getCurPage(),
						bean.getPerNumber(), bean.getOrderByField(),
						bean.getOrderByType(), bean.getSearchField(),
						bean.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public OptLog getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<OptLog> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<OptLog> dataRows) {
		this.dataRows = dataRows;
	}
}
