/**
 * 
 */
package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

/**
 * @author zhurx
 * 
 */
public class PopedomLog extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5405302250732782954L;
	private String serialId;
	private String companyId, companyName;
	private String userId;
	private String userName;
	private String userCn;
	private String state;
	private String roleId, roleName;
	private String userIds, roleIds;
	private String optId, optName, optTime, logId, logType, remark, jobName;

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getLogType() {
		return logType;
	}

	public String getRemark() {
		return remark;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOptId() {
		return optId;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCn() {
		return userCn;
	}

	public void setUserCn(String userCn) {
		this.userCn = userCn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return "PopedomLog [serialId=" + serialId
				+ ", companyId=" + companyId
				+ ", companyName=" + companyName
				+ ", userId=" + userId + ", userName="
				+ userName + ", userCn=" + userCn
				+ ", state=" + state + ", roleId="
				+ roleId + ", roleName=" + roleName
				+ ", userIds=" + userIds + ", roleIds="
				+ roleIds + ", optId=" + optId
				+ ", optName=" + optName + ", optTime="
				+ optTime + ", logId=" + logId
				+ ", logType=" + logType + ", remark="
				+ remark + ", jobName=" + jobName + "]";
	}
}
