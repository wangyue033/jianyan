package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.ProductType;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;
import framework.dhcc.utils.LogUtil;

public class ProductTypeImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getProductTypeList(ProductType productType, String curPage, String perNumber,
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
		String sql = "select type_id,type_name,short_name,parent_id,type_desc,is_valid,opt_id,opt_name,opt_time" +
				" from jd_product_type where 1 = 1 ";

		if(productType.getParentId()!=null && productType.getParentId().length()>0){
			sql = sql + " and parent_id='" + productType.getParentId() + "' ";
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
			// TODO: handle exception
			page.setSidx("type_id");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<ProductType> list = new ArrayList<ProductType>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			ProductType bean = new ProductType();
			
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setTypeDesc(crs.getString("type_desc"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			
			//根据PID获取上级产品名称
			if (crs.getString("parent_id").equals("-1")) {
				bean.setParentName("");
			} else {
				String sql1 = "select type_name from jd_product_type where type_id = '" + crs.getString("parent_id") + "'";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, null);
				if (crs1.next()) {
					bean.setParentName(crs1.getString("type_name"));
				}
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
	
	public Map<String, Object> getProdtypeById(String typeId) throws Exception {
		String sql = "select type_id,type_name,short_name,parent_id,type_desc,is_valid,opt_id,opt_name,opt_time" +
				" from jd_product_type where type_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { typeId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ProductType bean = new ProductType();
			
			bean.setTypeId(crs.getString("type_id"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setParentId(crs.getString("parent_id"));
			bean.setTypeDesc(crs.getString("type_desc"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			
			//根据PID获取上级产品名称
			if (crs.getString("parent_id").equals("-1")) {
				bean.setParentName("");
			} else {
				String sql1 = "select type_name from jd_product_type where type_id = '" + crs.getString("parent_id") + "'";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, null);
				if (crs1.next()) {
					bean.setParentName(crs1.getString("type_name"));
				}
			}
			
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}
	
	public void addProductType(ProductType bean) throws Exception {
		
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = "insert into jd_product_type(type_id, type_name, short_name, parent_id, type_desc, is_valid, " +
				"opt_id, opt_name, opt_time)" +
				"values (?,?,?,?,?,?,?,?,now())";

		params[0]  = new String[] {DBFacade.getInstance().getID(), //bean.getProductId(),
				bean.getTypeName(), CnToPiny.getInstance().getPinYinHeadChar(bean.getTypeName()), bean.getParentId()==null||bean.getParentId().equals("")?"-1":bean.getParentId(),
				bean.getTypeDesc()==null||bean.getTypeDesc().equals("")?"无":bean.getTypeDesc(),	"1", bean.getOptId(), bean.getOptName() };

		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品类别信息添加",
				"产品分类ID：" + bean.getTypeId() + ";产品分类名称："
						+ bean.getTypeName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public Map<String, Object> editProdtype(ProductType bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		// TODO: handle exception
//		String[] sql = new String[1];
//		String[][] param = new String[1][];
//		String prId = bean.getProdtypeId();
//		if (bean.getIsValid().equals("1")) {//有效
//			while (true) {
//				sql[0] = "update jd_product_type_info set is_valid = ? where prodtype_id = ? ";
//				param[0] = new String[] {bean.getIsValid(), prId};
//				DBFacade.getInstance().execute(sql, param);
//				String s = "select parent_id from jd_product_type_info where prodtype_id = ? ";
//				CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[]{prId});
//				if (c.next()) {
//					prId = c.getString("parent_id");
//				}
//				if (prId.equals("-1")) {
//					break;
//				}
//			}
//		} else {//无效
//			while (true) {
//				sql[0] = "update jd_product_type_info set is_valid=? where prodtype_id = ? ";
//				param[0] = new String[] {bean.getIsValid(), prId};
//				DBFacade.getInstance().execute(sql, param);
//				String s = "select prodtype_id from jd_product_type_info where parent_id = ? ";
//				CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[]{prId});
//				//关联产品信息表
//				String s1 = "select id from jd_product_info where prodtype_id = ?";
//				CachedRowSetImpl c1 = DBFacade.getInstance().getRowSet(s1, new String[]{prId});
//				while (c1.next()) {
//					String[] s2 = {"update jd_product_info set is_valid=? where id = ? "};
//					String[][] p2 = new String[1][];
//					p2[0] = new String[]{"0", c1.getString("id")};
//					DBFacade.getInstance().execute(s2, p2);
//				}
//				//更新peId的值
//				if (c.next()) {
//					prId = c.getString("prodtype_id");
//				} else {
//					break;
//				}
//			}
//		}
		
		sqls[0] = " update jd_product_type set type_name=? , short_name=? , parent_id=? , type_desc=? , " +
				" opt_id=? , opt_name=? , opt_time=now() " +
				" where type_id=?";
		params[0] = new String[] { bean.getTypeName(), bean.getShortName(),
				bean.getParentId(), bean.getTypeDesc(), 
				bean.getOptId(), bean.getOptName(), bean.getTypeId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品类别信息修改",
				"产品分类ID：" + bean.getTypeId() + ";产品分类名称："
						+ bean.getTypeName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
	public Map<String, Object> delProductType(ProductType bean) throws SQLException {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[1];
		String[][] params = new String[1][];
		
		String pid = bean.getTypeId();
		while (true) {
			String[] s = {"delete from jd_product_type where type_id = ? "};
			String[][] pr = new String[1][];
			pr[0] = new String[]{pid};
			DBFacade.getInstance().execute(s, pr);
			String s1 = "select type_id from jd_product_type where parent_id = '" + pid + "'";
			CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s1, null);
			if (c.next()) {
				pid = c.getString("type_id");
			} else {
				break;
			}
		}
		
//		String s = "select parent_id from jd_product_type_info where prodtype_id = ? ";
//		CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[]{bean.getProdtypeId()});
//		if (c.next()) {
//			bean.setParentId(c.getString("parent_id"));
//		}
//		
//		Map<String, String> t=new HashMap<String, String>();
//		String sql = "select prodtype_id,parent_id from jd_product_type_info order by prodtype_id desc";
//		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
//		while (crs.next()) {
//			t.put(crs.getString("prodtype_id"), crs.getString("parent_id"));
//		}
//		removeTreeNodes(t,bean.getParentId());
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品类别信息删除",
				"产品分类ID：" + bean.getTypeId() + ";产品分类名称："
						+ bean.getTypeName());
		sqls[0] = (String) obj[0];
		params[0] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	/**
	 * 树形结构
	 *@return List<ZTreeBean>
	 */
	public List<ZTreeBean> getProdtypeTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select type_id,parent_id,type_name from jd_product_type where is_valid='1' order by type_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setpId(crs.getString("parent_id"));
				bean.setName(crs.getString("type_name"));
				bean.setDepth("1");
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getProdtypeTree1(ProductType bean1) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String p = bean1.getTypeId();
		String p1 = bean1.getTypeId();
		Map<String, String> t=new HashMap<String, String>();
		String sql ="select type_id,parent_id,type_name from jd_product_type where is_valid='1' order by type_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				t.put(crs.getString("type_id"), crs.getString("parent_id"));
				
//				if (crs.getString("type_id").equals(p1)) {
//					continue;
//				}
//				if (crs.getString("type_id").equals(p) || crs.getString("parent_id").equals(p)) {
//					p = crs.getString("type_id");
//					continue;
//				}
//				ZTreeBean bean = new ZTreeBean();
//				bean.setId(crs.getString("type_id"));
//				bean.setpId(crs.getString("parent_id"));
//				bean.setName(crs.getString("type_name"));
//				bean.setDepth("1");
//				treeList.add(bean);
			}
			t = getEditTreeNodes(t, p);
			t.remove(p1);
			Iterator<Map.Entry<String, String>> it = t.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				ZTreeBean bean = new ZTreeBean();
				bean.setId(entry.getKey());
				bean.setpId(entry.getValue());
				String sq = "select type_name from jd_product_type where type_id = ? ";
				CachedRowSetImpl cs = DBFacade.getInstance().getRowSet(sq, new String[]{entry.getKey()});
				if (cs.next()) {
					bean.setName(cs.getString("type_name"));
				}
				bean.setDepth("1");
				treeList.add(bean);
			}
			
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	// 产品类别名称重复验证
	public boolean checkPlanNo(String typeName, String typeId, int flag) {
		String sql = "select count(*) from jd_product_type where type_name = ?";
		if (flag == 1) {
			sql += " and type_id != " + typeId;
		}
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { typeName });
			if (count>0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	// 产品简码是否重复
	public boolean editCheckPlanNo(String shortName, String typeId, int flag) {
		String sql = "select count(*) from jd_product_type where short_name = ?";
		if (flag == 1) {
			sql += " and type_id != " + typeId;
		}
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { shortName });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean getDelProInfo(String typeId) {
		String sql = "select count(*) from jd_enterprise_product where type_id = ?";
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { typeId });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//递归删除所有的子节点
    public Map<String, String> removeTreeNodes(Map<String, String> t,String k){ 
        //所有需要删除的子节点
        List<String> sons=new ArrayList<String>();
        sons.add(k);
        List<String> temp=new ArrayList<String>();
        String[] sqls = new String [1];
        String[][] params = new String[1][];
        //循环递归删除，所有以k为父节点的节点
        while(true){        
            for(String s:sons){
                Set<String> keys=t.keySet();
                Iterator<String> it=keys.iterator();
                while(it.hasNext()){
                	String n=it.next();
                    //如果父节点（即Map的value）为需要删除的节点，则记录此节点，并在Map中删除
                    if(t.get(n).equals(s)){
                        temp.add(n);
//                        it.remove();
                        sqls[0] = "delete from jd_product_type where type_id = ? ";
                		params[0] = new String[] {n+""};
                		DBFacade.getInstance().execute(sqls, params);
//                      System.out.println("删除了ID=["+n+"]的节点,其父节点为["+s+"]");
                    }
                }
            }

            //如果此节点包含子节点，则将子节点赋值给sons;否则表示所有子节点已经删除，结束循环
            if(temp.size()>0){
                sons=temp;    
                temp=new CopyOnWriteArrayList<String>();
            }else{
                break;
            }
        }
        return t;
    }
    
    public Map<String, String> getEditTreeNodes(Map<String, String> t,String k){ 
    	//所有需要删除的子节点
    	List<String> sons=new ArrayList<String>();
    	sons.add(k);
    	List<String> temp=new ArrayList<String>();
//    	String[] sqls = new String [1];
//    	String[][] params = new String[1][];
    	//循环递归删除，所有以k为父节点的节点
    	while(true){        
    		for(String s:sons){
    			Set<String> keys=t.keySet();
    			Iterator<String> it=keys.iterator();
    			while(it.hasNext()){
    				String n=it.next();
    				//如果父节点（即Map的value）为需要删除的节点，则记录此节点，并在Map中删除
    				if(t.get(n).equals(s)){
    					temp.add(n);
                        it.remove();
//    					sqls[0] = "delete from jd_product_type where type_id = ? ";
//    					params[0] = new String[] {n+""};
//    					DBFacade.getInstance().execute(sqls, params);
//                      System.out.println("删除了ID=["+n+"]的节点,其父节点为["+s+"]");
    				}
    			}
    		}
    		
    		//如果此节点包含子节点，则将子节点赋值给sons;否则表示所有子节点已经删除，结束循环
    		if(temp.size()>0){
    			sons=temp;    
    			temp=new CopyOnWriteArrayList<String>();
    		}else{
    			break;
    		}
    	}
    	return t;
    }

	/** *******************
	* @return
	* 2017-3-21
	* *******************
	*/
    public List<ZTreeBean> getTypeAllRoot(String typeName) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		List<ZTreeBean> treeList2 = new ArrayList<ZTreeBean>();
		String sql = "";
		if(!typeName.equals("")&&!typeName.equals(null)&&!typeName.equals("undefined")){
			String sql0 = "select type_id,type_name,parent_id from jd_product_type where type_name like '"+typeName+"%'";
			CachedRowSetImpl crs0 = DBFacade.getInstance().getRowSet(sql0, null);
			try {
				while(crs0.next()){
					ZTreeBean bean0 = new ZTreeBean();
					bean0.setId(crs0.getString("type_id"));
					bean0.setName(crs0.getString("type_name"));
					bean0.setpId(crs0.getString("parent_id"));
					bean0.setOpen(false);
					treeList.add(bean0);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			sql = "select a.type_id,a.type_name,a.parent_id from jd_product_type a left join jd_product_type b on a.parent_id=b.type_id where a.is_valid = '1' and b.type_name like '"+typeName+"%'";
		}else{
			sql = "select type_id,type_name,parent_id from jd_product_type where is_valid = '1' ";
		}
		
		//sql = "select type_id,type_name,parent_id from jd_product_type where is_valid = '1' ";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			if(!typeName.equals("")&&!typeName.equals(null)&&!typeName.equals("undefined")){
				while (crs.next()) {
					ZTreeBean bean = new ZTreeBean();
					bean.setId(crs.getString("type_id"));
					bean.setName(crs.getString("type_name"));
					bean.setpId(crs.getString("parent_id"));
					treeList2.add(bean);
				}
				String sql1 = "select type_id,type_name,parent_id from jd_product_type where is_valid = '1' ";
				if(treeList2 != null && treeList2.size()>0){
					String condi = "";
					ZTreeBean obj = new ZTreeBean();
					for(int i = 0;i<treeList2.size();i++){
						obj = treeList2.get(i);
						if(i == 0){
							condi = "and (type_id like '"+obj.getId()+"%' ";
						}else{

							condi = condi + "or type_id like '"+obj.getId()+"%' ";
						}
					}
					sql1 += condi + ")";
				}
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, null);
				while(crs1.next()){
					ZTreeBean bean1 = new ZTreeBean();
					bean1.setId(crs1.getString("type_id"));
					bean1.setName(crs1.getString("type_name"));
					bean1.setpId(crs1.getString("parent_id"));
					bean1.setOpen(false);
					treeList.add(bean1);
				}
			}else{
				while (crs.next()) {
					ZTreeBean bean = new ZTreeBean();
					bean.setId(crs.getString("type_id"));
					bean.setName(crs.getString("type_name"));
					bean.setpId(crs.getString("parent_id"));
					bean.setOpen(false);
					treeList.add(bean);
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无产品分类信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

}
