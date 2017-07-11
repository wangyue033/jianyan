package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.inspect.domain.JdPlanCaseCheck;
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
 * 抽检方案审核实现类
 */
public class JdPlanCaseCheckImpl {
	Page page = new Page();
	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-2-20
	* 获取已提交审核的抽检方案信息列表
	 * @throws SQLException 
	 * @throws ParseException 
	*/
	public String getJdPlanCaseCheckList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String companyId,String handleDeptId) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select b.task_no,b.task_id,b.task_name,b.main_dept_id,b.tran_id,b.comp_id,b.comp_name,a.case_id,a.check_status,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.is_handle,a.plan_name "
				+ "from jd_plan_task b left join jd_plan_case a on a.task_id = b.task_id where a.is_submit='1' and a.check_status !='0' ";

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
			page.setSidx("a.case_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlanCaseCheck> list = new ArrayList<JdPlanCaseCheck>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlanCaseCheck bean = new JdPlanCaseCheck();
			String sql1 = "select handle_dept_id,inspet_dept_id from jd_plan_handle where task_id=?";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] { crs.getString("task_id")});
			while(crs1.next()){
				if(crs.getString("comp_id").equals(handleDeptId)||crs.getString("main_dept_id").substring(0,10).equals(handleDeptId)||
						crs1.getString("handle_dept_id").substring(0,10).equals(handleDeptId)||
						crs1.getString("inspet_dept_id").substring(0,10).equals(handleDeptId)){
					if(crs.getString("comp_id").equals(companyId)){
						bean.setIsMain("1");/*以此控制审核人的权限*/
					}
					
				}
			}
			bean.setCaseId(crs.getString("case_id"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setTaskName(crs.getString("task_Name"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setOptTime(crs.getString("opt_time"));
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
	/** *******************
	* @param caseId
	* @return
	* 2017-2-20
	* *
	 * @throws ParseException 
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getJdPlanCaseCheckById(String caseId) throws SQLException, ParseException {
		String sql = "select a.case_id,a.task_id,a.plan_name,a.is_check,a.standard_check_time,a.is_have,a.is_carry,a.product_standard,a.carry_content,a.train_content," +
				"a.select_person,a.select_area,a.select_enterprise,a.select_end_date,a.inspect_end_date,a.aptitude_content," +
				"a.inspest_base,a.judge_base,a.remark,a.is_valid,a.handle_dept_id,a.handle_dept_name," +
				"a.opt_id,a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.attact_path,a.is_submit,"+TimeUtil.getTimeShow("a.submit_opt_time")+" submit_opt_time,a.check_status,a.check_desc," +
				"a.check_opt_id,a.check_opt_name,"+TimeUtil.getTimeShow("a.check_opt_time")+" check_opt_time,a.is_handle,a.handle_opt_time,b.task_name " +
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
			JSONArray jArray1 = JSONArray.fromObject((crs.getString("select_person")==null || 
					crs.getString("select_person").trim().equals(""))?"[]":crs.getString("select_person"));
			for(Object obj : jArray1){
				Map objs = JSONObject.fromObject(obj);
				person+= objs.get("name")+",";
			}
			person = person.length()==0?person:person.substring(0, person.length()-1);
			jdPlanCase.setSelectPerson(person);
			
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
			jdPlanCase.setOptTime(crs.getString("opt_time"));
			jdPlanCase.setAttactPath(crs.getString("attact_path"));
			jdPlanCase.setIsSubmit(crs.getString("is_submit"));
			jdPlanCase.setSubmitOptTime(crs.getString("submit_opt_time"));
			jdPlanCase.setCheckStatus(crs.getString("check_status"));
			jdPlanCase.setCheckDesc(crs.getString("check_desc"));
			jdPlanCase.setCheckOptId(crs.getString("check_opt_id"));
			jdPlanCase.setCheckOptName(crs.getString("check_opt_name"));
			jdPlanCase.setCheckOptTime(crs.getString("check_opt_time"));
			jdPlanCase.setIsHandle(crs.getString("is_handle"));
			jdPlanCase.setHandleOptTime(crs.getString("handle_opt_time"));
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
	* @param bean
	* @return
	* 2017-2-20
	* 抽检方案审核
	 * @throws SQLException 
	*/
	public Map<String, Object> addJdPlanCaseCheck(JdPlanCaseCheck bean) throws SQLException {
		String[] sqls;
		String[][] params;
		if("1".equals(bean.getStepIndex())){
			bean.setCheckStatus("2");
			bean.setCheckOptTime(null);
			sqls = new String[6];;
			params = new String[6][];
		}else{
			bean.setCheckStatus("3");
			bean.setCheckOptId(bean.getOptId());
			bean.setCheckOptName(bean.getOptName());
			//date对象代表当前的系统时间(毫秒)
			  Date date = new Date();
			  //format对象是用来以指定的时间格式格式化时间的
			  SimpleDateFormat from = new SimpleDateFormat(
			  "yyyy-MM-dd HH:mm:ss"); //这里的格式可以自己设置
			  //format()方法是用来格式化时间的方法
			  String times = from.format(date);
			bean.setCheckOptTime(times);
			sqls = new String[4];;
			params = new String[4][];
		}
		
		/*获取当前步骤的tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新jd_plan_case方案表*/
		sqls[0] = "update jd_plan_case set check_status=?,check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=? where case_id=?";
		params[0] = new String[] {bean.getCheckStatus(),bean.getReason(),bean.getCheckOptId(),bean.getCheckOptName(),bean.getCheckOptTime(),bean.getCaseId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检方案信息审核",
				"实施方案ID：" + bean.getCaseId(), "1" };
		
		
		if("1".equals(bean.getStepIndex())){
			/*审核不通过，更新当前步骤记录留痕，插入一条返回的新记录*/
			sqls[3] = "update dh_tranlist set back_type='3',deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='1',deal_view=? "
					+ " where pre_tran_id=?";
			params[3] = new String[] { bean.getOptId(),bean.getOptName(),bean.getReason(),bean.getTranId()};
			
			sqls[4] = "update dh_tranlist set work_status='1' where tran_id=?";
			params[4] = new String[] {bean.getTranId()};
			
			/*先查询上一步骤的信息，再重新插入*/
			String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,pre_tran_id from dh_tranlist where tran_id=? and work_status='0'";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
			if (crs.next()) {
				sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,'1','1',?)";
				params[5] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
						crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
						crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
						crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("pre_tran_id"),tranId};
				/*更新jd_plan_task抽检任务表*/
				sqls[2] = "update jd_plan_task set current_node_id=?,current_status='5',tran_id=? where task_id=?";
				params[2] = new String[] {FlowConstant.CC_NODE_ARR_ID[4],crs.getString("pre_tran_id"),bean.getTaskId()};
			}
			
		}else{
			/*更新jd_plan_task抽检任务表*/
			sqls[2] = "update jd_plan_task set current_node_id=?,current_status='5',tran_id=? where task_id=?";
			params[2] = new String[] {FlowConstant.CC_NODE_ARR_ID[4],tranId,bean.getTaskId()};
			
			/*审核通过，直接更新当前步骤记录留痕*/
			sqls[3] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',deal_view=? "
					+ " where pre_tran_id=? and work_status='0'";
			params[3] = new String[] { bean.getOptId(),bean.getOptName(),bean.getReason(),bean.getTranId()};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	/** *******************
	* @param tranId
	* @return
	* 2017-5-18
	* *
	 * @throws SQLException 根据上一流转Id获取当前流转Id
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("tran_id");
		}
		System.out.println(preTranId+"当前步骤Id");
		return preTranId;
	}
	/** *******************
	* @param bean
	* @return
	* 2017-5-18
	* 获取抽检任务、抽样单据、检验任务、检验报告的完成以及超时情况
	 * @throws SQLException 
	*/
	public String getTotal(JdPlanCaseCheck bean) throws SQLException {
		/*抽样任务*/
		String sql = "SELECT*FROM (SELECT flow_id,count(flow_id) total from dh_tranlist where flow_id='02' and tran_title='抽样任务下发' GROUP BY flow_id ) a,"
				+ "(SELECT SUM(CASE  WHEN deal_status='1' THEN '1' ELSE '0' END ) end,flow_id from dh_tranlist where flow_id='02' and tran_title='抽样任务签收' GROUP BY flow_id) b,"
				+ "(SELECT SUM(CASE  WHEN over_status='1' THEN '1' ELSE '0' END ) over,flow_id from dh_tranlist where flow_id='02' and tran_title='抽样任务签收' GROUP BY flow_id) c "
				+ " where a.flow_id=b.flow_id and a.flow_id=c.flow_id ";
		
		List<JdPlanCaseCheck> list = new ArrayList<JdPlanCaseCheck>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {});

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		while (crs.next()) {
			JdPlanCaseCheck bean1 = new JdPlanCaseCheck();
			bean1.setDevelopNumber(crs.getString("total"));
			bean1.setSignNumber(crs.getString("end").substring(0,crs.getString("end").length() - 2));
			bean1.setOverNumber(crs.getString("over").substring(0,crs.getString("over").length() - 2));
			list.add(bean1);
		}
		/*抽样单据*/
		String sql2 = "SELECT*FROM (SELECT flow_id,count(flow_id) total from dh_tranlist where flow_id='03' and tran_title='抽样单录入' GROUP BY flow_id ) a,"
				+ "(SELECT SUM(CASE  WHEN deal_status='1' THEN '1' ELSE '0' END ) end,flow_id from dh_tranlist where flow_id='03' and tran_title='抽样单审核' GROUP BY flow_id) b,"
				+ "(SELECT SUM(CASE  WHEN over_status='1' THEN '1' ELSE '0' END ) over,flow_id from dh_tranlist where flow_id='03' and tran_title='抽样单审核' GROUP BY flow_id) c "
				+ " where a.flow_id=b.flow_id and a.flow_id=c.flow_id ";
		
		List<JdPlanCaseCheck> list2 = new ArrayList<JdPlanCaseCheck>();
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] {});

		while (crs2.next()) {
			JdPlanCaseCheck bean2 = new JdPlanCaseCheck();
			bean2.setSampleNumber(crs2.getString("total"));
			bean2.setSampledNumber(crs2.getString("end").substring(0,crs2.getString("end").length() - 2));
			bean2.setSampleOverNumber(crs2.getString("over").substring(0,crs2.getString("over").length() - 2));
			list2.add(bean2);
		}
		/*检验任务*/
		String sql3 = "SELECT*FROM (SELECT flow_id,count(flow_id) total from dh_tranlist where flow_id='03' and tran_title='抽样单录入' GROUP BY flow_id ) a,"
				+ "(SELECT SUM(CASE  WHEN deal_status='1' THEN '1' ELSE '0' END ) end,flow_id from dh_tranlist where flow_id='03' and tran_title='检验数据审核' GROUP BY flow_id) b,"
				+ "(SELECT SUM(CASE  WHEN over_status='1' THEN '1' ELSE '0' END ) over,flow_id from dh_tranlist where flow_id='03' and tran_title='检验数据审核' GROUP BY flow_id) c "
				+ " where a.flow_id=b.flow_id and a.flow_id=c.flow_id ";
		
		List<JdPlanCaseCheck> list3 = new ArrayList<JdPlanCaseCheck>();
		CachedRowSetImpl crs3= DBFacade.getInstance().getRowSet(sql3,new String[] {});

		while (crs3.next()) {
			JdPlanCaseCheck bean3 = new JdPlanCaseCheck();
			bean3.setInspectNumber(crs3.getString("total"));
			bean3.setInspectedNumber(crs3.getString("end").substring(0,crs3.getString("end").length() - 2));
			bean3.setInspectOverNumber(crs3.getString("over").substring(0,crs3.getString("over").length() - 2));
			list3.add(bean3);
		}
		/*检验报告*/
		String sql4 = "SELECT*FROM (SELECT flow_id,count(flow_id) total from dh_tranlist where flow_id='03' and tran_title='抽样单录入' GROUP BY flow_id ) a,"
				+ "(SELECT SUM(CASE  WHEN deal_status='1' THEN '1' ELSE '0' END ) end,flow_id from dh_tranlist where flow_id='03' and tran_title='检验报告打印' GROUP BY flow_id) b,"
				+ "(SELECT SUM(CASE  WHEN over_status='1' THEN '1' ELSE '0' END ) over,flow_id from dh_tranlist where flow_id='03' and tran_title='检验报告打印' GROUP BY flow_id) c "
				+ " where a.flow_id=b.flow_id and a.flow_id=c.flow_id ";
		
		List<JdPlanCaseCheck> list4 = new ArrayList<JdPlanCaseCheck>();
		CachedRowSetImpl crs4= DBFacade.getInstance().getRowSet(sql4,new String[] {});

		while (crs4.next()) {
			JdPlanCaseCheck bean4 = new JdPlanCaseCheck();
			bean4.setReportNumber(crs4.getString("total"));
			bean4.setReportedNumber(crs4.getString("end").substring(0,crs4.getString("end").length() - 2));
			bean4.setReportOverNumber(crs4.getString("over").substring(0,crs4.getString("over").length() - 2));
			list4.add(bean4);
		}
		
		map.put("tasktotal", list);
		map.put("sampletotal", list2);
		map.put("inspectotal", list3);
		map.put("reporttotal", list4);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
}
