package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.dhcc.inspect.domain.JdSampleBill;
import com.dhcc.inspect.impl.JdSampleBillEntryImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBillEntryAction extends ActionSupport implements ModelDriven<JdSampleBill>,
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JdSampleBill bean = new JdSampleBill();

	List<JdSampleBill> dataRows = new ArrayList<JdSampleBill>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdSampleBillEntryImpl impl = new JdSampleBillEntryImpl();
	
	public String JdSampleBillEntry_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdSampleBillEntry_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompId(userInfo.getEmployee().getOrgId());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				String result = impl.getJdSampleTaskList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId(),bean.getCompanyId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdSampleBillEntry_addbill(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if(oper == 0){
			return "JdSampleBillEntry_addbill";
		}else if(oper == 1){
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
			    UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				String copId = userInfo.getEmployee().getOrgId();
				impl.addJdSampleBill(bean,copId);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdsamplebill.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebill.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if(oper == 2){
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map=impl.getJdSampleSignById(bean.getSampleId());
				map.put("msg", "true");
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(oper == 3){
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map=impl.getproInfoById(bean.getProductId());
				map.put("msg", "true");
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
	
	public String JdSampleBillEntry_getEp() {
		try {
			Map<String, Object> map = null;
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			bean.setCompId(userInfo.getEmployee().getOrgId());
			bean.setCompanyId(userInfo.getEmployee().getCompanyId());
			map = impl.getEpInfo(bean.getEnterpriseId());
			map.put("msg", "true");
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JdSampleBill getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<JdSampleBill> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdSampleBill> dataRows) {
		this.dataRows = dataRows;
	}
}
