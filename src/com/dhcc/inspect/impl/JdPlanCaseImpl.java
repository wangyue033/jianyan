package com.dhcc.inspect.impl;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-20    
 * 抽检方案信息Impl
 */
public class JdPlanCaseImpl {
	Page page = new Page();
	/** *******************
	* @return
	* 2017-2-20
	* 获得该单位作为牵头企业的抽检任务信息列表
	*/
	public String getTaskList(JdPlanCase bean) {
		String sql = "select select_area,select_enterprise,handle_dept_id " +
				"from jd_plan_handle where task_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{bean.getTaskId()});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String selectArea = "";
		String selectAreaId = "";
		String selectEnterprise = "";
		String selectEnterpriseId = "";
		String sampleDept = "";
		try {
			while (crs.next()) {
				sampleDept += crs.getString("handle_dept_id") + ",";
				//解析区域
				//JSon转成字符串
				JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_area")==null || 
						crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
				for(Object obj : jArray2){
					Map objs = JSONObject.fromObject(obj);
					selectArea += objs.get("name")+",";
					selectAreaId +=  "{id:'"+objs.get("id")+"',"+"name:'"+objs.get("name")+"'}"+",";
				}
				
				//解析企业
				//JSon转成字符串
				JSONArray jArray3 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
						crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
				for(Object obj : jArray3){
					Map objs = JSONObject.fromObject(obj);
					selectEnterprise += objs.get("name")+",";
					selectEnterpriseId +=  "{id:'"+objs.get("id")+"',"+"name:'"+objs.get("name")+"'}"+",";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (selectArea.length() > 0 ) selectArea = selectArea.substring(0,selectArea.length()-1);
		if (selectAreaId.length() > 0 ) selectAreaId = selectAreaId.substring(0,selectAreaId.length()-1);
		if (selectEnterprise.length() > 0 ) selectEnterprise = selectEnterprise.substring(0,selectEnterprise.length()-1);
		if (selectEnterpriseId.length() > 0 ) selectEnterpriseId = selectEnterpriseId.substring(0,selectEnterpriseId.length()-1);
		if (sampleDept.length() > 0 ) sampleDept = sampleDept.substring(0,sampleDept.length()-1);
		selectAreaId="["+selectAreaId+"]";
		selectEnterpriseId="["+selectEnterpriseId+"]";
		map.put("msg", "true");
		map.put("selectArea", selectArea);
		map.put("selectAreaId", selectAreaId);
		map.put("selectEnterprise", selectEnterprise);
		map.put("selectEnterpriseId", selectEnterpriseId);
		map.put("sampleDept", sampleDept);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	/** *******************
	* @param enterpriseName
	* @return
	* 2017-2-20
	* 方案名称重复验证
	*/
	public boolean checkJdPlanCaseName(String planName) {
		String sql="select count(*) from jd_plan_case where plan_name = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { planName });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 根据task_id获取handle_id
	 */
	public String getHandleId(String taskId) {
		String handleId = "";
		String sql = "select handle_id " +
				"from jd_plan_handle where task_id = ? and is_main = 1";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{taskId});

		try {
			while (crs.next()) {
				handleId = crs.getString("handle_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return handleId;
	}

	/** *******************
	* @param bean
	* 2017-2-20
	* 添加抽检方案信息
	 * @throws SQLException 
	*/
	public void addJdPlanCase(JdPlanCase bean) throws SQLException {
		String[] itemId = {};
		if(bean.getItemId().length()>0){
			itemId = bean.getItemId().split(",");
		}
		
		int len = itemId.length;
		String[] sqls = new String[2 + len];
		String[][] params = new String[2 +len][];
		bean.setCaseId(DBFacade.getInstance().getID());
		sqls[0] = "insert into jd_plan_case(case_id,task_id,plan_name,product_standard,carry_content,train_content," +
				"select_person,select_area,select_enterprise,select_end_date,inspect_end_date,aptitude_content," +
				"inspest_base,judge_base,remark,is_valid,handle_dept_id,handle_dept_name,opt_id,opt_name,opt_time," +
				"attact_path,is_submit,check_status,is_handle) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?,?,now(),?,'0','0','0') ";
		
		params[0] = new String[] { bean.getCaseId(),bean.getTaskId(),bean.getPlanName(),bean.getProductStandard(),
				bean.getCarryContent(),bean.getTrainContent(),bean.getSelectPersonId(),bean.getSelectAreaId(),
				bean.getSelectEnterpriseId(),bean.getSelectEndDate(),bean.getInspectEndDate(),bean.getAptitudeContent(),
				bean.getInspestBase(), bean.getJudgeBase(), bean.getRemark(),bean.getHandleDeptId(),bean.getHandleDeptName(),
				bean.getOptId(),bean.getOptName(),bean.getAttactPath()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽检方案信息添加",
				"抽检任务ID：" + bean.getTaskId() + ";方案ID："
						+ bean.getCaseId(), "1" };
		
		for (int i = 2; i < len + 2; i++) {
			String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] {itemId[i-2] });
			if(crs.next()){
				sqls[i] = "insert into jd_plan_item (case_item_id ,case_id,task_id,item_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name,third_name,"
						+ "max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic,data_from,charge_discount," +
						"main_comp,is_valid,opt_id,opt_name,opt_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,1,?,?,now())";
				params[i] = new String[] {DBFacade.getInstance().getID(),bean.getCaseId(),bean.getTaskId(),crs.getString("item_id"),crs.getString("standard_id"),crs.getString("standard_no"),crs.getString("standard_name"),
						crs.getString("type_id"),crs.getString("type_name"),crs.getString("item_name"),crs.getString("second_name"),crs.getString("third_name"),crs.getString("max_value"),crs.getString("min_value"),
						crs.getString("standard_value"),crs.getString("meter_unit"),crs.getString("shape_material"),crs.getString("grade_model"),crs.getString("sample_no"),crs.getString("special_judge"),crs.getString("inspect_model"),
						crs.getString("charge_standard"),crs.getString("charge_basic"),crs.getString("charge_discount"),crs.getString("main_comp"),bean.getOptId(),bean.getOptName()};
			}
		}
		
		DBFacade.getInstance().execute(sqls, params);
	}

	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-2-20
	* 获取抽检方案信息列表
	 * @throws SQLException 
	 * @throws ParseException 
	*/
	public String getJdPlanCaseList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String handleDeptId,String optId,String companyId) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select b.task_no,b.task_id,b.task_name,b.comp_id,b.comp_name,b.base_text,b.sample_base,b.product_id,b.main_dept_id,b.tran_id,b.trans_dept_id,b.trans_opt_id,a.case_id,a.check_status,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.is_submit,a.is_handle,a.plan_name "
				+ "from jd_plan_task b left join jd_plan_case a on a.task_id = b.task_id where b.sign_status='1' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("a.check_status,a.is_handle");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlanCase> list = new ArrayList<JdPlanCase>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlanCase bean = new JdPlanCase();
			bean.setMainDeptId(crs.getString("main_dept_id"));
			String sql1 = "select handle_dept_id,inspet_dept_id from jd_plan_handle where task_id=?";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] { crs.getString("task_id")});
			while(crs1.next()){
				if(crs.getString("main_dept_id").substring(0,10).equals(handleDeptId)||
						crs1.getString("handle_dept_id").substring(0,10).equals(handleDeptId)||
						crs1.getString("inspet_dept_id").substring(0,10).equals(handleDeptId)){
					
					/*if(((crs.getString("trans_opt_id")!=null||!crs.getString("trans_opt_id").equals(""))&&crs.getString("trans_opt_id").equals(optId))||((crs.getString("trans_opt_id")==null||crs.getString("trans_opt_id").equals(""))&&crs.getString("trans_dept_id").equals(companyId))){
						bean.setIsMain(1+"");
					}*/
					bean.setIsMain(1+"");
					
				}else{
					bean.setIsMain(0+"");
				}
				bean.setHandleDeptId(crs1.getString("handle_dept_id"));
			}
			bean.setCaseId(crs.getString("case_id"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setBaseText(crs.getString("base_text"));
			bean.setSampleBase(crs.getString("sample_base"));
			bean.setProductId(crs.getString("product_id"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setIsSubmit(crs.getString("is_submit"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setTranId(crs.getString("tran_id"));
			String sql2 = "select tran_title,deal_view,back_type from dh_tranlist where tran_id = (select deal_view from dh_tranlist where pre_tran_id=? and work_status='0')";
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[]{bean.getTranId()});
			if(crs2.next()){
				bean.setTranTitle(crs2.getString("tran_title"));
				bean.setDealView(crs2.getString("deal_view"));
				bean.setBackType(crs2.getString("back_type"));
			}
			bean.setCompId(crs.getString("comp_id"));
			bean.setCompName(crs.getString("comp_name"));
			
			list.add(bean);
		}
		if(list.isEmpty()){
			map.put("totalPage", "" + 0);
			map.put("curPage", "" + 0);
			map.put("totalRecord", "" + 0);
		}else{
			map.put("totalPage", "" + pageData.getPageAmount());
			map.put("curPage", "" + pageData.getPageNo());
			map.put("totalRecord", "" + pageData.getItemAmount());
		}
		map.put("msg", "true");
		map.put("record", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	/**
	 * 根据case_id获取handle_id
	 */
	public String getHandleId2(String caseId) {
		String handleId = "";
		String sql = "select handle_id " +
				"from jd_plan_case where case_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{caseId});

		try {
			while (crs.next()) {
				handleId = crs.getString("handle_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return handleId;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-20
	* 删除抽检方案信息
	*/
	public Map<String, Object> delJdPlanCase(JdPlanCase bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];
		
		sqls[0] = "delete from jd_plan_item where case_id like ?";
		params[0] = new String[] { bean.getCaseId() + "%" };

		sqls[1] = "delete from jd_plan_case where case_id like ?";
		params[1] = new String[] { bean.getCaseId() + "%" };

		sqls[2] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[2] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检方案信息删除",
				"实施方案ID：" + bean.getCaseId(), "1" };
		
		/*sqls[3] = "update jd_plan_handle set sign_status=1 where handle_id=?";
		params[3] = new String[] { getHandleId2(bean.getCaseId())};*/
		
		DBFacade.getInstance().execute(sqls, params);
		
		// 删除服务器上的方案附件信息
		String str = bean.getAttactPath();
		String[] temp = str.split(";;");
		for (int i = 0; i < temp.length; i++) {
			if (i % 2 == 1) {
				if (new File(bean.getPath() + temp[i]).exists()) {
					new File(bean.getPath() + temp[i]).delete();
				}
			}
		}
		// 删除服务器上的检验依据附件信息
		String str1 = bean.getInspestBase();
		String[] temp1 = str1.split(";;");
		for (int i = 0; i < temp1.length; i++) {
			if (i % 2 == 1) {
				if (new File(bean.getPath() + temp1[i]).exists()) {
					new File(bean.getPath() + temp1[i]).delete();
				}
			}
		}
		// 删除服务器上的判定依据附件信息
		String str2 = bean.getJudgeBase();
		String[] temp2 = str2.split(";;");
		for (int i = 0; i < temp2.length; i++) {
			if (i % 2 == 1) {
				if (new File(bean.getPath() + temp2[i]).exists()) {
					new File(bean.getPath() + temp2[i]).delete();
				}
			}
		}
		
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param caseId
	* @return
	* 2017-2-20
	* *
	 * @throws ParseException 
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getJdPlanCaseById(String caseId) throws SQLException, ParseException {
		String sql = "select a.case_id,a.task_id,a.plan_name,a.is_check,"+TimeUtil.getTimeShow("a.standard_check_time")+" standard_check_time,a.is_have,a.is_carry,a.product_standard," +
				"a.carry_content,a.train_content,a.select_person,a.select_area,a.select_enterprise,a.select_end_date,a.inspect_end_date,more_dept," +
				"a.aptitude_content,a.inspest_base,a.judge_base,a.remark,a.is_valid,a.handle_dept_id,a.handle_dept_name," +
				"a.opt_id,a.opt_name,a.opt_time,a.attact_path,a.is_submit,a.submit_opt_time,a.check_status,a.check_desc," +
				"a.check_opt_id,a.check_opt_name,a.check_opt_time,a.is_handle,a.handle_opt_time,b.task_name " +
				"from jd_plan_case a left join jd_plan_task b on a.task_id = b.task_id where a.case_id=?";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { caseId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdPlanCase jdPlanCase = new JdPlanCase();
			jdPlanCase.setCaseId(crs.getString("case_id"));
			jdPlanCase.setTaskId(crs.getString("task_id"));
			jdPlanCase.setPlanName(crs.getString("plan_name"));
			jdPlanCase.setIsCheck(crs.getString("is_check"));
			jdPlanCase.setStandardCheckTime(crs.getString("standard_check_time"));
			jdPlanCase.setIsHave(crs.getString("is_have"));
			jdPlanCase.setIsCarry(crs.getString("is_carry"));
			jdPlanCase.setStandardId(crs.getString("product_standard"));
			//解析标准
			//JSon转成字符串
			String standard = "";
			JSONArray jArray0 = JSONArray.fromObject((crs.getString("product_standard")==null || 
					crs.getString("product_standard").trim().equals(""))?"[]":crs.getString("product_standard"));
			for(Object obj : jArray0){
				Map objs = JSONObject.fromObject(obj);
				standard+= objs.get("name")+",";
			}
			standard = standard.length()==0?standard:standard.substring(0, standard.length()-1);
			jdPlanCase.setProductStandard(standard);
			
			jdPlanCase.setCarryContent(crs.getString("carry_content"));
			jdPlanCase.setTrainContent(crs.getString("train_content"));
			jdPlanCase.setSelectPersonId(crs.getString("select_person"));
			//解析抽样人员
			//JSon转成字符串
			String person = "";
			String personId = "";
			JSONArray jArray1 = JSONArray.fromObject((crs.getString("select_person")==null || 
					crs.getString("select_person").trim().equals(""))?"[]":crs.getString("select_person"));
			for(Object obj : jArray1){
				Map objs = JSONObject.fromObject(obj);
				person+= objs.get("name")+",";
				personId+=objs.get("id")+",";
			}
			person = person.length()==0?person:person.substring(0, person.length()-1);
			personId = personId.length()==0?personId:personId.substring(0, personId.length()-1);
			jdPlanCase.setSelectPerson(person);
			jdPlanCase.setSelectPersonIds(personId);
			
			jdPlanCase.setSelectAreaId(crs.getString("select_area"));
			//解析抽查区域
			//JSon转成字符串
			String area = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				area+= objs.get("name")+",";
			}
			area = area.length()==0?area:area.substring(0, area.length()-1);
			jdPlanCase.setSelectArea(area);
			
			jdPlanCase.setSelectEnterpriseId(crs.getString("select_enterprise"));
			//解析抽查企业
			//JSon转成字符串
			String comp = "";
			JSONArray jArray3 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray3){
				Map objs = JSONObject.fromObject(obj);
				comp+= objs.get("name")+",";
			}
			
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			jdPlanCase.setSelectEnterprise(comp);
			
			jdPlanCase.setSelectEndDate(crs.getString("select_end_date"));
			jdPlanCase.setInspectEndDate(crs.getString("inspect_end_date"));
			jdPlanCase.setMoreDept(crs.getString("more_dept"));
			jdPlanCase.setAptitudeContent(crs.getString("aptitude_content"));
			jdPlanCase.setInspestBase(crs.getString("inspest_base"));
			jdPlanCase.setJudgeBase(crs.getString("judge_base"));
			jdPlanCase.setRemark(crs.getString("remark"));
			jdPlanCase.setIsValid(crs.getString("is_valid"));
			jdPlanCase.setCheckStatus(crs.getString("check_status"));
			jdPlanCase.setHandleDeptId(crs.getString("handle_dept_id"));
			jdPlanCase.setHandleDeptName(crs.getString("handle_dept_name"));
			jdPlanCase.setOptId(crs.getString("opt_id"));
			jdPlanCase.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter.format(formatter
					.parse(optTime));
			jdPlanCase.setOptTime(time);
			jdPlanCase.setAttactPath(crs.getString("attact_path"));
			jdPlanCase.setIsSubmit(crs.getString("is_submit"));
			jdPlanCase.setSubmitOptTime(crs.getString("submit_opt_time"));
			jdPlanCase.setCheckStatus(crs.getString("check_status"));
			jdPlanCase.setCheckDesc(crs.getString("check_desc"));
			jdPlanCase.setCheckOptId(crs.getString("check_opt_id"));
			jdPlanCase.setCheckOptName(crs.getString("check_opt_name"));
			String optTime1 = crs.getString("check_opt_time");
			if(optTime1 == null){
				jdPlanCase.setCheckOptTime(crs.getString("check_opt_time"));
			}else{
				String time1 = formatter.format(formatter
						.parse(optTime1));
				jdPlanCase.setCheckOptTime(time1);
			}
			jdPlanCase.setIsHandle(crs.getString("is_handle"));

			String optTime2 = crs.getString("handle_opt_time");
			if(optTime2 == null){
				jdPlanCase.setHandleOptTime(crs.getString("handle_opt_time"));
			}else{
				String time2 = formatter.format(formatter
						.parse(optTime2));
				jdPlanCase.setHandleOptTime(time2);
			}
			jdPlanCase.setTaskName(crs.getString("task_name"));
			map.put("record", jdPlanCase);
		} else {
			map.put("msg", "noresult");
		}
		
		String sql2 = "select b.standard_name,c.item_id,c.standard_id,c.item_name,c.second_name,c.third_name,c.max_value,c.min_value,c.standard_value,"
				+ "c.meter_unit,c.shape_material,c.grade_model from jd_product_standard b,jd_plan_item c where b.standard_id = c.standard_id and c.case_id=?";
		
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
				new String[] {caseId });
		List<JdPlanCase> list = new ArrayList<JdPlanCase>();
		try{
			while(crs2.next()){
				JdPlanCase bean2 = new JdPlanCase();
				bean2.setItemId(crs2.getString("item_id"));
				bean2.setStandardId(crs2.getString("standard_id"));
				bean2.setStandardName(crs2.getString("standard_name"));
				bean2.setItemName(crs2.getString("item_name"));
				bean2.setSecondName(crs2.getString("second_name"));
				bean2.setThirdName(crs2.getString("third_name"));
				bean2.setMaxValue(crs2.getString("max_value"));
				bean2.setMinValue(crs2.getString("min_value"));
				bean2.setStandardValue(crs2.getString("standard_value"));
				bean2.setMeterUnit(crs2.getString("meter_unit"));
				bean2.setShapeMaterial(crs2.getString("shape_material"));
				bean2.setGradeModel(crs2.getString("grade_model"));
				list.add(bean2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("items", list);
		return map;
	}

	/** *******************
	* @param planName
	* @param caseId
	* @return
	* 2017-2-20
	* 修改时验证方案名称重复
	*/
	public boolean checkEditJdPlanCaseName(String planName, String caseId) {
		String sql="select count(*) from jd_plan_case where plan_name = ? and case_id != ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { planName,caseId});
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-20
	* 修改抽检方案信息
	* @throws SQLException 
	*/
	public Map<String, Object> editJdPlanCase(JdPlanCase bean) throws SQLException {
		String[] itemId = {};
		if(bean.getItemId().length()>0){
			itemId = bean.getItemId().split(",");
		}
		
		int len = itemId.length;
		String[] sqls = new String[3 + len];
		String[][] params = new String[3 + len][];
		sqls[0] = "update jd_plan_case set plan_name=?,product_standard=?,carry_content=?,train_content=?," +
				"select_person=?,select_end_date=?,inspect_end_date=?,aptitude_content=?,inspest_base=?," +
				"judge_base=?,remark=?,opt_id=?,opt_name=?,opt_time=now(),attact_path=? where case_id=?";
		
		params[0] = new String[] { bean.getPlanName(), bean.getProductStandard(),bean.getCarryContent(),bean.getTrainContent(),
				bean.getSelectPersonId(),bean.getSelectEndDate(),bean.getInspectEndDate(),bean.getAptitudeContent(),bean.getInspestBase(),
				bean.getJudgeBase(),bean.getRemark(),bean.getOptId(),bean.getOptName(),bean.getAttactPath(),bean.getCaseId()};
				
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检方案信息修改",
				"实施方案ID：" + bean.getCaseId(), "1" };
		
		sqls[2] = "delete from jd_plan_item where case_id like ?";
		params[2] = new String[] { bean.getCaseId() + "%" };
		
		for (int i = 3; i < len + 3; i++) {
			String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] {itemId[i-3] });
			if(crs.next()){
				sqls[i] = "insert into jd_plan_item (case_item_id ,case_id,task_id,item_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name,third_name,"
						+ "max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic,data_from,charge_discount," +
						"main_comp,is_valid,opt_id,opt_name,opt_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,1,?,?,now())";
				params[i] = new String[] {DBFacade.getInstance().getID(),bean.getCaseId(),bean.getTaskId(),crs.getString("item_id"),crs.getString("standard_id"),crs.getString("standard_no"),crs.getString("standard_name"),
						crs.getString("type_id"),crs.getString("type_name"),crs.getString("item_name"),crs.getString("second_name"),crs.getString("third_name"),crs.getString("max_value"),crs.getString("min_value"),
						crs.getString("standard_value"),crs.getString("meter_unit"),crs.getString("shape_material"),crs.getString("grade_model"),crs.getString("sample_no"),crs.getString("special_judge"),crs.getString("inspect_model"),
						crs.getString("charge_standard"),crs.getString("charge_basic"),crs.getString("charge_discount"),crs.getString("main_comp"),bean.getOptId(),bean.getOptName()};
			}
			
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-20
	* 提交抽检方案信息待审核
	*/
	public Map<String, Object> submitJdPlanCase(JdPlanCase bean) {
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		/*更新jd_plan_case表*/
		sqls[0] = "update jd_plan_case set check_status='1',is_submit='1',submit_opt_time=now() where case_id=?";
		params[0] = new String[] {bean.getCaseId()};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检方案信息提交审核",
				"实施方案ID：" + bean.getCaseId(), "1" };
		
		/*根据上一步骤tranId获取当前步骤tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*更新业务主表jd_plan_task*/
		sqls[2] = "update jd_plan_task set current_node_id=?,current_status='4',tran_id=? where task_id=?";
		params[2] = new String[] {FlowConstant.CC_NODE_ARR_ID[3],tranId,bean.getTaskId()};
		/**
		 * 抽检方案上报修改留痕表记录
		 */
		bean.setNextDept("[{'id':'"+bean.getCompId()+"',"+"'name':'"+bean.getCompName()+"'}]");
		
		sqls[3] = "update dh_tranlist set detail_id=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',"
				+ "next_dept=? where pre_tran_id=? and work_status='0'";
		params[3] = new String[] { bean.getCaseId(),bean.getOptId(),bean.getOptName(),bean.getNextDept(),bean.getTranId()};
		/**
		 * 给下一步骤插入数据待下一步骤更改
		 */
		sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_plan_task','抽检方案审核',?,?,?,?,now(),?,?,'0','0','0','0','5',?,'1','1')";
		params[4] = new String[] { DBFacade.getInstance().getID(),bean.getTaskId(),FlowConstant.CC_FLOW_ID,
				FlowConstant.CC_NODE_ARR_ID[4],FlowConstant.CC_STEP_ARR_ID[4],bean.getCaseId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getCompId(),bean.getCompName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-18
	* 获取当前步骤的tranId
	 * @throws SQLException 
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("tran_id");
		}
		
		return preTranId;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-21
	* 抽检方案下发
	*/
	public Map<String, Object> issueJdPlanCase(JdPlanCase bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_plan_case set is_handle='1',handle_opt_time=now(),sample_dept_id=?,sample_dept_name=?,sample_handle_id=?,sample_handle_name=? where case_id=?";
		params[0] = new String[] {bean.getNextDeptId(),bean.getNextDeptName(),bean.getNextDealId(),bean.getNextDealName(),bean.getCaseId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检方案下发",
				"实施方案ID：" + bean.getCaseId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param productName
	* @return
	* 2017-3-4
	* *******************
	*/
	public Map<String, Object> getJdItemById(String standardId) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdPlanCase> list = new ArrayList<JdPlanCase>();
		String[] arrs = standardId.split(",");
		for(int i=0;i<arrs.length;i++){
			String sql2 = "select c.standard_name,b.item_id,b.standard_id,b.item_name,b.second_name,b.third_name,b.max_value,b.min_value,b.standard_value,"
					+ " b.meter_unit,b.shape_material,b.grade_model from jd_product_item b,jd_product_standard c "
					+ " where b.standard_id = c.standard_id and c.is_valid = 1 and b.item_id = ? ";
			
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
					new String[] {arrs[i] });
			try{
				while(crs2.next()){
					JdPlanCase bean2 = new JdPlanCase();
					bean2.setItemId(crs2.getString("item_id"));
					bean2.setStandardId(crs2.getString("standard_id"));
					bean2.setStandardName(crs2.getString("standard_name"));
					bean2.setItemName(crs2.getString("item_name"));
					bean2.setSecondName(crs2.getString("second_name"));
					bean2.setThirdName(crs2.getString("third_name"));
					bean2.setMaxValue(crs2.getString("max_value"));
					bean2.setMinValue(crs2.getString("min_value"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setMeterUnit(crs2.getString("meter_unit"));
					bean2.setShapeMaterial(crs2.getString("shape_material"));
					bean2.setGradeModel(crs2.getString("grade_model"));
					list.add(bean2);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		map.put("msg", "true");
		map.put("items", list);
		return map;
	}

	/** *******************
	* @param taskId
	* @return
	* 2017-3-4
	* 方案信息修改时获取任务对应的检测项目信息
	 * @throws SQLException 
	*/
	public Map<String, Object> editJdItemById(String taskId,String caseId) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			String sql2 = "select b.standard_name,c.item_id,c.standard_id,c.item_name,c.second_name,c.third_name,c.max_value,c.min_value,c.standard_value,"
					+ "c.meter_unit,c.shape_material,c.grade_model from jd_product_standard b,jd_plan_item c "
					+ " where c.case_id = ? and b.standard_id = c.standard_id and c.is_valid = 1";
			
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
					new String[] {caseId });
			List<JdPlanCase> list = new ArrayList<JdPlanCase>();
			try{
				while(crs2.next()){
					JdPlanCase bean2 = new JdPlanCase();
					bean2.setItemId(crs2.getString("item_id"));
					
					bean2.setStandardId(crs2.getString("standard_id"));
					bean2.setStandardName(crs2.getString("standard_name"));
					bean2.setItemName(crs2.getString("item_name"));
					bean2.setSecondName(crs2.getString("second_name"));
					bean2.setThirdName(crs2.getString("third_name"));
					bean2.setMaxValue(crs2.getString("max_value"));
					bean2.setMinValue(crs2.getString("min_value"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setMeterUnit(crs2.getString("meter_unit"));
					bean2.setShapeMaterial(crs2.getString("shape_material"));
					bean2.setGradeModel(crs2.getString("grade_model"));
					list.add(bean2);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			map.put("msg", "true");
			map.put("items", list);
		return map;
	}

	/** *******************
	* @param productName
	* @return
	* 2017-3-13
	* 获取该产品分类的标准
	*/
	public String[] getByStandardId(String productName) {
		String[] standardId = null;
		String sql = "select distinct  a.standard_name from jd_product_standard a,jd_product_type b " +
				"where a.type_id = b.type_id and b.type_name = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { productName });
		standardId = new String[crs.size()];
		try {
			int i = 0;
			while (crs.next()) {
				standardId[i] = crs.getString("standard_name");
				i++;
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return standardId;
	}

	/** *******************
	* @param productName
	* @return
	* 2017-3-13
	* 获取该产品分类的所有检测项目
	*/
	public List<JdPlanCase> getItemAllList(String productId) {
		List<JdPlanCase> menuList = new ArrayList<JdPlanCase>();
		String sql = "select distinct  standard_id,standard_name from jd_product_standard where  type_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { productId });
		try {
			while (crs.next()) {
				JdPlanCase menu = new JdPlanCase(crs.getString("standard_id"), crs
						.getString("standard_name"),crs
						.getString("standard_name"));
				menu.setModuleFromMenu(crs.getString("standard_id"));
				menuList.add(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}

	/** *******************
	* @param caseId
	* @return
	* 2017-3-14
	* 查询已添加的标准项目id
	*/
	public String[] getPsByItemId(String caseId) {
		String[] selectedId = null;
		String sql = "select distinct item_id from jd_plan_item where case_id =? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { caseId });
		selectedId = new String[crs.size()];
		try {
			int i = 0;
			while (crs.next()) {
				selectedId[i] = crs.getString("item_id");
				i++;
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return selectedId;
	}

	/** *******************
	* @param bean
	* 2017-6-9
	* 方案保存第一步
	*/
	public void addFirst(JdPlanCase bean) {
		String[] itemId = {};
		if(bean.getItemId().length()>0){
			itemId = bean.getItemId().split(",");
		}
		
		int len = itemId.length;
		String[] sqls = new String[3 + len];
		String[][] params = new String[3 +len][];
		if(("0").equals(bean.getFlag())){
			sqls[0] = "insert into jd_plan_case(case_id,task_id,plan_name,is_check,standard_check_time,is_have,is_carry,product_standard,carry_content,train_content," +
					"select_person,select_area,select_enterprise,select_end_date,inspect_end_date,more_dept,aptitude_content," +
					"inspest_base,judge_base,remark,is_valid,handle_dept_id,handle_dept_name,opt_id,opt_name,opt_time," +
					"attact_path,is_submit,check_status,is_handle) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?,?,now(),?,'0','0','0') ";
			
			params[0] = new String[] { bean.getCaseId(),bean.getTaskId(),bean.getPlanName(),bean.getIsCheck(),bean.getStandardCheckTime(),bean.getIsHave(),bean.getIsCarry(),
					bean.getProductStandard(),bean.getCarryContent(),bean.getTrainContent(),bean.getSelectPersonId(),bean.getSelectAreaId(),bean.getSelectEnterpriseId(),
					bean.getSelectEndDate(),bean.getInspectEndDate(),bean.getMoreDept(),bean.getAptitudeContent(),bean.getInspestBase(), bean.getJudgeBase(),bean.getRemark(),
					bean.getHandleDeptId(),bean.getHandleDeptName(),bean.getOptId(),bean.getOptName(),bean.getAttactPath()};
					
		}else{
			sqls[0] = "update jd_plan_case set plan_name=?,is_check=?,standard_check_time=?,is_have=?,is_carry=?,product_standard=?,carry_content=?,train_content=?," +
					"select_person=?,select_end_date=?,inspect_end_date=?,more_dept=?,aptitude_content=?,inspest_base=?," +
					"judge_base=?,remark=?,opt_id=?,opt_name=?,opt_time=now(),attact_path=? where case_id=?";
			
			params[0] = new String[] { bean.getPlanName(),bean.getIsCheck(),bean.getStandardCheckTime(),bean.getIsHave(),bean.getIsCarry(),bean.getProductStandard(),
					bean.getCarryContent(),bean.getTrainContent(),bean.getSelectPersonId(),bean.getSelectEndDate(),bean.getInspectEndDate(),bean.getMoreDept(),bean.getAptitudeContent(),
					bean.getInspestBase(),bean.getJudgeBase(),bean.getRemark(),bean.getOptId(),bean.getOptName(),bean.getAttactPath(),bean.getCaseId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) values(?,?,?,?,now(),?,?)";
				
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"抽检方案信息添加","抽检任务ID：" + bean.getTaskId() + ";方案ID："+ bean.getCaseId(), "1" };
		
		sqls[2] = "delete from jd_plan_item where case_id like ?";
		params[2] = new String[] { bean.getCaseId() + "%" };
		
		for (int i = 3; i < len + 3; i++) {
			String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] {itemId[i-3] });
			try {
				if(crs.next()){
					sqls[i] = "insert into jd_plan_item (case_item_id ,case_id,task_id,item_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name,third_name,"
							+ "max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic,data_from,charge_discount," +
							"main_comp,is_valid,opt_id,opt_name,opt_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,1,?,?,now())";
					params[i] = new String[] {DBFacade.getInstance().getID(),bean.getCaseId(),bean.getTaskId(),crs.getString("item_id"),crs.getString("standard_id"),crs.getString("standard_no"),crs.getString("standard_name"),
							crs.getString("type_id"),crs.getString("type_name"),crs.getString("item_name"),crs.getString("second_name"),crs.getString("third_name"),crs.getString("max_value"),crs.getString("min_value"),
							crs.getString("standard_value"),crs.getString("meter_unit"),crs.getString("shape_material"),crs.getString("grade_model"),crs.getString("sample_no"),crs.getString("special_judge"),crs.getString("inspect_model"),
							crs.getString("charge_standard"),crs.getString("charge_basic"),crs.getString("charge_discount"),crs.getString("main_comp"),bean.getOptId(),bean.getOptName()};
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBFacade.getInstance().execute(sqls, params);
		
	}

}
