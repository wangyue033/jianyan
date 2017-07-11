package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.dhcc.login.impl.Function;
import com.dhcc.login.impl.Menu;
import com.dhcc.login.impl.Module;
import com.dhcc.popedom.domain.DeptRole;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.conf.SqlValues;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;

public class DeptRoleImpl {

	Page page = new Page();

	public String getDeptRoleList(String curPage,
			String perNumber, String orderByField,
			String orderBySort, String searchField,
			String searchValue,DeptRole bean1) throws SQLException,
			ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql ="select a.role_id,a.role_name,a.company_id,c.company_name,a.parent_id, a.can_modify,a.creator,a.create_time,a.is_valid,a.remark "
				+ "from p_role a,hr_company c " +
				"where a.company_id = c.company_id and a.company_id like '"+bean1.getCompanyId()+"%'";
		if (searchField != null
				&& !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField
					+ " like '%" + searchValue + "%' ";
		}
		if (orderByField != null
				&& !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("role_id");
			page.setSord("asc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<DeptRole> list = new ArrayList<DeptRole>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			DeptRole bean=new DeptRole();
			bean.setDeptRoleId(crs.getString("role_id"));
			bean.setDeptRoleName(crs.getString("role_name"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setCanModify(crs.getString("can_modify"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setIsValid(crs.getString("is_valid"));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		map.put("totalPage",
				"" + pageData.getPageAmount());
		map.put("curPage", "" + pageData.getPageNo());
		map.put("totalRecord",
				"" + pageData.getItemAmount());
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	public void addDeptRole(DeptRole bean) {
		String roleId = DBFacade.getInstance().getID();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into p_role (role_id,role_name,can_modify,creator,create_time,remark,is_valid,is_charge,company_id,is_admin) values (?,?,?,?,now(),?,?,?,?,?)";
		params[0] = new String[] { roleId,bean.getDeptRoleName(),"1",bean.getOptName(),bean.getRemark(),"1","1",bean.getCompanyId(),"0"};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "角色添加",
				"角色编号：" + roleId + ";员工名称：" + bean.getDeptRoleName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		}
	
	public String getDeptRoleId(){
		String sql = "select max(role_id) from p_role";
		String roleId = (String) DBFacade.getInstance().getValueBySql(sql, null);
	
		return roleId;
	}

	public boolean addCheck(String roleName) throws Exception{
		try {
			String sql = "select role_name from p_role where role_name = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { roleName });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public Map<String, Object> editUser(DeptRole bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update p_role set role_id=?, role_name=?, company_id=?, creator=? ,remark=?, is_valid=?, is_charge=? where role_id = ?";
		params[0] = new String[] { bean.getDeptRoleId(), bean.getDeptRoleName(), bean.getCompanyId(),  bean.getOptName(), bean.getRemark(), "1", "1", bean.getDeptRoleId()};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "角色信息修改",
				"角色编号：" + bean.getDeptRoleId() + ";角色名称：" + bean.getDeptRoleName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getDeptRoleById(String roleId) throws SQLException {
		String sql = "select a.role_id,a.role_name,c.company_id,c.company_name,a.can_modify,a.creator,a.create_time,a.is_valid,a.remark "
				+ "from p_role a,hr_company c where 1 = 1 and a.role_id = ? and a.company_id = c.company_id";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { roleId });
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			if (crs.next()) {
				map.put("msg", "true");
				DeptRole bean = new DeptRole();
				bean.setDeptRoleId(crs.getString("role_id"));
				bean.setDeptRoleName(crs.getString("role_name"));
				bean.setCompanyId(crs.getString("company_id"));
				bean.setCompanyName(crs.getString("company_name"));
				bean.setCanModify(crs.getString("can_modify"));
				bean.setIsValid(crs.getString("is_valid"));
				bean.setRemark(crs.getString("remark"));
				map.put("record", bean);
			} else {
				map.put("msg", "noresult");
			}
			return map;
	}

	public List<Menu> getMenuAllList() {
		List<Menu> menuList = new ArrayList<Menu>();
		String sql = SqlValues.getInstance().get("Role.Accredit", "get_menu");
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				Menu menu = new Menu(crs.getString("menu_id"), crs
						.getString("menu_name"));
				menu.setModuleFromMenu(crs.getString("menu_id"));
				menuList.add(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}
	public List<Menu> getMenuAllList(String roleId) {
		List<Menu> menuList = new ArrayList<Menu>();
		ArrayList<Module> moduleList;
		ArrayList<Function> funcList;
		String sqlmenu = " select distinct me.menu_id , me.menu_name from p_menu me,p_module mo,p_func f,p_popedom p,p_rs_map rs,p_role r "+
					 " where me.is_valid = '1' and r.role_id=? and rs.role_id=r.role_id and p.popedom_id=rs.popedom_id "+
					 " and p.func_id=f.func_id and f.module_id=mo.module_id and mo.menu_id=me.menu_id "+
					 " order by me.menu_id";
		String sqlmodule = " select distinct mo.module_id , mo.module_name from p_module mo,p_func f,p_popedom p,p_rs_map rs,p_role r "+
					 " where mo.is_valid = '1' and r.role_id=? and mo.menu_id=? and rs.role_id=r.role_id and p.popedom_id=rs.popedom_id "+
					 " and p.func_id=f.func_id and f.module_id=mo.module_id  "+
					 " order by mo.module_id";
		String sqlfunc = " select distinct f.func_id , f.func_name from p_func f,p_popedom p,p_rs_map rs,p_role r "+
						 " where f.is_valid = '1' and r.role_id=? and f.module_id=? and rs.role_id=r.role_id and p.popedom_id=rs.popedom_id "+
						 " and p.func_id=f.func_id "+
						 " order by f.func_id";
		CachedRowSetImpl crsMenu = DBFacade.getInstance().getRowSet(sqlmenu, new String[]{roleId});
		try {
			while (crsMenu.next()) {
				moduleList = new ArrayList<Module>();
				String menuId = crsMenu.getString("menu_id");
				Menu menu = new Menu(menuId, crsMenu
						.getString("menu_name"));
				CachedRowSetImpl crsModule=DBFacade.getInstance().getRowSet(sqlmodule, new String[]{roleId,menuId});
				while(crsModule.next()){
					funcList= new ArrayList<Function>();
					String moduleId = crsModule.getString("module_id");
					Module module = new Module();
					module.setModuleId(moduleId);
					module.setModuleName(crsModule.getString("module_name"));
					CachedRowSetImpl crsFunc=DBFacade.getInstance().getRowSet(sqlfunc, new String[]{roleId,moduleId});
					while(crsFunc.next()){
						Function func = new Function();
						String funcId = crsFunc.getString("func_id");
						func.setFuncId(funcId);
						func.setFuncName(crsFunc.getString("func_name"));
						func.setPopedomFromFuncId(funcId);
						funcList.add(func);
					}
					module.setFuncList(funcList);
					moduleList.add(module);
				}
				menu.setModuleList(moduleList);
				menuList.add(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}
	
	public String[] getPsByDeptRoleId(String roleId) {
		String[] selectedId = null;
		String sql = "select distinct  popedom_id from p_rs_map where role_id =? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { roleId });
		selectedId = new String[crs.size()];
		try {
			int i = 0;
			while (crs.next()) {
				selectedId[i] = crs.getString("popedom_id");
				i++;
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return selectedId;
	}
	
	public void accreditDeptRole(DeptRole bean) {
		String roleId = bean.getDeptRoleId();
		String[] psId = bean.getSelectedPsId();
		if (psId != null && psId.length > 0) {
			int len = psId.length;
			String[] sqls = new String[len + 2];
			String[][] sParas = new String[len + 2][];
			sqls[0] = SqlValues.getInstance().get(
					"com.dhcc.popedom.action.RoleAction",
					"delete_rsMapByRoleId");
			sParas[0] = new String[] { roleId };
			for (int i = 1; i < len + 1; i++) {
				sqls[i] = SqlValues.getInstance().get(
						"com.dhcc.popedom.action.RoleAction", "insert_rsMap");
				sParas[i] = new String[] { DBFacade.getInstance().getID(),
						roleId, psId[i - 1], "" };
			}
			sqls[sqls.length-1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
			sParas[sqls.length-1] = new String[]{DBFacade.getInstance().getID(), bean.getOptId(),
					bean.getOptName(), "角色授权", "角色编号："+roleId, "1"};
			DBFacade.getInstance().execute(sqls, sParas);
		} else {
			String[] sqls = new String[2];
			String[][] params = new String[2][];
			sqls[0] = SqlValues.getInstance().get(
					"com.dhcc.popedom.action.RoleAction",
					"delete_rsMapByRoleId");
			params[0] = new String[] { roleId };
			sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
			params[1] = new String[]{DBFacade.getInstance().getID(), bean.getOptId(),
					bean.getOptName(), "角色授权", "角色编号："+roleId, "1"};
			DBFacade.getInstance().execute(sqls, params);
		}
	}
	
	
	public Map<String, Object> delDeptRole(DeptRole bean) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];

		sqls[0] = "delete from p_re_map where role_id = ?";
		params[0] = new String[]{bean.getDeptRoleId()};
		System.out.println("roleId="+bean.getDeptRoleId());
		
		sqls[1] = "delete from p_role where role_id = ? or parent_id = ?";
		params[1] = new String[] { bean.getDeptRoleId(),bean.getDeptRoleId()};

		sqls[2] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[2] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "组织机构删除",
				"组织机构编号：" + bean.getDeptRoleId(), "1" };
	  System.out.println(sqls[1]);
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
}

