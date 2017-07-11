package com.dhcc.inspect.impl;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectReport;
import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.LogUtil;

public class JdInspectReportImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getJdInspectReportList(JdInspectReport jdInspectReport, String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String compId,String optId) throws Exception {

		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));

		String sql = "select f.tran_title,f.deal_view,c.bill_id,c.current_node_id,c.tran_id,a.report_id,c.sample_no,c.report_no,a.inspect_id,a.irp_title,a.verification_code,a.inspect_type,a.is_valid,a.is_submit,a.issue_status,a.check_status,a.report_path,e.enterprise_name," +
				"b.handle_dept_id,b.handle_dept_name,d.ita_title,f.back_type from jd_inspect_report a,jd_plan_handle b,jd_sample_bill c left join dh_tranlist f on c.tran_id=f.tran_id,jd_inspect d,jd_enterprise_info e " +
				"where a.ep_id = e.enterprise_id and d.inspect_id = a.inspect_id and d.bill_id = c.bill_id and c.task_id = b.task_id and a.is_valid = '1' and b.handle_dept_id like '%"+compId+"%' and (a.abolish_status = '0' or a.abolish_status is null ) and d.main_opt_id="+optId+"  ";

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
			bean.setBillId(crs.getString("bill_id"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setReportNo(crs.getString("report_no"));
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
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setReportPath(crs.getString("report_path"));
			bean.setCurrentNodeId(crs.getString("current_node_id"));
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
	
	public Map<String, Object> delJdInspectReport(JdInspectReport bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];
		
		sqls[0] = "delete from JD_INSPECT_REPORT where report_id=? ";
//		sqls[0] = "update jd_inspect set is_valid=? where inspect_id = ? ";
		params[0] = new String[] { bean.getReportId() };
//		params[0] = new String[] { "0", bean.getInspectId() + "%"};

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验报告删除",
				"检验报告ID：" + bean.getReportId() + ";检验报告名称："
						+ bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		sqls[2] = "update jd_inspect set check_status = '1' ";
		params[2] = new String[] { bean.getReportId() };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	public Map<String, Object> editJdInspectReport(JdInspectReport bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = "update jd_inspect_report set product_id=?,inspect_type=?,remark=? where report_id = ? ";
		params[0] = new String[] { bean.getProductId(),bean.getInspectType(),bean.getRemark(), bean.getReportId() };

		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验报告修改",
				"检验报告签发ID：" + bean.getReportId() + ";检验报告名称："
						+ bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		bean.setParentId(bean.getProductId());
		
		DBFacade.getInstance().execute(sqls, params);
//		addJdInspectReport(bean);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}
	
	public Map<String, Object> submitJdInspectReport(JdInspectReport bean) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getTranId()});
		bean.setPreTranId(bean.getTranId());//上一步骤的tranId
		if(crs.next()){
			bean.setTranId(crs.getString("tran_id"));//当前步骤的tranId
		}
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		
		bean.setNextDept("[{'id':'"+bean.getNextDeptId()+"',"+"'name':'"+bean.getNextDeptName()+"'}]");
		bean.setNextPerson("[{'id':'"+bean.getNextDealId()+"',"+"'name':'"+bean.getNextDealName()+"'}]");
		sqls[0] = "update jd_inspect_report set is_submit=?,submit_opt_time=now(),check_status=?,check_dept_id=?,check_dept_name=?,check_opt_id=?,check_opt_name=? where report_id = ? ";
		params[0] = new String[] { "1","1",bean.getNextDeptId(),bean.getNextDeptName(),bean.getNextDealId(),bean.getNextDealName(),bean.getReportId() };
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验报告提交",
				"检验报告签发ID：" + bean.getReportId() + ";检验报告名称："
						+ bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		//更新当前这条留痕
		sqls[2] = "update dh_tranlist set detail_id=?,deal_part_id=?,deal_part_name=?,next_dept=?,next_person=?,deal_part_id=?,deal_part_name=?,work_status='0',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1 where tran_id=?";
		params[2] = new String[]{bean.getReportId(),bean.getCompanyId(),bean.getCompanyName(),bean.getNextDept(),bean.getNextPerson(),bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
		//插入下一步骤留痕
		sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','检验报告审核',?,?,?,?,now(),?,?,?,?,'0','0','0','0','8',?,'1','1')";
		params[3] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[7],FlowConstant.CJ_STEP_ARR_ID[7],bean.getReportId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getNextDeptId(),bean.getNextDeptName(),
				bean.getNextDealId(),bean.getNextDealName(),bean.getTranId()};
		
		
		sqls[4] = "update jd_sample_bill set tran_id=?,current_node_id=?,current_status='7' where bill_id = ?";
		params[4] = new String[]{bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[6],bean.getBillId()};
		
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	public Map<String, Object> getJdInspectReportById(String reportId) throws Exception {
//		String sql = "select distinct a.report_id,a.inspect_id,a.product_id,a.irp_title,a.verification_code,a.inspect_type,a.is_valid," +
//				" a.is_assigned,a.check_status,a.opt_id,a.opt_time,b.ita_title,b.bill_id,d.comp_name,g.enterprise_name,f.company_name," +
//				" a.remark,a.ep_id,a.depart_id from jd_inspect_report a,jd_inspect b,jd_sample_bill c,jd_plan_task d,hr_staff e,hr_company f," +
//				" jd_enterprise_info g where b.is_assinged = '1' and b.is_valid = '1' and b.sign_status = '1' and" +
//				" b.bill_id = c.bill_id and c.check_status = '3' and c.is_valid = '1' and c.task_id = d.task_id and" +
//				" a.opt_id = e.serial_id and e.company_id = f.company_id and a.inspect_id = b.inspect_id and " +
//				" c.ep_id = g.enterprise_id and g.is_valid = '1' and 1 = 1 and a.report_id = ? ";
		String sql = "select a.report_id,a.depart_id,a.inspect_id,a.ep_id,a.remark,a.product_id,a.irp_title," +
				"a.verification_code,a.inspect_type,a.is_valid,a.issue_status,a.check_status,e.enterprise_name, " +
				"c.product_name,b.handle_dept_id,b.handle_dept_name,d.ita_title from jd_inspect_report a,"+
				"jd_plan_handle b,jd_sample_bill c,jd_inspect d,jd_enterprise_info e where c.ep_id = e.enterprise_id "+
				"and d.inspect_id = a.inspect_id and d.bill_id = c.bill_id and c.task_id = b.task_id and a.is_valid = '1' and a.report_id=? ";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			
			bean.setInspectId(crs.getString("inspect_id"));
//			bean.setParentId(crs.getString("product_id"));
//			//解析产品
//			//JSon转成字符串
//			String product = "";
//			JSONArray jArray2 = JSONArray.fromObject((crs.getString("product_id")==null || 
//					crs.getString("product_id").trim().equals(""))?"[]":crs.getString("product_id"));
//			for(Object obj : jArray2){
//				Map objs = JSONObject.fromObject(obj);
//				product+= objs.get("name")+",";
//			}
//			
//			product = product.length()==0?product:product.substring(0, product.length()-1);
//			bean.setProductName(product);
			bean.setParentId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setIrpTitle(crs.getString("irp_title"));
			bean.setVerificationCode(crs.getString("verification_code"));
			bean.setInspectType(crs.getString("inspect_type"));
			bean.setIsAssigned(crs.getString("issue_status"));
			bean.setCheckStatus(crs.getString("check_status"));
			bean.setItaTitle(crs.getString("ita_title"));
//			bean.setBillId(crs.getString("bill_id"));
			
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setRemark(crs.getString("remark"));
			bean.setReportId(crs.getString("report_id"));
			//bean.setCompName(crs.getString("comp_name"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setCompanyName(crs.getString("handle_dept_name"));
//			bean.setOptId(crs.getString("opt_id"));
			
			bean.setEpId(crs.getString("ep_id"));
			bean.setDepartId(crs.getString("depart_id"));
			String sql1 = "select company_name from hr_company a,jd_inspect_report b where a.company_id=b.depart_id and b.depart_id=? ";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[] {crs.getString("depart_id")});
			if(crs1.next()){
				bean.setCompName(crs1.getString("company_name"));
			}
//			SimpleDateFormat formatter3 = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			String optTime = crs.getString("opt_time");
//			String time = formatter3.format(formatter3.parse(optTime));
//			bean.setOptTime(time);
			
			map.put("record", bean);
			map.put("msg", "true");
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}
	
