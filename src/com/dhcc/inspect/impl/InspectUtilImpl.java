package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.InspectUtil;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;

public class InspectUtilImpl {

	public String getCompanyAndPerson(String deptId) throws Exception{
		JSONObject data = new JSONObject();
		String sql = "select company_id,company_name from hr_company where state='1' ";
		if(deptId!=null && !deptId.trim().equals("")){
			sql = sql + " and company_id like '"+ deptId +"%'";
		}
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		JSONArray companys = new JSONArray();
		crs.beforeFirst();
		while(crs.next()){
			JSONObject com = new JSONObject();
			com.put("companyId", crs.getString("company_id"));
			com.put("companyName", crs.getString("company_name"));
			JSONArray persons = new JSONArray();
			String psql = "select serial_id,user_name from hr_staff where state='2' and company_id=? ";
			CachedRowSetImpl pcrs = DBFacade.getInstance().getRowSet(psql, new String[]{crs.getString("company_id")});
			pcrs.beforeFirst();
			while(pcrs.next()){
				JSONObject p = new JSONObject();
				p.put("userName", pcrs.getString("user_name"));
				p.put("userId", pcrs.getString("serial_id"));
				persons.add(p);
			}
			com.put("persons",persons);
			companys.add(com);
		}
		data.put("msg", "true");
		data.put("record", companys);
		return data.toString();
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-17
	* *
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getMyInfo(String tranId,String stepStr) throws SQLException {
		String sql = "select next_step_id from dh_step where step_id='"+ stepStr +"'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			InspectUtil bean = new InspectUtil();
			bean.setNextStepId(crs.getString("next_step_id"));
			JSONArray jArray = JSONArray.fromObject(bean.getNextStepId());
			map.put("record", jArray);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}

	/** *******************
	* @param handleDeptId
	* @return
	* 2017-7-4
	* 获取抽样机构的部门和人员
	 * @throws SQLException 
	*/
	public String getSampleCompanyAndPerson(String handleDeptId) throws SQLException {
		JSONObject data = new JSONObject();
		String sql = "select company_id,company_name from hr_company where state='1' ";
		if(handleDeptId!=null && !handleDeptId.trim().equals("")){
			sql = sql + " and company_id like '"+ handleDeptId +"%'";
		}
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		JSONArray companys = new JSONArray();
		crs.beforeFirst();
		while(crs.next()){
			JSONObject com = new JSONObject();
			com.put("companyId", crs.getString("company_id"));
			com.put("companyName", crs.getString("company_name"));
			JSONArray persons = new JSONArray();
			String psql = "select serial_id,user_name from hr_staff where state='2' and company_id=? ";
			CachedRowSetImpl pcrs = DBFacade.getInstance().getRowSet(psql, new String[]{crs.getString("company_id")});
			pcrs.beforeFirst();
			while(pcrs.next()){
				JSONObject p = new JSONObject();
				p.put("userName", pcrs.getString("user_name"));
				p.put("userId", pcrs.getString("serial_id"));
				persons.add(p);
			}
			com.put("persons",persons);
			companys.add(com);
		}
		data.put("msg", "true");
		data.put("record", companys);
		return data.toString();
	}
}
