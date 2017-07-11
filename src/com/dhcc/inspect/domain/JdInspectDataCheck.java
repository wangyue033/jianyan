package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据审核Domain
 */
public class JdInspectDataCheck extends BaseBean{

	private static final long serialVersionUID = 1L;
	
		//检验任务管理
		private String inspectId;
		private String billId;
		private String itaTitle;
		private String itaPerson;
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
		private String companyId;
		private String companyName;
		private String inspectDate;
		private String mainOptId;
		private String mainOptName;
		private String compId;
		private String compName;
		private String checkDesc;
		private String sampleNo;
		private String productModel;
		private String isMain;
		
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
		private String backType;
				
		public String getCheckDesc() {
			return checkDesc;
		}
		public void setCheckDesc(String checkDesc) {
			this.checkDesc = checkDesc;
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
		public String getInspectDate() {
			return inspectDate;
		}
		public void setInspectDate(String inspectDate) {
			this.inspectDate = inspectDate;
		}
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
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
		private String checkStatus2;
		private String remark2;
		private String optId2;
		private String optName2;
		private String optTime2;
		//审核原因
		private String reason;
		
		//检验模板
		private String templetId;
		private String templetName;
		
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
		public String getRemark2() {
			return remark2;
		}
		public void setRemark2(String remark2) {
			this.remark2 = remark2;
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
		public String getCheckStatus2() {
			return checkStatus2;
		}
		public void setCheckStatus2(String checkStatus2) {
			this.checkStatus2 = checkStatus2;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
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
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
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
		public String getBackType() {
			return backType;
		}
		public void setBackType(String backType) {
			this.backType = backType;
		}
		public String getIsMain() {
			return isMain;
		}
		public void setIsMain(String isMain) {
			this.isMain = isMain;
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
		public String getOptName() {
			return optName;
		}
		public void setOptName(String optName) {
			this.optName = optName;
		}
}
