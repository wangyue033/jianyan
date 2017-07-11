package com.dhcc.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

/**
 * @author zhanghk
 */
public class ExcelUtil_Extend {
	/**
	 * @param fileName
	 *            文件名称
	 * @param headers
	 *            表头
	 * @param dataset
	 *            数据集
	 * @param isSortDataSet
	 *            是否对数据排序
	 * @param response
	 *            HttpServletResponse
	 * @param mergeBasis
	 *            合并基准列 可选
	 * @param mergeCells
	 *            要合并的列 可选
	 * @param sumCellsMap
	 *            要求和的列 可选
	 * @param timeCells
	 *            时间列 可选
	 * @throws IOException
	 */
	public static void exportExelMerge(String fileName, final String[] headers,
			List<String[]> dataset, boolean isSortDataSet,
			HttpServletResponse response, final Integer[] mergeBasis,
			final Integer[] mergeCells, final Integer[] sumCells,
			final Integer[] timeCells) throws IOException {
		String title = "Sheet1";
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename="
				+ URLEncoder.encode(fileName, "utf-8"));

		createExcelMerge(title, headers, dataset, isSortDataSet,
				response.getOutputStream(), mergeBasis, mergeCells, sumCells,
				timeCells);

		response.setStatus(HttpServletResponse.SC_OK);
		response.flushBuffer();
	}

	/**
	 * @param title
	 *            文件名称
	 * @param headers
	 *            表头
	 * @param dataset
	 *            数据集
	 * @param isSortDataSet
	 *            是否对数据排序
	 * @param out
	 *            OutputStream
	 * @param mergeBasis
	 *            合并基准列 可选
	 * @param mergeCells
	 *            要合并的列
	 * @param sumCells
	 *            要求和的列
	 * @param timeCells
	 *            时间列 可选
	 */
	public static void createExcelMerge(String title, final String[] headers,
			List<String[]> dataset, boolean isSortDataSet, OutputStream out,
			final Integer[] mergeBasis, final Integer[] mergeCells,
			final Integer[] sumCells, final Integer[] timeCells) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);

		sheet.setDefaultColumnWidth(15); // 设置表格默认列宽度为15个字节

		HSSFCellStyle headStyle = createHeadStyle(workbook); // 生成头部样式
		HSSFCellStyle commonDataStyle = createCommonDataStyle(workbook); // 生成一般数据样式
		HSSFCellStyle numStyle = createNumStyle(workbook); // 生成数字类型保留两位小数样式
		HSSFCellStyle sumRowStyle = createSumRowStyle(workbook); // 生成合计行样式

		if (headers == null || headers.length <= 0) {
			return;
		}

		HSSFRow row = sheet.createRow(0); // 产生表格标题行
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		if (isSortDataSet && mergeBasis != null && mergeBasis.length > 0) { // 是否排序数据
			Collections.sort(dataset, new Comparator<String[]>() {
				public int compare(String[] o1, String[] o2) {
					String s1 = "";
					String s2 = "";
					for (int i = 0; i < mergeBasis.length; i++) {
						s1 += (o1[mergeBasis[i].intValue()] + Character
								.valueOf((char) 127).toString());
						s2 += (o2[mergeBasis[i].intValue()] + Character
								.valueOf((char) 127).toString());
					}
					if (timeCells != null && timeCells.length > 0) {
						for (int i = 0; i < timeCells.length; i++) {
							s1 += o1[timeCells[i].intValue()];
							s2 += o2[timeCells[i].intValue()];
						}
					}
					if (s1.compareTo(s2) < 0) {
						return -1;
					} else if (s1.compareTo(s2) == 0) {
						return 0;
					} else {
						return 1;
					}
				}
			});
		}
		// 遍历集合数据，产生数据行
		Iterator<String[]> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			String[] dataSources = it.next();
			for (int i = 0; i < dataSources.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(commonDataStyle);
				cell.setCellValue(dataSources[i]);
			}
		}
		try {
			if (mergeBasis != null && mergeBasis.length > 0
					&& mergeCells != null && mergeCells.length > 0) {
				for (int i = 0; i < mergeCells.length; i++) {
					mergedRegion(sheet, mergeCells[i], 1,
							sheet.getLastRowNum(), workbook, mergeBasis);
				}
			}
			if (sumCells != null && sumCells.length > 0) {
				createSumRow(sheet, row, headers, sumCells, sumRowStyle,
						numStyle);
			}
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建合计行
	 * 
	 * @param sheet
	 * @param row
	 * @param headers
	 * @param sumCells
	 * @param sumRowStyle
	 * @param numStyle
	 */
	private static void createSumRow(HSSFSheet sheet, HSSFRow row,
			final String[] headers, final Integer[] sumCells,
			HSSFCellStyle sumRowStyle, HSSFCellStyle numStyle) {
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(sumRowStyle);
		}
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			for (int j = 0; j < sumCells.length; j++) {
				sheet.getRow(i)
						.getCell(sumCells[j])
						.setCellValue(
								Double.parseDouble(sheet.getRow(i)
										.getCell(sumCells[j])
										.getStringCellValue()));
				sheet.getRow(i).getCell(sumCells[j]).setCellStyle(numStyle);
			}
		}
		HSSFCell sumCell = row.getCell(0);
		sumCell.setCellValue("合计：");
		String sumFunctionStr = null;
		for (int i = 0; i < sumCells.length; i++) {
			sumFunctionStr = "SUM("
					+ CellReference.convertNumToColString(sumCells[i]) + "2:"
					+ CellReference.convertNumToColString(sumCells[i])
					+ sheet.getLastRowNum() + ")";
			row.getCell(sumCells[i]).setCellFormula(sumFunctionStr);
		}
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
	private static void mergedRegion(HSSFSheet sheet, int cellLine,
			int startRow, int endRow, HSSFWorkbook workbook,
			Integer[] mergeBasis) {
		HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平
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

	/**
	 * 标题单元格样式
	 * 
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle createHeadStyle(HSSFWorkbook workbook) {
		// 标题单元格样式
		HSSFCellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 标题单元格字体
		HSSFFont headFont = workbook.createFont();
		headFont.setColor(HSSFColor.VIOLET.index);
		headFont.setFontHeightInPoints((short) 12);
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		headStyle.setFont(headFont);
		return headStyle;
	}

	/**
	 * 合计行单元格样式
	 * 
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle createSumRowStyle(HSSFWorkbook workbook) {
		// 合计行单元格样式
		HSSFCellStyle sumRowStyle = workbook.createCellStyle();
		sumRowStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		sumRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		sumRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		sumRowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		sumRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		sumRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		sumRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		sumRowStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		// 合计行单元格字体
		HSSFFont sumRowFont = workbook.createFont();
		sumRowFont.setColor(HSSFColor.VIOLET.index);
		sumRowFont.setFontHeightInPoints((short) 12);
		sumRowFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		sumRowStyle.setFont(sumRowFont);
		return sumRowStyle;
	}

	/**
	 * 普通数据单元格样式
	 * 
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle createCommonDataStyle(HSSFWorkbook workbook) {
		// 普通数据单元格样式
		HSSFCellStyle commonDataStyle = workbook.createCellStyle();
		commonDataStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		commonDataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		commonDataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		commonDataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		commonDataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		commonDataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		commonDataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		commonDataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 普通数据单元格字体
		HSSFFont commonDataFont = workbook.createFont();
		commonDataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		commonDataStyle.setFont(commonDataFont);
		return commonDataStyle;
	}

	/**
	 * 自定义保留两位小数数字单元格格式
	 * 
	 * @param workbook
	 * @return
	 */
	private static HSSFCellStyle createNumStyle(HSSFWorkbook workbook) {
		// 自定义保留两位小数数字单元格格式
		HSSFCellStyle numStyle = workbook.createCellStyle();
		numStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		numStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		numStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		numStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		numStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		numStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		numStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		numStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		numStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		// 自定义保留两位小数数字单元格字体
		HSSFFont numFont = workbook.createFont();
		numFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		numStyle.setFont(numFont);
		return numStyle;
	}

	public static void main(String[] args) {
		String[] headers = { "大区", "部门", "金额", "数量", "日期" };
		List<String[]> dataset = new ArrayList<String[]>();
		dataset.add(new String[] { "华东", "部门3", "35", "1", "2015-01-01" });
		dataset.add(new String[] { "华北", "部门1", "20", "1", "2015-01-02" });
		dataset.add(new String[] { "华北", "部门2", "25", "1", "2015-01-03" });
		dataset.add(new String[] { "华北", "部门5", "25", "1", "2015-01-04" });
		dataset.add(new String[] { "华南", "部门1", "15", "1", "2015-01-05" });
		dataset.add(new String[] { "华北", "部门3", "30", "1", "2015-01-06" });
		dataset.add(new String[] { "华北", "部门3", "30", "1", "2015-01-07" });
		dataset.add(new String[] { "华东", "部门1", "25", "1", "2015-01-08" });
		dataset.add(new String[] { "华南", "部门4", "30", "1", "2015-01-09" });
		dataset.add(new String[] { "华东", "部门2", "25", "1", "2015-01-10" });
		dataset.add(new String[] { "华东", "部门2", "25", "1", "2015-01-11" });
		dataset.add(new String[] { "华东", "部门3", "35", "1", "2015-01-12" });
		dataset.add(new String[] { "华南", "部门1", "15", "1", "2015-01-13" });
		dataset.add(new String[] { "华北", "部门6", "20", "1", "2015-01-14" });
		dataset.add(new String[] { "华南", "部门2", "25", "1", "2015-01-15" });
		dataset.add(new String[] { "华南", "部门2", "25", "1", "2015-01-16" });
		dataset.add(new String[] { "华东", "部门1", "25", "1", "2015-01-17" });
		dataset.add(new String[] { "华南", "部门8", "30", "1", "2015-01-18" });
		dataset.add(new String[] { "华东", "部门2", "35", "1", "2015-01-01" });
		dataset.add(new String[] { "华北", "部门1", "20", "1", "2015-01-02" });
		dataset.add(new String[] { "华北", "部门11", "25", "1", "2015-01-03" });
		dataset.add(new String[] { "华北", "部门2", "25", "1", "2015-01-04" });
		dataset.add(new String[] { "华南", "部门1", "15", "1", "2015-01-05" });
		dataset.add(new String[] { "华北", "部门4", "30", "1", "2015-01-06" });
		dataset.add(new String[] { "华北", "部门3", "30", "1", "2015-01-07" });
		dataset.add(new String[] { "华东", "部门9", "25", "1", "2015-01-08" });
		dataset.add(new String[] { "华南", "部门3", "30", "1", "2015-01-09" });
		dataset.add(new String[] { "华东", "部门12", "25", "1", "2015-01-10" });
		dataset.add(new String[] { "华东", "部门2", "25", "1", "2015-01-11" });
		dataset.add(new String[] { "华东", "部门12", "35", "1", "2015-01-12" });
		dataset.add(new String[] { "华南", "部门1", "15", "1", "2015-01-13" });
		dataset.add(new String[] { "华北", "部门11", "20", "1", "2015-01-14" });
		dataset.add(new String[] { "华南", "部门21", "25", "1", "2015-01-15" });
		dataset.add(new String[] { "华南", "部门2", "25", "1", "2015-01-16" });
		dataset.add(new String[] { "华东", "部门15", "25", "1", "2015-01-17" });
		dataset.add(new String[] { "华南", "部门3", "30", "1", "2015-01-18" });
		dataset.add(new String[] { "华东", "部门3", "35", "1", "2015-01-01" });
		dataset.add(new String[] { "华北", "部门17", "20", "1", "2015-01-02" });
		dataset.add(new String[] { "华北", "部门22", "25", "1", "2015-01-03" });
		dataset.add(new String[] { "华北", "部门2", "25", "1", "2015-01-04" });
		dataset.add(new String[] { "华南", "部门1", "15", "1", "2015-01-05" });
		dataset.add(new String[] { "华北", "部门2", "30", "1", "2015-01-06" });
		dataset.add(new String[] { "华北", "部门3", "30", "1", "2015-01-07" });
		dataset.add(new String[] { "华东", "部门1", "25", "1", "2015-01-08" });
		dataset.add(new String[] { "华南", "部门8", "30", "1", "2015-01-09" });
		dataset.add(new String[] { "华东", "部门2", "25", "1", "2015-01-10" });
		dataset.add(new String[] { "华东", "部门2", "25", "1", "2015-01-11" });
		dataset.add(new String[] { "华东", "部门3", "35", "1", "2015-01-12" });
		dataset.add(new String[] { "华南", "部门7", "15", "1", "2015-01-13" });
		dataset.add(new String[] { "华北", "部门1", "20", "1", "2015-01-14" });
		dataset.add(new String[] { "华南", "部门2", "25", "1", "2015-01-15" });
		dataset.add(new String[] { "华南", "部门2", "25", "1", "2015-01-16" });
		dataset.add(new String[] { "华东", "部门9", "25", "1", "2015-01-17" });
		dataset.add(new String[] { "华南", "部门3", "30", "1", "2015-01-18" });
		try {
			OutputStream out = new FileOutputStream("D://a.xls");
			ExcelUtil_Extend.createExcelMerge("测试.xls", headers, dataset, true,
					out, new Integer[] { 0, 1 }, new Integer[] { 0, 1 },
					new Integer[] { 2, 3 }, new Integer[] { 4 });
			out.close();
			//JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
