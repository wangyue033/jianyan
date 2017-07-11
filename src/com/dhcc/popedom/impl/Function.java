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
public class Function implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6568362903631175557L;
	private String funcId;
	private String funcName;
	private String funcUrl;
	private String resourceId;
	private String moduleId;
	private String pdSort;
	private String jspModel;
	private String isValid;

	private ArrayList<Popedom> popedomList = new ArrayList<Popedom>();

	public Function() {

	}

	public Function(String funcId, String funcName, String funcUrl,
			String resourceId, String moduleId) {
		this.funcId = funcId;
		this.funcName = funcName;
		this.funcUrl = funcUrl;
		this.resourceId = resourceId;
		this.moduleId = moduleId;
	}

	public Function(String funcId, String funcName, String funcUrl,
			String resourceId, String moduleId, String jspModel, String pdSort) {
		this.funcId = funcId;
		this.funcName = funcName;
		this.funcUrl = funcUrl;
		this.resourceId = resourceId;
		this.moduleId = moduleId;
		this.jspModel = jspModel;
		this.pdSort = pdSort;
	}

	public void setPopedomFromFuncId(String funcId) {
		String[] sParas = new String[] { funcId };
		String sql = SqlValues.getInstance().get("Role.Accredit",
				"get_popedomByFuncId");
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, sParas);
		try {
			while (crs.next()) {
				Popedom sort = new Popedom(crs.getString("popedom_id"), crs
						.getString("popedom_name"), funcId);
				popedomList.add(sort);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncUrl() {
		return funcUrl;
	}

	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getJspModel() {
		return jspModel;
	}

	public void setJspModel(String jspModel) {
		this.jspModel = jspModel;
	}

	public ArrayList<Popedom> getPopedomList() {
		return popedomList;
	}

	public void setPopedomList(ArrayList<Popedom> popedomList) {
		this.popedomList = popedomList;
	}

	public String getPdSort() {
		return pdSort;
	}

	public void setPdSort(String pdSort) {
		this.pdSort = pdSort;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}