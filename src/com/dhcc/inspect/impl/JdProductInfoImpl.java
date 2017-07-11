package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdProductInfo;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;
import framework.dhcc.utils.LogUtil;

/**
 * 
 * @author longxialin
 * 产品信息
 */
public class JdProductInfoImpl {
	
	Page page = new Page();

	public String getJdProductInfoList(JdProductInfo jdProductInfo, String curPage, String perNumber,
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
		String sql = "select a.product_id,a.type_id,a.enterprise_id,a.product_name,a.short_name,a.product_desc," +
				"a.product_standard,a.product_model,a.is_valid,a.opt_id,a.opt_name,a.opt_time,b.enterprise_name," +
				"c.type_name from jd_enterprise_product a,jd_enterprise_info b,jd_product_type c where " +
				"a.enterprise_id = b.enterprise_id and a.type_id = c.type_id and b.is_valid = '1' and c.is_valid = '1' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			if (searchField.equals("short_name")) {
				sql += " and a." + searchField + " like '%" + searchValue + "%' ";
			} else {
				sql = sql + " and " + searchField + " like '%" + searchValue + "%' ";
			}
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			// TODO: handle exception
			page.setSidx("product_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdProductInfo> list = new ArrayList<JdProductInfo>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdProductInfo bean = new JdProductInfo();

			bean.setProductId(crs.getString("product_id"));
			bean.setTypeId(crs.getString("type_id"));
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setProductDesc(crs.getString("product_desc"));
			bean.setProductStandard(crs.getString("product_standard"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date t = crs.getDate("opt_time");
			if (t == null || "".equals(t)) {
				bean.setOptTime("");
			} else {
				bean.setOptTime(format.format(t));
			}
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setTypeName(crs.getString("type_name"));
			
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

	public void addJdProductInfo(JdProductInfo bean) throws Exception {
		
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		bean.setProductId(DBFacade.getInstance().getID());
		sqls[0] = "insert into jd_enterprise_product(product_id,type_id,enterprise_id,product_name,short_name," +
				"product_desc,product_standard,product_model,is_valid,opt_id,opt_name,opt_time,trade_mark)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,now(),?) ";

		params[0]  = new String[] {bean.getProductId(),bean.getTypeId(),bean.getEnterpriseId(),
				bean.getProductName(),CnToPiny.getInstance().getPinYinHeadChar(bean.getProductName()),
				bean.getProductDesc()==null||bean.getProductDesc().equals("")?"无":bean.getProductDesc(),
				bean.getProductStandard(), bean.getProductModel(), "1", bean.getOptId(),bean.getOptName(), bean.getTradeMark() };

	
		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品信息添加",
				"企业产品ID：" + bean.getProductId() + ";产品名称："
						+ bean.getProductName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public void addBillJdProductInfo(JdProductInfo bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into jd_enterprise_product(product_id,type_id,enterprise_id,product_name,short_name," +
				"product_desc,product_standard,product_model,is_valid,opt_id,opt_name,opt_time,trade_mark)" +
				" values (?,?,?,?,?,?,?,?,?,?,?,now(),?) ";

		params[0]  = new String[] {bean.getProductId(),bean.getTypeId(),bean.getEnterpriseId(),
				bean.getProductName(),CnToPiny.getInstance().getPinYinHeadChar(bean.getProductName()),
				bean.getProductDesc()==null||bean.getProductDesc().equals("")?"无":bean.getProductDesc(),
				bean.getProductStandard(), bean.getProductModel(), "1", bean.getOptId(),bean.getOptName(), bean.getTradeMark() };

	
		// TODO: handle exception
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽样单录入时产品信息添加",
				"企业产品ID：" + bean.getProductId() + ";产品名称："
						+ bean.getProductName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}

	public Map<String, Object> editJdProductInfo(JdProductInfo bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "update jd_enterprise_product set type_id=?,enterprise_id=?," +
				"product_name=?,short_name=?,product_desc=?,product_standard=?,product_model=?," +
				"opt_id=?,opt_name=?,opt_time=now(),trade_mark=? where product_id=? ";
		params[0] = new String[] { bean.getTypeId(), bean.getEnterpriseId(), bean.getProductName(), bean.getShortName(),bean.getProductDesc(),
				bean.getProductStandard(), bean.getProductModel(), bean.getOptId(),bean.getOptName(),bean.getTradeMark(),bean.getProductId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品信息修改",
				"企业产品ID：" + bean.getProductId() + ";产品名称："
						+ bean.getProductName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdProductInfo(JdProductInfo bean) throws SQLException {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
//		String s = "select parent_id from jd_product_info where product_id = ? ";
//		CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[]{bean.getProductId()});
//		if (c.next()) {
//			bean.setParentId(c.getString("parent_id"));
//		}
//		
//		Map<String, String> t=new HashMap<String, String>();
//		String sql = "select product_id,parent_id from jd_product_info";
//		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
//		while (crs.next()) {
//			t.put(crs.getString("product_id"), crs.getString("parent_id"));
//		}
//		removeTreeNodes(t,bean.getParentId());
		
		sqls[0] = "delete from jd_enterprise_product where product_id = ? ";
		params[0] = new String[] { bean.getProductId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "产品信息删除",
				"企业产品ID：" + bean.getProductId() + ";产品名称："
						+ bean.getProductName());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> getJdProductById(String productId) throws Exception {
		String sql = "select a.trade_mark,a.product_id,a.type_id,a.enterprise_id,a.product_name,a.short_name,a.product_desc," +
				"a.product_standard,a.product_model,a.is_valid,a.opt_id,a.opt_name,a.opt_time,b.enterprise_name," +
				"c.type_name from jd_enterprise_product a,jd_enterprise_info b,jd_product_type c where " +
				"a.enterprise_id = b.enterprise_id and a.type_id = c.type_id and b.is_valid = '1' and c.is_valid = '1'" +
				" and a.product_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { productId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdProductInfo bean = new JdProductInfo();

			bean.setTradeMark(crs.getString("trade_mark"));
			bean.setProductId(crs.getString("product_id"));
			bean.setTypeId(crs.getString("type_id"));
			bean.setEnterpriseId(crs.getString("enterprise_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setShortName(crs.getString("short_name"));
			bean.setProductDesc(crs.getString("product_desc"));
			bean.setProductStandard(crs.getString("product_standard"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date t = crs.getDate("opt_time");
			if (t == null || "".equals(t)) {
				bean.setOptTime("");
			} else {
				bean.setOptTime(format.format(t));
			}
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setTypeName(crs.getString("type_name"));
			
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	
	// 产品编号重复验证
	public boolean checkPlanNo(String productName, String productId, int flag) {
		String sql = "select count(*) from jd_enterprise_product where product_name = ?";
		if (flag == 1) {
			sql += " and product_id != '" + productId + "' ";
		}
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { productName });
			if (count>0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	

	// 产品简码是否重复
	public boolean editCheckPlanNo(String shortName, String productId, int flag) {
		String sql = "select count(*) from jd_enterprise_product where short_name = ?";
		if (flag == 1) {
			sql += " and product_id != '" + productId + "' ";
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
	
	/**
	 * 树形结构
	 *@return List<ZTreeBean>
	 */
	public List<ZTreeBean> getTreeCanParentRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select type_id,parent_id,type_name FROM jd_product_type where is_valid='1' order by type_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				String sql1 = "select product_id,type_id,product_name from jd_enterprise_product where type_id = ? ";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[]{crs.getString("type_id")});
				while (crs1.next()) {
					ZTreeBean bean = new ZTreeBean();
					bean.setId(crs1.getString("product_id"));
					bean.setpId(crs1.getString("type_id"));
					bean.setName(crs1.getString("product_name"));
					treeList.add(bean);
				}
				
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setpId(crs.getString("parent_id"));
				bean.setName(crs.getString("type_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getTreeCanParentRoot1() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select type_id,parent_id,type_name FROM jd_product_type where is_valid='1' order by type_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setpId(crs.getString("parent_id"));
				bean.setName(crs.getString("type_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getCompTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select enterprise_id,enterprise_name from jd_enterprise_info where is_valid = '1' order by enterprise_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("enterprise_id"));
				bean.setName(crs.getString("enterprise_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public String getCompList(JdProductInfo bean) {
		List<JdProductInfo> list = new ArrayList<JdProductInfo>();
		String sql = "select enterprise_id,enterprise_name from jd_enterprise_info where is_valid = '1'";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,null);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				JdProductInfo jdprog = new JdProductInfo();
				jdprog.setEnterpriseId(crs.getString("enterprise_id"));
				jdprog.setEnterpriseName(crs.getString("enterprise_name"));	
				list.add(jdprog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("list", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	/*public String getProductId(String pId) {
		String cId = "";
		String sql = "select ifnull(max(product_id),'') from jd_product_info where parent_id = ? ";
		String max = DBFacade.getInstance()
				.getValueBySql(sql, new String[] { pId }).toString();
		int item = 0;
		if (pId == null || pId.equals("undefined")) {
			String sql1 = "select count(product_id) from jd_product_info where parent_id = '-1'";
			pId = DBFacade.getInstance().getValueBySql(sql1, new String[] {}).toString();
			
			sql1 = "select ifnull(max(product_id),'') from jd_product_info where parent_id = '-1' ";
			String m = DBFacade.getInstance().getValueBySql(sql1, new String[] {}).toString();
			if (!m.equals("")) {
				int ma = Integer.parseInt(m.substring(0, m.length() / 2));
				if (ma - Integer.parseInt(pId) > 0) {
					pId = ma + 1 + "";
				}
			}
		}
		if(max!=null && !max.isEmpty()) {
			item = Integer.valueOf(max.substring(max.length()-2));
		}
		item = item+1;
		if (item < 10) {
			cId = pId + "0" + item;
		} else {
			cId = pId + item;
		}
		return cId;
	}
	
	public boolean productLength(String pId) {
		String id = getProductId(pId);
		return id.length() > 20;
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
                        it.remove();
                        sqls[0] = "update jd_product_info set is_valid=? where product_id = ? ";
                		params[0] = new String[] { "0", n+""};
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
    }*/
}
