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

import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.inspect.impl.JdProductInfoImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;
/**
 * 
 * @author longxl
 *
 */
public class JdProductInfoAction extends ActionSupport implements
ModelDriven<JdProductInfo>, ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = -7952290426921440857L;

	JdProductInfo bean = new JdProductInfo();
	List<JdProductInfo> list = new ArrayList<JdProductInfo>();
	HttpServletRequest request;
	HttpServletResponse response;
	JdProductInfoImpl impl  = new JdProductInfoImpl();
	
	
//	public String JdProductInfo_init() {
//		String items = bean.getProductId();
//		request.setAttribute("items", items);
//		request.setAttribute("leftAction", "JdProductInfo_tree");
//		request.setAttribute("rightAction", "JdProductInfo_list");
//		return "tree_frame";
//	}
	
	public String JdProductInfo_list() {
//		String parentId = (String)request.getParameter("parentId");
//		bean.setParentId(parentId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdProductInfo_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getJdProductInfoList(bean,bean.getCurPage(), bean
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
	
	public String JdProductInfo_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getTreeCanParentRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdProductInfo_tree";
	}
	
	public String JdProductInfo_add(){
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getTreeCanParentRoot1();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		
		List<ZTreeBean> treeList1 = new ArrayList<ZTreeBean>();
		treeList1 = impl.getCompTree();
		JSONArray obj1 = JSONArray.fromObject(treeList1);
		request.setAttribute("treeList1", obj1);
		
		if (bean.getOper() == 0) {
			return "JdProductInfo_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
//				bean.setProductId(impl.getProductId(bean.getParentId()));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addJdProductInfo(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.product.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.product.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.product.add.noparam"));
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
	
	public String JdProductInfo_add1 () throws IOException {
		JdProductInfoImpl impl = new JdProductInfoImpl();
		String productName = request.getParameter("productName");
		String productId = request.getParameter("productId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.checkPlanNo(productName, productId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		} else {
			flag = impl.checkPlanNo(productName, productId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdProductInfo_add2 () throws IOException {
		JdProductInfoImpl impl = new JdProductInfoImpl();
		String shortName = request.getParameter("shortName");
		String productId = request.getParameter("productId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.editCheckPlanNo(shortName, productId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			} 
		} else {
			flag = impl.editCheckPlanNo(shortName, productId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdProductInfo_edit() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getTreeCanParentRoot1();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		
		List<ZTreeBean> treeList1 = new ArrayList<ZTreeBean>();
		treeList1 = impl.getCompTree();
		JSONArray obj1 = JSONArray.fromObject(treeList1);
		request.setAttribute("treeList1", obj1);
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdProductInfo_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdProductById(bean.getProductId());
				map.put("msgInfo", getText("popedom.product.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.product.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editJdProductInfo(bean);
				map.put("msgInfo", getText("popedom.product.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.product.edit.failure"));
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
				map.put("msgInfo", getText("popedom.product.edit.noparam"));
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
	
	public String JdProductInfo_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdProductInfo_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJdProductInfo(bean);
				map.put("msgInfo", getText("popedom.product.delete.success"));
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
					map.put("msgInfo", getText("popedom.product.delete.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.product.delete.failure"));
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
				map.put("msgInfo", getText("popedom.product.delete.noparam"));
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
	
	public String JdProductInfo_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdProductInfo_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl
						.getJdProductById(bean.getProductId());
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
	
	public String JdProductInfo_treeP () {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getTreeCanParentRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		request.setAttribute("ztreeList", treeList);
		return "JdProductInfo_treeP";
	}
	
	public String JdProductInfo_treel () {
		PrintWriter pw = null;
		try {
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
//			boolean b = impl.productLength(bean.getId());
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("msg", "false");
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setCharacterEncoding("UTF-8");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("msg", "false");
			map.put("msgInfo", getText("popedom.product.add.failure"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		}
		return null;
	}
	
	public String JdProductInfo_comp() {
		PrintWriter pw = null;
		try {
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			String result = impl.getCompList(bean);
			pw.print(result);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setCharacterEncoding("UTF-8");
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("msg", "false");
			map.put("msgInfo", getText("inspect.data.get.failure"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		}
		return null;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request = arg0;
		
	}

	@Override
	public JdProductInfo getModel() {
		// TODO Auto-generated method stub
		return bean;
	}
	
	public List<JdProductInfo> getList() {
		return list;
	}
	public void setList(List<JdProductInfo> list) {
		this.list = list;
	}

}
