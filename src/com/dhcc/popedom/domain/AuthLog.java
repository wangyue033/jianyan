package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

public class AuthLog extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleName;
	private String logType;
	private String serialId;
	private String userName;
	private String companyName;
	private String jobName;
	private String optName;
	private String optTime;
	
	private String optId;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
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
	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	@Override
	public String toString() {
		return "AuthLog [roleName=" + roleName + ", logType=" + logType
				+ ", serialId=" + serialId + ", userName=" + userName
				+ ", companyName=" + companyName + ", jobName=" + jobName
				+ ", optName=" + optName + ", optTime=" + optTime + ", optId="
				+ optId + "]";
	}
	

}
