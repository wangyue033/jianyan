package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class JdInspectHandleSign extends BaseBean {

	/**
	 * @author longxl
	 */
	private static final long serialVersionUID = -4914313256561208289L;

	private String inspectId,billId,itaTitle,mainOptId,mainOptName,endTime,description,remark,isValid, 
		isHandle,optTime,signStatus,signOptId,signOptTime,signOptName, signOptOpinion;
	
	private String ssimTitle,optId,optName,companyId,companyName,productId,productName,sampleNo,productModel;
	/*留痕数据*/
	private String tranId;
	private String isMain;
	private String nextDept;
	private String nextPerson;
	private String stepIndex;
	private String currentStatus;
	
	public void reset () {
		this.billId = "";
		this.description = "";
		this.endTime = "";
		this.inspectId = "";
		this.isHandle = "";
		this.isValid = "";
		this.itaTitle = "";
		this.optId = "";
		this.optTime = "";
		this.remark = "";
		this.signOptTime = "";
		this.signOptId = "";
		this.signOptName = "";
		this.signStatus = "";
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

	public String getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getSignOptOpinion() {
		return signOptOpinion;
	}

	public void setSignOptOpinion(String signOptOpinion) {
		this.signOptOpinion = signOptOpinion;
	}

	public String getSsimTitle() {
		return ssimTitle;
	}

	public void setSsimTitle(String ssimTitle) {
		this.ssimTitle = ssimTitle;
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

	public String getItaTitle() {
		return itaTitle;
	}

	public void setItaTitle(String itaTitle) {
		this.itaTitle = itaTitle;
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

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
