package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.utils.LogUtil;

public class JdInspectReportAssignedImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getJdInspectReportList(JdInspectReport jdInspectReport, String curPage, String perNumber,
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

		String sql = "select a.issue_opt_id,a.report_id,a.inspect_id,c.sample_no,c.report_no,a.irp_title,a.verification_code,a.inspect_type,a.is_valid,a.issue_status,a.check_status,a.report_path,e.enterprise_name,c.tran_id,c.bill_id," +
				""+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.handle_dept_id,b.handle_dept_name from jd_inspect_report a,jd_plan_handle b,jd_sample_bill c,jd_inspect d,jd_enterprise_info e " +
				"where a.ep_id = e.enterprise_id and d.inspect_id = a.inspect_id and d.bill_id = c.bill_id and c.task_id = b.task_id and a.is_valid = '1' and a.check_status = '3' and b.handle_dept_id like '%"+compId+"%' and a.abolish_status='0' ";
		
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
			page.setSidx("report_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectReport> list = new ArrayList<JdInspectReport>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			bean.setIssueOptId(crs.getString("issue_opt_id"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setReportNo(crs.getString("report_no"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setInspectId(crs.getString("inspect_id"));
//			bean.setProductId(crs.getString("product_id"));
			bean.setIrpTitle(crs.getString("irp_title"));
			bean.setVerificationCode(crs.getString("verification_code"));
			bean.setInspectType(crs.getString("inspect_type"));
        	bean.setIsAssigned(crs.getString("issue_status"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setReportId(crs.getString("report_id"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));	
			bean.setCompanyName(crs.getString("handle_dept_name"));
			bean.setReportPath(crs.getString("report_path"));
			bean.setOptTime(crs.getString("opt_time"));
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
	
	public Map<String, Object> getJdInspectReportById(String reportId) throws Exception {
		String sql = "select distinct a.report_id,a.inspect_id, a.product_id, a.irp_title, a.verification_code, a.inspect_type, a.is_valid," +
				" a.is_assigned, a.check_status,a.opt_id, a.opt_time, b.ita_title, b.bill_id, d.comp_name, g.enterprise_name, f.company_name," +
				" a.remark from jd_inspect_report a, jd_inspect b, jd_sample_bill c, jd_plan_task d, hr_staff e, hr_company f," +
				" jd_enterprise_info g where b.is_assinged = '1' and b.is_valid = '1' and b.sign_status = '1' and" +
				" b.bill_id = c.bill_id and c.check_status = '3' and c.is_valid = '1' and c.task_id = d.task_id and" +
				" a.opt_id = e.serial_id and e.company_id = f.company_id and" +
				" c.ep_id = g.enterprise_id and g.is_valid = '1' and 1 = 1  and a.report_id = ? ";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setProductId(crs.getString("product_id"));
			
			String s = "select product_name from jd_enterprise_product where product_id = ? ";
			CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[] { bean.getProductId() });
			if (c.next()) {
				bean.setProductName(c.getString("product_name"));
			}
			
			if (bean.getProductName()==null) {
				s = "select type_name from jd_product_type where type_id = ? ";
				c = DBFacade.getInstance().getRowSet(s, new String[] { bean.getProductId() });
				if (c.next()) {
					bean.setProductName(c.getString("type_name"));
				}
			}
			
			bean.setIrpTitle(crs.getString("irp_title"));
			bean.setVerificationCode(crs.getString("verification_code"));
			bean.setInspectType(crs.getString("inspect_type"));
			bean.setIsAssigned(crs.getString("is_assigned"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setBillId(crs.getString("bill_id"));
			
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setRemark(crs.getString("remark"));
			bean.setReportId(crs.getString("report_id"));
			bean.setCompName(crs.getString("comp_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setOptId(crs.getString("opt_id"));
			SimpleDateFormat formatter3 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String optTime = crs.getString("opt_time");
			String time = formatter3.format(formatter3.parse(optTime));
			bean.setOptTime(time);
			
			map.put("record", bean);
			map.put("msg", "true");
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}
	/*检验报告签发*/
	public Map<String, Object> checkJdInspectReport(JdInspectReport bean) {
		String[] sqls = new String[6];
		String[][] params = new String[6][];
		/*更新检验报告管理表*/
		sqls[0] = "update jd_inspect_report set issue_status='1',issue_opt_id=?,issue_opt_name=?,issue_time=now() where report_id=? ";
		params[0] = new String[] {bean.getOptId(),bean.getOptName(),bean.getReportId()};
		/*日志记录*/
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				 "检验报告签发","检验报告签发ID："+ bean.getReportId()+";检验报告名称："+bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		/*获取当前步骤的tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务主表jd_sample_bill(抽样单信息表)*/
		sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='9',tran_id=? where bill_id=? ";
		params[2] = new String[] { FlowConstant.CJ_NODE_ARR_ID[8],tranId,bean.getBillId()};
		
		/**
		 * 签发成功，直接更新当前步骤记录留痕，并把记录修改成有效
		 */
		sqls[3] = "update dh_tranlist set deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0'"
				+ " where pre_tran_id=?";
		params[3] = new String[] { bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
		
		/*把上一记录改成有效*/
		sqls[4] = "update dh_tranlist set work_status='0' where tran_id=?";
		params[4] = new String[] { bean.getTranId()};
		
		/**
		 * 第二，为下一步骤报告打印插入留痕数据
		 */
		sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,"
				+ " deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','检验报告打印',?,?,?,?,now(),'0','0','0','0','10',?,'1','1')";
		params[5] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[9],FlowConstant.CJ_STEP_ARR_ID[9],bean.getReportId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(),bean.getOptName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-23
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
	* 2017-5-23
	* 签发退回到抽样单录入
	*/
	public Map<String, Object> checkReportToSample(JdInspectReport bean) {
		String[] sqls;
		String[][] params;
		String isInspect = "";
		if(bean.getBackType().equals("1")){
			sqls = new String[4];
			params = new String[4][];
			isInspect = "1";
			/*直接回退，则更新检验报告管理表*/
			sqls[0] = "update jd_inspect_report set issue_status='2' where report_id=?";
			params[0] = new String[] {bean.getReportId()};
			
		}else{
			sqls = new String[6];
			params = new String[6][];
			isInspect = "0";
			/*逐级回退，则把抽样单以下的数据都置为无效*/
			/*检验报告管理表置为无效*/
			sqls[0] = "update jd_inspect_report set issue_status='2',issue_opt_id=?,issue_opt_name=?,issue_time=now(),is_valid='0' where report_id=?";
			params[0] = new String[] {bean.getOptId(),bean.getOptName(),bean.getReportId()};
			/*检验任务管理表置为无效*/
			sqls[5] = "update jd_inspect set is_valid='0' where inspect_id=?";
			params[5] = new String[] {bean.getInspectId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(), bean.getOptName(),
				 "检验报告签发退回抽样单录入","检验报告编号："+bean.getReportId(),"1"};
		
		/*根据上一留痕Id获取当前留痕Id*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新当前步骤的留痕记录*/
		sqls[2] = "update dh_tranlist set deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1," +
				"deal_status='1',work_status='1',deal_view=?,back_type=? where pre_tran_id=? and work_status='0'";
		params[2] = new String[] {bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getIssueDesc(),bean.getBackType(),bean.getTranId()};
		
		/*更新抽样单信息，使之能继续修改*/
		sqls[3] = "update jd_sample_bill set remark=?,is_submit='0',is_inspect=?,current_node_id=?,current_status='9',tran_id=? where bill_id=?";
		params[3] = new String[] {bean.getIssueDesc(),isInspect,FlowConstant.CJ_NODE_ARR_ID[8],tranId,bean.getBillId()};
		
		/*逐级回退，需要把之前的留痕数据全都置为1(无效)*/
		if(bean.getBackType().equals("2")){
			sqls[4] = "update dh_tranlist set work_status='1' where flow_id='03' and work_id=?";
			params[4] = new String[] {bean.getBillId()};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-23
	* 签发退回到检验数据填报
	 * @throws SQLException 
	*/
	public Map<String, Object> checkReportToInspect(JdInspectReport bean) throws SQLException {
		String[] sqls;
		String[][] params;
		if(bean.getBackType().equals("1")){
			sqls = new String[5];
			params = new String[5][];
			/*直接回退，则更新检验报告管理表*/
			sqls[0] = "update jd_inspect_report set issue_status='2' where report_id=?";
			params[0] = new String[] {bean.getReportId()};
			/*更新检验数据信息，使之能再次修改*/
			sqls[4] = "update jd_inspect set remark=?,is_submit='0' where inspect_id=?";
			params[4] = new String[] {bean.getIssueDesc(),bean.getInspectId()};
			
		}else{
			sqls = new String[7];
			params = new String[7][];
			/*逐级回退，则把检验任务管理以下的数据都置为无效*/
			/*检验报告管理表置为无效*/
			sqls[0] = "update jd_inspect_report set issue_status='2',issue_opt_id=?,issue_opt_name=?,issue_time=now(),is_valid='0' where report_id=?";
			params[0] = new String[] {bean.getOptId(),bean.getOptName(),bean.getReportId()};
			/*更新检验数据信息，使之能再次修改*/
			sqls[4] = "update jd_inspect set remark=?,is_submit='0',check_status='0' where inspect_id=?";
			params[4] = new String[] {bean.getIssueDesc(),bean.getInspectId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(), bean.getOptName(),
				 "检验报告签发退回检验数据填报","检验报告编号："+bean.getReportId(),"1"};
		
		/*根据上一留痕Id获取当前留痕Id*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新当前步骤的留痕记录*/
		sqls[2] = "update dh_tranlist set work_status='1',deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1," +
				"deal_status='1',deal_view=?,back_type=? where pre_tran_id=? and work_status='0'";
		params[2] = new String[] {bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getIssueDesc(),bean.getBackType(),bean.getTranId()};
		
		/*逐级回退，需要把之前的留痕数据全都置为1(无效)并给要退回到的留痕数据加一条新的留痕*/
		if(bean.getBackType().equals("2")){
			/*给要退回到的留痕数据加一条新的留痕,先查询,再重新插入*/
			String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where node_id=? and work_id=? and work_status=? ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {"0305",bean.getBillId(),"0"});
			if (crs.next()) {
				sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1',?)";
				params[5] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
						crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
						crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
						crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id"),tranId};
				/*更新抽样单信息，记录当前的状态和tranId*/
				sqls[3] = "update jd_sample_bill set current_node_id=?,current_status='4',tran_id=? where bill_id=?";
				params[3] = new String[] {FlowConstant.CJ_NODE_ARR_ID[3],crs.getString("pre_tran_id"),bean.getBillId()};
			}
			/*之前的留痕数据全都置为1(无效)*/
			sqls[6] = "update dh_tranlist set work_status='1' where work_id=? and work_status='0' and deal_status='1' and node_id in ('0305','0306','0307','0308','0309')";
			params[6] = new String[] {bean.getBillId()};
		}else{
			/*更新抽样单信息，记录当前的状态和tranId*/
			sqls[3] = "update jd_sample_bill set current_node_id=?,current_status='9',tran_id=? where bill_id=?";
			params[3] = new String[] {FlowConstant.CJ_NODE_ARR_ID[8],tranId,bean.getBillId()};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-23
	*签发退回到检验报告编制
	 * @throws SQLException 
	*/
	public Map<String, Object> checkReportToReport(JdInspectReport bean) throws SQLException {
		String[] sqls;
		String[][] params;
		if(bean.getBackType().equals("1")){
			sqls = new String[4];
			params = new String[4][];
			/*直接回退，则更新检验报告管理表，使得能再次重新提交*/
			sqls[0] = "update jd_inspect_report set is_submit='0',issue_status='2',remark=? where report_id=?";
			params[0] = new String[] {bean.getIssueDesc(),bean.getReportId()};
		}else{
			sqls = new String[6];
			params = new String[6][];
			/*逐级回退，修改检验报告管理表，使得能再次重新提交审核*/
			sqls[0] = "update jd_inspect_report set is_submit='0',check_status='0',issue_status='0',remark=? where report_id=?";
			params[0] = new String[] {bean.getIssueDesc(),bean.getReportId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(), bean.getOptName(),
				 "检验报告签发退回检验报告编制","检验报告编号："+bean.getReportId(),"1"};
		
		/*根据上一留痕Id获取当前留痕Id*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新当前步骤的留痕记录*/
		sqls[2] = "update dh_tranlist set deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1," +
				"deal_status='1',work_status='1',deal_view=?,back_type=? where pre_tran_id=? and work_status='0'";
		params[2] = new String[] {bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getIssueDesc(),bean.getBackType(),bean.getTranId()};
		
		if(bean.getBackType().equals("2")){
			/*逐级回退，需要把之前的留痕数据全都置为1(无效)并给要退回到的留痕数据加一条新的留痕*/
			/*给要退回到的留痕数据加一条新的留痕,先查询,再重新插入*/
			String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where node_id=? and work_id=? and work_status=? ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {"0307",bean.getBillId(),"0"});
			if (crs.next()) {
				sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1',?)";
				params[4] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
						crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
						crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
						crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id"),tranId};
			
				/*更新抽样单信息，记录当前的状态和tranId*/
				sqls[3] = "update jd_sample_bill set current_node_id=?,current_status='6',tran_id=? where bill_id=?";
				params[3] = new String[] {FlowConstant.CJ_NODE_ARR_ID[5],crs.getString("pre_tran_id"),bean.getBillId()};
			}
			/*之前的留痕数据全都置为1(无效)*/
			sqls[5] = "update dh_tranlist set work_status='1' where work_id=? and work_status='0' and deal_status='1' and node_id in ('0307','0308','0309')";
			params[5] = new String[] {bean.getBillId()};
		}else{
			/*直接退回*/
			/*更新抽样单信息，记录当前的状态和tranId*/
			sqls[3] = "update jd_sample_bill set current_node_id=?,current_status='9',tran_id=? where bill_id=?";
			params[3] = new String[] {FlowConstant.CJ_NODE_ARR_ID[8],tranId,bean.getBillId()};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
}
