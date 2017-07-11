package com.dhcc.popedom.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.login.UserInfoBean;
import com.dhcc.popedom.domain.User;
import com.dhcc.popedom.impl.UserImpl;
import com.dhcc.utils.ZTreeImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

/**
 * 
 * @author WYH
 * 
 */

public class UserAction extends ActionSupport implements ModelDriven<User>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	User bean = new User();

	List<User> dataRows = new ArrayList<User>();

	HttpServletRequest request;

	HttpServletResponse response;

	UserImpl impl = new UserImpl();

	private File Filedata;// 这里的"fileName"一定要与表单中的文件域名相同,uploadify

	private String FiledataFileName;// 格式同上"fileName"+FileName uploadifyFileName

	private String saveFolder;// 存储路径

	public File getFiledata() {
		return Filedata;
	}

	public void setFiledata(File filedata) {
		Filedata = filedata;
	}

	public String getFiledataFileName() {
		return FiledataFileName;
	}

	public void setFiledataFileName(String filedataFileName) {
		FiledataFileName = filedataFileName;
	}

	public String getSaveFolder() {
		return saveFolder;
	}

	public void setSaveFolder(String saveFolder) {
		this.saveFolder = saveFolder;
	}

	public String User_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "User_tree");
		request.setAttribute("rightAction", "User_list");
		request.setAttribute("sWidth", "250");
		return "tree_frame";
	}

	public String User_list() {
		String companyId = (String) request.getParameter("companyId");
		String companyName = (String) request.getParameter("companyName");
		bean.setCompanyId(companyId);
		bean.setCompanyName(companyName);
		if (bean.getOper() == 0) {
			return "User_list";
		} else {
			try {
				// System.out.println("companyId:"+companyId);
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				String result = impl.getUserList(bean, bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public String User_add() throws IOException {
		if (bean.getOper() == 0) {
			JSONArray jobs = new JSONArray();
			jobs = impl.getJobList();
			request.setAttribute("jobs", jobs.toString());
			return "User_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				bean.setSerialId(impl.getSeriId());
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addUser(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("popedom.user.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.user.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if (bean.getOper() == 2) {
			try {
				boolean flag = impl.addCheck(bean.getUserId());
				PrintWriter pw = null;
				pw = response.getWriter();
				if (flag == true) {
					pw.print("true");
				} else {
					pw.print("false");
				}
				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.user.add.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String User_upload(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String saveFolder = request.getSession().getServletContext()
				.getRealPath("");
		PrintWriter out = null;
		String resultStr = "";
		String extName = "";// 扩展名
		String newFileName = "";// 新文件名
		try {
			// 1.解决乱码问题
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
			// 2.获取文件的扩展名
			if (FiledataFileName.lastIndexOf(".") >= 0) {
				extName = (FiledataFileName).substring(FiledataFileName
						.lastIndexOf("."));
				// 3.根据当前时间生成新的文件名称
				String nowTime = new SimpleDateFormat("yyyymmddHHmmss")
						.format(new Date());// 当前时间
				newFileName = nowTime + extName;
				// 4.设置文件保存路径
				String param = request.getParameter("saveFolder");
				saveFolder = saveFolder.substring(0, saveFolder.lastIndexOf("\\"));
				String savePath = saveFolder + "/ImgUpload/" + param + "/";
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				Filedata.renameTo(new File(savePath + newFileName));
				resultStr = "ImgUpload/" + param + "/" + newFileName;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.print(resultStr);
		}
		return null;
	}

	public String User_edit() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = ZTreeImpl.getInstance().getCompanyTree();
		JSONArray obj = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", obj);// 把treeList作为json对象传到前台
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			JSONArray jobs = new JSONArray();
			jobs = impl.getJobList();
			request.setAttribute("jobs", jobs.toString());
			return "User_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getUserById(bean.getSerialId());
				map.put("msgInfo", getText("popedom.user.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.user.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			Map<String, Object> map = null;
			try {
				// System.out.println("edit:"+bean);
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editUser(bean);
				map.put("msgInfo", getText("popedom.user.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.user.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (bean.getOper() == 3) {
			try {
				pw = response.getWriter();
				boolean flag = impl.addCheck(bean.getUserId());
				if (flag == true) {
					pw.print("true");
				} else {
					pw.print("false");
				}
				pw.flush();
				pw.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("popedom.user.edit.noparam"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public String User_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = ZTreeImpl.getInstance().getCompanyAllRoot("00");
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "User_tree";
	}
	
	//获取检测人员弹框的人员信息
	public String User_jsonTreeNot() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		bean.setCompanyId(userInfo.getEmployee().getCompanyId());
		bean.setCompanyName(userInfo.getEmployee().getCompanyName());
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getUserAllRoot(bean.getCompanyId(),bean.getCompanyName());
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "User_jsonTreeNot";
	}
	
	//获取每个检测项目检验人员信息
	public String User_inspectOpt() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		bean.setCompanyId(userInfo.getEmployee().getOrgId());
		bean.setCompanyName(userInfo.getEmployee().getOrgName());
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getInspectOpt(bean.getCompanyId(),bean.getCompanyName());
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "User_inspectOpt";
	}

	public User getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<User> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<User> dataRows) {
		this.dataRows = dataRows;
	}

}
