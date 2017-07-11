/**
 * 
 */
package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.Comp;
import com.dhcc.popedom.domain.Dept;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-3-29    
 ***********************
 */
public class DeptImpl {
	
	Page page = new Page();
	
	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-3-29
	* *
	 * @throws SQLException ******************
	*/
	public String getDeptList(Dept bean2,String curPage, String perNumber,
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
		String sql = "select a.company_id,a.description,a.company_name,a.charge,a.working_range,"
				+ "a.build_date,a.mail_code,a.phone,a.fax,a.state,a.is_valid,a.parent_id "
				+ "from hr_company a where a.is_root = '0' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		if(bean2.getParentId() != null &&  !"".equals(bean2.getParentId())){
			sql = sql + " and a.parent_id like '" + bean2.getParentId() + "%'";
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

		List<Dept> list = new ArrayList<Dept>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			Dept bean = new Dept();
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setCharge(crs.getString("charge"));
			bean.setBuildDate(crs.getString("build_date"));
			bean.setMailCode(crs.getString("mail_code"));
			bean.setPhone(crs.getString("phone"));
			bean.setFax(crs.getString("fax"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setState(crs.getString("state"));
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

	/** *******************
	* @return
	* 2017-3-29
	* 选择是根节点的机构信息
	*/
	public List<ZTreeBean> getDeptTreeRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,-1 parent_id,area_id from hr_company where is_valid = '1' and is_root='1' order by company_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setRemark(crs.getString("area_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}

	/** *******************
	* @param companyName
	* @return
	* 2017-3-29
	* 添加部门时验证部门信息是否重复
	*/
	public boolean checkDeptName(String companyName) {
		String sql="select count(*) from hr_company where company_name = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { companyName });
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
	* 2017-3-29
	* 添加部门信息
	*/
	public void addDept(Dept bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into hr_company(company_id,company_name,charge,working_range,build_date,comp_address,mail_code,phone,fax," +
				"description,parent_id,is_valid,comp_type,opt_id,opt_name,opt_time,is_root) values (?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,now(),0) ";
		params[0] = new String[] { bean.getCompanyId(), bean.getCompanyName(),bean.getCharge(), bean.getWorkingRange(), bean.getBuildDate(),
				bean.getCompAddress(),bean.getMailCode(),bean.getPhone(),bean.getFax(),bean.getDescription(),
				bean.getParentId(),bean.getCompType(),bean.getOptId(),bean.getOptName()};
		//System.out.println(bean);//1
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"组织机构部门添加",
				"组织机构部门编号：" + bean.getCompanyId() + ";组织机构部门名称："
						+ bean.getCompanyName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}

	/** *******************
	* @param companyId
	* @return
	* 2017-3-29
	* 修改机构下的部门
	 * @throws SQLException 
	*/
	public Map<String, Object> getDeptById(String companyId) throws SQLException {
		String sql = "select a.company_id,a.company_name,ifnull(a.charge,'') charge,a.working_range,"
				+ "a.build_date,a.comp_address,a.mail_code,a.phone,a.fax,a.description,a.parent_id,b.company_name parent_name," +
				" a.is_valid,a.comp_type,a.area_id,c.area_name,a.opt_id,a.opt_name,a.opt_time "
				+ "from hr_company a left join hr_company b on a.parent_id=b.company_id left join jd_area_info c on a.area_id=c.area_id where a.company_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { companyId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			Comp comp = new Comp();
			comp.setCompanyId(crs.getString("company_id"));
			comp.setCompanyName(crs.getString("company_name"));
			comp.setCharge(crs.getString("charge"));
			comp.setWorkingRange(crs.getString("working_range"));
			comp.setBuildDate(crs.getString("build_date"));
			comp.setCompAddress(crs.getString("comp_address"));
			comp.setMailCode(crs.getString("mail_code"));
			comp.setPhone(crs.getString("phone"));
			comp.setFax(crs.getString("fax"));
			comp.setIsValid(crs.getString("is_valid"));
			comp.setDescription(crs.getString("description"));
			comp.setParentId(crs.getString("parent_id"));
			comp.setParentName(crs.getString("parent_name"));
			comp.setCompType(crs.getString("comp_type"));
			comp.setAreaId(crs.getString("area_id"));
			comp.setAreaName(crs.getString("area_name"));
			comp.setOptId(crs.getString("opt_id"));
			comp.setOptName(crs.getString("area_name"));
			comp.setOptTime(crs.getString("opt_time"));
			map.put("record", comp);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-3-29
	* 修改机构下的部门
	*/
	public Map<String, Object> editDept(Dept bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update hr_company set company_name=?,charge=?,mail_code=?,phone=?,fax=?,build_date=?,working_range=?,"
				+ "description=?,is_valid=?,comp_address=? where company_id = ?";
		params[0] = new String[] { bean.getCompanyName(), bean.getCharge(),bean.getMailCode(),
				bean.getPhone(), bean.getFax(), bean.getBuildDate(),
				bean.getWorkingRange(), bean.getDescription(),
				bean.getIsValid(),bean.getCompAddress(),bean.getCompanyId() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "组织机构部门修改",
				"组织机构部门编号：" + bean.getCompanyId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-3-29
	* 删除机构下的部门
	*/
	public Map<String, Object> delDept(Dept bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from hr_company where company_id like ?";
		params[0] = new String[] { bean.getCompanyId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "组织机构部门删除",
				"组织机构部门编号：" + bean.getCompanyId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param parentId
	* @return
	* 2017-4-10
	* 获取部门顺序号
	*/
	public String getDeptId(String pId) {
		String cId = "";
		String sql = "select count(*) from hr_company where company_id like '"+pId+"%' ";
		int item = Integer.parseInt(DBFacade.getInstance().getValueBySql(sql,null).toString());
		if (item < 10) {
			cId = pId + "0" + item;
		} else {
			cId = pId + item;
		}
		return cId;
	}
}
