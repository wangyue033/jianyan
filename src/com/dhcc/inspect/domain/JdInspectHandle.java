package com.dhcc.inspect.domain;

import java.util.ArrayList;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.utils.BaseBean;

public class JdInspectHandle extends BaseBean {

	/**
	 * @author longxl
	 */
	private static final long serialVersionUID = 3193681586343238717L;

	private String inspectId,billId,itaTitle,mainOptId,mainOptName,orgId,startTime,endTime,description,remark,isValid,isAssinged,optId,optTime,productId;
	
	private String taskId,sampleId,ssimTitle,epId,compId,compName,checkStatus;
	
	private String optName,userId,userName,companyId,companyName,serialId,StandardName,signStatus;
	
	private String itemId,newItemId,caseId,standardId,itemName,secondName,thirdName,maxValue,minValue,standardValue,meterUnit,ck,shapeMaterial,gradeModel;
	private String inspectOptId,inspectOptName;
	
	private String selectedPsId,dataFrom,sampleNo,productModel,productName,itemfaId,itemfaName,outFlag,outCompname,outDate,outResult;
	
	/*留痕数据*/
	private String isMain;
	private String tranId;
	private String currentStatus;
	private String nextDept;
	private String nextPerson;
	private String dealPartId;
	private String dealPartName;
	private String dealOptId;
	private String dealOptName;
	private String tranTitle,dealView,backType;
	
	public void reset () {
		this.inspectId = "";
		this.billId = "";
		this.itaTitle = "";
		this.startTime = "";
		this.endTime = "";
		this.description = "";
		this.remark = "";
		this.isValid = "";
		this.isAssinged = "";
		this.optId = "";
		this.optTime = "";
		
		this.taskId = "";
		this.sampleId = "";
		this.ssimTitle = "";
		this.epId = "";
		this.compId = "";
		this.checkStatus = "";
	}
	
	private String menuId;
	private String menuName;
	private String path2;
	private String moduleId;
	private String moduleName;
	private String menuId2;
	private String checkOptId;
	private ArrayList<JdInspectHandle> moduleList = new ArrayList<JdInspectHandle>();
	
	public JdInspectHandle() {

	}

	public JdInspectHandle(String menuId, String menuName,String path2) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.path2 = path2;
	}
	
	public JdInspectHandle(String moduleId, String moduleName,String menuId2,String s) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.menuId2 = menuId2;
	}

	public void setModuleFromMenu(String menuId) {
		String sql = "select distinct  item_id,item_name " +
				"from jd_product_item " +
				"where standard_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { menuId });
		try {
			while (crs.next()) {
				String sql1 = "select count(*) from jd_plan_item where item_id = ? ";
				try{
					long obj = (Long) DBFacade.getInstance()
							.getValueBySql(sql1, new String[] { crs.getString("item_id") });
					if(obj>0){
						
					}else{
						JdInspectHandle module = new JdInspectHandle(crs.getString("item_id"), crs
								.getString("item_name"), menuId, "1");
						moduleList.add(module);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public String getCheckOptId() {
		return checkOptId;
	}

	public void setCheckOptId(String checkOptId) {
		this.checkOptId = checkOptId;
	}

	public String getTranTitle() {
		return tranTitle;
	}

	public void setTranTitle(String tranTitle) {
		this.tranTitle = tranTitle;
	}

	public String getDealView() {
		return dealView;
	}

	public void setDealView(String dealView) {
		this.dealView = dealView;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMainOptId() {
		return mainOptId;
	}

	public void setMainOptId(String mainOptId) {
		this.mainOptId = mainOptId;
	}

	public String getMainOptName() {
		return mainOptName;
	}

	public void setMainOptName(String mainOptName) {
		this.mainOptName = mainOptName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getCk() {
		return ck;
	}

	public void setCk(String ck) {
		this.ck = ck;
	}

	public String getStandardName() {
		return StandardName;
	}

	public void setStandardName(String standardName) {
		StandardName = standardName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getStandardId() {
		return standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}

	public String getItemName() {
		return itemName;
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

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getInspectId() {
		return inspectId;
	}

	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getItaTitle() {
		return itaTitle;
	}

	public void setItaTitle(String itaTitle) {
		this.itaTitle = itaTitle;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getIsAssinged() {
		return isAssinged;
	}

	public void setIsAssinged(String isAssinged) {
		this.isAssinged = isAssinged;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getSsimTitle() {
		return ssimTitle;
	}

	public void setSsimTitle(String ssimTitle) {
		this.ssimTitle = ssimTitle;
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	public String getSelectedPsId() {
		return selectedPsId;
	}

	public void setSelectedPsId(String selectedPsId) {
		this.selectedPsId = selectedPsId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getPath2() {
		return path2;
	}

	public void setPath2(String path2) {
		this.path2 = path2;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMenuId2() {
		return menuId2;
	}

	public void setMenuId2(String menuId2) {
		this.menuId2 = menuId2;
	}

	public ArrayList<JdInspectHandle> getModuleList() {
		return moduleList;
	}

	public void setModuleList(ArrayList<JdInspectHandle> moduleList) {
		this.moduleList = moduleList;
	}

	public String getNewItemId() {
		return newItemId;
	}

	public void setNewItemId(String newItemId) {
		this.newItemId = newItemId;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getItemfaId() {
		return itemfaId;
	}

	public void setItemfaId(String itemfaId) {
		this.itemfaId = itemfaId;
	}

	public String getItemfaName() {
		return itemfaName;
	}

	public void setItemfaName(String itemfaName) {
		this.itemfaName = itemfaName;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getNextDept() {
		return nextDept;
	}

	public void setNextDept(String nextDept) {
		this.nextDept = nextDept;
	}

	public String getNextPerson() {
		return nextPerson;
	}

	public void setNextPerson(String nextPerson) {
		this.nextPerson = nextPerson;
	}

	public String getDealPartId() {
		return dealPartId;
	}

	public void setDealPartId(String dealPartId) {
		this.dealPartId = dealPartId;
	}

	public String getDealPartName() {
		return dealPartName;
	}

	public void setDealPartName(String dealPartName) {
		this.dealPartName = dealPartName;
	}

	public String getDealOptId() {
		return dealOptId;
	}

	public void setDealOptId(String dealOptId) {
		this.dealOptId = dealOptId;
	}

	public String getDealOptName() {
		return dealOptName;
	}

	public void setDealOptName(String dealOptName) {
		this.dealOptName = dealOptName;
	}

	public String getOutFlag() {
		return outFlag;
	}

	public void setOutFlag(String outFlag) {
		this.outFlag = outFlag;
	}

	public String getOutCompname() {
		return outCompname;
	}

	public void setOutCompname(String outCompname) {
		this.outCompname = outCompname;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getOutResult() {
		return outResult;
	}

	public void setOutResult(String outResult) {
		this.outResult = outResult;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getInspectOptId() {
		return inspectOptId;
	}

	public void setInspectOptId(String inspectOptId) {
		this.inspectOptId = inspectOptId;
	}

	public String getInspectOptName() {
		return inspectOptName;
	}

	public void setInspectOptName(String inspectOptName) {
		this.inspectOptName = inspectOptName;
	}
}
