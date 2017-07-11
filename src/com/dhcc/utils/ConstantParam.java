/**
 * 
 */
package com.dhcc.utils;

/**
 * @author Administrator 定义系统参数常量
 * 
 */
public interface ConstantParam {
	static final String REPORT_TITLE = "贵州省质量技术监督局";//公司名称传递参数（在报表内显示）
	static final String SYSTEM_TITLE = "贵州省质监局产品质量监管平台";
	// 同存储函数getbatch_no()同时修改
	static final String MARKET_MAIN_ID = "123456789";

	public static final String PRODUCT_IS_NONVALID = "0";// 无效
	public static final String PRODUCT_IS_VALID = "1";// 有效

	public static final String RENT_CHARGE_PRINT_TITLE = "XXX租用收费凭证";// 租用收费打印
	public static final String RENT_TRANSFER_PRINT_TITLE = "XXX租用过户凭证";// 租用收费打印
	public static final String RENT_CONTRACT_PRINT_TITLE = "XXX租用合同凭证";// 租用收费打印
	public static final int ELEC_SCALE_DESPOSIT = 200; // 电子秤押金

	public static final String TIME_FORMAT = "yyyy-mm-dd HH24:mi:ss";
	public static final String DATE_FORMAT = "yyyy-mm-dd";
	
	public static final String 	BANK_CURRENCY="RMB";
	public static final String  BANK_REMARK="企业网银转帐";
}
