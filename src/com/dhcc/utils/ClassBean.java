package com.dhcc.utils;

public class ClassBean {
	
	private String stepId, className;
	
	public ClassBean(String stepId, String className){
		this.stepId = stepId;
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

}
