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
import com.dhcc.popedom.domain.DeptUser;
import com.dhcc.popedom.impl.DeptUserImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

/**
 * 
 * @author WYH
 * 
 */

public class DeptUserAction extends ActionSupport implements ModelDriven<DeptUser>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DeptUser bean = new DeptUser();

	List<DeptUser> dataRows = new ArrayList<DeptUser>();

	HttpServletRequest request;

	HttpServletResponse response;

	DeptUserImpl impl = new DeptUserImpl();

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

	public String DeptUser_init() {
		String items = bean.getItems();
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "DeptUser_tree");
		request.setAttribute("rightAction", "DeptUser_list");
		request.setAttribute("sWidth", "250");
		return "tree_frame";
	}

	public String DeptUser_list() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		String companyId = (String) request.getParameter("companyId");
		bean.setCompanyId(companyId);
		/*
		String companyName = (String) request.getParameter("companyName");
		bean.setCompanyName(companyName);*/
		if (bean.getOper() == 0) {
			return "DeptUser_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				
				if(companyId == null || "".equals(companyId)){
					bean.setCompanyId(userInfo.getEmployee().getOrgId());
				}
				String result = impl.getDeptUserList(bean, bean.getCurPage(), bean
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

	public String DeptUser_add() throws IOException {
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		String orgId = userInfo.getEmployee().getOrgId();
		if (bean.getOper() == 0) {
			JSONArray jobs = new JSONArray();
			jobs = impl.getJobList(orgId);
			request.setAttribute("jobs", jobs.toString());
			return "DeptUser_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				bean.setSerialId(impl.getSeriId());
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addDeptUser(bean);
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
	
	public String DeptUser_upload(){
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
			e.printStackTrace();
		} finally {
			out.print(resultStr);
		}
		return null;
	}

	public String DeptUser_edit() {
		int oper = bean.getOper();
		UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
		String orgId = userInfo.getEmployee().getOrgId();
		
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		treeList = impl.getCompanyAllRoot(orgId);
		request.setAttribute("treeList", JSONArray.fromObject(treeList));
		
		JSONArray jobs = new JSONArray();
		jobs = impl.getJobList(orgId);
		request.setAttribute("jobs", jobs.toString());
		
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "DeptUser_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getDeptUserById(bean.getSerialId());
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
				pw = response.getWriter();
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editDeptUser(bean);
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

	//刷新员工管理主页左边的部门树
	public String DeptUser_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			UserInfoBean userInfo = (UserInfoBean) request.getSession().getAttribute("userInfoBean");
			//bean.setCompanyId(userInfo.getEmployee().getOrgId());
			String orgId = userInfo.getEmployee().getOrgId();
			treeList = impl.getCompanyAllRoot(orgId);
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "DeptUser_tree";
	}
	
	//获取检测人员弹框的人员信息
	public String DeptUser_jsonTreeNot() {
		UserInfoBean userInfo = (UserInfoBean) request.getSession()
				.getAttribute("userInfoBean");
		bean.setCompanyId(userInfo.getEmployee().getCompanyId());
		bean.setCompanyName(userInfo.getEmployee().getCompanyName());
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getDeptUserAllRoot(bean.getCompanyId(),bean.getCompanyName());
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "DeptUser_jsonTreeNot";
	}

	public DeptUser getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<DeptUser> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<DeptUser> dataRows) {
		this.dataRows = dataRows;
	}

}
