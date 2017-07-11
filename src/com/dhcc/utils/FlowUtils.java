package com.dhcc.utils;

import com.sun.rowset.CachedRowSetImpl;

import framework.dhcc.db.DBFacade;

public class FlowUtils {

	public static String getFlowId(String belongWork) {
		String sql = "select flow_id from dh_flow where belong_work=?";
		return (String) DBFacade.getInstance().getValueBySql(sql,
				new String[] { belongWork });
	}

	public static String[] getNodeId(String belongWork) {
		try {
			String sql = "select a.node_id,a.node_order from dh_node a,dh_flow b "
					+ "where a.flow_id=b.flow_id and b.belong_work=? order by node_order asc";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { belongWork });
			String[] str = new String[crs.size()];
			while (crs.next()) {
				int order = crs.getInt("node_order") - 1;
				str[order] = crs.getString("node_id");
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String[] getStepId(String belongWork) {
		try {
			String sql = "select a.step_id from dh_step a,dh_flow c "
					+ "where a.flow_id=c.flow_id and c.belong_work=? order by step_id asc";
			CachedRowSetImpl crs = DBFacade.getInstance().getRowSet(sql,
					new String[] { belongWork });
			String[] str = new String[crs.size()];
			int index = 0;
			while (crs.next()) {
				str[index] = crs.getString("step_id");
				index++;
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
