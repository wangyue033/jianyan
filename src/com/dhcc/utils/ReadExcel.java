package com.dhcc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.dhcc.inspect.domain.JdComp;
import com.dhcc.inspect.domain.JdProductInfo;

import framework.dhcc.db.DBFacade;

public class ReadExcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		readExcelToObj("D:\\数据铁笼\\贵州省质监局\\监管平台\\数据字典2\\产品信息-4.11.xls");
	}

	/**
	 * 读取excel数据
	 * 
	 * @param path
	 */
	private static void readExcelToObj(String path) {

		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(new File(path));
			readExcel(wb, 0, 1, 0);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取excel文件
	 * 
	 * @param wb
	 * @param sheetIndex
	 *            sheet页下标：从0开始
	 * @param startReadLine
	 *            开始读取的行:从0开始
	 * @param tailLine
	 *            去除最后读取的行
	 */
	private static void readExcel(Workbook wb, int sheetIndex,
			int startReadLine, int tailLine) {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		Row row = null;
		ArrayList<JdProductInfo> list = new ArrayList<JdProductInfo>();
		for (int i = startReadLine; i < 127; i++) {
			JdProductInfo comp = new JdProductInfo();
			row = sheet.getRow(i);
			int index = 1;
			for (Cell c : row) {
				boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
				// 判断是否具有合并单元格
				if (isMerge) {
					String rs = getMergedRegionValue(sheet, row.getRowNum(),
							c.getColumnIndex());
					// System.out.print(rs + "  ");
				} else {
					// System.out.print(c.getRichStringCellValue() + "  ");
				}
				switch (index) {
				case 1:
					comp.setProductId(c.getRichStringCellValue() + "");
					break;
				case 2:
					comp.setProductName(c.getRichStringCellValue() + "");
					break;
				case 3:
					comp.setTypeId(c.getRichStringCellValue() + "");
					break;
				case 4:
					comp.setEnterpriseId(c.getRichStringCellValue() + "");
					break;
				case 5:
					comp.setProductStandard(c.getRichStringCellValue() + "");
					break;

				/*
				 * case 21: comp.setOrgCode(c.getRichStringCellValue() + "");
				 * break; case 22: comp.setOrgCode(c.getRichStringCellValue() +
				 * ""); break;
				 */
				}
				index++;
			}
			// System.out.println();
			list.add(comp);
		}
		String[] sqls = new String[list.size()];
		Object[][] params = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			JdProductInfo bean = (JdProductInfo) list.get(i);
			sqls[i] = " insert into JD_ENTERPRISE_PRODUCT(product_id,type_id,enterprise_id,product_name,product_standard)values(?,?,?,?,?)";
			params[i] = new String[] { bean.getProductId(), bean.getTypeId(),
					bean.getEnterpriseId(), bean.getProductName(),
					bean.getProductStandard() };
		}
		System.out.println("list size=" + list.size());
		DBFacade.getInstance().execute(sqls,params);
	}

	private static void readExcel2(Workbook wb, int sheetIndex,
			int startReadLine, int tailLine) {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		Row row = null;
		ArrayList<JdComp> list = new ArrayList<JdComp>();
		for (int i = startReadLine; i < 501; i++) {
			JdComp comp = new JdComp();
			row = sheet.getRow(i);
			int index = 1;
			for (Cell c : row) {
				boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
				// 判断是否具有合并单元格
				if (isMerge) {
					String rs = getMergedRegionValue(sheet, row.getRowNum(),
							c.getColumnIndex());
					// System.out.print(rs + "  ");
				} else {
					// System.out.print(c.getRichStringCellValue() + "  ");
				}
				switch (index) {
				case 1:
					comp.setEnterpriseId(c.getRichStringCellValue() + "");
					break;
				case 2:
					comp.setOrgCode(c.getRichStringCellValue() + "");
					break;
				case 3:
					comp.setEnterpriseName(c.getRichStringCellValue() + "");
					break;
				case 4:
					comp.setAddress(c.getRichStringCellValue() + "");
					break;
				case 5:
					comp.setDomicile(c.getRichStringCellValue() + "");
					break;
				case 6:
					comp.setAreaId(c.getRichStringCellValue() + "");
					break;
				case 7:
					comp.setEconomyType(c.getRichStringCellValue() + "");
					break;
				case 8:
					//
					comp.setModifyOptTime(c.getRichStringCellValue() + "");
					break;
				case 9:
					comp.setEpScale(c.getRichStringCellValue() + "");
					break;
				case 10:
					comp.setIsScale(c.getRichStringCellValue() + "");
					break;
				case 11:
					comp.setLegalRep(c.getRichStringCellValue() + "");
					break;
				case 12:

					comp.setTelephone(c.getRichStringCellValue() + "");
					break;
				case 13:
					comp.setMobile(c.getRichStringCellValue() + "");
					break;
				case 14:
					comp.setRegCapital(c.getRichStringCellValue() + "");
					break;
				case 15:
					comp.setEpStatus(c.getRichStringCellValue() + "");
					break;
				case 16:
					comp.setIsProd(c.getRichStringCellValue() + "");
					break;
				case 17:
					comp.setOrgId(c.getRichStringCellValue() + "");
					break;
				case 18:
					comp.setOptId(c.getRichStringCellValue() + "");
					break;
				case 19:
					comp.setOptTime(c.getRichStringCellValue() + "");
					break;
				case 20:
					comp.setIsElect(c.getRichStringCellValue() + "");
					break;
				case 21:
					comp.setYearValue(c.getRichStringCellValue() + "");
					break;
				/*
				 * case 21: comp.setOrgCode(c.getRichStringCellValue() + "");
				 * break; case 22: comp.setOrgCode(c.getRichStringCellValue() +
				 * ""); break;
				 */
				}
				index++;
			}
			// System.out.println();
			list.add(comp);
		}
		String[] sqls = new String[list.size()];
		String[][] params = new String[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			JdComp bean = (JdComp) list.get(i);
			sqls[i] = " insert into jd_enterprise_info(enterprise_id,area_id,enterprise_name,org_code,address,domicile,legal_rep,telephone,mobile,"
					+ "reg_capital,build_date,ep_scale,is_scale,economy_type,ep_status,is_prod,is_elect,year_value)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			params[i] = new String[] { bean.getEnterpriseId(),
					bean.getAreaId(), bean.getEnterpriseName(),
					bean.getOrgCode(), bean.getAddress(), bean.getDomicile(),
					bean.getLegalRep(), bean.getTelephone(), bean.getMobile(),
					bean.getRegCapital(), bean.getBuildDate(),
					bean.getEpScale(), bean.getIsScale(),
					bean.getEconomyType(), bean.getEpStatus(),
					bean.getIsProd(), bean.getIsElect(), bean.getYearValue() };
		}
		DBFacade.getInstance().execute(sqls, params);
		System.out.println("list size=" + list.size());
	}

	/**
	 * 获取合并单元格的值
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}

		return null;
	}

	/**
	 * 判断合并了行
	 * 
	 * @param sheet
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean isMergedRow(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row == firstRow && row == lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断指定的单元格是否是合并单元格
	 * 
	 * @param sheet
	 * @param row
	 *            行下标
	 * @param column
	 *            列下标
	 * @return
	 */
	private static boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断sheet页中是否含有合并单元格
	 * 
	 * @param sheet
	 * @return
	 */
	private static boolean hasMerged(Sheet sheet) {
		return sheet.getNumMergedRegions() > 0 ? true : false;
	}

	/**
	 * 合并单元格
	 * 
	 * @param sheet
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param lastCol
	 *            结束列
	 */
	private static void mergeRegion(Sheet sheet, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
				lastCol));
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {

		if (cell == null)
			return "";

		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}

}
