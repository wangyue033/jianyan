/**
 * 
 */
package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-21    
 * 抽检任务签收Domain
 */
public class JdPlanHandle extends BaseBean{

	private static final long serialVersionUID = 1L;
	//抽检任务信息
	private String taskId;
	private String taskNo;
	private String taskName;
	private String taskType;
	private String taskAttr;
	private String compId;
	private String compName;
	private String baseNo;
	private String baseText;
	private String productName;
	private String beginDate;
	private String endDate;
	private String sampleBase;
	private String specialAsk;
	private String mainDeptName;
	//抽检任务签收信息
	private String handleId;
	private String isValid;
	private String handleOptId;
	private String handleOptName;
	private String handleOptTime;
	private String isHandle;
	private String transStatus;
	private String transDesc;
	private String signStatus;
	private String signDesc;
	private String signOptTime;
	private String signOptId;
	private String signOptName;
	private String optId;
	private String optName;
	private String optTime;
	private String CompanyId;
	private String CompanyName;
	private String currentStatus;
	
	private String handleDeptId;
	private String handleDeptName;
	private String selectAreaId;
	private String selectArea;
	private String selectAreaIds;
	private String selectEnterpriseId;
	private String selectEnterprise;
	private String selectEnterpriseIds;
	private String inspetDeptId;
	private String inspetDeptName;
	private String dealDept;
	private String handleDesc;
	private String counts;
	//留痕数据
	private String tranId;
	private String nextDeptId;
	private String nextDeptName;
	private String nextDealId;
	private String nextDealName;
	private String nextDept;
	private String nextPerson;
	private String stepIndex;
	private String dealView;
	private String tranTitle;
	private String backType;
	//抽检任务转办信息
	private	String transDeptId;
	private	String transDeptName;
	private	String transOptId;
	private	String transOptName;
	private	String transOptTime;
	
	public String getTranTitle() {
		return tranTitle;
	}
	public void setTranTitle(String tranTitle) {
		this.tranTitle = tranTitle;
	}
	public String getBackType() {
		return backType;
	}
	public void setBackType(String backType) {
		this.backType = backType;
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
	public String getSelectArea() {
		return selectArea;
	}
	public void setSelectArea(String selectArea) {
		this.selectArea = selectArea;
	}
	public String getSelectEnterprise() {
		return selectEnterprise;
	}
	public void setSelectEnterprise(String selectEnterprise) {
		this.selectEnterprise = selectEnterprise;
	}
	public String getInspetDeptId() {
		return inspetDeptId;
	}
	public void setInspetDeptId(String inspetDeptId) {
		this.inspetDeptId = inspetDeptId;
	}
	public String getInspetDeptName() {
		return inspetDeptName;
	}
	public void setInspetDeptName(String inspetDeptName) {
		this.inspetDeptName = inspetDeptName;
	}
	public String getHandleDesc() {
		return handleDesc;
	}
	public void setHandleDesc(String handleDesc) {
		this.handleDesc = handleDesc;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskAttr() {
		return taskAttr;
	}
	public void setTaskAttr(String taskAttr) {
		this.taskAttr = taskAttr;
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
	public String getBaseNo() {
		return baseNo;
	}
	public void setBaseNo(String baseNo) {
		this.baseNo = baseNo;
	}
	public String getBaseText() {
		return baseText;
	}
	public void setBaseText(String baseText) {
		this.baseText = baseText;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSampleBase() {
		return sampleBase;
	}
	public void setSampleBase(String sampleBase) {
		this.sampleBase = sampleBase;
	}
	public String getSpecialAsk() {
		return specialAsk;
	}
	public void setSpecialAsk(String specialAsk) {
		this.specialAsk = specialAsk;
	}
	public String getHandleId() {
		return handleId;
	}
	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
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
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getSignDesc() {
		return signDesc;
	}
	public void setSignDesc(String signDesc) {
		this.signDesc = signDesc;
	}
	public String getSignOptTime() {
		return signOptTime;
	}
	public void setSignOptTime(String signOptTime) {
		this.signOptTime = signOptTime;
	}
	public String getSignOptId() {
		return signOptId;
	}
	public void setSignOptId(String signOptId) {
		this.signOptId = signOptId;
	}
	public String getSignOptName() {
		return signOptName;
	}
	public void setSignOptName(String signOptName) {
		this.signOptName = signOptName;
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
	public String getCompanyId() {
		return CompanyId;
	}
	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getCounts() {
		return counts;
	}
	public void setCounts(String counts) {
		this.counts = counts;
	}
	public String getSelectAreaId() {
		return selectAreaId;
	}
	public void setSelectAreaId(String selectAreaId) {
		this.selectAreaId = selectAreaId;
	}
	public String getSelectEnterpriseId() {
		return selectEnterpriseId;
	}
	public void setSelectEnterpriseId(String selectEnterpriseId) {
		this.selectEnterpriseId = selectEnterpriseId;
	}
	public String getMainDeptName() {
		return mainDeptName;
	}
	public void setMainDeptName(String mainDeptName) {
		this.mainDeptName = mainDeptName;
	}
	public String getDealDept() {
		return dealDept;
	}
	public void setDealDept(String dealDept) {
		this.dealDept = dealDept;
	}
	public String getSelectAreaIds() {
		return selectAreaIds;
	}
	public void setSelectAreaIds(String selectAreaIds) {
		this.selectAreaIds = selectAreaIds;
	}
	public String getSelectEnterpriseIds() {
		return selectEnterpriseIds;
	}
	public void setSelectEnterpriseIds(String selectEnterpriseIds) {
		this.selectEnterpriseIds = selectEnterpriseIds;
	}
	public String getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
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
	public String getTransDeptId() {
		return transDeptId;
	}
	public void setTransDeptId(String transDeptId) {
		this.transDeptId = transDeptId;
	}
	public String getTransDeptName() {
		return transDeptName;
	}
	public void setTransDeptName(String transDeptName) {
		this.transDeptName = transDeptName;
	}
	public String getTransOptId() {
		return transOptId;
	}
	public void setTransOptId(String transOptId) {
		this.transOptId = transOptId;
	}
	public String getTransOptName() {
		return transOptName;
	}
	public void setTransOptName(String transOptName) {
		this.transOptName = transOptName;
	}
	public String getTransOptTime() {
		return transOptTime;
	}
	public void setTransOptTime(String transOptTime) {
		this.transOptTime = transOptTime;
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
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getDealView() {
		return dealView;
	}
	public void setDealView(String dealView) {
		this.dealView = dealView;
	}
}
