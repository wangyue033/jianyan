package com.dhcc.popedom.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.dhcc.login.impl.Menu;
import com.dhcc.popedom.domain.PdInitBean;
import com.dhcc.popedom.util.PopedomInitSaxParser;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.conf.SystemConfig;
import framework.dhcc.db.DBFacade;

public class PdInitAction extends ActionSupport implements
		ModelDriven<PdInitBean>, ServletRequestAware {

	private static final long serialVersionUID = -1480207690116146367L;

	PdInitBean bean = new PdInitBean();

	HttpServletRequest request;

	List<PdInitBean> dataRows = new ArrayList<PdInitBean>();

	Menu impl = new Menu();

	public PdInitBean getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String PdInit_list() {
		List<Map<String, String>> menuList = SystemConfig.getInstance()
				.getMenuList();
		Set<String> menuSet = new HashSet<String>();
		String sql = "select menu_id from p_menu where 1= 1";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
		try {
			while (crs.next()) {
				menuSet.add(crs.getString("menu_Id"));
			}
			for (int i = 0; i < menuList.size(); i++) {
				Map<String, String> map = menuList.get(i);
				String menuId = map.get("menuId");
				if (menuSet != null && menuSet.contains(menuId)) {
					dataRows.add(new PdInitBean(map.get("menuId"), map
							.get("menuName"), map.get("menuPath"), "1"));
				} else {
					dataRows.add(new PdInitBean(map.get("menuId"), map
							.get("menuName"), map.get("menuPath"), "0"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "PdInit_list";
	}

	public String PdInit_init() {
		String forward = "PdInit_init";
		int oper = bean.getOper();
		if (oper == 0) {
			addActionMessage("");
		}
		if (oper == 1) {
			String menuIds = bean.getMenuId();
			try {
				long beginTime = System.currentTimeMillis();
				new PopedomInitSaxParser(menuIds, "INIT");
				long endTime = System.currentTimeMillis();
				System.out.println("初始化权限共用时间=" + (endTime - beginTime) + "ms");
				addActionMessage(getText("PdInit.init.success"));
				request.setAttribute("result", "Y");
			} catch (Exception se) {
				addActionMessage(getText("PdInit.init.failure"));
				request.setAttribute("result", "N");
				se.printStackTrace();
			}
		}
		return forward;
	}

	public String PdInit_add() {
		int oper = bean.getOper();
		if (oper == 0) {
			addActionMessage("");
		}
		if (oper == 1) {
			String menuIds = bean.getMenuId();
			try {
				long beginTime = System.currentTimeMillis();
				new PopedomInitSaxParser(menuIds, "ADD");
				long endTime = System.currentTimeMillis();
				System.out.println("追加权限共用时间=" + (endTime - beginTime) + "ms");
				addActionMessage(getText("PdInit.add.success"));
				request.setAttribute("result", "Y");
			} catch (Exception se) {
				addActionMessage(getText("PdInit.add.failure"));
				request.setAttribute("result", "N");
				se.printStackTrace();
			}
		}
		return "PdInit_add";
	}

	public String PdInit_code() {
		if (bean.getOper() == 0) {
			addActionMessage("");
		} else if (bean.getOper() == 1) {
			String tableName = bean.getTableName();
			// String sql = "select column_name from user_tab_columns where
			// Table_Name = '" + tableName.toString().toUpperCase() + "'";
			String sql = "select COLUMN_NAME from information_schema.COLUMNS where table_name ='"
					+ tableName
					+ "' and table_schema= '"
					+ bean.getSchemaName() + "' ";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql, null);
			String column_name = "";
			String listResult = "";
			String defaultValue = "";
			String procValue = "";
			String beanValue = "";
			String wh = "";
			try {
				while (crs.next()) {
					String column = crs.getString("column_name");
					column_name = column_name + column + ",";
					String[] cols = column.split("_");
					String setColsVal = "";
					String colsVal = "";
					for (int i = 0; i < cols.length; i++) {
						setColsVal += cols[i].substring(0, 1).toUpperCase()
								+ cols[i].substring(1, cols[i].length())
										.toLowerCase();
						if (i == 0) {
							colsVal += cols[i].substring(0, 1).toLowerCase()
									+ cols[i].substring(1, cols[i].length())
											.toLowerCase();
						} else {
							colsVal += cols[i].substring(0, 1).toUpperCase()
									+ cols[i].substring(1, cols[i].length())
											.toLowerCase();
						}
					}
					defaultValue += "this." + colsVal + " = \"\";\n";
					procValue += "proc.setString(\"" + colsVal + "\", bean.get"
							+ setColsVal + "().trim());\n";
					listResult = listResult + "bean.set" + setColsVal
							+ "(crs.getString(\"" + column.toLowerCase()
							+ "\"));\n";
					beanValue = beanValue + "bean.get" + setColsVal + "(), ";
					wh = wh + "?,";
				}
				column_name = column_name
						.substring(0, column_name.length() - 1);
			} catch (Exception se) {
				se.printStackTrace();
			}
			column_name = column_name.toLowerCase();
			String[] strs = column_name.split("_");
			String result = "";
			for (int i = 0; i < strs.length; i++) {
				if (i == 0) {
					result = strs[i];
				} else {
					result = result + strs[i].substring(0, 1).toUpperCase()
							+ strs[i].substring(1, strs[i].length());
				}
			}
			System.out.println(column_name);
			System.out.println(beanValue);

			String coloumn_field = "";
			if (bean.getAliasName().trim() != null
					&& bean.getAliasName().trim().length() > 0) {
				coloumn_field = bean.getAliasName().trim().toLowerCase()
						+ "."
						+ column_name.replaceAll(",", ","
								+ bean.getAliasName().trim().toLowerCase()
								+ ".");
			} else {
				coloumn_field = column_name;
			}
			String updateSql = "update " + tableName.toLowerCase() + " set "
					+ column_name.replaceAll(",", "=?,") + "=? where ";
			String insertSql = "insert into " + tableName.toLowerCase() + "("
					+ column_name + ") values ("
					+ wh.substring(0, wh.length() - 1) + ")";
			System.out.println(updateSql);
			System.out.println(insertSql);
			bean.setTableName(tableName.toLowerCase());
			bean.setColumnBean(result);
			bean.setColumnName(column_name);
			bean.setColumnField(coloumn_field);
			bean.setBeanValue(beanValue.substring(0,
					beanValue.trim().length() - 1));
			bean.setListValue(listResult);
			StringBuffer outResult = new StringBuffer("");
			outResult = outResult.append("1、使用到bean中\n").append(result);
			outResult = outResult.append("\n2、使用到查询字段\n").append(column_name);
			outResult = outResult.append("\n3、具有别名的查询\n").append(coloumn_field);
			outResult = outResult.append("\n4、新增sql语句，但是需要调整哪些是否使用\n").append(
					insertSql);
			outResult = outResult.append("\n5、修改sql语句，但是需要调整主见顺序\n");
			outResult = outResult.append(updateSql).append("\n6、新增、修改参数\n")
					.append(bean.getBeanValue());
			outResult = outResult.append("\n7、使用到查询、明细\n").append(listResult);
			outResult = outResult.append("\n8、初始化参数\n").append(defaultValue);
			outResult = outResult.append("\n9、存储过程设值\n").append(procValue);
			bean.setListValue(outResult.toString());
		}
		return "PdInit_code";
	}

	public String PdInit_file() {
		if (bean.getOper() == 0) {
			addActionMessage("");
		}else if(bean.getOper() == 1){
			String packagePath = bean.getPackagePath();// com.dhcc.popedom
			String domainName = bean.getDomainName();// MyJob
			String path = bean.getPath();// D:\\file
			String tableName = bean.getTableName();
			String schemaName = bean.getSchemaName();

			String actionName = domainName + "Action";
			String implName = domainName + "Impl";
			String srcPath = path + "\\src";
			String domainPath = srcPath + "\\domain";
			String actionPath = srcPath + "\\action";
			String implPath = srcPath + "\\impl";
			File derec = new File(path);
			File srcDerec = new File(srcPath);
			File domainDerec = new File(domainPath);
			File actionDerec = new File(actionPath);
			File implDerec = new File(implPath);

			if (!derec.exists() && !derec.isDirectory()) {
				derec.mkdir();
			}
			if (!srcDerec.exists() && !srcDerec.isDirectory()) {
				srcDerec.mkdir();
			}
			if (!domainDerec.exists() && !domainDerec.isDirectory()) {
				domainDerec.mkdir();
			}
			if (!actionDerec.exists() && !actionDerec.isDirectory()) {
				actionDerec.mkdir();
			}
			if (!implDerec.exists() && !implDerec.isDirectory()) {
				implDerec.mkdir();
			}

			String domainFile = domainPath + "\\" + domainName + ".java";
			String actionFile = actionPath + "\\" + actionName + ".java";
			String implFile = implPath + "\\" + implName + ".java";
			String aliasName = "";

			try {
				FileOutputStream domainFos = new FileOutputStream(domainFile);
				FileOutputStream actionFos = new FileOutputStream(actionFile);
				FileOutputStream implFos = new FileOutputStream(implFile);

				String column_name = "";
				String listResult = "";
				String defaultValue = "";
				String procValue = "";
				String beanValue = "";
				String wh = "";
				String sql = "select COLUMN_NAME,column_key from information_schema.COLUMNS where table_name ='"
						+ tableName
						+ "' and table_schema= '"
						+ schemaName
						+ "' ";
				CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
						null);
				String pri = "";
				String priColumn = "";
				try {
					while (crs.next()) {
						String column = crs.getString("column_name");
						String priKey = crs.getString("column_key");
						if (priKey != null && "PRI".equals(priKey)) {
							priColumn = column;
							String[] t = column.split("_");
							for (int i = 0; i < t.length; i++) {
								pri += t[i].substring(0, 1).toUpperCase()
										+ t[i].substring(1, t[i].length())
												.toLowerCase();
							}
							pri = "bean.get" + pri + "()";
						}
						column_name = column_name + column + ",";
						String[] cols = column.split("_");
						String setColsVal = "";
						String colsVal = "";
						for (int i = 0; i < cols.length; i++) {
							setColsVal += cols[i].substring(0, 1).toUpperCase()
									+ cols[i].substring(1, cols[i].length())
											.toLowerCase();
							if (i == 0) {
								colsVal += cols[i].substring(0, 1)
										.toLowerCase()
										+ cols[i]
												.substring(1, cols[i].length())
												.toLowerCase();
							} else {
								colsVal += cols[i].substring(0, 1)
										.toUpperCase()
										+ cols[i]
												.substring(1, cols[i].length())
												.toLowerCase();
							}
						}
						defaultValue += "this." + colsVal + " = \"\";\n";
						procValue += "proc.setString(\"" + colsVal
								+ "\", bean.get" + setColsVal + "().trim());\n";
						listResult = listResult + "bean.set" + setColsVal
								+ "(crs.getString(\"" + column.toLowerCase()
								+ "\"));\n";
						beanValue = beanValue + "bean.get" + setColsVal
								+ "(), ";
						wh = wh + "?,";
					}
					column_name = column_name.substring(0,
							column_name.length() - 1);
				} catch (Exception se) {
					se.printStackTrace();
				}
				column_name = column_name.toLowerCase();
				String[] strs = column_name.split("_");
				String result = "";
				for (int i = 0; i < strs.length; i++) {
					if (i == 0) {
						result = strs[i];
					} else {
						result = result + strs[i].substring(0, 1).toUpperCase()
								+ strs[i].substring(1, strs[i].length());
					}
				}

				String coloumn_field = "";
				if (aliasName.trim() != null && aliasName.trim().length() > 0) {
					coloumn_field = aliasName.trim().toLowerCase()
							+ "."
							+ column_name.replaceAll(",", ","
									+ aliasName.trim().toLowerCase() + ".");
				} else {
					coloumn_field = column_name;
				}
				String updateSql = "update " + tableName.toLowerCase()
						+ " set " + column_name.replaceAll(",", "=?,")
						+ "=? where ";
				String insertSql = "insert into " + tableName.toLowerCase()
						+ "(" + column_name + ") values ("
						+ wh.substring(0, wh.length() - 1) + ")";
				bean.setTableName(tableName.toLowerCase());
				bean.setColumnBean(result);
				bean.setColumnName(column_name);
				bean.setColumnField(coloumn_field);
				bean.setBeanValue(beanValue.substring(0, beanValue.trim()
						.length() - 1));
				bean.setListValue(listResult);
				
				StringBuffer outResult = new StringBuffer("");
				outResult = outResult.append("1、使用到bean中\n").append(result);
				outResult = outResult.append("\n2、使用到查询字段\n").append(column_name);
				outResult = outResult.append("\n3、具有别名的查询\n").append(coloumn_field);
				outResult = outResult.append("\n4、新增sql语句，但是需要调整哪些是否使用\n").append(
						insertSql);
				outResult = outResult.append("\n5、修改sql语句，但是需要调整主见顺序\n");
				outResult = outResult.append(updateSql).append("\n6、新增、修改参数\n")
						.append(bean.getBeanValue());
				outResult = outResult.append("\n7、使用到查询、明细\n").append(listResult);
				outResult = outResult.append("\n8、初始化参数\n").append(defaultValue);
				outResult = outResult.append("\n9、存储过程设值\n").append(procValue);
				bean.setListValue(outResult.toString());

				PrintWriter domainPW = new PrintWriter(domainFos);
				domainPW.println("package " + packagePath + ".domain;");
				domainPW.println();
				domainPW.println("import framework.dhcc.utils.BaseBean;");
				domainPW.println();
				domainPW.println("public class " + domainName
						+ " extends BaseBean {");
				domainPW.println();
				domainPW.println("private String " + result + ";");
				domainPW.println();
				domainPW.println("public void reset(){");
				domainPW.println(defaultValue);
				domainPW.println("}");
				domainPW.println("}");
				domainPW.close();

				String mypath = getClass().getResource("").getFile().toString();
				mypath = mypath.substring(0, mypath.length() - 7);
				
				
				PrintWriter actionPW = new PrintWriter(actionFos);
				BufferedReader actionBr = new BufferedReader(new InputStreamReader(
						new FileInputStream(mypath + "actionTemplete.txt"),
						"UTF-8"));
				String str = actionBr.readLine();
				while(str != null){
					str = str.replace("{{packagePath}}", packagePath + ".action");
					str = str.replace("{{domainPath}}", packagePath
							+ ".domain." + domainName);
					str = str.replace("{{implPath}}", packagePath
							+ ".impl." + implName);
					str = str.replace("{{domainName}}", domainName);
					str = str.replace("{{domainPri}}", pri);
					actionPW.println(str);
					str = actionBr.readLine();
				}
				actionPW.close();
				
				BufferedReader implBr = new BufferedReader(new InputStreamReader(
						new FileInputStream(mypath + "implTemplete.txt"),
						"UTF-8"));
				str = implBr.readLine();
				PrintWriter implPW = new PrintWriter(implFos);
				while (str != null) {
					str = str.replace("{{packagePath}}", packagePath + ".impl");
					str = str.replace("{{domainPath}}", packagePath
							+ ".domain." + domainName);
					str = str.replace("{{domainName}}", domainName);
					str = str.replace("{{listResult}}", listResult);
					str = str.replace("{{insertSql}}", insertSql);
					str = str.replace("{{beanValue}}", beanValue);
					str = str.replace("{{domainPri}}", pri);
					str = str.replace("{{updateSql}}", updateSql + priColumn.toLowerCase() + "=?");
					implPW.println(str);
					str = implBr.readLine();
				}

				implPW.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return "PdInit_file";
	}

	public List<PdInitBean> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<PdInitBean> dataRows) {
		this.dataRows = dataRows;
	}

	public static void main(String[] args) {
		String packagePath = "com.dhcc.popedom";
		String domainName = "Staff";
		String actionName = domainName + "Action";
		String implName = domainName + "Impl";
		String path = "D:\\file";
		String domainPath = path + "\\domain";
		String actionPath = path + "\\action";
		String implPath = path + "\\impl";
		File derec = new File(path);
		File domainDerec = new File(domainPath);
		File actionDerec = new File(actionPath);
		File implDerec = new File(implPath);

		if (!derec.exists() && !derec.isDirectory()) {
			derec.mkdir();
		}
		if (!domainDerec.exists() && !domainDerec.isDirectory()) {
			domainDerec.mkdir();
		}
		if (!actionDerec.exists() && !actionDerec.isDirectory()) {
			actionDerec.mkdir();
		}
		if (!implDerec.exists() && !implDerec.isDirectory()) {
			implDerec.mkdir();
		}

		String domainFile = domainPath + "\\" + domainName + ".java";
		String actionFile = actionPath + "\\" + actionName + ".java";
		String implFile = implPath + "\\" + implName + ".java";

		try {
			FileOutputStream domainFos = new FileOutputStream(domainFile);
			FileOutputStream actionFos = new FileOutputStream(actionFile);
			FileOutputStream implFos = new FileOutputStream(implFile);

			PrintWriter domainPW = new PrintWriter(domainFos);
			PrintWriter actionPW = new PrintWriter(actionFos);
			PrintWriter implPW = new PrintWriter(implFos);

			domainPW.println("package " + packagePath + ".domain;");
			domainPW.println();
			domainPW.println("import framework.dhcc.utils.BaseBean;");
			domainPW.println();
			domainPW.println("public class " + domainName
					+ " extends BaseBean {");
			domainPW.println();
			domainPW.println("}");

			domainPW.close();
			actionPW.close();
			implPW.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
