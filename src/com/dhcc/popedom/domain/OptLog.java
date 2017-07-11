package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;
/**
 * 
 * @author WYH
 *
 */
public class OptLog extends BaseBean {
	private static final long serialVersionUID = -5999209054845558476L;
	private String logId;
	private String optId;
	private String optName;
	private String optTime; 
	private String optModule;
	private String remark;
	private String isValid;

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public String getOptModule() {
		return optModule;
	}

	public void setOptModule(String optModule) {
		this.optModule = optModule;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OptLog [logId=" + logId + ", optId=" + optId + ", optName="
				+ optName + ", optTime=" + optTime + ", optModule=" + optModule
				+ ", remark=" + remark + ", isValid=" + isValid + "]";
	}

}
