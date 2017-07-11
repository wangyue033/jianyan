package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;
/**
 * 
 * @author WYH
 *
 */
public class UserLog extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logId;
	private String serialId;
	private String userId;
	private String userName;
	private String logIP;
	private String logType;
	private String logStatus;
	private String optTime;
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
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
	public String getLogIP() {
		return logIP;
	}
	public void setLogIP(String logIP) {
		this.logIP = logIP;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getLogStatus() {
		return logStatus;
	}
	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}
	public String getOptTime() {
		return optTime;
	}
	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	@Override
	public String toString() {
		return "UserLog [logId=" + logId + ", serialId=" + serialId
				+ ", userId=" + userId + ", userName=" + userName + ", logIP="
				+ logIP + ", logType=" + logType + ", logStatus=" + logStatus
				+ ", optTime=" + optTime + "]";
	}
	
	

}
