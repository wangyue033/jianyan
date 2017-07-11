package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.dhcc.inspect.domain.JdSampleBill;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBillCheckImpl {
	Page page = new Page();
	public String getJdSampleBillList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String companyId,String handleDeptId,String userId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.tran_id,a.bill_id,a.sample_num,a.task_id,a.sample_id,a.ep_id,a.comp_id,a.product_id,a.product_name," +
				"a.sample_date,a.is_submit,a.check_status,b.task_name,c.sample_title,d.enterprise_name,e.company_name,ifNull(a.check_dept_id,'') check_dept_id,"+
				"ifNull(a.check_opt_id,'') check_opt_id from jd_plan_task b left join jd_plan_handle g on b.task_id=g.task_id,jd_sample_bill a,jd_sample c,jd_enterprise_info d,hr_company e,jd_plan_handle f" +
				" where a.task_id=b.task_id and a.sample_id=c.sample_id and a.ep_id=d.enterprise_id and a.comp_id=e.company_id and b.task_id=f.task_id " +
				"and a.is_submit='1' and g.inspet_dept_id like '"+handleDeptId+"%' ";
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
			page.setSidx("a.check_status");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSampleBill> list = new ArrayList<JdSampleBill>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSampleBill bean = new JdSampleBill();
			String sql1 = "select check_opt_id from JD_SAMPLE_BILL where bill_id=?";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] { crs.getString("bill_id")});
			while(crs1.next()){
				if(crs.getString("check_opt_id").equals(userId)||(crs.getString("check_opt_id").equals("")&&crs.getString("check_dept_id").equals(companyId))){
					bean.setIsMain("1");/*以此控制审核人的权限*/
				}else{
					bean.setIsMain("0");
				}
				
				bean.setBillId(crs.getString("bill_id"));
				bean.setTranId(crs.getString("tran_id"));
				bean.setSampleNum(crs.getString("sample_num"));
				bean.setTaskId(crs.getString("task_id"));
				bean.setTaskName(crs.getString("task_name"));
				bean.setSampleId(crs.getString("sample_id"));
				bean.setEpId(crs.getString("ep_id"));
				bean.setCompId(crs.getString("comp_id"));
				bean.setProductId(crs.getString("product_id"));
				bean.setProductName(crs.getString("product_name"));
				bean.setSampleDate(crs.getString("sample_date"));
				bean.setIsSubmit(crs.getString("is_submit"));
				bean.setCheckStatus(crs.getString("check_status"));
				bean.setSampleTitle(crs.getString("sample_title"));
				bean.setEnterpriseName(crs.getString("enterprise_name"));
				bean.setCompanyName(crs.getString("company_name"));
				list.add(bean);
				
			}
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
	
	public Map<String, Object> getJdSampleBillById(JdSampleBill bean1)  {
		String sql = "select a.inspect_time,a.bill_id,a.sample_id,a.task_id,a.sample_num,a.comp_id,a.ep_id,a.ep_name,a.ep_corp,a.ep_address,a.ep_postcode,a.ep_linkman,a.ep_tel," +
				"a.scdw_id,a.scdw_name,a.scdw_address,a.scdw_legal,a.scdw_linkman,a.scdw_tel,a.scdw_post_code,a.scdw_licence,a.scdw_code,a.scdw_etype," +
				"a.scdw_people,a.scdw_output,a.scdw_yield,a.gyxk_num,a.qsxk_num,a.ccxk_num,a.qtxk_num,a.cert_num,a.product_id,a.product_name,a.trade_mark," +
				"a.product_model,a.product_date,a.product_batch,a.sample_qty,a.sample_base,a.batch_qty,a.backup_qty,a.sample_date,a.sample_state,a.product_standard," +
				"a.product_level,a.bak_place,a.save_place,a.post_place,a.post_end_date,a.is_export,a.sample_place,a.arrive_date,a.seal_person,a.remark," +
				"a.ep_seal,a.ep_seal_date,a.scdw_seal,a.scdw_seal_date,a.remark,a.is_submit,a.check_status,"+
				"a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.is_submit,"+TimeUtil.getTimeShow("a.submit_opt_time")+" submit_opt_time," +
				"a.check_status,a.check_desc,a.check_opt_name,"+TimeUtil.getTimeShow("a.check_opt_time")+" check_opt_time,a.is_inspect,b.enterprise_name from jd_sample_bill a,jd_enterprise_info b where a.ep_id=b.enterprise_id and bill_id=? ";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { bean1.getBillId() });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			if (crs.next()) {
				map.put("msg", "true");
				JdSampleBill bean = new JdSampleBill();
				bean.setInspectTime(crs.getString("inspect_time"));
				bean.setPostCode(crs.getString("ep_postcode"));
				bean.setBillId(crs.getString("bill_id"));
				bean.setSampleId(crs.getString("sample_id"));
				bean.setTaskId(crs.getString("task_id"));
				bean.setSampleNum(crs.getString("sample_num"));
				bean.setCompId(crs.getString("comp_id"));
				bean.setEpId(crs.getString("ep_id"));
				bean.setEpName(crs.getString("ep_name"));
				bean.setEpCorp(crs.getString("ep_corp"));
				bean.setEpAddress(crs.getString("ep_address"));
				bean.setEpLinkman(crs.getString("ep_linkman"));
				bean.setEpTel(crs.getString("ep_tel"));
				bean.setScdwId(crs.getString("scdw_id"));
				bean.setScdwName(crs.getString("scdw_name"));
				bean.setScdwAddress(crs.getString("scdw_address"));
				bean.setScdwLegal(crs.getString("scdw_legal"));
				bean.setScdwLinkman(crs.getString("scdw_linkman"));
				bean.setScdwTel(crs.getString("scdw_tel"));
				bean.setScdwPostcode(crs.getString("scdw_post_code"));
				bean.setScdwLicence(crs.getString("scdw_licence"));
				bean.setScdwCode(crs.getString("scdw_code"));
				bean.setScdwEtype(crs.getString("scdw_etype"));
				bean.setScdwPeople(crs.getString("scdw_people"));
				bean.setScdwOutput(crs.getString("scdw_output"));
				bean.setScdwYield(crs.getString("scdw_yield"));
				bean.setGyxkNum(crs.getString("gyxk_num"));
				bean.setQsxkNum(crs.getString("qsxk_num"));
				bean.setCcxkNum(crs.getString("ccxk_num"));
				bean.setQtxkNum(crs.getString("qtxk_num"));
				bean.setProductId(crs.getString("product_id"));
				bean.setProductName(crs.getString("product_name"));
				bean.setTradeMark(crs.getString("trade_mark"));
				bean.setProductModel(crs.getString("product_model"));
				bean.setProductDate(crs.getString("product_date"));
				bean.setProductBatch(crs.getString("product_batch"));
				bean.setSampleQty(crs.getString("sample_qty"));
				bean.setSampleBase(crs.getString("sample_base"));
				bean.setBatchQty(crs.getString("batch_qty"));
				bean.setBackupQty(crs.getString("backup_qty"));
				bean.setSampleDate(crs.getString("sample_date"));
				bean.setSampleState(crs.getString("sample_state"));
				bean.setProductStandard(crs.getString("product_standard"));
				bean.setProductLevel(crs.getString("product_level"));
				bean.setBakPlace(crs.getString("bak_place"));
				bean.setSavePlace(crs.getString("save_place"));
				bean.setPostPlace(crs.getString("post_place"));
				bean.setPostEndDate(crs.getString("post_end_date"));
				bean.setIsExport(crs.getString("is_export"));
				bean.setSamplePlace(crs.getString("sample_place"));
				bean.setArriveDate(crs.getString("arrive_date"));
				bean.setSealPerson(crs.getString("seal_person"));
				bean.setRemark(crs.getString("remark"));
				bean.setEpSeal(crs.getString("ep_seal"));
				bean.setEpSealDate(crs.getString("ep_seal_date"));
				bean.setScdwSeal(crs.getString("scdw_seal"));
				bean.setScdwSealDate(crs.getString("scdw_seal_date"));
				bean.setRemark(crs.getString("remark"));
				bean.setIsSubmit(crs.getString("is_submit"));
				bean.setCheckStatus(crs.getString("check_status"));
				bean.setOptName(crs.getString("opt_name"));
				bean.setOptTime(crs.getString("opt_time"));
				bean.setIsSubmit(crs.getString("is_submit"));
				bean.setSubmitOptTime(crs.getString("submit_opt_time"));
				bean.setCheckStatus(crs.getString("check_status"));
				bean.setCheckDesc(crs.getString("check_desc"));
				bean.setCheckOptName(crs.getString("check_opt_name"));
				bean.setCheckOptTime(crs.getString("check_opt_time"));
				bean.setIsInspect(crs.getString("is_inspect"));
				map.put("record", bean);
			} else {
				map.put("msg", "noresult");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public void JdSampleBillCheckshenhe(JdSampleBill bean) throws SQLException{
		
		String sql = "select tran_id from dh_tranlist where pre_tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getTranId()});
		
		bean.setPreTranId(bean.getTranId());//上一步骤的tranId
		if(crs.next()){
			bean.setTranId(crs.getString("tran_id"));//当前业务的tranId
		}
		
		String[] sqls = null;
		String[][] params = null;
		
		//审核通过
		if("030301".equals(bean.getNextStepId())){
			sqls = new String[4];
			params = new String[4][];
			sqls[0] = "update jd_sample_bill set current_status=2,tran_id=?,current_node_id=?,check_status='3',check_status=3,check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now(),is_overtime=case when TIMESTAMPDIFF(hour,(select select_end_date from jd_sample where sample_id=?),now())<0 then '0' else '1' end where bill_id=?";
			params[0] = new String[] {bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[1],bean.getCheckDesc(),bean.getOptId(),bean.getOptName(),bean.getSampleId(),bean.getBillId()};
			
			sqls[2] = "update dh_tranlist set deal_view=?,work_status='0',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1 where tran_id=?";
			params[2] = new String[]{bean.getCheckDesc(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
			//下一操作留痕,检验任务分派
			sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_sample_bill','检验任务分派',?,?,?,?,now(),?,?,'0','0','0','0','3',?,'1','1')";
			params[3] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
					FlowConstant.CJ_NODE_ARR_ID[2],FlowConstant.CJ_STEP_ARR_ID[2],bean.getBillId(),bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getCompanyId(),bean.getCompanyName(),bean.getTranId()};
		}
		//审核未通过
		if("030101".equals(bean.getNextStepId())){
			sqls = new String[4];
			params = new String[4][];
			sqls[0] = "update jd_sample_bill set current_status='2',is_submit='0',tran_id=?,current_node_id=?,check_status='2',check_status=2,check_desc=?,check_opt_id=?,check_opt_name=?,check_opt_time=now() where bill_id=?";
			params[0] = new String[] {bean.getTranId(),FlowConstant.CJ_NODE_ARR_ID[1],bean.getCheckDesc(),bean.getOptId(),bean.getOptName(),bean.getBillId()};
			
			sqls[2] = "update dh_tranlist set back_type='3',deal_view=?,work_status='1',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1 where tran_id=?";
			params[2] = new String[]{bean.getCheckDesc(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
			
			sqls[3] = "update dh_tranlist set work_status='1' where tran_id=?";
			params[3] = new String[]{bean.getPreTranId()};
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] { DBFacade.getInstance().getID(),bean.getOptId(), bean.getOptName(), "抽样单信息审核状态修改","抽样单ID：" + bean.getBillId(), "1" };
		
		DBFacade.getInstance().execute(sqls, params);
	}
}
