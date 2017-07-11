/**
 * 
 */
package com.dhcc.inspect.domain;

import java.util.ArrayList;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.utils.BaseBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-17    
 * 抽检方案信息类
 */
public class JdPlanCase extends BaseBean {
	
	private static final long serialVersionUID = 1L;
	
	private String caseId;
	private String taskNo;
	private String taskId;
	private String taskName;
	private String isMain;
	private String planName;
	private String compId;
	private String compName;
	private String productStandard;
	private String carryContent;
	private String trainContent;
	private String selectPersonId;
	private String selectPerson;
	private String selectPersonIds;
	private String selectAreaId;
	private String selectArea;
	private String selectEnterpriseId;
	private String selectEnterprise;
	private String selectEndDate;
	private String inspectEndDate;
	private String moreDept;
	private String confirmEndDate;
	private String aptitudeContent;
	private String remark;
	private String isValid;
	private String checkStatus;
	private String checkDesc;
	private String checkOptId;
	private String checkOptName;
	private String checkOptTime;
	private String handleDeptId;
	private String handleDeptName;
	private String companyId;
	private String companyName;
	private String optId;
	private String optName;
	private String optTime;
	private String attactPath;
	private String isHandle;
	private String handleOptId;
	private String handleOptName;
	private String handleOptTime;
	private String isSubmit;
	private String submitOptTime;
	private String baseText;
	private String sampleBase;
	private String productId;
	private String inspestBase;
	private String judgeBase;
	private String mainDeptId;
	private String mainDeptName;
	private String menuId;
	private String menuName;
	private String path2;
	private String moduleId;
	private String moduleName;
	private String menuId2;
	private String selectedPsId;
	private String dealPartId;
	private String dealOptId;
	private String isCheck;
	private String standardCheckTime;
	private String isHave;
	private String isCarry;
	private String flag;
	/**
	 * 留痕的数据
	 */
	private String tranId;
	private String nextDept;
	private String tranTitle,dealView,backType,index;
	
	private String nextDeptId,nextDeptName,nextDealId,nextDealName;
	
	private ArrayList<JdPlanCase> moduleList = new ArrayList<JdPlanCase>();
	
	public JdPlanCase() {

	}

