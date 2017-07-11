package com.dhcc.inspect.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sun.text.resources.FormatData;


import net.sf.json.JSONObject;

import com.dhcc.inspect.domain.JdInspectItem;
import com.dhcc.inspect.domain.JdInspectReportPrint;
import com.dhcc.utils.ExcelHandle;
import com.dhcc.utils.Office2PDF;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;
import framework.dhcc.page.Page;
import framework.dhcc.page.PageBean;

public class JdInspectReportPrintImpl {

	Page page = new Page();
	
	/**
	 * 列表查询
	 * @param curPage
	 * @param perNumber
	 * @param orderByField
	 * @param orderByType
	 * @param searchField
	 * @param searchValue
	 * @return
	 * @throws SQLException
	 */
	public String getJdInspectReportPrintList(String curPage, String perNumber, String orderByField,
			String orderByType, String searchField, String searchValue,String compId) throws SQLException {
		if (curPage == null || "".equals(curPage)) {
			curPage = "1";
		}
		if (perNumber == null || "".equals(perNumber)) {
			perNumber = "10";
		}
		page.setPage(Integer.parseInt(curPage));
		page.setRows(Integer.parseInt(perNumber));

		String sql = "select a.report_type,a.report_id,a.irp_title,a.report_path,a.inspect_type,b.company_name,c.enterprise_name,d.company_name jy_name,e.product_name,f.recordid,f.author from jd_inspect_report a " +
				"left join hr_company b on a.depart_id = b.company_id left join jd_enterprise_info c on a.ep_id = c.enterprise_id " +
				"left join hr_company d on a.comp_id = d.company_id left join jd_enterprise_product e on a.product_id = e.product_id left join document f on a.irp_title=f.subject where report_path is not NULL and a.comp_id like '%"+compId+"%' and (a.abolish_status='0' and (a.report_type='2' or a.report_type='3')) or a.issue_status = '1'  ";

//		if(jdInspectHandle.getInspectId()!=null && jdInspectHandle.getParentId().length()>0){
//			sql = sql + " and parent_id='" + jdInspectHandle.getParentId() + "' ";
//		}
		
		if (searchField != null && !"".equals(searchField)
				&& !"undefined".equals(searchField)
				&& !"undefined".equals(searchValue)) {
			sql = sql + " and " + searchField + " like '%" + searchValue
					+ "%' ";
		}

		if (orderByField != null && !"".equals(orderByField)
				&& !"undefined".equals(orderByField)) {
			page.setSidx(orderByField);
			page.setSord(orderByType);
		} else {
			page.setSidx("report_id");
			page.setSord("desc");
		}

		PageBean pageData = new PageBean(sql, page);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		List<JdInspectReportPrint> list = new ArrayList<JdInspectReportPrint>();
		CachedRowSetImpl crs = pageData.getCrs();
		while (crs.next()) {
			JdInspectReportPrint bean = new JdInspectReportPrint();
			
			bean.setReportId(crs.getString("report_id"));
			bean.setReportNo(crs.getString("irp_title"));
			bean.setReportType(crs.getString("report_type"));
			bean.setInspectType(crs.getString("inspect_type"));
			bean.setCompanyName(crs.getString("company_name"));
			bean.setEnterpriseName(crs.getString("enterprise_name"));
			bean.setJyName(crs.getString("jy_name"));
			bean.setProductName(crs.getString("product_name"));
			bean.setReportPath(crs.getString("report_path"));
			bean.setRecordId(crs.getString("recordid"));
			bean.setAuthor(crs.getString("author"));
			//bean.setIsAchive(crs.getString("is_achive"));
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

	/**
	 * 导出excel
	 * @param reportId
	 * @param path
	 * @return
	 */
	public Map<String, Object> achiveReport(String reportId,String path){
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        
		boolean flag = false;//报告导出是否成功
		
		JdInspectReportPrint report  =  getReport(reportId);
		JdInspectReportPrint bill = getBill(report.getInspectId());
		/*String issueName = getSignature("issue_opt_id",reportId);//批准人签名
		String checkName = getSignature("check_opt_id",reportId);//审核人签名
		String optName = getSignature("opt_id",reportId);//主检人签名*/
		List<JdInspectItem> inspectItems  = getInspect(reportId);
		
		//模板路径
		String tempFilePath = path +"\\inspect\\report\\cover.xlsx";
		//String tempFilePath1 = path +"\\inspect\\report\\second1.xlsx";
//		String tempFilePath2 = path +"\\inspect\\report\\third.xlsx";
		String filePath = path.substring(0, path.lastIndexOf("\\"))+"\\report";
		
		ExcelHandle handle = new ExcelHandle();
        
		//封面数据
		List<String> dataCell = new ArrayList<String>();
		dataCell.add("reportNo");
		dataCell.add("productName");
		dataCell.add("sjName");
		dataCell.add("enterpriseName");
		dataCell.add("companyName");
		dataCell.add("inspectType");
		dataCell.add("jyName");
		dataCell.add("verificationCode");
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("reportNo", bill.getReportNo());
		dataMap.put("productName", report.getProductName());
		dataMap.put("sjName", bill.getEpName());
		dataMap.put("enterpriseName", bill.getScdwName());		
		dataMap.put("companyName", report.getCompanyName());
		dataMap.put("inspectType", report.getInspectType());
		dataMap.put("jyName", report.getJyName());
		dataMap.put("verificationCode", "查询码："+report.getVerificationCode());
		
		//注意事项数据
		List<String> dataCell0 = new ArrayList<String>();
		dataCell0.add("jyjgAddress");
		dataCell0.add("jyjgTel");
		dataCell0.add("jyjgPostCode");
		dataCell0.add("jyjgFax");
		dataCell0.add("jyjgMail");
		
		Map<String, Object> dataMap0 = new HashMap<String, Object>();
		dataMap0.put("jyjgAddress", report.getCompAddress());
		dataMap0.put("jyjgTel", report.getPhone());
		dataMap0.put("jyjgPostCode", report.getMailCode());
		dataMap0.put("jyjgFax", report.getFax());		
		dataMap0.put("jyjgMail", "");
		
		//报告第一页数据
		List<String> dataCell1 = new ArrayList<String>();
		dataCell1.add("orgName");
		dataCell1.add("reportNo0");
		dataCell1.add("product");
		dataCell1.add("tradeMark");
		dataCell1.add("productModel");
		dataCell1.add("producBatch");
		dataCell1.add("sjInfo");
		dataCell1.add("enterpriseInfo");
		dataCell1.add("company");
		dataCell1.add("sampleDate");
		dataCell1.add("samplePerson");
		dataCell1.add("arriveDate");
		dataCell1.add("sampleQty");
		dataCell1.add("sampleBase");
		dataCell1.add("sealPerson");
		dataCell1.add("isQualified");
		dataCell1.add("billId");
		dataCell1.add("sampleState");
		dataCell1.add("sampleNo");
		dataCell1.add("inspectBase");
		dataCell1.add("judgeBase");
		dataCell1.add("inspectResult");
		dataCell1.add("issueName");
		dataCell1.add("checkName");
		dataCell1.add("optName");
		dataCell1.add("qfDate");
//		dataCell1.add("year");
//		dataCell1.add("month");
//		dataCell1.add("date");
		
		Map<String, Object> dataMap1 = new HashMap<String, Object>();
		dataMap1.put("orgName", report.getJyName());
		dataMap1.put("reportNo0", bill.getReportNo());
		dataMap1.put("product", report.getProductName());
		dataMap1.put("tradeMark", bill.getTradeMark());
		dataMap1.put("productModel", bill.getProductModel());		
		dataMap1.put("producBatch", bill.getProductBatch());
		dataMap1.put("sjInfo", bill.getSjInfo());
		dataMap1.put("enterpriseInfo", bill.getEnterpeiseInfo());
		dataMap1.put("company", report.getCompanyName());
		dataMap1.put("sampleDate", bill.getSampleDate());
		dataMap1.put("samplePerson", bill.getSamplePerson());
		dataMap1.put("arriveDate", bill.getArriveDate());
		dataMap1.put("sampleQty", bill.getSampleQty());
		dataMap1.put("sampleBase", bill.getSampleBase());
		dataMap1.put("sealPerson", bill.getSealPerson());
		dataMap1.put("isQualified", bill.getIsQualified());
		dataMap1.put("billId", bill.getSampleNum());
		dataMap1.put("sampleState", bill.getSampleState());
		dataMap1.put("sampleNo", bill.getSampleNo());
		/*检验依据是产品类型标准*/
		String inspectBase = "";
		for(int i = 0;i<inspectItems.size();i++){
			if(inspectBase.indexOf(inspectItems.get(i).getStandardName())==-1){
				inspectBase = inspectBase+inspectItems.get(i).getStandardName()+"、";
			}
		}
		inspectBase = inspectBase.length()==0?inspectBase:inspectBase.substring(0, inspectBase.length()-1);
		
		dataMap1.put("inspectBase", inspectBase);
		dataMap1.put("judgeBase", bill.getJudgeBase());
		/*检验结论写成固定格式的结论用语*/
		/*String result = "";
		if(bill.getInspectResult()=="合格"){
			result = "根据"+inspectBase+"检验依据，所检项目符合"+bill.getJudgeBase()+"判定要求。";
		}else{
			result = "根据"+inspectBase+"检验依据，所检项目不符合"+bill.getJudgeBase()+"判定要求。";
		}*/
		
		dataMap1.put("inspectResult", bill.getInspectResult());
		dataMap1.put("issueName", "");
		dataMap1.put("checkName", "");
		dataMap1.put("optName", "");
		dataMap1.put("qfDate", "");
//		dataMap1.put("year", "");
//		dataMap1.put("month", "");
//		dataMap1.put("date", "");
		//报告第一页数据
		//报告第二页数据
		List<String> dataCell2 = new ArrayList<String>();
		dataCell2.add("TestOrg");
		dataCell2.add("reportNo1");
		Map<String, Object> dataMap2 = new HashMap<String, Object>();
		dataMap2.put("TestOrg", report.getJyName());
		dataMap2.put("reportNo1", bill.getReportNo());
		
		List<String> listCell2 = new ArrayList<String>();
		listCell2.add("inspectTaskIds");
		listCell2.add("itemNames");
		listCell2.add("secondNames");
		listCell2.add("thirdNames");
		listCell2.add("meterUnits");
		listCell2.add("standardValues");
		listCell2.add("inspectValues");
		listCell2.add("inspectResults");
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		int num = 1;
		String itemName ="";
		if(inspectItems.size()>0){
			itemName = inspectItems.get(0).getItemName();
		}
		if(inspectItems.size()>0){
			
			for(JdInspectItem item:inspectItems){
				if(item.getItemName() != null && !itemName.equals(item.getItemName())){
					num++;
					itemName = item.getItemName();
				}
				Map<String, Object> mapI = new HashMap<String, Object>();
				mapI.put("inspectTaskIds", String.valueOf(num));
				mapI.put("itemNames", item.getItemName());
				mapI.put("secondNames", item.getSecondName());
				mapI.put("thirdNames", item.getThirdName());
				mapI.put("meterUnits", item.getMeterUnit());
				mapI.put("standardValues", item.getStandardValue());
				mapI.put("inspectValues", item.getInspectValue());
				mapI.put("inspectResults", item.getInspectResult());
				dataList.add(mapI);
			}
			
			Map<String, Object> mapI0 = new HashMap<String, Object>();
			mapI0.put("inspectTaskIds", "————本页为最后一页，以下空白————");
			dataList.add(mapI0);
		}
		/*
		//各项目对应检测值
		List<String[]> dataset = new ArrayList<String[]>();
		int num = 1;
		String itemName ="";
		if(inspectItems.size()>0){
			itemName = inspectItems.get(0).getItemName();
		}
		//System.out.println(itemName +"---------"+inspectItems);
		for(JdInspectItem item:inspectItems){
			if(item.getItemName() != null && !itemName.equals(item.getItemName())){
				num++;
				itemName = item.getItemName();
			}
			dataset.add(new String[]{String.valueOf(num),item.getItemName(),item.getSecondName(),item.getThirdName(),item.getMeterUnit(),item.getStandardValue(),item.getInspectValue(),item.getInspectResult()});
		}
		*/
		
		String id = getDateTime();
		try {
			handle.writeData(tempFilePath, dataCell, dataMap, 0);//写入封面
			handle.writeData(tempFilePath, dataCell0, dataMap0, 1);//写入报告第一页
     		handle.writeData(tempFilePath, dataCell1, dataMap1, 2);//写入报告第一页
			handle.writeData(tempFilePath, dataCell2, dataMap2, 3);//写入报告第二页
			handle.writeListData(tempFilePath, listCell2, dataList,3);
			File file = new File(filePath+"\\"+id+"-"+ reportId+".xlsx");
			//File file1 = new File(filePath+"\\"+id+"-"+ reportId+"(1)"+".xls");
			
			//如果不存在report文件夹，创建文件夹
			if (!new File(filePath).exists()) {
				new File(filePath).mkdirs();
			}
			
			OutputStream os = new FileOutputStream(file);
			//handle.writeDataSet(tempFilePath2, dataCell2,os, dataList, 0);//第二页
//			OutputStream os1 = new FileOutputStream(file1);
//			//handle.writeDataSet(tempFilePath2,os, dataset, 2);//第三页
//			String[] headers = { "序号","检测项目","二级项目","三级项目", "计量单位", "技术要求", "检验结果", "单项结论" };
//			ExcelUtil_Extend.createExcelMerge("测试.xls", headers, dataset, false,
//					os1, new Integer[] { 0, 1 , 2, 3 }, new Integer[] { 0, 1,2,3},
//					null, null);
			// 写到输出流并关闭资源
		    
			handle.writeAndClose(tempFilePath, os);

			os.flush();
			os.close();
			//os1.close();
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			flag = false;
			e.printStackTrace();
		}
		
		handle.readClose(tempFilePath);	
//		handle.readClose(tempFilePath2);
		Office2PDF.openOffice2Pdf(filePath+"\\"+id+"-"+ reportId+".xlsx",null);
		String pdfPath = id+"-"+ reportId+".pdf";
        if(flag){
        	String[] sqls = new String[1];
    		String[][] params = new String[1][];
    		
    		//更新检验报告
    		filePath = filePath.replace("\\", "\\\\");
    		System.out.println("akjshfklasjdjfklja"+ "    "+filePath);
    		sqls[0] = "update jd_inspect_report set report_path= ? where report_id =?";
    		params[0] = new String[]{filePath+"\\\\"+id+"-"+ reportId+".xlsx",reportId};
    		
    		DBFacade.getInstance().execute(sqls, params);
        }
        map.put("path", pdfPath);
		map.put("msg", flag);
		return map;
	}
	
	
	/**
	 * 导出excel
	 * @param reportId
	 * @param path
	 * @return
	 */
	public Map<String, Object> signReport(String reportId,String path,JdInspectReportPrint bean1){
		String ExcelPath=null;
		String issueTime = null;
		String sql = "select report_path,issue_time from jd_inspect_report where report_id=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[] {reportId});
		try {
			if(crs.next()){
				ExcelPath = crs.getString("report_path");
				issueTime = crs.getString("issue_time");
				System.out.println(issueTime);
				System.out.println(ExcelPath);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*获取hr_staff表里面的签名路径*/
		String[] issueName = getSignature("issue_opt_id",reportId).split(";;");//批准人签名
		String[] checkName = getSignature("check_opt_id",reportId).split(";;");//审核人签名
		//String[] optName = getSignature("opt_id",reportId).split(";;");//主检人签名
		String[] optName = getSignature("main_opt_id",reportId).split(";;");//主检人签名
		
		//判断报告是否签发

		FileOutputStream fileOut = null;  
		BufferedImage bufferImg = null; 
		BufferedImage bufferImg1 = null; 
		BufferedImage bufferImg2 = null; 
		XSSFWorkbook workbook;
		String filePath1 = path.substring(0, path.lastIndexOf("\\"));
		try {
			workbook = new XSSFWorkbook(new FileInputStream(ExcelPath));
			System.out.println(ExcelPath);
			XSSFSheet sheet = workbook.getSheet("content1");
			// 读取图片  
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(); 
			String path1 = filePath1.replace("\\", "/");
			System.out.println(path1+issueName[1]);
			bufferImg = ImageIO.read(new File(path1+issueName[1]));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);  
			// 设置图片位置  
			XSSFDrawing  patriarch = sheet.createDrawingPatriarch();  
			XSSFClientAnchor  anchor = new XSSFClientAnchor(0, 0, 0, 0,1, 19, 2, 20);   
			patriarch .createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG)); 
			
			ByteArrayOutputStream byteArrayOut1 = new ByteArrayOutputStream();
			bufferImg1 = ImageIO.read(new File(path1+checkName[1]));
			System.out.println(path1+checkName[1]);
			ImageIO.write(bufferImg1, "jpg", byteArrayOut1);  
			// 设置图片位置  
			XSSFDrawing  patriarch1 = sheet.createDrawingPatriarch();  
			XSSFClientAnchor  anchor1 = new XSSFClientAnchor(0, 0, 0, 0,3, 19, 4, 20);   
			patriarch1 .createPicture(anchor1, workbook.addPicture(byteArrayOut1.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

			ByteArrayOutputStream byteArrayOut2 = new ByteArrayOutputStream();
			bufferImg2 = ImageIO.read(new File(path1+optName[1]));
			System.out.println(path1+optName[1]);
			ImageIO.write(bufferImg2, "jpg", byteArrayOut2);  
			// 设置图片位置  
			XSSFDrawing  patriarch2 = sheet.createDrawingPatriarch();  
			XSSFClientAnchor  anchor2 = new XSSFClientAnchor(0, 0, 0, 0,5, 19, 6, 20);   
			patriarch2 .createPicture(anchor2, workbook.addPicture(byteArrayOut2.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			
			
	        XSSFRow row = null;  
	        row = sheet.getRow(16); 
	        Cell cell = row.getCell(5);
	        cell.setCellValue(issueTime.substring(0,10));
			
			
			
			// 输出到磁盘  
			fileOut = new FileOutputStream(ExcelPath); 
			System.out.println(ExcelPath);
			workbook.write(fileOut);  
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Office2PDF.openOffice2Pdf(ExcelPath,null);
		int start = ExcelPath.lastIndexOf('/');
		int end = ExcelPath.lastIndexOf('.');
		String name = ExcelPath.substring(start+1,end);
		String name1 = ExcelPath.replace("\\", "/");
		String pdfPath = name1+".pdf";
		
		//签完名之后把报告以流的形式存入数据库
		reportSign(bean1, path, reportId);
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("path", pdfPath);
		map.put("msg", true);
		return map;
	}

		
	
	
	
	
	/** *******************
	* @param reportId
	* @return
	* 2017-6-14
	* 获取报告第二页上的批准人、审核人和主检人签名
	*/
	private String getSignature(String nameStr,String reportId) {
		String signName = "";
		String sql = null;
		if(nameStr.equals("main_opt_id")){
			sql = "select IFNULL(b.sign_path,'/') sign_path from jd_inspect_report a,hr_staff b,jd_inspect c where c."+nameStr+"=b.serial_id and a.report_id =? and a.inspect_id = c.inspect_id ";
		}else{
			sql = "select IFNULL(b.sign_path,'/') sign_path from jd_inspect_report a,hr_staff b where a."+nameStr+"=b.serial_id and a.report_id =?";
		}
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		try {
			if(crs.next()){
				signName = crs.getString("sign_path");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return signName;
	}
	

	/**
	 * 当前时间字符串
	 * @return
	 */
	private String getDateTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = sdf.format(date);
		String count = "" + (int) (Math.random() * 1000000);
		if (count.length() < 6) {
			for (int i = 0; i < 6 - count.length(); i++) {
				result = result + "0";
			}
		}
		return result + count;
	}

    /**
     * 根据Id获取报告信息——报告第一页
     * @param reportId
     * @return
     */
	private JdInspectReportPrint getBill(String reportId) {
		// TODO Auto-generated method stub
		String sql = "select a.bill_id,a.sample_id,a.sample_num,a.product_id,a.product_name,a.trade_mark,a.product_model,a.sample_no,a.product_batch,a.product_date,a.sample_qty," +
				"a.product_level,a.sample_base,a.sample_state,b.inspest_base,b.judge_base,c.is_qualified,c.fill_desc,a.sample_date,a.opt_name," +
				"a.report_no,a.arrive_date,a.seal_person,a.ep_name,a.scdw_name,d.address,d.post_code,d.telephone,e.address address2,e.post_code post_code2,e.telephone telephone2 "+
				"from jd_sample_bill a left join jd_enterprise_info d on a.ep_id=d.enterprise_id left join jd_enterprise_info e on a.scdw_id=e.enterprise_id,jd_plan_case b,jd_inspect c where a.task_id = b.task_id and a.bill_id = c.bill_id and c.inspect_id= ?";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		JdInspectReportPrint bill = new JdInspectReportPrint();
		try {
			if(crs.next()){
				bill.setBillId(crs.getString("bill_id"));
				bill.setSampleNum(crs.getString("sample_num"));
				bill.setReportId(crs.getString("sample_no"));
				bill.setProductName(crs.getString("product_name"));
				bill.setTradeMark(crs.getString("trade_mark"));
				bill.setProductModel(crs.getString("product_model"));
				bill.setProductBatch(crs.getString("product_date")+"/"+crs.getString("product_batch"));
				bill.setSampleQty(crs.getString("sample_qty"));
				bill.setSampleBase(crs.getString("sample_base"));
				bill.setSampleDate(crs.getString("sample_date"));
				String sql1 = "select sample_person_name from jd_sample_base where sample_id=? and product_id=? ";
				CachedRowSetImpl crs1 = DBFacade.getInstance().getRowSet(sql1, new String[] { crs.getString("sample_id"),crs.getString("product_id") });
				try {
					while(crs1.next()){
						bill.setSamplePerson(crs1.getString("sample_person_name"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				bill.setInspectResult(crs.getString("fill_desc"));
				
				if(crs.getString("sample_state") !=null && "1".equals(crs.getString("sample_state"))){
					bill.setSampleState("已封样");
				}else{
					bill.setSampleState("");
				}
				
				bill.setInspestBase(crs.getString("inspest_base"));
				bill.setJudgeBase(crs.getString("judge_base"));
				bill.setIsQualified(crs.getString("product_level"));
				/*if(crs.getString("product_level") !=null && "0".equals(crs.getString("product_level"))){
					bill.setIsQualified("特等品");
				}else if(crs.getString("product_level") !=null && "1".equals(crs.getString("product_level"))){
					bill.setIsQualified("一等品");
				}else if(crs.getString("product_level") !=null && "2".equals(crs.getString("product_level"))){
					bill.setIsQualified("二等品");
				}else if(crs.getString("product_level") !=null && "3".equals(crs.getString("product_level"))){
					bill.setIsQualified("合格品");
				}else if(crs.getString("product_level") !=null && "4".equals(crs.getString("product_level"))){
					bill.setIsQualified("优等品");
				}*/
				
				bill.setSampleNo(crs.getString("sample_no"));
				bill.setReportNo(crs.getString("report_no"));
				bill.setArriveDate(crs.getString("arrive_date"));
				bill.setSealPerson(crs.getString("seal_person"));
				bill.setEpName(crs.getString("ep_name"));
				bill.setScdwName(crs.getString("scdw_name"));
				
				String sjInfo = "";
				if(crs.getString("ep_name") != null){
					sjInfo += crs.getString("ep_name")+"/";
				}
				if(crs.getString("address") != null){
					sjInfo += crs.getString("address")+"/";
				}
				if(crs.getString("telephone") != null){
					sjInfo += crs.getString("telephone")+"/";
				}
				if(crs.getString("post_code") != null){
					sjInfo += crs.getString("post_code");
				}
				bill.setSjInfo(sjInfo);
				
				String enterpriseInfo = "";
				if(crs.getString("scdw_name") != null){
					enterpriseInfo += crs.getString("scdw_name")+"/";
				}
				if(crs.getString("address2") != null){
					enterpriseInfo += crs.getString("address2")+"/";
				}
				if(crs.getString("telephone2") != null){
					enterpriseInfo += crs.getString("telephone2")+"/";
				}
				if(crs.getString("post_code2") != null){
					enterpriseInfo += crs.getString("post_code2");
				}
				bill.setEnterpeiseInfo(enterpriseInfo);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return bill;
	}


	/**
	 * 获取封面信息
	 * @param reportId
	 * @return
	 * @throws SQLException
	 */
	private JdInspectReportPrint getReport(String reportId) {
		String sql = "select a.report_id,a.inspect_id,a.verification_code,a.inspect_type,b.company_name,c.enterprise_name,c.is_prod,c.address,c.post_code,c.telephone,d.company_name jy_name," +
				"d.comp_address,d.phone,d.fax,d.mail_code,e.product_name from jd_inspect_report a " +
				"left join hr_company b on a.depart_id = b.company_id left join jd_enterprise_info c on a.ep_id = c.enterprise_id " +
				"left join hr_company d on a.comp_id = d.company_id left join jd_enterprise_product e on a.product_id = e.product_id " +
				"where a.report_id = ? ";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		JdInspectReportPrint report = new JdInspectReportPrint();
		try {
			if(crs.next()){
				report.setReportId(crs.getString("report_id"));
				report.setVerificationCode(crs.getString("verification_code"));
				
				if(crs.getString("inspect_type")!=null && !"".equals(crs.getString("inspect_type"))){
					if("1".equals(crs.getString("inspect_type"))){
						report.setInspectType("国家级监督抽查");						
					}else if("2".equals(crs.getString("inspect_type"))){
						report.setInspectType("省级监督抽查");						
					}else if("3".equals(crs.getString("inspect_type"))){
						report.setInspectType("市级监督抽查");						
					}else if("4".equals(crs.getString("inspect_type"))){
						report.setInspectType("县级监督抽查");						
					}else if("5".equals(crs.getString("inspect_type"))){
						report.setInspectType("其他监督抽查");						
					}			
				}
				
					
				report.setCompanyName(crs.getString("company_name"));
				report.setEnterpriseName(crs.getString("enterprise_name"));
				report.setJyName(crs.getString("jy_name"));
				report.setCompAddress(crs.getString("comp_address"));
				report.setPhone(crs.getString("phone"));
				report.setFax(crs.getString("fax"));
				report.setMailCode(crs.getString("mail_code"));
				report.setProductName(crs.getString("product_name"));
				report.setInspectId(crs.getString("inspect_id"));
				
				String enterpriseInfo = "";
				if(crs.getString("enterprise_name") != null){
					enterpriseInfo += crs.getString("enterprise_name")+"/";
				}
				if(crs.getString("address") != null){
					enterpriseInfo += crs.getString("address")+"/";
				}
				if(crs.getString("telephone") != null){
					enterpriseInfo += crs.getString("telephone")+"/";
				}
				if(crs.getString("post_code") != null){
					enterpriseInfo += crs.getString("post_code");
				}
				
				if(crs.getString("is_prod") != null && crs.getString("is_prod").equals("N")){
					report.setSjEnterpriseName(crs.getString("enterprise_name"));
					report.setEnterpriseName("------");
					report.setSjInfo(enterpriseInfo);
					report.setEnterpeiseInfo("------");
				}else if(crs.getString("is_prod") != null && crs.getString("is_prod").equals("S")){
					report.setSjEnterpriseName("------");
					report.setEnterpriseName(crs.getString("enterprise_name"));
					report.setSjInfo("------");
					report.setEnterpeiseInfo(enterpriseInfo);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return report;
	}	

	/**
	 * 各检测项目及检测值
	 * @param reportId
	 * @return
	 */
	private  List<JdInspectItem> getInspect(String reportId) {
		String sql = "select inspect_task_id,inspect_id,item_id,item_name,second_name,third_name," +
				"IFNULL(max_value,'/') max_value,IFNULL(min_value,'/') min_value,IFNULL(standard_value,'/') standard_value,IFNULL(meter_unit,'/') meter_unit," +
				"IFNULL(inspect_value,'/') inspect_value,IFNULL(inspect_result,'/') inspect_result,IFNULL(standard_name,'/') standard_name " +
				"from jd_inspect_item where inspect_id =(select b.inspect_id from jd_inspect_report b where b.report_id =?) order by item_name";

		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, new String[] { reportId });
		List<JdInspectItem> items = new ArrayList<JdInspectItem>();
		try {
			while(crs.next()){
				JdInspectItem item = new JdInspectItem();
				item.setInspectTaskId(crs.getString("inspect_task_id"));
				item.setInspectId(crs.getString("inspect_id"));
				item.setItemId(crs.getString("item_id"));
				item.setItemName(crs.getString("item_name"));	
				item.setSecondName(crs.getString("second_name"));
				item.setThirdName(crs.getString("third_name"));
				item.setMaxValue(crs.getString("max_value"));
				item.setMinValue(crs.getString("min_value"));
				item.setStandardValue(crs.getString("standard_value"));
				item.setMeterUnit(crs.getString("meter_unit"));
				item.setInspectValue(crs.getString("inspect_value"));
				item.setInspectResult(crs.getString("inspect_result"));
				item.setStandardName(crs.getString("standard_name"));
				items.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return items;
		
	}

	/** *******************
	* @return
	* 2017-6-21
	* 在进行电子签章之前先把报告以二进制的形式存入数据库
	*/
	public Map<String, Object> reportSign(JdInspectReportPrint bean1,String path,String reportId) {
		String mSubject;
		long mFileSize = 0;
		String mFileType="pdf";
		String mStatus="READ";
		byte[] mFileBody;
		String mFileName;
		String ExcelPath=null;
		String sql = "select report_path,irp_title from jd_inspect_report where report_id = ?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,new String[]{reportId});
		try {
			while(crs.next()){
				ExcelPath = crs.getString("report_path");
				mSubject = crs.getString("irp_title");
				mFileName = ExcelPath.substring(ExcelPath.lastIndexOf("-")+1, ExcelPath.lastIndexOf("."))+".pdf";
				
				File file = new File(ExcelPath); 
				mFileSize = file.length();
				
				mFileBody = getBytes(ExcelPath.substring(0, ExcelPath.lastIndexOf("."))+".pdf");
				
				String[] sqls = new String[2 ];
				Object[][] params = new Object[2][];
				/*先看看有没有记录*/
				String mysql="select recordid from  document where subject=? ";
				CachedRowSetImpl mycrs = DBFacade.getInstance().getRowSet(mysql,new String[]{mSubject});
				if (mycrs.next()){
					
					sqls[0] = "update Document set Subject=?,Author=?,FileDate=now(),FileType=?,Status=? where RecordID=? ";
					params[0] = new String[] {mSubject,bean1.getOptName(),mFileType,mStatus,mycrs.getString("recordid")};
					
					sqls[1] = "update Document_File set FileName=?,FileType=?,FileSize=?,FileDate=now(),FileBody=?,FilePath=?,UserName=?,Descript=? where RecordID=? ";
					params[1] = new Object[] {mFileName,mFileType,mFileSize,mFileBody,ExcelPath,bean1.getOptName(),mFileType,mycrs.getString("recordid")};
				}else{
					String decomentId = DBFacade.getInstance().getID();
					decomentId = decomentId.substring(decomentId.length()-13,decomentId.length());
					
					sqls[0] = "insert into Document (RecordID,Subject,Author,FileDate,FileType,Status) values (?,?,?,now(),?,?)";
					params[0] = new String[] {decomentId,mSubject,bean1.getOptName(),mFileType,mStatus};
					
					sqls[1] = "insert into Document_File (RecordID,FileName,FileType,FileSize,FileDate,FileBody,FilePath,UserName,Descript) values (?,?,?,?,now(),?,?,?,? )";
					params[1] = new Object[] { decomentId,mFileName,mFileType,mFileSize,mFileBody,ExcelPath,bean1.getOptName(),mFileType};
				}
				
				DBFacade.getInstance().execute(sqls, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("msg", true);
		return map;
	}
	
	private byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());  
            byte[] b = new byte[(int) file.length()];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
                bos.flush();
            }  
            
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
}
