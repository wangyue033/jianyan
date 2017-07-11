package com.dhcc.inspect.impl;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.JdPlan;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.utils.LogUtil;

/**
 * 
 * @author wangyue 抽查计划信息
 */
public class JdPlanImpl {
	Page page = new Page();

	public String getJdPlanList(String curPage, String perNumber,
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
		// TODO: handle exception
		String sql = "select a.plan_id,a.plan_no,a.plan_name,a.plan_type,a.plan_date,a.is_valid,"
				+ "a.plan_attact,a.comp_id,c.company_name,a.opt_name,count(b.task_id) task_count "
				+ "from jd_plan a left join jd_plan_task b on a.plan_id = b.plan_id ,hr_company c where a.comp_id = c.company_id ";
		if (searchField != null && !"".equals(searchField) && !"undefined".equals(searchField) && !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue + "%' ";
		}
		if((searchField == null || "".equals(searchField) || "undefined".equals(searchField) ) && (searchValue != null && !"".equals(searchValue) && !"undefined".equals(searchValue)) ){
			sql = sql + " and (a.plan_no like '%"+searchValue+"%' or a.plan_name like '%"+searchValue+"%' or c.company_name like '%"+searchValue+"%' )";
		}
		sql = sql +"group by a.plan_id,a.plan_no,a.plan_name,a.plan_type,a.plan_date,a.is_valid,a.comp_id,c.company_name,a.opt_name ";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			// TODO: handle exception
			page.setSidx("plan_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlan> list = new ArrayList<JdPlan>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlan bean = new JdPlan();
			bean.setPlanId(crs.getString("plan_id"));
			bean.setPlanNo(crs.getString("plan_no"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setPlanType(crs.getString("plan_type"));
			bean.setPlanDate(crs.getString("plan_date"));
			bean.setPlanAttact(crs.getString("plan_attact"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setCompId(crs.getString("comp_id"));
			bean.setCompName(crs.getString("company_name"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setTaskCount(crs.getString("task_count"));
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

	public void addJdPlan(JdPlan bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		String planId = DBFacade.getInstance().getID();

		sqls[0] = "insert into jd_plan (plan_id,plan_no,plan_name,plan_type,plan_date,plan_desc,plan_attact,is_valid,"
				+ "comp_id,opt_id,opt_name,opt_time)"
				+ "values (?,?,?,?,?,?,?,?,?,?,?,now())";
		params[0] = new String[] { planId, bean.getPlanNo(),
				bean.getPlanName(), bean.getPlanType(), bean.getPlanDate(),
				bean.getPlanDesc(), bean.getPlanAttact(), "1",
				bean.getCompId(), bean.getOptId(), bean.getOptName() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽查计划信息添加", "抽查计划编号："
						+ bean.getPlanNo() + " , 抽查计划名称：" + bean.getPlanName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}

	public Map<String, Object> editJdPlan(JdPlan bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		// TODO: handle exception
		sqls[0] = " update jd_plan set plan_no=? , plan_name=? , plan_type=? , plan_date=? , plan_desc=? , plan_attact=? , "
				+ " is_valid=? , comp_id=? , comp_name=? , opt_id=? , opt_name=? , opt_time=now() "
				+ " where plan_id=?";
		params[0] = new String[] { bean.getPlanNo(), bean.getPlanName(),
				bean.getPlanType(), bean.getPlanDate(), bean.getPlanDesc(),
				bean.getPlanAttact(), bean.getIsValid(), bean.getCompId(),
				bean.getCompName(), bean.getOptId(), bean.getOptName(),
				bean.getPlanId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽查计划信息修改", "抽查计划编号："
						+ bean.getPlanNo() + " , 抽查计划名称：" + bean.getPlanName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdPlan(JdPlan bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = "delete from jd_plan where plan_id=?";
		params[0] = new String[] { bean.getPlanId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽查计划信息删除", "抽查计划系统编号："
						+ bean.getPlanId());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		//删除服务器上的附件信息
		String str = bean.getPlanAttact();
		System.out.println(str);
		String[] temp = str.split(";;");
		for (int i = 0; i < temp.length; i++) {
			if (i % 2 == 1) {
				if (new File(bean.getPath() + temp[i]).exists()) {
					new File(bean.getPath() + temp[i]).delete();
				}
			}
		}
				
		DBFacade.getInstance().execute(sqls, params);

		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJdPlanById(String planId) throws Exception {
		String sql = "select a.plan_id,a.plan_no,a.plan_name,a.plan_type,a.plan_date,a.plan_desc,a.plan_attact,a.is_valid,"
				+ "a.comp_id ,b.company_name,a.opt_id,a.opt_name,a.opt_time from jd_plan a,hr_company b where a.comp_id = b.company_id and  plan_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { planId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdPlan bean = new JdPlan();
			bean.setPlanId(crs.getString("plan_id"));
			bean.setPlanNo(crs.getString("plan_no"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setPlanType(crs.getString("plan_type"));
			bean.setPlanDate(crs.getString("plan_date"));
			bean.setPlanDesc(crs.getString("plan_desc"));
			bean.setPlanAttact(crs.getString("plan_attact"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setCompId(crs.getString("comp_id"));
			bean.setCompName(crs.getString("company_name"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	// 计划编号重复验证
	public boolean checkPlanNo(String planNo) {
		String sql = "select count(*) from jd_plan where plan_no = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { planNo });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 计划名称重复验证
	public boolean checkPlanName(String planName) {
		String sql = "select count(*) from jd_plan where plan_name = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { planName });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 修改时验证计划编号是否重复
	public boolean editCheckPlanNo(String planNo, String planId) {
		String sql = "select count(*) from jd_plan where plan_no = ? and plan_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { planNo, planId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 修改时验证计划名称是否重复
	public boolean editCheckPlanName(String planName, String planId) {
		String sql = "select count(*) from jd_plan where plan_name = ? and plan_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { planName, planId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderBySort
	 * @param searchField
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public String getJdPlanJsonList(String curPage, String perNumber,
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
		// TODO: handle exception
		String sql = "select a.plan_id,a.plan_no,a.plan_name,a.plan_type,a.plan_date,count(b.task_id) task_count "
				+ "from jd_plan a left join jd_plan_task b on a.plan_id = b.plan_id and b.is_valid = '1' where 1 = 1  ";
		if (searchField != null && !"".equals(searchField) && !"undefined".equals(searchField) && !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue + "%' ";
		}
		if((searchField == null || "".equals(searchField) || "undefined".equals(searchField) ) && (searchValue != null && !"".equals(searchValue) && !"undefined".equals(searchValue)) ){
			sql = sql + " and (a.plan_no like '%"+searchValue+"%' or a.plan_name like '%"+searchValue+"%' )";
		}
		sql = sql +"group by a.plan_id,a.plan_no,a.plan_name,a.plan_type,a.plan_date ";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			// TODO: handle exception
			page.setSidx("plan_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlan> list = new ArrayList<JdPlan>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlan bean = new JdPlan();
			bean.setPlanId(crs.getString("plan_id"));
			bean.setPlanNo(crs.getString("plan_no"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setPlanType(crs.getString("plan_type"));
			bean.setPlanDate(crs.getString("plan_date"));
			bean.setTaskCount(crs.getString("task_count"));
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
	public String getJdPlanJsonList2(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		// TODO: handle exception
		String sql = "select plan_id,plan_no,plan_name,plan_type,plan_date "
				+ "from jd_plan where is_valid = '1' ";
		if (searchField != null && !"".equals(searchField) && !"undefined".equals(searchField) && !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue + "%' ";
		}
		if((searchField == null || "".equals(searchField) || "undefined".equals(searchField) ) && (searchValue != null && !"".equals(searchValue) && !"undefined".equals(searchValue)) ){
			sql = sql + " and (plan_no like '%"+searchValue+"%' or plan_name like '%"+searchValue+"%' )";
		}
		sql = sql +"group by plan_id,plan_no,plan_name,plan_type,plan_date ";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			// TODO: handle exception
			page.setSidx("plan_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlan> list = new ArrayList<JdPlan>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdPlan bean = new JdPlan();
			bean.setPlanId(crs.getString("plan_id"));
			bean.setPlanNo(crs.getString("plan_no"));
			bean.setPlanName(crs.getString("plan_name"));
			bean.setPlanType(crs.getString("plan_type"));
			bean.setPlanDate(crs.getString("plan_date"));
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
}
