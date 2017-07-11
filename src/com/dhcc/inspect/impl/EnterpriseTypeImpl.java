package com.dhcc.inspect.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.EnterpriseType;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.utils.LogUtil;

public class EnterpriseTypeImpl {
	Page page = new Page();

	public String getEnterpriseTypeList(String curPage, String perNumber,
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
		String sql = "select type_id,type_name,short_name,type_desc,is_valid,opt_id,opt_name,opt_time from jd_enterprise_type where 1=1 ";

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
			page.setSidx("type_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<EnterpriseType> list = new ArrayList<EnterpriseType>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			EnterpriseType bean = new EnterpriseType();
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setShortName(crs.getString("short_name"));
			if(crs.getString("type_desc")!=""&&crs.getString("type_desc")!=null){
			  if(crs.getString("type_desc").length()>5){
				 bean.setTypeDesc(crs.getString("type_desc").substring(0,5)+"...");
			  }else{
				 bean.setTypeDesc(crs.getString("type_desc"));
			}
			}else{
				bean.setTypeDesc("");
			}
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

	public void addEnterpriseType(EnterpriseType bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		// TODO: handle exception
		sqls[0] = "insert into jd_enterprise_type(type_id,type_name,short_name,type_desc,is_valid,opt_id,opt_name,opt_time) values (?,?,?,?,?,?,?,now())";
		params[0] = new String[] { DBFacade.getInstance().getID(), bean.getTypeName(), bean.getShortName(), bean.getTypeDesc(), "1", bean.getOptId(), bean.getOptName()};
		
		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "企业分类信息添加", "{{addOptRemark}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editEnterpriseType(EnterpriseType bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		// TODO: handle exception
		sqls[0] = "update jd_enterprise_type set type_id=?,type_name=?,short_name=?,type_desc=?,is_valid=?,opt_id=?,opt_name=?,opt_time=? where type_id=?";
		params[0] = new String[] { bean.getTypeId(), bean.getTypeName(), bean.getShortName(), bean.getTypeDesc(), bean.getIsValid(), bean.getOptId(), bean.getOptName(), bean.getOptTime(), bean.getTypeId()};
		
		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "企业分类信息修改", "{{editOptRemark}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delEnterpriseType(EnterpriseType bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		// TODO: handle exception
		sqls[0] = "delete from jd_enterprise_type where type_id=?";
		params[0] = new String[] { bean.getTypeId() };

		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "企业分类信息删除", "{{delOptRemark}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getEnterpriseTypeById(String typeId) throws Exception {
		String sql = "select type_id,type_name,short_name,type_desc,is_valid,opt_id,opt_name,opt_time from jd_enterprise_type where type_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { typeId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			EnterpriseType bean = new EnterpriseType();
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setTypeDesc(crs.getString("type_desc"));
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

	public boolean addCheck(String typeName) throws Exception{
		try {
			String sql = "select type_name from jd_enterprise_type where type_name = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { typeName });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
