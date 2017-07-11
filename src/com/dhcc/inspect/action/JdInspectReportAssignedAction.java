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
import com.dhcc.inspect.impl.JdInspectReportAssignedImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class JdInspectReportAssignedAction extends ActionSupport implements
ModelDriven<JdInspectReport>, ServletRequestAware, ServletResponseAware {

	/**
	 * @author longxl
	 * 检验报告签发
	 */
	private static final long serialVersionUID = 7763315182101988370L;

	JdInspectReport bean = new JdInspectReport();
	List<JdInspectReport> list = new ArrayList<JdInspectReport>();
	HttpServletRequest request;
	HttpServletResponse response;
	JdInspectReportAssignedImpl impl  = new JdInspectReportAssignedImpl();

	public String JdInspectReportAssigned_list () {
		String inspectId = (String)request.getParameter("inspectId");
		bean.setInspectId(inspectId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectReportAssigned_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompId(userInfo.getEmployee().getOrgId());
				String result = impl.getJdInspectReportList(bean,bean.getCurPage(), bean
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
	
	public String JdInspectReportAssigned_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectReportAssigned_detail";
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
	
	/*检验报告签发*/
	public String JdInspectReportAssigned_Assigned() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectReportAssigned_Assigned";
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
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				/*检验报告签发*/
				map = impl.checkJdInspectReport(bean);
				map.put("msgInfo", getText("popedom.inspectReportAssigned.Assigned.success"));
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
					map.put("msgInfo", getText("popedom.inspectReportAssigned.Assigned.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReportAssigned.Assigned.failure"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if (oper == 3) {
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				if(bean.getStepIndex().equals("2")){
					/*签发退回到抽样单录入*/
					map = impl.checkReportToSample(bean);
				}else if(bean.getStepIndex().equals("3")){
					/*签发退回到检验数据填报*/
					map = impl.checkReportToInspect(bean);
				}else if(bean.getStepIndex().equals("1")){
					/*签发退回到检验报告编制*/
					map = impl.checkReportToReport(bean);
				}
				map.put("msgInfo", getText("popedom.inspectReportAssigned.Assigned.success"));
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
					map.put("msgInfo", getText("popedom.inspectReportAssigned.Assigned.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectReportAssigned.Assigned.failure"));
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
				map.put("msgInfo", getText("popedom.inspectReportAssigned.Assigned.noparam"));
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
