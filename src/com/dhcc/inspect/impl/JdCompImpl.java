package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdComp;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;

/***********************
 * @author wangxp
 * @version 1.0
 * @created 2017-2-14
 *********************** 
 */
public class JdCompImpl {
	Page page = new Page();

	/**
	 * *******************
	 * 
	 * @param enterpriseName
	 * @return 2017-2-14 企业名称重复验证
	 */
	public boolean checkJdCompName(String enterpriseName) {
		String sql = "select count(*) from jd_enterprise_info where enterprise_name = ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { enterpriseName });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * *******************
	 * 
	 * @param bean
	 *            2017-2-14 添加企业基础信息
	 */
	public void addJdComp(JdComp bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into jd_enterprise_info(enterprise_id,area_id,enterprise_name,initial_name,"
				+ "org_code,address,domicile,legal_rep,telephone,mobile,reg_capital,"
				+ "build_date,ep_scale,is_scale,economy_type,is_valid,remark,ep_status,is_prod,"
				+ "is_elect,year_value,org_id,org_name,opt_id,opt_name,opt_time,modify_opt_time) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',?,?,?,?,?,?,?,?,?,now(),now()) ";
		params[0] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getAreaId(),
				bean.getEnterpriseName(),
				CnToPiny.getInstance().getPinYinHeadChar(
						bean.getEnterpriseName()), bean.getOrgCode(),
				bean.getAddress(), bean.getDomicile(), bean.getLegalRep(),
				bean.getTelephone(), bean.getMobile(), bean.getRegCapital(),
				bean.getBuildDate(), bean.getEpScale(), bean.getIsScale(),
				bean.getEconomyType(), bean.getRemark(), bean.getEpStatus(),
				bean.getIsProd(), bean.getIsElect(), bean.getYearValue(),
				bean.getOrgId(), bean.getOrgName(), bean.getOptId(),
				bean.getOptName() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"企业信息添加",
				"组织机构编号：" + bean.getOrgCode() + ";企业名称："
						+ bean.getEnterpriseName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}

	/**
	 * *******************
	 * 
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return 2017-2-14 获取企业信息列表
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String getJdCompList(JdComp bean1, String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String[] areaId = {};
		if (bean1.getAreaId() != "" && bean1.getAreaId() != null
				&& !("").equals(bean1.getAreaId())) {
			areaId = bean1.getAreaId().split(",");
		}

		String sql = "select enterprise_id,enterprise_name,ifnull(org_code,'') org_code,ifnull(ep_credit_code,'') ep_credit_code,"
				+ "license_number,address,business_scope,legal_rep,post_code,contacts,telephone,build_date,"
				+ "ep_scale,economy_type,is_valid,opt_id,opt_name,opt_time "
				+ "from jd_enterprise_info " + "where 1 = 1 ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		if (areaId.length == 1) {
			sql = sql + " and area_id like '" + bean1.getAreaId() + "%' ";
		}

		if (areaId.length > 1) {
			sql = sql + " and area_id like '" + areaId[0] + "%' ";
			for (int i = 1; i < areaId.length; i++) {
				sql = sql + " or area_id like '" + areaId[i] + "%' ";
			}
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("enterprise_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdComp> list = new ArrayList<JdComp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdComp bean = new JdComp();
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setOrgCode(crs.getString("org_code"));
			bean.setEpCreditCode(crs.getString("ep_credit_code"));
			bean.setLicenseNumber(crs.getString("license_number"));
			bean.setAddress(crs.getString("address"));
			bean.setBusinessScope(crs.getString("business_scope"));
			bean.setLegalRep(crs.getString("legal_rep"));
			bean.setPostCode(crs.getString("post_code"));
			bean.setContacts(crs.getString("contacts"));
			bean.setTelephone(crs.getString("telephone"));
			bean.setBuildDate(crs.getString("build_date"));
			bean.setEpScale(crs.getString("ep_scale"));
			bean.setEconomyType(crs.getString("economy_type"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			if (optTime != null) {
				String time = formatter.format(formatter.parse(optTime));
				bean.setOptTime(time);
			}
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

	/**
	 * *******************
	 * 
	 * @param bean
	 * @return 2017-2-14 删除企业信息
	 */
	public Map<String, Object> delJdComp(JdComp bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_enterprise_info where enterprise_id like ?";
		params[0] = new String[] { bean.getEnterpriseId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "企业信息删除",
				"企业信息ID：" + bean.getEnterpriseId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/**
	 * *******************
	 * 
	 * @param enterpriseId
	 * @return 2017-2-14 获取企业信息详情
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Map<String, Object> getJdCompById(String enterpriseId)
			throws SQLException, ParseException {
		String sql = "select a.enterprise_id,a.enterprise_name,ifnull(a.org_code,'') org_code,a.address,"
				+ "a.domicile,a.legal_rep,a.telephone,a.mobile,a.reg_capital,a.build_date,a.ep_scale,a.is_scale,"
				+ "a.economy_type,a.is_valid,a.remark,a.ep_status,a.is_prod,a.is_elect,a.year_value,a.org_id,"
				+ "a.org_name,a.opt_id,a.opt_name,a.opt_time,a.modify_opt_time,a.area_id,b.area_name "
				+ "from jd_enterprise_info a,jd_area_info b "
				+ "where a.enterprise_id=? and a.area_id = b.area_id ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { enterpriseId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdComp jdComp = new JdComp();
			jdComp.setEnterpriseId(crs.getString("enterprise_id"));
			jdComp.setEnterpriseName(crs.getString("enterprise_name"));
			jdComp.setOrgCode(crs.getString("org_code"));
			jdComp.setAddress(crs.getString("address"));
			jdComp.setDomicile(crs.getString("domicile"));
			jdComp.setLegalRep(crs.getString("legal_rep"));
			jdComp.setTelephone(crs.getString("telephone"));
			jdComp.setMobile(crs.getString("mobile"));
			jdComp.setRegCapital(crs.getString("reg_capital"));
			jdComp.setBuildDate(crs.getString("build_date"));
			jdComp.setEpScale(crs.getString("ep_scale"));
			jdComp.setIsScale(crs.getString("is_scale"));
			jdComp.setEconomyType(crs.getString("economy_type"));
			jdComp.setIsValid(crs.getString("is_valid"));
			jdComp.setRemark(crs.getString("remark"));
			jdComp.setEpStatus(crs.getString("ep_status"));
			jdComp.setIsProd(crs.getString("is_prod"));
			jdComp.setIsElect(crs.getString("is_elect"));
			jdComp.setYearValue(crs.getString("year_value"));
			jdComp.setOrgId(crs.getString("org_id"));
			jdComp.setOrgName(crs.getString("org_name"));
			jdComp.setOptId(crs.getString("opt_id"));
			jdComp.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			if(optTime!=null){
			String time = formatter.format(formatter.parse(optTime));
			jdComp.setOptTime(time);
			}
			String optTime2 = crs.getString("modify_opt_time");
			String time2 = "";
			if (optTime2 != null && optTime2 != "") {
				time2 = formatter.format(formatter.parse(optTime2));
			}
			jdComp.setModifyOptTime(time2);

			jdComp.setAreaId(crs.getString("area_id"));
			jdComp.setAreaName(crs.getString("area_name"));
			map.put("record", jdComp);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	/**
	 * *******************
	 * 
	 * @param bean
	 * @return 2017-2-14 修改企业基础信息
	 */
	public Map<String, Object> editJdComp(JdComp bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_enterprise_info set enterprise_name=?,initial_name=?,area_id=?,org_code=?,"
				+ "address=?,domicile=?,legal_rep=?,telephone=?,mobile=?,reg_capital=?,"
				+ "build_date=?,ep_scale=?,is_scale=?,economy_type=?,is_valid=?,remark=?,"
				+ "ep_status=?,is_prod=?,is_elect=?,year_value=?,org_id=?,org_name=?,"
				+ "opt_id=?,opt_name=?,modify_opt_time=now() where enterprise_id=?";
		params[0] = new String[] {
				bean.getEnterpriseName(),
				CnToPiny.getInstance().getPinYinHeadChar(
						bean.getEnterpriseName()), bean.getAreaId(),
				bean.getOrgCode(), bean.getAddress(), bean.getDomicile(),
				bean.getLegalRep(), bean.getTelephone(), bean.getMobile(),
				bean.getRegCapital(), bean.getBuildDate(), bean.getEpScale(),
				bean.getIsScale(), bean.getEconomyType(), bean.getIsValid(),
				bean.getRemark(), bean.getEpStatus(), bean.getIsProd(),
				bean.getIsElect(), bean.getYearValue(), bean.getOrgId(),
				bean.getOrgName(), bean.getOptId(), bean.getOptName(),
				bean.getEnterpriseId() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "企业信息修改",
				"企业信息ID：" + bean.getEnterpriseId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/**
	 * *******************
	 * 
	 * @param enterpriseName
	 * @return 2017-2-14 修改时验证企业名称重复
	 */
	public boolean checkEditJdCompName(String enterpriseName,
			String enterpriseId) {
		String sql = "select count(*) from jd_enterprise_info where enterprise_name = ? and enterprise_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { enterpriseName, enterpriseId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * *******************
	 * 
	 * @return 2017-3-20 *******************
	 */
	public List<ZTreeBean> getAreaAllRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where 1 = '1' order by area_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("area_id"));
				bean.setName(crs.getString("area_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无区域信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

	/**
	 * *******************
	 * 
	 * @param enterpriseId
	 * @return 2017-3-24 获取该企业的所有产品信息
	 */
	public List<ZTreeBean> getProductAllRoot(String enterpriseId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		// List<ZTreeBean> treeList2 = new ArrayList<ZTreeBean>();
		// String sql =
		// "select type_id,type_name,parent_id from jd_product_type where 1 = '1' order by type_id";
		String sql2 = "select product_id,product_name,type_id from jd_enterprise_product where enterprise_id = ? order by product_id";
		try {
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
					new String[] { enterpriseId });
			while (crs2.next()) {
				ZTreeBean bean2 = new ZTreeBean();
				bean2.setId(crs2.getString("product_id"));
				bean2.setName(crs2.getString("product_name"));
				bean2.setpId(crs2.getString("type_id"));
				treeList.add(bean2);
				// treeList2.add(bean2);
				if (!treeList.isEmpty()) {
					String sql = "select type_id,type_name,parent_id from jd_product_type where type_id = ? order by type_id";
					CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
							sql, new String[] { crs2.getString("type_id") });
					while (crs.next()) {
						ZTreeBean bean = new ZTreeBean();
						bean.setId(crs.getString("type_id"));
						bean.setName(crs.getString("type_name"));
						bean.setpId(crs.getString("parent_id"));
						treeList.add(bean);
					}
				}
			}
			/*
			 * if(!treeList2.isEmpty()){ CachedRowSetImpl crs =
			 * DBFacade.getInstance().getRowSet(sql, null); while (crs.next()) {
			 * ZTreeBean bean = new ZTreeBean();
			 * bean.setId(crs.getString("type_id"));
			 * bean.setName(crs.getString("type_name"));
			 * bean.setpId(crs.getString("parent_id")); treeList.add(bean); } }
			 */

		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {

			ZTreeBean bean2 = new ZTreeBean();
			bean2.setId("-1");
			bean2.setName("无企业产品信息");
			bean2.setpId("-1");
			treeList.add(bean2);
		}
		return treeList;
	}

	/**
	 * *******************
	 * 
	 * @param bean
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return 2017-4-20 *
	 * @throws ParseException
	 * @throws SQLException
	 *             ******************
	 */
	public String getJdCompList1(JdComp bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) throws SQLException,
			ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));

		String[] areaId = {};
		if (bean1.getAreaId() != "" && bean1.getAreaId() != null
				&& !("").equals(bean1.getAreaId())) {
			areaId = bean1.getAreaId().split(",");
		}
		String compId[] = {};
		if (bean1.getEnterpriseId() != "" && bean1.getEnterpriseId() != null
				&& !("").equals(bean1.getEnterpriseId())) {
			compId = bean1.getEnterpriseId().split(",");
		}

		String sql = "select enterprise_id,area_id,enterprise_name,is_valid from jd_enterprise_info "
				+ "where 1 = 1 ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		for (int i = 0; i < areaId.length; i++) {
			if (areaId[i].contains("0000")) {
				areaId[i] = areaId[i].substring(0, 2);
			} else if (areaId[i].contains("00")) {
				areaId[i] = areaId[i].substring(0, 4);
			}
			if (i == 0) {
				sql = sql + " and area_id like '" + areaId[i] + "%' ";
			} else {
				sql = sql + "or area_id like '" + areaId[i] + "%' ";
			}
		}

		/*
		 * if(areaId.length == 1){ sql = sql + " and area_id like '" +
		 * bean1.getAreaId() + "%' "; }
		 * 
		 * if(areaId.length>1){ sql = sql + " and area_id like '" + areaId[0] +
		 * "%' "; for(int i=1;i<areaId.length;i++){ sql = sql +
		 * " or area_id like '" + areaId[i] + "%' "; } }
		 */

		if (compId.length == 1) {
			sql = "select x.* from (" + sql
					+ ") x where x.enterprise_id like '"
					+ bean1.getEnterpriseId() + "%' ";
		}

		if (compId.length > 1) {
			sql = "select x.* from (" + sql
					+ ") x where x.enterprise_id like '" + compId[0] + "%' ";
			for (int i = 1; i < compId.length; i++) {
				sql = sql + " or x.enterprise_id like '" + compId[i] + "%' ";
			}
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("enterprise_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdComp> list = new ArrayList<JdComp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdComp bean = new JdComp();
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setIsValid(crs.getString("is_valid"));

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

	// 生产单位
	public String getJdCompAll(JdComp bean1, String curPage, String perNumber,
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

		String sql = "select enterprise_id,enterprise_name,org_code,telephone,address,area_id,is_valid "
				+ "from jd_enterprise_info where 1 = 1 ";
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
			page.setSidx("enterprise_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdComp> list = new ArrayList<JdComp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdComp bean = new JdComp();
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setOrgCode(crs.getString("org_code"));
			bean.setTelephone(crs.getString("telephone"));
			bean.setAddress(crs.getString("address"));
			bean.setAreaId(crs.getString("area_id"));
			bean.setIsValid(crs.getString("is_valid"));
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

	/**
	 * *******************
	 * 
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return 2017-2-14 获取企业信息列表
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String getJdCompJsonList(JdComp bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) throws SQLException,
			ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String condi = "";
		String areaId = bean1.getAreaId();
		if (areaId != null && !"".equals(areaId)) {
			String[] areaIds = {};
			if (areaId != null && !"".equals(areaId)) {
				areaIds = areaId.split(",");
			}
			condi = "and (";
			for (int i = 0; i < areaIds.length; i++) {
				if (areaIds[i].contains("0000")) {
					areaIds[i] = areaIds[i].substring(0, 2);
				} else if (areaIds[i].contains("00")) {
					areaIds[i] = areaIds[i].substring(0, 4);
				}
				if (i == 0) {
					condi = condi + " area_id like '" + areaIds[i] + "%' ";
				} else {
					condi = condi + "or area_id like '" + areaIds[i] + "%' ";
				}
			}
			condi = condi + ")";
		}
		String sql = "select enterprise_id,enterprise_name,org_code,telephone,address,area_id ,is_valid "
				+ "from jd_enterprise_info " + "where 1 = 1 ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		sql = sql + condi;
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("enterprise_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdComp> list = new ArrayList<JdComp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdComp bean = new JdComp();
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setOrgCode(crs.getString("org_code"));
			bean.setTelephone(crs.getString("telephone"));
			bean.setAddress(crs.getString("address"));
			bean.setAreaId(crs.getString("area_id"));
			bean.setIsValid(crs.getString("is_valid"));
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

	/**
	 * *******************
	 * 
	 * @param bean
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return 2017-5-11 *
	 * @throws SQLException
	 *             ******************
	 */
	public String getSampleCompList(JdComp bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		/* 先查询抽样任务下发时下发的抽样单位 */
		String sql1 = "select select_enterprise from jd_sample where sample_id=? ";
		CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,
				new String[] { bean1.getSampleId() });
		try {
			if (crs1.next()) {
				// 解析企业
				// JSon转成字符串
				String compId = "";
				JSONArray jArray2 = JSONArray
						.fromObject((crs1.getString("select_enterprise") == null || crs1
								.getString("select_enterprise").trim()
								.equals("")) ? "[]" : crs1
								.getString("select_enterprise"));
				for (Object obj : jArray2) {
					Map objs = JSONObject.fromObject(obj);
					compId += objs.get("id") + ",";
				}
				compId = compId.length() == 0 ? compId : compId.substring(0,
						compId.length() - 1);
				bean1.setCompId(compId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String condi = "";
		String compId = bean1.getCompId();
		if (compId != null && !"".equals(compId)) {
			String[] compIds = {};
			if (compId != null && !"".equals(compId)) {
				compIds = compId.split(",");
			}
			condi = "and (";
			for (int i = 0; i < compIds.length; i++) {
				if (i == 0) {
					condi = condi + " enterprise_id like '" + compIds[i]
							+ "%' ";
				} else {
					condi = condi + "or enterprise_id like '" + compIds[i]
							+ "%' ";
				}
			}
			condi = condi + ")";
		}
		String sql = "select enterprise_id,enterprise_name,org_code,telephone,address,area_id,is_valid "
				+ "from jd_enterprise_info where 1 = 1 ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		sql = sql + condi;
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("enterprise_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdComp> list = new ArrayList<JdComp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdComp bean = new JdComp();
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setOrgCode(crs.getString("org_code"));
			bean.setTelephone(crs.getString("telephone"));
			bean.setAddress(crs.getString("address"));
			bean.setAreaId(crs.getString("area_id"));
			bean.setIsValid(crs.getString("is_valid"));
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

	/**
	 * *******************
	 * 
	 * @param bean
	 */
	public void newAddsampleComp(JdComp bean) {
		/* 查询任务操作表的抽样企业 */
		String sampleEnt = "";
		String sql = "select select_enterprise from jd_plan_handle where task_id=? and handle_dept_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { bean.getTaskId(), bean.getOrgId() });
		try {
			if (crs.next()) {
				sampleEnt = crs.getString("select_enterprise");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sampleEnt = sampleEnt.substring(0, sampleEnt.length() - 1);
		sampleEnt = sampleEnt + "," + bean.getCompanyId() + "]";
		/* 查询抽样任务下发表的抽样企业 */
		String sampleEnt2 = "";
		String sql1 = "select select_enterprise from jd_sample where sample_id=? ";
		CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,
				new String[] { bean.getSampleId() });
		try {
			if (crs1.next()) {
				sampleEnt2 = crs1.getString("select_enterprise");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sampleEnt2 = sampleEnt2.substring(0, sampleEnt2.length() - 1);
		sampleEnt2 = sampleEnt2 + "," + bean.getCompanyId() + "]";

		String[] sqls = new String[3];
		String[][] params = new String[3][];

		sqls[0] = "update jd_plan_handle set select_enterprise=? where task_id=? and handle_dept_id=? ";
		params[0] = new String[] { sampleEnt, bean.getTaskId(), bean.getOrgId() };

		sqls[1] = "update jd_sample set select_enterprise=? where sample_id=? ";
		params[1] = new String[] { sampleEnt2, bean.getSampleId() };

		sqls[2] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[2] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "新增抽样企业",
				"抽检任务编号：" + bean.getTaskId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}
}
