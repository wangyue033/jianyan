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

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdInspectHandle;
import com.dhcc.inspect.impl.JdInspectHandleImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class JdInspectHandleAction extends ActionSupport implements
ModelDriven<JdInspectHandle>, ServletRequestAware, ServletResponseAware  {
	
	/**
	 * @author longxl
	 * 检验任务分派
	 */
	private static final long serialVersionUID = -5033736961721974318L;
	
	JdInspectHandle bean = new JdInspectHandle();
	List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
	HttpServletRequest request;
	HttpServletResponse response;
	JdInspectHandleImpl impl  = new JdInspectHandleImpl();

	public String JdInspectHandle_list () {
		String inspectId = (String)request.getParameter("inspectId");
		bean.setInspectId(inspectId);
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectHandle_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompId(userInfo.getEmployee().getOrgId());
				bean .setOptId(userInfo.getEmployee().getEmployeeId());
				String result = impl.getJdInspectHandleList(bean,bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId(),bean.getOptId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdInspectHandle_add(){
		if (bean.getOper() == 0) {
			return "JdInspectHandle_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				//bean.setProductId(impl.getProductId(bean.getParentId()));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0,10));
				impl.addJdInspectHandle(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				if(("1").equals(bean.getOutFlag())){
					map.put("msgInfo", getText("popedom.inspectHandle.add.success"));
				}else if(("2").equals(bean.getOutFlag())){
					map.put("msgInfo", getText("popedom.inspectHandle.out.success"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				if(("1").equals(bean.getOutFlag())){
					map.put("msgInfo", getText("popedom.inspectHandle.add.failure"));
				}else if(("2").equals(bean.getOutFlag())){
					map.put("msgInfo", getText("popedom.inspectHandle.out.failure"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 2) {
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdItemById(bean.getTaskId());
				map.put("msgInfo", getText("inspect.inspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.inspectHandle.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				String result = impl.getItemList();
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
		} else if (bean.getOper() == 4) {
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getItemFaById(bean.getItemId(),bean.getTaskId());
				map.put("msgInfo", getText("inspect.inspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.inspectHandle.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else if (bean.getOper() == 5) {
			Map<String, Object> map = null;
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				map = impl.getBillInfo(bean.getBillId(),bean);
				map.put("msgInfo", getText("popedom.InspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectHandle.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.inspectHandle.add.noparam"));
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
	
	public String JdInspectHandle_add1 () throws IOException {
		JdInspectHandleImpl impl = new JdInspectHandleImpl();
		String billId = request.getParameter("billId");
		String inspectId = request.getParameter("inspectId");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.checkPlanNo(inspectId, billId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		} else {
			flag = impl.checkPlanNo(inspectId, billId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdInspectHandle_add2 () throws IOException {
		JdInspectHandleImpl impl = new JdInspectHandleImpl();
		String billId = request.getParameter("billId");
		String itaTitle = request.getParameter("itaTitle");
		PrintWriter writer = response.getWriter();
		boolean flag;
		if (bean.getOper() == 2) {
			flag = impl.editCheckPlanNo(itaTitle, billId, 2);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			} 
		} else {
			flag = impl.checkPlanNo(itaTitle, billId, 1);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
		return null;
	}
	
	public String JdInspectHandle_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectHandle_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJdInspectHandle(bean);
				map.put("msgInfo", getText("popedom.inspectHandle.delete.success"));
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
					map.put("msgInfo", getText("popedom.inspectHandle.delete.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectHandle.delete.failure"));
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
				map.put("msgInfo", getText("popedom.inspectHandle.delete.noparam"));
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
	
	public String JdInspectHandle_fenpai() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdInspectHandle_fenpai";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.fenpaiJdInspectHandle(bean);
				map.put("msgInfo", getText("popedom.inspectHandle.fenpai.success"));
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
					map.put("msgInfo", getText("popedom.inspectHandle.fenpai.fk"));
				} else {
					map.put("msgInfo",
							getText("popedom.inspectHandle.fenpai.failure"));
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
				map.put("msgInfo", getText("popedom.inspectHandle.fenpai.noparam"));
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
	
	public String JdInspectHandle_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectHandle_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl
						.getJdInspectHandleById(bean.getInspectId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (oper == 2) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdInspectItemById(bean.getInspectId());
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
	
	public String JdInspectHandle_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectHandle_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdInspectHandleById(bean.getInspectId());
				map.put("msgInfo", getText("popedom.InspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectHandle.get.failure"));
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
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				map = impl.editJdInspectHandle(bean);
				map.put("msgInfo", getText("popedom.InspectHandle.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectHandle.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3) {
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdInspectById(bean.getInspectId());
				map.put("msgInfo", getText("inspect.inspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.inspectHandle.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 4) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				String result = impl.getItemList();
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
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.InspectHandle.edit.noparam"));
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
	
	//检验任务分派时新添加检测标准和检测项目
	public String JdInspectHandle_addItem() throws Exception {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			List<JdInspectHandle> menuList = impl.getInspectItemList(bean.getProductId());
			request.setAttribute("selectedPsId", bean.getSelectedPsId().split(","));
			request.setAttribute("menuList", menuList);
			return "JdInspectHandle_addItem";
		}else if(bean.getOper() == 2){
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getNewItems(bean.getStandardId());
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
		}else {
			List<JdInspectHandle> menuList = impl.getInspectItemList(bean.getProductId());
			request.setAttribute("selectedPsId", bean.getSelectedPsId().split(","));
			request.setAttribute("menuList", menuList);
			return "JdInspectHandle_addItem";
		}
	}
	
	//选择方案中的检测项目
	public String JdInspectHandle_addItemfa() {
		if (bean.getOper() == 0) {
			return "JdInspectHandle_addItemfa";
		} else {
			PrintWriter pw = null;
			Map<String, Object> map = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = impl.getJdItemListById(bean,bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue());
				map.put("msgInfo", getText("inspect.inspectHandle.get.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.inspectHandle.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}
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
	public JdInspectHandle getModel() {
		// TODO Auto-generated method stub
		return bean;
	}

	public List<JdInspectHandle> getList() {
		return list;
	}

	public void setList(List<JdInspectHandle> list) {
		this.list = list;
	}

}
