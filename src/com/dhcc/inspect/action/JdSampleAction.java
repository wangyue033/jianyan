package com.dhcc.inspect.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.login.UserInfoBean;
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.impl.JdSampleImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.tree.bean.ZTreeBean;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleAction extends ActionSupport implements ModelDriven<JdSample>,
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JdSample bean = new JdSample();

	List<JdSample> dataRows = new ArrayList<JdSample>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdSampleImpl impl = new JdSampleImpl();
	
	public String JdSample_init() {
		String items = bean.getItems();
		System.out.println("items="+items);
		request.setAttribute("items", items);
		request.setAttribute("leftAction", "JdSample_tree");
		request.setAttribute("rightAction", "JdSample_jsonTree");
		return "tree_frame";
	}	
	
	public String JdSample_tree() {
		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
		try {
			treeList = impl.getCompanyAllRoot("D00");;
		} catch (Exception se) {
			se.printStackTrace();
		}
		JSONArray jsonArray = JSONArray.fromObject(treeList);
		request.setAttribute("treeList", jsonArray);
		return "JdSample_tree";
	}

	public String JdSample_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdSample_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setSignOptId(userInfo.getEmployee().getEmployeeId());
				bean.setSignOptName(userInfo.getEmployee().getUserName());
				bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0, 10));
				String result = impl.getJdSampleList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@SuppressWarnings("null")
	public String JdSample_sign(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if(oper == 0){
		return "JdSample_sign";
		}else if(oper == 1){
			Map<String, Object> map = new LinkedHashMap<String, Object>();;
			try {
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setSignOptId(userInfo.getEmployee().getEmployeeId());
				bean.setSignOptName(userInfo.getEmployee().getUserName());
				pw = response.getWriter();
				bean.setSignStatus("1");
				impl.changeStatus(bean);
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdsample.sign.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (IOException e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			
			return null;
		}
		return null;
	}
	public String JdSample_add() {
		if (bean.getOper() == 0) {
			return "JdSample_add";
		} else if (bean.getOper() == 1) {
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setHandleDeptId(userInfo.getEmployee().getCompanyId().substring(0,10));
				bean.setHandleDeptName(impl.getCompanyName(bean.getHandleDeptId()));
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addJdSample(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdsample.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else if(bean.getOper()==2){
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				List<ZTreeBean> userList = new ArrayList<ZTreeBean>();
				userList = impl.treeUserList(bean.getSignCompId());
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("record", userList);
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.add.noparam"));
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

	public String JdSample_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSample_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdSampleById(bean.getSampleId());
				map.put("msgInfo", getText("inspect.jdsample.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else if (oper == 2) {
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.editJdSample(bean);
				map.put("msgInfo", getText("inspect.jdsample.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.edit.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.edit.noparam"));
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

	public String JdSample_del() {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdSample_del";
		} else if (oper == 1) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				map = impl.delJdSample(bean);
				map.put("msgInfo", getText("inspect.jdsample.delete.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				return null;
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				if(e.getMessage().contains("foreign key")){
					map.put("msgInfo", getText("inspect.jdsample.delete.fk"));
				}else{
					map.put("msgInfo", getText("inspect.jdsample.delete.failure"));
				}
				
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsample.delete.noparam"));
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

	public String JdSample_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSample_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdSampleById(bean.getSampleId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//抽样任务下发
	public String JdSample_issue() throws IOException, SQLException {
		int oper = bean.getOper();
		Map<String, Object> map = null;
		PrintWriter pw = null;
		if (oper == 0) {
			return "JdSample_issue";
		} else if (oper == 1) {
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setOptId(userInfo.getEmployee().getEmployeeId());
			bean.setOptName(userInfo.getEmployee().getUserName());
			bean.setCompanyId(userInfo.getEmployee().getCompanyId());
			bean.setCompanyName(userInfo.getEmployee().getCompanyName());
			map = impl.issueJdSample(bean);
			map.put("msgInfo", getText("inspect.JdSample.issue.success"));
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
			return null;
		}else {
			return null;
		}
	}
	
	public String JdSample_show() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSample_show";
			}
		else if (oper == 1) {
			try {
				List<JdSample> list = new ArrayList<JdSample>();
				String array[] = bean.getShowFiles().split(";;");
				for(int i=0 ; i<array.length;i=i+2){
					    JdSample jdbean = new JdSample();
						jdbean.setFilename(array[i]);
						jdbean.setFilepath(array[i+1]);
					    list.add(jdbean);
					    jdbean = new JdSample();
				}
				pw = response.getWriter();
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("showFileList", list);
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		return null;
	}
	
     public void JdSample_check(){
    	 if(bean.getOper()==0){
    	 try {
				boolean flag = impl.SampelTitleCheck(bean.getSampleTitle());
				PrintWriter pw = null;
				pw = response.getWriter();
				if(flag == true){
					pw.print("true");
				}else{
					pw.print("false");
				}
				pw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
    	 }
     }
     
     public String JdSample_jsonTree(){
 		if(bean.getOper() == 0){
 			return "JdSample_jsonTree";
 		}else if(bean.getOper() == 1){
 			try {
 				response.setCharacterEncoding("UTF-8");
 				PrintWriter pw = response.getWriter();
 				String result = impl.getUserMap(bean,bean.getCurPage(), bean
 						.getPerNumber(), bean.getOrderByField(), bean
 						.getOrderByType(), bean.getSearchField(), bean
 						.getSearchValue());
 				pw.print(result);
 				pw.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return null;
 	}
     
    //抽样人员
     public String JdSample_jsonTree1(){
  		if(bean.getOper() == 0){
  			return "JdSample_jsonTree1";
  		}else if(bean.getOper() == 1){
  			try {
  				response.setCharacterEncoding("UTF-8");
  				PrintWriter pw = response.getWriter();
  				String result = impl.getSampleUser(bean,bean.getCurPage(), bean
  						.getPerNumber(), bean.getOrderByField(), bean
  						.getOrderByType(), bean.getSearchField(), bean
  						.getSearchValue());
  				pw.print(result);
  				pw.close();
  			} catch (Exception e) {
  				e.printStackTrace();
  			}
  		}
  		return null;
  	}
     
     public String JdSample_TaskTree(){
  		if(bean.getOper() == 0){
  			return "JdSample_TaskTree";
  		}else if(bean.getOper() == 1){
  			try {
  				response.setCharacterEncoding("UTF-8");
  				PrintWriter pw = response.getWriter();
  				UserInfoBean userInfo = (UserInfoBean) request.getSession()
  						.getAttribute("userInfoBean");
  				bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0, 10));
  				String result = impl.getJdTaskList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getCompId());
  				pw.print(result);
  				pw.close();
  			} catch (Exception e) {
  				e.printStackTrace();
  			}
  		}
  		return null;
  	}
     
     public String JdSample_compTree() {
 		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
 		try {
 			treeList = impl.getCompanyAllRoot("D00");;
 		} catch (Exception se) {
 			se.printStackTrace();
 		}
 		JSONArray jsonArray = JSONArray.fromObject(treeList);
 		request.setAttribute("treeList", jsonArray);
 		return "JdSample_compTree";
 	}
    //抽样任务下发时只能读取本单位的部门 
 	public String JdSample_compTree3() {
 		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
 		try {
 			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0, 10));
 			treeList = impl.getCompanyAllRoot(bean.getCompId());;
 		} catch (Exception se) {
 			se.printStackTrace();
 		}
 		JSONArray jsonArray = JSONArray.fromObject(treeList);
 		request.setAttribute("treeList", jsonArray);
 		return "JdSample_compTree3";
 	}
     
     public String JdSample_compTree2() {
  		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
  		try {
  			treeList = impl.getCompanyAllRoot2();;
  		} catch (Exception se) {
  			se.printStackTrace();
  		}
  		JSONArray jsonArray = JSONArray.fromObject(treeList);
  		request.setAttribute("treeList", jsonArray);
  		return "JdSample_compTree2";
  	}
     

     public String JdSample_getPlanName() {
 		 response.setCharacterEncoding("UTF-8");
 		 PrintWriter pw = null;
    	 if (bean.getOper() == 1) {
 			Map<String, Object> map = null;
 			try {
 				pw = response.getWriter();
 				UserInfoBean userInfo = (UserInfoBean) request.getSession()
 						.getAttribute("userInfoBean");
 				bean.setCompId(userInfo.getEmployee().getCompanyId().substring(0, 10));
 				map = impl.getPlanName(bean.getTaskId(),bean.getCompId());
 				map.put("msgInfo", getText("inspect.jdsample.edit.success"));
 				JSONObject ob = new JSONObject();
 				ob.putAll(map);
 				pw.print(ob.toString());
 				pw.close();
 			} catch (Exception e) {
 				map = new HashMap<String, Object>();
 				map.put("msg", "false");
 				map.put("msgInfo", getText("inspect.jdsample.get.failure"));
 				JSONObject ob = new JSONObject();
 				ob.putAll(map);
 				pw.print(ob.toString());
 				pw.close();
 				e.printStackTrace();
 			}
 			return null;
 		} 
    	 return null;
     }

     public String JdSample_compTreeOut() {
  		List<ZTreeBean> treeList = new ArrayList<ZTreeBean>();
  		try {
  			treeList = impl.getCompanyAllRoot("D00");;
  		} catch (Exception se) {
  			se.printStackTrace();
  		}
  		JSONArray jsonArray = JSONArray.fromObject(treeList);
  		request.setAttribute("treeList", jsonArray);
  		return "JdSample_compTreeOut";
  	}
     

	public JdSample getModel() {
		return bean;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public List<JdSample> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdSample> dataRows) {
		this.dataRows = dataRows;
	}

}
