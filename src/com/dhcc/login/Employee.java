/**
 * 
 */
package com.dhcc.login;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author zhurx
 * 
 */
public abstract class Employee implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3909870472667555222L;

	private String employeeId;

	private String serialID;

	private String userId;

	private String userName;

	private String employeeName;

	private String roleId;

	private String photoPath;

	private ArrayList<String> roles = new ArrayList<String>();

	private String companyId;

	private String companyName;

	private String marketId;

	private String password;

	private String dutyTime;

	private String canOnduty;

	private String classId;

	private String state;

	private String isNotice = "0";// 2、仅登陆时提醒。1：查询所有提醒。0：不查询提醒信息，不显示提醒。

	private String compType;

	private String orgId;

	private String orgName;

	public String getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(String isNotice) {
		this.isNotice = isNotice;
	}

	public Employee() {
	}

	public static Employee getInstance() {
		try {
			Employee employee = new EmployeeImpl();
			return employee;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getMarketId() {
		return marketId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setMarketId(String marketId) {
		this.marketId = marketId;
	}

	public String getSerialID() {
		return serialID;
	}

	public String getDutyTime() {
		return dutyTime;
	}

	public void setDutyTime(String dutyTime) {
		this.dutyTime = dutyTime;
	}

	public String getCanOnduty() {
		return canOnduty;
	}

	public void setCanOnduty(String canOnduty) {
		this.canOnduty = canOnduty;
	}

	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
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

	public abstract boolean insertEmployeeInfo(String employeeID,
			String password);

	public abstract Employee checkUser(String userId, String password);

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
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

}
