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
import com.dhcc.inspect.domain.JdPlan;
import com.dhcc.inspect.impl.JdPlanImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class JdPlanAction extends ActionSupport implements ModelDriven<JdPlan>,
		ServletRequestAware, ServletResponseAware {
	/**
	 * wangyue 抽查计划信息
	 */
	private static final long serialVersionUID = 1L;

	JdPlan bean = new JdPlan();

	List<JdPlan> dataRows = new ArrayList<JdPlan>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdPlanImpl impl = new JdPlanImpl();

	public String JdPlan_list() {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			//System.out.println("items="+bean.getItems());
			request.setAttribute("items", bean.getItems());
			return "JdPlan_list";
		} else {
			try {
				PrintWriter pw = response.getWriter();
				String result = impl.getJdPlanList(bean.getCurPage(),
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

	public String JdPlan_add() throws IOException {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			return "JdPlan_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				// bean.setCompId(userInfo.getEmployee().getCompanyId());
				// bean.setCompName(userInfo.getEmployee().getCompanyName());
				String compId = userInfo.getEmployee().getCompanyId();
				if (compId != null && !"".equals(compId)) {
					compId = compId.substring(0, 10);
					bean.setOptId(userInfo.getEmployee().getEmployeeId());
					bean.setOptName(userInfo.getEmployee().getUserName());
					bean.setCompId(compId);
					impl.addJdPlan(bean);
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("msg", "true");
					map.put("msgInfo", getText("inspect.jdPlan.add.success"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				} else {
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("msg", "false");
					map.put("msgInfo", getText("inspect.jdPlan.add.failure"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlan.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 2) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkPlanNo(bean.getPlanNo());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else if (bean.getOper() == 3) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkPlanName(bean.getPlanName());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else {
			try {
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlan.add.noparam"));
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

	public String JdPlan_edit() throws IOException {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlan_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdPlanById(bean.getPlanId());
				map.put("msgInfo", getText("inspect.jdPlan.editGet.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlan.editGet.failure"));
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
				bean.setCompId(userInfo.getEmployee().getCompanyId());
				bean.setCompName(userInfo.getEmployee().getCompanyName());
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editJdPlan(bean);
				map.put("msgInfo", getText("inspect.jdPlan.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlan.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckPlanNo(bean.getPlanNo(),
					bean.getPlanId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else if (bean.getOper() == 4) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckPlanName(bean.getPlanName(),
					bean.getPlanId());
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
				map.put("msgInfo", getText("inspect.jdPlan.edit.noparam"));
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

	public String JdPlan_del() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlan_del";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				String path = request.getSession().getServletContext()
						.getRealPath("");
				path = path.substring(0, path.lastIndexOf("\\"));
				bean.setPath(path);
				map = impl.delJdPlan(bean);
				map.put("msgInfo", getText("inspect.jdPlan.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if (e.getMessage().contains("foreign key")) {
					map.put("msgInfo", getText("inspect.jdPlan.delete.fk"));
				} else {
					map.put("msgInfo", getText("inspect.jdPlan.delete.failure"));
				}

				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlan.delete.noparam"));
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

	public String JdPlan_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlan_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanById(bean.getPlanId());
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

	/**
	 * 任务制定-选择计划
	 * 
	 * @return
	 */
	public String JdPlan_jsonTree() {
		if (bean.getOper() == 0) {
			return "JdPlan_jsonTree";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getJdPlanJsonList2(bean.getCurPage(),
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

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public JdPlan getModel() {
		return bean;
	}

	public List<JdPlan> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdPlan> dataRows) {
		this.dataRows = dataRows;
	}

}
