package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.json.JSONObject;
import com.dhcc.popedom.domain.AuthLog;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;

public class AuthLogImpl {
	Page page = new Page();

	public String getAuthLogList(String curPage,
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
		String sql = "select a.role_name,a.log_type,a.serial_id,"
				+ " b.user_name,d.company_name,c.job_name,a.opt_id,"
				+ "a.opt_time from p_popedom_log a,hr_staff b,hr_job c,hr_company d where "
				+ "a.serial_id=b.serial_id and "
				+ "b.job_id=c.job_id and "
				+ "b.company_id=d.company_id";
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
			page.setSidx("a.serial_id");
			page.setSord("asc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<AuthLog> list = new ArrayList<AuthLog>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			AuthLog bean = new AuthLog();
			bean.setRoleName(crs.getString("role_name"));
			bean.setLogType(crs.getString("log_type"));
			bean.setSerialId(crs.getString("serial_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setCompanyName(crs
					.getString("company_name"));
			bean.setJobName(crs.getString("job_name"));
			bean.setOptId(crs.getString("opt_id"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter.format(formatter
					.parse(optTime));
			bean.setOptTime(time);
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
