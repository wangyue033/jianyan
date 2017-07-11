package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-23    
 * 区域信息domain
 */
public class JdAreaInfo extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	private String areaId;
	private String areaName;
	private String parentId;
	private String parentName;
	private String counyts;
	private String isValid;
	private String optId;
	private String optName;
	private String optTime;
	private String strId;
	private String index;
	
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getCounyts() {
		return counyts;
	}
	public void setCounyts(String counyts) {
		this.counyts = counyts;
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
	public String getStrId() {
		return strId;
	}
	public void setStrId(String strId) {
		this.strId = strId;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
}
