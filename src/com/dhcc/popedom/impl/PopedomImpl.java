package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


import com.dhcc.popedom.domain.Popedom;
import com.dhcc.popedom.domain.User;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
/**
 * 
 * @author WYH
 *
 */

public class PopedomImpl {
	Page page = new Page();
	List<User> dataRows = new ArrayList<User>();
	
	public String getPopedomList(Popedom bean1,String curPage, String perNumber,
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
		
		String sql = "select a.serial_id,a.company_id,a.user_id,a.user_name,a.user_cn,a.state,b.company_name,d.role_id,d.role_name "
				+ "from hr_staff a,hr_company b,p_re_map c,p_role d "
				+ "where a.state = '2' and a.company_id = b.company_id "
				+ "and a.serial_id = c.serial_id "
				+ "and c.role_id = d.role_id ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		if(bean1.getRoleId() != null &&  !"".equals(bean1.getRoleId())){
			sql = sql + " and d.role_id = '" + bean1.getRoleId() + "'";
			System.out.println(sql);
		}
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("d.role_id");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<Popedom> list = new ArrayList<Popedom>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			Popedom bean = new Popedom();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setUserId(crs.getString("user_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setUserCn(crs.getString("user_cn"));
			bean.setState(crs.getString("state"));
			bean.setRoleId(crs.getString("role_id"));
			bean.setRoleName(crs.getString("role_name"));
			//System.out.println(bean);
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
	
	public void addUserTRole(Popedom bean) {
		String userIds = bean.getUserIds();
		String roleId = bean.getRoleId();
		String optId = bean.getOptId();
		String roleName=bean.getRoleName();
		String[] userId = userIds.split(",");
		int len = userId.length;
		String[] sqls = null;
		sqls = new String[2 * len + 1];
		for (int i = 0; i < len; i++) {
			sqls[2 * i] = "insert into p_re_map(re_id,role_id,serial_id,remark) values('"
					+ DBFacade.getInstance().getID()
					+ "','"
					+ roleId
					+ "','"
					+ userId[i] + "','')";
			sqls[2 * i + 1] = "insert into p_popedom_log(log_id,serial_id,role_id,opt_id,opt_time,remark,role_name) values('"
					+ DBFacade.getInstance().getID()
					+ "','"
					+ userId[i]
					+ "','"
					+ roleId
					+ "','"
					+ optId
					+ "',now() ,'','"
					+ roleName + "') ";
		}
		sqls[sqls.length - 1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values('"
				+ DBFacade.getInstance().getID()
				+ "','"
				+ bean.getUserId()
				+ "','"
				+ bean.getOptName()
				+ "','添加用户到角色',now(),'角色编号："
				+ roleId
				+ ";用户编号："
				+ userIds
				+ "','1')";
		DBFacade.getInstance().execute(sqls, null);
	}

	public String getUserList(Popedom bean,
			String curPage, String perNumber,
			String orderByField, String orderBySort,
			String searchField, String searchValue) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			  curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.serial_id,a.company_id,a.user_id,a.user_name,a.user_cn,a.state,b.company_name,c.job_name "
				+ "from HR_STAFF a left join hr_job c on a.job_id = c.job_id,HR_COMPANY b "
				+ "where a.COMPANY_ID = b.COMPANY_ID "
				+ "and a.STATE = '2'"
				+ "and a.SERIAL_ID not in (select SERIAL_ID from P_Re_MAP where ROLE_ID = '"
				+ bean.getRoleId() + "') ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		if(orderByField != null
				&& !"".equals(orderByField)
				&& !"undefined".equals(orderByField)){
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		}else{
			page.setSidx("serial_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<User> list = new ArrayList<User>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			User bean1 = new User();
			bean1.setSerialId(crs.getString("serial_id"));
			bean1.setUserId(crs.getString("user_id"));
			bean1.setUserName(crs.getString("user_name"));
			bean1.setUserCn(crs.getString("user_cn"));
			bean1.setState(crs.getString("state"));
			bean1.setJobName(crs.getString("job_name"));
			bean1.setCompanyName(crs.getString("company_name"));
			list.add(bean1);
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


	public List<User> getDataRows() {
		return dataRows;
	}
	public void setDataRows(List<User> dataRows) {
		this.dataRows = dataRows;
	}

	//删除用户角色
	public Map<String, Object> delUserFRole(Popedom bean) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];
		sqls[0] = "delete from p_re_map where role_Id=? and serial_id=?";
		params[0] = new String[] { bean.getRoleId(),bean.getSerialId() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(),
				"功能权限删除", "角色编号：" + bean.getRoleId(), "1" };
		sqls[2] = "insert into p_popedom_log(" +
				"log_id,serial_id,log_type,opt_id,opt_time,remark,role_id,role_name" +
				") value(?,?,?,?,now(),?,?,?)";
		params[2]=new String[]{
				DBFacade.getInstance().getID(),bean.getSerialId(),"2",
				bean.getOptId(),"功能权限删除",
				bean.getRoleId(),bean.getRoleName()
		};
		System.out.println(sqls[2]);
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	/**
	 * 
	 * @return
	 */
	public List<ZTreeBean> getRoleTree(String orgId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select role_id,role_name,parent_id from p_role where is_valid='1' and company_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{orgId});
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("role_id"));
				bean.setName(crs.getString("role_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
}
