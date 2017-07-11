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

import com.dhcc.inspect.domain.JdAreaInfo;
import com.dhcc.inspect.impl.JdAreaInfoImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-23    
 * 区域信息Action
 */
public class JdAreaInfoAction extends ActionSupport implements ModelDriven<JdAreaInfo>,
ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	
	JdAreaInfo bean = new JdAreaInfo();

	List<JdAreaInfo> dataRows = new ArrayList<JdAreaInfo>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdAreaInfoImpl impl = new JdAreaInfoImpl();

	Page page = new Page();
	
	//区域信息列表
	public String JdAreaInfo_list() {
		if (bean.getOper() == 0) {
			return "JdAreaInfo_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getJdAreaInfoList(bean.getCurPage(), bean
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
	
	//区域信息添加
	public String JdAreaInfo_add() throws Exception {
		//区域
		List<ZTreeBean> areaTreeList = new ArrayList<ZTreeBean>();
		areaTreeList =impl.getAreaTree();
		JSONArray obj1 = JSONArray.fromObject(areaTreeList);
		request.setAttribute("areaList", obj1);
		
		if (bean.getOper() == 0) {
			return "JdAreaInfo_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				//bean.setAreaId(impl.getSumList3(bean.getParentId()));
				impl.addJdAreaInfo(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				//map.put("qybh", bean.getAreaId());
				map.put("msgInfo", getText("inspect.jdAreaInfo.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdAreaInfo.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			JdAreaInfoImpl jdAreaInfoImpl = new JdAreaInfoImpl();
			String areaName = request.getParameter("areaName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdAreaInfoImpl.checkAreaName(areaName);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if(bean.getOper() == 3){
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result= impl.getSumList(bean.getAreaId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else if(bean.getOper()==4){
			JdAreaInfoImpl jdAreaInfoImpl = new JdAreaInfoImpl();
			String areaId = request.getParameter("areaId");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdAreaInfoImpl.checkAreaId(areaId);
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
				map.put("msgInfo", getText("inspect.jdAreaInfo.add.noparam"));
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
	
	//删除区域信息
	public String JdAreaInfo_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdAreaInfo_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJdAreaInfo(bean);
				map.put("msgInfo", getText("inspect.jdAreaInfo.delete.success"));
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
					map.put("msgInfo", getText("inspect.jdAreaInfo.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.jdAreaInfo.delete.failure"));
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
				map.put("msgInfo", getText("inspect.jdAreaInfo.delete.noparam"));
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
	
	//区域信息详情
	public String JdAreaInfo_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdAreaInfo_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdAreaById(bean.getAreaId());
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
	
	//区域信息修改
	public String JdAreaInfo_edit() throws IOException {
		//区域
		List<ZTreeBean> areaTreeList = new ArrayList<ZTreeBean>();
		areaTreeList =impl.getAreaTree();
		JSONArray obj1 = JSONArray.fromObject(areaTreeList);
		request.setAttribute("areaList", obj1);
		
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdAreaInfo_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdAreaById(bean.getAreaId());
				map.put("msgInfo", getText("inspect.jdAreaInfo.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdAreaInfo.get.failure"));
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
				map = impl.editJdAreaInfo(bean);
				map.put("msgInfo", getText("inspect.jdAreaInfo.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdAreaInfo.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 3) {
			JdAreaInfoImpl jdAreaInfoImpl = new JdAreaInfoImpl();
			String areaName = request.getParameter("areaName");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdAreaInfoImpl.checkEditJdAreaName(areaName,bean.getAreaId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if(bean.getOper() == 4){
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				String result= impl.getSumList2(bean.getAreaId(),bean.getStrId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else if (oper == 5) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				String result= impl.checkEditJdAreaId(bean.getAreaId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdAreaInfo.edit.noparam"));
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
	
	public String JdAreaInfo_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaTree();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_tree";
	}
	
	/*public String JdAreaInfo_jsonTree() {
		if (bean.getOper() == 0) {
			return "JdAreaInfo_jsonTree";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getJdAreaInfoList(bean.getCurPage(), bean
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
	}*/
	
	public String JdAreaInfo_jsonTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaAllRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_jsonTree";
	}
	//抽查区域
	public String JdAreaInfo_jsonTree1() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaAllRoot(bean.getAreaId());
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_jsonTree1";
	}
	
	//区域多选
	public String JdAreaInfo_jsonTreeMore() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaAllRootCheck();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_jsonTreeMore";
	}
	
	public String JdAreaInfo_jsonTreeOut() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaAllRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_jsonTreeOut";
	}
	
	public String JdAreaInfo_jsonTreeInt() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getAreaAllRoot();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdAreaInfo_jsonTreeInt";
	}

	public List<JdAreaInfo> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdAreaInfo> dataRows) {
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
	public JdAreaInfo getModel() {
		return bean;
	}
	
}
