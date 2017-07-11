package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author wangyue 抽查计划信息
 */
public class JdPlan extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String planId;
	private String planNo;
	private String planName;
	private String planType;
	private String planDate;
	private String planDesc;
	private String planAttact;
	private String isValid;
	private String compId;
	private String compName;
	private String optId;
	private String optName;
	private String optTime;
	private String path;
	private String taskCount;

	public void reset() {
		this.planId = "";
		this.planNo = "";
		this.planName = "";
		this.planType = "";
		this.planDate = "";
		this.planDesc = "";
		this.planAttact = "";
		this.isValid = "";
		this.compId = "";
		this.compName = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(String taskCount) {
		this.taskCount = taskCount;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public String getPlanAttact() {
		return planAttact;
	}

	public void setPlanAttact(String planAttact) {
		this.planAttact = planAttact;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