public void addJdInspectReport(JdInspectReport bean) throws Exception {
		
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		String sql = "select company_id from hr_staff  where serial_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getOptId()});
		if (crs.next()) {
			bean.setCompId(crs.getString("company_id"));
		} else {
			bean.setCompId("");
		}
		
		String db = DBFacade.getInstance().getID();
		
		sqls[0] = "insert into jd_inspect_report(report_id,inspect_id,irp_title,verification_code,product_id," +
				"depart_id,comp_id,ep_id,inspect_type,is_valid,check_status,issue_status,remark,opt_id,opt_time) " +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		params[0]  = new String[] { db, bean.getInspectId(), bean.getIrpTitle(), db,
				bean.getParentId(), bean.getDepartId(), bean.getCompId().substring(0, 10), bean.getEpId(), bean.getInspectType(), 
				bean.getIsValid(), "0", "0", bean.getRemark()==null||bean.getRemark().equals("")?"无":bean.getRemark(), bean.getOptId() };
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验报告添加",
				"检验报告签发ID：" + bean.getReportId() + ";检验报告名称："
						+ bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		DBFacade.getInstance().execute(sqls, params);
	}
	
	/**
	 * 树形结构
	 *@return List<ZTreeBean>
	 */
	public List<ZTreeBean> getTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select inspect_id,bill_id,ita_title from jd_inspect where is_valid = '1' and is_handle = '1' and sign_status = '1' order by inspect_id ";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				String sql1 = "select count(*) from jd_inspect_report where inspect_id = ? ";
				long count = (Long) DBFacade.getInstance().getValueBySql(sql1,
						new String[] { crs.getString("inspect_id") });
				if (count > 0) {
					continue;
				}
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("inspect_id"));
				bean.setName(crs.getString("ita_title"));
				bean.setDepth("1");
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public List<ZTreeBean> getTreeCanParentRoot() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select type_id,type_name,parent_id from jd_product_type where is_valid='1' order by type_name";
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
			String s = "select product_id,type_id,product_name from jd_enterprise_product where is_valid='1' order by product_name ";
			CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, null);
			while (c.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(c.getString("product_id"));
				bean.setpId(c.getString("type_id"));
				bean.setName(c.getString("product_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}

	/**
	 * @return info
	 * @throws SQLException 
	 */
	public String getInfo (String inspectId) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdInspectReport> list = new ArrayList<JdInspectReport>();
		
		String sql = "select b.product_id,b.product_name,c.comp_name,c.comp_id,d.enterprise_name,b.ep_id,b.product_model,e.company_name,c.task_type from jd_inspect a, jd_sample_bill b," +
				" jd_plan_task c,jd_enterprise_info d,hr_company e where a.bill_id = b.bill_id and b.task_id = c.task_id and" +
				" b.ep_id = d.enterprise_id and d.is_valid = '1' and c.comp_id=e.company_id and a.inspect_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{inspectId});
		while (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setCompName(crs.getString("company_name"));
			bean.setDepartId(crs.getString("comp_id"));
			bean.setEpId(crs.getString("ep_id"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setTaskType(crs.getString("task_type"));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	// 编号重复验证
	public boolean checkPlanNo(JdInspectReport bean ,String product_id, int flag) {
		String sql = "select count(*) from jd_inspect_report where product_id = ? ";
		if (flag == 1) {
			sql += " and report_id != " + bean.getReportId();
		}
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { product_id });
			if (count>0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 标题是否重复
	public boolean editCheckPlanNo(String reportId, String irpTitle, int flag) {
		String sql = "select count(*) from jd_inspect_report where irp_title = ?";
		if (flag == 1) {
			sql += " and report_id != " + reportId;
		}
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { irpTitle });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Map<String, Object> getJdInspectReports(String enterpriseId,
			String searchField, String searchValue) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdProductInfo> list = new ArrayList<JdProductInfo>();
		
		String sql = "select a.product_id,a.product_name,a.type_id,b.type_name from jd_enterprise_product a left join jd_product_type b on a.type_id = b.type_id where a.is_valid='1' and a.enterprise_id = ?";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{enterpriseId});
		try {
			while (crs.next()) {
				JdProductInfo bean = new JdProductInfo();
				bean.setProductId(crs.getString("product_id"));
				bean.setProductName(crs.getString("product_name"));
				bean.setTypeId(crs.getString("type_id"));
				bean.setTypeName(crs.getString("type_name"));
				list.add(bean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("record", list);
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-24
	* 直接回退后的提交
	* @throws SQLException 
	*/
	public Map<String, Object> submitReport(JdInspectReport bean) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[4];
		String[][] params = new String[4][];
		
		/*先找出上一步骤留痕Id*/
		String[] preTranId = {};
		try {
			preTranId=getTranId(bean.getTranId()).split(",");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sqls[0] = "update jd_sample_bill set tran_id=?,current_node_id=?,current_status=?,is_submit='1',submit_opt_time=now() where bill_id=?";
		params[0] = new String[] { preTranId[0],preTranId[1],preTranId[2],bean.getBillId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"检验报告信息提交","检验报告ID："+bean.getReportId(),"1"};
		
		//为检验报告签发添加一条新的留痕数据
		String sql = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
				"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getTranId()});
		if (crs.next()) {
			sqls[2] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1')";
			params[2] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
					crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
					crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
					crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id")};
		}
		
		/*修改检验报告签发的状态，使得能再次签发*/
		if(preTranId[2].equals("8")){
			sqls[3] = "update jd_inspect_report set is_submit='1',submit_opt_time=now(),issue_status='0' where report_id=?";
			params[3] = new String[] {preTranId[3]};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-24
	* 根据当前留痕Id获取上一留痕Id
	* @throws SQLException 
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select pre_tran_id from dh_tranlist where tran_id=? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("pre_tran_id");
		}
		String sql2 = "select node_id,opt_order,detail_id from dh_tranlist where tran_id=? ";
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] { preTranId});
		if (crs2.next()) {
			preTranId += ","+crs2.getString("node_id");
			preTranId += ","+crs2.getString("opt_order");
			preTranId += ","+crs2.getString("detail_id");
		}
		System.out.println(preTranId+"直接");
		return preTranId;
	}
	
}
