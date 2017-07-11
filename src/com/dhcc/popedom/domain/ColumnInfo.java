package com.dhcc.popedom.domain;

public class ColumnInfo {

	private String columnName, columnZh, columnType, isPri, isSelect, isInsert,
			isUpdate;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnZh() {
		return columnZh;
	}

	public void setColumnZh(String columnZh) {
		this.columnZh = columnZh;
	}

	public String getIsInsert() {
		return isInsert;
	}

	public void setIsInsert(String isInsert) {
		this.isInsert = isInsert;
	}

	public String getIsPri() {
		return isPri;
	}

	public void setIsPri(String isPri) {
		this.isPri = isPri;
	}

	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String toString() {
		return "字段：" + this.columnName + ";中文：" + this.columnZh + ";类型："
				+ this.columnType + ";键：" + this.isPri + ";选择：" + this.isSelect
				+ ";插入：" + this.isInsert + ";更新：" + this.isUpdate;
	}

}
