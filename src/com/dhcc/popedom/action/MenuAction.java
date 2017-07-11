package com.dhcc.popedom.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.popedom.domain.MenuBean;
import com.dhcc.popedom.impl.MenuImpl;
import com.dhcc.utils.ZTreeImpl;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.PageJsonAction;
import framework.dhcc.tree.bean.ZTreeBean;

public class MenuAction extends PageJsonAction implements
		ModelDriven<MenuBean>, ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MenuBean bean = new MenuBean();
	HttpServletRequest request;
	HttpServletResponse response;
	MenuImpl impl = new MenuImpl();
	String perNumber, gotoPage;
	List<MenuBean> dataRows = new ArrayList<MenuBean>();

	public List<MenuBean> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<MenuBean> dataRows) {
		this.dataRows = dataRows;
	}

	public String getGotoPage() {
		return gotoPage;
	}

	public void setGotoPage(String gotoPage) {
		this.gotoPage = gotoPage;
	}

	public String getPerNumber() {
		return perNumber;
	}

	public void setPerNumber(String perNumber) {
		this.perNumber = perNumber;
	}

	public String Menu_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "Menu_tree");
		request.setAttribute("rightAction", "Menu_list");
		return "init_tree_frame";
	}
	
	public String Menu_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = ZTreeImpl.getInstance().getMenuAndModuleTree();
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "Menu_tree";
	}
	
	public String Menu_list() {
		String items = bean.getItems();
		if(perNumber == null || "".equals(perNumber)){
			perNumber = "10";
		}
		
		if (gotoPage == null || "".equals(gotoPage)) {
			gotoPage = "1";
		}
		try {
			int[] record = impl.setMenuList(perNumber, gotoPage, page, bean, dataRows);
			totalPages = record[0];
			curPage = record[1];
			totalRecords = record[2];
			request.setAttribute("items", items);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Menu_list";
	}
	
	public String Menu_addMenu() {
		int oper = bean.getOper();
		try {
			List<MenuBean> menuList = impl.getMenuList();
			request.setAttribute("menuList", menuList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (oper == 0) {
			addActionMessage("");
		} else if (oper == 1) {
			try {
				if (impl.checkMenuId(bean)) {
					impl.addMenu(bean);
					addActionMessage("添加成功！");
					request.setAttribute("result", "Y");
				} else {
					addActionMessage("该编号已存在！");
					request.setAttribute("result", "N");
				}
			} catch (Exception e) {
				addActionMessage("数据库错误！");
				request.setAttribute("result", "N");
				e.printStackTrace();
			}
		}
		return "Menu_addMenu";
	}
	
	public String Menu_editMenu() {
		int oper = bean.getOper();
		if (oper == 0) {
			try {
				MenuBean menuBean = impl.getMenuBean(bean);
				BeanUtils.copyProperties(bean, menuBean);
			} catch (Exception e) {
				addActionMessage("数据库错误！");
				request.setAttribute("result", "N");
				e.printStackTrace();
			}
			addActionMessage("");
		} else if (oper == 1) {
			try {
				impl.editMenu(bean);
				addActionMessage("修改成功！");
				request.setAttribute("result", "Y");
			} catch (Exception e) {
				addActionMessage("数据库错误！");
				request.setAttribute("result", "N");
				e.printStackTrace();
			}
		}
		return "Menu_editMenu";
	}
	
	public String Menu_addFunc() {
		int oper = bean.getOper();
		if (oper == 0) {
			try {
				MenuBean menuBean = impl.getModuleById(bean.getModuleId());
				BeanUtils.copyProperties(bean, menuBean);
			} catch (Exception e) {
				addActionMessage("数据库错误！");
				request.setAttribute("result", "N");
				e.printStackTrace();
			}
			addActionMessage("");
		} else if (oper == 1) {
			try {
				impl.addFunc(bean);
				addActionMessage("添加成功！");
				request.setAttribute("result", "Y");
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage().contains("PRIMARY")) {
					addActionMessage("功能编号或方法在数据库中已存在！");
				} else {
					addActionMessage("数据库错误！");
				}
				request.setAttribute("result", "N");
			}
		}
		return "Menu_addFunc";
	}
	
	public String Menu_delFunc() {
		int oper = bean.getOper();
		if (oper == 0) {
			addActionMessage("");
		} else if (oper == 1) {
			try {
				impl.delFunc(bean.getFuncId());
				request.setAttribute("result", "Y");
				addActionMessage("删除成功！");
			} catch (Exception e) {
				request.setAttribute("result", "N");
				addActionMessage("删除失败！");
				e.printStackTrace();
			}
		}
		return "Menu_delFunc";
	}
	
	public String Menu_editFunc() {
		int oper = bean.getOper();
		if (oper == 0) {
			try {
				MenuBean temp = impl.getFuncById(bean.getFuncId());
				BeanUtils.copyProperties(bean, temp);
			} catch (Exception e) {
				addActionMessage("数据库错误！");
				e.printStackTrace();
			}
		} else if (oper == 1) {
			try {
				impl.editFunc(bean);
				addActionMessage("修改成功！");
				request.setAttribute("result", "Y");
			} catch (Exception e) {
				if (e.getMessage().contains("PK_")) {
					addActionMessage("功能方法在数据库中已存在！");
				} else {
					addActionMessage("数据库错误！");
				}
				request.setAttribute("result", "N");
			}
		}
		return "Menu_editFunc";
	}
	
	public String Menu_setFunc() {
		int oper = bean.getOper();
		if (oper == 0) {

		} else if (oper == 1) {
			try {
				impl.setFunc(bean);
				request.setAttribute("result", "Y");
			} catch (Exception e) {
				request.setAttribute("result", "N");
				e.printStackTrace();
			}
		}

		try {
			List<MenuBean> funcList = impl
					.getFuncListByFuncId(bean.getFuncId());
			request.setAttribute("funcList", funcList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Menu_setFunc";
	}

	@Override
	public int getCurPage() {
		return this.curPage;
	}

	@Override
	public int getTotalPages() {
		return this.totalPages;
	}

	@Override
	public int getTotalRecords() {
		return this.totalRecords;
	}

	public MenuBean getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

}
