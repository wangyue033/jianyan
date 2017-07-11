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
public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7328976841639117750L;

	private String menuId;

	private String menuName;

	private ArrayList<Module> moduleList = new ArrayList<Module>();

	public Menu() {

	}

	public Menu(String menuId, String menuName) {
		this.menuId = menuId;
		this.menuName = menuName;
	}

	public void setModuleFromMenu(String menuId) {
		String[] sParas = new String[] { menuId };
		String sql = SqlValues.getInstance().get("Role.Accredit",
				"get_moduleByMenuId");
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, sParas);
		try {
			while (crs.next()) {
				Module module = new Module(crs.getString("module_id"), crs
						.getString("module_name"), crs.getString("menu_id"));
				module.setFuncFromModuleId(crs.getString("module_id"));
				moduleList.add(module);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public ArrayList<Module> getModuleList() {
		return moduleList;
	}

	public void setModuleList(ArrayList<Module> moduleList) {
		this.moduleList = moduleList;
	}
}
