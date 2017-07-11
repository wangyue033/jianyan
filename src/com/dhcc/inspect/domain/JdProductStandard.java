package com.dhcc.inspect.domain;

import framework.dhcc.utils.BaseBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdProductStandard extends BaseBean {

	private static final long serialVersionUID = 1L;
	private String standardId,typeId,standardNo,standardName,isValid,optId,optName,optTime;
	private String typeName;
			public String getTypeName() {
				return typeName;
			}
			
			public void setTypeName(String typeName) {
				this.typeName = typeName;
			}
			
			public String getStandardId() {
				return standardId;
			}
			
			public void setStandardId(String standardId) {
				this.standardId = standardId;
			}
			
			public String getTypeId() {
				return typeId;
			}
			
			public void setTypeId(String typeId) {
				this.typeId = typeId;
			}
			
			public String getStandardNo() {
				return standardNo;
			}
			
			public void setStandardNo(String standardNo) {
				this.standardNo = standardNo;
			}
			
			public String getStandardName() {
				return standardName;
			}
			
			public void setStandardName(String standardName) {
				this.standardName = standardName;
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
			
			public void reset(){
			this.standardId = "";
			this.typeId = "";
			this.standardNo = "";
			this.standardName = "";
			this.isValid = "";
			this.optId = "";
			this.optName = "";
			this.optTime = "";
			
			}
			}
