package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class JdInspectReport extends BaseBean {

	/**
	 * @author longxl
	 */
	private static final long serialVersionUID = -4743654742900615475L;

	private String reportId,inspectId,irpTitle,verificationCode,productId,departId,compId,compName,epId,inspectType,
		isValid,checkStatus,isAssigned,remark,optId,optName,optTime,reportPath,checkDesc;
	
	private String billId,itaTitle,signStatus,taskId,compNaT/*抽检任务表中的组织单位编号，对应的是放在本表格中的compId的位置*/,
		enterpriseName,companyId,companyName,parentId,productName,sampleNo,reportNo,productModel;
	private String preTranId,nextDeptId,nextDeptName,nextDealId,nextDealName,nextStepId;
	private String workId,nextDept,nextPerson,isSubmit,isMain,taskType;
	private String issueOptName,issueOptId,reportType;
	private String abolishStatus,abolishReason,abolishOptId,abolishOptTime,abolishOptName;
	
	/*留痕数据*/
	private String tranId;
	private String stepIndex;
	private String currentStatus;
	private String backType;
	private String issueDesc;
	private String currentNodeId;
	private String dealView;
	private String tranTitle;
	
	public void reset() {
		this.checkStatus = "";
		this.compId = "";
		this.departId = "";
		this.epId = "";
		this.inspectId = "";
		this.inspectType = "";
		this.irpTitle = "";
		this.isAssigned = "";
		this.isValid = "";
		this.optId = "";
		this.optTime = "";
		this.productId = "";
		this.remark = "";
		this.reportId = "";
		this.verificationCode = "";
	}
	

	public String getAbolishStatus() {
		return abolishStatus;
	}


	public void setAbolishStatus(String abolishStatus) {
		this.abolishStatus = abolishStatus;
	}


	public String getAbolishReason() {
		return abolishReason;
	}


	public void setAbolishReason(String abolishReason) {
		this.abolishReason = abolishReason;
	}


	public String getAbolishOptId() {
		return abolishOptId;
	}


	public void setAbolishOptId(String abolishOptId) {
		this.abolishOptId = abolishOptId;
	}


	public String getAbolishOptTime() {
		return abolishOptTime;
	}


	public void setAbolishOptTime(String abolishOptTime) {
		this.abolishOptTime = abolishOptTime;
	}


	public String getAbolishOptName() {
		return abolishOptName;
	}


	public void setAbolishOptName(String abolishOptName) {
		this.abolishOptName = abolishOptName;
	}


	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getIssueOptName() {
		return issueOptName;
	}

	public void setIssueOptName(String issueOptName) {
		this.issueOptName = issueOptName;
	}

	public String getIssueOptId() {
		return issueOptId;
	}

	public void setIssueOptId(String issueOptId) {
		this.issueOptId = issueOptId;
	}

	public String getDealView() {
		return dealView;
	}

	public void setDealView(String dealView) {
		this.dealView = dealView;
	}

	public String getTranTitle() {
		return tranTitle;
	}

	public void setTranTitle(String tranTitle) {
		this.tranTitle = tranTitle;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getItaTitle() {
		return itaTitle;
	}

	public void setItaTitle(String itaTitle) {
		this.itaTitle = itaTitle;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCompNaT() {
		return compNaT;
	}

	public void setCompNaT(String compNaT) {
		this.compNaT = compNaT;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getInspectId() {
		return inspectId;
	}

	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}

	public String getIrpTitle() {
		return irpTitle;
	}

	public void setIrpTitle(String irpTitle) {
		this.irpTitle = irpTitle;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getInspectType() {
		return inspectType;
	}

	public void setInspectType(String inspectType) {
		this.inspectType = inspectType;
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

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(String isAssigned) {
		this.isAssigned = isAssigned;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public String getOptTime() {
		return optTime;
	}

	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(String stepIndex) {
		this.stepIndex = stepIndex;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getIssueDesc() {
		return issueDesc;
	}

	public void setIssueDesc(String issueDesc) {
		this.issueDesc = issueDesc;
	}

	public String getPreTranId() {
		return preTranId;
	}

	public void setPreTranId(String preTranId) {
		this.preTranId = preTranId;
	}

	public String getNextDeptId() {
		return nextDeptId;
	}

	public void setNextDeptId(String nextDeptId) {
		this.nextDeptId = nextDeptId;
	}

	public String getNextDeptName() {
		return nextDeptName;
	}

	public void setNextDeptName(String nextDeptName) {
		this.nextDeptName = nextDeptName;
	}

	public String getNextDealId() {
		return nextDealId;
	}

	public void setNextDealId(String nextDealId) {
		this.nextDealId = nextDealId;
	}

	public String getNextDealName() {
		return nextDealName;
	}

	public void setNextDealName(String nextDealName) {
		this.nextDealName = nextDealName;
	}

	public String getNextStepId() {
		return nextStepId;
	}

	public void setNextStepId(String nextStepId) {
		this.nextStepId = nextStepId;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
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

	public String getCurrentNodeId() {
		return currentNodeId;
	}

	public void setCurrentNodeId(String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
}
