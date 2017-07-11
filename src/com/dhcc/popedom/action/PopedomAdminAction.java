package com.dhcc.popedom.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.login.UserInfoBean;
import com.dhcc.popedom.domain.PopedomAdmin;
import com.dhcc.popedom.domain.User;
import com.dhcc.popedom.impl.PopedomAdminImpl;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.PageJsonAction;
import framework.dhcc.tree.bean.ZTreeBean;

/**
 * 
 * @author WYH
 * 
 */

public class PopedomAdminAction extends PageJsonAction implements
		ModelDriven<PopedomAdmin>, ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 357696572348645595L;

	PopedomAdmin bean = new PopedomAdmin();

	List<PopedomAdmin> dataRows = new ArrayList<PopedomAdmin>();

	HttpServletRequest request;

	HttpServletResponse response;

	PopedomAdminImpl impl = new PopedomAdminImpl();

	String perNumber, gotoPage;

	List<User> userRows = new ArrayList<User>();

	public String PopedomAdmin_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "PopedomAdmin_tree");
		request.setAttribute("rightAction", "PopedomAdmin_list");
		request.setAttribute("sWidth", "250");
		return "tree_frame";
	}

	public String PopedomAdmin_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getRoleTree();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "PopedomAdmin_tree";
	}

	// 权限功能界面列表
	public String PopedomAdmin_list() {
		String roleId = (String) request.getParameter("roleId");
		String roleName = (String) request.getParameter("roleName");
		bean.setRoleId(roleId);
		bean.setRoleName(roleName);
		if (bean.getOper() == 0) {
			return "PopedomAdmin_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getPopedomList(bean, bean.getCurPage(),
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

	// 添加功能权限界面列表的action
	public String PopedomAdmin_addUser() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		String roleId = (String) request.getParameter("roleId");
		String userIds = (String) request.getParameter("userIds");
		String roleName = (String) request.getParameter("roleName");
		bean.setRoleId(roleId);
		bean.setUserIds(userIds);
		bean.setRoleName(roleName);
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "PopedomAdmin_addUser";
		} else if (bean.getOper() == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				String result = impl.getUserList(bean, bean.getCurPage(),
						bean.getPerNumber(), bean.getOrderByField(),
						bean.getOrderByType(), bean.getSearchField(),
						bean.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			// response.setContentType("text/html; charset=UTF-8");
			// PrintWriter out = null;
			try {
				pw = response.getWriter();
				bean.setOptId(userInfo.getEmployee().getUserId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setUserId(userInfo.getEmployee().getEmployeeId());
				impl.addUserTRole(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception se) {
				try {
					pw = response.getWriter();
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("msg", "false");
					map.put("msgInfo", getText("popedom.add.failuer"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return null;
		}
	}

	// 删除功能权限功能
	public String PopedomAdmin_delUser() throws IOException {
		String serialId = (String) request.getParameter("serialId");
		String roleId = (String) request.getParameter("roleId");
		String roleName = (String) request.getParameter("roleName");
		bean.setRoleId(roleId);
		bean.setSerialId(serialId);
		bean.setRoleName(roleName);
		int oper = bean.getOper();
		Map<String, Object> map = null;
		if (oper == 0) {
			return "PopedomAdmin_delUser";
		} else {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delUserFRole(bean);
				map.put("msgInfo", getText("popedom.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception se) {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("opedom.delete.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());// 将JSON对象传到后台
				pw.close();
				se.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int getCurPage() {
		return this.curPage;
	}

	@Override
	public int getTotalPages() {
		return this.totalPages;
	}

	public List<PopedomAdmin> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<PopedomAdmin> dataRows) {
		this.dataRows = dataRows;
	}

	public String getGotoPage() {
		return gotoPage;
	}

	public void setGotoPage(String gotoPage) {
		this.gotoPage = gotoPage;
	}

	public String getPerNumber() {
		return perNumber;
	}

	public void setPerNumber(String perNumber) {
		this.perNumber = perNumber;
	}

	@Override
	public int getTotalRecords() {
		return this.totalRecords;
	}

	public PopedomAdmin getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public List<User> getUserRows() {
		return userRows;
	}

	public void setUserRows(List<User> userRows) {
		this.userRows = userRows;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

}
