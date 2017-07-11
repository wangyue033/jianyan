package com.dhcc.popedom.action;

import java.io.PrintWriter;
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

import com.dhcc.login.UserInfoBean;
import com.dhcc.popedom.domain.User;
import com.dhcc.popedom.impl.PwdImpl;
import com.dhcc.popedom.impl.UserImpl;
import com.dhcc.utils.ZTreeImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

/**
 * 
 * @author lgchen
 * 
 */
public class PwdAction extends ActionSupport implements ModelDriven<User>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	User bean = new User();

	List<User> dataRows = new ArrayList<User>();

	HttpServletRequest request;

	HttpServletResponse response;

	PwdImpl impl = new PwdImpl();

	public String Pwd_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "Pwd_tree");
		request.setAttribute("rightAction", "Pwd_list");
		request.setAttribute("sWidth", "250");
		return "tree_frame";
	}

	public String Pwd_list() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		String companyId = (String) request.getParameter("companyId");
		bean.setCompanyId(companyId);
		if (bean.getOper() == 0) {
			return "Pwd_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				if (companyId == null || "".equals(companyId)) {
					bean.setCompanyId(userInfo.getEmployee().getOrgId());
				}
				UserImpl impl1 = new UserImpl();
				String result = impl1.getUserList(bean, bean.getCurPage(),
						bean.getPerNumber(), bean.getOrderByField(),
						bean.getOrderByType(), bean.getSearchField(),
						bean.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public String Pwd_resetpwd() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "Pwd_resetpwd";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.resetPassword(bean.getSerialId(), bean.getOptId(),
						bean.getOptName());
				map.put("msgInfo", getText("popedom.pwd.reset.success"));
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
					map.put("msgInfo", getText("popedom.comp.delete.fk"));
				} else {
					map.put("msgInfo", getText("popedom.pwd.reset.failure"));
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
				map.put("msgInfo", getText("popedom.comp.delete.noparam"));
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

	public String Pwd_tree() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			String orgId = userInfo.getEmployee().getOrgId();
			treeList = ZTreeImpl.getInstance().getCompanyAllRoot(orgId);
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "Pwd_tree";
	}

	public User getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<User> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<User> dataRows) {
		this.dataRows = dataRows;
	}

}
