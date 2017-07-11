package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdAreaInfo;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp
 * @version 1.0
 * @created 2017-2-23
 *********************** 
 */
public class JdAreaInfoImpl {
	Page page = new Page();

	/**
	 * *******************
	 * 
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return 2017-2-23 获取区域信息列表
	 * @throws SQLException
	 */
	public String getJdAreaInfoList(String curPage, String perNumber,
			String orderByField, String orderByType, String searchField,
			String searchValue) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.area_id,a.area_name,a.parent_id,ifnull(b.area_name,'') parent_name "
				+ "from jd_area_info a left join jd_area_info b on a.parent_id = b.area_id where 1 = 1  ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderByType);
		} else {
			page.setSidx("a.area_id");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdAreaInfo> list = new ArrayList<JdAreaInfo>();
		List<JdAreaInfo> areaList = getArea();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdAreaInfo bean = new JdAreaInfo();
			bean.setAreaId(crs.getString("area_id"));
			bean.setAreaName(crs.getString("area_name"));
			bean.setParentId(crs.getString("parent_id"));
			//bean.setParentName(crs.getString("parent_name"));
			bean.setParentName(getParentName(areaList,crs.getString("parent_id")));
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
	
	public List<JdAreaInfo> getArea() throws SQLException{
		List<JdAreaInfo> list = new ArrayList<JdAreaInfo>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where 1 = 1 order by area_id desc ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,null);
		while(crs.next()){
			JdAreaInfo area = new JdAreaInfo();
			area.setAreaId(crs.getString("area_id"));
			area.setAreaName(crs.getString("area_name"));
			area.setParentId(crs.getString("parent_id"));
			list.add(area);
		}
		return list;
	}
	public String getParentName(List<JdAreaInfo> areaList,String parentId) throws SQLException{
		String parentName = "";
		for(int i = 0;i<areaList.size();i++){
			JdAreaInfo bean = (JdAreaInfo)areaList.get(i);
			if(bean.getAreaId().equals(parentId)){
				parentName = bean.getAreaName() +"·" + parentName ;
				parentId = bean.getParentId();
			}
		}
		if(parentName != null && parentName.length()>1){
			parentName = parentName.substring(0,parentName.length() - 1);
		}
		return parentName;
	}
	/**
	 * *******************
	 * 
	 * @param areaId
	 * @return 2017-2-23 获取该编号作为父编号的个数
	 * @throws SQLException
	 */
	public String getSumList(String areaId) throws SQLException {
		String sql = "select max(area_id) area_id,COUNT(*) num from jd_area_info where parent_id = ?";

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { areaId });
		if (crs.next()) {
			JdAreaInfo bean = new JdAreaInfo();
			int num = 0;
			String str0 = crs.getString("area_id");
			if (str0 == null) {
				num = Integer.valueOf(crs.getString("num")) + 1;
			} else {
				String str = str0.substring(str0.length() - 2, str0.length());
				num = Integer.valueOf(str) + 1;
			}

			if (num < 10) {
				bean.setCounyts("0" + num);
			} else {
				bean.setCounyts(num + "");
			}
			map.put("num", bean.getCounyts());
		}

		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	/**
	 * *******************
	 * 
	 * @return 2017-2-23 获取区域树
	 */
	public List<ZTreeBean> getAreaTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where 1 = '1' order by area_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("area_id"));
				bean.setName(crs.getString("area_name"));
				bean.setpId(crs.getString("parent_id"));
				if(bean.getId().contains("0000")){
					bean.setOpen(true);
				}else{
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}

	/**
	 * *******************
	 * 
	 * @param bean
	 *            2017-2-23 添加区域信息
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void addJdAreaInfo(JdAreaInfo bean) throws NumberFormatException, SQLException {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into jd_area_info(area_id,area_name,parent_id,is_valid,opt_id,opt_name,opt_time) "
				+ "values (?,?,?,1,?,?,now()) ";
		params[0] = new String[] { bean.getAreaId(), bean.getAreaName(),
				bean.getParentId(), bean.getOptId(), bean.getOptName() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "区域信息添加",
				"区域编号：" + bean.getAreaId() + ";区域名称：" + bean.getAreaName(), "1" };

		DBFacade.getInstance().execute(sqls, params);
	}

	/**
	 * *******************
	 * 
	 * @param areaName
	 * @return 2017-2-24 添加时验证区域名称的重复
	 */
	public boolean checkAreaName(String areaName) {
		String sql = "select count(*) from jd_area_info where area_name = ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { areaName });
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
	 * @return 2017-2-24 删除区域信息
	 */
	public Map<String, Object> delJdAreaInfo(JdAreaInfo bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_area_info where area_id like ? ";
		params[0] = new String[] { bean.getAreaId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "区域信息删除",
				"区域编号：" + bean.getAreaId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/**
	 * *******************
	 * 
	 * @param areaId
	 * @return 2017-2-24 获取区域详情
	 * @throws ParseException
	 * @throws SQLException
	 */
	public Map<String, Object> getJdAreaById(String areaId)
			throws SQLException, ParseException {
		String sql = "select a.area_id,a.area_name,a.parent_id,ifnull(b.area_name,'') parent_name,a.is_valid,a.opt_id,"+TimeUtil.getTimeShow("a.opt_time")+" opt_name,a.opt_time "
				+ "from jd_area_info a left join jd_area_info b on a.parent_id = b.area_id where a.area_id = ?  ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { areaId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdAreaInfo bean = new JdAreaInfo();
			bean.setAreaId(crs.getString("area_id"));
			bean.setAreaName(crs.getString("area_name"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setParentName(crs.getString("parent_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	/**
	 * *******************
	 * 
	 * @param enterpriseName
	 * @param areaId
	 * @return 2017-2-24 修改时验证区域名称的重复
	 */
	public boolean checkEditJdAreaName(String areaName, String areaId) {
		String sql = "select count(*) from jd_area_info where area_name = ? and area_id != ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { areaName, areaId });
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
	 * @return 2017-2-24 修改区域信息
	 */
	public Map<String, Object> editJdAreaInfo(JdAreaInfo bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_area_info set area_id = ?,area_name=?,parent_id=? where area_id = ?";
		params[0] = new String[] { bean.getAreaId(), bean.getAreaName(),
				bean.getParentId(), bean.getStrId() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "区域信息修改",
				"区域编号：" + bean.getAreaId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/**
	 * *******************
	 * 
	 * @param areaId
	 * @return 2017-2-24 修改时获取子区域数量
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public String getSumList2(String areaId, String strId)
			throws NumberFormatException, SQLException {
		String sql = "select COUNT(*) num from jd_area_info where parent_id = ? and area_id != ?";

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { areaId, strId });
		if (crs.next()) {
			JdAreaInfo bean = new JdAreaInfo();
			int num = 0;
			num = Integer.valueOf(crs.getString("num")) + 1;
			if (num < 10) {
				bean.setCounyts("0" + num);
			} else {
				bean.setCounyts("num");
			}
			map.put("num", bean.getCounyts());
		}

		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	/**
	 * *******************
	 * 
	 * @return 2017-3-17 获取父编号为空的个数
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public String getSumList3(String parentId) throws NumberFormatException, SQLException {
		
		String qybm = "";

		String sql = "select ifnull(max(area_id),'') from jd_area_info where parent_id = ? ";
		String max = DBFacade.getInstance()
				.getValueBySql(sql, new String[] { parentId }).toString();
		int item = 0;
		if(max!=null && !max.isEmpty()) {
			item = Integer.valueOf(max.substring(max.length()-2));
		}
		item = item+1;
		if (item < 10) {
			qybm = parentId + "0" + item;
		} else {
			qybm = parentId + item;
		}
		return qybm;
	}

	/**
	 * *******************
	 * 
	 * @param areaId
	 * @return 2017-3-17 修改时验证区域是否已经被使用，已被使用则只能修改名称
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public String checkEditJdAreaId(String areaId)
			throws NumberFormatException, SQLException {
		String sql = "select COUNT(*) num from jd_area_info where parent_id = ? ";

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { areaId });
		if (crs.next()) {
			int num = 0;
			num = Integer.valueOf(crs.getString("num"));
			if (num > 0) {
				map.put("msg", "false");
			} else {
				map.put("msg", "true");
			}
		}

		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}

	/** *******************
	* @return
	* 2017-3-21
	* *******************
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
				if(bean.getId().contains("0000")){
					bean.setOpen(true);
				}else{
					bean.setOpen(false);
				}
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

	/** *******************
	* @param areaId
	* @return
	* 2017-3-30
	* 添加时验证区域编号是否重复
	*/
	public boolean checkAreaId(String areaId) {
		String sql = "select count(*) from jd_area_info where area_id = ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { areaId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @return
	* 2017-4-10
	* 区域多选
	*/
	public List<ZTreeBean> getAreaAllRootCheck() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where 1 = '1' order by area_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("area_id"));
				bean.setName(crs.getString("area_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setNocheck(false);
				if(bean.getId().contains("0000")){
					bean.setOpen(true);
				}else{
					bean.setOpen(false);
				}
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

	/** *******************
	* @param areaId
	* @return
	* 2017-4-20
	* *******************
	*/
	public List<ZTreeBean> getAreaAllRoot(String areaId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String areas[] = areaId.split(",");
		for(int i=0;i<areas.length;i++){
			String sql = "select area_id,area_name,parent_id from jd_area_info where area_id=? order by area_id";
			try {
				CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{areas[i]});
				while (crs.next()) {
					ZTreeBean bean = new ZTreeBean();
					bean.setId(crs.getString("area_id"));
					bean.setName(crs.getString("area_name"));
					bean.setpId(crs.getString("parent_id"));
					bean.setNocheck(false);
					treeList.add(bean);
				}
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无抽查区域信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

}
