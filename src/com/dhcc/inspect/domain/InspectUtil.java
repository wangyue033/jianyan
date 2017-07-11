package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

public class InspectUtil extends BaseBean {
	private static final long serialVersionUID = 1L;
	
	private String nextStepId;
	private String tranId;
	private String stepStr;
	private String index;
	private String handleDeptId;

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

	public String getStepStr() {
		return stepStr;
	}

	public void setStepStr(String stepStr) {
		this.stepStr = stepStr;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getHandleDeptId() {
		return handleDeptId;
	}

	public void setHandleDeptId(String handleDeptId) {
		this.handleDeptId = handleDeptId;
	}
	
}
