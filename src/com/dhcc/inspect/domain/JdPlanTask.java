package com.dhcc.inspect.domain;

import java.util.List;
import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author wangyue 
 * 抽检任务信息 JD_PLAN_TASK
 */
public class JdPlanTask extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String planId;
	private String taskNo;
	private String planName;
	private String taskName;
	private String taskType;
	private String taskAttr;
	private String compId;
	private String compName;
	private String baseNo;
	private String baseText;
	private String productId;
	private String productName;
	private String beginDate;
	private String endDate;
	private String sampleBatch;
	private String sampleBase;
	private String specialAsk;
	private String mainDeptId;
	private String mainDeptName;
	private String isValid;
	private String optId;
	private String optName;
	private String optTime;
	private String isHandle;
	private String handleOptId;
	private String handleOptName;
	private String handleOptTime;
	private String transStatus;
	private String signStatus;
	private String signDesc;
	private String signOptId;
	private String signOptName;
	private String signOptTime;
	private String path;
	private String rows;
	
	private String nextDept;
	private String nextPerson;
	private String nextDeptId;
	private String nextDeptName;
	private String nextOptId;
	private String nextOptName;
	private String handleDeptId;
	private String handleDeptName;
	private String selectAreaId;
	private String selectArea;
	private String selectAreaIds;
	private String selectEnterpriseId;
	private String selectEnterpriseIds;
	private String selectEnterprise;
	private String inspetDeptId;
	private String inspetDeptName;
	private String dealDept;
	private String flag;//区分是添加还是修改
	/*private String selectEndDate;
	private String inspetEndDate;*/
	
	private List<JdPlanHandle> list;

	private String tranTitle,dealView,backType;
	
	
	public void reset() {
		this.taskId = "";
		this.planId = "";
		this.planName = "";
		this.taskNo = "";
		this.taskName = "";
		this.taskType = "";
		this.taskAttr = "";
		this.compId = "";
		this.compName = "";
		this.baseNo = "";
		this.baseText = "";
		this.productName = "";
		this.beginDate = "";
		this.endDate = "";
		this.sampleBatch = "";
		this.sampleBase = "";
		this.specialAsk = "";
		this.mainDeptId = "";
		this.isValid = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
		this.isHandle = "";
		this.handleOptId = "";
		this.handleOptName = "";
		this.handleOptTime = "";
		this.path = "";
		this.list =null;
	}

	public String getTranTitle() {
		return tranTitle;
	}

	public void setTranTitle(String tranTitle) {
		this.tranTitle = tranTitle;
	}

	public String getDealView() {
		return dealView;
	}

	public void setDealView(String dealView) {
		this.dealView = dealView;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public List<JdPlanHandle> getList() {
		return list;
	}

	public void setList(List<JdPlanHandle> list) {
		this.list = list;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
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

	public String getSampleBatch() {
		return sampleBatch;
	}

	public void setSampleBatch(String sampleBatch) {
		this.sampleBatch = sampleBatch;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getMainDeptId() {
		return mainDeptId;
	}

	public void setMainDeptId(String mainDeptId) {
		this.mainDeptId = mainDeptId;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
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

	public String getSelectAreaId() {
		return selectAreaId;
	}

	public void setSelectAreaId(String selectAreaId) {
		this.selectAreaId = selectAreaId;
	}

	public String getSelectArea() {
		return selectArea;
	}

	public void setSelectArea(String selectArea) {
		this.selectArea = selectArea;
	}

	public String getSelectEnterpriseId() {
		return selectEnterpriseId;
	}

	public void setSelectEnterpriseId(String selectEnterpriseId) {
		this.selectEnterpriseId = selectEnterpriseId;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMainDeptName() {
		return mainDeptName;
	}

	public void setMainDeptName(String mainDeptName) {
		this.mainDeptName = mainDeptName;
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

	public String getSignOptTime() {
		return signOptTime;
	}

	public void setSignOptTime(String signOptTime) {
		this.signOptTime = signOptTime;
	}

	public String getDealDept() {
		return dealDept;
	}

	public void setDealDept(String dealDept) {
		this.dealDept = dealDept;
	}

	public String getSelectEnterpriseIds() {
		return selectEnterpriseIds;
	}

	public void setSelectEnterpriseIds(String selectEnterpriseIds) {
		this.selectEnterpriseIds = selectEnterpriseIds;
	}

	public String getSelectAreaIds() {
		return selectAreaIds;
	}

	public void setSelectAreaIds(String selectAreaIds) {
		this.selectAreaIds = selectAreaIds;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public String getNextOptId() {
		return nextOptId;
	}

	public void setNextOptId(String nextOptId) {
		this.nextOptId = nextOptId;
	}

	public String getNextOptName() {
		return nextOptName;
	}

	public void setNextOptName(String nextOptName) {
		this.nextOptName = nextOptName;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
}
