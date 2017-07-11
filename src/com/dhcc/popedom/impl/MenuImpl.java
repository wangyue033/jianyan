package com.dhcc.popedom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dhcc.popedom.domain.MenuBean;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;

public class MenuImpl {

	public List<MenuBean> getFuncList(PageBean pBean) {
		List<MenuBean> list = new ArrayList<MenuBean>();
		CachedRowSetImpl crs = pBean.getCrs();
		try {
			while (crs.next()) {
				String funcId = crs.getString("func_id");
				MenuBean bean = new MenuBean();
				bean.setFuncId(crs.getString("func_id"));
				bean.setFuncName(crs.getString("func_name"));
				bean.setFuncUrl(crs.getString("func_url"));
				bean.setResourceId(crs.getString("resource_id"));
				bean.setModuleId(crs.getString("module_id"));
				bean.setModuleName(crs.getString("module_name"));
				bean.setIsValid(crs.getString("is_valid"));
				String sql = "select popedom_id,popedom_name from p_popedom where func_id = ? ";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql,
						new String[] { funcId });
				String content = "";
				while (crs1.next()) {
					content += crs1.getString("popedom_name") + "|";
				}
				if (content.length() > 1)
					content = content.substring(0, content.length() - 1);
				bean.setPopedomContent(content);
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<MenuBean> getMenuList() throws Exception {
		List<MenuBean> menuList = new ArrayList<MenuBean>();
		String sql = "select * from p_menu where 1=1 order by menu_id asc ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		while (crs.next()) {
			MenuBean bean = new MenuBean();
			bean.setMenuId(crs.getString("menu_id"));
			bean.setMenuName(crs.getString("menu_name"));
			menuList.add(bean);
		}
		return menuList;
	}

	public MenuBean getModuleById(String moduleId) throws Exception {
		MenuBean temp = new MenuBean();
		String sql = "select * from p_module where module_id=?";
		String[] param = new String[] { moduleId };
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, param);
		if (crs.next()) {
			temp.setModuleName(crs.getString("module_name"));
			temp.setModuleId(crs.getString("module_id"));
		}
		return temp;
	}

	public boolean checkFuncEnterById(String moduleId) throws Exception {
		boolean flag = false;
		String sql = "select count(func_id) from p_func where module_id=?";
		String[] param = new String[] { moduleId };
		int count = (Integer) DBFacade.getInstance().getValueBySql(sql, param);
		if (count > 0) {// 存在入口方法
			flag = true;
		}
		return flag;
	}

