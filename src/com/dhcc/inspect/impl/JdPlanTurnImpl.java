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
public class JdPlanTurnImpl {
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
	* 获取需转办的任务列表
	 * @throws SQLException 
	*/
	public String getJdPlanHandleList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String companyId) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		/*String sql = "select distinct a.task_id,a.task_no,a.task_name,a.task_type,a.task_attr,a.main_dept_name,a.comp_name,"
				+ "a.product_name,a.begin_date,a.end_date,a.is_valid,a.trans_status,b.tran_id from jd_plan_task a,dh_tranlist b " +
				"where a.task_id=b.work_id and b.step_id='"+FlowConstant.CC_STEP_ARR_ID[1]+"' and a.is_handle = '1' and b.deal_status='0'";
		*/
		String sql = "select b.tran_title,b.deal_view,b.back_type,a.task_id,a.task_no,a.task_name,a.task_type,a.task_attr,a.main_dept_name,a.comp_name,"
				+ "a.product_name,a.begin_date,a.end_date,a.is_valid,a.trans_status,a.sign_status,a.tran_id,a.is_handle from jd_plan_task a left join dh_tranlist b on a.tran_id = b.tran_id" +
				" where (a.is_handle = '1' or a.trans_status!='0') ";

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
			page.setSidx("a.task_id");
			page.setSord("desc");
		}
		
		//companyId = companyId.substring(0,5);
		
		sql = sql + " and (a.main_dept_id = '"+companyId+"')";
		
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
			bean.setTransStatus(crs.getString("trans_status"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setIsHandle(crs.getString("is_handle"));
			String sql1 = "select tran_title,deal_view,back_type from dh_tranlist where tran_id = (select deal_view from dh_tranlist where pre_tran_id=? and work_status='0')";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[]{bean.getTranId()});
			if(crs1.next()){
				bean.setTranTitle(crs1.getString("tran_title"));
				bean.setDealView(crs1.getString("deal_view"));
				bean.setBackType(crs1.getString("back_type"));
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
				+ "handle_opt_name,"+TimeUtil.getTimeShow("handle_opt_time")+" handle_opt_time,trans_status,trans_dept_name,trans_opt_name,"+TimeUtil.getTimeShow("trans_opt_time")+" trans_opt_time,trans_desc,sign_status,"
				+ "sign_desc,sign_opt_name,"+TimeUtil.getTimeShow("sign_opt_time")+" sign_opt_time from jd_plan_task where task_id=? ";
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
			bean.setTransStatus(crs.getString("trans_status"));
			bean.setTransDeptName(crs.getString("trans_dept_name"));
			bean.setTransOptName(crs.getString("trans_opt_name"));
			bean.setTransOptTime(crs.getString("trans_opt_time"));
			bean.setTransDesc(crs.getString("trans_desc"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			if(crs.getString("sign_opt_name")==null){
				bean.setSignOptName("");
			}else{
				bean.setSignOptName(crs.getString("sign_opt_name"));
			}
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
	* 抽检任务转办
	*/
	public Map<String, Object> JdPlanHandleEnter(JdPlanHandle bean) {
		String[] sqls;
		String[][] params;
		if("".equals(bean.getNextDeptId())||"undefined".equals(bean.getNextDeptId())||bean.getNextDeptId()==null){
			/*转办时拒签*/
			bean.setIsHandle("0");
			bean.setTransStatus("2");
			sqls = new String[4];
			params = new String[4][];
		}else{
			bean.setIsHandle("1");
			bean.setTransStatus("1");
			bean.setTransDeptId(bean.getNextDeptId());
			bean.setTransDeptName(bean.getNextDeptName());
			bean.setTransOptId(bean.getNextDealId());
			bean.setTransOptName(bean.getNextDealName());
			 //date对象代表当前的系统时间(毫秒)
			  Date date = new Date();
			  //format对象是用来以指定的时间格式格式化时间的
			  SimpleDateFormat from = new SimpleDateFormat(
			  "yyyy-MM-dd HH:mm:ss"); //这里的格式可以自己设置
			  //format()方法是用来格式化时间的方法
			  String times = from.format(date);
			bean.setTransOptTime(times);
			sqls = new String[4];
			params = new String[4][];
		}
		
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sqls[0] = "update jd_plan_task set is_handle=?,trans_status=?,trans_dept_id=?,trans_dept_name=?," +
				"trans_opt_id=?,trans_opt_name=?,trans_opt_time=?,trans_desc=?,current_node_id=?,current_status='2',tran_id=? where task_id=?";
		params[0] = new String[] {bean.getIsHandle(),bean.getTransStatus(),bean.getTransDeptId(),bean.getTransDeptName(),
				bean.getTransOptId(),bean.getTransOptName(),bean.getTransOptTime(),bean.getTransDesc(),FlowConstant.CC_NODE_ARR_ID[1],tranId,bean.getTaskId()};
		
		if(bean.getTransStatus()=="2"){
			/**
			 * 抽检任务转办退回修改留痕表记录
			 */
			sqls[2] = "update dh_tranlist set back_type='3',deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='1',deal_view=? "
					+ " where pre_tran_id=? ";
			params[2] = new String[] { bean.getOptId(),bean.getOptName(),bean.getTransDesc(),bean.getTranId()};
			
			sqls[3] = "update dh_tranlist set work_status='1' where tran_id=? ";
			params[3] = new String[] {bean.getTranId()};
			
		}else if(bean.getTransStatus()=="1"){
			/**
			 * 抽检任务转办修改留痕表记录
			 */
			bean.setNextDept("[{'id':'"+bean.getNextDeptId()+"',"+"'name':'"+bean.getNextDeptName()+"'}]");
			bean.setNextPerson("[{'id':'"+bean.getNextDealId()+"',"+"'name':'"+bean.getNextDealName()+"'}]");
			
			sqls[2] = "update dh_tranlist set deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',deal_view=?,"
					+ "next_dept=?,next_person=? where pre_tran_id=? and work_status='0'";
			params[2] = new String[] { bean.getOptId(),bean.getOptName(),bean.getTransDesc(),bean.getNextDept(),bean.getNextPerson(),bean.getTranId()};
			/**
			 * 给下一步骤插入数据待下一步骤更改
			 */
			sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_plan_task','抽检任务签收',?,?,?,?,now(),?,?,?,?,'0','0','0','0','3',?,'1','1')";
			params[3] = new String[] { DBFacade.getInstance().getID(),bean.getTaskId(),FlowConstant.CC_FLOW_ID,
					FlowConstant.CC_NODE_ARR_ID[2],FlowConstant.CC_STEP_ARR_ID[2],bean.getTaskId(),bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getNextDeptId(),bean.getNextDeptName(),bean.getNextDealId(),bean.getNextDealName(),tranId};
			
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "抽检任务信息转办",
				"抽检任务ID：" + bean.getTaskId(), "1" };
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	/** *******************
	* @param taskId
	* @param string
	* @return
	* 2017-5-17
	* 获取当前步骤的tranId
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
	
}
