package com.dhcc.inspect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.JdProductStandard;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdProductStandardImpl {
	Page page = new Page();

	public String getJdProductStandardList(JdProductStandard bean1,String curPage, String perNumber,
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
		String sql = "select a.standard_id,a.type_id,b.type_name,a.standard_no,a.standard_name,a.is_valid,a.opt_id,a.opt_name,a.opt_time " +
				" from jd_product_standard a,jd_product_type b where a.type_id = b.type_id  ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		if(bean1.getTypeId() != null &&  !"".equals(bean1.getTypeId())){
			sql = sql + " and a.type_id = '" + bean1.getTypeId() + "'";
		}
		
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("standard_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdProductStandard> list = new ArrayList<JdProductStandard>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdProductStandard bean = new JdProductStandard();
			bean.setStandardId(crs.getString("standard_id"));
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setStandardNo(crs.getString("standard_no"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			if(crs.getString("opt_time")!=null){
				bean.setOptTime(TimeUtil.getStringDate(crs.getDate("opt_time")));
				}else{
					bean.setOptTime("");
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

	public void addJdProductStandard(JdProductStandard bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into jd_product_standard(standard_id,type_id,standard_no,standard_name,is_valid,opt_id,opt_name,opt_time) values (?,?,?,?,?,?,?,now())";
		params[0] = new String[] { DBFacade.getInstance().getID(), bean.getTypeId(), bean.getStandardNo(), bean.getStandardName(),"1", bean.getOptId(), bean.getOptName() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"产品检测标准添加",
				"产品类型ID：" + bean.getTypeId() + ";产品检测标准名称："
						+ bean.getStandardName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editJdProductStandard(JdProductStandard bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_product_standard set type_id=?,standard_no=?,standard_name=?,is_valid=?,opt_id=?,opt_name=?,opt_time=now() where standard_id=?";
		params[0] = new String[] { bean.getTypeId(), bean.getStandardNo(), bean.getStandardName(), bean.getIsValid(), bean.getOptId(), bean.getOptName(),bean.getStandardId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"产品检测标准修改",
				"产品类型ID：" + bean.getTypeId() + ";产品检测标准名称："
						+ bean.getStandardName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdProductStandard(JdProductStandard bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_product_standard where standard_id=?";
		params[0] = new String[] { bean.getStandardId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"产品检测标准删除",
				"产品类型ID：" + bean.getTypeId() + ";产品检测标准名称："
						+ bean.getStandardName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJdProductStandardById(String standardId) throws Exception {
		String sql = "select a.standard_id,a.type_id,b.type_name,a.standard_no,a.standard_name,a.is_valid,a.opt_id,a.opt_name,a.opt_time from jd_product_standard a,jd_product_type b where a.type_id=b.type_id and standard_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { standardId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdProductStandard bean = new JdProductStandard();
			bean.setStandardId(crs.getString("standard_id"));
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setStandardNo(crs.getString("standard_no"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			if(crs.getString("opt_time")!=null){
				bean.setOptTime(TimeUtil.getStringDate(crs.getDate("opt_time")));
				}else{
					bean.setOptTime("");
				}
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}
	
	public JSONArray getTypeList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select type_id ,type_name from jd_product_type";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("type_id"));
				bean.put("name", crs.getString("type_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public boolean addCheckName(String standardName) throws Exception{
		try {
			String sql = "select standard_name from jd_product_standard where standard_name = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { standardName });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean addCheckNo(String standardNo) throws Exception{
		try {
			String sql = "select standard_no from jd_product_standard where standard_no = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { standardNo });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public List<ZTreeBean> getTypeAllRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select type_id,type_name,parent_id from jd_product_type where is_valid = '1' ";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setName(crs.getString("type_name"));
				//String parentId = crs.getString("parent_id");
				bean.setpId(crs.getString("parent_id"));
				bean.setOpen(false);
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无所属产品类型信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}
}
