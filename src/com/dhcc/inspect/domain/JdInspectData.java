package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据填报Domain
 */
public class JdInspectData extends BaseBean{

	private static final long serialVersionUID = 1L;
	//检验任务管理
	private String inspectId;
	private String billId;
	private String itaNo;
	private String itaTitle;
	private String itaPerson;
	private String person;
	private String startTime;
	private String endTime;
	private String description;
	private String remark;
	private String isValid;
	private String isAssinged;
	private String optId;
	private String optName;
	private String optTime;
	private String signStatus;
	private String signOptId;
	private String signOptTime;
	private String signOptName;
	private String sampleNo;
	private String productModel;
	private String isSubmit;
	
	//检验数据管理
	private String dataId;
	private String idmTitle;
	private String citId;
	private String inspectTime;
	private String productId;
	private String productName;
	private String indexId;
	private String qualifiedStandard;
	private String isQualified;
	private String isValid2;
	private String checkStatus;
	private String fillDesc;
	private String optId2;
	private String optName2;
	private String optTime2;
	private String companyId;
	private String companyName;
	private String mainOptId;
	private String mainOptName;
	private String outFlag;
	private String outCompname;
		
	//检测项目信息
	private String inspectTaskId;
	private String itemId;
	private String standardId;
	private String standardName;
	private String itemName;
	private String secondName;
	private String thirdName;
	private String maxValue;
	private String minValue;
	private String standardValue;
	private String meterUnit;
	private String inspectValue;
	private String inspectResult;
	private String inspectOptName;
	private String inspectDate;
	
	/*留痕数据*/
	private String tranId;
	private String nextDept;
	private String nextPerson;
	private String stepIndex;
	private String currentStatus;
	private String nextDeptId;
	private String nextDeptName;
	private String nextDealId;
	private String nextDealName;
	private String reason;
	private String backType;
	private String ssimTitle;
	private String tranTitle;
	private String dealView;

	/*检验报告*/
	private String reportNo;
	private String verificationCode;
	private String reportType,departId,compId,epId,inspectType,reportPath,scdwId;
	
	public String getScdwId() {
		return scdwId;
	}
	public void setScdwId(String scdwId) {
		this.scdwId = scdwId;
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
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getOutCompname() {
		return outCompname;
	}
	public void setOutCompname(String outCompname) {
		this.outCompname = outCompname;
	}
	public String getOutFlag() {
		return outFlag;
	}
	public void setOutFlag(String outFlag) {
		this.outFlag = outFlag;
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
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}
	public String getSsimTitle() {
		return ssimTitle;
	}
	public void setSsimTitle(String ssimTitle) {
		this.ssimTitle = ssimTitle;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public String getInspectDate() {
		return inspectDate;
	}
	public void setInspectDate(String inspectDate) {
		this.inspectDate = inspectDate;
	}
	public String getFillDesc() {
		return fillDesc;
	}
	public void setFillDesc(String fillDesc) {
		this.fillDesc = fillDesc;
	}
	public String getInspectResult() {
		return inspectResult;
	}
	public void setInspectResult(String inspectResult) {
		this.inspectResult = inspectResult;
	}
	public String getInspectOptName() {
		return inspectOptName;
	}
	public void setInspectOptName(String inspectOptName) {
		this.inspectOptName = inspectOptName;
	}
	public String getMainOptId() {
		return mainOptId;
	}
	public void setMainOptId(String mainOptId) {
		this.mainOptId = mainOptId;
	}
	public String getMainOptName() {
		return mainOptName;
	}
	public void setMainOptName(String mainOptName) {
		this.mainOptName = mainOptName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getInspectId() {
		return inspectId;
	}
	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getItaNo() {
		return itaNo;
	}
	public void setItaNo(String itaNo) {
		this.itaNo = itaNo;
	}
	public String getItaTitle() {
		return itaTitle;
	}
	public void setItaTitle(String itaTitle) {
		this.itaTitle = itaTitle;
	}
	public String getItaPerson() {
		return itaPerson;
	}
	public void setItaPerson(String itaPerson) {
		this.itaPerson = itaPerson;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public String getIsAssinged() {
		return isAssinged;
	}
	public void setIsAssinged(String isAssinged) {
		this.isAssinged = isAssinged;
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
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getSignOptId() {
		return signOptId;
	}
	public void setSignOptId(String signOptId) {
		this.signOptId = signOptId;
	}
	public String getSignOptTime() {
		return signOptTime;
	}
	public void setSignOptTime(String signOptTime) {
		this.signOptTime = signOptTime;
	}
	public String getSignOptName() {
		return signOptName;
	}
	public void setSignOptName(String signOptName) {
		this.signOptName = signOptName;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getIdmTitle() {
		return idmTitle;
	}
	public void setIdmTitle(String idmTitle) {
		this.idmTitle = idmTitle;
	}
	public String getCitId() {
		return citId;
	}
	public void setCitId(String citId) {
		this.citId = citId;
	}
	public String getInspectTime() {
		return inspectTime;
	}
	public void setInspectTime(String inspectTime) {
		this.inspectTime = inspectTime;
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
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getQualifiedStandard() {
		return qualifiedStandard;
	}
	public void setQualifiedStandard(String qualifiedStandard) {
		this.qualifiedStandard = qualifiedStandard;
	}
	public String getIsQualified() {
		return isQualified;
	}
	public void setIsQualified(String isQualified) {
		this.isQualified = isQualified;
	}
	public String getIsValid2() {
		return isValid2;
	}
	public void setIsValid2(String isValid2) {
		this.isValid2 = isValid2;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getOptId2() {
		return optId2;
	}
	public void setOptId2(String optId2) {
		this.optId2 = optId2;
	}
	public String getOptName2() {
		return optName2;
	}
	public void setOptName2(String optName2) {
		this.optName2 = optName2;
	}
	public String getOptTime2() {
		return optTime2;
	}
	public void setOptTime2(String optTime2) {
		this.optTime2 = optTime2;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getInspectTaskId() {
		return inspectTaskId;
	}
	public void setInspectTaskId(String inspectTaskId) {
		this.inspectTaskId = inspectTaskId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getStandardId() {
		return standardId;
	}
	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getThirdName() {
		return thirdName;
	}
	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}
	public String getMeterUnit() {
		return meterUnit;
	}
	public void setMeterUnit(String meterUnit) {
		this.meterUnit = meterUnit;
	}
	public String getInspectValue() {
		return inspectValue;
	}
	public void setInspectValue(String inspectValue) {
		this.inspectValue = inspectValue;
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
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getBackType() {
		return backType;
	}
	public void setBackType(String backType) {
		this.backType = backType;
	}
	public String getIsSubmit() {
		return isSubmit;
	}
	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
}
