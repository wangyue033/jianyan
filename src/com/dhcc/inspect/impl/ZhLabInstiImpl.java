/**
 * 
 */
package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.ZhLabInsti;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-15    
 ***********************
 */
public class ZhLabInstiImpl {
	Page page = new Page();

	/** *******************
	* @return
	* 2017-2-16
	* 获得检测机构类别信息列表
	*/
	public String getMajorList() {
		List<ZhLabInsti> list = new ArrayList<ZhLabInsti>();
		String sql = "select major_id,major_name from zh_lab_major where is_valid=1";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				ZhLabInsti bean = new ZhLabInsti();
				bean.setMajorId(crs.getString("major_id"));
				bean.setMajorName(crs.getString("major_name"));				
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("list", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	/** *******************
	* @param labName
	* @return
	* 2017-2-16
	* 检测机构名称重复验证
	*/
	public boolean checkZhLabInstiName(String labName) {
		String sql="select count(*) from zh_lab where lab_name = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { labName });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @param bean
	* 2017-2-16
	* 添加检测机构基础信息
	*/
	public void addZhLabInsti(ZhLabInsti bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into zh_lab(lab_id,major_id,lab_name,office_tel,regist_address,fax_code," +
				"zip_code,belong_area,lab_address,cma_licence_no,cma_end_date,cal_licence_no," +
				"cal_end_date,corp_name,corp_tel,corp_mobile,corp_email,technical_name,technical_tel," +
				"technical_mobile,technical_email,quality_name,quality_tel,quality_mobile,quality_email," +
				"authorized_name,authorized_tel,authorized_mobile,authorized_email,linkman_name," +
				"linkman_tel,linkman_mobile,linkman_email) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		params[0] = new String[] { DBFacade.getInstance().getID(), bean.getMajorId(),
				bean.getLabName(), bean.getOfficeTel(), bean.getRegistAddress(),bean.getFaxCode(),
				bean.getZipCode(), bean.getBelongArea(), bean.getLabAddress(),bean.getCmaLicenceNo(),
				bean.getCmaEndDate(),bean.getCalLicenceNo(),bean.getCalEndDate(),bean.getCorpName(),
				bean.getCorpTel(),bean.getCorpMobile(),bean.getCorpEmail(),bean.getTechnicalName(),
				bean.getTechnicalTel(),bean.getTechnicalMobile(),bean.getTechnicalEmail(),bean.getQualityName(),
				bean.getQualityTel(),bean.getQualityMobile(),bean.getQualityEmail(),bean.getAuthorizedName(),
				bean.getAuthorizedTel(),bean.getAuthorizedMobile(),bean.getAuthorizedEmail(),bean.getLinkmanName(),
				bean.getLinkmanTel(),bean.getLinkmanMobile(),bean.getAuthorizedEmail()};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"检测机构信息添加",
				"机构专业类别：" + bean.getMajorId() + ";检测机构名称："
						+ bean.getLabName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}

	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-2-16
	* *获取检测机构信息列表
	 * @throws SQLException ******************
	*/
	public String getZhLabInstiList(ZhLabInsti bean1,String curPage, String perNumber,
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
		String sql = "select a.lab_id,a.major_id,a.lab_name,a.office_tel,a.regist_address,a.fax_code,"
				+ "a.zip_code,a.belong_area,a.lab_address,a.cma_licence_no,a.cma_end_date,a.cal_licence_no,"
				+ "a.cal_end_date,a.corp_name,a.corp_tel,a.corp_mobile,a.corp_email,a.technical_name,a.technical_tel,"
				+ "a.technical_mobile,a.technical_email,a.quality_name,a.quality_tel,a.quality_mobile,a.quality_email,"
				+ "a.authorized_name,a.authorized_tel,a.authorized_mobile,a.authorized_email,a.linkman_name,"
				+ "a.linkman_tel,a.linkman_mobile,a.linkman_email,ifnull(b.major_name,'') major_name "
				+ "from zh_lab a left join zh_lab_major b "
				+ "on a.major_id = b.major_id where 1 = 1 ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		if(bean1.getMajorId() != null &&  !"".equals(bean1.getMajorId())){
			sql = sql + " and a.major_id like '" + bean1.getMajorId() + "%' ";
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("a.lab_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<ZhLabInsti> list = new ArrayList<ZhLabInsti>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			ZhLabInsti bean = new ZhLabInsti();
			bean.setLabId(crs.getString("lab_id"));
			bean.setMajorId(crs.getString("major_id"));
			bean.setMajorName(crs.getString("major_name"));
			bean.setLabName(crs.getString("lab_name"));
			bean.setOfficeTel(crs.getString("office_tel"));
			bean.setRegistAddress(crs.getString("regist_address"));
			bean.setFaxCode(crs.getString("fax_code"));
			bean.setZipCode(crs.getString("zip_code"));
			bean.setBelongArea(crs.getString("belong_area"));
			bean.setLabAddress(crs.getString("lab_address"));
			bean.setCmaLicenceNo(crs.getString("cma_licence_no"));
			bean.setCmaEndDate(crs.getString("cma_end_date"));
			bean.setCalLicenceNo(crs.getString("cal_licence_no"));
			bean.setCalEndDate(crs.getString("cal_end_date"));
			bean.setCorpName(crs.getString("corp_name"));
			bean.setCorpTel(crs.getString("corp_tel"));
			bean.setCorpMobile(crs.getString("corp_mobile"));
			bean.setCorpEmail(crs.getString("corp_email"));
			bean.setTechnicalName(crs.getString("technical_name"));
			bean.setTechnicalTel(crs.getString("technical_tel"));
			bean.setTechnicalMobile(crs.getString("technical_mobile"));
			bean.setTechnicalEmail(crs.getString("technical_email"));
			bean.setQualityName(crs.getString("quality_name"));
			bean.setQualityTel(crs.getString("quality_tel"));
			bean.setQualityMobile(crs.getString("quality_mobile"));
			bean.setQualityEmail(crs.getString("quality_email"));
			bean.setAuthorizedName(crs.getString("authorized_name"));
			bean.setAuthorizedTel(crs.getString("authorized_tel"));
			bean.setAuthorizedMobile(crs.getString("authorized_mobile"));
			bean.setAuthorizedEmail(crs.getString("authorized_email"));
			bean.setLinkmanName(crs.getString("linkman_name"));
			bean.setLinkmanTel(crs.getString("linkman_tel"));
			bean.setLinkmanMobile(crs.getString("linkman_mobile"));
			bean.setLinkmanEmail(crs.getString("linkman_email"));
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

	/** *******************
	* @param bean
	* @return
	* 2017-2-16
	* 删除检测机构信息
	*/
	public Map<String, Object> delZhLabInsti(ZhLabInsti bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from zh_lab where lab_id like ?";
		params[0] = new String[] { bean.getLabId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检测机构信息删除",
				"机构信息ID：" + bean.getLabId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param labId
	* @return
	* 2017-2-16
	* 获取检测机构信息详情
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public Map<String, Object> getZhLabInstiById(String labId) throws SQLException, ParseException {
		String sql = "select a.lab_id,a.major_id,a.lab_name,a.office_tel,a.regist_address,a.fax_code,"
				+ "a.zip_code,a.belong_area,a.lab_address,a.cma_licence_no,a.cma_end_date,a.cal_licence_no,"
				+ "a.cal_end_date,a.corp_name,a.corp_tel,a.corp_mobile,a.corp_email,a.technical_name,a.technical_tel,"
				+ "a.technical_mobile,a.technical_email,a.quality_name,a.quality_tel,a.quality_mobile,a.quality_email,"
				+ "a.authorized_name,a.authorized_tel,a.authorized_mobile,a.authorized_email,a.linkman_name,"
				+ "a.linkman_tel,a.linkman_mobile,a.linkman_email,ifnull(b.major_name,'') major_name "
				+ "from zh_lab a left join zh_lab_major b "
				+ "on a.major_id = b.major_id where a.lab_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { labId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ZhLabInsti zhLabInsti = new ZhLabInsti();
			zhLabInsti.setLabId(crs.getString("lab_id"));
			zhLabInsti.setMajorId(crs.getString("major_id"));
			zhLabInsti.setMajorName(crs.getString("major_name"));
			zhLabInsti.setLabName(crs.getString("lab_name"));
			zhLabInsti.setOfficeTel(crs.getString("office_tel"));
			zhLabInsti.setRegistAddress(crs.getString("regist_address"));
			zhLabInsti.setFaxCode(crs.getString("fax_code"));
			zhLabInsti.setZipCode(crs.getString("zip_code"));
			zhLabInsti.setBelongArea(crs.getString("belong_area"));
			zhLabInsti.setLabAddress(crs.getString("lab_address"));
			zhLabInsti.setCmaLicenceNo(crs.getString("cma_licence_no"));
			zhLabInsti.setCmaEndDate(crs.getString("cma_end_date"));
			zhLabInsti.setCalLicenceNo(crs.getString("cal_licence_no"));
			zhLabInsti.setCalEndDate(crs.getString("cal_end_date"));
			zhLabInsti.setCorpName(crs.getString("corp_name"));
			zhLabInsti.setCorpTel(crs.getString("corp_tel"));
			zhLabInsti.setCorpMobile(crs.getString("corp_mobile"));
			zhLabInsti.setCorpEmail(crs.getString("corp_email"));
			zhLabInsti.setTechnicalName(crs.getString("technical_name"));
			zhLabInsti.setTechnicalTel(crs.getString("technical_tel"));
			zhLabInsti.setTechnicalMobile(crs.getString("technical_mobile"));
			zhLabInsti.setTechnicalEmail(crs.getString("technical_email"));
			zhLabInsti.setQualityName(crs.getString("quality_name"));
			zhLabInsti.setQualityTel(crs.getString("quality_tel"));
			zhLabInsti.setQualityMobile(crs.getString("quality_mobile"));
			zhLabInsti.setQualityEmail(crs.getString("quality_email"));
			zhLabInsti.setAuthorizedName(crs.getString("authorized_name"));
			zhLabInsti.setAuthorizedTel(crs.getString("authorized_tel"));
			zhLabInsti.setAuthorizedMobile(crs.getString("authorized_mobile"));
			zhLabInsti.setAuthorizedEmail(crs.getString("authorized_email"));
			zhLabInsti.setLinkmanName(crs.getString("linkman_name"));
			zhLabInsti.setLinkmanTel(crs.getString("linkman_tel"));
			zhLabInsti.setLinkmanMobile(crs.getString("linkman_mobile"));
			zhLabInsti.setLinkmanEmail(crs.getString("linkman_email"));
			map.put("record", zhLabInsti);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}

	/** *******************
	* @param labName
	* @param labId
	* @return
	* 2017-2-16
	* 修改时验证检测机构名称重复
	*/
	public boolean checkEditLabName(String labName, String labId) {
		String sql="select count(*) from zh_lab where lab_name = ? and lab_id != ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { labName,labId});
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-16
	* 修改检测机构基础信息
	*/
	public Map<String, Object> editZhLabInsti(ZhLabInsti bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update zh_lab set major_id=?,lab_name=?,office_tel=?,regist_address=?,fax_code=?,"
				+ "zip_code=?,belong_area=?,lab_address=?,cma_licence_no=?,cma_end_date=?,cal_licence_no=?,"
				+ "cal_end_date=?,corp_name=?,corp_tel=?,corp_mobile=?,corp_email=?,technical_name=?,technical_tel=?,"
				+ "technical_mobile=?,technical_email=?,quality_name=?,quality_tel=?,quality_mobile=?,quality_email=?,"
				+ "authorized_name=?,authorized_tel=?,authorized_mobile=?,authorized_email=?,linkman_name=?,"
				+ "linkman_tel=?,linkman_mobile=?,linkman_email=? where lab_id=?";
		params[0] = new String[] { bean.getMajorId(), bean.getLabName(),bean.getOfficeTel(),bean.getRegistAddress(),
				bean.getFaxCode(), bean.getZipCode(), bean.getBelongArea(),bean.getLabAddress(),bean.getCmaLicenceNo(),
				bean.getCmaEndDate(), bean.getCalLicenceNo(),bean.getCalEndDate(),bean.getCorpName(),bean.getCorpTel(),
				bean.getCorpMobile(),bean.getCorpEmail(),bean.getTechnicalName(),bean.getTechnicalTel(),bean.getTechnicalMobile(),
				bean.getTechnicalEmail(),bean.getQualityName(),bean.getQualityTel(),bean.getQualityMobile(),
				bean.getQualityEmail(),bean.getAuthorizedName(),bean.getAuthorizedTel(),bean.getAuthorizedMobile(),
				bean.getAuthorizedEmail(),bean.getLinkmanName(),bean.getLinkmanTel(),bean.getLinkmanMobile(),
				bean.getLinkmanEmail(),bean.getLabId()};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检测机构信息修改",
				"机构信息ID：" + bean.getLabId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @return
	* 2017-3-20
	* 获取检测机构专业类别信息
	*/
	public List<ZTreeBean> getMajorAllRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select major_id,major_name,-1 parent_id from zh_lab_major where is_valid = '1' ";
		
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("major_id"));
				bean.setName(crs.getString("major_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无检测机构类别信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}
}
