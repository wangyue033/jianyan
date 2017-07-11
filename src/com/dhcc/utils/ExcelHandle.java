package com.dhcc.utils;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 对excel进行操作工具类 https://www.oschina.net/code/snippet_149945_45187
 * 
 * 
 * @author xiliang.xiao
 * @date 2015年1月8日 下午1:46:36
 * 
 **/
@SuppressWarnings("rawtypes")
public class ExcelHandle {

	private Map<String, HashMap[]> tempFileMap = new HashMap<String, HashMap[]>();
	private Map<String, Map<String, Cell>> cellMap = new HashMap<String, Map<String, Cell>>();
	private Map<String, FileInputStream> tempStream = new HashMap<String, FileInputStream>();
	private Map<String, Workbook> tempWorkbook = new HashMap<String, Workbook>();
	private Map<String, Workbook> dataWorkbook = new HashMap<String, Workbook>();

	/**
	 * 单无格类
	 * 
	 * @author xiliang.xiao
	 * 
	 */
	class Cell {
		private int column;// 列
		private int line;// 行
		private CellStyle cellStyle;

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public CellStyle getCellStyle() {
			return cellStyle;
		}

		public void setCellStyle(CellStyle cellStyle) {
			this.cellStyle = cellStyle;
		}
	}

	/**
	 * 向Excel中输入相同title的多条数据
	 * 
	 * @param tempFilePath
	 *            excel模板文件路径
	 * @param cellList
	 *            需要填充的数据（模板<!%后的字符串）
	 * @param dataList
	 *            填充的数据
	 * @param sheet
	 *            填充的excel sheet,从0开始
	 * @throws IOException
	 */
	public void writeListData(String tempFilePath, List<String> cellList,
			List<Map<String, Object>> dataList, int sheet) {
		
		HashMap temp;
		try {
			// 获取模板填充格式位置等数据
			temp = getTemp(tempFilePath, sheet);
			// 按模板为写入板
			Workbook temWorkbook = getTempWorkbook(tempFilePath);
			// 获取数据填充开始行
			int startCell = Integer.parseInt((String) temp.get("STARTCELL"));
			// 数据填充的sheet
			Sheet wsheet = temWorkbook.getSheetAt(sheet);
			// 移除模板开始行数据即<!%
			wsheet.removeRow(wsheet.getRow(startCell));

			if (dataList != null && dataList.size() > 0) {

				for (Map<String, Object> map : dataList) {
					for (String cell : cellList) {
						// 获取对应单元格数据
						Cell c = getCell(cell, temp, temWorkbook, tempFilePath);
						// 写入数据
						ExcelUtil2.setValue(wsheet, startCell, c.getColumn(),
								map.get(cell), c.getCellStyle());
					}
					startCell++;
					
				}
				
			}
			
			Integer[] mergeBasis = new Integer[] { 0, 1 , 2, 3 };
			Integer[] mergeCells = new Integer[] { 0, 1 , 2, 3 };
			if (mergeBasis != null && mergeBasis.length > 0
					&& mergeCells != null && mergeCells.length > 0) {
				for (int i = 0; i < mergeCells.length; i++) {
					mergedRegion(wsheet, mergeCells[i], 1,
							wsheet.getLastRowNum(), temWorkbook, mergeBasis);
				}
			}
			/*加入最后一行“以下空白”*/
			wsheet.getRow(wsheet.getLastRowNum()).getCell(0).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//wsheet.getRow(wsheet.getLastRowNum()).getCell(0).getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_NONE);
			//wsheet.getRow(wsheet.getLastRowNum()).getCell(0).getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
			//wsheet.getRow(wsheet.getLastRowNum()).getCell(7).getCellStyle().setBorderRight(HSSFCellStyle.BORDER_NONE);
			wsheet.addMergedRegion(new CellRangeAddress(wsheet.getLastRowNum(),wsheet.getLastRowNum(),0,7));
			/*每一行如果它的二级项目和三级项目都为空，则合并一二三*/
			
			for(int j=4;j<wsheet.getLastRowNum()+1;j++){
				if((("").equals(wsheet.getRow(j).getCell(2).getStringCellValue())||wsheet.getRow(j).getCell(2).getStringCellValue()==null)&&
						(("").equals(wsheet.getRow(j).getCell(3).getStringCellValue())||wsheet.getRow(j).getCell(3).getStringCellValue()==null)){
					wsheet.addMergedRegion(new CellRangeAddress(j,j,1,3));
				}
			}
			
			/*每一行如果它的三级项目都为空，则合并二三*/
			/*for(int j=4;j<wsheet.getLastRowNum()+1;j++){
				if((("").equals(wsheet.getRow(j).getCell(3).getStringCellValue())||wsheet.getRow(j).getCell(3).getStringCellValue()==null)){
					wsheet.addMergedRegion(new CellRangeAddress(j,j,2,3));
				}
			}*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 按模板向Excel中相应地方填充数据
	 * 
	 * @param tempFilePath
	 *            excel模板文件路径
	 * @param cellList
	 *            需要填充的数据（模板<%后的字符串）
	 * @param dataMap
	 *            填充的数据
	 * @param sheet
	 *            填充的excel sheet,从0开始
	 * @throws IOException
	 */
	public void writeData(String tempFilePath, List<String> cellList,
			Map<String, Object> dataMap, int sheet) throws IOException {
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 按模板为写入板
		Workbook wbModule = getTempWorkbook(tempFilePath);
		// 数据填充的sheet
		Sheet wsheet = wbModule.getSheetAt(sheet);
		if (dataMap != null && dataMap.size() > 0) {
			for (String cell : cellList) {
				// 获取对应单元格数据
				Cell c = getCell(cell, tem, wbModule, tempFilePath);
				ExcelUtil2.setValue(wsheet, c.getLine(), c.getColumn(),
						dataMap.get(cell), c.getCellStyle());
			}
		}
	}

	/**
	 * Excel文件读值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public Object getValue(String tempFilePath, String cell, int sheet,
			File excelFile) throws IOException {
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 模板工作区
		Workbook temWorkbook = getTempWorkbook(tempFilePath);
		// 数据工作区
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 获取对应单元格数据
		Cell c = getCell(cell, tem, temWorkbook, tempFilePath);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		return ExcelUtil2.getCellValue(dataSheet, c.getLine(), c.getColumn());
	}

	/**
	 * 读值列表值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getListValue(String tempFilePath,
			List<String> cellList, int sheet, File excelFile)
			throws IOException {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 获取数据填充开始行
		int startCell = Integer.parseInt((String) tem.get("STARTCELL"));
		// 将Excel文件转换为工作区间
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		// 文件最后一行
		int lastLine = dataSheet.getLastRowNum();

		for (int i = startCell; i <= lastLine; i++) {
			dataList.add(getListLineValue(i, tempFilePath, cellList, sheet,
					excelFile));
		}
		return dataList;
	}

	/**
	 * 读值一行列表值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> getListLineValue(int line, String tempFilePath,
			List<String> cellList, int sheet, File excelFile)
			throws IOException {
		Map<String, Object> lineMap = new HashMap<String, Object>();
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 按模板为写入板
		Workbook temWorkbook = getTempWorkbook(tempFilePath);
		// 将Excel文件转换为工作区间
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		for (String cell : cellList) {
			// 获取对应单元格数据
			Cell c = getCell(cell, tem, temWorkbook, tempFilePath);
			lineMap.put(cell,
					ExcelUtil2.getCellValue(dataSheet, line, c.getColumn()));
		}
		return lineMap;
	}

	/**
	 * 获得模板输入流
	 * 
	 * @param tempFilePath
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileInputStream getFileInputStream(String tempFilePath)
			throws FileNotFoundException {
		if (!tempStream.containsKey(tempFilePath)) {
			tempStream.put(tempFilePath, new FileInputStream(tempFilePath));
		}

		return tempStream.get(tempFilePath);
	}

	/**
	 * 获得输入工作区
	 * 
	 * @param tempFilePath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Workbook getTempWorkbook(String tempFilePath)
			throws FileNotFoundException, IOException {
		if (!tempWorkbook.containsKey(tempFilePath)) {
			if (tempFilePath.endsWith(".xlsx")) {
				tempWorkbook.put(tempFilePath, new XSSFWorkbook(
						getFileInputStream(tempFilePath)));
			} else if (tempFilePath.endsWith(".xls")) {
				tempWorkbook.put(tempFilePath, new HSSFWorkbook(
						getFileInputStream(tempFilePath)));
			}
		}
		return tempWorkbook.get(tempFilePath);
	}

	/**
	 * 获取对应单元格样式等数据数据
	 * 
	 * @param cell
	 * @param tem
	 * @param wbModule
	 * @param tempFilePath
	 * @return
	 */
	private Cell getCell(String cell, HashMap tem, Workbook wbModule,
			String tempFilePath) {
		if (!cellMap.get(tempFilePath).containsKey(cell)) {
			Cell c = new Cell();

			int[] pos = ExcelUtil2.getPos(tem, cell);
			if (pos.length > 1) {
				c.setLine(pos[1]);
			}
			c.setColumn(pos[0]);
			c.setCellStyle((ExcelUtil2.getStyle(tem, cell, wbModule)));
			cellMap.get(tempFilePath).put(cell, c);
		}
		return cellMap.get(tempFilePath).get(cell);
	}

	/**
	 * 获取模板数据
	 * 
	 * @param tempFilePath
	 *            模板文件路径
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	private HashMap getTemp(String tempFilePath, int sheet) throws IOException {
		if (!tempFileMap.containsKey(tempFilePath)) {
			tempFileMap.put(tempFilePath,
					ExcelUtil2.getTemplateFile(tempFilePath));
			cellMap.put(tempFilePath, new HashMap<String, Cell>());
		}
		return tempFileMap.get(tempFilePath)[sheet];
	}

	/**
	 * 资源关闭
	 * 
	 * @param tempFilePath
	 *            模板文件路径
	 * @param os
	 *            输出流
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeAndClose(String tempFilePath, OutputStream os)
			throws FileNotFoundException, IOException {
		if (getTempWorkbook(tempFilePath) != null) {
			getTempWorkbook(tempFilePath).write(os);
			tempWorkbook.remove(tempFilePath);
		}
		if (getFileInputStream(tempFilePath) != null) {
			getFileInputStream(tempFilePath).close();
			tempStream.remove(tempFilePath);
		}
	}

	/**
	 * 获得读取数据工作间
	 * 
	 * @param tempFilePath
	 * @param excelFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Workbook getDataWorkbook(String tempFilePath, File excelFile)
			throws FileNotFoundException, IOException {
		if (!dataWorkbook.containsKey(tempFilePath)) {
			if (tempFilePath.endsWith(".xlsx")) {
				dataWorkbook.put(tempFilePath, new XSSFWorkbook(
						new FileInputStream(excelFile)));
			} else if (tempFilePath.endsWith(".xls")) {
				dataWorkbook.put(tempFilePath, new HSSFWorkbook(
						new FileInputStream(excelFile)));
			}
		}
		return dataWorkbook.get(tempFilePath);
	}

	/**
	 * 读取数据后关闭
	 * 
	 * @param tempFilePath
	 */
	public void readClose(String tempFilePath) {
		dataWorkbook.remove(tempFilePath);
	}

	public static void main(String args[]) throws IOException {
		// String tempFilePath =
		// ExcelHandle.class.getResource("test.xlsx").getPath();

		System.out.println("读取写入的数据0----------------------------------%%%");
		String tempFilePath = "D://test.xlsx";
		List<String> dataListCell = new ArrayList<String>();
		dataListCell.add("names");
		dataListCell.add("ages");
		dataListCell.add("sexs");
		dataListCell.add("deses");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<22;i++){
			
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("names", "names");
		map.put("ages", 11);
		map.put("sexs", "男");
		map.put("deses", "测试");
		dataList.add(map);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("names", "names");
		map1.put("ages", 12);
		map1.put("sexs", "男");
		map1.put("deses", "测试1");
		dataList.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("names", "names");
		map2.put("ages", "cc");
		map2.put("sexs", "女");
		map2.put("deses", "测试2");
		dataList.add(map2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("names", "names3");
		map3.put("ages", "dd");
		map3.put("sexs", "男");
		map3.put("deses", "测试3");
		dataList.add(map3);
		}
		ExcelHandle handle = new ExcelHandle();
		handle.writeListData(tempFilePath, dataListCell, dataList, 0);

		List<String> dataCell = new ArrayList<String>();
		dataCell.add("name");
		dataCell.add("age");
		dataCell.add("sex");
		dataCell.add("des");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name", "name");
		dataMap.put("age", "ee");
		dataMap.put("sex", "女");
		dataMap.put("des", "测试");

		handle.writeData(tempFilePath, dataCell, dataMap, 0);

		File file = new File("d:/data.xlsx");
		OutputStream os = new FileOutputStream(file);
		// 写到输出流并关闭资源
		handle.writeAndClose(tempFilePath, os);

		os.flush();
		os.close();

		System.out.println("读取写入的数据----------------------------------%%%");
		System.out.println("name:"
				+ handle.getValue(tempFilePath, "name", 0, file));
		System.out.println("age:"
				+ handle.getValue(tempFilePath, "age", 0, file));
		System.out.println("sex:"
				+ handle.getValue(tempFilePath, "sex", 0, file));
		System.out.println("des:"
				+ handle.getValue(tempFilePath, "des", 0, file));
		System.out.println("读取写入的列表数据----------------------------------%%%");
		List<Map<String, Object>> list = handle.getListValue(tempFilePath,
				dataListCell, 0, file);
		for (Map<String, Object> data : list) {
			for (String key : data.keySet()) {
				System.out.print(key + ":" + data.get(key) + "--");
			}
			System.out.println("");
		}

		handle.readClose(tempFilePath);
	}
	
	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param cellLine
	 * @param startRow
	 * @param endRow
	 * @param workbook
	 * @param mergeBasis
	 */
	private static void mergedRegion(Sheet sheet, int cellLine,
			int startRow, int endRow, Workbook workbook,
			Integer[] mergeBasis) {
//		HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
//		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平
		String s_will = sheet.getRow(startRow).getCell(cellLine)
				.getStringCellValue(); // 获取第一行的数据,以便后面进行比较
		int count = 0;
		Set<Integer> set = new HashSet<Integer>();
		CollectionUtils.addAll(set, mergeBasis);
		for (int i = 2; i <= endRow; i++) {
			String s_current = sheet.getRow(i).getCell(cellLine)
					.getStringCellValue();
			if (s_will.equals(s_current)) {
				boolean isMerge = true;
				if (!set.contains(cellLine)) {// 如果不是作为基准列的列 需要所有基准列都相同
					for (int j = 0; j < mergeBasis.length; j++) {
						if (!sheet
								.getRow(i)
								.getCell(mergeBasis[j])
								.getStringCellValue()
								.equals(sheet.getRow(i - 1)
										.getCell(mergeBasis[j])
										.getStringCellValue())) {
							isMerge = false;
						}
					}
				} else {// 如果作为基准列的列 只需要比较列号比本列号小的列相同
					for (int j = 0; j < mergeBasis.length
							&& mergeBasis[j] < cellLine; j++) {
						if (!sheet
								.getRow(i)
								.getCell(mergeBasis[j])
								.getStringCellValue()
								.equals(sheet.getRow(i - 1)
										.getCell(mergeBasis[j])
										.getStringCellValue())) {
							isMerge = false;
						}
					}
				}
				if (isMerge) {
					count++;
				} else {
					sheet.addMergedRegion(new CellRangeAddress(startRow,
							startRow + count, cellLine, cellLine));
					startRow = i;
					s_will = s_current;
					count = 0;
				}
			} else {
				sheet.addMergedRegion(new CellRangeAddress(startRow, startRow
						+ count, cellLine, cellLine));
				startRow = i;
				s_will = s_current;
				count = 0;
			}
			if (i == endRow && count > 0) {
				sheet.addMergedRegion(new CellRangeAddress(startRow, startRow
						+ count, cellLine, cellLine));
			}
		}
	}	
}
