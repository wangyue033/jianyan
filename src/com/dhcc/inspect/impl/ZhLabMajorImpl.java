package com.dhcc.inspect.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.ZhLabMajor;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.utils.LogUtil;

/**
 * 
 * @author wangyue 检测机构专业类别信息 ZH_LAB_MAJOR
 */
public class ZhLabMajorImpl {
	Page page = new Page();

	public String getZhLabMajorList(String curPage, String perNumber,
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
		String sql = "select major_id , major_name , major_code , remark , is_valid , "
				+ "  opt_id , opt_name , opt_time from zh_lab_major where 1=1 ";

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
			page.setSidx("major_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<ZhLabMajor> list = new ArrayList<ZhLabMajor>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			ZhLabMajor bean = new ZhLabMajor();
			bean.setMajorId(crs.getString("major_id"));
			bean.setMajorName(crs.getString("major_name"));
			bean.setMajorCode(crs.getString("major_code"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
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

	public void addZhLabMajor(ZhLabMajor bean) throws Exception {
		String majorId = DBFacade.getInstance().getID();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into zh_lab_major ( major_id , major_name , major_code , remark , is_valid , opt_id , opt_name , opt_time ) "
				+ "values (?,?,?,?,?,?,?,now())";

		params[0] = new String[] {majorId,
				bean.getMajorName(), bean.getMajorCode(), bean.getRemark(),
				"1", bean.getOptId(), bean.getOptName() };

		Object[] obj = LogUtil
				.getOptParam(DBFacade.getInstance().getID(), bean.getOptId(),
						bean.getOptName(), "检测机构专业类别信息添加", "专业列别编号："+majorId+" ,专业类别名称："+bean.getMajorName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}

	public Map<String, Object> editZhLabMajor(ZhLabMajor bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = " update zh_lab_major set major_name=? , major_code=? , remark=? , is_valid=? ,  opt_id=? , opt_name=? , opt_time=now() "
				+ " where major_id=?";
		params[0] = new String[] { bean.getMajorName(), bean.getMajorCode(), bean.getRemark(),
				bean.getIsValid(), bean.getOptId(), bean.getOptName() ,
				bean.getMajorId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检测机构专业类别信息修改",
				"专业列别编号："+bean.getMajorId()+" ,专业类别名称："+bean.getMajorName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delZhLabMajor(ZhLabMajor bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from zh_lab_major where major_id=?";
		params[0] = new String[] { bean.getMajorId() };
		Object[] obj = LogUtil
				.getOptParam(DBFacade.getInstance().getID(), bean.getOptId(),
						bean.getOptName(), "检测机构专业类别信息删除", "专业列别编号："+bean.getMajorId());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getZhLabMajorById(String planId) throws Exception {
		String sql = "select major_id , major_name , major_code , remark , is_valid , "
				+ "  opt_id , opt_name , opt_time from zh_lab_major where major_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { planId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ZhLabMajor bean = new ZhLabMajor();
			bean.setMajorId(crs.getString("major_id"));
			bean.setMajorName(crs.getString("major_name"));
			bean.setMajorCode(crs.getString("major_code"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
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

	// 检测机构专业类别名称重复验证
	public boolean checkMajorName(String majorName) {
		String sql = "select count(*) from zh_lab_major where major_name = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { majorName });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	// 检测机构专业代码重复验证
	public boolean checkMajorCode(String majorCode) {
		String sql = "select count(*) from zh_lab_major where major_code = ?";
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { majorCode });
			if (count > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 修改时检测机构专业类别名称重复验证
	public boolean editCheckMajorName(String majorName, String majorId) {
		String sql = "select count(*) from zh_lab_major where major_name = ? and major_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { majorName, majorId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	// 修改时检测机构专业类别代码重复验证
		public boolean editCheckMajorCode(String majorCode, String majorId) {
			String sql = "select count(*) from zh_lab_major where major_code = ? and major_id != ?";
			try {
				long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
						new String[] { majorCode, majorId });
				if (obj > 0) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
}
