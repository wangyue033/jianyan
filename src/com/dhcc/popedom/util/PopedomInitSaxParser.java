/**
 * 
 */
package com.dhcc.popedom.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dhcc.login.Employee;

import framework.dhcc.conf.SqlValues;
import framework.dhcc.conf.SystemConfig;
import framework.dhcc.db.DBFacade;

/**
 * @author Administrator
 * 
 */
public class PopedomInitSaxParser extends DefaultHandler {
	private String menuId;
	private String moduleId;
	private String funcId;
	private String admin;
	private String resourceId;
	
	private String funcIco;

	public PopedomInitSaxParser(String initIds, String initType)
			throws Exception {
		if (initIds == null || initIds.trim().equals("")) {
			return;
		}
		String[] initId = initIds.split(",");
		if (initType != null && initType.trim().equals("INIT")) {
			admin = SystemConfig.getInstance().getAdminAccount();
			boolean temp = Employee.getInstance().insertEmployeeInfo(admin,
					SystemConfig.getInstance().getAdminPassword());
			if (!temp) {
				System.out.println("在数据库中添加管理员基本信息的操作未执行!");
			}
			String[] sqls = new String[10];
			sqls[0] = SqlValues.getInstance().get("init.Popedom", "del_rs_map");
			sqls[1] = SqlValues.getInstance()
					.get("init.Popedom", "del_popedom");
			sqls[2] = SqlValues.getInstance().get("init.Popedom", "del_func");
			sqls[3] = SqlValues.getInstance().get("init.Popedom", "del_module");
			sqls[4] = SqlValues.getInstance().get("init.Popedom", "del_menu");
			sqls[5] = SqlValues.getInstance().get("init.Popedom", "del_re_map");
			sqls[6] = "delete from P_POPEDOM_LOG";
			sqls[7] = SqlValues.getInstance().get("init.Popedom", "del_role");
			sqls[8] = "insert into p_role (role_id,role_name,remark,can_modify,creator,create_time,parent_id) values ( '00', '系统管理员','系统初始化管理员','0','system',now(),'-1')";
			sqls[9] = "insert into p_re_map (re_id,serial_id, role_id,remark) VALUES ( '"
					+ DBFacade.getInstance().getID()
					+ "','"
					+ SystemConfig.getInstance().getAdminId() + "', '00',' ')";
			DBFacade.getInstance().execute(sqls);
			List<Map<String, String>> roleList = (ArrayList<Map<String, String>>) SystemConfig
					.getInstance().getRoleList();
			if (roleList != null) {
				String[] roleSqls = new String[roleList.size()];
				Object[][] roleParas = new Object[roleList.size()][];
				for (int i = 0; i < roleList.size(); i++) {
					Map<String, String> roleMap = (HashMap<String, String>) roleList
							.get(i);
					roleSqls[i] = "insert into p_role (role_id,role_name,parent_id,can_modify,is_charge,remark,create_time) values (?,?,?,?,?,?,?)";
					roleParas[i] = new Object[7];
					roleParas[i][0] = (String) roleMap.get("roleId");
					roleParas[i][1] = (String) roleMap.get("roleName");
					roleParas[i][2] = (String) roleMap.get("parentId");
					roleParas[i][3] = "0";
					roleParas[i][4] = (String) roleMap.get("charge");
					roleParas[i][5] = (String) roleMap.get("roleName");
					roleParas[i][6] = new Date(System.currentTimeMillis());
				}
				DBFacade.getInstance().execute(roleSqls, roleParas);
			}
		}
		for (int i = 0; i < initId.length; i++) {
			System.out.println("id===============" + initId[i]);
			InputStream is = this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"com/dhcc/" + initId[i] + "/PopedomDefines.xml");
			if (is != null) {
				parse(is);
			} else {
				System.out.println("读取的文件不存在");
			}
		}

	}

	private void parse(InputStream fileIs) throws SAXException,
			ParserConfigurationException, IOException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(fileIs, this);
	}

	public void startElement(java.lang.String namespaceURI,
			java.lang.String localName, java.lang.String qName,
			Attributes attributes) throws SAXException {
		String eName = localName;
		if ("".equals(eName)) {
			eName = qName;
		}
		// insert menus
		if ("menus".equals(eName)) {
			String sql = SqlValues.getInstance().get("init.Popedom",
					"insert_menu");
			this.menuId = attributes.getValue("menu-id");
			String[] sParas = { this.menuId, attributes.getValue("menu-name") };
			DBFacade.getInstance().execute(sql, sParas);
		}
		// insert modules
		if ("modules".equals(eName)) {
			String sql = SqlValues.getInstance().get("init.Popedom",
					"insert_module");
			this.moduleId = attributes.getValue("module-id");
			String[] paras = { this.moduleId,
					attributes.getValue("module-name"), this.menuId, attributes.getValue("is-valid") == null ? "1" : attributes.getValue("is-valid"),attributes.getValue("module-ico") };
			DBFacade.getInstance().execute(sql, paras);
		}
		if ("functions".equals(eName)) {
			String sql = SqlValues.getInstance().get("init.Popedom",
					"insert_func");
			this.funcId = attributes.getValue("func-id");
			this.resourceId = attributes.getValue("resource-id");
			this.funcIco = attributes.getValue("func-ico");
			String[] paras = { this.funcId, attributes.getValue("func-name"),
					attributes.getValue("func-uri"), this.moduleId,
					this.resourceId, attributes.getValue("is-valid") == null ? "1" : attributes.getValue("is-valid"), this.funcIco };
			DBFacade.getInstance().execute(sql, paras);

		}
		if ("popedoms".equals(eName)) {
			String popedomId = attributes.getValue("popedom-id");
			String[] sqls = new String[2];
			String[][] sParas = new String[2][];
			// insert p_popedom
			sqls[0] = SqlValues.getInstance().get("init.Popedom",
					"insert_popedom");
			sParas[0] = new String[] { popedomId,
					attributes.getValue("popedom-name"), popedomId,
					this.funcId };
			// insert p_rs_map
			sqls[1] = SqlValues.getInstance().get("init.Popedom",
					"insert_rs_map");
			sParas[1] = new String[] { DBFacade.getInstance().getID(), "00",
					popedomId, "for admin" };
			DBFacade.getInstance().execute(sqls, sParas);

		}
	}
}
