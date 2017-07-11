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

import com.dhcc.inspect.domain.ZhLabMajor;
import com.dhcc.inspect.impl.ZhLabMajorImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class ZhLabMajorAction extends ActionSupport implements ModelDriven<ZhLabMajor>,
ServletRequestAware, ServletResponseAware{
	/**
	 * wangyue 
	 * 检测机构专业类别信息
	 */
	private static final long serialVersionUID = 1L;

	ZhLabMajor bean = new ZhLabMajor();

	List<ZhLabMajor> dataRows = new ArrayList<ZhLabMajor>();

	HttpServletRequest request;

	HttpServletResponse response;

	ZhLabMajorImpl impl = new ZhLabMajorImpl();
	
	public String ZhLabMajor_list() {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "ZhLabMajor_list";
		} else {
			try {
				PrintWriter pw = response.getWriter();
				String result = impl.getZhLabMajorList(bean.getCurPage(), bean
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
	
	public String ZhLabMajor_add() throws IOException {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			return "ZhLabMajor_add";
		}else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addZhLabMajor(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.ZhLabMajor.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if (bean.getOper() == 2){
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkMajorName(bean.getMajorName());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if (bean.getOper() == 3){
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkMajorCode(bean.getMajorCode());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else {
			try {
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.add.noparam"));
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
	
	public String ZhLabMajor_edit() throws IOException {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ZhLabMajor_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getZhLabMajorById(bean.getMajorId());
				map.put("msgInfo", getText("inspect.ZhLabMajor.editGet.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.editGet.failure"));
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
				map = impl.editZhLabMajor(bean);
				map.put("msgInfo", getText("inspect.ZhLabMajor.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3){
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckMajorCode(bean.getMajorCode(), bean.getMajorId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if (bean.getOper() == 4){
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckMajorName(bean.getMajorName(), bean.getMajorId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.edit.noparam"));
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
	
	public String ZhLabMajor_del() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "ZhLabMajor_del";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delZhLabMajor(bean);
				map.put("msgInfo", getText("inspect.ZhLabMajor.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if(e.getMessage().contains("foreign key")){
					map.put("msgInfo", getText("inspect.ZhLabMajor.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.ZhLabMajor.delete.failure"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.ZhLabMajor.delete.noparam"));
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
	
	public String ZhLabMajor_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ZhLabMajor_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getZhLabMajorById(bean.getMajorId());
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
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public ZhLabMajor getModel() {
		return bean;
	}

	public List<ZhLabMajor> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<ZhLabMajor> dataRows) {
		this.dataRows = dataRows;
	}
	
}
