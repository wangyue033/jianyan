package com.dhcc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;

/**
 * @author xinxin jquery.autocomplete的下拉列表的读取,从数据库中读取的列表
 */
public class CommonUtil {
	/**
	 * 
	 * @param flag
	 *            : 如果为1，则不包括系统管理员
	 * @return 获得所有未离职员工列表
	 */
	public static JSONArray userList(String flag) {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select user_id,user_name,job_id,company_id from hr_user where state!='3' ";
			if ("1".equals(flag)) {
				sql += " and user_id != '0000'";
			}
			sql += " order by user_name";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("userId", crs.getString("user_id"));
				bean.put("userName", crs.getString("user_name"));
				bean.put("jobId", crs.getString("job_id"));
				bean.put("companyId", crs.getString("company_id"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 提交审核时选择用
	 * 
	 * @param flag
	 *            暂时未定义
	 * @return 获得所有有效职位和对应机构编号的列表
	 */
	public static JSONArray jobList(String flag) {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select a.job_id,a.job_name,a.company_id,b.company_name from hr_job a,hr_company b "
					+ "where a.is_valid = '1' and b.is_valid = '1' and a.company_id=b.company_id order by a.company_id,a.job_id";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("jobId", crs.getString("job_id"));
				bean.put("jobName", crs.getString("job_name"));
				bean.put("companyId", crs.getString("company_id"));
				bean.put("companyName", crs.getString("company_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 * @param flag
	 *            company_id
	 * @return 获得所有指定组织机构的有效职位列表
	 */
	public static JSONArray jobValidList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select job_id,job_name from hr_job "
					+ "where is_valid = '1' ";
			sql += " order by job_id ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("jobId", crs.getString("job_id"));
				bean.put("jobName", crs.getString("job_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	
	/**
	 * 
	 * @param flag
	 *            暂时未定义
	 * @return 获得结算中心
	 */
	public static JSONArray balanceCenterList(String id) {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select center_id,center_name from balance_center_info where (is_valid='1' or center_id = ?)";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { id });
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("centerId", crs.getString("center_id"));
				bean.put("centerName", crs.getString("center_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	// 三级区域列表
	public static JSONArray getMarketJson() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select a.market_id,a.market_name,a.short_name "
					+ "from market_info a where a.depth = '3' "
					+ "and a.is_valid = '1' order by a.market_id asc";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("marketId", crs.getString("market_id"));
				bean.put("marketName", crs.getString("market_name"));
				bean.put("shortName", crs.getString("short_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

}
