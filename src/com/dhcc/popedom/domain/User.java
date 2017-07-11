package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author lgchen
 *
 */
public class User extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String serialId,companyId,jobId,userId,userName,userCn,password,question,answer,state,marketId;
	
	private String optId,optName,companyName,jobName,olduserId;
	
	private String sex,certType,certNo,email,telPhone,workDesc,birthDate,photoPath,mobilePhone;
	
	private String checkDept;
	
	public String getOlduserId() {
		return olduserId;
	}

	public void setOlduserId(String olduserId) {
		this.olduserId = olduserId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public void reset(){
		this.serialId = "";
		this.companyId = "";
		this.jobId = "";
		this.userId = "";
		this.userName = "";
		this.userCn = "";
		this.password = "";
		this.question = "";
		this.answer = "";
		this.state = "";
		this.marketId = "";
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCn() {
		return userCn;
	}

	public void setUserCn(String userCn) {
		this.userCn = userCn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMarketId() {
		return marketId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getWorkDesc() {
		return workDesc;
	}

	public void setWorkDesc(String workDesc) {
		this.workDesc = workDesc;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCheckDept() {
		return checkDept;
	}

	public void setCheckDept(String checkDept) {
		this.checkDept = checkDept;
	}

	@Override
	public String toString() {
		return "User [serialId=" + serialId
				+ ", companyId=" + companyId + ", jobId="
				+ jobId + ", userId=" + userId
				+ ", userName=" + userName + ", userCn="
				+ userCn + ", password=" + password
				+ ", question=" + question + ", answer="
				+ answer + ", state=" + state
				+ ", marketId=" + marketId + ", optId="
				+ optId + ", optName=" + optName
				+ ", companyName=" + companyName
				+ ", jobName=" + jobName + ", olduserId="
				+ olduserId + ", sex=" + sex
				+ ", certType=" + certType + ", certNo="
				+ certNo + ", email=" + email
				+ ", telPhone=" + telPhone
				+ ", workDesc=" + workDesc
				+ ", birthDate=" + birthDate
				+ ", photoPath=" + photoPath + "]";
	}

	
}
