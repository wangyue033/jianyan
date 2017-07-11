package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.popedom.domain.Comp;
import com.dhcc.popedom.domain.User;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;
import framework.dhcc.utils.DCiperTools;
/**
 * 
 * @author WYH
 *
 */
public class UserImpl {
	Page page = new Page();
	
	public List<User> getUserList(PageBean pageData) throws Exception {
		List<User> list = new ArrayList<User>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			User bean = new User();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setUserId(crs.getString("user_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setUserCn(crs.getString("user_cn"));
			bean.setState(crs.getString("state"));
			bean.setJobName(crs.getString("job_name"));
			bean.setCompanyName(crs.getString("company_name"));
			list.add(bean);
		}
		return list;
	}

	public String getUserList(User bean1,String curPage, String perNumber,
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
		
		String sql = "select a.serial_id,a.company_id,a.user_id,a.user_name,a.user_cn,a.state,a.cert_type,a.cert_no,b.company_name,ifnull(c.job_name,'') job_name "
				+ "from hr_staff a left join hr_job c on a.job_id = c.job_id, hr_company b "
				+ "where a.company_id = b.company_id ";
		if(bean1.getCompanyId() != null &&  !"".equals(bean1.getCompanyId())){
			sql = sql + " and a.company_id like '" + bean1.getCompanyId() + "%'";
		}
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
			page.setSidx("a.user_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<User> list = new ArrayList<User>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			User bean = new User();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setJobName(crs.getString("job_Name"));
			bean.setUserId(crs.getString("user_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setUserCn(crs.getString("user_cn"));
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
	
//递增生成员工编号方法，从0000开始
	public String getSeriId() {
		 String sql="select ifnull(max(serial_id),'0') from hr_staff";//如果查询的值为空返回0
		 String it=DBFacade.getInstance().getValueBySql(sql, new String[]{}).toString();
		 int i = Integer.parseInt(it) + 1;
		 String its = String.format("%" + 4 + "s", i).replace(' ', '0');
		 return its;
		}
	
	

	public void addUser(User bean) throws Exception {
		String userId = bean.getUserId();
		String userName = bean.getUserName();
		String userCn = CnToPiny.getInstance().getPingYin(userName);
		String password = DCiperTools.getDigest(userId);
		String state = bean.getState();
		//String serialId = DBFacade.getInstance().getID().substring(16, 20);
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into hr_staff (serial_id,job_id,company_id,user_id," +
							"user_name,user_cn,password,question,answer,state,sex,cert_type," +
							"cert_no,tel_phone,work_desc,birth_date,photo_path,email,mobile_phone) " +
							"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		params[0] = new String[] { 
				bean.getSerialId(),bean.getJobId(),bean.getCompanyId(),
				userId, userName, userCn, password, userName,
				userName, state,bean.getSex(),bean.getCertType(),
				bean.getCertNo(),bean.getTelPhone(),bean.getWorkDesc(),
				bean.getBirthDate(),bean.getPhotoPath(),bean.getEmail(),
				bean.getMobilePhone()
				};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "员工信息添加",
				"员工编号：" + bean.getSerialId() + ";员工姓名：" + bean.getUserName(), "1" };
	
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editUser(User bean) throws Exception {
		String userId=bean.getUserId();
		String password = DCiperTools.getDigest(userId);
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update hr_staff set serial_id=?,company_id=?," +
				"job_id=?,user_id=?,user_name=?,user_cn=?,password=?," +
				"question=?,answer=?,state=?,cert_type=?," +
				"cert_no=?,sex=?,email=?,tel_phone=?,work_desc=?,birth_date=?,photo_path=?," +
				"password=?,question=?,answer=? where serial_id=?";
		params[0] = new String[] { 
				bean.getSerialId(), bean.getCompanyId(), bean.getJobId(),
				bean.getUserId(), bean.getUserName(), bean.getUserCn(),
				bean.getPassword(), bean.getQuestion(), bean.getAnswer(), 
				bean.getState(), bean.getCertType(), bean.getCertNo(), 
				bean.getSex(),bean.getEmail(),bean.getTelPhone(),
				bean.getWorkDesc(),bean.getBirthDate(),bean.getPhotoPath(),
				password,bean.getUserName(),bean.getUserName(),bean.getSerialId() 
				};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "员工信息修改",
				"员工编号：" + bean.getSerialId() + ";员工姓名：" + bean.getUserName(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}


	public Map<String, Object> getUserById(String serialId) throws Exception {
		String sql = "select a.serial_id,c.job_name,a.company_id,a.job_id," +
				"a.user_id,a.user_name,a.user_cn,a.sex,a.state,a.cert_type,a.cert_no," +
				"a.email,a.tel_phone,a.mobile_phone,a.work_desc,a.birth_date,a.photo_path," +
				"b.company_name,ifnull(c.job_name,'') job_name from hr_staff a left " +
				"join hr_job c on a.job_id = c.job_id, hr_company b where a.serial_id =? " +
				"and a.company_id = b.company_id ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { serialId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			User bean = new User();
			bean.setSerialId(crs.getString("serial_id"));
			bean.setCompanyId(crs.getString("company_id"));
			bean.setCompanyName(crs.getString("company_Name"));
			bean.setJobId(crs.getString("job_id"));
			bean.setJobName(crs.getString("job_name"));
			bean.setUserId(crs.getString("user_id"));
			bean.setUserName(crs.getString("user_name"));
			bean.setUserCn(crs.getString("user_cn"));
			bean.setSex(crs.getString("sex"));
			bean.setState(crs.getString("state"));
			bean.setCertType(crs.getString("cert_type"));
			bean.setCertNo(crs.getString("cert_no"));
			bean.setEmail(crs.getString("email"));
			bean.setWorkDesc(crs.getString("work_desc"));
			bean.setBirthDate(crs.getString("birth_date"));
			bean.setTelPhone(crs.getString("tel_phone"));
			bean.setMobilePhone(crs.getString("mobile_phone"));
			bean.setPhotoPath(crs.getString("photo_path"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}
	
	
	public  JSONArray getJobList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select job_id ,job_name from hr_job";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
					sql.toString(), null);

			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("job_id"));
				bean.put("title", crs.getString("job_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public boolean addCheck(String userId) throws Exception{
		try {
			String sql = "select user_id from hr_staff where user_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { userId });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @return
	* 2017-3-24
	* 获取检测人员弹框的人员信息
	*/
	public List<ZTreeBean> getUserAllRoot(String companyId,String companyName) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select serial_id,user_name,company_id from hr_staff where company_id = ? order by serial_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { companyId });
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("serial_id"));
				bean.setName(crs.getString("user_name"));
				bean.setpId(crs.getString("company_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (!treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId(companyId);
			bean.setName(companyName);
			bean.setpId("-1");
			treeList.add(bean);
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无检验人员信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

	public List<Comp> getCompInfo() throws SQLException{
		List<Comp> list = new ArrayList<Comp>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' order by company_id desc ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,null);
		while(crs.next()){
			Comp comp = new Comp();
			comp.setCompanyId(crs.getString("company_id"));
			comp.setCompanyName(crs.getString("company_name"));
			comp.setParentId(crs.getString("parent_id"));
			list.add(comp);
		}
		return list;
	}
	public String getCompParentName(List<Comp> compList,String parentId) throws SQLException{
		String parentName = "";
		for(int i = 0;i<compList.size();i++){
			Comp bean = (Comp)compList.get(i);
			if(bean.getCompanyId().equals(parentId)){
				parentName = bean.getCompanyName() +"·" + parentName ;
				parentId = bean.getParentId();
			}
		}
		if(parentName != null && parentName.length()>1){
			parentName = parentName.substring(0,parentName.length() - 1);
		}
		return parentName;
	}

	/** *******************
	* @param companyId
	* @param companyName
	* @return
	* 2017-6-26
	* *******************
	*/
	public List<ZTreeBean> getInspectOpt(String companyId, String companyName) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select serial_id,user_name from hr_staff where state='2' ";
		if(companyId!=null && !companyId.trim().equals("")){
			sql = sql + " and company_id like '"+ companyId +"%'";
		}
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] {  });
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("serial_id"));
				bean.setName(crs.getString("user_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无检验人员信息");
			treeList.add(bean);
		}
		return treeList;
	}
}
