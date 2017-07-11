/**
 * 
 */
package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

/**
 * @author zhangzhongfeng
 * 
 */
public class Comp extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1909246284029681758L;
	private String companyId;
	private String companyName;
	private String charge;
	private String workingRange;
	private String buildDate;
	private String compAddress;
	private String mailCode;
	private String phone;
	private String fax;
	private String description;
	private String state;
	private String parentId, parentName;
	private String isValid;
	private String compType;
	private String areaId;
	private String areaName;
	private String optId;
	private String optName;
	private String optTime;
	private String shortName;
	private String signPath1;
	private String signPath2;
	private String signPath3;
	
	private String objId, objName;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getWorkingRange() {
		return workingRange;
	}

	public void setWorkingRange(String workingRange) {
		this.workingRange = workingRange;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDescription() {
		return description;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParentId() {
		return parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}
	public void reset(){
		this.parentName = "";
		this.companyName="";
		this.workingRange="";
		this.buildDate="";
		this.phone="";
		this.charge="";
		this.description = "";
		this.fax = "";
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

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

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

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getOptTime() {
		return optTime;
	}

	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}

	public String getSignPath1() {
		return signPath1;
	}

	public void setSignPath1(String signPath1) {
		this.signPath1 = signPath1;
	}

	public String getSignPath2() {
		return signPath2;
	}

	public void setSignPath2(String signPath2) {
		this.signPath2 = signPath2;
	}

	public String getSignPath3() {
		return signPath3;
	}

	public void setSignPath3(String signPath3) {
		this.signPath3 = signPath3;
	}
	
}
