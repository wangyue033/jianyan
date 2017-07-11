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
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

public class JdInspectReportAction extends ActionSupport implements
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
	JdInspectReportImpl impl  = new JdInspectReportImpl();

	public String JdInspectReport_list () {
		String inspectId = (String)request.getParameter("inspectId");
		bean.setInspectId(inspectId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectReport_list";
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
				String result = impl.getJdInspectReportList(bean,bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId(),bean.getOptId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectReport_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectReport_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJdInspectReport(bean);
				map.put("msgInfo", getText("popedom.inspectReport.delete.success"));
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
					map.put("msgInfo", getText("popedom.inspectReport.delete.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReport.delete.failure"));
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
				map.put("msgInfo", getText("popedom.inspectReport.delete.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	/*生成报告*/
	public String JdInspectReport_create() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReport_create";
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
	
	/*报告详情*/
	public String JdInspectReport_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReport_detail";
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

	public String JdInspectReport_product() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReport_product";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl
						.getJdInspectReports(request.getParameter("enterpriseId"),bean.getSearchField(),bean.getSearchValue());
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
	
	public String JdInspectReport_add(){
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getTree();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		
		List<ZTreeBean> treeList1 = new ArrayList<ZTreeBean>();
		treeList1 = impl.getTreeCanParentRoot();
		JSONArray obj1 = JSONArray.fromObject(treeList1);
		request.setAttribute("treeListP", obj1);
		
		if (bean.getOper() == 0) {
			return "JdInspectReport_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				//bean.setProductId(impl.getProductId(bean.getParentId()));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setIsValid("1");
				impl.addJdInspectReport(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.inspectReport.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.inspectReport.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.inspectReport.add.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectReport_add1 () throws IOException {
		JdInspectReportImpl impl = new JdInspectReportImpl();
		String productId = request.getParameter("productId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.checkPlanNo(bean, productId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		} else {
			flag = impl.checkPlanNo(bean, productId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdInspectReport_add2 () throws IOException {
		JdInspectReportImpl impl = new JdInspectReportImpl();
		String irpTitle = request.getParameter("irpTitle");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.editCheckPlanNo(bean.getReportId(), irpTitle, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			} 
		} else {
			flag = impl.editCheckPlanNo(bean.getReportId(), irpTitle, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdInspectReport_edit() {
		
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReport_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdInspectReportById(bean.getReportId());
				map.put("msgInfo", getText("popedom.InspectReport.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectReport.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editJdInspectReport(bean);
				map.put("msgInfo", getText("popedom.InspectReport.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectReport.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectReport.edit.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectReport_submit() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectReport_submit";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.submitJdInspectReport(bean);
				map.put("msgInfo", getText("popedom.inspectReport.fenpai.success"));
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
					map.put("msgInfo", getText("popedom.inspectReport.fenpai.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReport.fenpai.failure"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (oper == 2) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				/*直接回退后的提交*/
				map = impl.submitReport(bean);
				map.put("msgInfo", getText("popedom.inspectReport.fenpai.success"));
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
					map.put("msgInfo", getText("popedom.inspectReport.fenpai.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReport.fenpai.failure"));
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
				map.put("msgInfo", getText("popedom.inspectReport.submit.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectReport_info () {
		String inspectId = request.getParameter("inspectId");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String mp = impl.getInfo(inspectId);
			pw.print(mp);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
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
