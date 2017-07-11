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
import com.dhcc.login.impl.Menu;
import com.dhcc.popedom.domain.DeptRole;
import com.dhcc.popedom.impl.DeptRoleImpl;
import com.dhcc.utils.ZTreeImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

public class DeptRoleAction extends ActionSupport implements ModelDriven<DeptRole>,
ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	DeptRole bean = new DeptRole();
	List<DeptRole> dataRows = new ArrayList<DeptRole>();
	HttpServletRequest request;
	HttpServletResponse response;
	DeptRoleImpl impl = new DeptRoleImpl();

	public String DeptRole_list() {
		if (bean.getOper() == 0) {
			return "DeptRole_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				String result = impl.getDeptRoleList(
						bean.getCurPage(),
						bean.getPerNumber(),
						bean.getOrderByField(),
						bean.getOrderByType(),
						bean.getSearchField(),
						bean.getSearchValue(),
						bean);
				
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String DeptRole_add() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		if (bean.getOper() == 0) {
			return "DeptRole_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				//bean.setCompanyName(userInfo.getEmployee().getOrgId());
				impl.addDeptRole(bean);
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
				boolean flag = impl.addCheck(bean.getDeptRoleName());
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
	
	public String DeptRole_edit() {
		
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = ZTreeImpl.getInstance().getRoleTree();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "DeptRole_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getDeptRoleById(bean.getDeptRoleId());
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
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
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
				boolean flag = impl.addCheck(bean.getDeptRoleName());
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
	
	public String DeptRole_detail() {
		List<Menu> menuList = impl.getMenuAllList(bean.getDeptRoleId());
		request.setAttribute("menuList", menuList);
		return "DeptRole_detail";
	}
	
	/**
	 * 角色授权
	 * @return
	 */
	public String DeptRole_accredit() {
		String roleId = bean.getDeptRoleId();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			String[] selectedPsId = impl.getPsByDeptRoleId(roleId);
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
				System.out.println("1111111111111111111111");
				impl.accreditDeptRole(bean);
				System.out.println("222222222222222222222");
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
			String[] selectedPsId = impl.getPsByDeptRoleId(roleId);
			List<Menu> menuList = impl.getMenuAllList();
			request.setAttribute("selectedPsId", selectedPsId);
			request.setAttribute("menuList", menuList);
			
			return null;
			
		}
		return "DeptRole_accredit";
	}
	
	
	public String DeptRole_del(){
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "DeptRole_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delDeptRole(bean);
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


	public DeptRole getModel() {
		return bean;
	}
	
	public List<DeptRole> getDataRows() {
		return dataRows;
	}

	@Override
	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}
	
	public void setDataRows(List<DeptRole> dataRows) {
		this.dataRows = dataRows;
	}
}
