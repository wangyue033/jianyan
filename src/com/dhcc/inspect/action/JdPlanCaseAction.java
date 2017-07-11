package com.dhcc.inspect.action;

import java.io.IOException;
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

import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.inspect.impl.JdPlanCaseImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-17    
 * 抽检方案Action
 */
public class JdPlanCaseAction extends ActionSupport implements ModelDriven<JdPlanCase>,
ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	JdPlanCase bean = new JdPlanCase();

	List<JdPlanCase> dataRows = new ArrayList<JdPlanCase>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdPlanCaseImpl impl = new JdPlanCaseImpl();

	Page page = new Page();
	
	//抽检方案信息列表
	public String JdPlanCase_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdPlanCase_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				//String handleDeptId = userInfo.getEmployee().getCompanyId().substring(0, 10);
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setHandleDeptId(userInfo.getEmployee().getOrgId());
				bean.setHandleDeptName(userInfo.getEmployee().getCompanyName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				String result = impl.getJdPlanCaseList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getHandleDeptId(),bean.getOptId(),bean.getCompanyId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//新增抽检方案时添加检测标准和检测项目
	public String JdPlanCase_addItem() throws Exception {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			List<JdPlanCase> menuList = impl.getItemAllList(bean.getProductId());
			request.setAttribute("selectedPsId", bean.getSelectedPsId().split(","));
			request.setAttribute("menuList", menuList);
			return "JdPlanCase_addItem";
		}else {
			List<JdPlanCase> menuList = impl.getItemAllList(bean.getProductId());
			request.setAttribute("selectedPsId", bean.getSelectedPsId().split(","));
			request.setAttribute("menuList", menuList);
			return "JdPlanCase_addItem";
		} 
	}
	
	//抽检方案信息添加
	public String JdPlanCase_add() throws Exception {
		if (bean.getOper() == 0) {
			return "JdPlanCase_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setHandleDeptId(userInfo.getEmployee().getCompanyId());
				bean.setHandleDeptName(userInfo.getEmployee().getCompanyName());
				impl.addJdPlanCase(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdPlanCase.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			JdPlanCaseImpl jdProgImpl = new JdPlanCaseImpl();
			String planName = request.getParameter("planName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdProgImpl.checkJdPlanCaseName(planName);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if(bean.getOper() == 3){
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				String result = impl.getTaskList(bean);
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.task.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if (bean.getOper() == 4) {
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdItemById(bean.getStandardId());
				map.put("msgInfo", getText("inspect.jdItem.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdItem.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}  else if (bean.getOper() == 5) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setHandleDeptId(userInfo.getEmployee().getCompanyId());
				bean.setHandleDeptName(userInfo.getEmployee().getCompanyName());
				if(("").equals(bean.getCaseId())||bean.getCaseId()==null||("undefined").equals(bean.getCaseId())){
					bean.setCaseId(DBFacade.getInstance().getID());
					bean.setFlag("0");
				}else{
					bean.setFlag("1");
				}
				impl.addFirst(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("caseId", bean.getCaseId());
				map.put("msgInfo", getText("inspect.jdPlanCase.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else{
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.add.noparam"));
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
	
	//删除抽检方案信息
	public String JdPlanCase_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlanCase_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				String path = request.getSession().getServletContext().getRealPath("");
				path = path.substring(0, path.lastIndexOf("\\"));
				bean.setPath(path);
				map = impl.delJdPlanCase(bean);
				map.put("msgInfo", getText("inspect.jdPlanCase.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if(e.getMessage().contains("foreign key")){
					map.put("msgInfo", getText("inspect.jdPlanCase.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.jdPlanCase.delete.failure"));
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
				map.put("msgInfo", getText("inspect.jdPlanCase.delete.noparam"));
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
	
	//企业信息详情
	public String JdPlanCase_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanCase_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanCaseById(bean.getCaseId());
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
	
	//企业基础信息修改
	public String JdPlanCase_edit() throws IOException {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanCase_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdPlanCaseById(bean.getCaseId());
				map.put("msgInfo", getText("inspect.jdPlanCase.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setHandleDeptId(userInfo.getEmployee().getCompanyId());
				bean.setHandleDeptName(userInfo.getEmployee().getCompanyName());
				map = impl.editJdPlanCase(bean);
				map.put("msgInfo", getText("inspect.jdPlanCase.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 3) {
			JdPlanCaseImpl jdPlanCaseImpl = new JdPlanCaseImpl();
			String planName = request.getParameter("planName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdPlanCaseImpl.checkEditJdPlanCaseName(planName,bean.getCaseId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if (bean.getOper() == 4) {
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.editJdItemById(bean.getTaskId(),bean.getCaseId());
				map.put("msgInfo", getText("inspect.jdItem.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdItem.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}  else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdComp.edit.noparam"));
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
	
	//提交抽检方案信息待审核
	public String JdPlanCase_submit() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlanCase_submit";
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
				map = impl.submitJdPlanCase(bean);
				map.put("msgInfo", getText("inspect.jdPlanCase.submit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCase.submit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			return null;
		}
	}
	
	//抽检方案下发
	public String JdPlanCase_issue() throws IOException {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlanCase_issue";
		} else if (oper == 1) {
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			map = impl.issueJdPlanCase(bean);
			map.put("msgInfo", getText("inspect.jdPlanCaseCheck.issue.success"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
			return null;
		}else {
			return null;
		}
	}

	public List<JdPlanCase> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdPlanCase> dataRows) {
		this.dataRows = dataRows;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
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
	public JdPlanCase getModel() {
		return bean;
	}
}
