package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdSample;
import com.dhcc.login.UserInfoBean;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleImpl {
	Page page = new Page();

	public String getJdSampleList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String compId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select ab.*,count(c.bill_id) bill_count from (select a.tran_id,a.sample_id,a.task_id,a.sample_title,a.handle_dept_name,a.select_area,a.select_enterprise,"+
		             "a.select_comp_name,a.is_handle,a.sign_status,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.task_name from jd_sample a,jd_plan_task b "+
				     "where a.task_id=b.task_id and a.handle_dept_id like '"+compId+"%'";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		sql = sql + " group by a.sample_id) ab left join jd_sample_bill c on ab.sample_id = c.sample_id and c.check_status='3' group by ab.sample_id";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("ab.sample_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setTranId(crs.getString("tran_id"));
			String sql1 = "select tran_title,deal_view,back_type from dh_tranlist where tran_id = ? and work_status='1'";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[]{bean.getTranId()});
			if(crs1.next()){
				bean.setTranTitle(crs1.getString("tran_title"));
				bean.setDealView(crs1.getString("deal_view"));
				bean.setBackType(crs1.getString("back_type"));
			}
			bean.setBillCount(crs.getString("bill_count"));
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectArea(crs.getString("select_area"));
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setOptTime(crs.getString("opt_time"));
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

	public void addJdSample(JdSample bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		String tranId = DBFacade.getInstance().getID();
		String SampleId=DBFacade.getInstance().getID();
		sqls[0] = "insert into jd_sample(sample_id,task_id,sample_title,handle_dept_id,handle_dept_name,select_end_date,select_area,select_enterprise," +
				"select_comp_id,select_comp_name,select_opt_id,select_opt_name,opt_id,opt_name,opt_time,is_handle) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),'0')";
		params[0] = new String[] { SampleId,bean.getTaskId(),bean.getSampleTitle(),bean.getHandleDeptId(), bean.getHandleDeptName(),bean.getSelectEndDate(),
				bean.getSelectArea(),bean.getSelectEnterprise(),bean.getSelectCompId(),bean.getSelectCompName(),bean.getSelectOptId(),
				bean.getSelectOptName(),bean.getOptId(),bean.getOptName()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务下达信息添加",
				"抽样任务ID：" + SampleId + ";抽样任务名称："
						+ bean.getSampleTitle(), "1" };
		
		
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editJdSample(JdSample bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_sample set sample_title=?,task_id=?,handle_dept_id=?,handle_dept_name=?,select_end_date=?,select_area=?,select_enterprise=?," +
				"select_comp_id=?,select_comp_name=?,select_opt_id=?,select_opt_name=?,opt_id=?,opt_name=?,opt_time=now() where sample_id=?";
		params[0] = new String[] { bean.getSampleTitle(),bean.getTaskId(),bean.getHandleDeptId(),bean.getHandleDeptName(),bean.getSelectEndDate(),
				bean.getSelectArea(),bean.getSelectEnterprise(),bean.getSelectCompId(),bean.getSelectCompName(),bean.getSelectOptId(),
				bean.getSelectOptName(),bean.getOptId(),bean.getOptName(),bean.getSampleId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务下达信息修改",
				"抽查任务ID：" + bean.getTaskId() + ";抽样任务名称："
						+ bean.getSampleTitle(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdSample(JdSample bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_sample where sample_id=?";
		params[0] = new String[] { bean.getSampleId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务下达信息删除",
				"抽样任务ID：" + bean.getSampleId() + ";抽样任务名称："
						+ bean.getSampleTitle(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJdSampleById(String sampleId) throws Exception {
		String sql = "select a.task_id,b.task_name,a.sample_title,a.handle_dept_id,a.handle_dept_name,a.select_end_date,a.select_area,"+
	                  "a.select_enterprise,a.select_comp_id,a.select_comp_name,a.select_opt_id,a.select_opt_name,a.opt_id,a.opt_name," +
	                  ""+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.is_handle,"+TimeUtil.getTimeShow("a.handle_opt_time")+" handle_opt_time," +
              		  "a.sign_status,a.sign_desc,a.sign_opt_id,a.sign_opt_name,"+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time " +
	                  "from jd_sample a,jd_plan_task b where a.task_id=b.task_id and a.sample_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSample bean = new JdSample();
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectEndDate(crs.getString("select_end_date"));
			bean.setSelectArea(crs.getString("select_area"));
			//解析区域
			//JSon转成字符串
			String areaId = "";
			String area = "";
			JSONArray jArray = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				areaId+= objs.get("id")+",";
				area+= objs.get("name")+",";
			}
			areaId = areaId.length()==0?areaId:areaId.substring(0, areaId.length()-1);
			area = area.length()==0?area:area.substring(0, area.length()-1);
			bean.setAreaId(areaId);
			bean.setAreaName(area);
			
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			//解析企业
			//JSon转成字符串
			String compId = "";
			String comp = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				compId+= objs.get("id")+",";
				comp+= objs.get("name")+",";
			}
			compId = compId.length()==0?compId:compId.substring(0, compId.length()-1);
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			bean.setEnterpriseId(compId);
			bean.setEnterpriseName(comp);
			
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setSelectOptId(crs.getString("select_opt_id"));
			bean.setSelectOptName(crs.getString("select_opt_name"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setHandleOptTime(crs.getString("handle_opt_time"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	public JSONArray getTaskList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select task_id ,task_name from jd_plan_task where is_handle='1' and is_valid='1' ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("task_id"));
				bean.put("name", crs.getString("task_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public JSONArray getCase1List() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select case_id ,plan_name from jd_plan_case";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("case_id"));
				bean.put("name", crs.getString("plan_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public List<ZTreeBean> treeUserList(String CompId) {
		ArrayList<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select serial_id,user_name from hr_staff where company_id like'"+CompId+"%' order by serial_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			int i = 0 ;
			while (crs.next()) {
				i++;
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("serial_id"));
				bean.setName(crs.getString("user_name"));
				bean.setNocheck(false);
				if (i == 1) {
					bean.setOpen(true);
				} else {
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public String getUserMap(JdSample bean1, String curPage, String perNumber,
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
		String userId = "";
		String sql0 = "select select_person from jd_plan_case where is_valid='1' and task_id=?";
		CachedRowSetImpl crs0 = DBFacade.getInstance().getRowSet(sql0.toString(), new String[] { bean1.getTaskId() });
		while (crs0.next()) {
			//JSon转成字符串
			JSONArray jArray = JSONArray.fromObject((crs0.getString("select_person")==null || 
					crs0.getString("select_person").trim().equals(""))?"[]":crs0.getString("select_person"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				userId+= objs.get("id")+",";
			}
			userId = userId.length()==0?userId:userId.substring(0, userId.length()-1);
		}
		
		String sql = "select serial_id,user_name from hr_staff where serial_id in ("+userId+") and 1=1 ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		if(bean1.getCompId() != null &&  !"".equals(bean1.getCompId())){
			sql = sql + " and company_id = '"+bean1.getCompId()+"'";
		}
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("serial_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setUserName(crs.getString("user_name"));
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
	
	
	public JSONArray getEpList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select enterprise_id ,enterprise_name from jd_enterprise_info where is_valid='1'";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
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
	public JSONArray getProList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select product_id ,product_name from jd_enterprise_product";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("product_id"));
				bean.put("name", crs.getString("product_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	public void changeStatus(JdSample bean){
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_sample set sign_status=?,sign_desc=?,sign_opt_time=now(),sign_opt_id=?,sign_opt_name=? where sample_id=?";
		params[0] = new String[] {bean.getSignStatus(),bean.getSignDesc(),bean.getSignOptId(),bean.getSignOptName(),bean.getSampleId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务下达信息签收状态修改",
				"抽样任务ID：" + bean.getSampleId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		
	}
	public List<ZTreeBean> getAreaTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where 1 = '1' order by area_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("area_id"));
				bean.setName(crs.getString("area_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getCompanyAllRoot(String companyId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' ";
		if (companyId != null && !companyId.isEmpty()
				&& !companyId.equals("D00")) {
			sql += " and company_id like '" + companyId + "%'";
		}
		sql += " order by company_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无所属组织机构信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}
	
	
	
	public boolean SampelTitleCheck(String SampleTitle) throws Exception{
		try {
			String sql = "select sample_title from jd_sample where sample_title = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { SampleTitle });
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public Map<String, Object> getPlanName(String taskId,String compId) throws Exception {
		String sql = "select a.select_area,a.select_enterprise,b.select_end_date from jd_plan_handle a,jd_plan_case b where a.task_id=b.task_id and a.task_id=? and a.handle_dept_id='"+compId+"' ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { taskId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSample bean = new JdSample();
			//解析区域
			//JSon转成字符串
			String area = "";
			JSONArray jArray = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				area+= objs.get("id")+",";
			}
			
			area = area.length()==0?area:area.substring(0, area.length()-1);
			bean.setSelectArea(area);
			
			//解析企业
			//JSon转成字符串
			String comp = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				comp+= objs.get("id")+",";
			}
			
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			bean.setSelectEnterprise(comp);
			bean.setSelectEndDate(crs.getString("select_end_date"));
			
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	public String getJdTaskList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String compId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.task_id,a.task_name from jd_plan_task a,jd_plan_handle b,jd_plan_case c where a.task_id=b.task_id and a.task_id=c.task_id and c.is_handle='1' and b.handle_dept_id like '"+compId+"%' ";
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
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
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

	/** *******************
	* @return
	* 2017-4-18
	* *******************
	*/
	public List<ZTreeBean> getCompanyAllRoot2() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' and is_root='1' ";
		/*if (companyId != null && !companyId.isEmpty()
				&& !companyId.equals("00")) {
			sql += " and company_id = '" + companyId + "'";
		}*/
		sql += " order by company_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无所属组织机构信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

	/** *******************
	* @param bean
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-4-26
	* *
	 * @throws SQLException ******************
	*/
	public String getSampleUser(JdSample bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select serial_id,user_name from hr_staff where 1=1 ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		String[] arr = bean1.getSampleDept().split(",");
		if(arr.length==1){
			sql += " and company_id like '"+arr[0]+"%'";
		}else if(arr.length>1){
			sql += " and company_id like '"+arr[0]+"%'";
			for(int i=1;i<arr.length;i++){
				sql += " or company_id like '"+arr[i]+"%'";
			}
		}
		
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("serial_id");
			page.setSord("desc");
		}
		
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setUserName(crs.getString("user_name"));
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

	/** *******************
	* @return
	* 2017-4-27
	* *
	 * @throws SQLException ******************
	*/
	public String getCompanyName(String handleDeptId) throws SQLException {
		String companyName="";
		String sql = "select company_name from hr_company where company_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { handleDeptId } );
		if(crs.next()){
			companyName = crs.getString("company_name");
		}
		return companyName;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-4-27
	* *
	 * @throws SQLException ******************
	*/
	public Map<String, Object> issueJdSample(JdSample bean) throws SQLException {
		
		String sql = "select select_comp_id,select_comp_name,select_opt_id,select_opt_name from jd_sample where sample_id = ?"; 
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { bean.getSampleId() });
		if(crs.next()){
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setSelectOptId(crs.getString("select_opt_id"));
			bean.setSelectOptName(crs.getString("select_opt_name"));
		}
		
		String[] sqls = new String[4];
		String[][] params = new String[4][];
		String tranId = DBFacade.getInstance().getID();
		bean.setNextDept("[{'id':'"+bean.getSelectCompId()+"',"+"'name':'"+bean.getSelectCompName()+"'}]");
		bean.setNextPerson("[{'id':'"+bean.getSelectOptId()+"',"+"'name':'"+bean.getSelectOptName()+"'}]");
		sqls[0] = "update jd_sample set current_status=1,is_handle='1',sign_status='0',handle_opt_time=now(),current_node_id=?,tran_id=? where sample_id=?";
		params[0] = new String[] {FlowConstant.CY_NODE_ARR_ID[0],tranId,bean.getSampleId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽样任务下发",
				"抽样任务ID：" + bean.getSampleId(), "1" };
		
		sqls[2] = "insert into dh_tranlist(work_status,tran_id,work_id,flow_id,node_id,step_id,detail_id,"
				+"belong_work,tran_title,"//
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,"
				+"create_opt_time,"//
				+"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,"
				+"deal_opt_time,"//
				+"deal_time,over_status,deal_status,opt_order,"//
				+ " next_dept,next_person,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,?,'jd_sample','抽样任务下发',?,?,?,?,now(),?,?,?,?,now(),'0','0','1','1',?,?,'1','1')";
		
		params[2] = new String[] { "0",tranId,bean.getSampleId(),FlowConstant.CY_FLOW_ID,
				FlowConstant.CY_NODE_ARR_ID[0],FlowConstant.CY_STEP_ARR_ID[0],bean.getSampleId(),
				bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(), bean.getOptName(), 
				bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(), bean.getOptName(),
				bean.getNextDept(),bean.getNextPerson()};
		
		//下一操作留痕
		sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_time,over_status,deal_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample','抽样任务签收',?,?,?,?,now(),?,?,'0','0','0','2',?,'1','1')";
		params[3] = new String[] { DBFacade.getInstance().getID(),bean.getSampleId(),FlowConstant.CY_FLOW_ID,
				FlowConstant.CY_NODE_ARR_ID[1],FlowConstant.CY_STEP_ARR_ID[1],bean.getSampleId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getSelectOptId(),bean.getSelectOptName(),bean.getSelectCompId(),bean.getSelectCompName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
}
