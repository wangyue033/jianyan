package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBill extends BaseBean{
    
	private static final long serialVersionUID = 1L;
		private String billId;
		private String taskId;
		private String taskName;
		private String sampleId;
		private String sampleNum;
		private String ssimTitle;
		private String epId;
		private String compId;
		private String sampleNo,reportNo;
		private String typeId;
		private String productId;
        private String productName;
        private String tradeMark;
        private String productModel;
        private String productDate;
        private String productBatch;
        private String sampleQty;
        private String sampleBase;
        private String batchQty;
        private String sampleDate;
        private String sampleState;
        private String productStandard;
		private String description;
		private String remark;
		private String checkStatus;
		private String isValid;
		private String optId;
		private String optName;
		private String optTime;
        private String sampleTitle;
		private String enterpriseName,companyId,companyName;
        private String productLevel;
        private String savePlace;
        private String postPlace;
        private String isExport;
        private String samplePlace;
        private String checkDept;
        private String isOvertime;
        private String isSubmit;
        private String submitOptTime;
        private String checkDesc;
        private String checkOptId;
        private String checkOptName;
        private String checkOptTime;
        private String checkOpt;
        private String isInspect;
        private String endTime;
        private String showFiles;
        /*数据留痕*/
        private String currentNodeId;
        private String backType;
        private String nextDept,nextPerson,nextDeptId,nextDeptName,nextDealName,nextDealId,tranId,preTranId,nextStepId,handleDeptId,isMain;
        private String serialId;
        private String dealView,tranTitle;
        
        private String epName,epCorp,epAddress,epLinkman,epCreditCode,epOrgCode,epPostcode,epTel,scdwName,scdwAddress,scdwPostcode,scdwLegal,
        scdwLinkman,scdwTel,scdwId,scdwLicence,scdwCode,scdwPeople,scdwOutput,scdwYield,scdwEtype,
        gyxkNum,qsxkNum,ccxkNum,qtxkNum,
        sampleBackups,bakPlace,arriveDate,postEndDate,
        sealPerson,epSeal,epSealDate,scdwSeal,scdwSealDate,backupQty,inspectTime;
        
        private String postCode,licenseNumber,orgCode,yearValue,enterpriseId,address,legalRep,contacts,telephone,economyType;
        
        public String getInspectTime() {
			return inspectTime;
		}
		public void setInspectTime(String inspectTime) {
			this.inspectTime = inspectTime;
		}
		public String getEconomyType() {
			return economyType;
		}
		public void setEconomyType(String economyType) {
			this.economyType = economyType;
		}
		public String getEpOrgCode() {
			return epOrgCode;
		}
		public void setEpOrgCode(String epOrgCode) {
			this.epOrgCode = epOrgCode;
		}
		public String getEpPostcode() {
			return epPostcode;
		}
		public void setEpPostcode(String epPostcode) {
			this.epPostcode = epPostcode;
		}
		public String getEpCreditCode() {
			return epCreditCode;
		}
		public void setEpCreditCode(String epCreditCode) {
			this.epCreditCode = epCreditCode;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getLegalRep() {
			return legalRep;
		}
		public void setLegalRep(String legalRep) {
			this.legalRep = legalRep;
		}
		public String getContacts() {
			return contacts;
		}
		public void setContacts(String contacts) {
			this.contacts = contacts;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getEnterpriseId() {
			return enterpriseId;
		}
		public void setEnterpriseId(String enterpriseId) {
			this.enterpriseId = enterpriseId;
		}
		public String getPostCode() {
			return postCode;
		}
		public void setPostCode(String postCode) {
			this.postCode = postCode;
		}
		public String getLicenseNumber() {
			return licenseNumber;
		}
		public void setLicenseNumber(String licenseNumber) {
			this.licenseNumber = licenseNumber;
		}
		public String getOrgCode() {
			return orgCode;
		}
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
		public String getYearValue() {
			return yearValue;
		}
		public void setYearValue(String yearValue) {
			this.yearValue = yearValue;
		}
		public String getReportNo() {
			return reportNo;
		}
		public void setReportNo(String reportNo) {
			this.reportNo = reportNo;
		}
		public String getPostEndDate() {
			return postEndDate;
		}
		public void setPostEndDate(String postEndDate) {
			this.postEndDate = postEndDate;
		}
		public String getBackupQty() {
			return backupQty;
		}
		public void setBackupQty(String backupQty) {
			this.backupQty = backupQty;
		}
		public String getScdwId() {
			return scdwId;
		}
		public void setScdwId(String scdwId) {
			this.scdwId = scdwId;
		}
		public String getEpName() {
			return epName;
		}
		public void setEpName(String epName) {
			this.epName = epName;
		}
		public String getEpCorp() {
			return epCorp;
		}
		public void setEpCorp(String epCorp) {
			this.epCorp = epCorp;
		}
		public String getEpAddress() {
			return epAddress;
		}
		public void setEpAddress(String epAddress) {
			this.epAddress = epAddress;
		}
		public String getEpLinkman() {
			return epLinkman;
		}
		public void setEpLinkman(String epLinkman) {
			this.epLinkman = epLinkman;
		}
		public String getEpTel() {
			return epTel;
		}
		public void setEpTel(String epTel) {
			this.epTel = epTel;
		}
		public String getScdwName() {
			return scdwName;
		}
		public void setScdwName(String scdwName) {
			this.scdwName = scdwName;
		}
		public String getScdwAddress() {
			return scdwAddress;
		}
		public void setScdwAddress(String scdwAddress) {
			this.scdwAddress = scdwAddress;
		}
		public String getScdwPostcode() {
			return scdwPostcode;
		}
		public void setScdwPostcode(String scdwPostcode) {
			this.scdwPostcode = scdwPostcode;
		}
		public String getScdwLegal() {
			return scdwLegal;
		}
		public void setScdwLegal(String scdwLegal) {
			this.scdwLegal = scdwLegal;
		}
		public String getScdwLinkman() {
			return scdwLinkman;
		}
		public void setScdwLinkman(String scdwLinkman) {
			this.scdwLinkman = scdwLinkman;
		}
		public String getScdwTel() {
			return scdwTel;
		}
		public void setScdwTel(String scdwTel) {
			this.scdwTel = scdwTel;
		}
		public String getScdwLicence() {
			return scdwLicence;
		}
		public void setScdwLicence(String scdwLicence) {
			this.scdwLicence = scdwLicence;
		}
		public String getScdwCode() {
			return scdwCode;
		}
		public void setScdwCode(String scdwCode) {
			this.scdwCode = scdwCode;
		}
		public String getScdwPeople() {
			return scdwPeople;
		}
		public void setScdwPeople(String scdwPeople) {
			this.scdwPeople = scdwPeople;
		}
		public String getScdwOutput() {
			return scdwOutput;
		}
		public void setScdwOutput(String scdwOutput) {
			this.scdwOutput = scdwOutput;
		}
		public String getScdwYield() {
			return scdwYield;
		}
		public void setScdwYield(String scdwYield) {
			this.scdwYield = scdwYield;
		}
		public String getScdwEtype() {
			return scdwEtype;
		}
		public void setScdwEtype(String scdwEtype) {
			this.scdwEtype = scdwEtype;
		}
		public String getGyxkNum() {
			return gyxkNum;
		}
		public void setGyxkNum(String gyxkNum) {
			this.gyxkNum = gyxkNum;
		}
		public String getQsxkNum() {
			return qsxkNum;
		}
		public void setQsxkNum(String qsxkNum) {
			this.qsxkNum = qsxkNum;
		}
		public String getCcxkNum() {
			return ccxkNum;
		}
		public void setCcxkNum(String ccxkNum) {
			this.ccxkNum = ccxkNum;
		}
		public String getQtxkNum() {
			return qtxkNum;
		}
		public void setQtxkNum(String qtxkNum) {
			this.qtxkNum = qtxkNum;
		}
		public String getSampleBackups() {
			return sampleBackups;
		}
		public void setSampleBackups(String sampleBackups) {
			this.sampleBackups = sampleBackups;
		}
		public String getBakPlace() {
			return bakPlace;
		}
		public void setBakPlace(String bakPlace) {
			this.bakPlace = bakPlace;
		}
		public String getArriveDate() {
			return arriveDate;
		}
		public void setArriveDate(String arriveDate) {
			this.arriveDate = arriveDate;
		}
		public String getSealPerson() {
			return sealPerson;
		}
		public void setSealPerson(String sealPerson) {
			this.sealPerson = sealPerson;
		}
		public String getEpSeal() {
			return epSeal;
		}
		public void setEpSeal(String epSeal) {
			this.epSeal = epSeal;
		}
		public String getEpSealDate() {
			return epSealDate;
		}
		public void setEpSealDate(String epSealDate) {
			this.epSealDate = epSealDate;
		}
		public String getScdwSeal() {
			return scdwSeal;
		}
		public void setScdwSeal(String scdwSeal) {
			this.scdwSeal = scdwSeal;
		}
		public String getScdwSealDate() {
			return scdwSealDate;
		}
		public void setScdwSealDate(String scdwSealDate) {
			this.scdwSealDate = scdwSealDate;
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
		public String getSerialId() {
			return serialId;
		}
		public void setSerialId(String serialId) {
			this.serialId = serialId;
		}
		public String getIsMain() {
			return isMain;
		}
		public void setIsMain(String isMain) {
			this.isMain = isMain;
		}
		public String getHandleDeptId() {
			return handleDeptId;
		}
		public void setHandleDeptId(String handleDeptId) {
			this.handleDeptId = handleDeptId;
		}
		public String getNextStepId() {
			return nextStepId;
		}
		public void setNextStepId(String nextStepId) {
			this.nextStepId = nextStepId;
		}
		public String getTranId() {
			return tranId;
		}
		public void setTranId(String tranId) {
			this.tranId = tranId;
		}
		public String getPreTranId() {
			return preTranId;
		}
		public void setPreTranId(String preTranId) {
			this.preTranId = preTranId;
		}
		public String getNextDeptName() {
			return nextDeptName;
		}
		public void setNextDeptName(String nextDeptName) {
			this.nextDeptName = nextDeptName;
		}
		public String getNextDeptId() {
			return nextDeptId;
		}
		public void setNextDeptId(String nextDeptId) {
			this.nextDeptId = nextDeptId;
		}
		public String getNextDealName() {
			return nextDealName;
		}
		public void setNextDealName(String nextDealName) {
			this.nextDealName = nextDealName;
		}
		public String getNextDealId() {
			return nextDealId;
		}
		public void setNextDealId(String nextDealId) {
			this.nextDealId = nextDealId;
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
		public String getSampleTitle() {
			return sampleTitle;
		}
		public void setSampleTitle(String sampleTitle) {
			this.sampleTitle = sampleTitle;
		}
        public String getProductId() {
			return productId;
		}
        public String getOptName() {
			return optName;
		}
		public void setOptName(String optName) {
			this.optName = optName;
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
		public String getTradeMark() {
			return tradeMark;
		}
		public void setTradeMark(String tradeMark) {
			this.tradeMark = tradeMark;
		}
		public String getProductModel() {
			return productModel;
		}
		public void setProductModel(String productModel) {
			this.productModel = productModel;
		}
		public String getProductBatch() {
			return productBatch;
		}
		public void setProductBatch(String productBatch) {
			this.productBatch = productBatch;
		}
		public String getSampleQty() {
			return sampleQty;
		}
		public void setSampleQty(String sampleQty) {
			this.sampleQty = sampleQty;
		}
		public String getSampleBase() {
			return sampleBase;
		}
		public void setSampleBase(String sampleBase) {
			this.sampleBase = sampleBase;
		}
		public String getSampleDate() {
			return sampleDate;
		}
		public void setSampleDate(String sampleDate) {
			this.sampleDate = sampleDate;
		}
		public String getSampleState() {
			return sampleState;
		}
		public void setSampleState(String sampleState) {
			this.sampleState = sampleState;
		}
		public String getProductStandard() {
			return productStandard;
		}
		public void setProductStandard(String productStandard) {
			this.productStandard = productStandard;
		}
		public String getProductLevel() {
			return productLevel;
		}
		public void setProductLevel(String productLevel) {
			this.productLevel = productLevel;
		}
		public String getSavePlace() {
			return savePlace;
		}
		public void setSavePlace(String savePlace) {
			this.savePlace = savePlace;
		}
		public String getPostPlace() {
			return postPlace;
		}
		public void setPostPlace(String postPlace) {
			this.postPlace = postPlace;
		}
		public String getIsExport() {
			return isExport;
		}
		public void setIsExport(String isExport) {
			this.isExport = isExport;
		}
		public String getSamplePlace() {
			return samplePlace;
		}
		public void setSamplePlace(String samplePlace) {
			this.samplePlace = samplePlace;
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

		public void reset(){
			this.billId = "";
			this.taskId = "";
			this.sampleId = "";
			this.ssimTitle = "";
			this.compId = "";
			this.description = "";
			this.remark = "";
			this.checkStatus = "";
			this.isValid = "";
			this.optId = "";
			this.optTime = "";
}
        
		public String getBillId() {
			return billId;
		}

		public void setBillId(String billId) {
			this.billId = billId;
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

		public String getCheckDept() {
			return checkDept;
		}

		public void setCheckDept(String checkDept) {
			this.checkDept = checkDept;
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getSampleNum() {
			return sampleNum;
		}

		public void setSampleNum(String sampleNum) {
			this.sampleNum = sampleNum;
		}
		public String getEpId() {
			return epId;
		}
		public void setEpId(String epId) {
			this.epId = epId;
		}
		public String getSampleNo() {
			return sampleNo;
		}
		public void setSampleNo(String sampleNo) {
			this.sampleNo = sampleNo;
		}
		public String getProductDate() {
			return productDate;
		}
		public void setProductDate(String productDate) {
			this.productDate = productDate;
		}
		public String getBatchQty() {
			return batchQty;
		}
		public void setBatchQty(String batchQty) {
			this.batchQty = batchQty;
		}
		public String getIsOvertime() {
			return isOvertime;
		}
		public void setIsOvertime(String isOvertime) {
			this.isOvertime = isOvertime;
		}
		public String getIsSubmit() {
			return isSubmit;
		}
		public void setIsSubmit(String isSubmit) {
			this.isSubmit = isSubmit;
		}
		public String getSubmitOptTime() {
			return submitOptTime;
		}
		public void setSubmitOptTime(String submitOptTime) {
			this.submitOptTime = submitOptTime;
		}
		public String getCheckDesc() {
			return checkDesc;
		}
		public void setCheckDesc(String checkDesc) {
			this.checkDesc = checkDesc;
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
		public String getCheckOpt() {
			return checkOpt;
		}
		public void setCheckOpt(String checkOpt) {
			this.checkOpt = checkOpt;
		}
		public String getIsInspect() {
			return isInspect;
		}
		public void setIsInspect(String isInspect) {
			this.isInspect = isInspect;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getCheckOptTime() {
			return checkOptTime;
		}
		public void setCheckOptTime(String checkOptTime) {
			this.checkOptTime = checkOptTime;
		}
		public String getTypeId() {
			return typeId;
		}
		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public String getCurrentNodeId() {
			return currentNodeId;
		}
		public void setCurrentNodeId(String currentNodeId) {
			this.currentNodeId = currentNodeId;
		}
		public String getBackType() {
			return backType;
		}
		public void setBackType(String backType) {
			this.backType = backType;
		}
		public String getShowFiles() {
			return showFiles;
		}
		public void setShowFiles(String showFiles) {
			this.showFiles = showFiles;
		}
	}
