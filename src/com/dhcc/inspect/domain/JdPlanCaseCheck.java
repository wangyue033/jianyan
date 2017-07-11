package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-20    
 * 抽检方案审核domain
 */
public class JdPlanCaseCheck extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String caseId;
	private String taskId;
	private String taskNo;
	private String taskName;
	private String handleId;
	private String isMain;
	private String planName;
	private String description;
	private String remark;
	private String isValid;
	private String checkStatus;
	private String checkStatus2;
	private String handleDeptId;
	private String handleDeptName;
	private String companyId;
	private String companyName;
	private String optId;
	private String optName;
	private String optTime;
	private String isHandle;
	private String handleOptId;
	private String handleOptName;
	private String handleOptTime;
	private String reason;
	private String compId;
	private String compName;
	private String checkOptId;
	private String checkOptName;
	private String checkOptTime;
	/*留痕数据*/
	private String tranId;
	private String nextDept;
	private String nextPerson;
	private String stepIndex;
	private String currentStatus;
	/*抽样任务*/
	private String developNumber;
	private String signNumber;
	private String overNumber;
	/*抽样单据*/
	private String sampleNumber;
	private String sampledNumber;
	private String sampleOverNumber;
	/*检验任务*/
	private String inspectNumber;
	private String inspectedNumber;
	private String inspectOverNumber;
	/*检验报告*/
	private String reportNumber;
	private String reportedNumber;
	private String reportOverNumber;
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getHandleId() {
		return handleId;
	}
	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
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
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public String getCheckStatus2() {
		return checkStatus2;
	}
	public void setCheckStatus2(String checkStatus2) {
		this.checkStatus2 = checkStatus2;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getHandleDeptId() {
		return handleDeptId;
	}
	public void setHandleDeptId(String handleDeptId) {
		this.handleDeptId = handleDeptId;
	}
	public String getHandleDeptName() {
		return handleDeptName;
	}
	public void setHandleDeptName(String handleDeptName) {
		this.handleDeptName = handleDeptName;
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
	public String getOptTime() {
		return optTime;
	}
	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	public String getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}
	public String getHandleOptId() {
		return handleOptId;
	}
	public void setHandleOptId(String handleOptId) {
		this.handleOptId = handleOptId;
	}
	public String getHandleOptName() {
		return handleOptName;
	}
	public void setHandleOptName(String handleOptName) {
		this.handleOptName = handleOptName;
	}
	public String getHandleOptTime() {
		return handleOptTime;
	}
	public void setHandleOptTime(String handleOptTime) {
		this.handleOptTime = handleOptTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getNextDept() {
		return nextDept;
	}
	public void setNextDept(String nextDept) {
		this.nextDept = nextDept;
	}
	public String getNextPerson() {
		return nextPerson;
	}
	public void setNextPerson(String nextPerson) {
		this.nextPerson = nextPerson;
	}
	public String getStepIndex() {
		return stepIndex;
	}
	public void setStepIndex(String stepIndex) {
		this.stepIndex = stepIndex;
	}
	public String getCheckOptId() {
		return checkOptId;
	}
	public void setCheckOptId(String checkOptId) {
		this.checkOptId = checkOptId;
	}
	public String getCheckOptName() {
		return checkOptName;
	}
	public void setCheckOptName(String checkOptName) {
		this.checkOptName = checkOptName;
	}
	public String getCheckOptTime() {
		return checkOptTime;
	}
	public void setCheckOptTime(String checkOptTime) {
		this.checkOptTime = checkOptTime;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getDevelopNumber() {
		return developNumber;
	}
	public void setDevelopNumber(String developNumber) {
		this.developNumber = developNumber;
	}
	public String getSignNumber() {
		return signNumber;
	}
	public void setSignNumber(String signNumber) {
		this.signNumber = signNumber;
	}
	public String getOverNumber() {
		return overNumber;
	}
	public void setOverNumber(String overNumber) {
		this.overNumber = overNumber;
	}
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
	public String getSampledNumber() {
		return sampledNumber;
	}
	public void setSampledNumber(String sampledNumber) {
		this.sampledNumber = sampledNumber;
	}
	public String getSampleOverNumber() {
		return sampleOverNumber;
	}
	public void setSampleOverNumber(String sampleOverNumber) {
		this.sampleOverNumber = sampleOverNumber;
	}
	public String getInspectNumber() {
		return inspectNumber;
	}
	public void setInspectNumber(String inspectNumber) {
		this.inspectNumber = inspectNumber;
	}
	public String getInspectedNumber() {
		return inspectedNumber;
	}
	public void setInspectedNumber(String inspectedNumber) {
		this.inspectedNumber = inspectedNumber;
	}
	public String getInspectOverNumber() {
		return inspectOverNumber;
	}
	public void setInspectOverNumber(String inspectOverNumber) {
		this.inspectOverNumber = inspectOverNumber;
	}
	public String getReportNumber() {
		return reportNumber;
	}
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	public String getReportedNumber() {
		return reportedNumber;
	}
	public void setReportedNumber(String reportedNumber) {
		this.reportedNumber = reportedNumber;
	}
	public String getReportOverNumber() {
		return reportOverNumber;
	}
	public void setReportOverNumber(String reportOverNumber) {
		this.reportOverNumber = reportOverNumber;
	}
}
