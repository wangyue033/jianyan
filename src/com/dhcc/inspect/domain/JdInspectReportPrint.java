package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class JdInspectReportPrint extends BaseBean {
	/** 
	
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	
	*/ 
	private static final long serialVersionUID = 1L;
	
	private String reportId,inspectType,companyName,enterpriseName,jyName,productName,reportPath,sjEnterpriseName;

	private String billId,tradeMark,productModel,productBatch,sampleQty,sampleBase,sampleState,inspestBase,judgeBase,
	isQualified,sampleDate,samplePerson;
	
	private String inspectId,enterpeiseInfo,sjInfo,optId,optName,inspectResult,issueDate;
	
	private String sampleNo,reportNo,arriveDate,sealPerson,epName,scdwName,sampleNum,verificationCode,compAddress,phone,fax,mailCode,reportType;
	
	private String recordId,author;
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getInspectResult() {
		return inspectResult;
	}

	public void setInspectResult(String inspectResult) {
		this.inspectResult = inspectResult;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getSamplePerson() {
		return samplePerson;
	}

	public void setSamplePerson(String samplePerson) {
		this.samplePerson = samplePerson;
	}

	public String getSampleDate() {
		return sampleDate;
	}

	public void setSampleDate(String sampleDate) {
		this.sampleDate = sampleDate;
	}

	public String getSjEnterpriseName() {
		return sjEnterpriseName;
	}

	public void setSjEnterpriseName(String sjEnterpriseName) {
		this.sjEnterpriseName = sjEnterpriseName;
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

	public String getInspectId() {
		return inspectId;
	}

	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}

	public String getEnterpeiseInfo() {
		return enterpeiseInfo;
	}

	public void setEnterpeiseInfo(String enterpeiseInfo) {
		this.enterpeiseInfo = enterpeiseInfo;
	}

	public String getSjInfo() {
		return sjInfo;
	}

	public void setSjInfo(String sjInfo) {
		this.sjInfo = sjInfo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
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

	public String getSampleState() {
		return sampleState;
	}

	public void setSampleState(String sampleState) {
		this.sampleState = sampleState;
	}

	public String getInspestBase() {
		return inspestBase;
	}

	public void setInspestBase(String inspestBase) {
		this.inspestBase = inspestBase;
	}

	public String getJudgeBase() {
		return judgeBase;
	}

	public void setJudgeBase(String judgeBase) {
		this.judgeBase = judgeBase;
	}

	public String getIsQualified() {
		return isQualified;
	}

	public void setIsQualified(String isQualified) {
		this.isQualified = isQualified;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getInspectType() {
		return inspectType;
	}

	public void setInspectType(String inspectType) {
		this.inspectType = inspectType;
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

	public String getJyName() {
		return jyName;
	}

	public void setJyName(String jyName) {
		this.jyName = jyName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName;
	}

	public String getScdwName() {
		return scdwName;
	}

	public void setScdwName(String scdwName) {
		this.scdwName = scdwName;
	}

	public String getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(String sampleNum) {
		this.sampleNum = sampleNum;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
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

	public String getMailCode() {
		return mailCode;
	}

	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
