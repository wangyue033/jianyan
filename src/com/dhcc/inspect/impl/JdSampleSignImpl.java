package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.domain.JdSampleBase;
import com.dhcc.inspect.domain.JdSampleBill;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.CnToPiny;
import framework.dhcc.utils.LogUtil;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleSignImpl {
	Page page = new Page();

	public String getJdSampleSignList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String OptId,String CompanyId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select ab.*,count(c.bill_id) bill_count from(select a.sample_id,a.task_id,a.sample_title,a.handle_dept_name,a.select_area,a.select_enterprise,"
					+ "a.select_comp_name,a.is_handle,a.sign_status,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.task_name "
					+ "from jd_sample a,jd_plan_task b "
					+ "where a.task_id=b.task_id and (a.is_handle = '1' or a.sign_status!='0') " 
					+ "and (case when a.select_opt_id is null or a.select_opt_id='' then LOCATE(a.select_comp_id,'"+CompanyId+"')>0 else a.select_opt_id LIKE '%"+OptId+"%'  end) "+
				     "and 1=1 ";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		sql = sql + " group by a.sample_id ) ab left join jd_sample_bill c on ab.sample_id = c.sample_id and c.check_status='3' group by ab.sample_id";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("ab.sample_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setBillCount(crs.getString("bill_count"));
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectArea(crs.getString("select_area"));
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setSignStatus(crs.getString("sign_status"));
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
	
	//新写，查看抽样单详情
	public Map<String, Object> getBillCount(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String OptId,String sampleId) throws SQLException{
		
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql = "select a.sample_no,a.product_name,a.is_inspect from jd_sample_bill a,jd_sample b where a.sample_id=b.sample_id and b.sample_id=? and a.check_status='3'";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		sql = sql + " group by a.sample_id ) ab left join jd_sample_bill c on ab.sample_id = c.sample_id and c.check_status='3' group by ab.sample_id";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("a.sample_no");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{sampleId});
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductName(crs.getString("product_name"));
			bean.setIsInspect(crs.getString("is_inspect"));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		return map;
	}
	
	
	public String getJdSampleBaseList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String OptId,String signCompId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.sample_id,a.sample_title,a.task_id,b.task_name,a.handle_dept_id,a.handle_dept_name," 
				  + "a.select_area,a.select_enterprise,a.sign_status,a.sign_desc,a.sign_opt_time,a.sign_opt_id," 
				  + "a.sign_opt_name,a.opt_id,a.opt_name,a.opt_time,d.company_id,d.company_name,count(c.bious_id) base_count " 
				  + "from jd_sample a left join jd_sample_base c on a.sample_id = c.sample_id ,jd_plan_task b,hr_company d " 
				  + "where a.task_id = b.task_id " 
				  + "and a.select_comp_id = d.company_id " 
				  + "and a.sign_status = '1' "
				  + "and (case when a.sign_opt_id is null or a.sign_opt_id='' then LOCATE(a.select_comp_id,'"+signCompId+"')>0 else a.sign_opt_id LIKE '%"+OptId+"%'  end)";

		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		sql = sql + "group by a.sample_id ";
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("sample_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectArea(crs.getString("select_area"));
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignCompId(crs.getString("company_id"));
			bean.setSignCompName(crs.getString("company_name"));
			bean.setBaseCount(crs.getString("base_count"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
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
	
	public String getJdSampleBillList(String curPage, String perNumber,
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
		String sql = "select a.sample_no,a.arrive_date,f.tran_title,f.deal_view,a.bill_id,a.sample_num,a.task_id,a.sample_id,a.ep_id,a.comp_id,a.product_id,a.product_name," +
				"a.sample_date,a.is_submit,a.check_status,b.task_name,c.sample_title,d.enterprise_name,e.company_name,a.current_node_id,a.tran_id,f.back_type "+
				" from jd_plan_task b left join jd_plan_handle g on b.task_id=g.task_id,jd_sample_bill a left join dh_tranlist f on a.tran_id=f.tran_id,jd_sample c,jd_enterprise_info d,hr_company e" +
				" where a.task_id=b.task_id and a.sample_id=c.sample_id and a.ep_id=d.enterprise_id and a.comp_id=e.company_id and g.inspet_dept_id like '"+compId+"%' ";
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
			page.setSidx("a.is_submit");
			page.setSord("asc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdSampleBill> list = new ArrayList<JdSampleBill>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSampleBill bean = new JdSampleBill();
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setArriveDate(crs.getString("arrive_date"));
			bean.setTranTitle(crs.getString("tran_title"));
			bean.setDealView(crs.getString("deal_view"));
			bean.setBillId(crs.getString("bill_id"));
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
			bean.setTranId(crs.getString("tran_id"));
			bean.setCurrentNodeId(crs.getString("current_node_id"));
			bean.setBackType(crs.getString("back_type"));
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
	
	
	public void deleteSampleBase(String sampleId){
		String[] sqls = new String[1];
		String[][] params = new String[1][];
		sqls[0]="delete from jd_sample_base where sample_id=?";
		params[0] = new String[] {sampleId};
		DBFacade.getInstance().execute(sqls, params);
	}
	public void addJdSampleSign(JdSample bean) throws Exception {
		int size=0;
		size=bean.getSpList().size();
		String[] sqls = new String[1+size];
		String[][] params = new String[1+size][];
		sqls[0] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[0] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样基本信息添加",
				"抽样任务ID：" + bean.getSampleId(), "1" };
		ArrayList<JdSampleBase> list=bean.getSpList();
		for(int i = 1;i<list.size()+1;i++){
			sqls[i]="insert into jd_sample_base(bious_id,sample_id,ep_id,product_id,cert_url,product_url,description,remark,is_valid,opt_id,opt_time) values(?,?,?,?,?,?,?,?,?,?,now())";
			params[i] = new String[]{DBFacade.getInstance().getID(),bean.getSampleId(),list.get(i-1).getEnterpriseId(),list.get(i-1).getProductId(),list.get(i-1).getCertUrl(),list.get(i-1).getProductUrl(),list.get(i-1).getDescription(),list.get(i-1).getRemark(),"1",bean.getOptId()};
		}
		DBFacade.getInstance().execute(sqls, params);
	}
	public void editJdSampleBase(JdSample bean)throws Exception {
		int size=0;
		size=bean.getSpList().size();
		String[] sqls = new String[1+size];
		String[][] params = new String[1+size][];
		deleteSampleBase(bean.getSampleId());
		sqls[0] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[0] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样基本信息修改",
				"抽样任务ID：" + bean.getSampleId(), "1" };
		ArrayList<JdSampleBase> list=bean.getSpList();
		for(int i = 1;i<list.size()+1;i++){
			sqls[i]="insert into jd_sample_base(bious_id,sample_id,ep_id,product_id,cert_url,product_url,description,is_sample,sample_person_id,sample_person_name,reasons,is_valid,opt_id,opt_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
			params[i] = new String[]{DBFacade.getInstance().getID(),bean.getSampleId(),list.get(i-1).getEnterpriseId(),list.get(i-1).getProductId(),list.get(i-1).getCertUrl(),list.get(i-1).getProductUrl(),list.get(i-1).getDescription(),list.get(i-1).getIsSample(),list.get(i-1).getSamplePersonId(),list.get(i-1).getSamplePersonName(),list.get(i-1).getReasons(),"1",bean.getOptId()};
		}
		DBFacade.getInstance().execute(sqls, params);
	}
	
	public void addJdSampleSignBill(JdSampleBill bean) throws Exception {
		if(("").equals(bean.getProductId())||bean.getProductId()==null||("undefined").equals(bean.getProductId())){
			JdProductInfo bean1 = new JdProductInfo();
			JdProductInfoImpl impl1 = new JdProductInfoImpl();
			bean1.setTypeId(bean.getTypeId());
			bean1.setProductId(DBFacade.getInstance().getID());
			bean.setProductId(bean1.getProductId());
			bean1.setProductName(bean.getProductName());
			bean1.setEnterpriseId(bean.getEpId());
			bean1.setShortName(CnToPiny.getInstance().getPinYinHeadChar(bean.getProductName()));
			bean1.setProductStandard(bean.getProductStandard());
			bean1.setProductModel(bean.getProductModel());
			bean1.setTradeMark(bean.getTradeMark());
			bean1.setOptId(bean.getOptId());
			bean1.setOptName(bean.getOptName());
			impl1.addBillJdProductInfo(bean1);
		}
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		String billId = DBFacade.getInstance().getID();
		sqls[0] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[0] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"抽样单信息添加","抽查任务ID：" + bean.getTaskId() + ";抽样单ID："+ billId, "1" };
		
		sqls[1]="insert into jd_sample_bill(bill_id,task_id,sample_id,sample_num,ssim_title,ep_id,comp_id,product_id,product_name,trade_mark,product_model," +
				"product_date,product_batch,sample_qty,sample_base,sample_date,batch_qty,sample_state,product_standard,product_level,save_place," +
				"post_place,is_export,sample_place,description,is_overtime,is_valid,opt_id,opt_name,opt_time,is_submit,check_status,is_inspect) " +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'0','1',?,?,now(),'0','0','0')";
		params[1] = new String[]{billId,bean.getTaskId(),bean.getSampleId(),bean.getSampleNum(),bean.getSsimTitle(),bean.getEpId(),bean.getCompId(),bean.getProductId(),bean.getProductName(),
				bean.getTradeMark(),bean.getProductModel(),bean.getProductDate(),bean.getProductBatch(),bean.getSampleQty(),bean.getSampleBase(),bean.getSampleDate(),
				bean.getBatchQty(),bean.getSampleState(),bean.getProductStandard(),bean.getProductLevel(),bean.getSavePlace(),bean.getPostPlace(),bean.getIsExport(),
				bean.getSamplePlace(),bean.getDescription(),bean.getOptId(),bean.getOptName()};
		
		DBFacade.getInstance().execute(sqls, params);
	}
	/**
	 * *******************
	* @param bean
	* @throws Exception
	* 2017-6-12
	* 修改抽样单信息
	 */
	public void JdSampleBillEdit(JdSampleBill bean) throws Exception {
		String[] sqls = new String[2];
		String[][] params = new String[2][];
		
		sqls[0] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) values(?,?,?,?,now(),?,?)";
		params[0] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"抽样单信息修改","抽样单ID：" + bean.getBillId() + ";抽样单编号："+ bean.getSampleNum(), "1" };
		
		sqls[1]="update jd_sample_bill set inspect_time=?,sample_num=?,ep_id=?,ep_name=?,ep_corp=?,ep_address=?,ep_postcode=?,ep_linkman=?,ep_tel=?,scdw_id=?,scdw_name=?,scdw_address=?," +
				"scdw_legal=?,scdw_linkman=?,scdw_tel=?,scdw_post_code=?,scdw_licence=?,scdw_code=?,scdw_etype=?,scdw_people=?,scdw_output=?,scdw_yield=?," +
				"gyxk_num=?,qsxk_num=?,ccxk_num=?,qtxk_num=?,product_id=?,product_name=?,trade_mark=?,product_model=?,product_date=?,product_batch=?,sample_qty=?," +
				"sample_base=?,batch_qty=?,backup_qty=?,sample_date=?,sample_state=?,product_standard=?,product_level=?,bak_place=?," +
				"is_export=?,sample_place=?,arrive_date=?,seal_person=?,remark=?,ep_seal=?,ep_seal_date=?,scdw_seal=?,scdw_seal_date=?," +
				"opt_id=?,opt_name=?,opt_time=now() where bill_id=?";
		params[1] = new String[]{bean.getInspectTime(),bean.getSampleNum(),bean.getEpId(),bean.getEpName(),bean.getEpCorp(),bean.getEpAddress(),bean.getEpPostcode(),bean.getEpLinkman(),bean.getEpTel(),
				bean.getScdwId(),bean.getScdwName(),bean.getScdwAddress(),bean.getScdwLegal(),bean.getScdwLinkman(),bean.getScdwTel(),bean.getScdwPostcode(),
				bean.getScdwLicence(),bean.getScdwCode(),bean.getScdwEtype(),bean.getScdwPeople(),bean.getScdwOutput(),bean.getScdwYield(),bean.getGyxkNum(),
				bean.getQsxkNum(),bean.getCcxkNum(),bean.getQtxkNum(),bean.getProductId(),bean.getProductName(),bean.getTradeMark(),bean.getProductModel(),
				bean.getProductDate(),bean.getProductBatch(),bean.getSampleQty(),bean.getSampleBase(),bean.getBatchQty(),bean.getBackupQty(),bean.getSampleDate(),
				bean.getSampleState(),bean.getProductStandard(),bean.getProductLevel(),bean.getBakPlace(),
				bean.getIsExport(),bean.getSamplePlace(),bean.getArriveDate(),bean.getSealPerson(),bean.getRemark(),bean.getEpSeal(),bean.getEpSealDate(),
				bean.getScdwSeal(),bean.getScdwSealDate(),bean.getOptId(),bean.getOptName(),bean.getBillId()};
		try{
			DBFacade.getInstance().execute(sqls, params);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * *******************
	* @param sampleId
	* @return
	* @throws Exception
	* 2017-6-12
	* 获取抽样任务信息
	 */
	public Map<String, Object> getJdSampleSignById(String sampleId) throws Exception {
		String sql = "select a.sample_id,a.sample_title,a.task_id,a.select_comp_id,a.select_comp_name,b.task_name,a.handle_dept_id,a.handle_dept_name," +
				"a.select_area,a.select_enterprise,a.sign_status,a.sign_desc,"+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time,a.sign_opt_id," +
				"a.sign_opt_name,a.opt_id,a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.product_id,b.comp_id,b.comp_name,b.task_type "+
                 "from jd_sample a,jd_plan_task b where a.task_id=b.task_id and a.sample_id=?";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			String sql1 = "select comp_address,phone,mail_code,fax,charge from hr_company where company_id=?";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] { bean.getHandleDeptId() });
			if(crs1.next()){
				bean.setCompAddress(crs1.getString("comp_address"));
				bean.setPhone(crs1.getString("phone"));
				bean.setMailCode(crs1.getString("mail_code"));
				bean.setFax(crs1.getString("fax"));
				bean.setCharge(crs1.getString("charge"));
			}
			bean.setSelectArea(crs.getString("select_area"));
			//解析区域
			//JSon转成字符串
			String areaId = "";
			String area = "";
			JSONArray jArray = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				areaId+= objs.get("id")+",";
				area+= objs.get("name")+",";
			}
			areaId = areaId.length()==0?areaId:areaId.substring(0, areaId.length()-1);
			area = area.length()==0?area:area.substring(0, area.length()-1);
			bean.setAreaId(areaId);
			bean.setAreaName(area);
			
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			//解析企业
			//JSon转成字符串
			String compId = "";
			String comp = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				compId+= objs.get("id")+",";
				comp+= objs.get("name")+",";
			}
			compId = compId.length()==0?compId:compId.substring(0, compId.length()-1);
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			bean.setEnterpriseId(compId);
			bean.setEnterpriseName(comp);
			
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setTypeId(crs.getString("product_id"));
			bean.setCompId(crs.getString("comp_id"));
			bean.setCompName(crs.getString("comp_name"));
			bean.setTaskType(crs.getString("task_type"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	public Map<String, Object> getJdSampleSignByIdone(String sampleId) throws Exception {
		String sql = "select a.sample_id,a.sample_title,a.task_id,a.select_comp_id,a.select_comp_name,b.task_name,a.handle_dept_id,a.handle_dept_name," +
					"a.select_area,a.select_enterprise,a.sign_status,a.sign_desc,"+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time,a.sign_opt_id," +
					"a.sign_opt_name,a.opt_id,a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.product_id from jd_sample a,jd_plan_task b "+
	                 "where a.task_id=b.task_id and a.sample_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSample bean = new JdSample();
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectArea(crs.getString("select_area"));
			//解析区域
			//JSon转成字符串
			String areaId = "";
			String area = "";
			JSONArray jArray = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				areaId+= objs.get("id")+",";
				area+= objs.get("name")+",";
			}
			areaId = areaId.length()==0?areaId:areaId.substring(0, areaId.length()-1);
			area = area.length()==0?area:area.substring(0, area.length()-1);
			bean.setAreaId(areaId);
			bean.setAreaName(area);
			
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			//解析企业
			//JSon转成字符串
			String compId = "";
			String comp = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				compId+= objs.get("id")+",";
				comp+= objs.get("name")+",";
			}
			compId = compId.length()==0?compId:compId.substring(0, compId.length()-1);
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			bean.setEnterpriseId(compId);
			bean.setEnterpriseName(comp);
			
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			
			String sql1 = "select a.bious_id,a.sample_id,a.ep_id,a.product_id,b.enterprise_name,c.product_name,a.cert_url,a.product_url,a.description,a.remark," +
					"a.is_sample,a.sample_person_id,a.sample_person_name,a.reasons,a.is_valid,a.opt_id,a.opt_time"+
					" from jd_sample_base a left join jd_enterprise_product c on a.product_id = c.product_id,jd_enterprise_info b " +
					" where a.ep_id=b.enterprise_id and sample_id = ?";
			ArrayList<JdSampleBase> list = new ArrayList<JdSampleBase>();
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] {sampleId});
		  	while (crs1.next()) {
		  		JdSampleBase bean1 = new JdSampleBase();
				bean1.setBiousId(crs1.getString("bious_id"));
				bean1.setSampleId(crs1.getString("sample_id"));
				bean1.setEnterpriseId(crs1.getString("ep_id"));
				bean1.setProductId(crs1.getString("product_id"));
				bean1.setEnterpriseName(crs1.getString("enterprise_name"));
				bean1.setProductName(crs1.getString("product_name"));
				bean1.setCertUrl(crs1.getString("cert_url"));
				bean1.setProductUrl(crs1.getString("product_url"));
				bean1.setDescription(crs1.getString("description"));
				bean1.setRemark(crs1.getString("remark"));
				bean1.setIsSample(crs1.getString("is_sample"));
				bean1.setSamplePersonId(crs1.getString("sample_person_id"));
				bean1.setSamplePersonName(crs1.getString("sample_person_name"));
				bean1.setReasons(crs1.getString("reasons"));
				bean1.setIsValid(crs1.getString("is_valid"));
				bean1.setOptId(crs1.getString("opt_id"));
				bean1.setOptTime(crs1.getString("opt_time"));
				list.add(bean1);
			}
			 bean.setSpList(list);
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	/**
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	//dao
	public Map<String, Object> getJdSampleBillById(JdSampleBill bean1)  {
		String sql = "select a.inspect_time,a.bill_id,a.sample_id,a.task_id,a.sample_num,a.comp_id,a.ep_id,a.ep_name,a.ep_corp,a.ep_address,a.ep_postcode,a.ep_linkman,a.ep_tel," +
				"a.scdw_id,a.scdw_name,a.scdw_address,a.scdw_legal,a.scdw_linkman,a.scdw_tel,a.scdw_post_code,a.scdw_licence,a.scdw_code,a.scdw_etype," +
				"a.scdw_people,a.scdw_output,a.scdw_yield,a.gyxk_num,a.qsxk_num,a.ccxk_num,a.qtxk_num,a.cert_num,a.product_id,a.product_name,a.trade_mark," +
				"a.product_model,a.product_date,a.product_batch,a.sample_qty,a.sample_base,a.batch_qty,a.backup_qty,a.sample_date,a.sample_state,a.product_standard," +
				"a.product_level,a.bak_place,a.save_place,a.post_place,a.post_end_date,a.is_export,a.sample_place,a.arrive_date,a.seal_person,a.remark," +
				"a.ep_seal,a.ep_seal_date,a.scdw_seal,a.scdw_seal_date,a.remark,a.is_submit,a.check_status from jd_sample_bill a where a.bill_id=? ";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { bean1.getBillId() });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			if (crs.next()) {
				map.put("msg", "true");
				JdSampleBill bean = new JdSampleBill();
				bean.setInspectTime(crs.getString("inspect_time"));
				bean.setBillId(crs.getString("bill_id"));
				bean.setSampleId(crs.getString("sample_id"));
				bean.setTaskId(crs.getString("task_id"));
				bean.setSampleNum(crs.getString("sample_num"));
				bean.setCompId(crs.getString("comp_id"));
				bean.setEpId(crs.getString("ep_id"));
				bean.setEpName(crs.getString("ep_name"));
				bean.setEpCorp(crs.getString("ep_corp"));
				bean.setEpAddress(crs.getString("ep_address"));
				bean.setEpPostcode(crs.getString("ep_postcode"));
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
				
				map.put("record", bean);
			} else {
				map.put("msg", "noresult");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public Map<String, Object> getproInfoById(String productId) throws Exception {
		String sql = "select product_id,product_name,product_standard,product_model,trade_mark from jd_enterprise_product where product_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { productId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSampleBill bean = new JdSampleBill();
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setProductStandard(crs.getString("product_standard"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setTradeMark(crs.getString("trade_mark"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	public JSONArray getTaskList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select task_id ,task_name from jd_plan_task";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("task_id"));
				bean.put("name", crs.getString("task_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public JSONArray getSampleList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select sample_id ,sample_title from jd_sample where sign_status=1";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("sample_id"));
				bean.put("name", crs.getString("sample_title"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	
	public JSONArray getCase1List() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select case_id ,plan_name from jd_plan_case";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("case_id"));
				bean.put("name", crs.getString("plan_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public JSONArray getUserNameList(String CompId) {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select serial_id ,user_name from hr_staff where company_id='"+CompId+"'";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("serial_id"));
				bean.put("name", crs.getString("user_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public JSONArray getEpList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select enterprise_id ,enterprise_name from jd_enterprise_info";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("enterprise_id"));
				bean.put("name", crs.getString("enterprise_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	public JSONArray getProList() {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select product_id ,product_name from jd_enterprise_product";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("product_id"));
				bean.put("name", crs.getString("product_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	

	public JSONArray getcompList(String sampleId) {
		JSONArray json = null;
		try {
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			String sql = "select product_id ,product_name from jd_enterprise_product";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql.toString(), null);
			while (crs.next()) {
				HashMap<String, String> bean = new HashMap<String, String>();
				bean.put("id", crs.getString("product_id"));
				bean.put("name", crs.getString("product_name"));
				list.add(bean);
			}
			json = JSONArray.fromObject(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public Map<String, Object> delJdSampleBase(JdSample bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_sample_base where bious_id=?";
		params[0] = new String[] {bean.getBiousId()};

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务下达信息删除",
				"抽样基本ID：" + bean.getBiousId(), "1" };
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	public Map<String, Object> delJdSampleBill(JdSampleBill bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[2];
		String[][] params = new String[2][];

		sqls[0] = "delete from jd_sample_bill where bill_id=?";
		params[0] = new String[] { bean.getBillId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样单信息删除",
				"抽样单ID：" + bean.getBillId(), "1" };
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(), bean
				.getOptId(), bean.getOptName(), "抽样单信息删除", "{{delOptRemark}}");
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	public void changeStatus(JdSample bean) throws ParseException, SQLException{
		String sql = "select tran_id from dh_tranlist where pre_tran_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{bean.getTranId()});
		bean.setPreTranId(bean.getTranId());//上一步骤的tranId
		if(crs.next()){
			bean.setTranId(crs.getString("tran_id"));//当前步骤的tranId
		}
		String[] sqls = null;
		String[][] params = null;
		if("020301".equals(bean.getNextStepId())){		
			sqls = new String[3];
			params = new String[3][];
			sqls[0] = "update jd_sample set current_status=2,current_node_id=?,tran_id=?,sign_status='1',sign_desc=?,sign_opt_id=?,sign_opt_name=?,sign_opt_time=now() where sample_id=?";
			params[0] = new String[] {FlowConstant.CY_NODE_ARR_ID[1],bean.getTranId(),bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getSampleId()};
			sqls[2] = "update dh_tranlist set deal_view=?,work_status='0',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1 where tran_id=?";
			params[2] = new String[]{bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
		}
		if("020101".equals(bean.getNextStepId())){//不通过
			sqls = new String[4];
			params = new String[4][];
			sqls[0] = "update jd_sample set current_status='2',current_node_id=?,tran_id=?,sign_status='2',is_handle=0,sign_desc=?,sign_opt_id=?,sign_opt_name=?,is_handle='0',sign_opt_time=now() where sample_id=?";
			params[0] = new String[] {FlowConstant.CY_NODE_ARR_ID[1],bean.getTranId(),bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getSampleId()};
			sqls[2] = "update dh_tranlist set back_type='3',deal_view=?,work_status='1',deal_status='1',deal_opt_id = ?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1 where tran_id=?";
			params[2] = new String[]{bean.getSignDesc(),bean.getOptId(),bean.getOptName(),bean.getTranId()};
			//修改所有留痕的work——status=1
			sqls[3] = "update dh_tranlist set work_status='1' where tran_id = ?";
			params[3] = new String[]{bean.getPreTranId()};
			
		}
		
		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {
				DBFacade.getInstance().getID(),
				bean.getOptId(),
				bean.getOptName(),
				"抽样任务签收",
				"抽样任务ID：" + bean.getSampleId(), "1" };
		
		
		DBFacade.getInstance().execute(sqls, params);
		
	}
	
	public List<ZTreeBean> getSampleTreeCheck(String area) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select area_id,area_name,parent_id from jd_area_info where area_id like '"
				+ area + "%' order by area_id";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			int i = 0 ;
			while (crs.next()) {
				i++;
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("area_id"));
				bean.setName(crs.getString("area_name"));
				bean.setpId(crs.getString("parent_id"));
				bean.setNocheck(false);
				if (i == 1) {
					bean.setOpen(true);
				} else {
					bean.setOpen(false);
				}
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public boolean getSampleBillName(String SsimTitle) throws Exception{
		try {
			String sql = "select ssim_title from jd_sample_bill where ssim_title = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { SsimTitle });
            
			while (crs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public Map<String, Object> submitJdSampleBill(JdSampleBill bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[4];
		String[][] params = new String[4][];
		String tranId = DBFacade.getInstance().getID();
		sqls[0] = "update jd_sample_bill set current_status=1,tran_id=?,current_node_id=?,is_submit='1',submit_opt_time=now(),check_opt_id=?,check_opt_name=?,check_dept_id=?,check_dept_name=?,check_status='1' where bill_id=?";
		params[0] = new String[] { tranId,FlowConstant.CJ_NODE_ARR_ID[0],bean.getNextDealId(),bean.getNextDealName(),bean.getNextDeptId(),bean.getNextDeptName(),bean.getBillId() };

		sqls[1] = "insert into opt_log(log_id,opt_id,opt_name,opt_module,opt_time,remark,is_valid) "
				+ "values(?,?,?,?,now(),?,?)";
		params[1] = new String[] {DBFacade.getInstance().getID(),bean.getOptId(),bean.getOptName(),
				"抽样单信息提交","抽样单ID：" + bean.getBillId(), "1" };
		
		bean.setNextDept("[{'id':'"+bean.getNextDeptId()+"',"+"'name':'"+bean.getNextDeptName()+"'}]");
		bean.setNextPerson("[{'id':'"+bean.getNextDealId()+"',"+"'name':'"+bean.getNextDealName()+"'}]");
		
		sqls[2] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_opt_time,deal_time,over_status,deal_status,work_status,opt_order,"
				+ " next_dept,next_person,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','抽样单录入',?,?,?,?,now(),?,?,?,?,now(),'0','0','1','0','1',?,?,'1','1')";
		params[2] = new String[] {tranId,bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[0],FlowConstant.CJ_STEP_ARR_ID[0],bean.getBillId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(), bean.getOptName(), bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(), bean.getOptName(),bean.getNextDept(),bean.getNextPerson()};
		//下一操作留痕
		sqls[3] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','抽样单审核',?,?,?,?,now(),?,?,?,?,'0','0','0','0','2',?,'1','1')";
		params[3] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[1],FlowConstant.CJ_STEP_ARR_ID[1],bean.getBillId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getNextDeptId(),bean.getNextDeptName(),
				bean.getNextDealId(),bean.getNextDealName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	/*查询抽样单编号是否重复*/
	public boolean sampleNumCheck(String sampleNum,String billId) throws Exception{
		String sql = "select count(*) from jd_sample_bill where sample_num = ? and bill_id != ?";
		try{
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql, new String[] {sampleNum,billId});
			if(obj>0){
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public List<ZTreeBean> getJdProductTree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql = "select product_id,product_name from jd_enterprise_product where 1 = '1' order by product_id";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("product_id"));
				bean.setName(crs.getString("product_name"));
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		if (treeList.isEmpty()) {
			ZTreeBean bean = new ZTreeBean();
			bean.setId("-1");
			bean.setName("无产品信息");
			bean.setpId("-1");
			treeList.add(bean);
		}
		return treeList;
	}
	
	public String getProductMap(JdSample bean1, String curPage, String perNumber,
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
		String sql = "select a.product_id,b.product_name from jd_sample_base a,jd_enterprise_product b where a.product_id=b.product_id and a.sample_id='"+bean1.getSampleId()+"' and a.ep_id = '"+bean1.getSelectEnterprise()+"'";
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
			page.setSidx("a.product_id");
			page.setSord("desc");
		}
		
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSampleBase> list = new ArrayList<JdSampleBase>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSampleBase bean = new JdSampleBase();
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
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

	public String getSampleMap(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,String OptId,String CompanyId) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select sample_id,sample_title from jd_sample where 1=1 "+
				 " and (case when sign_opt_id is null or sign_opt_id='' then LOCATE(select_comp_id,'"+CompanyId+"')>0 else sign_opt_id LIKE '%"+OptId+"%'  end)"+
				 "and sign_status = '1'";
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
			page.setSidx("sample_id");
			page.setSord("desc");
		}
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSample bean = new JdSample();
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
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
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-3-24
	* 获取通过审核的抽样单信息
	*/
	public String getJdSampleBillList2(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue,JdSampleBill bean2) throws Exception {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select a.bill_id,a.ssim_title,a.sample_num,a.sample_no,a.product_name,a.product_model,a.product_id,a.is_valid,a.task_id,b.product_id type_id,c.inspet_dept_id,c.inspet_dept_name,d.inspect_end_date "+
				" from jd_sample_bill a,jd_plan_task b,jd_plan_handle c,jd_plan_case d where a.check_status = 3 and a.is_inspect = 0 and a.task_id=b.task_id and a.task_id = c.task_id and b.task_id=d.task_id and c.inspet_dept_id like '%"+bean2.getCompanyId()+"%'";
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
			page.setSidx("bill_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdSampleBill> list = new ArrayList<JdSampleBill>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdSampleBill bean = new JdSampleBill();
			bean.setBillId(crs.getString("bill_id"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setSampleNum(crs.getString("sample_num"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTypeId(crs.getString("type_id"));
			bean.setProductId(crs.getString("product_id"));
			bean.setProductName(crs.getString("product_name"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setEndTime(crs.getString("inspect_end_date"));
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
	public Map<String, Object> getJdSampleById(String sampleId) throws Exception {
		String sql = "select a.tran_id,a.task_id,b.task_name,a.sample_title,a.handle_dept_id,a.handle_dept_name,a.select_end_date,a.select_area,"+
                "a.select_enterprise,a.select_comp_id,a.select_comp_name,a.select_opt_id,a.select_opt_name,a.opt_id,a.opt_name," +
                ""+TimeUtil.getTimeShow("a.opt_time")+" opt_time,a.is_handle,"+TimeUtil.getTimeShow("a.handle_opt_time")+" handle_opt_time,a.sign_status,a.sign_desc,a.sign_opt_id,a.sign_opt_name,"+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time " +
                "from jd_sample a,jd_plan_task b where a.task_id=b.task_id and a.sample_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSample bean = new JdSample();
			bean.setTranId(crs.getString("tran_id"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			bean.setHandleDeptName(crs.getString("handle_dept_name"));
			bean.setSelectEndDate(crs.getString("select_end_date"));
			bean.setSelectArea(crs.getString("select_area"));
			//解析区域
			//JSon转成字符串
			String areaId = "";
			String area = "";
			JSONArray jArray = JSONArray.fromObject((crs.getString("select_area")==null || 
					crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
			for(Object obj : jArray){
				Map objs = JSONObject.fromObject(obj);
				areaId+= objs.get("id")+",";
				area+= objs.get("name")+",";
			}
			areaId = areaId.length()==0?areaId:areaId.substring(0, areaId.length()-1);
			area = area.length()==0?area:area.substring(0, area.length()-1);
			bean.setAreaId(areaId);
			bean.setAreaName(area);
			
			bean.setSelectEnterprise(crs.getString("select_enterprise"));
			//解析企业
			//JSon转成字符串
			String compId = "";
			String comp = "";
			JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
					crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
			for(Object obj : jArray2){
				Map objs = JSONObject.fromObject(obj);
				compId+= objs.get("id")+",";
				comp+= objs.get("name")+",";
			}
			compId = compId.length()==0?compId:compId.substring(0, compId.length()-1);
			comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
			bean.setEnterpriseId(compId);
			bean.setEnterpriseName(comp);
			
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setSelectOptId(crs.getString("select_opt_id"));
			bean.setSelectOptName(crs.getString("select_opt_name"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setIsHandle(crs.getString("is_handle"));
			bean.setHandleOptTime(crs.getString("handle_opt_time"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setSignDesc(crs.getString("sign_desc"));
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}

	public Map<String, Object> getJdPlanCaseById(String sampleId) {
		String sql = "select a.case_id,a.task_id,a.plan_name,a.product_standard,a.carry_content,a.train_content," +
				"a.select_person,a.select_area,a.select_enterprise,a.select_end_date,a.inspect_end_date,a.aptitude_content," +
				"a.inspest_base,a.judge_base,a.remark,a.is_valid,a.handle_dept_id,a.handle_dept_name," +
				"a.opt_id,a.opt_name,a.opt_time,a.attact_path,a.is_submit,a.submit_opt_time,a.check_status,a.check_desc," +
				"a.check_opt_id,a.check_opt_name,a.check_opt_time,a.is_handle,a.handle_opt_time,b.task_name " +
				"from jd_plan_case a left join jd_plan_task b on a.task_id = b.task_id where a.case_id = " +
				"(select f3.case_id from jd_plan_case f3 where f3.task_id = (select f1.task_id from jd_sample f1 where f1.sample_id=?))";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			if (crs.next()) {
				map.put("msg", "true");
				JdPlanCase jdPlanCase = new JdPlanCase();
				jdPlanCase.setCaseId(crs.getString("case_id"));
				jdPlanCase.setTaskId(crs.getString("task_id"));
				jdPlanCase.setPlanName(crs.getString("plan_name"));
				jdPlanCase.setStandardId(crs.getString("product_standard"));
				//解析标准
				//JSon转成字符串
				String standard = "";
				JSONArray jArray0 = JSONArray.fromObject((crs.getString("product_standard")==null || 
						crs.getString("product_standard").trim().equals(""))?"[]":crs.getString("product_standard"));
				for(Object obj : jArray0){
					Map objs = JSONObject.fromObject(obj);
					standard+= objs.get("name")+",";
				}
				standard = standard.length()==0?standard:standard.substring(0, standard.length()-1);
				jdPlanCase.setProductStandard(standard);
				
				jdPlanCase.setCarryContent(crs.getString("carry_content"));
				jdPlanCase.setTrainContent(crs.getString("train_content"));
				jdPlanCase.setSelectPersonId(crs.getString("select_person"));
				//解析抽样人员
				//JSon转成字符串
				String person = "";
				JSONArray jArray1 = JSONArray.fromObject((crs.getString("select_person")==null || 
						crs.getString("select_person").trim().equals(""))?"[]":crs.getString("select_person"));
				for(Object obj : jArray1){
					Map objs = JSONObject.fromObject(obj);
					person+= objs.get("name")+",";
				}
				person = person.length()==0?person:person.substring(0, person.length()-1);
				jdPlanCase.setSelectPerson(person);
				
				jdPlanCase.setSelectAreaId(crs.getString("select_area"));
				//解析抽查区域
				//JSon转成字符串
				String area = "";
				JSONArray jArray2 = JSONArray.fromObject((crs.getString("select_area")==null || 
						crs.getString("select_area").trim().equals(""))?"[]":crs.getString("select_area"));
				for(Object obj : jArray2){
					Map objs = JSONObject.fromObject(obj);
					area+= objs.get("name")+",";
				}
				area = area.length()==0?area:area.substring(0, area.length()-1);
				jdPlanCase.setSelectArea(area);
				
				jdPlanCase.setSelectEnterpriseId(crs.getString("select_enterprise"));
				//解析抽查企业
				//JSon转成字符串
				String comp = "";
				JSONArray jArray3 = JSONArray.fromObject((crs.getString("select_enterprise")==null || 
						crs.getString("select_enterprise").trim().equals(""))?"[]":crs.getString("select_enterprise"));
				for(Object obj : jArray3){
					Map objs = JSONObject.fromObject(obj);
					comp+= objs.get("name")+",";
				}
				
				comp = comp.length()==0?comp:comp.substring(0, comp.length()-1);
				jdPlanCase.setSelectEnterprise(comp);
				
				jdPlanCase.setSelectEndDate(crs.getString("select_end_date"));
				jdPlanCase.setInspectEndDate(crs.getString("inspect_end_date"));
				jdPlanCase.setAptitudeContent(crs.getString("aptitude_content"));
				jdPlanCase.setInspestBase(crs.getString("inspest_base"));
				jdPlanCase.setJudgeBase(crs.getString("judge_base"));
				jdPlanCase.setRemark(crs.getString("remark"));
				jdPlanCase.setIsValid(crs.getString("is_valid"));
				jdPlanCase.setCheckStatus(crs.getString("check_status"));
				jdPlanCase.setHandleDeptId(crs.getString("handle_dept_id"));
				jdPlanCase.setHandleDeptName(crs.getString("handle_dept_name"));
				jdPlanCase.setOptId(crs.getString("opt_id"));
				jdPlanCase.setOptName(crs.getString("opt_name"));
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String optTime = crs.getString("opt_time");
				String time = formatter.format(formatter
						.parse(optTime));
				jdPlanCase.setOptTime(time);
				jdPlanCase.setAttactPath(crs.getString("attact_path"));
				jdPlanCase.setIsSubmit(crs.getString("is_submit"));
				jdPlanCase.setSubmitOptTime(crs.getString("submit_opt_time"));
				jdPlanCase.setCheckStatus(crs.getString("check_status"));
				jdPlanCase.setCheckDesc(crs.getString("check_desc"));
				jdPlanCase.setCheckOptId(crs.getString("check_opt_id"));
				jdPlanCase.setCheckOptName(crs.getString("check_opt_name"));
				String optTime1 = crs.getString("check_opt_time");
				if(optTime1 == null){
					jdPlanCase.setCheckOptTime(crs.getString("check_opt_time"));
				}else{
					String time1 = formatter.format(formatter
							.parse(optTime1));
					jdPlanCase.setCheckOptTime(time1);
				}
				jdPlanCase.setIsHandle(crs.getString("is_handle"));

				String optTime2 = crs.getString("handle_opt_time");
				if(optTime2 == null){
					jdPlanCase.setHandleOptTime(crs.getString("handle_opt_time"));
				}else{
					String time2 = formatter.format(formatter
							.parse(optTime2));
					jdPlanCase.setHandleOptTime(time2);
				}
				jdPlanCase.setTaskName(crs.getString("task_name"));
				map.put("record", jdPlanCase);
			} else {
				map.put("msg", "noresult");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		/*
		String sql2 = "select b.standard_name,c.item_id,c.standard_id,c.item_name,c.second_name,c.third_name,c.max_value,c.min_value,c.standard_value,"
				+ "c.meter_unit,c.shape_material,c.grade_model from jd_product_standard b,jd_plan_item c where b.standard_id = c.standard_id and c.case_id=?";
		
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
				new String[] {caseId });
		List<JdPlanCase> list = new ArrayList<JdPlanCase>();
		try{
			while(crs2.next()){
				JdPlanCase bean2 = new JdPlanCase();
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
				bean2.setShapeMaterial(crs2.getString("shape_material"));
				bean2.setGradeModel(crs2.getString("grade_model"));
				list.add(bean2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		map.put("items", list);
		*/
		return map;
	}

	/** *******************
	* @param bean
	* @return
	* 2017-5-22
	* 直接回退时提交
	 * @throws SQLException 
	*/
	public Map<String, Object> submitZJ(JdSampleBill bean) throws SQLException {
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
			params[2] = new String[] { DBFacade.getInstance().getID(),crs.getString("work_id"),crs.getString("flow_id"),
					crs.getString("node_id"),crs.getString("step_id"),crs.getString("detail_id"),crs.getString("belong_work"),crs.getString("tran_title"),crs.getString("create_dept_id"),
					crs.getString("create_dept_name"),crs.getString("create_opt_id"),crs.getString("create_opt_name"),crs.getString("deal_part_id"),crs.getString("deal_part_name"),
					crs.getString("deal_opt_id"),crs.getString("deal_opt_name"),crs.getString("opt_order"),crs.getString("next_dept"),crs.getString("next_person"),crs.getString("pre_tran_id")};
		}
		
		/*修改检验数据审核的状态，使得能再次审核*/
		if(preTranId[2].equals("5")){
			sqls[3] = "update jd_inspect set check_status='1' where inspect_id=?";
			params[3] = new String[] {preTranId[3]};
		}
		//检验报告再次审核
		if(preTranId[2].equals("7")){
			sqls[3] = "update jd_inspect_report set check_status='1' where report_id=?";
			params[3] = new String[] {preTranId[3]};
		}
		/*修改检验报告签发状态，使得能再次签发*/
		if(preTranId[2].equals("8")){
			sqls[3] = "update jd_inspect_report set issue_status='0' where report_id=?";
			params[3] = new String[] {preTranId[3]};
		}
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}
	
	/** *******************
	* @param tranId
	* @return
	* 2017-5-22
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

	/** *******************
	* @param epId
	* @param sampleId
	* @return
	* 2017-6-13
	* 获取抽样单的照片路径
	*/
	public String getSampleBillPhoto(String epId, String sampleId) {
		String sql = "select product_url from jd_sample_base where sample_id=? and ep_id=? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { sampleId,epId});
		String productUrl = "";
		try {
			if (crs.next()) {
				productUrl = crs.getString("product_url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productUrl;
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
	* 2017-6-14
	* 查询抽样信息上传里的抽查产品
	*/
	public String getProductMap2(JdSample bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sql = "select product_id,product_name from jd_enterprise_product where 1=1 ";
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		if(bean1.getSelectEnterprise() != null &&  !"".equals(bean1.getSelectEnterprise())){
			sql = sql + " and enterprise_id = '"+bean1.getSelectEnterprise()+"'";
		}
		
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("product_id");
			page.setSord("desc");
		}
		
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSampleBase> list = new ArrayList<JdSampleBase>();
		CachedRowSetImpl crs = pageData.getCrs();
		try {
			while (crs.next()) {
				JdSampleBase bean = new JdSampleBase();
				bean.setProductId(crs.getString("product_id"));
				bean.setProductName(crs.getString("product_name"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @return
	* 2017-6-22
	* 上传抽样基础信息时选择抽样人员
	*/
	public String getSampleUser(JdSample bean1, String curPage,
			String perNumber, String orderByField, String orderBySort,
			String searchField, String searchValue) {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		String sampleUser = "";
		String sql0 = "select select_opt_id from jd_sample where sample_id=?";
		CachedRowSetImpl crs0 = DBFacade.getInstance().getRowSet(sql0,new String[] { bean1.getSampleId()});
		try {
			if(crs0.next()){
				sampleUser = crs0.getString("select_opt_id");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "select serial_id,user_name from hr_staff where serial_id in ("+sampleUser+") ";
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
			page.setSidx("serial_id");
			page.setSord("desc");
		}
		
		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdSample> list = new ArrayList<JdSample>();
		CachedRowSetImpl crs = pageData.getCrs();
		try {
			while (crs.next()) {
				JdSample bean = new JdSample();
				bean.setSerialId(crs.getString("serial_id"));
				bean.setUserName(crs.getString("user_name"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
