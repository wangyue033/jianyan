package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author wangyue
 * 检测机构专业类别信息   ZH_LAB_MAJOR
 */
public class ZhLabMajor extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String majorId;
	private String majorName;
	private String majorCode;
	private String remark;
	private String isValid;
	private String optId;
	private String optName;
	private String optTime;
	
	public void reset(){
		this.majorId = "";
		this.majorName = "";
		this.majorCode = "";
		this.remark = "";
		this.isValid = "";
		this.optId = "";
		this.optName = "";
		this.optTime = "";
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}
