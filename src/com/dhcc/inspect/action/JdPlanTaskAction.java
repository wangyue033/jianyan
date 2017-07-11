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

import com.dhcc.inspect.domain.JdPlanTask;
import com.dhcc.inspect.impl.JdPlanTaskImpl;
import com.dhcc.login.UserInfoBean;
import com.dhcc.popedom.domain.Comp;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class JdPlanTaskAction extends ActionSupport implements
		ModelDriven<JdPlanTask>, ServletRequestAware, ServletResponseAware {
	/**
	 * wangyue 抽检任务信息
	 */
	private static final long serialVersionUID = 1L;

	JdPlanTask bean = new JdPlanTask();

	List<JdPlanTask> dataRows = new ArrayList<JdPlanTask>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdPlanTaskImpl impl = new JdPlanTaskImpl();

	public String JdPlanTask_list() {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdPlanTask_list";
		} else {
			try {
				PrintWriter pw = response.getWriter();
				String result = impl.getJdPlanTaskList(bean.getCurPage(),
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

	public String JdPlanTask_add() throws IOException {
		response.setCharacterEncoding("UTF-8");
		if (bean.getOper() == 0) {
			return "JdPlanTask_add";
		} else if (bean.getOper() == 10) {
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
				String taskNo = impl.getTaskNoByCompId(userInfo.getEmployee().getCompanyId());
				bean.setTaskNo(taskNo);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("record", bean);
				List<Comp> list = impl.getCompList();
				map.put("list", list);
				map.put("msg", "true");
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo",
						getText("inspect.jdPlanTask.getTaskNo.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				String compId = userInfo.getEmployee().getCompanyId();
				if (compId != null && !"".equals(compId)) {
					compId = compId.substring(0, 10);
					bean.setCompId(compId);
					bean.setCompName(impl.getCompName(bean.getCompId()));
					System.out.println(bean.getCompName()+"组织机构");
					bean.setOptId(userInfo.getEmployee().getEmployeeId());
					bean.setOptName(userInfo.getEmployee().getUserName());
					impl.addJdPlanTask(bean);
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("msg", "true");
					map.put("msgInfo",
							getText("inspect.jdPlanTask.add.success"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				} else {
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					map.put("msg", "false");
					map.put("msgInfo",
							getText("inspect.jdPlanTask.add.failure"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanTask.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 2) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkTaskNo(bean.getTaskNo());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else if (bean.getOper() == 3) {
			PrintWriter writer = response.getWriter();
			boolean flag = impl.checkTaskName(bean.getTaskName());
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
				map.put("msgInfo", getText("inspect.jdPlanTask.add.noparam"));
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

	public String JdPlanTask_edit() throws IOException {
		response.setCharacterEncoding("UTF-8");
		
		int oper = bean.getOper();
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanTask_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdPlanTaskById(bean.getTaskId());
				map.put("msgInfo",
						getText("inspect.jdPlanTask.editGet.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo",
						getText("inspect.jdPlanTask.editGet.failure"));
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
				String compId = userInfo.getEmployee().getCompanyId();
				if (compId != null && !"".equals(compId)) {
					compId = compId.substring(0, 10);
					bean.setCompId(compId);
					bean.setCompName(userInfo.getEmployee().getCompanyName());
					bean.setOptId(userInfo.getEmployee().getEmployeeId());
					bean.setOptName(userInfo.getEmployee().getUserName());
					map = impl.editJdPlanTask(bean);
					map.put("msgInfo",
							getText("inspect.jdPlanTask.edit.success"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				} else {
					map = new HashMap<String, Object>();
					map.put("msg", "false");
					map.put("msgInfo",
							getText("inspect.jdPlanTask.edit.failure"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				}

			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanTask.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3) {
			String taskNo = request.getParameter("taskNo");
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckTaskNo(taskNo, bean.getTaskId());
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else if (bean.getOper() == 4) {
			String taskName = request.getParameter("taskName");
			PrintWriter writer = response.getWriter();
			boolean flag = impl.editCheckTaskName(taskName, bean.getTaskId());
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
				map.put("msgInfo", getText("inspect.jdPlanTask.edit.noparam"));
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

	public String JdPlanTask_del() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlanTask_del";
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
				map = impl.delJdPlanTask(bean);
				map.put("msgInfo", getText("inspect.jdPlanTask.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if (e.getMessage().contains("foreign key")) {
					map.put("msgInfo", getText("inspect.jdPlanTask.delete.fk"));
				} else {
					map.put("msgInfo",
							getText("inspect.jdPlanTask.delete.failure"));
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
				map.put("msgInfo", getText("inspect.jdPlanTask.delete.noparam"));
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

	public String JdPlanTask_handle() {
		response.setCharacterEncoding("UTF-8");
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdPlanTask_handle";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setHandleOptId(userInfo.getEmployee().getEmployeeId());
				bean.setHandleOptName(userInfo.getEmployee().getUserName());
				bean.setCompId(userInfo.getEmployee().getCompanyId());
				bean.setCompName(userInfo.getEmployee().getCompanyName());
				map = impl.handleJdPlanTask(bean);
				map.put("msgInfo", getText("inspect.jdPlanTask.handle.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdPlanTask.handle.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
		}
		return null;
	}

	public String JdPlanTask_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdPlanTask_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdPlanTaskById(bean
						.getTaskId());
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

	public String JdPlanTask_jsonTree() {
		if (bean.getOper() == 0) {
			return "JdPlanTask_jsonTree";
		}
		return null;
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
	public JdPlanTask getModel() {
		return bean;
	}

	public List<JdPlanTask> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdPlanTask> dataRows) {
		this.dataRows = dataRows;
	}

}
