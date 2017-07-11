package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.JdInspectDataCheck;
import com.dhcc.inspect.impl.JdInspectDataCheckImpl;
import com.dhcc.login.UserInfoBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import framework.dhcc.page.Page;

/***********************
 * @author wangxp    
 * @version 1.0        
 * @created 2017-2-22    
 * 检验数据审核Action
 */
public class JdInspectDataCheckAction extends ActionSupport implements ModelDriven<JdInspectDataCheck>,
ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	JdInspectDataCheck bean = new JdInspectDataCheck();

	List<JdInspectDataCheck> dataRows = new ArrayList<JdInspectDataCheck>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdInspectDataCheckImpl impl = new JdInspectDataCheckImpl();

	Page page = new Page();
	
	//已提交审核的抽检方案信息列表
	public String JdInspectDataCheck_list() {
		if (bean.getOper() == 0) {
			request.setAttribute("items", bean.getItems());
			return "JdInspectDataCheck_list";
		} else {
			try {
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setCompanyId(userInfo.getEmployee().getOrgId());
				bean.setOptId(userInfo.getEmployee().getEmployeeId());
				bean.setCompId(userInfo.getEmployee().getCompanyId());
				String result = impl.getJdInspectDataCheckList(bean.getCurPage(),bean
						.getPerNumber(),bean.getOrderByField(),bean
						.getOrderByType(),bean.getSearchField(),bean
						.getSearchValue(),bean.getCompanyId(),bean.getCompId(),bean.getOptId());
				pw.print(result);
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	//检验数据信息审核
	public String JdInspectDataCheck_check(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectDataCheck_check";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdInspectDataCheckById(bean.getInspectId());
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(oper == 2){
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.addJdInspectDataCheck(bean);//检验数据信息审核成功
				map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.success"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else if(oper == 3){
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.dataCheckToSample(bean);//退回，修改抽样单信息
				if("1".equals(bean.getBackType())){//直接
					map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.toSample1"));
				}else{
					map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.toSample2"));
				}
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.failure"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
				e.printStackTrace();
			}
			return null;
		}else if(oper == 4){
			Map<String ,Object> map = null;
			try {
				pw = response.getWriter();
				UserInfoBean userInfo = (UserInfoBean) request.getSession()
						.getAttribute("userInfoBean");
				bean.setOptId2(userInfo.getEmployee().getEmployeeId());
				bean.setOptName2(userInfo.getEmployee().getUserName());
				bean.setCompanyId(userInfo.getEmployee().getCompanyId());
				bean.setCompanyName(userInfo.getEmployee().getCompanyName());
				map = impl.dataCheckToInspect(bean);//退回，修改检验数据
				map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.ToInspect"));
				JSONObject ob = new JSONObject();
				ob.putAll(map);
				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				map = new HashMap<String, Object>();
				map.put("msg", "false");
				map.put("msgInfo", getText("inspect.jdInspectDataCheck.add.failure"));
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
	
	//检验数据信息详情
	public String JdInspectDataCheck_detail(){
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		if (bean.getOper() == 0) {
			return "JdInspectDataCheck_detail";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getJdInspectDataCheckById(bean.getInspectId());
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

	public List<JdInspectDataCheck> getDataRows() {
		return dataRows;
	}

	public void setDataRows(List<JdInspectDataCheck> dataRows) {
		this.dataRows = dataRows;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public JdInspectDataCheck getModel() {
		return bean;
	}

}
