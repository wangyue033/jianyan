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
import com.dhcc.inspect.domain.JdInspectDataCheck;
import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.utils.LogUtil;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据审核Impl
 */
public class JdInspectDataCheckImpl {
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
	* 获取已提交审核的检验数据信息列表
	 * @throws ParseException 
	 * @throws SQLException 
	*/
	public String getJdInspectDataCheckList(String curPage, String perNumber,
			String orderByField, String orderByType, String searchField,
			String searchValue,String compId,String companyId,String optId) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql = "select b.inspect_id,b.bill_id,c.sample_no,c.product_model,b.main_opt_id,b.main_opt_name,b.fill_status,b.inspect_date,"
				+ "b.is_qualified,c.product_id,c.product_name,b.check_status,c.tran_id,ifNull(b.check_dept_id,'') check_dept_id,ifNull(b.check_opt_id,'') check_opt_id from jd_inspect b left join jd_sample_bill c on b.bill_id=c.bill_id "+
				"where b.is_valid='1' and b.check_status !='0' and b.org_id like'%"+compId+"%' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderByType);
		} else {
			page.setSidx("b.inspect_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectDataCheck> list = new ArrayList<JdInspectDataCheck>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectDataCheck bean = new JdInspectDataCheck();
			if(crs.getString("check_opt_id").equals(optId)||(crs.getString("check_opt_id").equals("")&&crs.getString("check_dept_id").equals(companyId))){
				bean.setIsMain("1");
			}else{
				bean.setIsMain("0");
			}
			
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setDataId(crs.getString("fill_status"));
			bean.setIsQualified(crs.getString("is_qualified"));
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setCheckStatus(crs.getString("check_status"));	
			bean.setInspectDate(crs.getString("inspect_date"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setTranId(crs.getString("tran_id"));
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
	* @param inspectId
	* @return
	* 2017-2-22
	* *
	 * @throws ParseException 
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getJdInspectDataCheckById(String inspectId) throws SQLException, ParseException {
		String sql = "select a.inspect_id,a.ita_no,a.ita_title,a.main_opt_id,a.main_opt_name,a.inspect_date,"
				+ "a.check_status,b.product_id,b.product_name,a.is_qualified,a.fill_desc "
				+ "from jd_inspect a,jd_sample_bill b where a.bill_id = b.bill_id and inspect_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { inspectId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdInspectData bean = new JdInspectData();
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setItaNo(crs.getString("ita_no"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setInspectDate(crs.getString("inspect_date"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setIsQualified(crs.getString("is_qualified"));
			bean.setFillDesc(crs.getString("fill_desc"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-2-22
	* 检验数据信息审核成功
	*/
	public Map<String, Object> addJdInspectDataCheck(JdInspectDataCheck bean) {
		String[] sqls = new String[6];
		String[][] params = new String[6][];
		
		/*获取当前步骤的tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务副表jd_inspect(检验任务管理信息表)*/
		sqls[0] = "update jd_inspect set check_status='3',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now(),is_overtime=case when TIMESTAMPDIFF(hour,end_time,now())+1<0 then '0' else '1' end where inspect_id = ? ";
		params[0] = new String[] {bean.getCheckDesc(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
		
		/*日志记录*/
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId2(), bean.getOptName2(), "检验信息审核",
				"检验编号：" + bean.getInspectId(), "1" };
		
		/*更新业务主表jd_sample_bill(抽样单信息表)*/
		sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='6',tran_id=? where bill_id=? ";
		params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[5],tranId,bean.getBillId()};
		
		/**
		 * 签收，直接更新当前步骤记录留痕，并把记录修改成有效
		 */
		sqls[3] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',deal_view=?"
				+ " where pre_tran_id=?";
		params[3] = new String[] { bean.getOptId2(),bean.getOptName2(),bean.getCheckDesc(),bean.getTranId()};
		
		/*把上一记录改成有效*/
		sqls[4] = "update dh_tranlist set work_status='0' where tran_id=?";
		params[4] = new String[] { bean.getTranId()};
		
		/**
		 * 第二，为下一步骤插入留痕数据
		 */
		sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,"
				+ " deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','检验报告编制',?,?,?,?,now(),'0','0','0','0','7',?,'1','1')";
		params[5] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[6],FlowConstant.CJ_STEP_ARR_ID[6],bean.getInspectId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId2(),bean.getOptName2(),tranId};
		DBFacade.getInstance().execute(sqls, params);
		
		
		String sql = "select b.sample_no,b.product_id,c.comp_id,b.ep_id,c.task_type from jd_inspect a, jd_sample_bill b," +
				" jd_plan_task c,jd_enterprise_info d,hr_company e where a.bill_id = b.bill_id and b.task_id = c.task_id and" +
				" b.ep_id = d.enterprise_id and d.is_valid = '1' and c.comp_id=e.company_id and a.inspect_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getInspectId()});
		try {
			while (crs.next()) {
				String db = DBFacade.getInstance().getID();
				String[] sql2 = new String[1];
				String[][] param2 = new String[1][];
				sql2[0] = "insert into jd_inspect_report(report_id,inspect_id,irp_title,verification_code,product_id," +
						"depart_id,comp_id,ep_id,inspect_type,is_valid,check_status,issue_status,abolish_status," +
						"report_type,opt_id,opt_name,opt_time) " +
						"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
				param2[0]  = new String[] { db,bean.getInspectId(),crs.getString("sample_no"),getCharAndNumr(8),
						crs.getString("product_id"), crs.getString("comp_id"), crs.getString("comp_id").substring(0, 10),
						crs.getString("ep_id"),crs.getString("task_type"),"1", "0", "0","0","1",bean.getOptId2(),bean.getOptName2()};
				DBFacade.getInstance().execute(sql2, param2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
	//生产数字加字母的编号的函数
	public String getCharAndNumr(int length){     
	    String val = "";     
	             
	    Random random = new Random();     
	    for(int i = 0; i < length; i++)     
	    {     
	        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字     
	                 
	        if("char".equalsIgnoreCase(charOrNum)) // 字符串     
	        {     
	            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
	            val += (char) (choice + random.nextInt(26));     
	        }     
	        else if("num".equalsIgnoreCase(charOrNum)) // 数字     
	        {     
	            val += String.valueOf(random.nextInt(10));     
	        }     
	    }     
	    
	    String sql = "select verification_code FROM jd_inspect_report where verification_code =?";
	    CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{val});
	    if(crs.size()>0){
	    	val = getCharAndNumr(length);
	    }
	    return val;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-22
	* 退回，修改抽样单信息
	*/
	public Map<String, Object> dataCheckToSample(JdInspectDataCheck bean) {
		String[] sqls;
		String[][] params;
		String isInspect = "";
		if(bean.getBackType().equals("1")){
			sqls = new String[4];
			params = new String[4][];
			isInspect = "1";
			/*直接回退，则更新检验任务管理表*/
			sqls[0] = "update jd_inspect set check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where inspect_id=?";
			params[0] = new String[] {bean.getCheckDesc(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
			
		}else{
			sqls = new String[5];
			params = new String[5][];
			isInspect = "0";
			/*逐级回退，则把抽样单以下的数据都置为无效*/
			sqls[0] = "update jd_inspect set check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now(),is_valid='0' where inspect_id=?";
			params[0] = new String[] {bean.getCheckDesc(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId2(), bean.getOptName2(), "检验数据信息提交",
				"检验数据编号：" + bean.getInspectId(), "1" };
		
		/*根据上一留痕Id获取当前留痕Id*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新当前步骤的留痕记录*/
		sqls[2] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1," +
				"deal_status='1',work_status='1',deal_view=?,back_type=? where pre_tran_id=? and work_status='0'";
		params[2] = new String[] {bean.getOptId2(),bean.getOptName2(),bean.getCheckDesc(),bean.getBackType(),bean.getTranId()};
		
		/*逐级回退，需要把之前的留痕数据全都置为1(无效)*/
		if(bean.getBackType().equals("2")){
			/*更新抽样单信息，使之能继续修改*/
			sqls[3] = "update jd_sample_bill set remark=?,is_submit='0',check_status='0',is_inspect=?,current_node_id=?,current_status='6',tran_id=? where bill_id=?";
			params[3] = new String[] {bean.getCheckDesc(),isInspect,FlowConstant.CJ_NODE_ARR_ID[5],tranId,bean.getBillId()};
			
			sqls[4] = "update dh_tranlist set work_status='1' where flow_id='03' and work_id=? ";
			params[4] = new String[] {bean.getBillId()};
		}else{
			/*更新抽样单信息，使之能继续修改*/
			sqls[3] = "update jd_sample_bill set remark=?,is_submit='0',current_node_id=?,current_status='6',tran_id=? where bill_id=?";
			params[3] = new String[] {bean.getCheckDesc(),FlowConstant.CJ_NODE_ARR_ID[5],tranId,bean.getBillId()};
		}
		
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
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0' ";
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
	* 2017-5-22
	* 退回，修改检验数据
	 * @throws SQLException 
	*/
	public Map<String, Object> dataCheckToInspect(JdInspectDataCheck bean) throws SQLException {
		String[] sqls = new String[6];
		String[][] params = new String[6][];
		
		/*获取当前步骤的tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务副表jd_inspect(检验任务管理信息表)*/
		sqls[0] = "update jd_inspect set is_submit='0',check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where inspect_id = ? ";
		params[0] = new String[] {bean.getCheckDesc(),bean.getOptId2(),bean.getOptName2(),bean.getInspectId()};
		
		/*日志记录*/
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),bean.getOptId2(),bean.getOptName2(),"检验数据审核",
				"检验任务ID："+bean.getInspectId()+";样品编号："+bean.getSampleNo());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		/*拒签，更新当前步骤记录留痕，把当前记录和上一记录修改成无效，插入一条返回的新记录*/
		sqls[3] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='1',deal_view=?,back_type='3'"
				+ " where pre_tran_id=? and work_status='0'";
		params[3] = new String[] { bean.getOptId2(),bean.getOptName2(),bean.getCheckDesc(),bean.getTranId()};
		
		/*把上一记录改成无效*/
		sqls[4] = "update dh_tranlist set work_status='1' where tran_id=?";
		params[4] = new String[] {bean.getTranId()};
		
		/*先查询上一步骤的信息，再重新插入*/
		String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
				"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
		if (crs.next()) {
			sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,deal_view,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,?,'1','1')";
			params[5] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
					crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
					crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
					crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),tranId,crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id")};
			/*更新业务主表jd_sample_bill(抽样单信息表)*/
			sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='6',tran_id=? where bill_id=? ";
			params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[5],crs.getString("pre_tran_id"),bean.getBillId()};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
}
