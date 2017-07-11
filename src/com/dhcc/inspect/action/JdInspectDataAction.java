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

import com.dhcc.inspect.domain.JdInspectData;
import com.dhcc.inspect.impl.JdInspectDataImpl;
import com.dhcc.inspect.impl.JdPlanTaskImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据填报Action
 */
public class JdInspectDataAction extends ActionSupport implements ModelDriven<JdInspectData>,
ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	JdInspectData bean = new JdInspectData();

	List<JdInspectData> dataRows = new ArrayList<JdInspectData>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdInspectDataImpl impl = new JdInspectDataImpl();
	
	JdPlanTaskImpl impl1 = new JdPlanTaskImpl();

	Page page = new Page();
	
	//检验数据信息列表
	public String JdInspectData_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectData_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				String result = impl.getJdInspectDataList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompanyId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//检验数据信息添加
	public String JdInspectData_add() throws Exception {
		//产品
		List<ZTreeBean> productTreeList = new ArrayList<ZTreeBean>();
		productTreeList =impl1.getProductTree();
		JSONArray obj1 = JSONArray.fromObject(productTreeList);
		request.setAttribute("productList", obj1);
				
		if (bean.getOper() == 0) {
			return "JdInspectData_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				impl.addJdInspectData(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdInspectData.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectData.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			JdInspectDataImpl jdInspectDataImpl = new JdInspectDataImpl();
			String idmTitle = request.getParameter("idmTitle");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdInspectDataImpl.checkJdInspectDataName(idmTitle);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		}else if(bean.getOper() == 3){
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdItemById(bean.getInspectId());
				map.put("msgInfo", getText("inspect.jdItem.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdItem.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else if(bean.getOper() == 4){
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				String result = impl.getDataList2(bean);
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.data2.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else if(bean.getOper() == 5){
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				String result = impl.getProductList(bean);
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.product.get.failure"));
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
				map.put("msgInfo", getText("inspect.jdInspectData.add.noparam"));
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
	
	//删除检验数据信息
	public String JdInspectData_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectData_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				map = impl.delJdInspectData(bean);
				map.put("msgInfo", getText("inspect.jdInspectData.delete.success"));
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
					map.put("msgInfo", getText("inspect.jdInspectData.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.jdInspectData.delete.failure"));
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
				map.put("msgInfo", getText("inspect.jdInspectData.delete.noparam"));
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
	public String JdInspectData_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectData_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdInspectDataById(bean.getInspectId());
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
	
	//检验数据信息修改
	public String JdInspectData_edit() throws IOException {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectData_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdInspectDataById(bean.getInspectId());
				map.put("msgInfo", getText("inspect.jdInspectData.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectData.get.failure"));
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
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				map = impl.editJdInspectData(bean);
				map.put("msgInfo", getText("inspect.jdInspectData.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectData.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 3) {
			JdInspectDataImpl jdInspectDataImpl = new JdInspectDataImpl();
			String idmTitle = request.getParameter("idmTitle");
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jdInspectDataImpl.checkEditJdInspectDataName(idmTitle,bean.getDataId());
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
				map.put("msgInfo", getText("inspect.jdInspectData.edit.noparam"));
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
	
	//提交检验数据信息待审核
	public String JdInspectData_submit() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectData_submit";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.submitJdInspectData(bean);
				map.put("msgInfo", getText("inspect.jdInspectData.submit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectData.submit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(oper ==2){
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.submitZJ(bean);
				map.put("msgInfo", getText("inspect.jdInspectData.submit.success"));
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
					map.put("msgInfo", getText("inspect.jdInspectData.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.jdInspectData.submit.failure"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}else {
			return null;
		}
	}

	public List<JdInspectData> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdInspectData> dataRows) {
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
	public JdInspectData getModel() {
		return bean;
	}
	
}
