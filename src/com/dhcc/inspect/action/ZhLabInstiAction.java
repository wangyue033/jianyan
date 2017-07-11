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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.ZhLabInsti;
import com.dhcc.inspect.impl.ZhLabInstiImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-15    
 ***********************
 */
public class ZhLabInstiAction extends ActionSupport implements ModelDriven<ZhLabInsti>,
ServletRequestAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	
	ZhLabInsti bean = new ZhLabInsti();

	List<ZhLabInsti> dataRows = new ArrayList<ZhLabInsti>();

	HttpServletRequest request;

	HttpServletResponse response;

	ZhLabInstiImpl impl = new ZhLabInstiImpl();

	Page page = new Page();
	
	//初始化左树及右列表
	public String ZhLabInsti_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "ZhLabInsti_tree");
		request.setAttribute("rightAction", "ZhLabInsti_list");
		return "tree_frame";
	}
	
	//检测机构信息列表
	public String ZhLabInsti_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "ZhLabInsti_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getZhLabInstiList(bean,bean.getCurPage(), bean
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
	
	//企业信息添加
	public String ZhLabInsti_add() throws Exception {
		if (bean.getOper() == 0) {
			return "ZhLabInsti_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addZhLabInsti(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.zhLabInsti.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.zhLabInsti.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			ZhLabInstiImpl zhLabInstiImpl = new ZhLabInstiImpl();
			String labName = request.getParameter("labName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = zhLabInstiImpl.checkZhLabInstiName(labName);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if(bean.getOper() == 3){
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				String result = impl.getMajorList();
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.major.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else{
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.zhLabInsti.add.noparam"));
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
	
	//删除检测机构信息
	public String ZhLabInsti_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "ZhLabInsti_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delZhLabInsti(bean);
				map.put("msgInfo", getText("inspect.zhLabInsti.delete.success"));
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
					map.put("msgInfo", getText("inspect.zhLabInsti.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.zhLabInsti.delete.failure"));
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
				map.put("msgInfo", getText("inspect.zhLabInsti.delete.noparam"));
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
	
	//企业信息详情
	public String ZhLabInsti_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ZhLabInsti_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getZhLabInstiById(bean.getLabId());
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
	
	//检测机构基础信息修改
	public String ZhLabInsti_edit() throws IOException {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ZhLabInsti_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getZhLabInstiById(bean.getLabId());
				map.put("msgInfo", getText("inspect.zhLabInsti.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.zhLabInsti.get.failure"));
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
				map = impl.editZhLabInsti(bean);
				map.put("msgInfo", getText("inspect.zhLabInsti.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.zhLabInsti.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 3) {
			ZhLabInstiImpl zhLabInstiImpl = new ZhLabInstiImpl();
			String labName = request.getParameter("labName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = zhLabInstiImpl.checkEditLabName(labName,bean.getLabId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.zhLabInsti.edit.noparam"));
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
	
	//刷新检测机构专业类别信息(左边的树)
	public String ZhLabInsti_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getMajorAllRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "ZhLabInsti_tree";
	}
	
	//获取检测机构专业类别信息
	public String ZhLabInsti_jsonTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getMajorAllRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "ZhLabInsti_jsonTree";
	}
	
	public List<ZhLabInsti> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<ZhLabInsti> dataRows) {
		this.dataRows = dataRows;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
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
	public ZhLabInsti getModel() {
		return bean;
	}
	
}
