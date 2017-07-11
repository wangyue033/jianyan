package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.impl.JdSampleSignImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleBaseAction extends ActionSupport implements ModelDriven<JdSample>,
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JdSample bean = new JdSample();

	List<JdSample> dataRows = new ArrayList<JdSample>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdSampleSignImpl impl = new JdSampleSignImpl();
	
	public String JdSampleBase_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdSampleBase_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				bean.setSignCompId(userInfo.getEmployee().getCompanyId());
				String result = impl.getJdSampleBaseList(bean.getCurPage(), bean
						.getPerNumber(), bean.getOrderByField(), bean
						.getOrderByType(), bean.getSearchField(), bean
						.getSearchValue(),bean.getOptId(),bean.getSignCompId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public String JdSampleBase_add() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleBase_add";
		}else if(oper == 1){
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdSampleSignById(bean.getSampleId());
				map.put("msgInfo", getText("inspect.jdsamplebase.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.get.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		} 
		else if (oper == 2) {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setOptName(userInfo.getEmployee().getUserName());
				impl.addJdSampleSign(bean);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdsamplebase.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		}
		else {
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.add.noparam"));
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

	public String JdSampleBase_edit() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleBase_edit";
		} else if (oper == 1) {
			Map<String, Object> map = null;
			try {
				pw = response.getWriter();
				map = impl.getJdSampleSignByIdone(bean.getSampleId());
				map.put("msgInfo", getText("inspect.jdsamplebase.edit.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.get.failure"));
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
				impl.editJdSampleBase(bean);
				map = new LinkedHashMap<String, Object>();
				map.put("msg", "true");
				map.put("msgInfo", getText("inspect.jdsamplebase.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				response.setCharacterEncoding("UTF-8");
			    map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			}
			return null;
		} else {
			try {
				pw = response.getWriter();
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdsamplebase.edit.noparam"));
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

	public String JdSampleBase_del() {
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
				map = impl.delJdSampleBase(bean);
				map.put("msgInfo", getText("inspect.jdsamplebase.delete.success"));
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
	
	public String JdSampleBase_show() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleBase_show";
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
	
	public String JdSampleBase_detail() {
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdSampleBase_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdSampleSignByIdone(bean.getSampleId());
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
	//选择抽样单录入里面的抽样产品
	public String JdSampleBase_jsonTree() {
		if(bean.getOper() == 0){
  			return "JdSampleBase_jsonTree";
  		}else if(bean.getOper() == 1){
  			try {
  				response.setCharacterEncoding("UTF-8");
  				PrintWriter pw = response.getWriter();
  				System.out.println(bean.getSampleId()+"抽样任务编号");
  				String result = impl.getProductMap(bean,bean.getCurPage(), bean
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
	//上传抽样信息里面的抽样产品
	public String JdSampleBase_addJsonTree() {
		if(bean.getOper() == 0){
  			return "JdSampleBase_addJsonTree";
  		}else if(bean.getOper() == 1){
  			try {
  				response.setCharacterEncoding("UTF-8");
  				PrintWriter pw = response.getWriter();
  				String result = impl.getProductMap2(bean,bean.getCurPage(), bean
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
	
	public String JdSampleBase_addBase() {
		if (bean.getOper() == 0) {
			return "JdSampleBase_addBase";
		}
		return null;
	}
	/*上传抽样基础信息时选择抽样人员*/
	public String JdSampleBase_sampleUser() {
		if (bean.getOper() == 0) {
			return "JdSampleBase_sampleUser";
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
