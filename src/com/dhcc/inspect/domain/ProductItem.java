package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp
 * @version 1.0
 * @created 2017-3-3 产品检测项目domain
 */
public class ProductItem extends BaseBean {

	private static final long serialVersionUID = 1L;

	private String itemId, standardId, standardName, itemName, secondName,
			thirdName, maxValue, minValue, standardValue, meterUnit, isValid,
			optId, optName, optTime,itemClassify;
	private String shapeMaterial, gradeModel, sampleNo, specialJudge,
			inspectModel, chargeStandard, chargeBasic, chargeDiscount,
			mainComp, orgId, orgName,meterUnitId,meterUnitName,index;

	private String typeName, standardNo,standardIds;

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

	public String getStandardIds() {
		return standardIds;
	}

	public void setStandardIds(String standardIds) {
		this.standardIds = standardIds;
	}

	public String getStandardName() {
		return standardName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStandardNo() {
		return standardNo;
	}

	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getItemName() {
		return itemName;
	}

	public String getShapeMaterial() {
		return shapeMaterial;
	}

	public void setShapeMaterial(String shapeMaterial) {
		this.shapeMaterial = shapeMaterial;
	}

	public String getGradeModel() {
		return gradeModel;
	}

	public void setGradeModel(String gradeModel) {
		this.gradeModel = gradeModel;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getSpecialJudge() {
		return specialJudge;
	}

	public void setSpecialJudge(String specialJudge) {
		this.specialJudge = specialJudge;
	}

	public String getInspectModel() {
		return inspectModel;
	}

	public void setInspectModel(String inspectModel) {
		this.inspectModel = inspectModel;
	}

	public String getChargeStandard() {
		return chargeStandard;
	}

	public void setChargeStandard(String chargeStandard) {
		this.chargeStandard = chargeStandard;
	}

	public String getChargeBasic() {
		return chargeBasic;
	}

	public void setChargeBasic(String chargeBasic) {
		this.chargeBasic = chargeBasic;
	}

	public String getChargeDiscount() {
		return chargeDiscount;
	}

	public void setChargeDiscount(String chargeDiscount) {
		this.chargeDiscount = chargeDiscount;
	}

	public String getMainComp() {
		return mainComp;
	}

	public void setMainComp(String mainComp) {
		this.mainComp = mainComp;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getMeterUnitId() {
		return meterUnitId;
	}

	public void setMeterUnitId(String meterUnitId) {
		this.meterUnitId = meterUnitId;
	}

	public String getMeterUnitName() {
		return meterUnitName;
	}

	public void setMeterUnitName(String meterUnitName) {
		this.meterUnitName = meterUnitName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getItemClassify() {
		return itemClassify;
	}

	public void setItemClassify(String itemClassify) {
		this.itemClassify = itemClassify;
	}
}
