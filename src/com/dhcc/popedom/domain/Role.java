package com.dhcc.popedom.domain;

import java.util.Arrays;

import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author lgchen
 * 
 */
public class Role extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5256394480331202588L;
	private String roleId;
	private String roleName;
	private String oldRoleName;
	private String canModify;
	private String remark;
	private String creator;
	private String createTime;
	private String parentId, parentName;
	private String isValid;
	private String[] selectedPsId;
	private String userIds;

	private String companyId;
	private String companyName;

	private String optId, optName;

	public String getOldRoleName() {
		return oldRoleName;
	}

	public void setOldRoleName(String oldRoleName) {
		this.oldRoleName = oldRoleName;
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

	public void reset() {
		this.setParentName("");
		this.setRoleName("");
		this.setParentId("");
		this.setRemark("");
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String[] getSelectedPsId() {
		return selectedPsId;
	}

	public void setSelectedPsId(String[] selectedPsId) {
		this.selectedPsId = selectedPsId;
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

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName
				+ ", canModify=" + canModify + ", remark=" + remark
				+ ", creator=" + creator + ", createTime=" + createTime
				+ ", parentId=" + parentId + ", parentName=" + parentName
				+ ", isValid=" + isValid + ", selectedPsId="
				+ Arrays.toString(selectedPsId) + ", userIds=" + userIds
				+ ", optId=" + optId + ", optName=" + optName + "]";
	}
}
