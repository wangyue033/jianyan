package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdInspectReportPrint;
import com.dhcc.inspect.impl.JdInspectReportPrintImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


public class JdInspectReportPrintAction extends ActionSupport implements ModelDriven<JdInspectReportPrint>,
ServletRequestAware, ServletResponseAware{
	private static final long serialVersionUID = 1L;

	JdInspectReportPrint bean = new JdInspectReportPrint();

	List<JdInspectReportPrint> dataRows = new ArrayList<JdInspectReportPrint>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdInspectReportPrintImpl impl = new JdInspectReportPrintImpl();
	
	public String JdInspectReportPrint_list() {
		
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectReportPrint_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				String compId = userInfo.getEmployee().getOrgId();
				String result = impl.getJdInspectReportPrintList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),compId);
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}		
	}
	public String JdInspectReportPrint_achive() {
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> map = null;
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			String path = request.getSession().getServletContext().getRealPath("");
			System.out.println(path);
			map = impl.achiveReport(request.getParameter("reportId"),path);
			map.put("msgInfo", getText("inspect.inspectReportPrint.achive.success"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
			return null;
		} catch (Exception e) {
			response.setCharacterEncoding("UTF-8");
			map = new HashMap<String, Object>();
			map.put("msg", "false");
			map.put("msgInfo", getText("inspect.inspectReportPrint.achive.failure"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		}
		return null;
	}
	//签名
	public String JdInspectReportPrint_sign() {
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> map = null;
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			String path = request.getSession().getServletContext().getRealPath("");
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			map = impl.signReport(request.getParameter("reportId"),path,bean);
			map.put("msgInfo", getText("inspect.inspectReportPrint.sign.success"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//签章
	public String JdInspectReportPrint_reportSign() {
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		Map<String, Object> map = null;
		try {
			pw = response.getWriter();
			String path = request.getSession().getServletContext().getRealPath("");
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			map = impl.reportSign(bean,path,"");
			System.out.println(111);
			map.put("msgInfo", getText("inspect.inspectReportPrint.reportSign.success"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String JdInspectReportPrint_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReportPrint_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				/*
				Map<String, Object> map = impl
						.getJdInspectReportById(bean.getReportId());*/
				JSONObject ob = new JSONObject();
				//ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}	
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public JdInspectReportPrint getModel() {
		return bean;
	}

	public List<JdInspectReportPrint> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdInspectReportPrint> dataRows) {
		this.dataRows = dataRows;
	}
	

}
