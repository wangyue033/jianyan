package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class JdInspectTemplet extends BaseBean {

	/**
	 * @author longxl
	 */
	private static final long serialVersionUID = 4179920574530615008L;

	private String templetId, templetName, billId, templetContent, optId, optName, optTime;
	
	private String taskId, sampleId, ssimTitle, epId, compId, description, remark, checkStatus, isValid;
	
	private String enterpriseName,companyName;
	
	public void reset() {
		this.templetId = "";
		this.templetName = ""; 
		this.billId = "";
		this.templetContent = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
		
		this.taskId = "";
		this.sampleId = "";
		this.ssimTitle = "";
		this.epId = "";
		this.compId = "";
		this.description = "";
		this.remark = "";
		this.checkStatus = "";
		this.isValid = "";
		
		this.enterpriseName = "";
		this.companyName = "";
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTempletId() {
		return templetId;
	}

	public void setTempletId(String templetId) {
		this.templetId = templetId;
	}

	public String getTempletName() {
		return templetName;
	}

	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getTempletContent() {
		return templetContent;
	}

	public void setTempletContent(String templetContent) {
		this.templetContent = templetContent;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getSsimTitle() {
		return ssimTitle;
	}

	public void setSsimTitle(String ssimTitle) {
		this.ssimTitle = ssimTitle;
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
}
