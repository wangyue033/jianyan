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
import framework.dhcc.utils.LogUtil;

public class JdInspectReportCheckImpl {

	/**
	 * @author longxl
	 */
	Page page = new Page();

	public String getJdInspectReportList(JdInspectReport jdInspectReport, String curPage, String perNumber,
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

		String sql = "select c.bill_id,c.tran_id,a.report_id,c.sample_no,c.report_no,a.inspect_id,a.irp_title,a.verification_code,a.inspect_type,a.is_valid,a.issue_status,a.check_status,a.report_path,e.enterprise_name," +
				"b.handle_dept_id,b.handle_dept_name,d.ita_title,ifNull(a.check_dept_id,'') check_dept_id,ifNull(a.check_opt_id,'') check_opt_id from jd_inspect_report a,jd_plan_handle b,jd_sample_bill c,jd_inspect d,jd_enterprise_info e " +
				"where a.ep_id = e.enterprise_id and d.inspect_id = a.inspect_id and d.bill_id = c.bill_id and c.task_id = b.task_id and a.is_valid = '1' and a.check_status != '0' and b.handle_dept_id like '"+jdInspectReport.getCompanyId()+"%' and a.abolish_status='0' ";

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
			if(crs.getString("check_opt_id").equals(jdInspectReport.getOptId())||(crs.getString("check_opt_id").equals("")&&crs.getString("check_dept_id").equals(jdInspectReport.getCompanyId()))){
				bean.setIsMain("1");
			}else{
				bean.setIsMain("0");
			}
			
			bean.setBillId(crs.getString("bill_id"));
			bean.setTranId(crs.getString("tran_id"));
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
				" a.is_assigned, a.check_status,a.depart_id,a.opt_id, a.opt_time, b.ita_title, b.bill_id, d.comp_name, g.enterprise_name, f.company_name," +
				" a.remark from jd_inspect_report a, jd_inspect b, jd_sample_bill c, jd_plan_task d, hr_staff e, hr_company f," +
				" jd_enterprise_info g where b.is_assinged = '1' and b.is_valid = '1' and b.sign_status = '1' and" +
				" b.bill_id = c.bill_id and c.check_status = '3' and c.is_valid = '1' and c.task_id = d.task_id and" +
				" a.opt_id = e.serial_id and e.company_id = f.company_id and" +
				" c.ep_id = g.enterprise_id and g.is_valid = '1' and a.check_status != '0' and 1 = 1 and a.report_id = ? ";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			JdInspectReport bean = new JdInspectReport();
			
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setParentId(crs.getString("product_id"));
			//解析产品
			//JSon转成字符串
			String product = "";
			/*
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("product_id")==null || 
					crs.getString("product_id").trim().equals(""))?"[]":crs.getString("product_id"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				product+= objs.get("name")+",";
			}
			*/
			product = product.length()==0?product:product.substring(0, product.length()-1);
			bean.setProductName(product);
			
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
			
			bean.setDepartId(crs.getString("depart_id"));
			String sql1 = "select company_name from hr_company a,jd_inspect_report b where a.company_id=b.depart_id and b.depart_id=? ";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[] {crs.getString("depart_id")});
			if(crs1.next()){
				bean.setCompName(crs1.getString("company_name"));
			}
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
	//审核
	public Map<String, Object> checkJdInspectReport(JdInspectReport bean) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = null;
		String[][] params = null;
		String tranId7 = bean.getTranId();
		//获取当前的tranId和preTranId
		String sql = "select tran_id from dh_tranlist where pre_tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getTranId()});
		bean.setPreTranId(bean.getTranId());//上一步骤的tranId
		if(crs.next()){
			bean.setTranId(crs.getString("tran_id"));//当前步骤的tranId
		}
		
