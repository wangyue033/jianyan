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

import com.dhcc.inspect.domain.ProductType;
import com.dhcc.inspect.impl.ProductTypeImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

public class ProductTypeAction extends ActionSupport implements ModelDriven<ProductType>,
ServletRequestAware, ServletResponseAware {

	/**
	 * @author longxl
	 * 产品分类
	 */
	private static final long serialVersionUID = -7149544145684257995L;

	ProductType bean = new ProductType();
	List<ProductType> list = new ArrayList<ProductType>();
	HttpServletRequest request;
	HttpServletResponse response;
	ProductTypeImpl impl  = new ProductTypeImpl();
	
	public String ProductType_list() {
		String parentId = (String)request.getParameter("parentId");
		bean.setParentId(parentId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "ProductType_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getProductTypeList(bean,bean.getCurPage(), bean
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
	
	public String ProductType_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ProductType_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl
						.getProdtypeById(bean.getTypeId());
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
	
	public String ProductType_add(){
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getProdtypeTree();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		
		if (bean.getOper() == 0) {
			return "ProductType_add";
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
				impl.addProductType(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.productType.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.productType.add.failure"));
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
				map.put("msgInfo", getText("popedom.productType.add.noparam"));
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
	
	public String ProductType_add1 () throws IOException {
		ProductTypeImpl impl = new ProductTypeImpl();
		String typeName = request.getParameter("typeName1");
		String prodtypeId = request.getParameter("prodtypeId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.checkPlanNo(typeName, prodtypeId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		} else {
			flag = impl.checkPlanNo(typeName, prodtypeId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String ProductType_add2 () throws IOException {
		ProductTypeImpl impl = new ProductTypeImpl();
		String shortName = request.getParameter("shortName");
		String prodtypeId = request.getParameter("prodtypeId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.editCheckPlanNo(shortName, prodtypeId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			} 
		} else {
			flag = impl.editCheckPlanNo(shortName, prodtypeId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String ProductType_edit() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getProdtypeTree();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "ProductType_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getProdtypeById(bean.getTypeId());
				map.put("msgInfo", getText("popedom.productType.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.productType.get.failure"));
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
				map = impl.editProdtype(bean);
				map.put("msgInfo", getText("popedom.productType.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.productType.edit.failure"));
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
				map.put("msgInfo", getText("popedom.productType.edit.noparam"));
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
	
	public String ProductType_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "ProductType_del";
		} else if (oper == 1) {
			boolean msg = impl.getDelProInfo(bean.getTypeId());
			try {
				pw = response.getWriter();
				pw.write(msg + "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delProductType(bean);
				map.put("msgInfo", getText("popedom.productType.delete.success"));
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
					map.put("msgInfo", getText("popedom.productType.delete.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.productType.delete.failure"));
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
				map.put("msgInfo", getText("popedom.productType.delete.noparam"));
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
	
	public String ProductType_treeP () {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getProdtypeTree();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("ztreeList", jsonArray);
		return "ProductType_treeP";
	}
	
	//选择抽查产品类别
	public String ProductType_jsonTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		if(bean.getOper()==0){
			bean.setTypeName("");
		}
		
		try {
			treeList = impl.getTypeAllRoot(bean.getTypeName());
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "ProductType_jsonTree";
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
	public ProductType getModel() {
		return bean;
	}

	public List<ProductType> getList() {
		return list;
	}

	public void setList(List<ProductType> list) {
		this.list = list;
	}

}