	public MenuBean getFuncById(String funcId) throws Exception {
		MenuBean bean = new MenuBean();
		String sql = "select f.*,p.popedom_name from p_func f , p_popedom p where f.func_id=? and f.resource_id=popedom_id";
		String[] param = new String[] { funcId };
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, param);
		if (crs.next()) {
			bean.setFuncId(crs.getString("func_id"));
			bean.setFuncName(crs.getString("func_name"));
			bean.setResourceId(crs.getString("resource_id"));
			bean.setFuncUrl(crs.getString("func_url"));
			bean.setJspModel(crs.getString("jsp_model"));
			bean.setResourceName(crs.getString("popedom_name"));
		}
		return bean;
	}

	public MenuBean getMenuBean(MenuBean bean) throws Exception {
		String parentId = bean.getParentId();
		String sql = "";
		MenuBean temp = new MenuBean();
		if (parentId.equals("00")) {
			sql = "select * from p_menu where menu_id=?";
		} else {
			sql = "select * from p_module where module_id=?";
		}
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { bean.getMenuId() });
		if (crs.next()) {
			if (parentId.equals("00")) {
				temp.setMenuName(crs.getString("menu_name"));
				temp.setMenuId(crs.getString("menu_id"));
				temp.setIsValid(crs.getString("is_valid"));
			} else {
				temp.setMenuName(crs.getString("module_name"));
				temp.setMenuId(crs.getString("module_id"));
				temp.setIsValid(crs.getString("is_valid"));
			}
		}
		temp.setParentId(bean.getParentId());
		return temp;
	}

	public void editMenu(MenuBean bean) {
		String parentId = bean.getParentId();
		String sql = "";
		String[] sParas = null;
		if (parentId != null && parentId.trim().equals("00")) {// menu
			sql = "update p_menu set menu_name = ?, is_valid = ? where menu_id = ? ";
		} else {// module
			sql = "update p_module set module_name = ?, is_valid = ? where module_id = ?";
		}
		sParas = new String[] { bean.getMenuName(), bean.getIsValid(), bean.getMenuId() };
		DBFacade.getInstance().execute(sql, sParas);
	}

	public void addMenu(MenuBean bean) throws Exception {
		String operType = bean.getOperType();
		String sql = "";
		String[] sParas = null;
		if (operType != null && operType.equals("1")) {// menu
			sql = "insert into p_menu (menu_id,menu_name)values(?,?)";
			sParas = new String[] { bean.getMenuId(), bean.getMenuName() };
		} else {// module
			sql = "insert into p_module (module_id,module_name,menu_id)values(?,?,?)";
			sParas = new String[] { bean.getMenuId(), bean.getMenuName(),
					bean.getParentId() };
		}
		DBFacade.getInstance().execute(sql, sParas);
	}

	public boolean checkMenuId(MenuBean bean) throws Exception {
		boolean flag = true;
		String sql = "";
		String operType = bean.getOperType();
		long count = 0;
		if (operType != null && operType.equals("1")) {
			sql = "select count(*) from p_menu where menu_id=?";
			count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { bean.getMenuId() });
		} else {
			sql = "select COUNT(*) from p_menu a , p_module b where (a.menu_id=? or b.module_id=?) and a.menu_id=b.menu_id";
			count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { bean.getMenuId() , bean.getMenuId() });
		}
		
		if (count > 0) {
			flag = false;
		}
		return flag;
	}

	public void addFunc(MenuBean bean) throws Exception {
		String moduleId = bean.getModuleId();
		String funcId = bean.getFuncId();
		String funcName = bean.getFuncName();
		String resourceId = bean.getResourceId();
		String resourceName = bean.getResourceName();
		String funcURL = bean.getFuncUrl();
		String jspModel = bean.getJspModel();
		String[] sqls = new String[3];
		String[][] sParas = new String[3][];
		sqls[0] = "insert into p_func(func_id,func_name,func_url,resource_id,module_id) values(?,?,?,?,?)";
		sParas[0] = new String[] { funcId, funcName, funcURL, resourceId,
				moduleId};
		sqls[1] = "insert into p_popedom(popedom_id,popedom_name,func_id,resource_id)values(?,?,?,?)";
		sParas[1] = new String[] { resourceId, resourceName, funcId,
				resourceId};
		sqls[2] = "insert into p_rs_map values(?,?,?,?)";
		sParas[2] = new String[] { DBFacade.getInstance().getID(), "00",
				resourceId, "for admin" };
		DBFacade.getInstance().execute(sqls, sParas);
	}

	public void editFunc(MenuBean bean) throws Exception {
		String sql = "select * from p_func where func_id=?";
		String[] param = new String[] { bean.getFuncId() };
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, param);
		String[] sqls = null;
		String[][] params = null;
		if (crs.next()) {
			String resourceId = crs.getString("resource_id");
			if (resourceId.equals(bean.getResourceId().trim())) {
				sqls = new String[2];
				params = new String[2][];
				sqls[0] = "update p_func set func_name=? , func_url=? , jsp_model=?, is_valid=? where func_id=?";
				params[0] = new String[] { bean.getFuncName(),
						bean.getFuncUrl(), bean.getJspModel(), bean.getIsValid(), bean.getFuncId() };
				sqls[1] = "update p_popedom set popedom_name = ? where popedom_id = ?";
				params[1] = new String[] { bean.getResourceName(),
						bean.getResourceId() };
			} else {
				sqls = new String[5];
				params = new String[5][];
				sqls[0] = "delete from p_rs_map where popedom_id=?";
				params[0] = new String[] { resourceId };
				sqls[1] = "delete from p_popedom where popedom_id=?";
				params[1] = new String[] { resourceId };
				sqls[2] = "update p_func set func_name=? , func_url=? , resource_id=? , jsp_model=?, is_valid=? where func_id=?";
				params[2] = new String[] { bean.getFuncName(),
						bean.getFuncUrl(), bean.getResourceId(),
						bean.getJspModel(), bean.getIsValid(), bean.getFuncId() };
				sqls[3] = "insert into p_popedom(popedom_id,popedom_name,func_id,resource_id,order_id,jsp_icon,jsp_func)values(?,?,?,?,?,?,?)";
				params[3] = new String[] { bean.getResourceId(),
						bean.getResourceName(), bean.getFuncId(),
						bean.getResourceId(), "8", "search", "_search" };
				;
				sqls[4] = "insert into p_rs_map values(?,?,?,?)";
				params[4] = new String[] { DBFacade.getInstance().getID(),
						"00", bean.getResourceId(), "for admin" };
			}
		}

		DBFacade.getInstance().execute(sqls, params);
	}

	public void delFunc(String funcIds) throws Exception {
		String[] id = funcIds.split(",");
		String[] sqls = new String[3];
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < id.length; i++) {
			sbf.append("'" + id[i] + "',");
			// sqls[i] = "delete from p_rs_map where product_id ='" + id[i] +
			// "'";
		}
		String tempId = sbf.substring(0, sbf.lastIndexOf(","));
		StringBuffer popedomIds = new StringBuffer();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(
				"select popedom_id from p_popedom where func_id in (" + tempId
						+ ")", null);
		while (crs.next()) {
			String popedomId = crs.getString("popedom_id");
			popedomIds.append("'" + popedomId + "',");
		}
		String tempPopedomId = popedomIds.substring(0, popedomIds
				.lastIndexOf(","));

		sqls[0] = "delete from p_rs_map where popedom_id in (" + tempPopedomId
				+ ")";
		sqls[1] = "delete from p_popedom where func_id in (" + tempId + ") ";
		sqls[2] = "delete from p_func where func_id in (" + tempId + ")";

		DBFacade.getInstance().execute(sqls);
	}

	public List<MenuBean> getFuncListByFuncId(String funcId) throws Exception {
		List<MenuBean> list = new ArrayList<MenuBean>();
		String sql = "select a.*,b.resource_id tempId from p_popedom a, p_func b "
				+ "where a.func_id=b.func_id and a.func_id=? ";
		String[] param = new String[] { funcId };
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, param);
		while (crs.next()) {
			MenuBean bean = new MenuBean();
			bean.setFuncId(crs.getString("func_id"));
			bean.setResourceId(crs.getString("resource_id"));
			bean.setTempResourceId(crs.getString("tempId"));
			bean.setPopedomName(crs.getString("popedom_name"));
			bean.setPopedomId(crs.getString("popedom_id"));
			list.add(bean);
		}
		return list;
	}

	public void setFunc(MenuBean bean) throws Exception {
		String popedomIds = bean.getPopedomIdArray();
		String popedomNames = bean.getPopedomNameArray();
		String orderIds = bean.getOrderIdArray();
		String jspIcons = bean.getJspIconArray();
		String jspFuncs = bean.getJspFuncArray();
		String funcId = bean.getFuncId();
		String popedomId = (String) DBFacade.getInstance().getValueBySql(
				"select resource_id from p_func where func_id=?",
				new String[] { funcId });
		String delMap = "delete from p_rs_map where popedom_id in (select popedom_id from p_popedom where func_id=? and popedom_id != ?)";
		String[] delMapParam = new String[]{funcId , popedomId};
		
		String delPopedom = "delete from p_popedom where func_id=? and popedom_id != ?";
		String[] delPopedomParam = new String[]{funcId , popedomId};
		
		String[] popedomIdArray = popedomIds.split(",");
		String[] popedomNameArray = popedomNames.split(",");
		String[] orderIdArray = orderIds.split(",");
		String[] jspIconArray = jspIcons.split(",");
		String[] jspFuncArray = jspFuncs.split(",");
		String[] sqls = new String[(popedomIdArray.length - 1) * 2 + 3];
		String[][] params = new String[(popedomIdArray.length - 1) * 2 + 3][];
		sqls[0] = delMap;
		params[0] = delMapParam;
		sqls[1] = delPopedom;
		params[1] = delPopedomParam;
		int index = 0;
		for(int i = 0 ; i < popedomIdArray.length ; i++){
			String pId = popedomIdArray[i];
			String pName = popedomNameArray[i];
			String orderId = orderIdArray[i];
			String jspIcon = jspIconArray[i];
			String jspFunc = jspFuncArray[i];
			if(pId.equals(popedomId)){
				sqls[i + 2] = "update p_popedom set popedom_name=?,order_id=?,jsp_icon=?,jsp_func=? where popedom_id=?";
				params[i + 2] = new String[]{pName , orderId , jspIcon , jspFunc , pId};
			}else{
				sqls[i + 2] = "insert into p_popedom values(?,?,?,?,?,?,?,?)";
				params[i + 2] = new String[]{pId , funcId , pName , pId , orderId , jspIcon , jspFunc , "1"};
				sqls[sqls.length - 1 - index] = "insert into p_rs_map values(?,?,?,?)";
				params[sqls.length - 1 - index] = new String[]{DBFacade.getInstance().getID() , "00" , pId , "for admin"};
				index++;
			}
		}
//		for(String sql : sqls){
//			System.out.println("sql==" + sql);
//		}
		DBFacade.getInstance().execute(sqls , params);
	}

	public int[] setMenuList(String perNumber, String gotoPage, Page page, MenuBean bean, List<MenuBean> dataRows) {
		page.setRows(Integer.parseInt(perNumber));
		page.setPage(Integer.parseInt(gotoPage));
		String sql = "select a.func_id,a.func_name,a.func_url,a.resource_id,a.module_id,b.module_name,a.is_valid "
			+ "from p_func a, p_module b "
			+ "where 1 = 1 and a.module_id = b.module_id ";
		if (bean.getSearchField() != null && !"".equals(bean.getSearchField())) {
			sql = sql + " and " + bean.getSearchField() + " like '%"
					+ bean.getSearchValue() + "%' ";
		}
		String nodeId = bean.getMenuId();
		String parentId = bean.getPId();
		if (nodeId != null && !nodeId.trim().equals("00")) {
			if (parentId != null && parentId.trim().equals("00")) {
				sql = sql + " and b.menu_id = '" + nodeId.trim() + "' ";
			} else {
				sql = sql + " and a.module_id = '" + nodeId.trim() + "' ";
			}
		}
		String sidx = bean.getOrderByField();
		String sord = bean.getOrderByType();
		if (sidx == null || "".equals(sidx)) {
			sidx = "func_id";
			bean.setOrderByField(sidx);
		}
		if (sord == null || "".equals(sord)) {
			sord = "asc";
			bean.setOrderByType(sord);
		}
		page.setSidx(sidx);
		page.setSord(sord);
		PageBean pageData = new PageBean(sql, page);
		int[] record = new int[3];
		dataRows.addAll(getFuncList(pageData));
		record[0] = pageData.getPageAmount();
		record[1] = pageData.getPageNo();
		record[2] = pageData.getItemAmount();
		return record;
	}

}
