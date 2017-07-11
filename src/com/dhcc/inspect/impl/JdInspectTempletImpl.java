package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectTemplet;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.LogUtil;

public class JdInspectTempletImpl {

	Page page = new Page();

	public String getJdInspectTempletList(JdInspectTemplet jdInspectTemplet, String curPage, String perNumber,
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
		String sql = "SELECT a.templet_id,a.bill_id,b.ssim_title,a.opt_id,a.opt_name,a.opt_time,a.templet_name,a.templet_content" +
				" FROM jd_inspect_templet a, JD_SAMPLE_BILL b WHERE a.bill_id = b.bill_id and 1 = 1";

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
			// TODO: handle exception
			page.setSidx("templet_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectTemplet> list = new ArrayList<JdInspectTemplet>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectTemplet bean = new JdInspectTemplet();
			
			bean.setTempletId(crs.getString("templet_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			bean.setTempletName(crs.getString("templet_name"));
			bean.setTempletContent(crs.getString("templet_content"));
			
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
	
	public Map<String, Object> delJdInspectTemplet(JdInspectTemplet bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = "delete from JD_INSPECT_TEMPLET where templet_id=? ";
//		sqls[0] = "update jd_inspect set is_valid=? where inspect_id = ? ";
		params[0] = new String[] { bean.getTempletId() };
//		params[0] = new String[] { "0", bean.getInspectId() + "%"};

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽样检验模板删除",
				"{{delJdInspectTemplet}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	public void addJdInspectTemplet(JdInspectTemplet bean) throws Exception {
		
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		
		sqls[1] = "INSERT INTO JD_INSPECT_TEMPLET(templet_id,bill_id,opt_id,opt_name,templet_name,templet_content,opt_time) " +
				"values (?,?,?,?,?,?,now())";

		params[1]  = new String[] { DBFacade.getInstance().getID(),
				bean.getBillId(), bean.getOptId(), bean.getOptName(),
				bean.getTempletName(), bean.getTempletContent() };

	
		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽样检验模板添加",
				"{{addJdInspectTemplet}}");
		sqls[0] = (String) obj[0];
		params[0] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> getJdInspectTempletById(String templetId) throws Exception {
		String sql = "SELECT a.templet_id, a.bill_id, a.opt_id, a.opt_name, a.opt_time, a.templet_name, a.templet_content," +
				" b.ssim_title, c.enterprise_name,	d.COMPANY_NAME FROM	jd_inspect_templet a, jd_sample_bill b," +
				" jd_enterprise_info c, hr_company d WHERE a.bill_id = b.bill_id AND b.ep_id = c.enterprise_id " +
				"AND b.comp_id = d.COMPANY_ID AND a.templet_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { templetId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdInspectTemplet bean = new JdInspectTemplet();
			
			bean.setTempletId(crs.getString("templet_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			bean.setTempletName(crs.getString("templet_name"));
			bean.setTempletContent(crs.getString("templet_content"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setCompanyName(crs.getString("company_name"));

			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}
	
	public Map<String, Object> editJdInspectTemplet(JdInspectTemplet bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = " update jd_inspect_templet set templet_name = ? , bill_id = ? , templet_content = ? , opt_id = ? , opt_name = ? , opt_time = now() where templet_id = ? ";
		params[0] = new String[] { bean.getTempletName(), bean.getBillId(), bean.getTempletContent(), bean.getOptId(), bean.getOptName(), bean.getTempletId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽样检验模板信息修改",
				"{{editJdInspectTemplet}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
	/**
	 * 树形结构
	 *@return List<ZTreeBean>
	 */
	public List<ZTreeBean> getTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="SELECT a.bill_id,a.ssim_title,a.ep_id,a.comp_id from JD_SAMPLE_BILL a WHERE a.check_status = '3' and a.is_valid = '1' order by a.bill_id ";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				String sql1 = "select count(*) from JD_INSPECT_TEMPLET where bill_id = ? ";
				long count = (Long) DBFacade.getInstance().getValueBySql(sql1,
						new String[] { crs.getString("bill_id") });
				if (count > 0) {
					continue;
				}
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("bill_id"));
				bean.setName(crs.getString("ssim_title"));
				bean.setDepth("1");
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	/**
	 * @return info
	 * @throws SQLException 
	 */
	public String getInfo (String billId) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdInspectTemplet> list = new ArrayList<JdInspectTemplet>();
		
		String sql = "SELECT a.bill_id,a.ssim_title,b.enterprise_name,c.COMPANY_NAME from jd_sample_bill a, jd_enterprise_info b , hr_company c WHERE a.ep_id = b.enterprise_id AND a.comp_id = c.COMPANY_ID AND a.bill_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{billId});
		while (crs.next()) {
			JdInspectTemplet bean = new JdInspectTemplet();
			
			bean.setBillId(crs.getString("bill_id"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setCompanyName(crs.getString("COMPANY_NAME"));
			
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	public boolean checkPlanNo(String templetId, String templetName, int flag) {
		String sql = "select count(*) from jd_inspect_templet where templet_name = '" + templetName+"'";
		if (flag == 1) {
			sql += " and templet_id != '" + templetId+"'";
		}
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql, null);
			if (count>0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean checkName (String templetName) {
		String sql = "select count(*) from jd_inspect_templet where templet_name = ?";
		long count = (Long) DBFacade.getInstance().getValueBySql(sql,
				new String[] { templetName });
		if (count>0) {
			return false;
		}
		return true;
	}
	
}
