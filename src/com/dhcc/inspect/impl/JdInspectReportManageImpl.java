package com.dhcc.inspect.impl;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.LogUtil;

public class JdInspectReportManageImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getJdInspectReportManageList(JdInspectReport jdInspectReport, String curPage, String perNumber,
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

		String sql = "select report_type,report_id,irp_title,report_path from jd_inspect_report ";

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
			page.setSidx("report_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectReport> list = new ArrayList<JdInspectReport>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			
//			bean.setTranId(crs.getString("tran_id"));
//			bean.setBillId(crs.getString("bill_id"));
//			bean.setSampleNo(crs.getString("sample_no"));
//			bean.setInspectId(crs.getString("inspect_id"));
			bean.setIrpTitle(crs.getString("irp_title"));
			bean.setReportType(crs.getString("report_type"));
//			bean.setVerificationCode(crs.getString("verification_code"));
//			bean.setInspectType(crs.getString("inspect_type"));
//			bean.setIsAssigned(crs.getString("issue_status"));
//			bean.setCheckStatus(crs.getString("check_status"));
			bean.setReportId(crs.getString("report_id"));
//			bean.setIsValid(crs.getString("is_valid"));
//			bean.setEnterpriseName(crs.getString("enterprise_name"));	
//			bean.setCompanyName(crs.getString("handle_dept_name"));
//			bean.setItaTitle(crs.getString("ita_title"));
			bean.setReportPath(crs.getString("report_path"));
//			bean.setCurrentNodeId(crs.getString("current_node_id"));
//			bean.setIsSubmit(crs.getString("is_submit"));
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
	
	public Map<String, Object> uploadReport(JdInspectReport bean){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[1];
		String[][] params = new String[1][];
		System.out.println(bean.getReportPath());
		sqls[0] = "update jd_inspect_report set report_path=?,report_type=? where report_id = ? ";
		params[0] = new String[] { bean.getReportPath().replace("\\", "\\\\"),bean.getReportType(),bean.getReportId() };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	public Map<String, Object> tovoidReport(JdInspectReport bean){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[1];
		String[][] params = new String[1][];
		sqls[0] = "update jd_inspect_report set abolish_status='1',abolish_reason=?,abolish_opt_id=?,abolish_opt_name=?,abolish_opt_time=now() where report_id = ? ";
		params[0] = new String[] { bean.getAbolishReason(),bean.getAbolishOptId(),bean.getAbolishOptName(), bean.getReportId() };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
}
