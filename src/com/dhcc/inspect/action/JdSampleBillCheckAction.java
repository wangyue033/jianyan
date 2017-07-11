package com.dhcc.inspect.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdSampleBill;
import com.dhcc.inspect.impl.JdSampleBillCheckImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBillCheckAction extends ActionSupport implements ModelDriven<JdSampleBill>,
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JdSampleBill bean = new JdSampleBill();
	HttpServletRequest request;
	HttpServletResponse response;
	JdSampleBillCheckImpl impl = new JdSampleBillCheckImpl();
	
	public String JdSampleBillCheck_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdSampleBillCheck_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setHandleDeptId(userInfo.getEmployee().getOrgId());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setSerialId(userInfo.getEmployee().getEmployeeId());
				String result = impl.getJdSampleBillList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompanyId(),bean.getHandleDeptId(),bean.getSerialId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@SuppressWarnings("null")
	//审核函数
	public String JdSampleBillCheck_shenhe() throws SQLException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if(bean.getOper() == 0){
			return "JdSampleBillCheck_shenhe";
		}else if(bean.getOper() == 1){
			Map<String, Object> map = new LinkedHashMap<String, Object>();;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				impl.JdSampleBillCheckshenhe(bean);
				map.put("msg", "true");
				if("030101".equals(bean.getNextStepId())){
					map.put("msgInfo", getText("inspect.jdsamplebill.shenhe.notpass"));
				}else{
					map.put("msgInfo", getText("inspect.jdsamplebill.shenhe.success"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (IOException e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebill.shenhe.failure"));
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
	
	public String JdSampleBillCheck_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleBillCheck_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdSampleBillById(bean);
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
	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
		
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
		
	}

	@Override
	public JdSampleBill getModel() {
		return bean;
	}
	

}
