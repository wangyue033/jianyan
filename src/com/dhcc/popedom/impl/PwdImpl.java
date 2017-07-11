package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.utils.DCiperTools;
/**
 * 
 * @author lgchen
 *
 */
public class PwdImpl {
	Page page = new Page();

	public Map<String, Object> resetPassword(String serialId,String optId,String optName) throws SQLException{
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet("select user_id , user_name from hr_staff where serial_id = '"+serialId+"'",null);
		String[] sqls = new String[crs.size() + 1];
		int i = 0;
		String userName="";
		while(crs.next()){
			sqls[i] = "update HR_STAFF set PASSWORD = '"+DCiperTools.getDigest(crs.getString("user_id"))+"' where USER_ID = '"+crs.getString("user_id")+"'";
			i++;
			userName = crs.getString("user_name");
		}
		sqls[i] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) " +
				"values('"+DBFacade.getInstance().getID()+"','"+optId+"','"+optName+"','密码重置',now(),'重置密码的用户编号："+serialId+";用户名称："+userName+"','1')";
		DBFacade.getInstance().execute(sqls);
		
		map.put("msg", "true");
		return map;
	}
	
	public boolean canLogin(String userID, String password) {
		
		boolean flag=false;
		String sql="select * from HR_STAFF where user_id= ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { userID });
		if (crs != null && crs.size() > 0) {
			try {
				while (crs.next()) {
					if (crs.getString("password").trim().equals(DCiperTools.getDigest(password))) {
						flag=true;
					}
				}
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean changePassword(String userID, String newPassword) {
		boolean result = false;
		String sql="update HR_STAFF set PASSWORD=? where USER_ID=?";
		 try {
			DBFacade.getInstance().execute(sql,new String[] { DCiperTools.getDigest(newPassword),userID });
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean addCheck(String productTypeName) throws Exception{
		try {
			String sql = "select user_id from hr_staff where user_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { productTypeName });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


}
