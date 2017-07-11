package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.inspect.impl.JdInspectReportCheckImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class JdInspectReportCheckAction extends ActionSupport implements
ModelDriven<JdInspectReport>, ServletRequestAware, ServletResponseAware {

	/**
	 * @author longxl
	 * 检验报告审核
	 */
	private static final long serialVersionUID = 7763315182101988370L;

	JdInspectReport bean = new JdInspectReport();
	List<JdInspectReport> list = new ArrayList<JdInspectReport>();
	HttpServletRequest request;
	HttpServletResponse response;
	JdInspectReportCheckImpl impl  = new JdInspectReportCheckImpl();

	public String JdInspectReportCheck_list () {
		String inspectId = (String)request.getParameter("inspectId");
		bean.setInspectId(inspectId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectReportCheck_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				String result = impl.getJdInspectReportList(bean,bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectReportCheck_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReportCheck_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl
						.getJdInspectReportById(bean.getReportId());
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
	
	public String JdInspectReportCheck_check() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectReportCheck_check";
		} else if(oper == 1) {
			try {
				pw = response.getWriter();
				map = impl.getJdInspectReportById(bean.getReportId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (oper == 2) {
			try {
				
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
//				bean.setOptId(userInfo.getEmployee().getOrgId());
//				bean.setOptName(userInfo.getEmployee().getOrgName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				System.out.println(bean.getBackType());
				map = impl.checkJdInspectReport(bean);
				if("030101".equals(bean.getNextStepId())){
					map.put("msgInfo", getText("popedom.inspectReportCheck.sample.refuse"));
				}else if("030501".equals(bean.getNextStepId())){
					map.put("msgInfo", getText("popedom.inspectReportCheck.data.refuse"));
				}else if("030701".equals(bean.getNextStepId())){
					map.put("msgInfo", getText("popedom.inspectReportCheck.roport.refuse"));
				}else{
					map.put("msgInfo", getText("popedom.inspectReportCheck.check.success"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if (e.getMessage().contains("foreign key")) {
					map.put("msgInfo", getText("popedom.inspectReportCheck.check.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReportCheck.check.failure"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.inspectReportCheck.check.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
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
		// TODO Auto-generated method stub
		return bean;
	}

	public List<JdInspectReport> getList() {
		return list;
	}

	public void setList(List<JdInspectReport> list) {
		this.list = list;
	}

}
