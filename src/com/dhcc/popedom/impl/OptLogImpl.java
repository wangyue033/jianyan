package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.OptLog;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
/**
 * 
 * @author WYH
 *
 */
public class OptLogImpl {
	Page page = new Page();

	public String getOptLogList(String curPage,
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
		String sql = "select log_id,opt_id,opt_name,opt_module,is_valid,opt_time,remark "
				+ "from opt_log where 1=1";
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
			page.setSidx("log_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<OptLog> list = new ArrayList<OptLog>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			OptLog bean = new OptLog();
			bean.setLogId(crs.getString("log_id"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptModule(crs.getString("opt_module"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter.format(formatter
					.parse(optTime));
			bean.setOptTime(time);
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
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
