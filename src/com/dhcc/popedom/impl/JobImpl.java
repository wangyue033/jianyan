package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.Job;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.utils.LogUtil;
/**
 * 
 * @author WYH
 *
 */
public class JobImpl {
	Page page = new Page();

	public String getJobList(Job bean2,String curPage,
			String perNumber, String orderByField,
			String orderBySort, String searchField,
			String searchValue) throws SQLException,
			ParseException {
		if (curPage == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.job_id,a.company_id,a.job_name,a.is_valid,a.opt_id,a.opt_time,b.company_name " +
				"from hr_job a left join hr_company b on a.company_id=b.company_id where 1=1";
		if (searchField != null
				&& !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField
					+ " like '%" + searchValue.trim() + "%' ";
		}
		if(bean2.getCompanyId() != null &&  !"".equals(bean2.getCompanyId())){
			sql = sql + " and a.company_id like '" + bean2.getCompanyId() + "%'";
		}
		if (orderByField != null
				&& !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("job_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<Job> list = new ArrayList<Job>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			Job bean = new Job();
			bean.setJobId(crs.getString("job_id"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setJobName(crs.getString("job_name"));
			bean.setIsValid(crs.getString("is_Valid"));
			bean.setOptId(crs.getString("opt_Id"));
			bean.setOptTime(TimeUtil.getStringDate(crs.getDate("opt_time")));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		map.put("totalPage",
				"" + pageData.getPageAmount());
		map.put("curPage", "" + pageData.getPageNo());
		map.put("totalRecord",
				"" + pageData.getItemAmount());
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	//职位名称重复验证
	public boolean checkJobName(String jobName,String orgId) throws SQLException{
		String sql="select count(*) from hr_job where job_name = ? and company_id = ? ";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { jobName,orgId });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			return true;
	}

	
	// 添加职位信息
	public void addJob(Job bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into hr_job(job_id,company_id,job_name,job_range,is_valid,opt_id,opt_name,opt_time) values(?,?,?,?,?,?,?,now())";
		params[0] = new String[] { bean.getJobId(),bean.getCompanyId(),
				bean.getJobName(), bean.getJobRange(),
				"1", bean.getOptId(),bean.getOptName() };
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "职位信息添加", "职位编号：" + bean.getJobId() + ";职位名称："
				+ bean.getJobName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		DBFacade.getInstance().execute(sqls, params);
	}

	// 删除职位
	public Map<String, Object> delJob(Job bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "delete from hr_job where job_id=?";
		params[0] = new String[] { bean.getJobId() };
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "职位信息删除", "职位编号：" + bean.getJobId());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	// 编辑职位功能
	public Map<String, Object> editJob(Job bean)
			throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update hr_job set job_name=?,opt_time=?,opt_id=?,job_range=?,is_valid=? where job_id=?";
		params[0] = new String[] {
				bean.getJobName(), bean.getOptTime(),
				bean.getOptId(), bean.getJobRange(),
				bean.getIsValid(), bean.getJobId() };
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "职位信息修改", "职位编号：" + bean.getJobId());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		DBFacade.getInstance()
				.execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJobById(
			String jobId) throws Exception {
		String sql = "select a.job_id,a.company_id,a.job_name,a.job_range,a.is_valid,a.opt_id,a.opt_name,a.opt_time,b.company_name " +
				"from hr_job a,hr_company b where a.company_id=b.company_id and job_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { jobId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			Job bean = new Job();
			bean.setJobId(crs.getString("job_id"));
			bean.setJobName(crs.getString("job_name"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptTime(TimeUtil.getStringDate(crs.getDate("opt_time")));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setJobRange(crs.getString("job_range"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}

}
