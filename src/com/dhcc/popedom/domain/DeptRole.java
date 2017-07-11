package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

public class DeptRole extends BaseBean{
	private static final long serialVersionUID = 5256394480331202588L;
	private String deptRoleId;
	private String deptRoleName;
	private String oldDeptRoleName;
	private String canModify;
	private String remark;
	private String creator;
	private String createTime;
	private String parentId, parentName;
	private String isValid;
	private String[] selectedPsId;
	private String userIds;
	private String companyId,companyName;
	private String isCharge;
	private String optId, optName;
	
	public void reset(){
		this.setParentName("");
		this.setDeptRoleId("");
		this.setDeptRoleName("");
		this.setOldDeptRoleName("");
		this.setCanModify("");
		this.setRemark("");
		this.setCreator("");
		this.setCreateTime("");
		this.setParentId("");
		this.setParentName("");
		this.setIsValid("");
		this.setSelectedIds("");
		this.setUserIds("");
		this.setCompanyId("");
		this.setCompanyName("");
		this.setIsCharge("");
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
	public String getDeptRoleId() {
		return deptRoleId;
	}
	public void setDeptRoleId(String deptRoleId) {
		this.deptRoleId = deptRoleId;
	}
	public String getDeptRoleName() {
		return deptRoleName;
	}
	public void setDeptRoleName(String deptRoleName) {
		this.deptRoleName = deptRoleName;
	}
	public String getOldDeptRoleName() {
		return oldDeptRoleName;
	}
	public void setOldDeptRoleName(String oldDeptRoleName) {
		this.oldDeptRoleName = oldDeptRoleName;
	}
	public String getCanModify() {
		return canModify;
	}
	public void setCanModify(String canModify) {
		this.canModify = canModify;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String[] getSelectedPsId() {
		return selectedPsId;
	}
	public void setSelectedPsId(String[] selectedPsId) {
		this.selectedPsId = selectedPsId;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIsCharge() {
		return isCharge;
	}
	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}
}
