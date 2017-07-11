/**
 * 
 */
package com.dhcc.login;

import java.util.Map;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.conf.SystemConfig;
import framework.dhcc.db.DBFacade;
import framework.dhcc.utils.DCiperTools;

/**
 * @author Administrator
 * 
 */
public class EmployeeImpl extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4682521205919014179L;

	public boolean insertEmployeeInfo(String employeeID, String password) {
		try {
			Map<String, String> compMap = SystemConfig.getInstance()
					.getCompMap();
			String pwd = SystemConfig.getInstance().getAdminPassword();
			String sql_s = "select * from HR_STAFF where user_id=?";
			String[] sql_s_value = { employeeID };
			if (DBFacade.getInstance().getValueBySql(sql_s, sql_s_value) != null) {
				System.out.println("用户名为" + employeeID + "的管理员在数据库中已经存在!");
			} else {
				String[] sqls = new String[2];
				String[][] sParas = new String[2][];
				sqls[0] = "insert into hr_company(company_id,company_name,parent_id,is_valid) values(?,?,?,?)";
				sParas[0] = new String[] { compMap.get("cId"),
						compMap.get("cName"), compMap.get("pId"),
						compMap.get("isValid") };
				sqls[1] = "insert into hr_staff(serial_id,company_id,user_id,user_name,user_cn,password,question,answer,state) values(?,?,?,?,?,?,?,?,?)";
				sParas[1] = new String[] {
						SystemConfig.getInstance().getAdminId(),
						compMap.get("cId"),
						SystemConfig.getInstance().getAdminAccount(),
						SystemConfig.getInstance().getAdminName(),
						SystemConfig.getInstance().getAdminCn(),
						DCiperTools.getDigest(pwd), "您的姓名是什么？",
						SystemConfig.getInstance().getAdminName(), "2" };
				DBFacade.getInstance().execute(sqls, sParas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Employee checkUser(String userId, String password) {
		String pwd = DCiperTools.getDigest(password);
		String sql = "select aa.*,bb.company_name,bb.comp_type from (select a.serial_id,a.user_id,a.user_name,a.password,a.company_id,c.role_id,c.is_charge,a.state,a.photo_path "
				+ "from hr_staff a,p_re_map b,p_role c "
				+ "where a.serial_id = b.serial_id "
				+ "and b.role_id = c.role_id "
				+ "and a.user_id = ?) aa left join hr_company bb on aa.company_id = bb.company_id ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { userId });
		if (crs != null && crs.size() > 0) {
			try {
				while (crs.next()) {
					if (crs.getString("password").trim().equals(pwd)) {
						String compId = crs.getString("company_id");
						this.setEmployeeId(crs.getString("serial_id"));// 系统编号，保存到数据库中
						this.setUserId(crs.getString("user_id"));//
						this.setUserName(crs.getString("user_name"));
						this.setPhotoPath(crs.getString("photo_path"));
						this.setPassword(crs.getString("password"));
						this.setCompanyId(compId);
						this.setCompanyName(crs.getString("company_name"));
						this.setRoleId(crs.getString("role_id"));
						this.setCanOnduty(crs.getString("is_charge"));
						this.setState(crs.getString("state"));
						this.setCompType(crs.getString("comp_type"));
						if(compId != null && compId.length()>10){
							String sql_org = "select company_id,company_name from hr_company where company_id = ?";
							CachedRowSetImpl crs_org = DBFacade.getInstance().getRowSet(sql_org, new String[]{compId.substring(0,10)});
							if(crs_org.next()){
								this.setOrgId(crs_org.getString("company_id"));
								this.setOrgName(crs_org.getString("company_name"));
							}
						}else{
							this.setOrgId(compId);
							this.setOrgName(crs.getString("company_name"));
						}
						
						
						return this;
					}
				}
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		return null;
	}

}
