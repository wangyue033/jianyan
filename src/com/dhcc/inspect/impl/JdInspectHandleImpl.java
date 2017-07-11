package com.dhcc.inspect.impl;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectHandle;
import com.dhcc.inspect.domain.JdPlanCase;
import com.dhcc.inspect.domain.JdSampleBill;
import com.dhcc.utils.FlowConstant;
import com.sun.rowset.CachedRowSetImpl;
import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;
import framework.dhcc.time.TimeUtil;
import framework.dhcc.tree.bean.ZTreeBean;
import framework.dhcc.utils.LogUtil;

/**
 * 
 * @author longxialin
 * 检验任务分派
 */
public class JdInspectHandleImpl {
	
	Page page = new Page();

	public String getJdInspectHandleList(JdInspectHandle jdInspectHandle, String curPage, String perNumber,
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
		
		String sql = "select b.check_opt_id,a.is_handle,a.sign_status,a.inspect_id,b.bill_id,b.sample_no,a.main_opt_id,a.main_opt_name," +
				"a.end_time,a.description,a.remark,a.is_valid,a.opt_id,a.opt_time,a.org_id,a.out_flag,b.ssim_title,b.product_model,b.product_name,b.product_id,b.tran_id,b.current_status " +
				" from jd_inspect a right join jd_sample_bill b on a.bill_id=b.bill_id,jd_plan_task c,jd_plan_handle d where b.is_valid = '1' and b.check_status = '3' " +
				"and b.task_id = c.task_id and c.task_id = d.task_id and d.inspet_dept_id like '%"+compId+"%' ";

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
			// TODO: handle exception
			page.setSidx("inspect_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectHandle bean = new JdInspectHandle();
			bean.setCheckOptId(crs.getString("check_opt_id"));
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setSsimTitle(crs.getString("ssim_title"));
			bean.setSampleNo(crs.getString("sample_no"));
			bean.setProductName(crs.getString("product_name"));
			bean.setProductModel(crs.getString("product_model"));
			bean.setProductId(crs.getString("product_id"));
			bean.setEndTime(crs.getString("end_time"));
			bean.setDescription(crs.getString("description"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setIsAssinged(crs.getString("is_handle"));
			bean.setOptId(crs.getString("opt_id"));
			bean.setSignStatus(crs.getString("sign_status"));
			bean.setCompanyId(crs.getString("org_id"));
			bean.setOutFlag(crs.getString("out_flag"));
			bean.setTranId(crs.getString("tran_id"));
			bean.setCurrentStatus(crs.getString("current_status"));
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

	public void addJdInspectHandle(JdInspectHandle bean) throws Exception {
		int item1 = 0;
		int item2 = 0;
		String[] itemId1 = {};
		String[] itemId2 = {};
		if(bean.getItemId().length()>0){
			itemId1 = bean.getItemId().split(",");
		}
		if(bean.getNewItemId().length()>0){
			itemId2 = bean.getNewItemId().split(",");
		}
		item1 = itemId1.length;
		item2 = itemId2.length;
		
		String[] sqls = null;
		String[][] params = null;
		String id = DBFacade.getInstance().getID();
		
		if(("1").equals(bean.getOutFlag())){
			sqls = new String[3+item1+item2];
			params = new String[3+item1+item2][];
			sqls[0] = "insert into jd_inspect (inspect_id,bill_id,main_opt_id,main_opt_name,description,remark,end_time," +
					"is_valid,is_handle,opt_id,opt_time,sign_status,org_id,ita_no,out_flag) values (?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?)";
			params[0]  = new String[] { id,bean.getBillId(),bean.getSerialId(),bean.getUserName(),bean.getDescription(),
					bean.getRemark()==null||bean.getRemark().equals("")?"无":bean.getRemark(),
				   bean.getEndTime(), "1", "0", bean.getOptId(), "0",bean.getCompId(),bean.getSampleNo(),bean.getOutFlag()};
		}else if(("2").equals(bean.getOutFlag())){
			sqls = new String[3+item1+item2+3];
			params = new String[3+item1+item2+3][];
			sqls[0] = "insert into jd_inspect (inspect_id,bill_id,description,remark,end_time,is_valid,is_handle,opt_id,opt_time," +
					"sign_status,org_id,ita_no,out_flag,out_compname,out_date,out_result) values (?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?)";
			params[0]  = new String[] { id,bean.getBillId(),bean.getDescription(),bean.getRemark()==null||bean.getRemark().equals("")?"无":bean.getRemark(),
				   bean.getEndTime(), "1", "0", bean.getOptId(), "0",bean.getCompId(),bean.getSampleNo(),bean.getOutFlag(),bean.getOutCompname(),
				   bean.getOutDate(),"0"};
			
			/*根据billId去找上一步tranId*/
			String prTranId = "";
			String sql = "select tran_id from jd_sample_bill where bill_id=?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getBillId()});
			try {
				if (crs.next()) {
					prTranId = crs.getString("tran_id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			String tranId = DBFacade.getInstance().getID();
			/*修改业务主表*/
			sqls[3+item1+item2] = "update jd_sample_bill set current_node_id=?,current_status='11',tran_id=? where bill_id=? ";
			params[3+item1+item2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[10],tranId,bean.getBillId()};
			/*插入当前留痕*/
			sqls[3+item1+item2+1] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
					+ " deal_part_name,deal_opt_id,deal_opt_name,deal_opt_time,deal_time,over_status,deal_status,work_status,opt_order,"
					+ " pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_sample_bill','检验任务外送',?,?,?,?,now(),?,?,?,?,now(),'0','0','1','0','11',?,'1','1')";
			params[3+item1+item2+1] = new String[] {tranId,bean.getBillId(),FlowConstant.CJ_FLOW_ID,
					FlowConstant.CJ_NODE_ARR_ID[10],FlowConstant.CJ_STEP_ARR_ID[10],id,bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(), bean.getOptName(), bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(), bean.getOptName(),prTranId};
			/*为结果填报插入留痕*/
			sqls[3+item1+item2+2] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
					+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,"
					+ " deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
					+ " values (?,?,?,?,?,?,'jd_sample_bill','检验数据填报',?,?,?,?,now(),'0','0','0','0','5',?,'1','1')";
			params[3+item1+item2+2] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
					FlowConstant.CJ_NODE_ARR_ID[4],FlowConstant.CJ_STEP_ARR_ID[4],id,bean.getCompanyId(),
					bean.getCompanyName(),bean.getOptId(),bean.getOptName(),tranId};
		}
		
		
		sqls[1] = "update jd_sample_bill set is_inspect = ? where bill_id = ?";
		params[1] = new String[]{"1",bean.getBillId()};
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验任务添加",
				"检验任务ID：" + id + ";检验任务名称："
						+ bean.getItaTitle());
		sqls[2] = (String) obj[0];
		params[2] = (String[]) obj[1];
		
		for(int i=3,m=0;i<3+item1;i++,m++){
			String itemId = null;
			String inspectOptId = null;
			String inspectOptName = null;
			if(itemId1[m].contains(";")){
				itemId = itemId1[m].split(";")[0];
				inspectOptId = itemId1[m].split(";")[1];
				inspectOptName = itemId1[m].split(";")[2];
			}else{
				itemId = itemId1[m].split(";")[0];
				inspectOptId = bean.getSerialId();
				inspectOptName = bean.getUserName();
			}
			String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { itemId });
			if(crs.next()) {
				sqls[i] = "insert into jd_inspect_item (inspect_task_id,item_id,inspect_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name," +
						"third_name,max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic," +
						"data_from,charge_discount,main_comp,inspect_opt_id,inspect_opt_name ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,?)";
				params[i] = new String[]{DBFacade.getInstance().getID(),crs.getString("item_id"),id,crs.getString("standard_id"),crs.getString("standard_no"),crs.getString("standard_name"),
						crs.getString("type_id"),crs.getString("type_name"),crs.getString("item_name"),crs.getString("second_name"),crs.getString("third_name"),
						crs.getString("max_value"),crs.getString("min_value"),crs.getString("standard_value"),crs.getString("meter_unit"),crs.getString("shape_material"),
						crs.getString("grade_model"),bean.getSampleNo(),crs.getString("special_judge"),crs.getString("inspect_model"),crs.getString("charge_standard"),
						crs.getString("charge_basic"),crs.getString("charge_discount"),bean.getCompanyId(),inspectOptId,inspectOptName};
			}
		}
		
		for(int j=3+item1,n=0;j<3+item1+item2;j++,n++){
			String itemId = null;
			String inspectOptId = null;
			String inspectOptName = null;
			if(itemId2[n].contains(";")){
				itemId = itemId2[n].split(";")[0];
				inspectOptId = itemId2[n].split(";")[1];
				inspectOptName = itemId2[n].split(";")[2];
			}else{
				itemId = itemId2[n].split(";")[0];
				inspectOptId = bean.getSerialId();
				inspectOptName = bean.getUserName();
			}
			String sql2 = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[] { itemId });
			if(crs2.next()) {
				sqls[j] = "insert into jd_inspect_item (inspect_task_id,item_id,inspect_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name," +
						"third_name,max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic," +
						"data_from,charge_discount,main_comp,inspect_opt_id,inspect_opt_name ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,2,?,?,?,?)";
				params[j] = new String[]{DBFacade.getInstance().getID(),crs2.getString("item_id"),id,crs2.getString("standard_id"),crs2.getString("standard_no"),crs2.getString("standard_name"),
						crs2.getString("type_id"),crs2.getString("type_name"),crs2.getString("item_name"),crs2.getString("second_name"),crs2.getString("third_name"),
						crs2.getString("max_value"),crs2.getString("min_value"),crs2.getString("standard_value"),crs2.getString("meter_unit"),crs2.getString("shape_material"),
						crs2.getString("grade_model"),bean.getSampleNo(),crs2.getString("special_judge"),crs2.getString("inspect_model"),crs2.getString("charge_standard"),
						crs2.getString("charge_basic"),crs2.getString("charge_discount"),bean.getCompanyId(),inspectOptId,inspectOptName};
			}
		}
		
		DBFacade.getInstance().execute(sqls, params);
		
	}

	public Map<String, Object> editJdInspectHandle(JdInspectHandle bean) throws Exception {
		int item1 = 0;
		int item2 = 0;
		String[] itemId1 = {};
		String[] itemId2 = {};
		if(bean.getItemId().length()>0){
			itemId1 = bean.getItemId().split(",");
		}
		if(bean.getNewItemId().length()>0){
			itemId2 = bean.getNewItemId().split(",");
		}
		item1 = itemId1.length;
		item2 = itemId2.length;
		
		String[] sqls = new String[3+item1+item2];
		String[][] params = new String[3+item1+item2][];
		//更新管理表中的数据
		sqls[0] = "update jd_inspect set bill_id=?,ita_title=?,main_opt_id=?,main_opt_name=?,end_time=?,description=?,remark=?,sign_status =0,is_handle=0" +
				" where inspect_id = ? ";
		params[0] = new String[]{bean.getBillId(),bean.getItaTitle(),bean.getSerialId(),bean.getUserName(),bean.getEndTime(),
				bean.getDescription(),bean.getRemark()==null?"无":bean.getRemark(),bean.getInspectId()};
		//删除检查表中的信息
		sqls[1] = " delete from jd_inspect_item where inspect_id=? ";
		params[1] = new String[] { bean.getInspectId() };
		//日志信息
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验任务分派修改",
				"检验任务ID：" + bean.getInspectId() + ";检验任务名称："
						+ bean.getItaTitle());
		sqls[2] = (String) obj[0];
		params[2] = (String[]) obj[1];
		
		for(int i=3,m=0;i<3+item1;i++,m++){
			String itemId = null;
			String inspectOptId = null;
			String inspectOptName = null;
			if(itemId1[m].contains(";")){
				itemId = itemId1[m].split(";")[0];
				inspectOptId = itemId1[m].split(";")[1];
				inspectOptName = itemId1[m].split(";")[2];
			}else{
				itemId = itemId1[m].split(";")[0];
				inspectOptId = bean.getSerialId();
				inspectOptName = bean.getUserName();
			}
			String sql = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { itemId });
			if(crs.next()) {
				sqls[i] = "insert into jd_inspect_item (inspect_task_id,item_id,inspect_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name," +
						"third_name,max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic," +
						"data_from,charge_discount,main_comp,inspect_opt_id,inspect_opt_name ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,?,?,?)";
				params[i] = new String[]{DBFacade.getInstance().getID(),crs.getString("item_id"),bean.getInspectId(),crs.getString("standard_id"),crs.getString("standard_no"),crs.getString("standard_name"),
						crs.getString("type_id"),crs.getString("type_name"),crs.getString("item_name"),crs.getString("second_name"),crs.getString("third_name"),
						crs.getString("max_value"),crs.getString("min_value"),crs.getString("standard_value"),crs.getString("meter_unit"),crs.getString("shape_material"),
						crs.getString("grade_model"),bean.getSampleNo(),crs.getString("special_judge"),crs.getString("inspect_model"),crs.getString("charge_standard"),
						crs.getString("charge_basic"),crs.getString("charge_discount"),bean.getCompanyId(),inspectOptId,inspectOptName};
			}
		}
		
		for(int j=3+item1,n=0;j<3+item1+item2;j++,n++){
			String itemId = null;
			String inspectOptId = null;
			String inspectOptName = null;
			if(itemId2[n].contains(";")){
				itemId = itemId2[n].split(";")[0];
				inspectOptId = itemId2[n].split(";")[1];
				inspectOptName = itemId2[n].split(";")[2];
			}else{
				itemId = itemId2[n].split(";")[0];
				inspectOptId = bean.getSerialId();
				inspectOptName = bean.getUserName();
			}
			String sql2 = "select a.item_id,a.standard_id,a.item_name,a.second_name,a.third_name,a.max_value,a.min_value,a.standard_value,a.meter_unit," +
					"a.shape_material,a.grade_model,a.sample_no,a.special_judge,a.inspect_model,a.charge_standard,a.charge_basic,a.charge_discount,a.main_comp," +
					"b.standard_no,b.standard_name,c.type_id,c.type_name from jd_product_item a,jd_product_standard b,jd_product_type c where a.standard_id=b.standard_id and b.type_id=c.type_id and item_id = ?";
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[] { itemId });
			if(crs2.next()) {
				sqls[j] = "insert into jd_inspect_item (inspect_task_id,item_id,inspect_id,standard_id,standard_no,standard_name,type_id,type_name,item_name,second_name," +
						"third_name,max_value,min_value,standard_value,meter_unit,shape_material,grade_model,sample_no,special_judge,inspect_model,charge_standard,charge_basic," +
						"data_from,charge_discount,main_comp,inspect_opt_id,inspect_opt_name ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,2,?,?,?,?)";
				params[j] = new String[]{DBFacade.getInstance().getID(),crs2.getString("item_id"),bean.getInspectId(),crs2.getString("standard_id"),crs2.getString("standard_no"),crs2.getString("standard_name"),
						crs2.getString("type_id"),crs2.getString("type_name"),crs2.getString("item_name"),crs2.getString("second_name"),crs2.getString("third_name"),
						crs2.getString("max_value"),crs2.getString("min_value"),crs2.getString("standard_value"),crs2.getString("meter_unit"),crs2.getString("shape_material"),
						crs2.getString("grade_model"),bean.getSampleNo(),crs2.getString("special_judge"),crs2.getString("inspect_model"),crs2.getString("charge_standard"),
						crs2.getString("charge_basic"),crs2.getString("charge_discount"),bean.getCompanyId(),inspectOptId,inspectOptName};
			}
		}
		
		DBFacade.getInstance().execute(sqls, params);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> delJdInspectHandle(JdInspectHandle bean) {

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[3];
		String[][] params = new String[3][];
		
		String[] s = new String[1];
		String[][] p = new String[1][];
		
		s[0] = "delete from jd_inspect_item where inspect_id = ? ";
		p[0] = new String[]{bean.getInspectId()};
		DBFacade.getInstance().execute(s, p);
		
		sqls[0] = "update jd_sample_bill set is_inspect = '0' where bill_id = (select b.bill_id from jd_inspect b where b.inspect_id=?) ";
//		sqls[0] = "update jd_inspect set is_valid=? where inspect_id = ? ";
		params[0] = new String[] { bean.getInspectId() };
//		params[0] = new String[] { "0", bean.getInspectId() + "%"};		
		
		sqls[1] = "delete from jd_inspect where inspect_id=? ";
//		sqls[0] = "update jd_inspect set is_valid=? where inspect_id = ? ";
		params[1] = new String[] { bean.getInspectId() };
//		params[0] = new String[] { "0", bean.getInspectId() + "%"};
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(), bean.getOptName(), "检验任务删除",
				"检验任务ID：" + bean.getInspectId() + ";检验任务名称："
						+ bean.getItaTitle());
		sqls[2] = (String) obj[0];
		params[2] = (String[]) obj[1];

		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	public Map<String, Object> fenpaiJdInspectHandle(JdInspectHandle bean) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String[] sqls = new String[5];
		String[][] params = new String[5][];
		
		/*根据上一步骤tranId获取当前步骤tranId*/
		String tranId="";
		try {
			tranId = getTranId(bean.getTranId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*根据检验任务Id获取主检人及其所在部门*/
		String sql = "select a.main_opt_id,a.main_opt_name,c.company_id,c.company_name " +
				"from jd_inspect a,hr_staff b,hr_company c where a.main_opt_id=b.serial_id and b.company_id=c.company_id and a.inspect_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {bean.getInspectId()});
		try {
			if (crs.next()) {
				bean.setDealOptId(crs.getString("main_opt_id"));
				bean.setDealOptName(crs.getString("main_opt_name"));
				bean.setDealPartId(crs.getString("company_id"));
				bean.setDealPartName(crs.getString("company_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*更新业务副表jd_inspect(检验任务管理表)*/
		sqls[0] = "update jd_inspect set is_handle=?,handle_opt_time=now(),sign_status=? where inspect_id = ? ";
		params[0] = new String[] {"1","0",bean.getInspectId()};
		
		Object[] obj = LogUtil.getOptParam(DBFacade.getInstance().getID(),
				bean.getOptId(),bean.getOptName(),"检验任务分派","检验任务ID："+bean.getInspectId() 
				+";样品编号："+bean.getSampleNo());
		sqls[1] = (String) obj[0];
		params[1] = (String[]) obj[1];
		
		/*更新业务主表jd_sample_bill(抽样单信息表)*/
		sqls[2] = "update jd_sample_bill set current_node_id=?,current_status='3',tran_id=? where bill_id=? ";
		params[2] = new String[] {FlowConstant.CJ_NODE_ARR_ID[2],tranId,bean.getBillId()};
		
		/**
		 * 检验任务分派修改留痕表记录
		 */
		bean.setNextDept("[{'id':'"+bean.getDealPartId()+"',"+"'name':'"+bean.getDealPartName()+"'}]");
		bean.setNextPerson("[{'id':'"+bean.getDealOptId()+"',"+"'name':'"+bean.getDealOptName()+"'}]");
		
		sqls[3] = "update dh_tranlist set detail_id=?,deal_part_id=?,deal_part_name=?,deal_opt_id=?,deal_opt_name=?,deal_opt_time=now(),deal_time=TIMESTAMPDIFF(hour,create_opt_time,now())+1,deal_status='1',work_status='0',"
				+ " next_dept=?,next_person=? where pre_tran_id=? and work_status='0' ";
		params[3] = new String[] { bean.getInspectId(),bean.getCompanyId(),bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getNextDept(),bean.getNextPerson(),bean.getTranId()};
		/**
		 * 给下一步骤检验任务签收插入留痕数据
		 */
		sqls[4] = "insert into dh_tranlist(tran_id,work_id,flow_id,node_id,step_id,detail_id,belong_work,tran_title,"
				+ " create_dept_id,create_dept_name,create_opt_id,create_opt_name,create_opt_time,deal_part_id,"
				+ " deal_part_name,deal_opt_id,deal_opt_name,deal_time,over_status,deal_status,work_status,opt_order,pre_tran_id,data_resource,data_type) "
				+ " values (?,?,?,?,?,?,'jd_sample_bill','检验任务签收',?,?,?,?,now(),?,?,?,?,'0','0','0','0','4',?,'1','1')";
		params[4] = new String[] { DBFacade.getInstance().getID(),bean.getBillId(),FlowConstant.CJ_FLOW_ID,
				FlowConstant.CJ_NODE_ARR_ID[3],FlowConstant.CJ_STEP_ARR_ID[3],bean.getInspectId(),bean.getCompanyId(),
				bean.getCompanyName(),bean.getOptId(),bean.getOptName(),bean.getDealPartId(),bean.getDealPartName(),bean.getDealOptId(),bean.getDealOptName(),tranId};
		
		DBFacade.getInstance().execute(sqls, params);
		map.put("msg", "true");
		return map;
	}

	/** *******************
	* @param tranId
	* @return
	* 2017-5-19
	* 根据上一步骤tranId获取当前步骤tranId
	 * @throws SQLException 
	*/
	private String getTranId(String tranId) throws SQLException {
		String sql = "select tran_id from dh_tranlist where pre_tran_id=? and work_status='0'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {tranId});
		String preTranId = "";
		if (crs.next()) {
			preTranId = crs.getString("tran_id");
		}
		return preTranId;
	}

	public Map<String, Object> getJdInspectHandleById(String inspectId) throws Exception {
		String sql = "select inspect_id,bill_id,ita_title,main_opt_id,main_opt_name,end_time,description,remark,is_valid," +TimeUtil.getTimeShow("opt_time")+" opt_time,"+
				"is_handle,opt_id,opt_time from jd_inspect where inspect_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { inspectId });
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (crs.next()) {
			map.put("msg", "true");
			JdInspectHandle bean = new JdInspectHandle();
			
			bean.setInspectId(crs.getString("inspect_id"));
			bean.setBillId(crs.getString("bill_id"));
			
			String sql1 = "select sample_no,product_name,product_model from jd_sample_bill where bill_id = ? ";
			CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1,
					new String[] { bean.getBillId() });
			if (crs1.next()) {
				//bean.setSsimTitle(crs1.getString("ssim_title"));
				bean.setSampleNo(crs1.getString("sample_no"));
				bean.setProductName(crs1.getString("product_name"));
				bean.setProductModel(crs1.getString("product_model"));
			}
			bean.setOptTime(crs.getString("opt_time"));
			bean.setItaTitle(crs.getString("ita_title"));
			bean.setMainOptId(crs.getString("main_opt_id"));
			bean.setMainOptName(crs.getString("main_opt_name"));
			bean.setSerialId(crs.getString("main_opt_id"));
			bean.setUserName(crs.getString("main_opt_name"));
//			bean.setSerialId(crs.getString("ita_person").split(";;")[0]);
//			bean.setStartTime(crs.getString("start_time"));
			bean.setEndTime(crs.getString("end_time"));
			bean.setDescription(crs.getString("description"));
			bean.setRemark(crs.getString("remark"));
			bean.setIsValid(crs.getString("is_valid"));
			bean.setIsAssinged(crs.getString("is_handle"));
			bean.setOptId(crs.getString("opt_id"));
//			SimpleDateFormat formatter3 = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			String optTime = crs.getString("opt_time");
//			String time = formatter3.format(formatter3.parse(optTime));
//			bean.setOptTime(time);
			
			String s = "select user_name from hr_staff where serial_id = ? ";
			CachedRowSetImpl c = DBFacade.getInstance().getRowSet(s, new String[] { bean.getOptId() });
			if (c.next()) {
				bean.setOptName(c.getString("user_name"));
			}

			map.put("record", bean);
		} else {
			map.put("msg", "noresult");
		}

		return map;
	}
	public Map<String, Object> getJdInspectItemById(String inspectId) throws Exception {
		String sql = "select item_name,standard_name,second_name,third_name,max_value,min_value,standard_value,meter_unit," +
				"shape_material,grade_model,inspect_opt_id,inspect_opt_name from jd_inspect_item where inspect_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { inspectId });
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		
		while (crs.next()) {
			JdInspectHandle bean = new JdInspectHandle();
			bean.setItemName(crs.getString("item_name"));
			bean.setSecondName(crs.getString("second_name"));
			bean.setStandardName(crs.getString("standard_name"));
			bean.setThirdName(crs.getString("third_name"));
			bean.setMaxValue(crs.getString("max_value"));
			bean.setMinValue(crs.getString("min_value"));
			bean.setStandardValue(crs.getString("standard_value"));
			bean.setMeterUnit(crs.getString("meter_unit"));
			bean.setShapeMaterial(crs.getString("shape_material"));
			bean.setGradeModel(crs.getString("grade_model"));
			bean.setInspectOptId(crs.getString("inspect_opt_id"));
			bean.setInspectOptName(crs.getString("inspect_opt_name"));
			list.add(bean);
		}
		map.put("msg", "true");
		map.put("record", list);
		return map;
	}

	// 编号重复验证
	public boolean checkPlanNo(String inspectId, String billId, int flag) {
		String sql = "select count(*) from jd_inspect where inspect_id = ?";
		if (flag == 1) {
			sql += " and bill_id != " + billId;
		}
		try {
			long count = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { inspectId });
			if (count>0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// 标题是否重复
	public boolean editCheckPlanNo(String itaTitle, String billId, int flag) {
		String sql = "select count(*) from jd_inspect where ita_title = ?";
		if (flag == 1) {
			sql += " and bill_id != " + billId;
		}
		try {
			long obj = (Long) DBFacade.getInstance().getValueBySql(sql,
					new String[] { itaTitle });
			if (obj > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 树形结构
	 *@return List<ZTreeBean>
	 */
//	public List<ZTreeBean> getTree() {
//		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
//		String sql ="SELECT b.bill_id,b.ssim_title FROM jd_sample_bill b WHERE b.is_valid = '1' AND b.check_status = '3' order by bill_id ";
//		try {
//			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
//			while (crs.next()) {
////				String sql1 = "select count(*) from jd_inspect where bill_id = ? ";
////				long count = (Long) DBFacade.getInstance().getValueBySql(sql1,
////						new String[] { crs.getString("bill_id") });
////				if (count > 0) {
////					continue;
////				}
//				ZTreeBean bean = new ZTreeBean();
//				bean.setId(crs.getString("bill_id"));
//				bean.setName(crs.getString("ssim_title"));
//				bean.setDepth("1");
//				treeList.add(bean);
//			}
//		} catch (Exception se) {
//			se.printStackTrace();
//		}
//		return treeList;
//	}
	
	public String getItemList() {
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		String sql = "select b.bill_id,b.ssim_title from jd_sample_bill b where b.is_valid = '1' and b.check_status = '3' order by bill_id ";
		
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{});
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		try {
			while (crs.next()) {
				JdInspectHandle jdprog = new JdInspectHandle();
				jdprog.setBillId(crs.getString("bill_id"));
				jdprog.setSsimTitle(crs.getString("ssim_title"));	
				list.add(jdprog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("list", list);
		JSONObject ob = new JSONObject();
		ob.putAll(map);
		return ob.toString();
	}
	
	/**
	 * 树形结构（检验人员）
	 *@return List<ZTreeBean>
	 */
	public List<ZTreeBean> getTree1(int flag) {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		String sql ="select serial_id,user_name,state from hr_staff  order by serial_id ";
		try {
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			while (crs.next()) {
				ZTreeBean bean = new ZTreeBean();
				bean.setId(crs.getString("serial_id"));
				bean.setName(crs.getString("user_name"));
				
//				if (flag == 1) {
//					String sql1 = "select count(*) from jd_inspect where ita_person = ? and is_assinged = '0' and is_valid = '1' ";
//					long count = (Long) DBFacade.getInstance().getValueBySql(sql1,
//							new String[] { crs.getString("serial_id")+";;"+crs.getString("user_name") });
//					if (count > 0) {
//						bean.setNocheck(true);
//					} else {
//						bean.setNocheck(false);
//					}
//				} else {
//					bean.setNocheck(false);
//				}
				bean.setDepth("1");
				treeList.add(bean);
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return treeList;
	}
	
	public Map<String, Object> getJdItemById(String taskId) {
		String sql2 = "select item_id,standard_name,item_name,second_name,third_name," +
				"standard_value,max_value,min_value,meter_unit,shape_material,grade_model from jd_plan_item " +
			    "where is_valid = '1' and task_id = ? ";
		/*String sql2 = "select distinct a.bill_id, a.item_id, a.standard_id,	a.item_name, a.second_name,	a.third_name," +
				" a.max_value, a.min_value,	a.standard_value, a.meter_unit,	b.standard_name from jd_inspect_item a," +
				" jd_product_standard b where a.standard_id = b.standard_id and a.bill_id = ?";*/
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[]{taskId});
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		try{
			while(crs2.next()){
				JdInspectHandle bean2 = new JdInspectHandle();
				
				/*String s = "select count(*) from jd_inspect_item where item_id = ? ";
				try{
					long obj = (Long) DBFacade.getInstance()
							.getValueBySql(s, new String[] { crs2.getString("item_id") });
					if(obj>0){
						bean2.setCk("checked");
					}
				}catch(Exception e){
					e.printStackTrace();
				}*/
				
				bean2.setItemId(crs2.getString("item_id"));
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
		map.put("msg", "true");
		map.put("items", list);
		return map;
	}
	
	public Map<String, Object> getJdInspectById(String inspctId) throws SQLException {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String sql2 = "select item_id,standard_id,standard_name,type_id,item_name,second_name,third_name,max_value,min_value,standard_value,meter_unit," +
				"shape_material,grade_model,sample_no,data_from,inspect_opt_id,inspect_opt_name from jd_inspect_item where inspect_id = ? ";
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[]{inspctId});
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		List<JdInspectHandle> list2 = new ArrayList<JdInspectHandle>();
		String typeId = "";
		String sampleNo = "";
		try{
			while(crs2.next()){
				JdInspectHandle bean2 = new JdInspectHandle();
				if(crs2.getString("data_from").equals("2")){
					bean2.setCk("checked");
					bean2.setItemId(crs2.getString("item_id"));
					bean2.setStandardName(crs2.getString("standard_name"));
					bean2.setItemName(crs2.getString("item_name"));
					bean2.setSecondName(crs2.getString("second_name"));
					bean2.setThirdName(crs2.getString("third_name"));
					bean2.setMaxValue(crs2.getString("max_value"));
					bean2.setMinValue(crs2.getString("min_value"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setMeterUnit(crs2.getString("meter_unit"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setShapeMaterial(crs2.getString("shape_material"));
					bean2.setGradeModel(crs2.getString("grade_model"));
					bean2.setDataFrom(crs2.getString("data_from"));
					bean2.setInspectOptId(crs2.getString("inspect_opt_id"));
					bean2.setInspectOptName(crs2.getString("inspect_opt_name"));
					typeId = crs2.getString("type_id");
					sampleNo = crs2.getString("sample_no");
					list2.add(bean2);
				}else{
					bean2.setCk("checked");
					bean2.setItemId(crs2.getString("item_id"));
					bean2.setStandardName(crs2.getString("standard_name"));
					bean2.setItemName(crs2.getString("item_name"));
					bean2.setSecondName(crs2.getString("second_name"));
					bean2.setThirdName(crs2.getString("third_name"));
					bean2.setMaxValue(crs2.getString("max_value"));
					bean2.setMinValue(crs2.getString("min_value"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setMeterUnit(crs2.getString("meter_unit"));
					bean2.setStandardValue(crs2.getString("standard_value"));
					bean2.setShapeMaterial(crs2.getString("shape_material"));
					bean2.setGradeModel(crs2.getString("grade_model"));
					bean2.setDataFrom(crs2.getString("data_from"));
					bean2.setInspectOptId(crs2.getString("inspect_opt_id"));
					bean2.setInspectOptName(crs2.getString("inspect_opt_name"));
					typeId = crs2.getString("type_id");
					sampleNo = crs2.getString("sample_no");
					list.add(bean2);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("items", list);
		map.put("newitems", list2);
		map.put("typeId", typeId);
		map.put("sampleNo", sampleNo);
		return map;
	}
	
	public String getCharAndNumr(int length)     
	{     
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
	    
//	    if()
	    String sql = "select ita_no FROM jd_inspect where ita_no =?";
	    CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[]{val});
	    if(crs.size()>0){
	    	val = getCharAndNumr(length);
	    }
	    return val;
	    
	    
	}

	/** *******************
	* @param productId
	* @return
	* 2017-5-10
	* *******************
	*/
	public List<JdInspectHandle> getInspectItemList(String productId) {
		List<JdInspectHandle> menuList = new ArrayList<JdInspectHandle>();
		String sql = "select distinct  standard_id,standard_name from jd_product_standard where  type_id = ? ";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { productId });
		try {
			while (crs.next()) {
				JdInspectHandle menu = new JdInspectHandle(crs.getString("standard_id"), crs
						.getString("standard_name"),crs
						.getString("standard_name"));
				menu.setModuleFromMenu(crs.getString("standard_id"));
				menuList.add(menu);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}

	/** *******************
	* @param standardId
	* @return
	* 2017-5-10
	* *******************
	*/
	public Map<String, Object> getNewItems(String standardId) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		List<JdPlanCase> list = new ArrayList<JdPlanCase>();
		String[] arrs = standardId.split(",");
		for(int i=0;i<arrs.length;i++){
			String sql2 = "select c.standard_name,b.item_id,b.standard_id,b.item_name,b.second_name,b.third_name,b.max_value,b.min_value,b.standard_value,"
					+ " b.meter_unit,b.shape_material,b.grade_model from jd_product_item b,jd_product_standard c "
					+ " where b.standard_id = c.standard_id and c.is_valid = 1 and b.item_id = ? ";
			
			CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2,
					new String[] {arrs[i] });
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
		}
		
		map.put("msg", "true");
		map.put("items", list);
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
	* 2017-5-12
	* *******************
	*/
	public String getItemFaList(JdInspectHandle bean, String curPage,
			String perNumber, String orderByField, String orderByType,
			String searchField, String searchValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/** *******************
	* @param itemId
	* @return
	* 2017-5-13
	* *******************
	*/
	public Map<String, Object> getItemFaById(String itemId,String taskId) {
		String[] itemIds = itemId.split(",");
		
		String sql2 = "select item_id,standard_name,item_name,second_name,third_name," +
				"standard_value,max_value,min_value,meter_unit,shape_material,grade_model from jd_plan_item " +
			    "where is_valid = '1' and task_id = ? ";
		
		String condi = "";
		for(int i=0;i<itemIds.length;i++){
			if(i == 0){
				condi = "and (item_id = '"+itemIds[i]+"' ";
			}else{

				condi = condi + "or item_id = '"+itemIds[i]+"' ";
			}
		}
		sql2 += condi + ")";
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs2 = DBFacade.getInstance().getRowSet(sql2, new String[]{taskId});
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		try{
			while(crs2.next()){
				JdInspectHandle bean2 = new JdInspectHandle();
				bean2.setItemId(crs2.getString("item_id"));
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
				bean2.setCk("checked");
				list.add(bean2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		map.put("msg", "true");
		map.put("items", list);
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
	* 2017-5-16
	* *******************
	*/
	public Map<String, Object> getJdItemListById(JdInspectHandle bean1,
			String curPage, String perNumber, String orderByField,
			String orderBySort, String searchField, String searchValue) {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));
		
		String sql2 = "select item_id,standard_name,item_name,second_name,third_name," +
				"standard_value,max_value,min_value,meter_unit,shape_material,grade_model from jd_plan_item " +
			    "where is_valid = '1' and task_id = '"+bean1.getTaskId()+"'";
		
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql2 = sql2 + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}
		
		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderBySort);
		} else {
			page.setSidx("item_id");
			page.setSord("desc");
		}
		
		PageBean pageData = new PageBean(sql2, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CachedRowSetImpl crs2 = pageData.getCrs();
		List<JdInspectHandle> list = new ArrayList<JdInspectHandle>();
		try{
			while(crs2.next()){
				JdInspectHandle bean2 = new JdInspectHandle();
				bean2.setItemId(crs2.getString("item_id"));
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
		map.put("msg", "true");
		map.put("items", list);
		map.put("totalPage", "" + pageData.getPageAmount());
		map.put("curPage", "" + pageData.getPageNo());
		map.put("totalRecord", "" + pageData.getItemAmount());
		return map;
	}

	/** *******************
	* @param billId
	* @return
	* 2017-6-26
	* 根据抽样单系统编号获取相应的信息并显示在页面上
	*/
	public Map<String, Object> getBillInfo(String billId,JdInspectHandle bean1) {
		String sql = "select a.bill_id,a.sample_num,a.sample_no,a.product_name,a.product_model,a.product_id,a.is_valid,a.task_id,b.product_id type_id,c.inspet_dept_id,c.inspet_dept_name,d.inspect_end_date "+
				" from jd_sample_bill a,jd_plan_task b,jd_plan_handle c,jd_plan_case d where a.check_status = 3 and a.is_inspect = 0 and a.task_id=b.task_id and a.task_id = c.task_id and b.task_id=d.task_id " +
				"and a.bill_id=? and c.inspet_dept_id like '%"+bean1.getCompanyId()+"%'";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] { billId });
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			if (crs.next()) {
				map.put("msg", "true");
				JdSampleBill bean = new JdSampleBill();
				bean.setBillId(crs.getString("bill_id"));
				bean.setIsValid(crs.getString("is_valid"));
				bean.setSampleNum(crs.getString("sample_num"));
				bean.setSampleNo(crs.getString("sample_no"));
				bean.setTaskId(crs.getString("task_id"));
				bean.setTypeId(crs.getString("type_id"));
				bean.setProductId(crs.getString("product_id"));
				bean.setProductName(crs.getString("product_name"));
				bean.setProductModel(crs.getString("product_model"));
				bean.setEndTime(crs.getString("inspect_end_date"));
				map.put("record", bean);
			} else {
				map.put("msg", "noresult");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}	
}
