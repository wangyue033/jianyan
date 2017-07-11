package com.dhcc.inspect.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.inspect.impl.JdInspectReportImpl;
import com.dhcc.inspect.impl.JdInspectReportManageImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

public class JdInspectReportManageAction extends ActionSupport implements
ModelDriven<JdInspectReport>, ServletRequestAware, ServletResponseAware {

	/**
	 * @author longxl
	 * 检验报告编制
	 */
	private static final long serialVersionUID = 2040143900934960225L;

	JdInspectReport bean = new JdInspectReport();
	List<JdInspectReport> list = new ArrayList<JdInspectReport>();
	HttpServletRequest request;
	HttpServletResponse response;
	JdInspectReportManageImpl impl  = new JdInspectReportManageImpl();

	public String JdInspectReportManage_list () {
		String inspectId = (String)request.getParameter("inspectId");
		bean.setInspectId(inspectId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectReportManage_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0,10));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				String result = impl.getJdInspectReportManageList(bean,bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//复检报告上传
	public String JdInspectReportManage_uploads() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectReportManage_uploads";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				if("2".equals(bean.getReportType())){
				}else{
					bean.setReportType("3");
				}
				map = impl.uploadReport(bean);
				map.put("msg", true);
				if("2".equals(bean.getReportType())){
					map.put("msgInfo", getText("inspectReportManage.upload.success"));
				}else{
					bean.setReportType("3");
					map.put("msgInfo", getText("inspectReportManage.uploads.success"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", false);
				if("2".equals(bean.getReportType())){
					map.put("msgInfo",getText("inspectReportManage.upload.failure"));
				}else{
					map.put("msgInfo",getText("inspectReportManage.uploads.failure"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}
		return null;
	}
	
	
	//报告作废
	public String JdInspectReportManage_tovoid() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReportManage_tovoid";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
				bean.setAbolishOptId(userInfo.getEmployee().getEmployeeId());
				bean.setAbolishOptName(userInfo.getEmployee().getUserName());
				Map<String, Object> map = impl.tovoidReport(bean);
				JSONObject ob = new JSONObject();
				ob.putAll(map);
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
		// TODO Auto-generated method stub
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
	}

	@Override
	public JdInspectReport getModel() {
		return bean;
	}

	public List<JdInspectReport> getList() {
		return list;
	}

	public void setList(List<JdInspectReport> list) {
		this.list = list;
	}

}
