/**
 * 
 */
package com.dhcc.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dhcc.login.impl.Function;
import com.dhcc.login.impl.Menu;
import com.dhcc.login.impl.Module;
import com.dhcc.login.impl.Popedom;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.time.TimeUtil;

/**
 * @author 朱荣祥
 * 
 */
public class UserInfoBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2373809130518634124L;

	private ArrayList<Menu> menus = new ArrayList<Menu>();

	private ArrayList<Module> modules = new ArrayList<Module>();

	private ArrayList<Function> functions = new ArrayList<Function>();

	private ArrayList<Popedom> popedom = new ArrayList<Popedom>();

	private ArrayList<String> roles = new ArrayList<String>();

	private ArrayList<NoticeType> myNoticeTypeList = new ArrayList<NoticeType>();

	private Set<String> pSet = new HashSet<String>();

	static final String RoleModel = "T";

	private Employee employee;

	public UserInfoBean() {
	}

	public int checkUserinfo(String userId, String password) {
		try {
			this.employee = Employee.getInstance().checkUser(userId, password);
			if (employee == null) {
				return 1; // 用户名密码错误
			} else if (!"1".equals(employee.getState())
					&& !"2".equals(employee.getState())) {
				return 2; // 未报到或已离职
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 3; // 连接失败
		}
		return 0;
	}

	public boolean initiallizeUserACL() {
		if (this.employee == null) {
			return false;
		}
		menus.clear();
		String employeeId = this.employee.getEmployeeId();
		try {
			String roleSQL = "select role_id from p_re_map where serial_id = ?";
			CachedRowSetImpl crs_r = DBFacade.getInstance().getRowSet(roleSQL,
					new String[] { employeeId });
			while (crs_r.next()) {
				this.getRoles().add(crs_r.getString("role_id"));
			}

			CachedRowSetImpl crs_p = DBFacade.getInstance().getRowSet(
					getRoleList(employeeId), null);
			while (crs_p.next()) {
				pSet.add(crs_p.getString("popedom_id"));
				popedom.add(new Popedom(crs_p.getString("popedom_id"), crs_p
						.getString("popedom_name"), crs_p.getString("func_id"),
						"",""));
			}
			String sql_1 = "select menu_id,menu_name from p_menu where is_valid = '1' order by menu_id asc ";
			String sql_2 = "select module_id,module_name,module_ico from p_module where is_valid = '1' and menu_id = ? order by module_id asc ";
			String sql_3 = "select func_id,module_id,func_name,resource_id,func_url,func_ico from p_func where is_valid = '1' and module_id=? order by func_id asc";
			CachedRowSetImpl crsMenus = DBFacade.getInstance().getRowSet(sql_1,
					null);
			while (crsMenus.next()) {
				String menuId = crsMenus.getString("menu_id");
				String menuName = crsMenus.getString("menu_name");
				Menu menu = new Menu(menuId, menuName);
				menu.setModuleList(new ArrayList<Module>());
				CachedRowSetImpl crsModules = DBFacade.getInstance().getRowSet(
						sql_2, new String[] { menuId });
				while (crsModules.next()) {
					String moduleId = crsModules.getString("module_id");
					String moduleName = crsModules.getString("module_name");
					String moduleIco = crsModules.getString("module_ico");
					Module module = new Module(moduleId, moduleName, menuId, moduleIco);
					module.setFuncList(new ArrayList<Function>());
					CachedRowSetImpl crsFuncs = DBFacade.getInstance()
							.getRowSet(sql_3, new String[] { moduleId });
					while (crsFuncs.next()) {
						String functionCode = crsFuncs.getString("resource_id");
						Function function = null;
						if (pSet != null && pSet.contains(functionCode)) {
							function = new Function(
									crsFuncs.getString("func_id"),
									crsFuncs.getString("func_name"),
									crsFuncs.getString("func_url"),
									crsFuncs.getString("resource_id"),
									crsFuncs.getString("module_id"),
									"", "", crsFuncs.getString("func_ico"));
							setPopedom(function);
							module.getFuncList().add(function);
							functions.add(function);
						}
					}
					if (!module.getFuncList().isEmpty()) {
						menu.getModuleList().add(module);
						modules.add(module);
					}
				}
				if (!menu.getModuleList().isEmpty()) {
					menus.add(menu);
				}
			}
			resetNoticeTypes(employeeId);
		} catch (Exception se) {
			se.printStackTrace();
		}
		return false;
	}

	private void resetNoticeTypes(String employeeId) throws Exception {
		ArrayList<NoticeType> deleteNoticeTypeList = new ArrayList<NoticeType>();
		ArrayList<NoticeType> insertNoticeTypeList = new ArrayList<NoticeType>();
		String sql_notice = "select * from (select a.notice_type,a.action_name,a.type_name,a.notice_attr,a.show_content,a.is_from_sys,"
				+ "b.notice_type staff_notice_type,b.notice_attr staff_notice_attr,b.is_assign staff_is_assign,a.is_assign "
				+ "from sys_notice_type a left outer join sys_notice_staff b on a.notice_type = b.notice_type and b.serial_id=? "
				+ "where a.is_valid='1' order by a.notice_type) x";
		CachedRowSetImpl crsNoice = DBFacade.getInstance().getRowSet(
				sql_notice, new String[] { employeeId });
		boolean haveNotice = false;
		boolean haveNoLoginNotice = false;
		boolean haveFunc = false;
		while (crsNoice.next()) {
			haveFunc = false;
			NoticeType typeBean = new NoticeType();
			typeBean.setNoticeType(crsNoice.getString("notice_type"));
			typeBean.setActionName(crsNoice.getString("action_name"));
			typeBean.setTypeName(crsNoice.getString("type_name"));
			typeBean.setNoticeAttr(crsNoice.getString("notice_attr"));
			typeBean.setShowContent(crsNoice.getString("show_content"));
			typeBean.setIsFromSys(crsNoice.getString("is_from_sys"));
			typeBean.setStaffIsAssign(crsNoice.getString("staff_is_assign"));
			String typeAction = crsNoice.getString("action_name");
			String isAssign = crsNoice.getString("is_assign");
			for (Function function : functions) {
				if (typeAction.equals(function.getResourceId())) {
					haveNotice = true;
					haveFunc = true;
					if (crsNoice.getString("staff_notice_type") != null) {
						typeBean.setNoticeAttr(crsNoice
								.getString("staff_notice_attr"));
					}
					if (typeBean.getNoticeAttr().equals("1")) {
						haveNoLoginNotice = true;
					}
					myNoticeTypeList.add(typeBean);
					break;
				}
			}
			if (!haveFunc && crsNoice.getString("staff_notice_type") != null
					&& "0".equals(crsNoice.getString("staff_is_assign"))) {
				deleteNoticeTypeList.add(typeBean);
			}
			if (haveFunc && crsNoice.getString("staff_notice_type") == null
					&& isAssign.equals("0")) {
				typeBean.setIsAssign("0");
				insertNoticeTypeList.add(typeBean);
			}
		}
		int sqlcount = (deleteNoticeTypeList.size() == 0 ? 0 : 1)
				+ (insertNoticeTypeList.size() == 0 ? 0 : 1);
		String[] sqls = new String[sqlcount];
		String[][][] paras = new String[sqlcount][][];
		if (deleteNoticeTypeList.size() > 0) {
			sqls[0] = "delete from sys_notice_staff where notice_type=? and serial_id=? ";
			paras[0] = new String[deleteNoticeTypeList.size()][2];
			for (int i = 0; i < deleteNoticeTypeList.size(); i++) {
				NoticeType typeBean = deleteNoticeTypeList.get(i);
				paras[0][i][0] = typeBean.getNoticeType();
				paras[0][i][1] = employeeId;
			}
		}
		if (insertNoticeTypeList.size() > 0) {
			sqls[sqlcount - 1] = "insert into sys_notice_staff(serial_id,notice_type,notice_attr,is_assign,is_from_sys) "
					+ "values(?,?,?,?,?) ";
			paras[sqlcount - 1] = new String[insertNoticeTypeList.size()][5];
			for (int i = 0; i < insertNoticeTypeList.size(); i++) {
				NoticeType typeBean = insertNoticeTypeList.get(i);
				paras[sqlcount - 1][i][0] = employeeId;
				paras[sqlcount - 1][i][1] = typeBean.getNoticeType();
				paras[sqlcount - 1][i][2] = typeBean.getNoticeAttr();
				paras[sqlcount - 1][i][3] = typeBean.getIsAssign();
				paras[sqlcount - 1][i][4] = typeBean.getIsFromSys();
			}
		}
		if (sqlcount > 0) {
			DBFacade.getInstance().execute(sqls, paras, "");
		}
		if (haveNotice) {
			employee.setIsNotice("2");
			if (haveNoLoginNotice) {
				employee.setIsNotice("1");
			}
		} else {
			employee.setIsNotice("0");
		}
	}

	public boolean onduty(String employeeId, String roleId) {
		boolean flag = true;
		String thisTime = TimeUtil.getDBTime();
		String sql = "select duty_id,a.onduty_time "
				+ "from charger_duty where is_onduty = '1' and opt_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { employeeId.trim() });
		if (crs == null || crs.size() == 0) {
			String sql_insert = "insert into charger_duty (duty_id,opt_id,onduty_time,is_onduty,role_id)"
					+ " values('"
					+ DBFacade.getInstance().getID()
					+ "','"
					+ employeeId
					+ "',to_date('"
					+ thisTime
					+ "','yyyy-MM-dd hh24:mi:ss'),'1','" + roleId + "')";
			try {
				DBFacade.getInstance().execute(sql_insert);
			} catch (Exception se) {
				se.printStackTrace();
				flag = false;
			}
			this.employee.setDutyTime(thisTime);
		} else {
			try {
				while (crs.next()) {
					this.employee.setDutyTime(crs.getString("onduty_time"));
				}
			} catch (Exception se) {

			}
		}
		return flag;
	}

	public String getRoleList(String userId) throws Exception {
		String sql = "";
		if (RoleModel.trim().equals("T")) {
			List<String> roleList = new ArrayList<String>();
			String sql_ = "select role_id from p_re_map where serial_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql_,
					new String[] { userId });
			String sql_head = "select a.popedom_id,func_id,popedom_name,resource_id "
					+ "from p_popedom a,p_rs_map b "
					+ "where a.popedom_id = b.popedom_id "
					+ "and a.is_valid = '1' ";
			while (crs.next()) {
				String roleId = crs.getString("role_id");
				if (roleId.trim().equals("00")) {
					sql = sql_head + "and b.ROLE_ID = '" + roleId
							+ "' order by a.func_id asc ";
					return sql;
				}
				roleList.add(roleId);
			}
			if (roleList != null && roleList.size() == 1) {
				sql = sql_head + "and b.ROLE_ID like '" + roleList.get(0)
						+ "%'  ";
			} else {
				for (int i = 0; i < roleList.size(); i++) {
					if (i == roleList.size() - 1) {
						sql += sql_head + " and b.ROLE_ID like '"
								+ roleList.get(i) + "%' ";
					} else {
						sql += sql_head + " and b.ROLE_ID like '"
								+ roleList.get(i) + "%' UNION ";
					}
				}
			}
			sql = "select distinct * from (" + sql
					+ ")aa order by aa.func_id asc ";
		} else {
			sql = "select distinct a.* from p_popedom a,p_rs_map b,p_re_map c where a.popedom_id = b.popedom_id and b.role_id = c.role_id "
					+ "and a.is_valid = '1' and c.serial_id = '"
					+ userId
					+ "' order by a.func_id asc";
		}
		return sql;
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}

	public ArrayList<Module> getModules() {
		return modules;
	}

	public void setModules(ArrayList<Module> modules) {
		this.modules = modules;
	}

	public ArrayList<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(ArrayList<Function> functions) {
		this.functions = functions;
	}

	public boolean contains(String resourceId) {
		if (pSet != null && resourceId != null) {
			return pSet.contains(resourceId);
		}
		return false;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Set<String> getpSet() {
		return pSet;
	}

	public void setpSet(Set<String> pSet) {
		this.pSet = pSet;
	}

	public ArrayList<Popedom> getPopedom() {
		return popedom;
	}

	public void setPopedom(ArrayList<Popedom> popedom) {
		this.popedom = popedom;
	}

	/**
	 * 给指定的功能按照权限显示按钮以及相关方法
	 * 
	 * @param f
	 */
	public void setPopedom(Function f) {
		if (f.getPopedomList().isEmpty()) {
			f.setPdSort("");
		}
		String pd = "";
		for (int i = 0; i < popedom.size(); i++) {
			Popedom pdm = popedom.get(i);
			if (pdm.getFuncId().equals(f.getFuncId())) {
				pd += pdm.getPopedomId() + "@" + pdm.getPopedomName() + "!";
			}
		}
		if (pd.length() > 0)
			pd = pd.substring(0, pd.length() - 1);
		f.setPdSort(pd);
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public boolean equals(Object obj) {
		if (obj instanceof UserInfoBean) {
			UserInfoBean uib = (UserInfoBean) obj;
			return uib.getEmployee().getUserId()
					.equals(this.getEmployee().getUserId());
		} else {
			return false;
		}

	}

	public int hashCode() {
		return this.getEmployee().getUserId().hashCode();
	}

	public ArrayList<NoticeType> getMyNoticeTypeList() {
		return myNoticeTypeList;
	}

	public void setMyNoticeTypeList(ArrayList<NoticeType> noticeTypes) {
		this.myNoticeTypeList = noticeTypes;
	}

	public void loginLog(String ip, boolean isForce) throws Exception {
		String sql = "insert into hr_user_log(log_id,serial_id,user_id,user_name,log_ip,log_type,log_status,opt_time) values (?,?,?,?,?,?,?,now())";
		DBFacade.getInstance().execute(
				sql,
				new String[] { DBFacade.getInstance().getID(),
						this.employee.getEmployeeId(),
						this.employee.getUserId(), this.employee.getUserName(),
						ip, "01", isForce ? "2" : "1" });
	}
}
