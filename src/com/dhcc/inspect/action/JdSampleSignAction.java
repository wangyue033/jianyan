package com.dhcc.inspect.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
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
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.impl.JdSampleSignImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleSignAction extends ActionSupport implements ModelDriven<JdSample>,
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JdSample bean = new JdSample();

	List<JdSample> dataRows = new ArrayList<JdSample>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdSampleSignImpl impl = new JdSampleSignImpl();
	
	public String JdSampleSign_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdSampleSign_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setSignCompId(userInfo.getEmployee().getCompanyId());
				String result = impl.getJdSampleSignList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getOptId(),bean.getSignCompId());
				pw.print(result);
				pw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	@SuppressWarnings("null")
	public String JdSampleSign_sign() throws ParseException, SQLException{
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if(oper == 0){
		return "JdSampleSign_sign";
		}else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdSampleById(bean.getSampleId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(oper == 2){
			Map<String, Object> map = new LinkedHashMap<String, Object>();;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				impl.changeStatus(bean);
				map.put("msg", "true");
				if("020101".equals(bean.getNextStepId())){
					map.put("msgInfo", getText("popedom.jdsamplesign.sign.refuse"));
				}else{
					map.put("msgInfo", getText("popedom.jdsamplesign.sign.success"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (IOException e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.jdsample.get.failure"));
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

	public String JdSampleSign_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleSign_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdSampleById(bean.getSampleId());
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
	
	public String JdSampleShowBill_list() {
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		
		try {
			pw = response.getWriter();
			Map<String, Object> map = impl.getBillCount(bean.getCurPage(), bean.getPerNumber(), bean.getOrderByField(),
					bean.getOrderByType(), bean.getSearchField(), bean.getSearchValue(),bean.getOptId(),bean.getSampleId());
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String JdSampleSign_case() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleSign_case";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanCaseById(bean.getSampleId());
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
	public JdSample getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<JdSample> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdSample> dataRows) {
		this.dataRows = dataRows;
	}
}
