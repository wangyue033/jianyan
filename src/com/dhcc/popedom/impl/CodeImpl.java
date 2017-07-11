package com.dhcc.popedom.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.dhcc.popedom.domain.Code;
import com.dhcc.popedom.domain.ColumnInfo;
import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;

public class CodeImpl {

	public List<ColumnInfo> getColumnInfoList(String schemaName,
			String tableName) throws Exception {
		List<ColumnInfo> list = new ArrayList<ColumnInfo>();
		String sql = "select column_name,column_type,column_key,column_comment from information_schema.COLUMNS "
				+ "where table_schema=? and table_name=?";
		CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
				new String[] { schemaName, tableName });
		while (crs.next()) {
			ColumnInfo bean = new ColumnInfo();
			bean.setColumnName(crs.getString("column_name").toLowerCase());
			bean.setColumnType(crs.getString("column_type"));
			bean.setColumnZh(crs.getString("column_comment"));
			bean.setIsPri(crs.getString("column_key"));
			list.add(bean);
		}
		return list;
	}

	public String srcFile(Code bean, List<ColumnInfo> columnList) {
		String packagePath = bean.getPackagePath();// com.dhcc.popedom
		String domainName = bean.getDomainName();// MyJob
		String path = bean.getPath();// D:\\file
		String tableName = bean.getTableName();
		String moduleName = bean.getModuleName();
		String module = packagePath.substring(packagePath.lastIndexOf(".") + 1,
				packagePath.length());

		String actionName = domainName + "Action";
		String implName = domainName + "Impl";
		String srcPath = path + "\\src";
		String domainPath = srcPath + "\\com\\dhcc\\" + module + "\\domain";
		String actionPath = srcPath + "\\com\\dhcc\\" + module + "\\action";
		String implPath = srcPath + "\\com\\dhcc\\" + module + "\\impl";
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
		
		File domainF = new File(domainFile);
		File actionF = new File(actionFile);
		File implF = new File(implFile);
		if(domainF.exists() || actionF.exists() || implF.exists()){
			return "99";
		}

		try {
			FileOutputStream domainFos = new FileOutputStream(domainFile);
			FileOutputStream actionFos = new FileOutputStream(actionFile);
			FileOutputStream implFos = new FileOutputStream(implFile);

			String column_name = "";
			String listResult = "";
			String defaultValue = "";
			String beanValue = "";
			String wh = "";
			String pri = "";
			String priColumn = "";
			String priC = "";
			try {
				for (int k = 0; k < columnList.size(); k++) {
					ColumnInfo info = columnList.get(k);
					String column = info.getColumnName();
					String priKey = info.getIsPri();
					if (priKey != null && "PRI".equals(priKey)) {
						priColumn = column;
						String[] t = column.split("_");
						for (int i = 0; i < t.length; i++) {
							pri += t[i].substring(0, 1).toUpperCase()
									+ t[i].substring(1, t[i].length())
											.toLowerCase();
							if (i == 0) {
								priC += t[i].substring(0, 1).toLowerCase()
										+ t[i].substring(1, t[i].length())
												.toLowerCase();
							} else {
								priC += t[i].substring(0, 1).toUpperCase()
										+ t[i].substring(1, t[i].length())
												.toLowerCase();
							}

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

			String updateSql = "update " + tableName.toLowerCase() + " set "
					+ column_name.replaceAll(",", "=?,") + "=? where ";
			String insertSql = "insert into " + tableName.toLowerCase() + "("
					+ column_name + ") values ("
					+ wh.substring(0, wh.length() - 1) + ")";

			PrintWriter domainPW = new PrintWriter(new OutputStreamWriter(
					domainFos, "UTF-8"));
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
			mypath = mypath.substring(0, mypath.length() - 5);

			PrintWriter actionPW = new PrintWriter(new OutputStreamWriter(
					actionFos, "UTF-8"));
			BufferedReader actionBr = new BufferedReader(
					new InputStreamReader(new FileInputStream(mypath
							+ "actionTemplete.txt"), "UTF-8"));
			String str = actionBr.readLine();
			while (str != null) {
				str = str.replace("{{packagePath}}", packagePath + ".action");
				str = str.replace("{{domainPath}}", packagePath + ".domain."
						+ domainName);
				str = str.replace("{{implPath}}", packagePath + ".impl."
						+ implName);
				str = str.replace("{{domainName}}", domainName);
				str = str.replace("{{msgModule}}", domainName.toLowerCase());
				str = str.replace("{{domainPri}}", pri);
				actionPW.println(str);
				str = actionBr.readLine();
			}
			actionBr.close();
			actionPW.close();

			BufferedReader implBr = new BufferedReader(new InputStreamReader(
					new FileInputStream(mypath + "implTemplete.txt"), "UTF-8"));
			str = implBr.readLine();
			PrintWriter implPW = new PrintWriter(new OutputStreamWriter(
					implFos, "UTF-8"));
			while (str != null) {
				str = str.replace("{{packagePath}}", packagePath + ".impl");
				str = str.replace("{{domainPath}}", packagePath + ".domain."
						+ domainName);
				str = str.replace("{{domainName}}", domainName);
				str = str.replace("{{listResult}}", listResult);
				str = str.replace("{{insertSql}}", insertSql);
				str = str.replace("{{deleteSql}}", "delete from "
						+ tableName.toLowerCase() + " where "
						+ priColumn.toLowerCase() + "=?");
				str = str.replace("{{beanValue}}", beanValue);
				str = str.replace("{{searchSql}}", "select " + column_name
						+ " from " + tableName.toLowerCase() + " where 1=1 ");
				str = str.replace("{{domainPri}}", pri);
				str = str.replace("{{moduleName}}", moduleName);
				str = str.replace("{{getByIdSql}}", "select " + column_name
						+ " from " + tableName.toLowerCase() + " where "
						+ priColumn.toLowerCase() + "=?");
				str = str.replace("{{priColumn}}", priC);
				str = str.replace("{{orderByField}}", priColumn.toLowerCase());
				str = str.replace("{{updateSql}}", updateSql
						+ priColumn.toLowerCase() + "=?");
				implPW.println(str);
				str = implBr.readLine();
			}
			implBr.close();
			implPW.close();

		} catch (Exception e) {
			e.printStackTrace();
			return "90";
		}
		return "10";
	}

	public String webFile(Code bean, List<ColumnInfo> columnList,
			LinkedHashMap<String, String> selectMap,
			LinkedHashMap<String, String> addMap,
			LinkedHashMap<String, String> editMap) {
		String domainName = bean.getDomainName();// MyJob
		String moduleName = bean.getModuleName();
		String namespace = bean.getNamespace();
		String path = bean.getPath();// D:\\file
		String module = bean.getPackagePath().substring(
				bean.getPackagePath().lastIndexOf(".") + 1,
				bean.getPackagePath().length());
		String listJsp = domainName + "_list.jsp";
		String js = domainName + "_list.js";
		String delJsp = domainName + "_del.jsp";
		String addJsp = domainName + "_add.jsp";
		String editJsp = domainName + "_edit.jsp";
		String detailJsp = domainName + "_detail.jsp";

		String webPath = path + "\\web";
		String modulePath = webPath + "\\" + module;

		String jspPath = modulePath + "\\" + domainName;

		File derec = new File(path);
		File webDerec = new File(webPath);
		File moduleDerec = new File(modulePath);
		File jspDerec = new File(jspPath);

		if (!derec.exists() && !derec.isDirectory()) {
			derec.mkdir();
		}
		if (!webDerec.exists() && !webDerec.isDirectory()) {
			webDerec.mkdir();
		}
		if (!moduleDerec.exists() && !moduleDerec.isDirectory()) {
			moduleDerec.mkdir();
		}
		if (!jspDerec.exists() && !jspDerec.isDirectory()) {
			jspDerec.mkdir();
		}
		String mypath = getClass().getResource("").getFile().toString();
		mypath = mypath.substring(0, mypath.length() - 5);
		String listFile = jspPath + "\\" + listJsp;
		String jsFile = jspPath + "\\" + js;
		String delFile = jspPath + "\\" + delJsp;
		String addFile = jspPath + "\\" + addJsp;
		String editFile = jspPath + "\\" + editJsp;
		String detailFile = jspPath + "\\" + detailJsp;
		
		File listF = new File(listFile);
		File jsF = new File(jsFile);
		File delF = new File(delFile);
		File addF = new File(addFile);
		File editF = new File(editFile);
		File detailF = new File(detailFile);
		if(listF.exists() || jsF.exists() || delF.exists() || addF.exists() || editF.exists() || detailF.exists()){
			return "99";
		}
		
		String priKey = "";

		for (int i = 0; i < columnList.size(); i++) {
			ColumnInfo info = columnList.get(i);
			if ("PRI".equals(info.getIsPri())) {
				String columnName = info.getColumnName();
				String[] cols = columnName.split("_");
				for (int k = 0; k < cols.length; k++) {
					if (k == 0) {
						priKey += cols[k].substring(0, 1).toLowerCase()
								+ cols[k].substring(1, cols[k].length())
										.toLowerCase();
					} else {
						priKey += cols[k].substring(0, 1).toUpperCase()
								+ cols[k].substring(1, cols[k].length())
										.toLowerCase();
					}
				}
			}
		}

		try {
			this.mylist(bean, selectMap, domainName, priKey, listFile, mypath,
					module, moduleName);

			this.myjs(bean, jsFile, mypath, domainName, namespace, priKey,
					moduleName);
			this.mydel(delFile, mypath, domainName, moduleName, namespace,
					priKey);

			this.myadd(bean, addFile, mypath, domainName, moduleName,
					namespace, addMap);

			this.myedit(bean, editFile, mypath, domainName, moduleName,
					namespace, editMap, priKey);

			this.mydetail(detailFile, mypath, domainName, moduleName,
					namespace, priKey, columnList);

		} catch (Exception e) {
			e.printStackTrace();
			return "90";
		}
		return "10";
	}

	public void mydetail(String detailFile, String mypath, String domainName,
			String moduleName, String namespace, String priKey,
			List<ColumnInfo> columnList) throws Exception {

		StringBuffer tableApp = new StringBuffer("");

		for (int k = 0; k < columnList.size(); k++) {
			ColumnInfo info = columnList.get(k);
			String columnName = info.getColumnName();
			String[] cols = columnName.split("_");
			String colsVal = "";
			for (int i = 0; i < cols.length; i++) {
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
			String columnZh = info.getColumnZh();
			if (k % 2 == 0) {
				tableApp.append("<tr>\n");
			}
			tableApp.append("<td width=\"15%\" class=\"wenzi\">" + columnZh
					+ "：</td>\n");
			tableApp
					.append("<td width=\"35%\" class=\"val\" align=\"left\">\n");
			tableApp.append("{{" + domainName.toLowerCase() + "." + colsVal
					+ "}}\n");
			tableApp.append("</td>\n");
			if (k % 2 == 1) {
				tableApp.append("</tr>\n");
			} else if (k == columnList.size() - 1) {
				tableApp.append("</tr>\n");
			}

		}

		FileOutputStream detailFos = new FileOutputStream(detailFile);
		PrintWriter detailPW = new PrintWriter(new OutputStreamWriter(
				detailFos, "UTF-8"));
		BufferedReader detailBr = new BufferedReader(new InputStreamReader(
				new FileInputStream(mypath + "detailJspTemplete.txt"), "UTF-8"));
		String detailStr = detailBr.readLine();
		while (detailStr != null) {
			detailStr = detailStr.replace("{{domainName}}", domainName);
			detailStr = detailStr.replace("{{moduleName}}", moduleName);
			detailStr = detailStr.replace("{{namespace}}", namespace);
			detailStr = detailStr.replace("{{priKey}}", priKey);
			detailStr = detailStr.replace("{{tableList}}", tableApp.toString());
			detailStr = detailStr.replace("{{domainList}}", domainName
					.toLowerCase());
			detailPW.println(detailStr);
			detailStr = detailBr.readLine();
		}

		detailPW.close();
		detailBr.close();
	}

	public void myedit(Code bean, String editFile, String mypath,
			String domainName, String moduleName, String namespace,
			LinkedHashMap<String, String> editMap, String priKey)
			throws Exception {
		String funcThisEdit = bean.getFuncThisEdit();
		String funcOpenEdit = bean.getFuncOpenEdit();

		Set<String> set = editMap.keySet();
		Iterator<String> it = set.iterator();
		StringBuffer tableApp = new StringBuffer("");
		StringBuffer ruleAppend = new StringBuffer("");

		int index = 0;
		while (it.hasNext()) {
			String columnName = it.next();
			String[] cols = columnName.split("_");
			String colsVal = "";
			for (int i = 0; i < cols.length; i++) {
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
			String columnZh = editMap.get(columnName);
			if (index % 2 == 0) {
				tableApp.append("<tr>\n");
			}
			// <input type="text" name="jobName" id="jobName"
			// ng-model="job.jobName" tabindex="1" ng-readonly="myreadonly"
			// class="text1"/>
			tableApp
					.append("<td width=\"13%\" class=\"wenzi\" align=\"center\"><b>"
							+ columnZh + "</b></td>\n");
			tableApp.append("<td width=\"37%\">\n");
			tableApp
					.append("<input type=\"text\" name=\""
							+ colsVal
							+ "\" id=\""
							+ colsVal
							+ "\" ng-model=\""
							+ domainName.toLowerCase()
							+ "."
							+ colsVal
							+ "\" tabindex=\""+(index+1)+"\" ng-readonly=\"myreadonly\" class=\"text1\"/>\n");
			tableApp.append("</td>\n");
			if (index % 2 == 1) {
				tableApp.append("</tr>\n");
			} else if (index == set.size() - 1) {
				tableApp.append("</tr>\n");
			}

			if (index != 0) {
				ruleAppend.append(",\n");
			}
			ruleAppend.append(colsVal + ": {\n");
			ruleAppend.append("required: true\n");
			ruleAppend.append("}");

			index++;

		}

		BufferedReader editBr = null;
		boolean flag = false;
		if ("true".equals(funcThisEdit)) {
			editBr = new BufferedReader(new InputStreamReader(
					new FileInputStream(mypath + "editJspTemplete.txt"),
					"UTF-8"));
			flag = true;
		} else if ("true".equals(funcOpenEdit)) {
			editBr = new BufferedReader(new InputStreamReader(
					new FileInputStream(mypath + "editOpenTemplete.txt"),
					"UTF-8"));
			flag = true;
		}
		if (flag) {
			FileOutputStream editFos = new FileOutputStream(editFile);
			PrintWriter editPW = new PrintWriter(new OutputStreamWriter(
					editFos, "UTF-8"));
			String editStr = editBr.readLine();
			while (editStr != null) {
				editStr = editStr.replace("{{domainName}}", domainName);
				editStr = editStr.replace("{{moduleName}}", moduleName);
				editStr = editStr.replace("{{namespace}}", namespace);
				editStr = editStr.replace("{{tableList}}", tableApp.toString());
				editStr = editStr
						.replace("{{ruleList}}", ruleAppend.toString());
				editStr = editStr.replace("{{priColumn}}", priKey);
				editStr = editStr.replace("{{myindex}}", "" + (index + 1));
				editStr = editStr.replace("{{domainList}}", domainName
						.toLowerCase());
				editPW.println(editStr);
				editStr = editBr.readLine();
			}

			editPW.close();
			editBr.close();
		}

	}

	public void myadd(Code bean, String addFile, String mypath,
			String domainName, String moduleName, String namespace,
			LinkedHashMap<String, String> addMap) throws Exception {
		String funcThisAdd = bean.getFuncThisAdd();
		String funcOpenAdd = bean.getFuncOpenAdd();
		Set<String> set = addMap.keySet();
		Iterator<String> it = set.iterator();
		StringBuffer tableApp = new StringBuffer("");
		StringBuffer ruleAppend = new StringBuffer("");
		String force = "";
		int index = 0;
		while (it.hasNext()) {
			String columnName = it.next();
			String[] cols = columnName.split("_");
			String colsVal = "";
			for (int i = 0; i < cols.length; i++) {
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
			if (index == 0) {
				force = colsVal;
			}
			String columnZh = addMap.get(columnName);
			if (index % 2 == 0) {
				tableApp.append("<tr>\n");
			}
			tableApp
					.append("<td width=\"13%\" class=\"wenzi\" align=\"center\"><b>"
							+ columnZh + "</b></td>\n");
			tableApp.append("<td width=\"37%\">\n");
			tableApp
					.append("<input type=\"text\" name=\""
							+ colsVal
							+ "\" id=\""
							+ colsVal
							+ "\" ng-model=\""
							+ domainName.toLowerCase()
							+ "."
							+ colsVal
							+ "\" tabindex=\""+(index+1)+"\" ng-readonly=\"myreadonly\" class=\"text1\"/>\n");
			tableApp.append("</td>\n");
			if (index % 2 == 1) {
				tableApp.append("</tr>\n");
			} else if (index == set.size() - 1) {
				tableApp.append("</tr>\n");
			}

			if (index != 0) {
				ruleAppend.append(",\n");
			}
			ruleAppend.append(colsVal + ": {\n");
			ruleAppend.append("required: true\n");
			ruleAppend.append("}");

			index++;

		}

		BufferedReader addBr = null;
		boolean flag = false;
		if ("true".equals(funcThisAdd)) {
			addBr = new BufferedReader(
					new InputStreamReader(new FileInputStream(mypath
							+ "addJspTemplete.txt"), "UTF-8"));
			flag = true;
		} else if ("true".equals(funcOpenAdd)) {
			addBr = new BufferedReader(new InputStreamReader(
					new FileInputStream(mypath + "addOpenTemplete.txt"),
					"UTF-8"));
			flag = true;
		}
		if (flag) {
			FileOutputStream addFos = new FileOutputStream(addFile);
			PrintWriter addPW = new PrintWriter(new OutputStreamWriter(addFos,
					"UTF-8"));
			String addStr = addBr.readLine();
			while (addStr != null) {
				addStr = addStr.replace("{{domainName}}", domainName);
				addStr = addStr.replace("{{moduleName}}", moduleName);
				addStr = addStr.replace("{{myforce}}", force);
				addStr = addStr.replace("{{myindex}}", "" + (index + 1));
				addStr = addStr.replace("{{namespace}}", namespace);
				addStr = addStr.replace("{{tableList}}", tableApp.toString());
				addStr = addStr.replace("{{ruleList}}", ruleAppend.toString());
				addStr = addStr.replace("{{domainList}}", domainName
						.toLowerCase());
				addPW.println(addStr);
				addStr = addBr.readLine();
			}
			addBr.close();
			addPW.close();
		}

	}

	public void myjs(Code bean, String jsFile, String mypath,
			String domainName, String namespace, String priKey,
			String moduleName) throws Exception {
		String funcThisAdd = bean.getFuncThisAdd();
		String funcOpenAdd = bean.getFuncOpenAdd();
		String funcThisEdit = bean.getFuncThisEdit();
		String funcOpenEdit = bean.getFuncOpenEdit();
		FileOutputStream jsFos = new FileOutputStream(jsFile);
		PrintWriter jsPW = new PrintWriter(new OutputStreamWriter(jsFos,
				"UTF-8"));
		BufferedReader jsBr = new BufferedReader(new InputStreamReader(
				new FileInputStream(mypath + "jsTemplete.txt"), "UTF-8"));
		String jsStr = jsBr.readLine();
		while (jsStr != null) {

			if (jsStr.trim().equals("{{myadd}}")) {
				if ("true".equals(funcThisAdd)) {
					jsStr = jsStr.replace("{{myadd}}",
							"location.replace(\"{{domainName}}_add.action\");");
				} else if ("true".equals(funcOpenAdd)) {
					StringBuffer add = new StringBuffer("top.layer.open({\n");
					add.append("type: 2,\n");
					add
							.append("title: ['{{moduleName}}添加','font-size:15px;font-family: 微软雅黑;font-weight:bold;'],\n");
					add.append("shadeClose: false,\n");
					add.append("shade: 0.4,\n");
					add.append("area: ['580px', '350px'],\n");
					add
							.append("content: path + '/{{namespace}}/{{domainName}}_add.action'\n");
					add.append("});");
					jsStr = jsStr.replace("{{myadd}}", add.toString());
				} else {
					jsStr = jsStr.replace("{{myadd}}", "");
				}
			}

			if (jsStr.trim().equals("{{myedit}}")) {
				if ("true".equals(funcThisEdit)) {
					jsStr = jsStr
							.replace("{{myedit}}",
									"location.replace('{{domainName}}_edit.action?{{domainPri}}='+{{domainPri}});");
				} else if ("true".equals(funcOpenEdit)) {
					StringBuffer edit = new StringBuffer("top.layer.open({\n");
					edit.append("type: 2,\n");
					edit
							.append("title: ['{{moduleName}}修改','font-size:15px;font-family: 微软雅黑;font-weight:bold;'],\n");
					edit.append("shadeClose: false,\n");
					edit.append("shade: 0.4,\n");
					edit.append("area: ['580px', '350px'],\n");
					edit
							.append("content: path + '/{{namespace}}/{{domainName}}_edit.action?{{domainPri}}='+{{domainPri}}\n");
					edit.append("});");
					jsStr = jsStr.replace("{{myedit}}", edit.toString());
				} else {
					jsStr = jsStr.replace("{{myedit}}", "");
				}
			}

			jsStr = jsStr.replace("{{domainName}}", domainName);
			jsStr = jsStr.replace("{{namespace}}", namespace);
			jsStr = jsStr.replace("{{domainPri}}", priKey);
			jsStr = jsStr.replace("{{moduleName}}", moduleName);
			jsStr = jsStr.replace("{{domainList}}", domainName.toLowerCase()
					+ "s");
			jsPW.println(jsStr);
			jsStr = jsBr.readLine();
		}

		jsPW.close();
		jsBr.close();
	}

	public void mydel(String delFile, String mypath, String domainName,
			String moduleName, String namespace, String priKey)
			throws Exception {
		FileOutputStream delFos = new FileOutputStream(delFile);
		PrintWriter delPW = new PrintWriter(new OutputStreamWriter(delFos,
				"UTF-8"));
		BufferedReader delBr = new BufferedReader(new InputStreamReader(
				new FileInputStream(mypath + "deleteJspTemplete.txt"), "UTF-8"));
		String delStr = delBr.readLine();
		while (delStr != null) {
			delStr = delStr.replace("{{domainName}}", domainName);
			delStr = delStr.replace("{{moduleName}}", moduleName);
			delStr = delStr.replace("{{namespace}}", namespace);
			delStr = delStr.replace("{{priColumn}}", priKey);
			delStr = delStr.replace("{{domainList}}", domainName.toLowerCase()
					+ "s");
			delPW.println(delStr);
			delStr = delBr.readLine();
		}

		delPW.close();
		delBr.close();
	}

	public void mylist(Code bean, LinkedHashMap<String, String> selectMap,
			String domainName, String priKey, String listFile, String mypath,
			String module, String moduleName) throws Exception {
		String funcThisAdd = bean.getFuncThisAdd();
		String funcOpenAdd = bean.getFuncOpenAdd();
		String funcThisEdit = bean.getFuncThisEdit();
		String funcOpenEdit = bean.getFuncOpenEdit();
		String funcDel = bean.getFuncDel();
		String funcDetail = bean.getFuncDetail();
		String funcSubmit = bean.getFuncSubmit();
		String funcCheck = bean.getFuncCheck();
		Set<String> set = selectMap.keySet();
		Iterator<String> it = set.iterator();
		StringBuffer mytableth = new StringBuffer("<tr>\n");
		StringBuffer mytabletr = new StringBuffer("");
		mytabletr.append("<tr class=\"wite\" ng-repeat=\""
				+ domainName.toLowerCase() + " in " + domainName.toLowerCase()
				+ "s\" ng-class-even=\"'gary'\">\n");

		StringBuffer selectLi = new StringBuffer();
		int index = 0;
		int x = 1;
		int size = set.size();
		int width = (100 - 15) / size;
		String css = "";
		while (it.hasNext()) {
			String columnName = it.next();
			String columnZh = selectMap.get(columnName);
			mytableth.append("<th width=\"" + width + "%\">\n");
			mytableth.append("<a href=\"#\" ng-click=\"myorder('"
					+ columnName.toLowerCase() + "')\">" + columnZh + "</a>\n");
			if (index == 0) {
				x = 2;
			} else {
				x = 1;
			}
			mytableth.append("<img id=\"" + columnName.toLowerCase()
					+ "Img\" src=\"<%=path%>/css/images/aro" + x
					+ ".png\" width=\"7\" height=\"12\" />\n");
			mytableth.append("</th>\n");

			String[] cols = columnName.split("_");
			String colsVal = "";
			for (int i = 0; i < cols.length; i++) {
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
			if(index == 0){
				css = "style=\"border-left:0px;\"";
			}else{
				css = "";
			}
			
			mytabletr.append("				<td "+css+">{{" + domainName.toLowerCase() + "."
					+ colsVal + "}}</td>\n");

			selectLi.append("<li ng-click=\"selectedThis('" + columnZh + "','"
					+ columnName.toLowerCase() + "');\" >" + columnZh
					+ "</li>\n");
			index++;
		}

		mytabletr.append("<td>\n");
		mytabletr
				.append("<div class=\"caozuo\" style=\"width:caozuosizepx;\">\n");
		int czSize = 0;
		if ("true".equals(funcOpenEdit) || "true".equals(funcThisEdit)) {
			mytabletr.append("<span ng-click=\"edit" + domainName + "("
					+ domainName.toLowerCase() + "." + priKey
					+ ")\" class=\"change\" title=\"修改\"></span>\n");
			czSize++;
		}
		if ("true".equals(funcDel)) {
			mytabletr.append("<span ng-click=\"mydel("
					+ domainName.toLowerCase() + "." + priKey
					+ ")\" class=\"delete\" title=\"删除\"></span>\n");
			czSize++;
		}
		if ("true".equals(funcDetail)) {
			mytabletr.append("<span ng-click=\"mydetail("
					+ domainName.toLowerCase() + "." + priKey
					+ ")\" class=\"yulan\" title=\"预览\"></span>\n");
			czSize++;
		}

		if ("true".equals(funcSubmit)) {
			mytabletr.append("<span ng-click=\"mysubmit("
					+ domainName.toLowerCase() + "." + priKey
					+ ")\" class=\"tijiao\" title=\"提交\"></span>\n");
			czSize++;
		}

		if ("true".equals(funcCheck)) {
			mytabletr.append("<span ng-click=\"mycheck("
					+ domainName.toLowerCase() + "." + priKey
					+ ")\" class=\"shenhe\" title=\"审核\"></span>\n");
			czSize++;
		}
		mytabletr.append("</div>\n");
		mytabletr.append("</td>\n");
		mytabletr.append("</tr>");
		mytableth.append("<th width=\"15%\">操作</th>\n");
		mytableth.append("</tr>");

		FileOutputStream listFos = new FileOutputStream(listFile);
		PrintWriter listPW = new PrintWriter(new OutputStreamWriter(listFos,
				"UTF-8"));
		BufferedReader listBr = new BufferedReader(new InputStreamReader(
				new FileInputStream(mypath + "listJspTemplete.txt"), "UTF-8"));
		String listStr = listBr.readLine();
		while (listStr != null) {
			if (listStr.trim().equals("{{myadd}}")) {
				if ("true".equals(funcThisAdd) || "true".equals(funcOpenAdd)) {
					listStr = listStr
							.replace(
									"{{myadd}}",
									"<li><input ng-click=\"add()\" name=\"button\" class=\"oper1\" type=\"button\" value=\"添加\" /></li>");
				} else {
					listStr = listStr.replace("{{myadd}}", "");
				}
			}
			listStr = listStr.replace("{{domainName}}", domainName);
			listStr = listStr.replace("{{packageName}}", module);
			listStr = listStr.replace("{{mytableth}}", mytableth);
			listStr = listStr.replace("{{mytabletr}}", mytabletr.toString()
					.replace("caozuosize", "" + czSize * 27));
			listStr = listStr.replace("{{selectLi}}", selectLi);
			listStr = listStr.replace("{{moduleName}}", moduleName);
			listPW.println(listStr);
			listStr = listBr.readLine();
		}

		listPW.close();
		listBr.close();
	}

}
