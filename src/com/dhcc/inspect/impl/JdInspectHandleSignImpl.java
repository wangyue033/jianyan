package com.dhcc.inspect.impl;

import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectHandleSign;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.utils.LogUtil;

public class JdInspectHandleSignImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getJdInspectHandleSignList(JdInspectHandleSign bean1, String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String compId) throws Exception {

		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql = "select a.inspect_id,a.bill_id,a.ita_title,b.sample_no,b.product_name,b.product_model,a.main_opt_id,a.main_opt_name,a.end_time," +
				"a.description,a.remark,a.is_valid,a.is_handle,a.opt_id,a.opt_time,a.sign_status,a.sign_opt_id,a.sign_opt_name," +
				" "+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time,b.ssim_title,b.tran_id from jd_inspect a,jd_sample_bill b " +
				" where a.is_valid='1' and b.is_valid = '1' and b.check_status = '3' and a.bill_id = b.bill_id and a.is_handle = '1' and a.org_id like '%"+compId+"%' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			if (searchField.equals("description")) {
				sql = sql + " and " + searchField + " like '%" + searchValue
						+ "%' ";
			} else {
				sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
			}
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("a.inspect_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectHandleSign> list = new ArrayList<JdInspectHandleSign>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectHandleSign bean = new JdInspectHandleSign();
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			if(crs.getString("main_opt_id").equals(bean1.getOptId())){
				bean.setIsMain("1");
			}else{
				bean.setIsMain("0");
			}
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setEndTime(crs.getString("end_time"));
			bean.setDescription(crs.getString("description"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductName(crs.getString("product_name"));
			bean.setProductModel(crs.getString("product_model"));
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
	
	public Map<String, Object> signJdInspectHandleSign(JdInspectHandleSign bean) throws SQLException {
		String[] sqls = new String[6];
		String[][] params = new String[6][];
		if("1".equals(bean.getStepIndex())){
			bean.setSignStatus("2");
			bean.setIsHandle("0");
		}else{
			bean.setSignStatus("1");
			bean.setIsHandle("1");
			bean.setSignOptId(bean.getOptId());
			bean.setSignOptName(bean.getOptName());
			//date对象代表当前的系统时间(毫秒)
			  Date date = new Date();
			  //format对象是用来以指定的时间格式格式化时间的
			  SimpleDateFormat from = new SimpleDateFormat(
			  "yyyy-MM-dd HH:mm:ss"); //这里的格式可以自己设置
			  //format()方法是用来格式化时间的方法
			  String times = from.format(date);
			bean.setSignOptTime(times);
		}
		
		/*获取当前步骤的tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务副表jd_inspect(检验任务管理信息表)*/
		sqls[0] = "update jd_inspect set is_handle=?,sign_status=?,sign_opt_id=?,sign_opt_name=?,sign_opt_time=now(),sign_opt_opinion=? where inspect_id = ? ";
		params[0] = new String[] {bean.getIsHandle(),bean.getSignStatus(),bean.getOptId(),bean.getOptName(),bean.getSignOptOpinion(),bean.getInspectId()};
		
		/*日志记录*/
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),"检验任务签收",
				"检验任务签收ID："+bean.getInspectId()+";样品编号："+bean.getSampleNo());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		
		if("1".equals(bean.getStepIndex())){
			/*拒签，更新当前步骤记录留痕，把当前记录和上一记录修改成无效，插入一条返回的新记录*/
			sqls[3] = "update dh_tranlist set back_type='3',deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='1',deal_view=?"
					+ " where pre_tran_id=?";
			params[3] = new String[] { bean.getOptId(),bean.getOptName(),bean.getSignOptOpinion(),bean.getTranId()};
			
			/*把当前记录改成无效*/
			sqls[4] = "update dh_tranlist set work_status='1' where tran_id=?";
			params[4] = new String[] {bean.getTranId()};
			
			/*先查询上一步骤的信息，再重新插入*/
			String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
			if (crs.next()) {
				sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1',?)";
				params[5] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
						crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
						crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
						crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id"),tranId};
				/*更新业务主表jd_sample_bill(抽样单信息表)*/
				sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='4',tran_id=? where bill_id=? ";
				params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[3],crs.getString("pre_tran_id"),bean.getBillId()};
				
			}
			
		}else{
			
			
			/*更新业务主表jd_sample_bill(抽样单信息表)*/
			sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='4',tran_id=? where bill_id=? ";
			params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[3],tranId,bean.getBillId()};
			
			/**
			 * 签收，直接更新当前步骤记录留痕，并把记录修改成有效
			 */
			sqls[3] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',deal_view=?"
					+ " where pre_tran_id=?";
			params[3] = new String[] { bean.getOptId(),bean.getOptName(),bean.getSignOptOpinion(),bean.getTranId()};
			
			/*把上一记录改成有效*/
			sqls[4] = "update dh_tranlist set work_status='0' where tran_id=?";
			params[4] = new String[] { bean.getTranId()};
			
			/**
			 * 第二，为下一步骤插入留痕数据
			 */
			sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_sample_bill','检验数据填报',?,?,?,?,now(),?,?,?,?,'0','0','0','0','5',?,'1','1')";
			params[5] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
					FlowConstant.CJ_NODE_ARR_ID[4],FlowConstant.CJ_STEP_ARR_ID[4],bean.getInspectId(),bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),tranId};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
	/** *******************
	* @param tranId
	* @return
	* 2017-5-19
	* *
	 * @throws SQLException 根据上一流转Id获取当前流转Id
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("tran_id");
		}
		System.out.println(preTranId+"当前步骤Id");
		return preTranId;
	}

	public Map<String, Object> getJdInspectHandleSignById(String inspectId) throws Exception {
		String sql = "SELECT inspect_id,bill_id,ita_title,main_opt_id,main_opt_name,end_time,description,remark,is_valid,sign_opt_opinion," +
				"is_handle,opt_id,"+TimeUtil.getTimeShow("opt_time")+" opt_time,sign_status,sign_opt_id,sign_opt_name,"+TimeUtil.getTimeShow("sign_opt_time")+" sign_opt_time from jd_inspect where inspect_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { inspectId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdInspectHandleSign bean = new JdInspectHandleSign();
			
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			
			String sql1 = "SELECT ssim_title,sample_no,product_name,product_model FROM jd_sample_bill WHERE bill_id = ? ";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,
					new String[] { bean.getBillId() });
			if (crs1.next()) {
				bean.setSsimTitle(crs1.getString("ssim_title"));
				bean.setSampleNo(crs1.getString("sample_no"));
				bean.setProductName(crs1.getString("product_name"));
				bean.setProductModel(crs1.getString("product_model"));
			}
			
			bean.setItaTitle(crs.getString("ita_title"));
//			bean.setItaPerson(crs.getString("ita_person").split(";;")[1]);
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
//			bean.setUserName(bean.getItaPerson().split(";;")[1]);
//			bean.setStartTime(crs.getString("start_time"));
//			bean.setEndTime(crs.getString("end_time"));
			bean.setDescription(crs.getString("description"));
			bean.setSignOptOpinion(crs.getString("sign_opt_opinion"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptTime(crs.getString("opt_time"));
			String endTime = crs.getString("end_time");
			bean.setEndTime(endTime);
			
			String s = "select user_name from hr_staff where serial_id = ? ";
			CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[] { bean.getOptId() });
			if (c.next()) {
				bean.setOptName(c.getString("user_name"));
			}
			
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}

	/** *******************
	* @param bean
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-3-27
	* *
	 * @throws ParseException 
	 * @throws SQLException ******************
	*/
	public String getJdInspectHandleSignList2(JdInspectHandleSign bean2,
			String curPage, String perNumber, String orderByField,
			String orderBySort, String searchField, String searchValue) throws SQLException, ParseException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql ="select a.inspect_id,a.bill_id,b.product_id,b.product_name,a.ita_title,a.is_valid,b.sample_no from jd_inspect a,jd_sample_bill b where a.bill_id = b.bill_id and a.is_valid = '1' and a.is_handle = '1' and a.sign_status = '1' ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			if (searchField.equals("description")) {
				sql = sql + " and a." + searchField + " like '%" + searchValue
						+ "%' ";
			} else {
				sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
			}
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("a.inspect_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectHandleSign> list = new ArrayList<JdInspectHandleSign>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			
			JdInspectHandleSign bean = new JdInspectHandleSign();
			System.out.println(crs.getString("inspect_id"));
			String sql1 = "select count(*) from jd_inspect_report where is_valid='1' and inspect_id = ? ";
			long count = (Long) DBFacade.getInstance().getValueBySql(sql1,
					new String[] { crs.getString("inspect_id") });
			if (count > 0) {
				continue;
			}
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setSampleNo(crs.getString("sample_no"));
			
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
	
}