		//退回，修改抽样单信息
		if("030101".equals(bean.getNextStepId())){
			if("1".equals(bean.getBackType())){//,直接回退
				sqls = new String[4];
				params = new String[4][];
				sqls[0] = "update jd_inspect_report set check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where report_id = ? ";
				params[0] = new String[] {bean.getCheckDesc(),bean.getOptId(),bean.getOptName(), bean.getReportId() };
				sqls[2] = "update jd_sample_bill set is_submit='0',tran_id=?,current_node_id=?,current_status='8' where bill_id = ?";
				params[2] = new String[]{bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[7],bean.getBillId()};
				
				sqls[3] = "update dh_tranlist set back_type ='1',work_status='1',deal_view=?,deal_opt_time=now() where tran_id = ?";
				params[3] = new String[]{bean.getCheckDesc(),bean.getTranId()};
			}
			if("2".equals(bean.getBackType())){//,逐级回退
				sqls = new String[6];
				params = new String[6][];
				sqls[0] = "update jd_inspect_report set is_valid='0',check_status='0',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where report_id = ? ";
				params[0] = new String[] {bean.getCheckDesc(),bean.getOptId(),bean.getOptName(), bean.getReportId() };
				sqls[2] = "update jd_sample_bill set tran_id=?,is_inspect='0',current_node_id=?,current_status='8',is_submit='0',check_status='2' where bill_id = ?";
				params[2] = new String[]{bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[7],bean.getBillId()};
				
				sqls[3] = "update dh_tranlist set back_type ='2',work_status='1',deal_view=?,deal_opt_time=now() where pre_tran_id = ? and work_status='0'";
				params[3] = new String[]{bean.getCheckDesc(),bean.getPreTranId()};
				
				sqls[4] = "update dh_tranlist set work_status='1' where node_id in ('0301','0302','0303','0304','0305','0306','0307') and work_status='0' and work_id = ? ";
				params[4] = new String[]{bean.getBillId()};
				
				sqls[5] = "update jd_inspect set is_valid='0' where inspect_id=?";
				params[5] = new String[]{bean.getInspectId()};
				
			}
			
		}
		//退回，修改检验数据
		if("030501".equals(bean.getNextStepId())){
			
			//,直接回退
			if("1".equals(bean.getBackType())){
				sqls = new String[5];
				params = new String[5][];
				sqls[2] = "update jd_inspect set is_submit='0' where bill_id = ?";
				params[2] = new String[]{bean.getBillId()};
				
				sqls[3] = "update dh_tranlist set back_type ='1',work_status='1',deal_view=?,deal_opt_time=now() where pre_tran_id=? and work_status='0'";
				params[3] = new String[]{bean.getCheckDesc(),bean.getPreTranId()};
				
				sqls[4] = "update jd_sample_bill set tran_id=?,current_node_id=?,current_status='8' where bill_id = ?";
				params[4] = new String[]{bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[7],bean.getBillId()};
			}
			//,逐级回退
			if("2".equals(bean.getBackType())){
				sqls = new String[7];
				params = new String[7][];
				sqls[2] = "update jd_inspect set is_submit='0',check_status='0' where inspect_id=?";
				params[2] = new String[]{bean.getInspectId()};
				
				sqls[3] = "update dh_tranlist set work_status='1' where work_id = ? and node_id in ('0305','0306','0307') and work_status='0' and deal_status='1'";
				params[3] = new String[]{bean.getBillId()};
				
				sqls[4] = "update dh_tranlist set deal_opt_time=now(),deal_status='1',back_type ='2',work_status='1',deal_view=? where tran_id=?";
				params[4] = new String[]{bean.getCheckDesc(),bean.getTranId()};
				
				//获取当前的第5步tranId
				String sql1 = "select tran_id from dh_tranlist where work_id=? and node_id='0305' and work_status='0'";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[]{bean.getBillId()});
				String tranId5= null;
				String preTranId = null;
				if(crs1.next()){
					tranId5=crs1.getString("tran_id");
				}
				/*先查询上一步骤的信息，再重新插入*/
				String sql2 = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
						"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
				CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] {tranId5});
				if (crs2.next()) {
					sqls[5] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
							+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
							+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type,deal_view) "
							+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1',?)";
					params[5] = new String[] { DBFacade.getInstance().getID(),crs2.getString("work_id"),crs2.getString("flow_id"),
							crs2.getString("node_id"),crs2.getString("step_id"),crs2.getString("detail_id"),crs2.getString("belong_work"),crs2.getString("tran_title"),crs2.getString("create_dept_id"),
							crs2.getString("create_dept_name"),crs2.getString("create_opt_id"),crs2.getString("create_opt_name"),crs2.getString("deal_part_id"),crs2.getString("deal_part_name"),
							crs2.getString("deal_opt_id"),crs2.getString("deal_opt_name"),crs2.getString("opt_order"),crs2.getString("next_dept"),crs2.getString("next_person"),crs2.getString("pre_tran_id"),bean.getTranId()};
					preTranId = crs2.getString("pre_tran_id");
				}
				sqls[6] = "update jd_sample_bill set is_inspect='0',tran_id=?,current_node_id=?,current_status='4' where bill_id = ?";
				params[6] = new String[]{preTranId,FlowConstant.CJ_NODE_ARR_ID[3],bean.getBillId()};
			}
			sqls[0] = "update jd_inspect_report set is_valid='0',check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where report_id = ? ";
			params[0] = new String[] { bean.getCheckDesc(),bean.getOptId(),bean.getOptName(), bean.getReportId() };
			
		}
		//退回，重新编制检验报告,直接回退,逐级回退
		if("030701".equals(bean.getNextStepId())){
			sqls = new String[6];
			params = new String[6][];
			
			sqls[0] = "update jd_inspect_report set check_status='2',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where report_id = ? ";
			params[0] = new String[] {bean.getCheckDesc(),bean.getOptId(),bean.getOptName(), bean.getReportId() };
			sqls[2] = "update dh_tranlist set back_type='3',deal_opt_time=now(),work_status='1',deal_status='1',deal_view=? where tran_id=?";
			params[2] = new String[]{bean.getCheckDesc(),bean.getTranId()};

			/*先查询上一步骤的信息，再重新插入*/
			String sql2 = "select work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,create_dept_id,create_dept_name,create_opt_id,create_opt_name," +
					"deal_part_id,deal_part_name,deal_opt_id,deal_opt_name,opt_order,next_dept,next_person,pre_tran_id from dh_tranlist where tran_id=?";
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,new String[] {bean.getPreTranId()});
			String newTranId8 = null;
			if (crs2.next()) {
				sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
						+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
						+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,next_dept,next_person,pre_tran_id,data_resource,data_type,deal_view) "
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,'0','0','0','0',?,?,?,?,'1','1',?)";
				params[4] = new String[] { DBFacade.getInstance().getID(),crs2.getString("work_id"),crs2.getString("flow_id"),
						crs2.getString("node_id"),crs2.getString("step_id"),crs2.getString("detail_id"),crs2.getString("belong_work"),crs2.getString("tran_title"),crs2.getString("create_dept_id"),
						crs2.getString("create_dept_name"),crs2.getString("create_opt_id"),crs2.getString("create_opt_name"),crs2.getString("deal_part_id"),crs2.getString("deal_part_name"),
						crs2.getString("deal_opt_id"),crs2.getString("deal_opt_name"),crs2.getString("opt_order"),crs2.getString("next_dept"),crs2.getString("next_person"),crs2.getString("pre_tran_id"),bean.getTranId()};
				newTranId8 = crs2.getString("pre_tran_id");
			}
			sqls[3] = "update jd_sample_bill set tran_id=?,current_node_id='0308',current_status='8' where bill_id = ?";
			params[3] = new String[]{newTranId8,bean.getBillId()};
			sqls[5] = "update dh_tranlist set work_status='1' where tran_id=?";
			params[5] = new String[]{tranId7};
		}
		
		//审核通过，检验报告签发
		if("030901".equals(bean.getNextStepId())){
			sqls = new String[5];
			params = new String[5][];
			//获取当前的tranId和preTranId
//			String sql2 = "select tran_id from dh_tranlist where pre_tran_id=?";
//			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[]{bean.getTranId()});
//			bean.setPreTranId(bean.getTranId());//上一步骤的tranId
//			if(crs2.next()){
//				bean.setTranId(crs.getString("tran_id"));//当前步骤的tranId
//			}
			sqls[0] = "update jd_inspect_report set issue_opt_name=?,issue_opt_id=?,check_status='3',check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where report_id = ? ";
			params[0] = new String[] { bean.getNextDealName(),bean.getNextDealId(),bean.getCheckDesc(),bean.getOptId(),bean.getOptName(), bean.getReportId() };
			
			//更新当前这条留痕
			sqls[2] = "update dh_tranlist set next_dept=?,next_person=?,work_status='0',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_view=? where pre_tran_id=?";
			params[2] = new String[]{bean.getNextDept(),bean.getNextPerson(),bean.getOptId(),bean.getOptName(),bean.getCheckDesc(),tranId7};
			//插入下一步骤留痕
			sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_sample_bill','检验报告签发',?,?,?,?,now(),?,?,?,?,'0','0','0','0','9',?,'1','1')";
			params[3] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
					FlowConstant.CJ_NODE_ARR_ID[8],FlowConstant.CJ_STEP_ARR_ID[8],bean.getReportId(),bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getNextDeptId(),bean.getNextDeptName(),
					bean.getNextDealId(),bean.getNextDealName(),bean.getTranId()};
			
			sqls[4] = "update jd_sample_bill set tran_id=?,current_node_id=?,current_status='8' where bill_id = ?";
			params[4] = new String[]{bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[7],bean.getBillId()};
		
		}
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验报告审核",
				"检验报告ID：" + bean.getReportId() + ";检验报告名称："
						+ bean.getIrpTitle());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		
		
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
}