package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class ProductType extends BaseBean {

	/**
	 * @author longxl
	 * 产品分类
	 */
	private static final long serialVersionUID = 3482371137152260013L;

	private String typeId, typeName, shortName, parentId, typeDesc, isValid, optId, optName, optTime;
	
	private String parentName;
	
	public void reset () {
		this.typeId = "";
		this.typeName = "";
		this.shortName = "";
		this.parentId = "";
		this.typeDesc = "";
		this.isValid = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getOptTime() {
		return optTime;
	}

	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	
}
