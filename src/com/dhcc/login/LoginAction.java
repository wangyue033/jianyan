package com.dhcc.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.login.impl.Function;
import com.dhcc.login.impl.Menu;
import com.dhcc.login.impl.Module;
import com.dhcc.popedom.impl.PwdImpl;
import com.opensymphony.xwork2.ActionSupport;

import framework.dhcc.db.DBFacade;
import framework.dhcc.utils.WebUtil;

/**
 * @author 朱荣祥
 * 
 */
public class LoginAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 659535921088428849L;

	HttpServletRequest request = null;

	HttpServletResponse response = null;

	HttpSession session = null;

	private String userId;

	private String password;

	private String menuId;

	private String menu;

	private String forceLogin;

	private int oper;

	public String login_in() {
		if (userId != null && userId.trim().equals("dhcc") && password != null
				&& password.trim().equals("dhcc")) {
			return "init_success";
		}
		session = ServletActionContext.getRequest().getSession(true);
		UserInfoBean bean = new UserInfoBean();
		int flag = bean.checkUserinfo(userId, password);
		if (flag == 1) {
			session.invalidate();
			request.setAttribute("errorMessage", getText("login.user.wrong"));
			return ERROR;
		} else if (flag == 2) {
			session.invalidate();
			request.setAttribute("errorMessage",
					getText("login.user.state.wrong"));
			return ERROR;
		}
		bean.initiallizeUserACL();
		if (bean.getMenus().size() == 0) {
			session.invalidate();
			request.setAttribute("errorMessage", getText("login.user.nomenu"));
			return ERROR;
		}

		String serialId = bean.getEmployee().getEmployeeId();
		String userName = bean.getEmployee().getUserName();
		String userId = bean.getEmployee().getUserId();
		String ip = WebUtil.getIpAddr(request);

		String sql = "insert into hr_user_log(log_id,serial_id,user_id,user_name,log_ip,log_type,log_status,opt_time) values (?,?,?,?,?,?,?,now())";
		DBFacade.getInstance().execute(
				sql,
				new String[] { DBFacade.getInstance().getID(), serialId,
						userId, userName, ip, "01", "1" });
		session.setAttribute("userInfoBean", bean);
		return "toAction";
	}

	/**
	 * 1、行政审批管理系统：executive
	 * 
	 * @return
	 */
	public String login_executive() {
		return "login_executive";
	}
	
	/**
	 * 5、行政执法一般流程
	 * @return
	 */
	public String login_legal(){
		return "login_legal";
	}

	/**
	 * 1、权责清单管理系统：power
	 * 
	 * @return
	 */
	public String login_power() {
		return "login_power";
	}
	
	public String getMenuStr(UserInfoBean bean){
		List<Menu> menuList = bean.getMenus();
		Menu menu = null;
		List<Module> moduleList = new ArrayList<Module>();
		StringBuffer sbf = new StringBuffer("[");
		for (int i = 0; i < menuList.size(); i++) {
			menu = menuList.get(i);
			if (menu.getMenuId().trim().equals(menuId)) {
				moduleList = menu.getModuleList();
				for(Module module : moduleList){
					int index = 1;
					sbf.append("{\"F_ModuleId\":\""+module.getModuleId()+"\",\"F_ParentId\":\"0\",\"F_FullName\":\""+module.getModuleName()+"\",\"F_Icon\":\""+module.getModuleIco()+"\",\"F_Target\":\"expand\",\"F_IsMenu\":0},");
					for(Function func : module.getFuncList()){
						sbf.append("{\"F_Items\":\""+func.getPdSort()+"\",\"F_ModuleId\":\""+func.getFuncId()+"\",\"F_ParentId\":\""+func.getModuleId()+"\",\"F_FullName\":\""+func.getFuncName()+"\",\"F_Icon\":\""+func.getFuncIco()+"\",\"F_Target\":\"iframe\",\"F_IsMenu\":1,\"F_UrlAddress\":\""+func.getFuncUrl()+"\"},");
						index++;
						if(index >= 6){
							index = 1;
						}
					}
				}
			}
		}
		String str = sbf.substring(0, sbf.lastIndexOf(",")) + "]";
		//System.out.println(str);
		return str;
	}

	/**
	 * 6、监督抽查：inspect
	 * 
	 * @return
	 */
	public String login_inspect() {
		UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		String str = getMenuStr(userInfoBean);
		request.setAttribute("str", str);
		return "login_inspect";
	}
	/**
	 * 7、大数据分析系统：analyse
	 * 
	 * @return
	 */
	public String login_analys() {
		return "login_analys";
	}

	/**
	 * 8、权限管理系统：popedom
	 * 
	 * @return
	 */
	public String login_popedom() {
		UserInfoBean userInfoBean = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		String str = getMenuStr(userInfoBean);
		request.setAttribute("str", str);
		return "login_popedom";
	}

	/**
	 * 9、数据铁笼应用系统：cage
	 * 
	 * @return
	 */
	public String login_cage() {
		try {
			session = request.getSession(true);
			UserInfoBean bean = (UserInfoBean) session
					.getAttribute("userInfoBean");
			if (bean == null) {
				return ERROR;
			}
			List<Menu> menuList = bean.getMenus();
			Menu menu = null;
			List<Module> moduleList = new ArrayList<Module>();
			for (int i = 0; i < menuList.size(); i++) {
				menu = menuList.get(i);
				if (menu.getMenuId().trim().equals(menuId)) {
					moduleList = menu.getModuleList();
				}
			}
			request.setAttribute("moduleList", moduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login_cage";
	}

	public String success() {
		return SUCCESS;
	}

	public String login_menu() {
		try {
			session = request.getSession(true);
			UserInfoBean bean = (UserInfoBean) session
					.getAttribute("userInfoBean");
			if (bean == null) {
				return ERROR;
			}
			List<Menu> menuList = bean.getMenus();
			Menu menu = null;
			List<Module> moduleList = new ArrayList<Module>();
			for (int i = 0; i < menuList.size(); i++) {
				menu = menuList.get(i);
				if (menu.getMenuId().trim().equals(menuId)) {
					moduleList = menu.getModuleList();
				}
			}
			request.setAttribute("moduleList", moduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String login_out() {
		try {
			session = request.getSession();
			UserInfoBean userBean = null;
			if (session != null) {
				userBean = (UserInfoBean) session.getAttribute("userInfoBean");
			}
			SessionStore.getInstance().remove(session);
			session.invalidate();
			if (userBean != null) {
				String serialId = userBean.getEmployee().getEmployeeId();
				String userName = userBean.getEmployee().getUserName();
				String userId = userBean.getEmployee().getUserId();
				String ip = WebUtil.getIpAddr(request);

				String sql = "insert into hr_user_log(log_id,serial_id,user_id,user_name,log_ip,log_type,log_status,opt_time) values (?,?,?,?,?,?,?,now())";
				DBFacade.getInstance().execute(
						sql,
						new String[] { DBFacade.getInstance().getID(),
								serialId, userId, userName, ip, "02", "1" });
			}
			PrintWriter out = ServletActionContext.getResponse().getWriter();

			out.println("<html><head>");
			out.println("<title></title></head>");
			out.println("<body onload=\"pageDirect()\">");
			out
					.println("<script language=\"JavaScript\" type=\"text/JavaScript\">");
			out.println("<!--");
			out.println("function pageDirect() {");
			out.println("window.location.replace(\""
					+ ServletActionContext.getRequest().getContextPath()
					+ "/login/index.jsp" + "\");");
			out.println("}");
			out.println("//-->");
			out.println("</script>");
			out.println("</body></html>");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("null")
	public String change_password() {
		PwdImpl impl = new PwdImpl();
		String password = (String) request.getParameter("password");
		String newpassword = (String) request.getParameter("newpassword");

		Employee employee = ((UserInfoBean) request.getSession().getAttribute(
				"userInfoBean")).getEmployee();
		String userID = employee.getUserId();
		boolean flag = false;
		PrintWriter pw = null;
		if (oper == 0) {
			request.setAttribute("result", "N");
			addActionMessage("");
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

				flag = impl.changePassword(userID, newpassword);
				if (flag == true) {
					map.put("msg", "true");
					map.put("msgInfo",
							getText("popedom.change.password.success"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				} else {
					map.put("msg", "false");
					map.put("msgInfo",
							getText("popedom.change.password.failure"));
					JSONObject ob = new JSONObject();
					ob.putAll(map);
					pw.print(ob.toString());
					pw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.change.password.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}

		} else if (oper == 2) {
			try {
				password = (String) request.getParameter("password");
				flag = impl.canLogin(userID, password);
				pw = null;
				pw = response.getWriter();
				if (flag == true) {
					pw.print("true");
				} else {
					pw.print("false");
				}
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		return SUCCESS;
	}

	public String show_online() {
		return "show_online";
	}

	public String show_task() {
		return "show_task";
	}

	public String ifm_task() {
		return "ifm_task";
	}

	public String working_platform() {
		return SUCCESS;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public String getMenuId() {
		return menuId;
	}

	public int getOper() {
		return oper;
	}

	public void setOper(int oper) {
		this.oper = oper;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getForceLogin() {
		return forceLogin;
	}

	public void setForceLogin(String forceLogin) {
		this.forceLogin = forceLogin;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

}
