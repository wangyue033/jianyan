package com.dhcc.inspect.domain;

import java.util.ArrayList;
import java.util.List;
import framework.dhcc.utils.BaseBean;

/***********************
 * @author yangrc
 * @version 1.0
 * @created 2017-3-24
 *********************** 
 */
public class JdSample extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String sampleId;
	private String taskId, taskName;
	private String selectArea, selectEnterprise;
	private String handleDeptId, handleDeptName;
	private String showFiles;
	private String filepath;
	private String compId;
	private String compName;
	private String filename;
	private String filename2;
	private ArrayList<JdSampleBase> spList;
	private List<JdSampleBill> billList;
	private String rows;
	private String flag;
	private String taskType;
	private String productId;
	private String productName;
	private String description;
	private String remark;
	private String certUrl;
	private String productUrl;
	private String isSample;
	private String reasons;
	private String areaId;
	private String areaName;
	private String enterpriseId;
	private String enterpriseName;
	private String biousId;
	private String selectCompId;
	private String selectCompName;
	private String selectOptId;
	private String selectOptName;
	private String optId, optName, optTime;
	private String isHandle;
	private String handleOptTime;
	private String signStatus, signDesc, signOptId, signOptName, signOptTime;
	private String signCompId, signCompName, charge;
	private String sampleTitle;
	private String serialId, userName;
	private String selectEndDate;
	private String index, sampleDept;
	private String baseCount;
	private String typeId;
	private String companyId;
	private String companyName;
	private String selectPersonId;
	private String selectPerson;
	private String selectPersonIds;

	private String tranId,endOptId,endOptName,endOptTime,nextDept,nextPerson,nextStepId,dealOptTime,preTranId;
	private String billCount,sampleNo,isInspect;
	
	private String tranTitle,dealView,backType;
	private String compAddress,mailCode,phone,fax;
	private String samplePersonId,samplePersonName;
	
	public void reset() {
		this.sampleId = "";
		this.taskId = "";
		this.handleDeptId = "";
		this.handleDeptName = "";
		this.signStatus = "";
		this.signDesc = "";
		this.signOptTime = "";
		this.signOptId = "";
		this.signOptName = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
		this.selectEndDate = "";
	}

	
	public String getCompAddress() {
		return compAddress;
	}
	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getFax() {
		return fax;
	}



	public void setFax(String fax) {
		this.fax = fax;
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



	public String getSampleNo() {
		return sampleNo;
	}



	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}



	public String getIsInspect() {
		return isInspect;
	}



	public void setIsInspect(String isInspect) {
		this.isInspect = isInspect;
	}



	public String getBillCount() {
		return billCount;
	}



	public void setBillCount(String billCount) {
		this.billCount = billCount;
	}



	public String getPreTranId() {
		return preTranId;
	}



	public void setPreTranId(String preTranId) {
		this.preTranId = preTranId;
	}



	public String getDealOptTime() {
		return dealOptTime;
	}



	public void setDealOptTime(String dealOptTime) {
		this.dealOptTime = dealOptTime;
	}

	

	public String getNextStepId() {
		return nextStepId;
	}



	public void setNextStepId(String nextStepId) {
		this.nextStepId = nextStepId;
	}



	public String getNextPerson() {
		return nextPerson;
	}



	public void setNextPerson(String nextPerson) {
		this.nextPerson = nextPerson;
	}



	public String getNextDept() {
		return nextDept;
	}



	public void setNextDept(String nextDept) {
		this.nextDept = nextDept;
	}



	public String getTranId() {
		return tranId;
	}



	public void setTranId(String tranId) {
		this.tranId = tranId;
	}



	public String getEndOptId() {
		return endOptId;
	}



	public void setEndOptId(String endOptId) {
		this.endOptId = endOptId;
	}



	public String getEndOptName() {
		return endOptName;
	}



	public void setEndOptName(String endOptName) {
		this.endOptName = endOptName;
	}



	public String getEndOptTime() {
		return endOptTime;
	}



	public void setEndOptTime(String endOptTime) {
		this.endOptTime = endOptTime;
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

	public String getSampleTitle() {
		return sampleTitle;
	}

	public void setSampleTitle(String sampleTitle) {
		this.sampleTitle = sampleTitle;
	}

	public String getSignCompId() {
		return signCompId;
	}

	public void setSignCompId(String signCompId) {
		this.signCompId = signCompId;
	}

	public String getSignCompName() {
		return signCompName;
	}

	public String getBaseCount() {
		return baseCount;
	}

	public void setBaseCount(String baseCount) {
		this.baseCount = baseCount;
	}

	public void setSignCompName(String signCompName) {
		this.signCompName = signCompName;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getBiousId() {
		return biousId;
	}

	public void setBiousId(String biousId) {
		this.biousId = biousId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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

	public List<JdSampleBill> getBillList() {
		return billList;
	}

	public void setBillList(ArrayList<JdSampleBill> billList) {
		this.billList = billList;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ArrayList<JdSampleBase> getSpList() {
		return spList;
	}

	public void setSpList(ArrayList<JdSampleBase> spList) {
		this.spList = spList;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getShowFiles() {
		return showFiles;
	}

	public void setShowFiles(String showFiles) {
		this.showFiles = showFiles;
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

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCertUrl() {
		return certUrl;
	}

	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getSampleDept() {
		return sampleDept;
	}

	public void setSampleDept(String sampleDept) {
		this.sampleDept = sampleDept;
	}

	public String getSelectCompId() {
		return selectCompId;
	}

	public void setSelectCompId(String selectCompId) {
		this.selectCompId = selectCompId;
	}

	public String getSelectCompName() {
		return selectCompName;
	}

	public void setSelectCompName(String selectCompName) {
		this.selectCompName = selectCompName;
	}

	public String getSelectOptId() {
		return selectOptId;
	}

	public void setSelectOptId(String selectOptId) {
		this.selectOptId = selectOptId;
	}

	public String getSelectOptName() {
		return selectOptName;
	}

	public void setSelectOptName(String selectOptName) {
		this.selectOptName = selectOptName;
	}

	public String getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}

	public String getHandleOptTime() {
		return handleOptTime;
	}

	public void setHandleOptTime(String handleOptTime) {
		this.handleOptTime = handleOptTime;
	}

	public String getSelectEndDate() {
		return selectEndDate;
	}

	public void setSelectEndDate(String selectEndDate) {
		this.selectEndDate = selectEndDate;
	}

	public void setBillList(List<JdSampleBill> billList) {
		this.billList = billList;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public String getSelectPersonId() {
		return selectPersonId;
	}

	public void setSelectPersonId(String selectPersonId) {
		this.selectPersonId = selectPersonId;
	}

	public String getSelectPerson() {
		return selectPerson;
	}

	public void setSelectPerson(String selectPerson) {
		this.selectPerson = selectPerson;
	}

	public String getSelectPersonIds() {
		return selectPersonIds;
	}

	public void setSelectPersonIds(String selectPersonIds) {
		this.selectPersonIds = selectPersonIds;
	}
	
	public String getFilename2() {
		return filename2;
	}

	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getIsSample() {
		return isSample;
	}

	public void setIsSample(String isSample) {
		this.isSample = isSample;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public String getSamplePersonId() {
		return samplePersonId;
	}

	public void setSamplePersonId(String samplePersonId) {
		this.samplePersonId = samplePersonId;
	}

	public String getSamplePersonName() {
		return samplePersonName;
	}

	public void setSamplePersonName(String samplePersonName) {
		this.samplePersonName = samplePersonName;
	}
}
