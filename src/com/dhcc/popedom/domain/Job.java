/**
 * 
 */
package com.dhcc.popedom.domain;

import framework.dhcc.utils.BaseBean;

/**
 * 
 * @author WYH
 *
 */
public class Job extends BaseBean {
	private static final long serialVersionUID = 5013570995625092701L;
	private String jobId;
	private String jobName;
	private String jobRange;
	private String optId;
	private String optTime;
	private String isValid;
	private String optName;
	private String companyId;
	private String companyName;

	public void reset() {
		this.jobId = "";
		this.jobName = "";
		this.jobRange = "";
		this.isValid = "";
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobRange() {
		return jobRange;
	}

	public void setJobRange(String jobRange) {
		this.jobRange = jobRange;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getOptName() {
		return optName;
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

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobName=" + jobName + ", jobRange="
				+ jobRange + ", optId=" + optId + ", optTime=" + optTime
				+ ", isValid=" + isValid + ", optName=" + optName + "]";
	}

}
