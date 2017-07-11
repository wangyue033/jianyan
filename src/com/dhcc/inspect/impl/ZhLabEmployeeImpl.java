package com.dhcc.inspect.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.ZhLabEmployee;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class ZhLabEmployeeImpl {
	Page page = new Page();

	public String getZhLabEmployeeList(ZhLabEmployee bean1,String curPage, String perNumber,
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
		String sql = "select a.emp_id,a.lab_id,b.lab_name,a.emp_name,a.sex,a.age,a.edu_degree,a.title_name,a.title_level,a.professional,a.total_year,a.station,a.now_year,a.remark,a.telephone from zh_lab_employee a,zh_lab b where a.lab_id=b.lab_id and 1=1 ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		if(bean1.getLabId() != null &&  !"".equals(bean1.getLabId())){
			sql = sql + " and a.lab_id = '" + bean1.getLabId() + "'";
		}
		
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("emp_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<ZhLabEmployee> list = new ArrayList<ZhLabEmployee>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			ZhLabEmployee bean = new ZhLabEmployee();
			bean.setEmpId(crs.getString("emp_id"));
			bean.setLabId(crs.getString("lab_id"));
			bean.setLabName(crs.getString("lab_name"));
			bean.setEmpName(crs.getString("emp_name"));
			bean.setSex(crs.getString("sex"));
			bean.setAge(crs.getString("age"));
			bean.setEduDegree(crs.getString("edu_degree"));
			bean.setTitleName(crs.getString("title_name"));
			bean.setTitleLevel(crs.getString("title_level"));
			bean.setProfessional(crs.getString("professional"));
			bean.setTotalYear(crs.getString("total_year"));
			bean.setStation(crs.getString("station"));
			bean.setNowYear(crs.getString("now_year"));
			bean.setRemark(crs.getString("remark"));
			bean.setTelephone(crs.getString("telephone"));
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

	public void addZhLabEmployee(ZhLabEmployee bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into zh_lab_employee(emp_id,lab_id,emp_name,sex,age,edu_degree,title_name,title_level,professional,total_year,station,now_year,remark,telephone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		params[0] = new String[] { DBFacade.getInstance().getID(), bean.getLabId(), bean.getEmpName(), bean.getSex(), bean.getAge(), bean.getEduDegree(), bean.getTitleName(), bean.getTitleLevel(), bean.getProfessional(), bean.getTotalYear(), bean.getStation(), bean.getNowYear(), bean.getRemark(), bean.getTelephone(),  };
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"检测机构人员信息添加",
				"企业信息ID：" + bean.getLabId() + ";姓名："
						+ bean.getEmpName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editZhLabEmployee(ZhLabEmployee bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update zh_lab_employee set emp_id=?,lab_id=?,emp_name=?,sex=?,age=?,edu_degree=?,title_name=?,title_level=?,professional=?,total_year=?,station=?,now_year=?,remark=?,telephone=? where emp_id=?";
		params[0] = new String[] { bean.getEmpId(),bean.getLabId(), bean.getEmpName(), bean.getSex(), bean.getAge(), bean.getEduDegree(), bean.getTitleName(), bean.getTitleLevel(), bean.getProfessional(), bean.getTotalYear(), bean.getStation(), bean.getNowYear(), bean.getRemark(), bean.getTelephone(),bean.getEmpId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"检测机构人员信息修改",
				"企业信息ID：" + bean.getLabId() + ";姓名："
						+ bean.getEmpName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delZhLabEmployee(ZhLabEmployee bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from zh_lab_employee where emp_id=?";
		params[0] = new String[] { bean.getEmpId() };
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"检测机构人员信息删除",
				"人员ID：" + bean.getEmpId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getZhLabEmployeeById(String empId) throws Exception {
		String sql = "select a.emp_id,a.lab_id,b.lab_name,a.emp_name,a.sex,a.age,a.edu_degree,a.title_name,a.title_level,a.professional,a.total_year,a.station,a.now_year,a.remark,a.telephone from zh_lab_employee a,zh_lab b where a.lab_id=b.lab_id and emp_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { empId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ZhLabEmployee bean = new ZhLabEmployee();
			bean.setEmpId(crs.getString("emp_id"));
			bean.setLabId(crs.getString("lab_id"));
			bean.setLabName(crs.getString("lab_name"));
			bean.setEmpName(crs.getString("emp_name"));
			bean.setSex(crs.getString("sex"));
			bean.setAge(crs.getString("age"));
			bean.setEduDegree(crs.getString("edu_degree"));
			bean.setTitleName(crs.getString("title_name"));
			bean.setTitleLevel(crs.getString("title_level"));
			bean.setProfessional(crs.getString("professional"));
			bean.setTotalYear(crs.getString("total_year"));
			bean.setStation(crs.getString("station"));
			bean.setNowYear(crs.getString("now_year"));
			bean.setRemark(crs.getString("remark"));
			bean.setTelephone(crs.getString("telephone"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	public JSONArray getLabList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select lab_id ,lab_name from zh_lab";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("lab_id"));
				bean.put("name", crs.getString("lab_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public List<ZTreeBean> getLabAllRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select lab_id,lab_name from zh_lab";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("lab_id"));
				bean.setName(crs.getString("lab_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无检测机构信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

}
