package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.domain.JdSampleBase;
import com.dhcc.inspect.domain.JdSampleBill;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;
import framework.dhcc.utils.LogUtil;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleShowBillImpl {
	Page page = new Page();
	
	//新写，查看抽样单详情
	public Map<String, Object> getBillCount(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String OptId,String sampleId) throws SQLException{
		
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql = "select a.sample_no,a.product_name,a.is_inspect from jd_sample_bill a,jd_sample b where a.sample_id=b.sample_id and b.sample_id="+ sampleId+" and a.check_status='3'";
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
			page.setSidx("a.sample_no");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		//CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{sampleId});
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductName(crs.getString("product_name"));
			bean.setIsInspect(crs.getString("is_inspect"));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		map.put("totalPage", "" + pageData.getPageAmount());
		map.put("curPage", "" + pageData.getPageNo());
		map.put("totalRecord", "" + pageData.getItemAmount());
		return map;
	}
}
