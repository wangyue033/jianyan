/**
 * 
 */
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

import com.dhcc.inspect.domain.JdPlanHandle;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-21    
 * 抽检任务签收Impl
 */
public class JdPlanHandleImpl {
	Page page = new Page();
	/** *******************
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-2-21
	* 获取需签收的任务列表
	 * @throws SQLException 
	*/
	public String getJdPlanHandleList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String companyId,String optId) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select task_id,task_no,task_name,task_type,task_attr,main_dept_name,comp_name,"
				+ "product_name,begin_date,end_date,is_valid,sign_status,tran_id,trans_status from jd_plan_task where (trans_status='1' or sign_status!='0') " +
				" and (case when trans_opt_id is null or trans_opt_id='' then LOCATE(trans_dept_id,'"+companyId+"')>0 else trans_opt_id='"+optId+"'  end)";

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
			page.setSidx("task_id");
			page.setSord("desc");
		}
		
		//companyId = companyId.substring(0,5);
		
		//sql = sql + " and (main_dept_id = '"+companyId+"')";
		
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdPlanHandle> list = new ArrayList<JdPlanHandle>();
		CachedRowSetImpl crs = pageData.getCrs();
		
		while (crs.next()) {
			JdPlanHandle bean = new JdPlanHandle();
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskType(crs.getString("task_type"));
			bean.setTaskAttr(crs.getString("task_attr"));
			bean.setProductName(crs.getString("product_name"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setTransStatus(crs.getString("trans_status"));
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
	* @param taskId
	* @return
	* 2017-2-21
	* 获取页面上显示的抽检任务信息
	 * @throws SQLException 
	 * @throws ParseException 
	*/
	public Map<String, Object> getJdPlanHandleById(String taskId) throws SQLException, ParseException {
		String sql = "select task_id,task_name,task_no,task_type,task_attr,base_no,base_text,"
				+ "product_name,begin_date,end_date,sample_base,special_ask,main_dept_name,"
				+ "handle_opt_name,"+TimeUtil.getTimeShow("handle_opt_time")+" handle_opt_time,trans_status,trans_dept_name,trans_opt_name,"+TimeUtil.getTimeShow("trans_opt_time")+" trans_opt_time,sign_status,"
				+ "sign_desc,sign_opt_name,"+TimeUtil.getTimeShow("sign_opt_time")+" sign_opt_time,tran_id from jd_plan_task where task_id=? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { taskId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdPlanHandle bean = new JdPlanHandle();
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setTaskNo(crs.getString("task_no"));
			bean.setTaskType(crs.getString("task_type"));
			bean.setTaskAttr(crs.getString("task_attr"));
			bean.setBaseNo(crs.getString("base_no"));
			bean.setBaseText(crs.getString("base_text"));
			bean.setProductName(crs.getString("product_name"));
			bean.setBeginDate(crs.getString("begin_date"));
			bean.setEndDate(crs.getString("end_date"));
			bean.setSampleBase(crs.getString("sample_base"));
			bean.setSpecialAsk(crs.getString("special_ask"));
			bean.setMainDeptName(crs.getString("main_dept_name"));
			bean.setHandleOptName(crs.getString("handle_opt_name"));
			bean.setHandleOptTime(crs.getString("handle_opt_time"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			if(crs.getString("sign_opt_name")==null){
				bean.setSignOptName("");
			}else{
				bean.setSignOptName(crs.getString("sign_opt_name"));
			}
			bean.setTransStatus(crs.getString("trans_status"));
			bean.setTransDeptName(crs.getString("trans_dept_name"));
			bean.setTransOptName(crs.getString("trans_opt_name"));
			bean.setTransOptTime(crs.getString("trans_opt_time"));
			bean.setTranId(crs.getString("tran_id"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	/** *******************
	* @param bean
	* @return
	* 2017-2-21
	* 签收抽检任务
	 * @throws SQLException 
	*/
	public Map<String, Object> JdPlanHandleEnter(JdPlanHandle bean) throws SQLException {
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		if("1".equals(bean.getStepIndex())){
			bean.setTransStatus("0");
			bean.setSignStatus("2");
		}else{
			bean.setTransStatus("1");
			bean.setSignStatus("1");
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
		
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sqls[0] = "update jd_plan_task set trans_status=?,sign_status=?,sign_desc=?,sign_opt_id=?,sign_opt_name=?,sign_opt_time=?,current_node_id=?,current_status='3',tran_id=? where task_id=?";
		params[0] = new String[] {bean.getTransStatus(),bean.getSignStatus(),bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getSignOptTime(),FlowConstant.CC_NODE_ARR_ID[2],tranId,bean.getTaskId()};
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检任务信息签收",
				"抽检任务ID：" + bean.getTaskId(), "1" };
		
		if("1".equals(bean.getStepIndex())){
			/**
			 * 如果拒签修改当前记录留痕,并且复制一条上一步骤的留痕
			 */
			sqls[2] = "update dh_tranlist set back_type='3',deal_view=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='1' "
					+ " where pre_tran_id=?";
			params[2] = new String[] { bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
			
			sqls[3] = "update dh_tranlist set work_status='1' where tran_id=?";
			params[3] = new String[] {bean.getTranId()};
			
			
			/*先查询上一步骤的信息，再重新插入*/
			String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,pre_tran_id from dh_tranlist where tran_id=?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
			if (crs.next()) {
				sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,'1','1',?)";
				params[4] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
						crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
						crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
						crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("pre_tran_id"),tranId};
				/*更新业务主表jd_plan_task*/
				sqls[0] = "update jd_plan_task set trans_status=?,sign_status=?,sign_desc=?,sign_opt_id=?,sign_opt_name=?,sign_opt_time=?,current_node_id=?,current_status='3',tran_id=? where task_id=?";
				params[0] = new String[] {bean.getTransStatus(),bean.getSignStatus(),bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getSignOptTime(),FlowConstant.CC_NODE_ARR_ID[2],crs.getString("pre_tran_id"),bean.getTaskId()};
			}
		
		}else{
			/**
			 * 如果同意签收，第一先修改当前记录留痕
			 */
			/*更新业务主表jd_plan_task*/
			sqls[0] = "update jd_plan_task set trans_status=?,sign_status=?,sign_desc=?,sign_opt_id=?,sign_opt_name=?,sign_opt_time=?,current_node_id=?,current_status='3',tran_id=? where task_id=?";
			params[0] = new String[] {bean.getTransStatus(),bean.getSignStatus(),bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getSignOptTime(),FlowConstant.CC_NODE_ARR_ID[2],tranId,bean.getTaskId()};
			
			bean.setNextDept("[{'id':'"+bean.getCompanyId()+"',"+"'name':'"+bean.getCompanyName()+"'}]");
			bean.setNextPerson("[{'id':'"+bean.getOptId()+"',"+"'name':'"+bean.getOptName()+"'}]");
			
			sqls[2] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',"
					+ " next_dept=?,next_person=? where pre_tran_id=? ";
			params[2] = new String[] { bean.getOptId(),bean.getOptName(),bean.getNextDept(),bean.getNextPerson(),bean.getTranId()};
			
			sqls[3] = "update dh_tranlist set work_status='0' where tran_id=?";
			params[3] = new String[] {bean.getTranId()};
			
			/**
			 * 第二，为下一步骤插入留痕数据
			 */
			sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_plan_task','实施方案上报',?,?,?,?,now(),?,?,?,?,'0','0','0','0','4',?,'1','1')";
			params[4] = new String[] { DBFacade.getInstance().getID(),bean.getTaskId(),FlowConstant.CC_FLOW_ID,
					FlowConstant.CC_NODE_ARR_ID[3],FlowConstant.CC_STEP_ARR_ID[3],bean.getTaskId(),bean.getCompanyId(),
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
	* 2017-5-18
	* 根据上一步流转Id获取当前tranId
	 * @throws SQLException 
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
	
}
