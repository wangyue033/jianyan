package com.dhcc.popedom.action;

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

import com.dhcc.login.UserInfoBean;
import com.dhcc.popedom.domain.Job;
import com.dhcc.popedom.impl.JobImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import framework.dhcc.db.DBFacade;

/**
 * 
 * @author WYH
 *
 */
public class JobAction extends ActionSupport implements ModelDriven<Job>,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	Job bean = new Job();
	List<Job> dataRows = new ArrayList<Job>();
	HttpServletRequest request;
	HttpServletResponse response;
	JobImpl impl = new JobImpl();

	public String Job_list() {
		if (bean.getOper() == 0) {
			return "Job_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				String compId = userInfo.getEmployee().getCompanyId();
				if(compId != null && compId.length()>=10){
					compId = compId.substring(0,10);
					bean.setCompanyId(compId);
					String result = impl.getJobList(bean,bean.getCurPage(),
							bean.getPerNumber(), bean.getOrderByField(),
							bean.getOrderByType(), bean.getSearchField(),
							bean.getSearchValue());
					pw.print(result);
					pw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// 添加职位
	public String Job_add() throws Exception {
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		if (bean.getOper() == 0) {
			return "Job_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			response.setCharacterEncoding("UTF-8");
			try {
				pw = response.getWriter();
				bean.setJobId(DBFacade.getInstance().getID());
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				impl.addJob(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.job.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.job.add.failuer"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 2) {
			JobImpl jobImpl = new JobImpl();
			String jobName = request.getParameter("jobName");
			String orgId = userInfo.getEmployee().getOrgId();
			PrintWriter writer = response.getWriter();
			boolean flag;
			flag = jobImpl.checkJobName(jobName,orgId);
			if (flag == true) {
				writer.write("true");
			} else {
				writer.write("false");
			}
			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.job.add.noparam"));
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

	// 编辑功能
	public String Job_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "Job_edit";

		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJobById(bean.getJobId());
				map.put("msgInfo", getText("popedom.job.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.job.get.failure"));
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
				map = impl.editJob(bean);
				map.put("msgInfo", getText("popedom.job.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.job.edit.failure"));
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
				map.put("msgInfo", getText("popedom.job.edit.noparam"));
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

	// 删除功能
	public String Job_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "Job_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				System.out.println(userInfo.getEmployee());
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJob(bean);
				map.put("msgInfo", getText("popedom.job.delete.success"));
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
					map.put("msgInfo", getText("popedom.job.delete.fk"));
				} else {
					map.put("msgInfo", getText("popedom.job.delete.failure"));
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
				map.put("msgInfo", getText("popedom.job.delete.noparam"));
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

	public String Job_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "Job_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJobById(bean.getJobId());
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

	public Job getModel() {
		return bean;
	}

	public void setBean(Job bean) {
		this.bean = bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<Job> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<Job> dataRows) {
		this.dataRows = dataRows;
	}
}
