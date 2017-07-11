package com.dhcc.inspect.impl;

import java.sql.SQLException;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectData;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据填报Impl
 */
public class JdInspectDataImpl {
	Page page = new Page();

	/**
	 * 获取检验模板名称
	 * @throws SQLException 
	 */
	public String getTempletName(String templetId) throws SQLException{
		String sql = "select templet_name "
				+ "from jd_inspect_templet where templet_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { templetId });
		String templetName = "";
		if(crs.next()){
			templetName = crs.getString("templet_name");
		}
		return templetName;
	}
	
	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-2-22
	* 获取检验数据管理列表
	 * @throws SQLException 
	 * @throws ParseException 
	*/
	public String getJdInspectDataList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String compId) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select c.scdw_id,e.comp_id,c.report_no,b.out_flag,d.tran_title,d.deal_view,d.back_type,c.current_node_id,c.ssim_title,b.inspect_id,b.bill_id,c.sample_no,c.product_model,c.tran_id,b.main_opt_id,b.main_opt_name,b.fill_status,b.is_qualified," +
				"c.product_id,c.product_name,b.check_status,b.is_submit from jd_inspect b left join jd_sample_bill c on b.bill_id=c.bill_id left join dh_tranlist d on c.tran_id = d.tran_id left join jd_plan_task e on c.task_id= e.task_id "+
				"where  b.is_valid='1' and (b.sign_status= '1' or b.out_flag='2') and b.org_id like '%"+compId+"%' ";
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
			page.setSidx("b.inspect_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectData> list = new ArrayList<JdInspectData>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectData bean = new JdInspectData();
			bean.setTranId(crs.getString("tran_id"));
			if("1".equals(crs.getString("back_type"))){
				bean.setTranTitle(crs.getString("tran_title"));
				bean.setDealView(crs.getString("deal_view"));
				bean.setBackType(crs.getString("back_type"));
			}else{
				String sql1 = "select tran_title,deal_view,back_type from dh_tranlist where tran_id = (select deal_view from dh_tranlist where pre_tran_id=? and work_status='0')";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[]{bean.getTranId()});
				if(crs1.next()){
					bean.setTranTitle(crs1.getString("tran_title"));
					bean.setDealView(crs1.getString("deal_view"));
					bean.setBackType(crs1.getString("back_type"));
				}
			}
			bean.setScdwId(crs.getString("scdw_id"));
			bean.setOutFlag(crs.getString("out_flag"));
			bean.setReportNo(crs.getString("report_no"));
			bean.setCompId(crs.getString("comp_id"));
			bean.setCurrentStatus(crs.getString("current_node_id"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setDataId(crs.getString("fill_status"));
			bean.setIsQualified(crs.getString("is_qualified"));
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setIsSubmit(crs.getString("is_submit"));
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
	* @param bean
	* @return
	* 2017-2-22
	* 获取检验数据管理里属于该操作人员的检测项目列表
	*/
	public Map<String, Object> getJdItemById(String inspectId) {
		String sql2 = "select a.inspect_task_id, a.item_id , a.standard_id , a.item_name , a.second_name, a.third_name, "
				+ " a.max_value , a.min_value , a.standard_value , a.meter_unit , a.inspect_value ,b.standard_name,a.inspect_result,a.inspect_opt_name "
				+ " from jd_inspect_item a left join jd_product_standard b on a.standard_id = b.standard_id "
				+ " where a.inspect_id = ? order by a.item_name,a.second_name,a.third_name ";
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
				new String[] {inspectId });
		List<JdInspectData> list = new ArrayList<JdInspectData>();
		try{
			while(crs2.next()){
				JdInspectData bean2 = new JdInspectData();
				bean2.setInspectTaskId(crs2.getString("inspect_task_id"));
				bean2.setItemId(crs2.getString("item_id"));
				bean2.setStandardId(crs2.getString("standard_id"));
				bean2.setStandardName(crs2.getString("standard_name"));
				bean2.setItemName(crs2.getString("item_name"));
				bean2.setSecondName(crs2.getString("second_name"));
				bean2.setThirdName(crs2.getString("third_name"));
				bean2.setMaxValue(crs2.getString("max_value"));
				bean2.setMinValue(crs2.getString("min_value"));
				bean2.setStandardValue(crs2.getString("standard_value"));
				bean2.setMeterUnit(crs2.getString("meter_unit"));
				bean2.setInspectValue(crs2.getString("inspect_value"));
				bean2.setInspectResult(crs2.getString("inspect_result"));
				bean2.setInspectOptName(crs2.getString("inspect_opt_name"));
				list.add(bean2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("items", list);
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-22
	* 获取检验模板的列表
	*/
	public String getDataList2(JdInspectData bean) {
		List<JdInspectData> list = new ArrayList<JdInspectData>();
		String sql = "select templet_id,templet_name,templet_content " +
				"from jd_inspect_templet where 1 = 1";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				JdInspectData jdprog = new JdInspectData();
//				jdprog.setTempletId(crs.getString("templet_id"));
//				jdprog.setTempletName(crs.getString("templet_name"));
//				jdprog.setTempletContent(crs.getString("templet_content"));
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
	* @param bean
	* @return
	* 2017-2-22
	* 获取检验的产品信息
	*/
	public String getProductList(JdInspectData bean) {
		List<JdInspectData> list = new ArrayList<JdInspectData>();
		String sql = "select b.product_id " +
				"from jd_inspect a,jd_sample_bill b " +
				"where a.bill_id = b.bill_id and a.inspect_id = ?";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{bean.getInspectId()});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				JdInspectData jdprog = new JdInspectData();
				jdprog.setProductId(crs.getString("product_id"));
				String sql2 = "select product_name from jd_product_info where product_id = '"+crs.getString("product_id")+"'";
				CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[]{});
				while (crs2.next()) {
					jdprog.setProductName(crs2.getString("product_name"));
				}
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
	* @param idmTitle
	* @return
	* 2017-2-22
	* 检验数据标题重复验证
	*/
	public boolean checkJdInspectDataName(String idmTitle) {
		String sql="select count(*) from jd_inspect_data where idm_title = ?";
		try{
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { idmTitle });
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
	* 2017-2-22
	* 添加检验数据信息
	*/
	public void addJdInspectData(JdInspectData bean) {
		System.out.println(bean.getTranId());
		System.out.println(bean.getOutFlag());
		String[] inspectTaskId = bean.getInspectTaskId().split(",");
		int len = inspectTaskId.length;
		String[] inspectValue = bean.getInspectValue().split(",");
		String[] inspectResult = bean.getInspectResult().split(",");
		String[] standardValue = bean.getStandardValue().split(",");
//		System.out.println(bean.getStandardValue());
		String[] sqls = null;
		String[][] params = null;
		if("1".equals(bean.getOutFlag())){//标志为1，本机构检验,没有留痕
			sqls = new String[1 + len ];
			params = new String[1 + len][];
			sqls[0] = "update jd_inspect set fill_status =?,is_qualified=?,fill_desc=?,inspect_date=?,fill_opt_id=?,fill_opt_name=?,fill_opt_time=now(),is_submit='0',check_status='0' where inspect_id=? ";
			params[0] =new String []{"1",bean.getIsQualified(),bean.getFillDesc(),bean.getInspectDate(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
		}else{//外送检验，增加留痕信息，结束流程
			String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0' ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { bean.getTranId()});
			String tranId = null;
			String preTranId = bean.getTranId();
			try {
				if(crs.next()){
					tranId = crs.getString("tran_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sqls = new String[4 + len ];
			params = new String[4 + len][];
			sqls[0] = "update jd_inspect set fill_status =?,is_qualified=?,fill_desc=?,inspect_date=?,fill_opt_id=?,fill_opt_name=?,fill_opt_time=now(),is_submit='0',check_status='0',out_result='1' where inspect_id=? ";
			params[0] =new String []{"1",bean.getIsQualified(),bean.getFillDesc(),bean.getInspectDate(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
			
			sqls[1+len] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1," +
					"deal_status='1',work_status='0',deal_view=? where pre_tran_id=? and work_status='0'";
			params[1+len] = new String[] { bean.getOptId2(),bean.getOptName2(),bean.getFillDesc(),bean.getTranId()};
			
			sqls[2+len] = "update jd_sample_bill set current_node_id=?,current_status='5',tran_id=? where bill_id=? ";
			params[2+len] = new String[] {FlowConstant.CJ_NODE_ARR_ID[4],tranId,bean.getBillId()};
			
			sqls[3+len] = "insert into jd_inspect_report(report_id,inspect_id,bill_id,verification_code,report_type,product_id," +//6
					"depart_id,ep_id,inspect_type,opt_id,opt_name,opt_time) " +//8
					"values (?,?,?,?,'2',?,?,?,'1',?,?,now())";
			params[3+len] = new String[] { DBFacade.getInstance().getID(),bean.getInspectId(),bean.getBillId(),getNumr(8),bean.getProductId(),
					bean.getCompId(),bean.getScdwId(),bean.getOptId2(),bean.getOptName2()};
			
			
		}
//		sqls[0] = "insert into jd_inspect_data(data_id,inspect_id,idm_title," +
//				"ita_person,inspect_time,product_id,index_id,qualified_standard," +
//				"is_qualified,is_valid,check_status,remark,opt_id,opt_time) " +
//				"values (?,?,?,?,?,?,?,?,?,1,0,?,?,now()) ";
//		params[0] = new String[] { DBFacade.getInstance().getID(),bean.getInspectId(),bean.getIdmTitle(),
//				bean.getPerson(), bean.getInspectTime(),bean.getProductId(),bean.getIndexId(),
//				bean.getQualifiedStandard(), bean.getIsQualified(),bean.getRemark2(),bean.getOptId2()};
//		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
//				+ "values(?,?,?,?,now(),?,?)";
//		params[1] = new String[] {
//				DBFacade.getInstance().getID(),
//				bean.getOptId2(),
//				bean.getOptName2(),
//				"检验数据信息添加",
//				"检验任务ID：" + bean.getInspectId() + ";检验数据标题："
//						+ bean.getIdmTitle(), "1" };
		
		
		/*sqls[2] = "update jd_inspect set sign_status=2 where Inspect_id=?";
		params[2] = new String[] {bean.getInspectId()};
		*/
		for (int i = 1; i < len + 1; i++) {
			sqls[i] = "update jd_inspect_item set inspect_value = ?,inspect_result=?,standard_value = ? "
				 + " where inspect_task_id = ?";
			params[i] = new String[] { inspectValue[i-1],inspectResult[i-1],standardValue[i-1],inspectTaskId[i-1]};
		}
		DBFacade.getInstance().execute(sqls, params);
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-22
	* 删除未提交的检验数据信息
	*/
	public Map<String, Object> delJdInspectData(JdInspectData bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = null;
		String[][] params = null;
		if("2".equals(bean.getOutFlag())){
			sqls = new String[6];
			params = new String[6][];
			sqls[2] = "DELETE from jd_inspect_report where bill_id = ?";
			params[2] = new String[] { bean.getBillId()};
			String sql2 = "select tran_id from jd_sample_bill where bill_id=?";
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[] { bean.getBillId() });
			String tranId = null;
			try {
				if(crs2.next()){
					tranId = crs2.getString("tran_id");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			String sql = "select pre_tran_id,work_id,flow_id,node_id,step_id,detail_id," +
					"create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name" +
					" from dh_tranlist where tran_id=? and work_status='0'";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { tranId });
			try {
				if(crs.next()){
					sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id," +
							"belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id," +
							"create_opt_name,create_opt_time,deal_part_id,deal_part_name,deal_opt_id," +
							"deal_opt_name,deal_opt_time,deal_time,over_status,deal_status,work_status," +
							"opt_order,data_resource,data_type,pre_tran_id) "
							+ " values (?,?,?,?,?,?,'jd_sample_bill','检验任务外送',?,?,?,?,now(),?,?,?,?,now(),'0','0','1','0','11','1','1',?)";
					params[3] = new String[] {DBFacade.getInstance().getID(),crs.getString("work_id"),FlowConstant.CJ_FLOW_ID,
							FlowConstant.CJ_NODE_ARR_ID[10],FlowConstant.CJ_STEP_ARR_ID[10],crs.getString("detail_id"),crs.getString("create_dept_id"),
							crs.getString("create_dept_name"),crs.getString("create_opt_id"), crs.getString("deal_opt_name"),
							crs.getString("deal_part_id"),crs.getString("deal_part_name"),crs.getString("deal_opt_id"), crs.getString("deal_opt_name"),crs.getString("pre_tran_id")};
					sqls[4] = "update jd_sample_bill set tran_id =? where bill_id=?";
					params[4] = new String[] {crs.getString("pre_tran_id"),bean.getBillId()};
					sqls[5] = "update dh_tranlist set work_status ='1' where tran_id = ?";
					params[5] = new String[] {tranId};
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			sqls = new String[2];
			params = new String[2][];
		}
		
		sqls[0] = "update jd_inspect_item set inspect_value='',inspect_result='' where inspect_id=?";
		params[0] = new String[] { bean.getInspectId()};
		
		sqls[1] = "update jd_inspect set fill_status='0',fill_desc='',fill_opt_id='',fill_opt_name='',inspect_date='',is_qualified='',out_result='0' where inspect_id=?";
		params[1] = new String[] { bean.getInspectId()};
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param dataId
	* @return
	* 2017-2-22
	* 获取检验数据详情信息
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public Map<String, Object> getJdInspectDataById(String dataId) throws SQLException, ParseException {
		String sql = "select a.out_compname,a.inspect_id,a.ita_no,a.ita_title,b.sample_no,a.main_opt_id,a.main_opt_name,a.inspect_date,a.is_valid,a.is_qualified,a.check_status," +
				"a.fill_desc,b.product_name from jd_inspect a,jd_sample_bill b where a.bill_id = b.bill_id and a.inspect_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { dataId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdInspectData bean = new JdInspectData();
			bean.setOutCompname(crs.getString("out_compname"));
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setInspectDate(crs.getString("inspect_date"));
			System.out.println(crs.getString("inspect_date"));
			bean.setIsQualified(crs.getString("is_qualified"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setFillDesc(crs.getString("fill_desc"));
			bean.setProductName(crs.getString("product_name"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setSampleNo(crs.getString("sample_no"));
//			String time1 = formatter.format(formatter
//					.parse(optTime1));
//			bean.setOptTime2(time1);
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}

	/** *******************
	* @param planName
	* @param dataId
	* @return
	* 2017-2-22
	* 修改时验证检验数据标题重复
	*/
	public boolean checkEditJdInspectDataName(String idmTitle, String dataId) {
		String sql="select count(*) from jd_inspect_data where idm_title = ? and data_id != ?";
		try{ 
			long obj = (Long) DBFacade.getInstance()
					.getValueBySql(sql, new String[] { idmTitle,dataId});
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
	* 2017-2-22
	* 修改检验数据信息
	*/
	public Map<String, Object> editJdInspectData(JdInspectData bean) {
		String[] inspectTaskId = bean.getInspectTaskId().split(",");
		int len = inspectTaskId.length;
		String[] inspectValue = bean.getInspectValue().split(",");
		String[] inspectResult = bean.getInspectResult().split(",");
		String[] standardValue = bean.getStandardValue().split(",");
		
		String[] sqls = new String[1 + len];
		String[][] params = new String[1 + len][];
//		sqls[0] = "update jd_inspect_data set idm_title=?,inspect_time=?,"
//				+ "index_id=?,qualified_standard=?,is_qualified=?,is_valid=?,remark=?,opt_id=?,"
//				+ "opt_time=now() where data_id=?";
//		params[0] = new String[] { bean.getIdmTitle(),bean.getInspectTime(),bean.getIndexId(),
//				bean.getQualifiedStandard(), bean.getIsQualified(), bean.getIsValid2(),bean.getRemark2(),
//				bean.getOptId2(),bean.getDataId()};
//		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
//				+ "values(?,?,?,?,now(),?,?)";
		sqls[0] = "update jd_inspect set fill_status =?,is_qualified=?,fill_desc=?,inspect_date=?,check_status=0 where inspect_id=? ";
		params[0] =new String []{"1",bean.getIsQualified(),bean.getFillDesc(),bean.getInspectDate(),bean.getInspectId()};
		/*sqls[2] = "update jd_inspect set sign_status=2 where Inspect_id=?";
		params[2] = new String[] {bean.getInspectId()};
		*/
		for (int i = 1; i < len + 1; i++) {
			sqls[i] = "update jd_inspect_item set inspect_value = ?,inspect_result=?,standard_value = ? "
				 + " where inspect_task_id = ?";
			params[i] = new String[] { inspectValue[i-1],inspectResult[i-1],standardValue[i-1],inspectTaskId[i-1]};
		}
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-22
	* 提交检验数据信息待审核
	*/
	public Map<String, Object> submitJdInspectData(JdInspectData bean) {
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		sqls[0] = "update jd_inspect set is_submit='1',submit_opt_time=now(),check_status='1',check_dept_id=?,check_dept_name=?,check_opt_id=?,check_opt_name=? where inspect_id=?";
		params[0] = new String[] {bean.getNextDeptId(),bean.getNextDeptName(),bean.getNextDealId(),bean.getNextDealName(),bean.getInspectId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId2(), bean.getOptName2(), "检验数据信息提交",
				"检验数据编号：" + bean.getInspectId(), "1" };
		
		/*根据上一步骤tranId获取当前步骤tranId*/
		String tranId="";
		try {
			tranId=getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务主表jd_sample_bill(抽样单信息表)*/
		sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='5',tran_id=? where bill_id=? ";
		params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[4],tranId,bean.getBillId()};
		
		/**
		 * 检验数据提交，修改留痕表记录
		 */
		bean.setNextDept("[{'id':'"+bean.getNextDeptId()+"',"+"'name':'"+bean.getNextDeptName()+"'}]");
		bean.setNextPerson("[{'id':'"+bean.getNextDealId()+"',"+"'name':'"+bean.getNextDealName()+"'}]");
		
		sqls[3] = "update dh_tranlist set detail_id=?,deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',"
				+ " next_dept=?,next_person=? where pre_tran_id=? and work_status='0'";
		params[3] = new String[] { bean.getInspectId(),bean.getCompanyId(),bean.getCompanyName(),bean.getOptId2(),bean.getOptName2(),bean.getNextDept(),bean.getNextPerson(),bean.getTranId()};
		/**
		 * 给下一步骤检验数据审核插入留痕数据
		 */
		sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','检验数据审核',?,?,?,?,now(),?,?,?,?,'0','0','0','0','6',?,'1','1')";
		params[4] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[5],FlowConstant.CJ_STEP_ARR_ID[5],bean.getInspectId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId2(),bean.getOptName2(),bean.getNextDeptId(),bean.getNextDeptName(),bean.getNextDealId(),bean.getNextDealName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-22
	* 根据上一留痕Id获取当前留痕Id
	 * @throws SQLException 
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("tran_id");
		}
		return preTranId;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-23
	* 直接回退时提交
	 * @throws SQLException 
	*/
	public Map<String, Object> submitZJ(JdInspectData bean) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		
		
//		String sql2 = "select current_node_id,tran_id from jd_sample_bill where bill_id=?";
//		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] { bean.getBillId()});
////		String nodeId=null;
//		if (crs2.next()) {
////			nodeId =  crs2.getString("current_node_id").substring(3);
//			bean.setTranId(crs2.getString("tran_id"));
//		}
		
		String sql2 = "select pre_tran_id,node_id,opt_order,detail_id from dh_tranlist where tran_id=? ";
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] { bean.getTranId()});
		String preTranId=null,nodeId=null,optOrder=null,detailId=null;
		if (crs2.next()) {
			preTranId = crs2.getString("pre_tran_id");//7tranId
		}
		
		String sql3 = "select node_id,opt_order,detail_id from dh_tranlist where tran_id=? ";
		CachedRowSetImpl crs3 = DBFacade.getInstance().getRowSet(sql3,new String[] {preTranId});
		if (crs3.next()) {
			nodeId=crs3.getString("node_id");//7
			optOrder=crs3.getString("opt_order");//7
			detailId=crs3.getString("detail_id");
		}
		
		
		
		String nowTranId = DBFacade.getInstance().getID();
		
		sqls[0] = "update jd_sample_bill set current_node_id=?,current_status=?,tran_id=?,submit_opt_time=now() where bill_id=?";
		params[0] = new String[] { nodeId,optOrder,preTranId,bean.getBillId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"抽样单信息提交","抽样单ID：" + bean.getBillId(), "1" };
		
		
		//为检验数据审核添加一条新的留痕数据
		String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
				"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
		if (crs.next()) {
			sqls[2] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1')";
			params[2] = new String[] { nowTranId,crs.getString("work_id"),crs.getString("flow_id"),
					crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
					crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
					crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id")};
		}
		
		//检验报告再次审核
		if(optOrder.equals("7")){
			sqls[3] = "update jd_inspect_report set is_valid='1' ,check_status='1' where report_id=?";
			params[3] = new String[] {detailId};
		}
		
		//检验报告再次审核
		if(optOrder.equals("8")){
			sqls[3] = "update jd_inspect_report set issue_status='0' where report_id=?";
			params[3] = new String[] {detailId};
		}
		
		sqls[4] = "update jd_inspect set is_submit='1',is_valid='1' where inspect_id = ?";
		params[4] = new String[]{bean.getInspectId()};
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	//生产数字加字母的编号的函数
	public String getNumr(int length) {     
		    String val = "";     
		    Random random = new Random();     
		    for(int i = 0; i < length; i++)  {     
		         val += String.valueOf(random.nextInt(10));   
		    }     
		    String sql = "select verification_code FROM jd_inspect_report where verification_code =?";
		    CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{val});
		    if(crs.size()>0){
		    	val = getNumr(length);
		    }
		    return val;
		    
		}
}
