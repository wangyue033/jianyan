/**
 * 
 */
package com.dhcc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.tree.bean.ZTreeBean;

/**
 * @author zhurx
 * 
 */
public class ZTreeImpl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1839286589033361437L;
	private static ZTreeImpl tree;

	public synchronized static ZTreeImpl getInstance() {
		if (tree == null) {
			tree = new ZTreeImpl();
		}
		return tree;
	}

	public List<ZTreeBean> getCompTreeCheck(String roleCompId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' and company_id like '"
				+ roleCompId + "%' order by company_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			int i = 0 ;
			while (crs.next()) {
				i++;
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setNocheck(false);
				if (i == 1) {
					bean.setOpen(true);
				} else {
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}

	public List<ZTreeBean> getCompTreeRoot(String roleCompId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' and company_id like '"
				+ roleCompId + "%' order by company_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getMenuAndModuleTree() throws Exception {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql_menu = "select menu_id,menu_name,is_valid from p_menu order by menu_id";
		String sql_module = "select module_id,module_name,menu_id,is_valid from p_module order by module_id";
		ZTreeBean root = new ZTreeBean();
		root.setId("00");
		root.setName("东华农产品流通平台");
		root.setpId("-1");
		treeList.add(root);
		CachedRowSetImpl crs_menu = DBFacade.getInstance().getRowSet(sql_menu,
				null);
		while (crs_menu.next()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId(crs_menu.getString("menu_id"));
			bean.setName(crs_menu.getString("menu_name") + "("
					+ crs_menu.getString("menu_id") + ")");
			bean.setpId("00");
			bean.setRemark(crs_menu.getString("is_valid"));
			treeList.add(bean);
		}
		CachedRowSetImpl crs_module = DBFacade.getInstance().getRowSet(
				sql_module, null);
		while (crs_module.next()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId(crs_module.getString("module_id"));
			bean.setName(crs_module.getString("module_name") + "("
					+ crs_module.getString("module_id") + ")");
			bean.setpId(crs_module.getString("menu_id"));
			bean.setRemark(crs_module.getString("is_valid"));
			treeList.add(bean);
		}
		return treeList;
	}
	
	public List<ZTreeBean> getRoleTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select role_id,role_name,parent_id from p_role where is_valid='1'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
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
	
	/**
	 * 一般在左侧树展现时使用，若未查到任何信息，则显示无所属组织机构信息
	 * 
	 * @param companyId
	 *            ：只显示当前组织机构之下的机构（包括当前）
	 * @return
	 */
	public List<ZTreeBean> getCompanyAllRoot(String companyId) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid = '1' ";
		if (companyId != null && !companyId.isEmpty()
				&& !companyId.equals("00")) {
			sql += " and company_id like '" + companyId + "%'";
		}
		sql += " order by company_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无所属组织机构信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}
	
	public List<ZTreeBean> getCompanyTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select company_id,company_name,parent_id from hr_company where is_valid='1'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("company_id"));
				bean.setName(crs.getString("company_name"));
				bean.setpId(crs.getString("parent_id"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public LinkedHashMap<String, Object> getCertTree(String specialType) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select cert_id,cert_name from xk_cert_info where is_valid = '1' and special_type = '"+specialType+"'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			int i = 0 ;
			while (crs.next()) {
				i++;
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("cert_id"));
				bean.setName(crs.getString("cert_name"));
				bean.setNocheck(false);
				if (i == 1) {
					bean.setOpen(true);
				} else {
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		map.put("treeList", treeList);
		return map;
	}
	
	public List<ZTreeBean> getCertTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select cert_id,cert_name from xk_cert_info where is_valid = '1'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			int i = 0 ;
			while (crs.next()) {
				i++;
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("cert_id"));
				bean.setName(crs.getString("cert_name"));
				bean.setNocheck(false);
				if (i == 1) {
					bean.setOpen(true);
				} else {
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
}
