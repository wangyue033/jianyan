package com.dhcc.inspect.impl;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdPlanHandle;
import com.dhcc.inspect.domain.JdPlanTask;
import com.dhcc.popedom.domain.Comp;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.LogUtil;

/**
 * 
 * @author wangyue 抽检任务信息 JD_PLAN_TASK
 */
public class JdPlanTaskImpl {
	Page page = new Page();

	public String getJdPlanTaskList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue) throws Exception {

		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select b.tran_title,b.deal_view,b.back_type,a.task_id,a.task_no,a.task_name,a.main_dept_id,a.main_dept_name,a.comp_name,a."
				+ "product_name,a.begin_date,a.end_date,a.is_valid,a.opt_id,a.opt_name,a.opt_time,a.is_handle,a.trans_status,a.sign_status from jd_plan_task a left join dh_tranlist b on a.tran_id = b.tran_id";

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
			page.setSidx("task_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdPlanTask> list = new ArrayList<JdPlanTask>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlanTask bean = new JdPlanTask();
			bean.setTranTitle(crs.getString("tran_title"));
			bean.setDealView(crs.getString("deal_view"));
			bean.setBackType(crs.getString("back_type"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setMainDeptId(crs.getString("main_dept_id"));
			bean.setMainDeptName(crs.getString("main_dept_name"));
			bean.setCompName(crs.getString("comp_name"));
			bean.setProductName(crs.getString("product_name"));
			bean.setBeginDate(crs.getString("begin_date"));
			bean.setEndDate(crs.getString("end_date"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter.format(formatter.parse(optTime));
			bean.setOptTime(time);
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setTransStatus(crs.getString("trans_status"));
			bean.setSignStatus(crs.getString("sign_status"));
			
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		map.put("totalPage", "" + pageData.getPageAmount());
		map.put("curPage", "" + pageData.getPageNo());
		map.put("totalRecord", "" + pageData.getItemAmount());
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	public void addJdPlanTask(JdPlanTask bean) throws Exception {

		int size = 0;
		if(null!=bean.getList()){
			size=bean.getList().size();
		}
		String taskId = DBFacade.getInstance().getID();

		String[] sqls = new String[2 + size];
		String[][] params = new String[2 + size][];
		//记录任务信息
		sqls[0] = "insert into jd_plan_task (task_id,plan_id,task_no,task_name,task_type,task_attr,comp_id,comp_Name,base_no,"
				+ "base_text,product_id,product_name,begin_date,end_date,sample_batch,sample_base,special_ask,"
				+ "main_dept_id,main_dept_name,is_valid,opt_id,opt_name,opt_time,is_handle,trans_status) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,now(),'0','0')";

		params[0] = new String[] {taskId,bean.getPlanId(),bean.getTaskNo(),bean.getTaskName(),bean.getTaskType(),
				bean.getTaskAttr(),bean.getCompId(),bean.getCompName(),bean.getBaseNo(),bean.getBaseText(),bean.getProductId(),
				bean.getProductName(),bean.getBeginDate(),bean.getEndDate(),bean.getSampleBatch(),bean.getSampleBase(),
				bean.getSpecialAsk(),bean.getMainDeptId(),bean.getMainDeptName(),bean.getOptId(),bean.getOptName()};
				
		//记录日志信息
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检任务信息添加",
				"抽查任务ID：" + taskId);
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		for (int i = 2; i < size + 2; i++) {
			JdPlanHandle jph = bean.getList().get(i - 2);
			
			sqls[i] = "insert into jd_plan_handle (handle_id,task_id,handle_dept_id,handle_dept_name,select_area,"
					+ "select_enterprise,inspet_dept_id,inspet_dept_name,deal_dept,opt_id,opt_name,opt_time) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,now())";
			params[i] = new String[] {
					DBFacade.getInstance().getID(),taskId,jph.getHandleDeptId(),jph.getHandleDeptName(),
					jph.getSelectAreaId(),jph.getSelectEnterpriseId(),jph.getInspetDeptId(),jph.getInspetDeptName(),
					jph.getDealDept(),bean.getOptId(),bean.getOptName() };
		}

		DBFacade.getInstance().execute(sqls, params);
	}

	public Map<String, Object> editJdPlanTask(JdPlanTask bean) throws Exception {
		
		int size = 0;
		if(null!=bean.getList()){
			size=bean.getList().size();
		}

		String[] sqls = new String[3 + size];
		String[][] params = new String[3 + size][];
		
		//先删除抽检任务签收信息
		sqls[0] = "delete from jd_plan_handle where task_id=? ";
		params[0] = new String[] { bean.getTaskId()};
		
		sqls[1] = " update jd_plan_task set task_no=?,task_name=?,task_type=?,task_attr=?,"
				+ "base_no=?,base_text=?,product_id=?,product_name=?,begin_date=?,end_date=?,sample_batch=?,"
				+ "sample_base=?,special_ask=?,main_dept_id=?,main_dept_name=?,is_valid=?,opt_id=?,opt_name=?,opt_time=now() "
				+ " where task_id=?";
		params[1] = new String[] { bean.getTaskNo(),bean.getTaskName(),bean.getTaskType(),bean.getTaskAttr(),
				bean.getBaseNo(),bean.getBaseText(),bean.getProductId(),bean.getProductName(), bean.getBeginDate(),
				bean.getEndDate(), bean.getSampleBatch(),bean.getSampleBase(), bean.getSpecialAsk(), bean.getMainDeptId(),
				bean.getMainDeptName(), bean.getIsValid(),bean.getOptId(), bean.getOptName(), bean.getTaskId() };
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检任务信息修改",
				"抽检任务ID:"+bean.getTaskId());
		sqls[2] = (String) obj[0];
		params[2] = (String[]) obj[1];
		
		for (int i = 3; i < size + 3; i++) {
			JdPlanHandle jph = bean.getList().get(i - 3);
			sqls[i] = "insert into jd_plan_handle (handle_id,task_id,handle_dept_id,handle_dept_name,select_area,"
					+ "select_enterprise,inspet_dept_id,inspet_dept_name,deal_dept,opt_id,opt_name,opt_time) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,now())";
			params[i] = new String[] {
					DBFacade.getInstance().getID(),bean.getTaskId(),jph.getHandleDeptId(),jph.getHandleDeptName(),
					jph.getSelectAreaId(),jph.getSelectEnterpriseId(),jph.getInspetDeptId(),jph.getInspetDeptName(),
					jph.getDealDept(), bean.getOptId(),bean.getOptName() };
		}

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdPlanTask(JdPlanTask bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];
		
		//删除抽检任务签收信息
		sqls[0] = "delete from jd_plan_handle where task_id=?";
		params[0] = new String[] { bean.getTaskId()};
		
		//删除抽检任务表中的信息
		sqls[1] = "delete from jd_plan_task where task_id=? and is_handle=? ";
		params[1] = new String[] { bean.getTaskId(), "0" };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检任务信息删除",
				"{{delJdPlanTask}}");
		sqls[2] = (String) obj[0];
		params[2] = (String[]) obj[1];
		
		DBFacade.getInstance().execute(sqls, params);

		// 删除服务器上的附件信息
		String str = bean.getBaseText();
		String[] temp = str.split(";;");
		for (int i = 0; i < temp.length; i++) {
			if (i % 2 == 1) {
				if (new File(bean.getPath() + temp[i]).exists()) {
					new File(bean.getPath() + temp[i]).delete();
				}
			}
		}

		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> handleJdPlanTask(JdPlanTask bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[4];
		String[][] params = new String[4][];
		String tranId = DBFacade.getInstance().getID();
		/**
		 * 修改抽检任务信息表的下发状态
		 */
		sqls[0] = "update jd_plan_task set is_handle=?,trans_status=?,trans_dept_id=?,trans_dept_name=?,sign_status=?,handle_opt_id=?,handle_opt_name=?,handle_opt_time=now(),current_node_id=?,current_status='1',tran_id=? where task_id=? ";
		params[0] = new String[] { "1","0",bean.getMainDeptId(),bean.getMainDeptName(),"0",bean.getHandleOptId(),bean.getHandleOptName(),FlowConstant.CC_NODE_ARR_ID[0],tranId,bean.getTaskId() };
		
		/**
		 * 抽检任务下发进留痕表，第一步给本步骤插入数据
		 */
		bean.setNextDept("[{'id':'"+bean.getMainDeptId()+"',"+"'name':'"+bean.getMainDeptName()+"'}]");
		
		sqls[1] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_opt_time,deal_time,over_status,deal_status,work_status,opt_order,"
				+ " next_dept,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_plan_task','抽检任务下发',?,?,?,?,now(),?,?,?,?,now(),'0','0','1','0','1',?,'1','1')";
		params[1] = new String[] { tranId,bean.getTaskId(),FlowConstant.CC_FLOW_ID,
				FlowConstant.CC_NODE_ARR_ID[0],FlowConstant.CC_STEP_ARR_ID[0],bean.getTaskId(),bean.getCompId(),
				bean.getCompName(),bean.getHandleOptId(), bean.getHandleOptName(), bean.getCompId(),
				bean.getCompName(),bean.getHandleOptId(), bean.getHandleOptName(),bean.getNextDept()};
		/**
		 * 给下一步骤插入数据待下一步骤更改
		 */
		sqls[2] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_plan_task','抽检任务转办',?,?,?,?,now(),?,?,'0','0','0','0','2',?,'1','1')";
		params[2] = new String[] { DBFacade.getInstance().getID(),bean.getTaskId(),FlowConstant.CC_FLOW_ID,
				FlowConstant.CC_NODE_ARR_ID[1],FlowConstant.CC_STEP_ARR_ID[1],bean.getTaskId(),bean.getCompId(),
				bean.getCompName(),bean.getHandleOptId(),bean.getHandleOptName(),bean.getMainDeptId(),bean.getMainDeptName(),tranId};
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),bean.getHandleOptId(), bean.getHandleOptName(), 
				"抽检任务信息下发","抽检任务ID："+bean.getTaskId());
		sqls[3] = (String) obj[0];
		params[3] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);

		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJdPlanTaskById(String taskId)
			throws Exception {
		String sql = "select a.task_id,a.plan_id,a.task_no,a.task_name,a.task_type,a.task_attr,a.comp_id,a.comp_name,a.base_no,"
				+ "a.base_text,a.product_id,a.product_name,a.begin_date,a.end_date,a.sample_batch,a.sample_base,a.special_ask,"
				+ "a.main_dept_id,a.main_dept_name,a.is_valid,a.opt_id,a.opt_name,a.opt_time,a.is_handle,a.handle_opt_id," +
				" a.handle_opt_name,a.handle_opt_time,b.plan_name "
				+ " from jd_plan_task a,jd_plan b where a.plan_id = b.plan_id and a.task_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { taskId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdPlanTask bean = new JdPlanTask();
			bean.setTaskId(crs.getString("task_id"));
			bean.setPlanId(crs.getString("plan_id"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setTaskType(crs.getString("task_type"));
			bean.setTaskAttr(crs.getString("task_attr"));
			bean.setCompId(crs.getString("comp_id"));
			bean.setCompName(crs.getString("comp_name"));
			bean.setBaseNo(crs.getString("base_no"));
			bean.setBaseText(crs.getString("base_text"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setBeginDate(crs.getString("begin_date"));
			bean.setEndDate(crs.getString("end_date"));
			bean.setSampleBatch(crs.getString("sample_batch"));
			bean.setSampleBase(crs.getString("sample_base"));
			bean.setSpecialAsk(crs.getString("special_ask"));
			bean.setMainDeptId(crs.getString("main_dept_id"));
			bean.setMainDeptName(crs.getString("main_dept_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter.format(formatter.parse(optTime));
			bean.setOptTime(time);
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setHandleOptId(crs.getString("handle_opt_id"));
			bean.setHandleOptName(crs.getString("handle_opt_name"));
			String handleoptTime = crs.getString("handle_opt_time");
			if(handleoptTime == null){
				bean.setHandleOptTime(crs.getString("handle_opt_time"));
			}else{
				String handleTime = formatter
						.format(formatter.parse(handleoptTime));
				bean.setHandleOptTime(handleTime);
			}
			
			String sql2 = "select handle_id,task_id,handle_dept_id,handle_dept_name,select_area,"
					+ "select_enterprise,inspet_dept_id,inspet_dept_name,deal_dept from jd_plan_handle where task_id = ? ";
			
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
					new String[] {taskId });
			List<JdPlanHandle> list = new ArrayList<JdPlanHandle>();
			int num = 1;
			while(crs2.next()){
				JdPlanHandle bean2 = new JdPlanHandle();
				bean2.setHandleId(crs2.getString("handle_id"));
				bean2.setTaskId(crs2.getString("task_id"));
				bean2.setHandleDeptId(crs2.getString("handle_dept_id"));
				bean2.setHandleDeptName(crs2.getString("handle_dept_name"));
				bean2.setSelectAreaId(crs2.getString("select_area"));
				
				//取多个区域的名称
				//解析区域
				//JSon转成字符串
				String area = "";
				String areaIds = "";
				JSONArray jArray2 = JSONArray.fromObject((crs2.getString("select_area")==null || 
						crs2.getString("select_area").trim().equals(""))?"[]":crs2.getString("select_area"));
				for(Object obj : jArray2){
					Map objs = JSONObject.fromObject(obj);
					area+= objs.get("name")+",";
					areaIds+=objs.get("id")+",";
				}
				area = area.length()==0?area:area.substring(0, area.length()-1);
				areaIds = areaIds.length()==0?areaIds:areaIds.substring(0, areaIds.length()-1);
				bean2.setSelectArea(area);
				bean2.setSelectAreaIds(areaIds);
				
				bean2.setSelectEnterpriseId(crs2.getString("select_enterprise"));
				
				//解析企业
				//JSon转成字符串
				String comp = "";
				String compIds = "";
				JSONArray jArray3 = JSONArray.fromObject((crs2.getString("select_enterprise")==null || 
						crs2.getString("select_enterprise").trim().equals(""))?"[]":crs2.getString("select_enterprise"));
				for(Object obj : jArray3){
					Map objs = JSONObject.fromObject(obj);
					comp+= objs.get("name")+",";
					compIds+=objs.get("id")+",";
				}
				
				comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
				compIds = compIds.length()==0?compIds:compIds.substring(0, compIds.length()-1);
				bean2.setSelectEnterprise(comp);
				bean2.setSelectEnterpriseIds(compIds);
				
				bean2.setInspetDeptId(crs2.getString("inspet_dept_id"));
				bean2.setInspetDeptName(crs2.getString("inspet_dept_name"));
				bean2.setDealDept(crs2.getString("deal_dept"));
				bean2.setCounts(String.valueOf(num));
				num++;
				list.add(bean2);
			}
			map.put("jdPlanTasks", list);
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	// 任务编号重复验证
	public boolean checkTaskNo(String taskNo) {
		String sql = "select count(*) from jd_plan_task where task_no = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { taskNo });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 任务名称重复验证
	public boolean checkTaskName(String taskName) {
		String sql = "select count(*) from jd_plan_task where task_name = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { taskName });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 修改时验证任务编号是否重复
	public boolean editCheckTaskNo(String taskNo, String taskId) {
		String sql = "select count(*) from jd_plan_task where task_no = ? and task_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { taskNo, taskId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 修改时验证任务名称是否重复
	public boolean editCheckTaskName(String taskName, String taskId) {
		String sql = "select count(*) from jd_plan_task where task_name = ? and task_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { taskName, taskId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 获取计划信息列表
	public JSONArray getPlanList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select plan_id ,plan_name , plan_no , plan_type from jd_plan where is_valid = '1' ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);
			
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("plan_id"));
				bean.put("name", crs.getString("plan_name"));
				bean.put("no", crs.getString("plan_no"));
				bean.put("type", crs.getString("plan_type"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public List<ZTreeBean> getProductTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select product_id,product_name from jd_enterprise_product where is_valid = '1' order by product_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("product_id"));
				bean.setName(crs.getString("product_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	public List<ZTreeBean> getProductTypeTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select type_id,type_name , parent_id from jd_product_type where is_valid = '1' ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setName(crs.getString("type_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}

	public String getTaskNoByCompId(String compId) {
		String sql = "select task_no from jd_plan_task order by task_no asc ";
		String taskNo = "";
		int i = 0;
		String index = "";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				taskNo = crs.getString("task_no");
				String compIdTemp = taskNo.substring(4, taskNo.length() - 3);
				if (compIdTemp.trim().equals(compId)) {
					i = Integer.parseInt(taskNo.substring(taskNo.length() - 3,taskNo.length()));
				}
			}
			i++;
			index = i + "";
			if (index.length() == 1) {
				index = "00" + index;
			} else if (index.length() == 2) {
				index = "0" + index;
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(new Date());
			taskNo = year + compId + index;
		} catch (Exception se) {
			se.printStackTrace();
		}

		return taskNo;

	}
	
	//获取检验机构列表
	public List<Comp> getCompList() {
		List<Comp> list = new ArrayList<Comp>();
		String sql = "select company_id,company_name from hr_company where is_valid=1";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{});
		try {
			while (crs.next()) {
				Comp bean = new Comp();
				bean.setCompanyId(crs.getString("company_id"));
				bean.setCompanyName(crs.getString("company_name"));				
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 获取企业信息列表
	public JSONArray getEnterpriseList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select enterprise_id ,enterprise_name  from jd_enterprise_info where 1 = 1 ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("enterprise_id"));
				bean.put("name", crs.getString("enterprise_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	
	//获取检验机构列表
	public JSONArray getCompanyList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select company_id,company_name from hr_company where is_valid=1";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("company_id"));
				bean.put("name", crs.getString("company_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	

	//获取区域列表
	public JSONArray getAreaList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select area_id,area_name from jd_area_info ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("area_id"));
				bean.put("name", crs.getString("area_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/** *******************
	* @param compId
	* @return
	* 2017-5-1
	* *
	 * @throws SQLException ******************
	*/
	public String getCompName(String compId) throws SQLException {
		String sql = "select company_id,company_name from hr_company where company_id=? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
				sql.toString(), new String[]{compId});
		String compName = "";
		if (crs.next()) {
			compName = crs.getString("company_name");
		}
		return compName;
	}
}
