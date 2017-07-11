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

import com.dhcc.inspect.domain.JdPlanHandle;
import com.dhcc.inspect.impl.JdPlanTurnImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-21    
 * 抽检任务签收Action
 */
public class JdPlanTurnAction extends ActionSupport implements ModelDriven<JdPlanHandle>,
ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	JdPlanHandle bean = new JdPlanHandle();

	List<JdPlanHandle> dataRows = new ArrayList<JdPlanHandle>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdPlanTurnImpl impl = new JdPlanTurnImpl();

	Page page = new Page();
	
	//抽检任务转办信息列表
	public String JdPlanTurn_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdPlanTurn_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				//String compId = userInfo.getEmployee().getCompanyId().substring(0, 10);
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				String result = impl.getJdPlanHandleList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompanyId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//抽检任务转办
	public String JdPlanTurn_handle(){

		if (bean.getOper() == 0) {
			return "JdPlanTurn_handle";
		}else if (bean.getOper() == 2) {
			Map<String ,Object> map =  new HashMap<String, Object>();
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo1 = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo1.getEmployee().getEmployeeId());
				bean.setOptName(userInfo1.getEmployee().getUserName());
				bean.setCompanyId(userInfo1.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo1.getEmployee().getCompanyName());
                map = impl.JdPlanHandleEnter(bean);
				map.put("msg", "true");
				if("1".equals(bean.getStepIndex())){
					map.put("msgInfo", getText("jdPlanTurn.handle.refuse"));
				}else{
					map.put("msgInfo", getText("jdPlanTurn.handle.success"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}  catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				map.put("msg", "false");
				map.put("msgInfo", getText("jdPlanTurn.handle.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if(bean.getOper() == 3){
			Map<String ,Object> map =  new HashMap<String, Object>();
			PrintWriter pw = null;
			try{
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdPlanHandleById(bean.getTaskId());
				map.put("msg", "true");
				map.put("msgInfo", getText("jdPlanTurn.handle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
			return null;
		}else{
			return null;
		}
	}
	
	//抽检任务转办详情
	public String JdPlanTurn_detail(){

		if (bean.getOper() == 0) {
			return "JdPlanTurn_detail";
		}else if(bean.getOper() == 3){
			Map<String ,Object> map =  new HashMap<String, Object>();
			PrintWriter pw = null;
			try{
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdPlanHandleById(bean.getTaskId());
				map.put("msg", "true");
				map.put("msgInfo", getText("jdPlanTurn.handle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}catch(Exception e){
				e.printStackTrace();
			}			
			return null;
		}else{
			return null;
		}
	}
	
	public List<JdPlanHandle> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdPlanHandle> dataRows) {
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
	public JdPlanHandle getModel() {
		// TODO Auto-generated method stub
		return bean;
	}

}
