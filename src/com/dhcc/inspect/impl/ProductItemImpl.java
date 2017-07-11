package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.ProductItem;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-3-3    
 * 产品检测项目Impl
 */
public class ProductItemImpl {
	Page page = new Page();

	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-3-3
	* 获取产品检测项目信息列表
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public String getProductItemList(ProductItem bean1,String curPage, String perNumber,
			String orderByField, String orderByType, String searchField,
			String searchValue) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,"
				+ "a.min_value,a.standard_value,a.meter_unit,a.is_valid,a.opt_id,a.opt_name,a.opt_time,shape_material,grade_model,"
				+ "b.standard_name,c.type_name "
				+ "from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id = b.standard_id and b.type_id = c.type_id ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		String standardIds = bean1.getStandardIds();
		
		if(standardIds != null){
			if("".equals(standardIds)){
				//sql = sql + "and a.standard_id in ('')";
			}else{
				sql = sql + " and a.standard_id in (" + bean1.getStandardIds() + ") ";
			}
		}
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderByType);
		} else {
			page.setSidx("a.item_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<ProductItem> list = new ArrayList<ProductItem>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			ProductItem bean = new ProductItem();
			bean.setItemId(crs.getString("item_id"));
			bean.setStandardId(crs.getString("standard_id"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setItemName(crs.getString("item_name"));
			bean.setSecondName(crs.getString("second_name"));
			bean.setThirdName(crs.getString("third_name"));
			bean.setMaxValue(crs.getString("max_value"));
			bean.setMinValue(crs.getString("min_value"));
			bean.setStandardValue(crs.getString("standard_value"));
			bean.setMeterUnit(crs.getString("meter_unit"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setShapeMaterial(crs.getString("shape_material"));
			bean.setGradeModel(crs.getString("grade_model"));
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

	/** *******************
	* @param itemName
	* @return
	* 2017-3-3
	* 添加产品检测项目信息是验证项目名称的重复
	*/
	public boolean checkProductItemName(String itemName) {
		String sql="select count(*) from jd_product_item where item_name = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { itemName });
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @param bean
	* 2017-3-3
	* 添加产品检测项目信息
	*/
	public void addProductItem(ProductItem bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "insert into jd_product_item(item_id,standard_id,item_name," +
				"second_name,third_name,max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic,charge_discount,main_comp,org_id,org_name," +
				"is_valid,opt_id,opt_name,opt_time,item_classify) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,now(),?) ";
		params[0] = new String[] { DBFacade.getInstance().getID(),bean.getStandardId(),bean.getItemName(),
				bean.getSecondName(), bean.getThirdName(), bean.getMaxValue(), bean.getMinValue(),
				bean.getStandardValue(), bean.getMeterUnit(),bean.getShapeMaterial(),bean.getGradeModel(),
				bean.getSampleNo(),bean.getSpecialJudge(),bean.getInspectModel(),bean.getChargeStandard(),
				bean.getChargeBasic(),bean.getChargeDiscount(),bean.getMainComp(),bean.getOrgId(),
				bean.getOrgName(), bean.getOptId(),bean.getOptName(),bean.getItemClassify()};
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"产品检测项目信息添加",
				"检测标准ID：" + bean.getStandardId() + ";产品检测项目名称："
						+ bean.getItemName(), "1" };
		
		DBFacade.getInstance().execute(sqls, params);
	}

	/** *******************
	* @param bean
	* @return
	* 2017-3-3
	* 获取检测标准列表
	*/
	public String getStandardList(ProductItem bean) {
		List<ProductItem> list = new ArrayList<ProductItem>();
		String sql = "select standard_id,standard_name " +
				"from jd_product_standard where is_valid = 1";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				ProductItem jdprog = new ProductItem();
				jdprog.setStandardId(crs.getString("standard_id"));
				jdprog.setStandardName(crs.getString("standard_name"));	
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

	/** *******************
	* @param itemId
	* @return
	* 2017-3-3
	* 获取项目的详细信息
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public Map<String, Object> getProductItemById(String itemId) throws SQLException, ParseException {
		String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,"
				+ "a.min_value,a.standard_value,a.meter_unit,a.is_valid,a.opt_id,a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+ " opt_time,"
				+ "a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp,a.org_id,a.org_name, "
				+ "a.item_classify,b.standard_name,b.standard_no,c.type_name "
				+ "from jd_product_item a,jd_product_standard b,jd_product_type c " 
				+ "where a.standard_id = b.standard_id "
				+ "and b.type_id = c.type_id "
				+"and a.item_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { itemId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ProductItem bean = new ProductItem();
			bean.setItemId(crs.getString("item_id"));
			bean.setStandardId(crs.getString("standard_id"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setItemName(crs.getString("item_name"));
			bean.setSecondName(crs.getString("second_name"));
			bean.setThirdName(crs.getString("third_name"));
			bean.setMaxValue(crs.getString("max_value"));
			bean.setMinValue(crs.getString("min_value"));
			bean.setStandardValue(crs.getString("standard_value"));
			bean.setMeterUnit(crs.getString("meter_unit"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setItemClassify(crs.getString("item_classify"));
			bean.setShapeMaterial(crs.getString("shape_material"));
			bean.setGradeModel(crs.getString("grade_model"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setSpecialJudge(crs.getString("special_judge"));
			bean.setInspectModel(crs.getString("inspect_model"));
			bean.setChargeStandard(crs.getString("charge_standard"));
			bean.setChargeBasic(crs.getString("charge_basic"));
			bean.setChargeDiscount(crs.getString("charge_discount"));
			bean.setMainComp(crs.getString("main_comp"));
			bean.setOrgId(crs.getString("org_id"));
			bean.setOrgName(crs.getString("org_name"));
			bean.setTypeName(crs.getString("type_name"));
			bean.setStandardNo(crs.getString("standard_no"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}
	
	public Map<String, Object> getProductStandardById(String standardId) throws SQLException, ParseException {
		String sql = "select a.standard_no,a.standard_name,b.type_name from jd_product_standard a,jd_product_type b  where a.type_id = b.type_id and a.standard_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { standardId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			ProductItem bean = new ProductItem();
			bean.setStandardNo(crs.getString("standard_no"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setTypeName(crs.getString("type_name"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}
	/** *******************
	* @param itemName
	* @param itemId
	* @return
	* 2017-3-3
	* 修改项目信息时项目名称的重复验证
	*/
	public boolean checkEditJdCompName(String itemName, String itemId) {
		String sql="select count(*) from jd_product_item where item_name = ? and item_id != ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { itemName,itemId});
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-3-3
	* 修改项目信息
	*/
	public Map<String, Object> editProductItem(ProductItem bean) {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		sqls[0] = "update jd_product_item set item_name=?,standard_id=?,second_name=?," +
				"third_name=?,max_value=?,min_value=?,standard_value=?,meter_unit=?,is_valid=?,item_classify=?," +
				"opt_id=?,opt_name=?,opt_time=now(),shape_material=?,grade_model=?,sample_no=?,special_judge=?,inspect_model=?,charge_standard=?,charge_basic=?,charge_discount=? where item_id=?";
		params[0] = new String[] { bean.getItemName(), bean.getStandardId(),bean.getSecondName(),
				bean.getThirdName(), bean.getMaxValue(), bean.getMinValue(),bean.getStandardValue(),
				bean.getMeterUnit(), bean.getIsValid(),bean.getItemClassify(),bean.getOptId(),bean.getOptName(),bean.getShapeMaterial(),bean.getGradeModel(),
				bean.getSampleNo(),bean.getSpecialJudge(),bean.getInspectModel(),bean.getChargeStandard(),
				bean.getChargeBasic(),bean.getChargeDiscount(),bean.getItemId() };
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "项目信息修改",
				"项目信息ID：" + bean.getItemId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-3-3
	* 删除项目信息
	*/
	public Map<String, Object> delProductItem(ProductItem bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_product_item where item_id like ?";
		params[0] = new String[] { bean.getItemId() + "%" };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "项目信息删除",
				"项目信息ID：" + bean.getItemId(), "1" };

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @return
	* 2017-3-20
	* 获取分类标准数据
	*/
	public List<ZTreeBean> getStandardAllRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select type_id,type_name,parent_id from jd_product_type where is_valid = '1' ";
		String sql2 = "select standard_id,standard_name,type_id parent_id from jd_product_standard where is_valid = '1' ";
		
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("type_id"));
				bean.setName(crs.getString("type_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setOpen(false);
				bean.setRemark("1");
				treeList.add(bean);
			}
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, null);
			while (crs2.next()) {
				ZTreeBean bean2 = new ZTreeBean();
				bean2.setId(crs2.getString("standard_id"));
				bean2.setName(crs2.getString("standard_name"));
				bean2.setpId(crs2.getString("parent_id"));
				bean2.setOpen(false);
				bean2.setRemark("2");
				treeList.add(bean2);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无分类标准信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}

}
