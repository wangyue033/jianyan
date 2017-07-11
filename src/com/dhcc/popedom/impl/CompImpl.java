package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.Comp;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;

public class CompImpl {
	Page page = new Page();

	public String getCompList(String curPage, String perNumber,
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
		String sql = "select a.company_id,a.description,a.company_name,short_name,charge,a.working_range,"
				+ "a.build_date,a.comp_address,a.mail_code,a.phone,a.fax,a.state,a.comp_type,a.is_valid,a.parent_id,a.area_id,"
				+ "b.area_name "
				+ "from hr_company a left join jd_area_info b "
				+ "on a.area_id = b.area_id where is_root = 1 ";

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
			page.setSidx("a.company_id");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<Comp> list = new ArrayList<Comp>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			Comp bean = new Comp();
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setCharge(crs.getString("charge"));
			bean.setWorkingRange(crs.getString("working_range"));
			bean.setBuildDate(crs.getString("build_date"));
			bean.setCompAddress(crs.getString("comp_address"));
			bean.setMailCode(crs.getString("mail_code"));
			bean.setPhone(crs.getString("phone"));
			bean.setFax(crs.getString("fax"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setState(crs.getString("state"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setDescription(crs.getString("description"));
			bean.setAreaId(crs.getString("area_id"));
			bean.setAreaName(crs.getString("area_name"));
			bean.setCompType(crs.getString("comp_type"));
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

	public List<Comp> getComList(PageBean pBean) throws Exception {
		List<Comp> list = new ArrayList<Comp>();
		CachedRowSetImpl crs = pBean.getCrs();
		while (crs.next()) {
			Comp bean = new Comp();
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setCharge(crs.getString("charge"));
			bean.setWorkingRange(crs.getString("working_range"));
			bean.setBuildDate(crs.getString("build_date"));
			bean.setCompAddress(crs.getString("comp_address"));
			bean.setMailCode(crs.getString("mail_code"));
			bean.setPhone(crs.getString("phone"));
			bean.setFax(crs.getString("fax"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setParentName(crs.getString("parent_name"));
			bean.setState(crs.getString("state"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setDescription(crs.getString("description"));
			list.add(bean);
		}
		return list;
	}

	public void addComp(Comp bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into hr_company(company_id,company_name,short_name,charge,working_range,build_date,comp_address,phone,is_valid,comp_type,area_id,sign_path1,sign_path2,sign_path3,opt_id,opt_name,opt_time,is_root) values (?,?,?,?,?,?,?,?,1,?,?,?,?,?,?,?,now(),1) ";
		params[0] = new String[] { bean.getCompanyId(), bean.getCompanyName(),bean.getShortName(),bean.getCharge(), bean.getWorkingRange(), bean.getBuildDate(),
				bean.getCompAddress(),bean.getPhone(), bean.getCompType(), bean.getAreaId(),bean.getSignPath1(),bean.getSignPath2(),bean.getSignPath3(),bean.getOptId(),bean.getOptName()};
		//System.out.println(bean);//1
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"组织机构添加",
				"组织机构编号：" + bean.getCompanyId() + ";组织机构名称："
						+ bean.getCompanyName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}

	public String getCompId(String pId) {
		String cId = "";
		String sql = "select count(*) + 1 from hr_company where company_id like '"+pId+"%' ";
		int item = Integer.parseInt(DBFacade.getInstance().getValueBySql(sql,null).toString());
		if (item < 10) {
			cId = pId + "0" + item;
		} else {
			cId = pId + item;
		}
		return cId;
	}

	public Map<String, Object> delComp(Comp bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from hr_company where company_id like ?";
		params[0] = new String[] { bean.getCompanyId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "组织机构删除",
				"组织机构编号：" + bean.getCompanyId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public int[] setCompList(String perNumber, String gotoPage, Page page,
			Comp bean, List<Comp> dataRows) {
		page.setRows(Integer.parseInt(perNumber));
		page.setPage(Integer.parseInt(gotoPage));
		String sql = "select a.company_id,a.description,a.company_name,ifnull(a.charge,'') charge,a.working_range,"
				+ "a.build_date,a.comp_address,a.mail_code,a.phone,a.fax,a.state,a.is_valid,a.parent_id,"
				+ "ifnull(b.company_name,'') parent_name "
				+ "from hr_company a left join hr_company b "
				+ "on a.parent_id = b.company_id where 1 = 1 ";
		if (bean.getSearchField() != null && !"".equals(bean.getSearchField())) {
			sql = sql + " and " + bean.getSearchField() + " like '%"
					+ bean.getSearchValue() + "%' ";
		}
		String sidx = bean.getOrderByField();
		String sord = bean.getOrderByType();
		if (sidx == null || "".equals(sidx)) {
			sidx = "company_id";
			bean.setOrderByField(sidx);
		}
		if (sord == null || "".equals(sord)) {
			sord = "asc";
			bean.setOrderByType(sord);
		}
		page.setSidx(sidx);
		page.setSord(sord);
		PageBean pageData = new PageBean(sql, page);
		int[] record = new int[3];
		try {
			dataRows.addAll(getComList(pageData));
			record[0] = pageData.getPageAmount();
			record[1] = pageData.getPageNo();
			record[2] = pageData.getItemAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}

	public Map<String, Object> getCompById(String companyId) throws Exception {
		String sql = "select a.company_id,a.company_name,short_name,a.charge,a.working_range,"
				+ "a.build_date,a.comp_address,a.phone,a.fax,a.description,a.is_valid,a.comp_type,a.area_id,b.area_name," +
				"a.opt_id,a.opt_name,a.opt_time,a.sign_path1,a.sign_path2,a.sign_path3 "
				+ "from hr_company a left join jd_area_info b on a.area_id=b.area_id where a.company_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { companyId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			Comp comp = new Comp();
			comp.setCompanyId(crs.getString("company_id"));
			comp.setCompanyName(crs.getString("company_name"));
			comp.setShortName(crs.getString("short_name"));
			comp.setCharge(crs.getString("charge"));
			comp.setWorkingRange(crs.getString("working_range"));
			comp.setBuildDate(crs.getString("build_date"));
			comp.setCompAddress(crs.getString("comp_address"));
			comp.setPhone(crs.getString("phone"));
			comp.setFax(crs.getString("fax"));
			comp.setIsValid(crs.getString("is_valid"));
			comp.setDescription(crs.getString("description"));
			comp.setCompType(crs.getString("comp_type"));
			comp.setAreaId(crs.getString("area_id"));
			comp.setAreaName(crs.getString("area_name"));
			comp.setOptId(crs.getString("opt_id"));
			comp.setOptName(crs.getString("opt_name"));
			comp.setOptTime(crs.getString("opt_time"));
			comp.setSignPath1(crs.getString("sign_path1"));
			comp.setSignPath2(crs.getString("sign_path2"));
			comp.setSignPath3(crs.getString("sign_path3"));
			map.put("record", comp);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}

	public Map<String, Object> editComp(Comp bean) throws Exception {
		String isValid = bean.getIsValid();
		if (isValid.equals("0")) {
			String[] sqls = new String[3];
			String[][] params = new String[3][];
			sqls[0] = "update hr_company set company_name=?,short_name=?,charge=?,phone=?,fax=?,build_date=?,working_range=?,"
					+ "description=?,is_valid=?,comp_address=?,sign_path1=?,sign_path2=?,sign_path3=? where company_id=?";
			params[0] = new String[] { bean.getCompanyName(),bean.getShortName(), bean.getCharge(),
					bean.getPhone(), bean.getFax(), bean.getBuildDate(),
					bean.getWorkingRange(), bean.getDescription(),
					bean.getIsValid(),bean.getCompAddress(),bean.getSignPath1(),bean.getSignPath2(),bean.getSignPath3(),bean.getCompanyId() };

			sqls[1] = "update hr_company set is_valid='0' where company_id like ?";
			params[1] = new String[] { bean.getCompanyId() + "%" };

			sqls[2] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
					+ "values(?,?,?,?,now(),?,?)";
			params[2] = new String[] { DBFacade.getInstance().getID(),
					bean.getOptId(), bean.getOptName(), "组织机构修改",
					"组织机构编号：" + bean.getCompanyId(), "1" };
			DBFacade.getInstance().execute(sqls, params);
		} else {
			String[] sqls = new String[2];
			String[][] params = new String[2][];
			sqls[0] = "update hr_company set company_name=?,short_name=?,charge=?,phone=?,fax=?,build_date=?,working_range=?,"
					+ "description=?,is_valid=?,comp_address=?,sign_path1=?,sign_path2=?,sign_path3=? where company_id=?";
			params[0] = new String[] { bean.getCompanyName(),bean.getShortName(), bean.getCharge(),
					bean.getPhone(), bean.getFax(), bean.getBuildDate(),
					bean.getWorkingRange(), bean.getDescription(),
					bean.getIsValid(),bean.getCompAddress(),bean.getSignPath1(),bean.getSignPath2(),bean.getSignPath3(),bean.getCompanyId() };
			sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
					+ "values(?,?,?,?,now(),?,?)";
			params[1] = new String[] { DBFacade.getInstance().getID(),
					bean.getOptId(), bean.getOptName(), "组织机构修改",
					"组织机构编号：" + bean.getCompanyId(), "1" };
			DBFacade.getInstance().execute(sqls, params);
		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;

	}
	
//职位名称重复验证
	public boolean checkCompName(String compName) throws SQLException{
		String sql="select count(*) from hr_company where company_name = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { compName });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			return true;
	}

	public List<ZTreeBean> getCompTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,-1 parent_id from hr_company where is_valid = '1' and is_root='1' order by company_id desc ";

		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setRemark("1");
				treeList.add(bean);
			}
			
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无组织机构信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

	/** 
	* @param companyId
	* @return
	* 2017-3-30
	* 添加组织机构信息时验证机构编号的重复
	*/
	public boolean checkCompId(String companyId) {
		String sql="select count(*) from hr_company where company_id = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { companyId });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			return true;
	}

}