	public JdPlanCase(String menuId, String menuName,String path2) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.path2 = path2;
	}
	
	public JdPlanCase(String moduleId, String moduleName,String menuId2,String s) {
		this.moduleId = moduleId;
		this.moduleName = moduleName;
		this.menuId2 = menuId2;
	}

	public void setModuleFromMenu(String menuId) {
		String sql = "select distinct  item_id,item_name,shape_material " +
				"from jd_product_item " +
				"where standard_id = ? ";
		String gm = "";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { menuId });
		try {
			while (crs.next()) {
				if(!("").equals(crs.getString("shape_material"))&&crs.getString("shape_material")!=null){
					gm = "("+crs.getString("shape_material")+")";
				}
				JdPlanCase module = new JdPlanCase(crs.getString("item_id"), gm+crs
						.getString("item_name"), menuId, "1");
				moduleList.add(module);
				gm = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//检测项目信息
	private String itemId;
	private String standardId;
	private String standardName;
	private String itemName;
	private String secondName;
	private String thirdName;
	private String maxValue;
	private String minValue;
	private String standardValue;
	private String meterUnit;
	private String shapeMaterial;
	private String gradeModel;
	private String path;
	
	private String ck;
	
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

	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getTaskId() {
		return taskId;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
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
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getHandleDeptId() {
		return handleDeptId;
	}
	public void setHandleDeptId(String handleDeptId) {
		this.handleDeptId = handleDeptId;
	}
	public String getHandleDeptName() {
		return handleDeptName;
	}
	public void setHandleDeptName(String handleDeptName) {
		this.handleDeptName = handleDeptName;
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
	public String getIsHandle() {
		return isHandle;
	}
	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
	}
	public String getHandleOptId() {
		return handleOptId;
	}
	public void setHandleOptId(String handleOptId) {
		this.handleOptId = handleOptId;
	}
	public String getHandleOptName() {
		return handleOptName;
	}
	public void setHandleOptName(String handleOptName) {
		this.handleOptName = handleOptName;
	}
	public String getHandleOptTime() {
		return handleOptTime;
	}
	public void setHandleOptTime(String handleOptTime) {
		this.handleOptTime = handleOptTime;
	}
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

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
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
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
	public String getCk() {
		return ck;
	}
	public void setCk(String ck) {
		this.ck = ck;
	}
	public String getAttactPath() {
		return attactPath;
	}
	public void setAttactPath(String attactPath) {
		this.attactPath = attactPath;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getInspestBase() {
		return inspestBase;
	}
	public void setInspestBase(String inspestBase) {
		this.inspestBase = inspestBase;
	}
	public String getJudgeBase() {
		return judgeBase;
	}
	public void setJudgeBase(String judgeBase) {
		this.judgeBase = judgeBase;
	}
	
	public String getMainDeptName() {
		return mainDeptName;
	}

	public void setMainDeptName(String mainDeptName) {
		this.mainDeptName = mainDeptName;
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

	public ArrayList<JdPlanCase> getModuleList() {
		return moduleList;
	}

	public void setModuleList(ArrayList<JdPlanCase> moduleList) {
		this.moduleList = moduleList;
	}

	public String getSelectedPsId() {
		return selectedPsId;
	}

	public void setSelectedPsId(String selectedPsId) {
		this.selectedPsId = selectedPsId;
	}

	public String getMainDeptId() {
		return mainDeptId;
	}

	public void setMainDeptId(String mainDeptId) {
		this.mainDeptId = mainDeptId;
	}

	public String getProductStandard() {
		return productStandard;
	}

	public void setProductStandard(String productStandard) {
		this.productStandard = productStandard;
	}

	public String getCarryContent() {
		return carryContent;
	}

	public void setCarryContent(String carryContent) {
		this.carryContent = carryContent;
	}

	public String getTrainContent() {
		return trainContent;
	}

	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
	}

	public String getSelectPerson() {
		return selectPerson;
	}

	public void setSelectPerson(String selectPerson) {
		this.selectPerson = selectPerson;
	}

	public String getSelectArea() {
		return selectArea;
	}

	public void setSelectArea(String selectArea) {
		this.selectArea = selectArea;
	}

	public String getSelectEnterprise() {
		return selectEnterprise;
	}

	public void setSelectEnterprise(String selectEnterprise) {
		this.selectEnterprise = selectEnterprise;
	}

	public String getSelectEndDate() {
		return selectEndDate;
	}

	public void setSelectEndDate(String selectEndDate) {
		this.selectEndDate = selectEndDate;
	}

	public String getInspectEndDate() {
		return inspectEndDate;
	}

	public void setInspectEndDate(String inspectEndDate) {
		this.inspectEndDate = inspectEndDate;
	}

	public String getMoreDept() {
		return moreDept;
	}

	public void setMoreDept(String moreDept) {
		this.moreDept = moreDept;
	}

	public String getConfirmEndDate() {
		return confirmEndDate;
	}

	public void setConfirmEndDate(String confirmEndDate) {
		this.confirmEndDate = confirmEndDate;
	}

	public String getAptitudeContent() {
		return aptitudeContent;
	}

	public void setAptitudeContent(String aptitudeContent) {
		this.aptitudeContent = aptitudeContent;
	}

	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getSubmitOptTime() {
		return submitOptTime;
	}

	public void setSubmitOptTime(String submitOptTime) {
		this.submitOptTime = submitOptTime;
	}

	public String getSelectPersonId() {
		return selectPersonId;
	}

	public void setSelectPersonId(String selectPersonId) {
		this.selectPersonId = selectPersonId;
	}

	public String getSelectPersonIds() {
		return selectPersonIds;
	}

	public void setSelectPersonIds(String selectPersonIds) {
		this.selectPersonIds = selectPersonIds;
	}

	public String getSelectAreaId() {
		return selectAreaId;
	}

	public void setSelectAreaId(String selectAreaId) {
		this.selectAreaId = selectAreaId;
	}

	public String getSelectEnterpriseId() {
		return selectEnterpriseId;
	}

	public void setSelectEnterpriseId(String selectEnterpriseId) {
		this.selectEnterpriseId = selectEnterpriseId;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	public String getCheckOptId() {
		return checkOptId;
	}

	public void setCheckOptId(String checkOptId) {
		this.checkOptId = checkOptId;
	}

	public String getCheckOptName() {
		return checkOptName;
	}

	public void setCheckOptName(String checkOptName) {
		this.checkOptName = checkOptName;
	}

	public String getCheckOptTime() {
		return checkOptTime;
	}

	public void setCheckOptTime(String checkOptTime) {
		this.checkOptTime = checkOptTime;
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
	
	public String getDealPartId() {
		return dealPartId;
	}

	public void setDealPartId(String dealPartId) {
		this.dealPartId = dealPartId;
	}

	public String getDealOptId() {
		return dealOptId;
	}

	public void setDealOptId(String dealOptId) {
		this.dealOptId = dealOptId;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public String getNextDept() {
		return nextDept;
	}

	public void setNextDept(String nextDept) {
		this.nextDept = nextDept;
	}

	public String getBaseText() {
		return baseText;
	}

	public void setBaseText(String baseText) {
		this.baseText = baseText;
	}

	public String getSampleBase() {
		return sampleBase;
	}

	public void setSampleBase(String sampleBase) {
		this.sampleBase = sampleBase;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getStandardCheckTime() {
		return standardCheckTime;
	}

	public void setStandardCheckTime(String standardCheckTime) {
		this.standardCheckTime = standardCheckTime;
	}

	public String getIsHave() {
		return isHave;
	}

	public void setIsHave(String isHave) {
		this.isHave = isHave;
	}

	public String getIsCarry() {
		return isCarry;
	}

	public void setIsCarry(String isCarry) {
		this.isCarry = isCarry;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getNextDeptId() {
		return nextDeptId;
	}

	public void setNextDeptId(String nextDeptId) {
		this.nextDeptId = nextDeptId;
	}

	public String getNextDeptName() {
		return nextDeptName;
	}

	public void setNextDeptName(String nextDeptName) {
		this.nextDeptName = nextDeptName;
	}

	public String getNextDealId() {
		return nextDealId;
	}

	public void setNextDealId(String nextDealId) {
		this.nextDealId = nextDealId;
	}

	public String getNextDealName() {
		return nextDealName;
	}

	public void setNextDealName(String nextDealName) {
		this.nextDealName = nextDealName;
	}
}
