/**
 * 
 */
package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

/**
 * @author Administrator
 * 
 */
public class PdInitBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3469639157163452356L;
	private String menuId;
	private String menuName;
	private String menuPath;
	private String isInit;
	private String tableName, columnName, columnBean, columnField, aliasName, beanValue, listValue, insertSql, updateSql;

	private String schemaName, domainName, path, packagePath;
	
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public PdInitBean() {
	}

	public String getMenuId() {
		return menuId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnBean() {
		return columnBean;
	}

	public String getBeanValue() {
		return beanValue;
	}

	public void setBeanValue(String beanValue) {
		this.beanValue = beanValue;
	}

	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	public void setColumnBean(String columnBean) {
		this.columnBean = columnBean;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public String getColumnField() {
		return columnField;
	}

	public void setColumnField(String columnField) {
		this.columnField = columnField;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

	public String getIsInit() {
		return isInit;
	}

	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}

	public PdInitBean(String menuId, String menuName, String menuPath, String isInit) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuPath = menuPath;
		this.isInit = isInit;
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

}
