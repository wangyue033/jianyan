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
import com.dhcc.popedom.domain.Dept;
import com.dhcc.popedom.impl.DeptImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;
import framework.dhcc.tree.bean.ZTreeBean;


public class DeptAction extends ActionSupport implements ModelDriven<Dept>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Dept bean = new Dept();

	List<Dept> dataRows = new ArrayList<Dept>();

	HttpServletRequest request;

	HttpServletResponse response;

	DeptImpl impl = new DeptImpl();

	Page page = new Page();
	
	//机构下的部门列表
	public String Dept_list() {
		if (bean.getOper() == 0) {
			return "Dept_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				String compId = userInfo.getEmployee().getCompanyId();
				if(compId != null && compId.length()>=10){
					compId = compId.substring(0,10);
					bean.setParentId(compId);
					String result = impl.getDeptList(bean,bean.getCurPage(), bean
							.getPerNumber(), bean.getOrderByField(), bean
							.getOrderByType(), bean.getSearchField(), bean
							.getSearchValue());
					pw.print(result);
					pw.close();
				}else{
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public String Dept_add() throws Exception {
		/*List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getDeptTreeRoot();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);*/
		if (bean.getOper() == 0) {
			return "Dept_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				String parentId = userInfo.getEmployee().getCompanyId();
				bean.setCompanyId(impl.getDeptId(parentId));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setParentId(userInfo.getEmployee().getCompanyId());
				impl.addDept(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.dept.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.dept.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			DeptImpl deptImpl = new DeptImpl();
			String companyName = request.getParameter("companyName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = deptImpl.checkDeptName(companyName);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else{
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.dept.add.noparam"));
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
	
	//修改机构下的部门
	public String Dept_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "Dept_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getDeptById(bean.getCompanyId());
				map.put("msgInfo", getText("popedom.dept.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.dept.get.failure"));
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
				map = impl.editDept(bean);
				map.put("msgInfo", getText("popedom.dept.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.dept.edit.failure"));
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
				map.put("msgInfo", getText("popedom.dept.edit.noparam"));
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
	
	//删除机构下的部门
	public String Dept_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "Dept_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delDept(bean);
				map.put("msgInfo", getText("popedom.dept.delete.success"));
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
					map.put("msgInfo", getText("popedom.dept.delete.fk"));
				}else{
					map.put("msgInfo", getText("popedom.dept.delete.failure"));
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
				map.put("msgInfo", getText("popedom.dept.delete.noparam"));
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
	
	//机构下部门的详情
	public String Dept_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "Dept_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getDeptById(bean.getCompanyId());
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

	//刷新左边的机构树
	public String Dept_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getDeptTreeRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "Dept_tree";
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Dept getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<Dept> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<Dept> dataRows) {
		this.dataRows = dataRows;
	}

}
