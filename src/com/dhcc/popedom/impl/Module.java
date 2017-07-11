/**
 * 
 */
package com.dhcc.popedom.impl;

import java.io.Serializable;
import java.util.ArrayList;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.conf.SqlValues;
import framework.dhcc.db.DBFacade;

/**
 * @author Administrator
 * 
 */
public class Module implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2385185775731983349L;
	private String moduleId;
	private String moduleName;
	private String menuId;
	private String isValid;
	private ArrayList<Function> funcList = new ArrayList<Function>();

	public Module() {

	}

	public void setFuncFromModuleId(String moduleId) {
		String[] sParas = new String[] { moduleId };
		String sql = SqlValues.getInstance().get("Role.Accredit",
				"get_funcByModuleId");
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, sParas);
		try {
			while (crs.next()) {
				Function func = new Function(crs.getString("func_id"), crs
						.getString("func_name"), crs.getString("func_url"), crs
						.getString("resource_id"), crs.getString("module_id"));
				func.setPopedomFromFuncId(crs.getString("func_id"));
				funcList.add(func);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Module(String moduleId, String moduleName, String menuId) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.menuId = menuId;
	}

	public Module(String moduleId, String moduleName, String menuId,
			ArrayList<Function> funcList) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.menuId = menuId;
		this.funcList = funcList;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public ArrayList<Function> getFuncList() {
		return funcList;
	}

	public void setFuncList(ArrayList<Function> funcList) {
		this.funcList = funcList;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
}
