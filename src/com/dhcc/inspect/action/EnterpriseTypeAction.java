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
import com.dhcc.login.UserInfoBean;
import com.dhcc.inspect.domain.EnterpriseType;
import com.dhcc.inspect.impl.EnterpriseTypeImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class EnterpriseTypeAction extends ActionSupport implements ModelDriven<EnterpriseType>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EnterpriseType bean = new EnterpriseType();

	List<EnterpriseType> dataRows = new ArrayList<EnterpriseType>();

	HttpServletRequest request;

	HttpServletResponse response;

	EnterpriseTypeImpl impl = new EnterpriseTypeImpl();

	public String EnterpriseType_list() {
		if (bean.getOper() == 0) {
			return "EnterpriseType_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getEnterpriseTypeList(bean.getCurPage(), bean
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

	public String EnterpriseType_add() {
		if (bean.getOper() == 0) {
			return "EnterpriseType_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addEnterpriseType(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.enterprisetype.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.enterprisetype.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			try {
				boolean flag = impl.addCheck(bean.getTypeName());
				PrintWriter pw = null;
				pw = response.getWriter();
				if(flag == true){
					pw.print("true");
				}else{
					pw.print("false");
				}
				pw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.enterprisetype.add.noparam"));
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

	public String EnterpriseType_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "EnterpriseType_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getEnterpriseTypeById(bean.getTypeId());
				map.put("msgInfo", getText("inspect.enterprisetype.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.enterprisetype.get.failure"));
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
				map = impl.editEnterpriseType(bean);
				map.put("msgInfo", getText("inspect.enterprisetype.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.enterprisetype.edit.failure"));
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
				map.put("msgInfo", getText("inspect.enterprisetype.edit.noparam"));
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

	public String EnterpriseType_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "EnterpriseType_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delEnterpriseType(bean);
				map.put("msgInfo", getText("inspect.enterprisetype.delete.success"));
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
					map.put("msgInfo", getText("inspect.enterprisetype.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.enterprisetype.delete.failure"));
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
				map.put("msgInfo", getText("inspect.enterprisetype.delete.noparam"));
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

	public String EnterpriseType_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "EnterpriseType_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getEnterpriseTypeById(bean.getTypeId());
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

	public EnterpriseType getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<EnterpriseType> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<EnterpriseType> dataRows) {
		this.dataRows = dataRows;
	}

}
