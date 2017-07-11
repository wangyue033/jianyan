package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBase extends BaseBean{

	private static final long serialVersionUID = 1L;
	private String biousId,sampleId,enterpriseId,productId,certUrl,productUrl,description,remark,isValid,optId,optTime,optName;
    private String enterpriseName,productName,productStandard,productModel,tradeMark,isSample,reasons;
	private String samplePersonId,samplePersonName;
    public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void reset(){
		this.biousId = "";
		this.sampleId = "";
		this.enterpriseId = "";
		this.productId = "";
		this.certUrl = "";
		this.productUrl = "";
		this.description = "";
		this.remark = "";
		this.isValid = "";
		this.optName = "";
		this.optId = "";
		this.optTime = "";
	}
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}
	public String getBiousId() {
		return biousId;
	}
	public void setBiousId(String biousId) {
		this.biousId = biousId;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCertUrl() {
		return certUrl;
	}
	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}
	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
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
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getProductStandard() {
		return productStandard;
	}
	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public String getTradeMark() {
		return tradeMark;
	}
	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
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
