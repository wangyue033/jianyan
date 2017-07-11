package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

public class Code extends BaseBean {
	private static final long serialVersionUID = -9064105934087931274L;
	private String schemaName, tableName;
	private String domainName, path, packagePath, moduleName, namespace,
			funcId;

	private String funcSearch, funcThisAdd, funcOpenAdd, funcThisEdit,
			funcOpenEdit, funcDel, funcDetail, funcProcDetail, funcSubmit,
			funcCheck;

	public void reset() {
		this.schemaName = "demo08";
		this.tableName = "";
		this.domainName = "";
		this.path = "D:\\11\\11";
		this.packagePath = "com.dhcc.";
		this.moduleName = "";
		this.namespace = "";
		
		this.funcCheck = "false";
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFuncSearch() {
		return funcSearch;
	}

	public void setFuncSearch(String funcSearch) {
		this.funcSearch = funcSearch;
	}

	public String getFuncThisAdd() {
		return funcThisAdd;
	}

	public void setFuncThisAdd(String funcThisAdd) {
		this.funcThisAdd = funcThisAdd;
	}

	public String getFuncOpenAdd() {
		return funcOpenAdd;
	}

	public void setFuncOpenAdd(String funcOpenAdd) {
		this.funcOpenAdd = funcOpenAdd;
	}

	public String getFuncThisEdit() {
		return funcThisEdit;
	}

	public void setFuncThisEdit(String funcThisEdit) {
		this.funcThisEdit = funcThisEdit;
	}

	public String getFuncOpenEdit() {
		return funcOpenEdit;
	}

	public void setFuncOpenEdit(String funcOpenEdit) {
		this.funcOpenEdit = funcOpenEdit;
	}

	public String getFuncDel() {
		return funcDel;
	}

	public void setFuncDel(String funcDel) {
		this.funcDel = funcDel;
	}

	public String getFuncDetail() {
		return funcDetail;
	}

	public void setFuncDetail(String funcDetail) {
		this.funcDetail = funcDetail;
	}

	public String getFuncProcDetail() {
		return funcProcDetail;
	}

	public void setFuncProcDetail(String funcProcDetail) {
		this.funcProcDetail = funcProcDetail;
	}

	public String getFuncSubmit() {
		return funcSubmit;
	}

	public void setFuncSubmit(String funcSubmit) {
		this.funcSubmit = funcSubmit;
	}

	public String getFuncCheck() {
		return funcCheck;
	}

	public void setFuncCheck(String funcCheck) {
		this.funcCheck = funcCheck;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

}
