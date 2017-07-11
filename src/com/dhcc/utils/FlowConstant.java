package com.dhcc.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

public class FlowConstant {
	// 监督抽查任务流程编号
	public static final String CC_FLOW_ID = FlowUtils.getFlowId("jd_plan_task");
	// 监督抽查任务节点编号数组
	public static final String[] CC_NODE_ARR_ID = FlowUtils.getNodeId("jd_plan_task");
	// 监督抽查任务步骤编号数组
	public static final String[] CC_STEP_ARR_ID = FlowUtils.getStepId("jd_plan_task");
	
	// 抽样任务分配流程编号
	public static final String CY_FLOW_ID = FlowUtils.getFlowId("jd_sample");
	// 抽样任务分配编号数组
	public static final String[] CY_NODE_ARR_ID = FlowUtils.getNodeId("jd_sample");
	// 抽样任务分配步骤编号数组
	public static final String[] CY_STEP_ARR_ID = FlowUtils.getStepId("jd_sample");
	
	
	// 抽样检验流程编号
	public static final String CJ_FLOW_ID = FlowUtils.getFlowId("jd_sample_bill");
	// 抽样检验编号数组
	public static final String[] CJ_NODE_ARR_ID = FlowUtils.getNodeId("jd_sample_bill");
	// 抽样检验步骤编号数组
	public static final String[] CJ_STEP_ARR_ID = FlowUtils.getStepId("jd_sample_bill");
		
//	// 特种设备审批流程编号
//	public static final String XK_FLOW_ID = FlowUtils.getFlowId("xk_special_apply");
//	// 特种设备审批节点编号数组
//	public static final String[] XK_NODE_ARR_ID = FlowUtils.getNodeId("xk_special_apply");
//	// 特种设备审批编号数组
//	public static final String[] XK_STEP_ARR_ID = FlowUtils.getStepId("xk_special_apply");
//	
//	//执法
//	public static final String ZF_FLOW_ID = FlowUtils.getFlowId("zf_case_info");
//	public static final String[] ZF_NODE_ARR_ID = FlowUtils.getNodeId("zf_case_info");
//	public static final String[] ZF_STEP_ARR_ID = FlowUtils.getStepId("zf_case_info");
//		
//	
	
	// 步骤对应的业务操作类
	public static JSONArray CLASS_JSON = new JSONArray();
	
	static {
		try{
			// 督查督办
			List<ClassBean> list = new ArrayList<ClassBean>();
//			list.add(new ClassBean(DB_STEP_ARR_ID[1], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(DB_STEP_ARR_ID[2], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(DB_STEP_ARR_ID[3], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(DB_STEP_ARR_ID[4], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(DB_STEP_ARR_ID[5], "com.dhcc.business.impl.TestOptBusiness"));
//			
//			// 会议管理
//			
//			// 建议提案yanggf
//			list.add(new ClassBean(JY_STEP_ARR_ID[1], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[2], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[3], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[4], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[5], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[6], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[7], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[8], "com.dhcc.business.impl.TestOptBusiness"));
//			list.add(new ClassBean(JY_STEP_ARR_ID[9], "com.dhcc.business.impl.TestOptBusiness"));
			
			
			CLASS_JSON = JSONArray.fromObject(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
