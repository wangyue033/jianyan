package com.dhcc.login;

import framework.dhcc.utils.BaseBean;

/**
 * @author xinxin
 * 
 */
public class NoticeType extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6710991292072069763L;
	private String noticeType, actionName, typeName, showContent, noticeAttr, isFromSys, isAssign;
	private String staffIsAssign;
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getNoticeAttr() {
		return noticeAttr;
	}
	public void setNoticeAttr(String noticeAttr) {
		this.noticeAttr = noticeAttr;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getShowContent() {
		return showContent;
	}
	public void setShowContent(String showContent) {
		this.showContent = showContent;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getIsAssign() {
		return isAssign;
	}
	public void setIsAssign(String isAssign) {
		this.isAssign = isAssign;
	}
	public String getIsFromSys() {
		return isFromSys;
	}
	public void setIsFromSys(String isFromSys) {
		this.isFromSys = isFromSys;
	}
	public String getStaffIsAssign() {
		return staffIsAssign;
	}
	public void setStaffIsAssign(String staffIsAssign) {
		this.staffIsAssign = staffIsAssign;
	}

}
