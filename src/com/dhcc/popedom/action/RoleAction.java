package com.dhcc.popedom.action;

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
import com.dhcc.login.impl.Menu;
import com.dhcc.popedom.domain.Role;
import com.dhcc.popedom.impl.RoleImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @author lgchen
 *
 */
public class RoleAction extends ActionSupport implements ModelDriven<Role>,
		ServletRequestAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	Role bean = new Role();
	List<Role> dataRows = new ArrayList<Role>();
	HttpServletRequest request;
	HttpServletResponse response;
	RoleImpl impl = new RoleImpl();

	public String Role_list() {
		if (bean.getOper() == 0) {
			return "Role_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getRoleList(
						bean.getCurPage(),
						bean.getPerNumber(),
						bean.getOrderByField(),
						bean.getOrderByType(),
						bean.getSearchField(),
						bean.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String Role_add() {
		
		if (bean.getOper() == 0) {
			return "Role_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addRole(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.role.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if(bean.getOper() == 2){
			try {
				boolean flag = impl.addCheck(bean.getRoleName());
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
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.add.noparam"));
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
	
	public String Role_edit() {
		
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "Role_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getRoleById(bean.getRoleId());
				map.put("msgInfo", getText("popedom.role.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.get.failure"));
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
				map = impl.editUser(bean);
				map.put("msgInfo", getText("popedom.role.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else if(bean.getOper() == 3){
			try {
				pw = response.getWriter();
				boolean flag = impl.addCheck(bean.getRoleName());
				if(flag == true){
					pw.print("true");
				}else{
					pw.print("false");
				}
				pw.flush();
				pw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.edit.noparam"));
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
	
	public String Role_detail() {
		List<Menu> menuList = impl.getMenuAllList(bean.getRoleId());
		request.setAttribute("menuList", menuList);
		return "Role_detail";
	}
	
	/**
	 * 角色授权
	 * @return
	 */
	public String Role_accredit() {
		String roleId = bean.getRoleId();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			String[] selectedPsId = impl.getPsByRoleId(roleId);
			List<Menu> menuList = impl.getMenuAllList();
			request.setAttribute("selectedPsId", selectedPsId);
			request.setAttribute("menuList", menuList);
		} else if (bean.getOper() == 1) {
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userBean = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userBean.getEmployee().getEmployeeId());
				bean.setOptName(userBean.getEmployee().getUserName());
				impl.accreditRole(bean);
				map = new HashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.role.reset.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				
			} catch (Exception se) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.role.reset.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				se.printStackTrace();
			}
			String[] selectedPsId = impl.getPsByRoleId(roleId);
			List<Menu> menuList = impl.getMenuAllList();
			request.setAttribute("selectedPsId", selectedPsId);
			request.setAttribute("menuList", menuList);
			
			return null;
			
		}
		return "Role_accredit";
	}
	
	
	public String Role_del(){
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "Role_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delRole(bean);
				map.put("msgInfo", getText("popedom.comp.delete.success"));
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
					map.put("msgInfo", getText("popedom.comp.delete.fk"));
				}else{
					map.put("msgInfo", getText("popedom.comp.delete.failure"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				//e.printStackTrace();
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



	public Role getModel() {
		return bean;
	}

	public void setServletRequest(
			HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(
			HttpServletResponse res) {
		this.response = res;
	}

	public List<Role> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<Role> dataRows) {
		this.dataRows = dataRows;
	}

}
