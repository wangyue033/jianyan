/**
 * 
 */
package com.dhcc.inspect.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdProductInfo;
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.domain.JdSampleBill;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.utils.CnToPiny;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-5-15    
 ***********************
 */
/**
 * @author wangxiaopiao
 *
 */
public class JdSampleBillEntryImpl {
	Page page = new Page();
	/** 抽样任务列表查询
	* @param curPage
	* @param perNumber
	* @param orderByField
	* @param orderByType
	* @param searchField
	* @param searchValue
	* @param compId
	* @return
	* 2017-5-15
	* *
	 * @throws SQLException ******************
	*/
	public String getJdSampleTaskList(String curPage, String perNumber,
			String orderByField, String orderBySort, String searchField,
			String searchValue, String compId,String companyId) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql = "select a.sample_id,a.sample_title,a.task_id,b.task_name," 
				  + ""+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time,a.select_comp_name,count(c.bill_id) bill_count " 
				  + "from jd_sample a left join jd_sample_bill c on a.sample_id = c.sample_id ,jd_plan_task b left join jd_plan_handle d on b.task_id=d.task_id " 
				  + "where a.task_id = b.task_id and a.sign_status = '1' "
				  + "and d.inspet_dept_id like '"+compId+"%' ";

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
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setBaseCount(crs.getString("bill_count"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
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
	/** 根据从列表传过来的sampleId获取抽样任务的其他信息
	* @param sampleId
	* @return
	* 2017-5-15
	* *
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getJdSampleSignById(String sampleId) throws SQLException {
		String sql = "select b.comp_name,b.task_type,a.sample_id,a.sample_title,a.task_id,a.select_comp_id,a.select_comp_name,b.task_name,a.handle_dept_id,a.handle_dept_name," +
				"a.select_area,a.select_enterprise,a.sign_status,a.sign_desc,"+TimeUtil.getTimeShow("a.sign_opt_time")+" sign_opt_time,a.sign_opt_id," +
				"a.sign_opt_name,a.opt_id,a.opt_name,"+TimeUtil.getTimeShow("a.opt_time")+" opt_time,b.product_id from jd_sample a,jd_plan_task b "+
                 "where a.task_id=b.task_id and a.sample_id=?";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { sampleId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			JdSample bean = new JdSample();
			bean.setTaskType(crs.getString("task_type"));
			bean.setCompName(crs.getString("comp_name"));
			bean.setSampleId(crs.getString("sample_id"));
			bean.setSampleTitle(crs.getString("sample_title"));
			bean.setTaskId(crs.getString("task_id"));
			bean.setTaskName(crs.getString("task_name"));
			bean.setSelectCompId(crs.getString("select_comp_id"));
			bean.setSelectCompName(crs.getString("select_comp_name"));
			bean.setHandleDeptId(crs.getString("handle_dept_id"));
			
			String sql1 = "select comp_address,phone,mail_code,fax,charge from hr_company where company_id=?";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,new String[] { bean.getHandleDeptId() });
			if(crs1.next()){
				bean.setCompAddress(crs1.getString("comp_address"));
				bean.setPhone(crs1.getString("phone"));
				bean.setMailCode(crs1.getString("mail_code"));
				bean.setFax(crs1.getString("fax"));
				bean.setCharge(crs1.getString("charge"));
			}
			
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
			bean.setSignOptId(crs.getString("sign_opt_id"));
			bean.setSignOptName(crs.getString("sign_opt_name"));
			bean.setSignOptTime(crs.getString("sign_opt_time"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setOptName(crs.getString("opt_name"));
			bean.setOptTime(crs.getString("opt_time"));
			bean.setTypeId(crs.getString("product_id"));
			
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	/** 添加抽样单信息
	* @param bean
	* 2017-5-15
	* *
	 * @throws Exception ******************
	*/
	public void addJdSampleBill(JdSampleBill bean,String copId) throws Exception {
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
		
//		sqls[1]="insert into jd_sample_bill(bill_id,task_id,sample_id,sample_num,ssim_title,ep_id,comp_id,product_id,product_name,trade_mark,product_model," +
//				"product_date,product_batch,sample_qty,sample_base,sample_date,batch_qty,sample_state,product_standard,product_level,save_place," +
//				"post_place,is_export,sample_place,description,is_overtime,is_valid,opt_id,opt_name,opt_time,is_submit,check_status,is_inspect) " +
//				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'0','1',?,?,now(),'0','0','0')";
//		params[1] = new String[]{billId,bean.getTaskId(),bean.getSampleId(),bean.getSampleNum(),bean.getSsimTitle(),bean.getEpId(),bean.getCompId(),bean.getProductId(),bean.getProductName(),
//				bean.getTradeMark(),bean.getProductModel(),bean.getProductDate(),bean.getProductBatch(),bean.getSampleQty(),bean.getSampleBase(),bean.getSampleDate(),
//				bean.getBatchQty(),bean.getSampleState(),bean.getProductStandard(),bean.getProductLevel(),bean.getSavePlace(),bean.getPostPlace(),bean.getIsExport(),
//				bean.getSamplePlace(),bean.getDescription(),bean.getOptId(),bean.getOptName()};
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy");
		bean.setSampleNo(getSampleThree(copId)+format.format(date)+getSampleNum(copId));
		bean.setReportNo(getCharAndNumr(15));
		
		sqls[1]="insert into jd_sample_bill(inspect_time,bill_id,task_id,sample_id,sample_num,ssim_title,comp_id,ep_id," +//7
				"ep_name,ep_corp,ep_address,ep_linkman,ep_tel," +//5
				"scdw_id,scdw_name,scdw_address,scdw_legal,scdw_linkman,scdw_tel," +//6
				"scdw_post_code,scdw_licence,scdw_code,scdw_etype,scdw_people,scdw_output,scdw_yield," +//7
				"gyxk_num,qsxk_num,ccxk_num,qtxk_num,sample_no,report_no," +//6
				"product_id,product_name,trade_mark,product_model," +//4
				"product_date,product_batch,sample_qty,sample_base,batch_qty," +//5
				"backup_qty,sample_date,sample_state,product_standard,product_level,bak_place," +//6
				"is_export,sample_place,arrive_date,seal_person," +//4
				"remark,ep_seal,ep_seal_date,scdw_seal,scdw_seal_date," +//5
				"is_overtime,is_valid,opt_id,opt_name,opt_time,is_submit,check_status,is_inspect,ep_org_code,ep_postcode) " +//4
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'0','1',?,?,now(),'0','0','0',?,?)";
		params[1] = new String[]{bean.getInspectTime(),billId,bean.getTaskId(),bean.getSampleId(),bean.getSampleNum(),bean.getSsimTitle(),bean.getCompId(),bean.getEpId(),//7
				bean.getEpName(),bean.getEpCorp(),bean.getEpAddress(),bean.getEpLinkman(),bean.getEpTel(),//5
				bean.getScdwId(),bean.getScdwName(),bean.getScdwAddress(),bean.getScdwLegal(),bean.getScdwLinkman(),bean.getScdwTel(),//6
				bean.getScdwPostcode(),bean.getScdwLicence(),bean.getScdwCode(),bean.getScdwEtype(),bean.getScdwPeople(),bean.getScdwOutput(),bean.getScdwYield(),//7
				bean.getGyxkNum(),bean.getQsxkNum(),bean.getCcxkNum(),bean.getQtxkNum(),bean.getSampleNo(),bean.getReportNo(),//6
				bean.getProductId(),bean.getProductName(),bean.getTradeMark(),bean.getProductModel(),//4
				bean.getProductDate(),bean.getProductBatch(),bean.getSampleQty(),bean.getSampleBase(),bean.getBatchQty(),//5
				bean.getBackupQty(),bean.getSampleDate(),bean.getSampleState(),bean.getProductStandard(),bean.getProductLevel(),bean.getBakPlace(),//6
				bean.getIsExport(),bean.getSamplePlace(),bean.getArriveDate(),bean.getSealPerson(),//4
				bean.getRemark(),bean.getEpSeal(),bean.getEpSealDate(),bean.getScdwSeal(),bean.getScdwSealDate(),//5
				bean.getOptId(),bean.getOptName(),bean .getEpOrgCode(),bean.getEpPostcode()};//2
		try{
			System.out.println(bean.getInspectTime());
			DBFacade.getInstance().execute(sqls, params);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/** 获取企业产品的基本信息
	* @param productId
	* @return
	* 2017-5-15
	* *
	 * @throws SQLException ******************
	*/
	public Map<String, Object> getproInfoById(String productId) throws SQLException {
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
	/**
	 * 获取受检单位的信息
	 * @param epId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getEpInfo(String enterpriseId) throws SQLException {
		String sql = "select economy_type,address,legal_rep,contacts,telephone,post_code,ep_credit_code,license_number,org_code,year_value from jd_enterprise_info where enterprise_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { enterpriseId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdSampleBill bean = new JdSampleBill();
			bean.setEconomyType(crs.getString("economy_type"));
			bean.setAddress(crs.getString("address"));
			bean.setLegalRep(crs.getString("legal_rep"));
			bean.setContacts(crs.getString("contacts"));
			bean.setTelephone(crs.getString("telephone"));
			bean.setPostCode(crs.getString("post_code"));
			bean.setEpCreditCode(crs.getString("ep_credit_code"));
			bean.setLicenseNumber(crs.getString("license_number"));
			bean.setOrgCode(crs.getString("org_code"));
			bean.setYearValue(crs.getString("year_value"));
			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}
		return map;
	}
	
	
	//生产数字加字母的编号的函数
	public String getCharAndNumr(int length){     
		String val = "";     
		Random random = new Random();     
		for(int i = 0; i < length; i++){     
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字     
			if("char".equalsIgnoreCase(charOrNum)) {// 字符串      
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
				val += (char) (choice + random.nextInt(26));     
			} else if("num".equalsIgnoreCase(charOrNum)){// 数字     
				val += String.valueOf(random.nextInt(10));     
			}     
		}     
		String sql = "select report_no FROM jd_sample_bill where report_no =?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{val});
		if(crs.size()>0){
			val = getCharAndNumr(length);
		}
		
		return val;
	}
	
	//获取机构简称充当样品编号前三位
	public String getSampleThree(String companyId) throws SQLException{
		String mark = "";
		String sql = "select short_name from hr_company where company_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {companyId });
		if(crs.next()){
			mark = crs.getString("short_name");
		}
		return mark;
	}

	//获取样品编号后四位顺序号
	public String getSampleNum(String companyId){
		String cId = "";
		String sql = "select count(*)+1 from jd_sample_bill where comp_id like '"+companyId+"%'";
		int item = Integer.parseInt(DBFacade.getInstance().getValueBySql(sql,null).toString());
		if (item < 10) {
			cId = "000" + item;
		} else if(item < 100){
			cId = "00" + item;
		}else if(item<1000){
			cId = "0" + item;
		}
		return cId;
	}

}
