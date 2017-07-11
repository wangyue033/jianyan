package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.UserLog;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
/**
 * 
 * @author WYH
 *
 */
public class UserLogImpl {
	Page page = new Page();

	public String getUserLogList(String curPage,
			String perNumber, String orderByField,
			String orderBySort, String searchField,
			String searchValue) throws SQLException,
			ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select log_id,serial_id,user_name,user_id,log_ip,log_type,log_status,opt_time "
				+ "from hr_user_log where 1=1";
		if (searchField != null
				&& !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField
					+ " like '%" + searchValue + "%' ";
		}
		if (orderByField != null
				&& !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("serial_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<UserLog> list = new ArrayList<UserLog>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			UserLog bean = new UserLog();
			bean.setLogId(crs.getString("log_id"));
			bean.setSerialId(crs.getString("serial_id"));
			bean.setUserId(crs.getString("user_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setLogIP(crs.getString("log_ip"));
			bean.setLogType(crs.getString("log_type"));
			bean.setLogStatus(crs.getString("log_status"));
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

}
