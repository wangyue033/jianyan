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

import com.dhcc.inspect.domain.JdPlanCaseCheck;
import com.dhcc.inspect.impl.JdPlanCaseCheckImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-20    
 * 抽检方案审核Action
 */
public class JdPlanCaseCheckAction extends ActionSupport implements ModelDriven<JdPlanCaseCheck>,
ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	JdPlanCaseCheck bean = new JdPlanCaseCheck();

	List<JdPlanCaseCheck> dataRows = new ArrayList<JdPlanCaseCheck>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdPlanCaseCheckImpl impl = new JdPlanCaseCheckImpl();

	Page page = new Page();
	
	//已提交审核的抽检方案信息列表
	public String JdPlanCaseCheck_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdPlanCaseCheck_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setHandleDeptId(userInfo.getEmployee().getOrgId());
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				String result = impl.getJdPlanCaseCheckList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompanyId(),bean.getHandleDeptId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//抽检方案审核
	public String JdPlanCaseCheck_check(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanCaseCheck_check";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanCaseCheckById(bean.getCaseId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(oper == 2){
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.addJdPlanCaseCheck(bean);
				if("1".equals(bean.getStepIndex())){
					map.put("msgInfo", getText("inspect.jdPlanCaseCheck.add.refuse"));
				}else{
					map.put("msgInfo", getText("inspect.jdPlanCaseCheck.add.success"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanCaseCheck.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	//抽检方案预览
	public String JdPlanCaseCheck_detail(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanCaseCheck_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanCaseCheckById(bean.getCaseId());
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
	
	/*任务抽检汇总*/
	public String JdPlanCaseCheck_total(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanCaseCheck_total";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				String result = impl.getTotal(bean);
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<JdPlanCaseCheck> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdPlanCaseCheck> dataRows) {
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
	public JdPlanCaseCheck getModel() {
		return bean;
	}
	
}
