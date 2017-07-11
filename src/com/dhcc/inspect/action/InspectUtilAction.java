package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.inspect.domain.InspectUtil;
import com.dhcc.inspect.impl.InspectUtilImpl;
import com.dhcc.login.UserInfoBean;
import com.dhcc.utils.FlowConstant;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @ClassName: LegalUtilAction 
 * @Description: 用于执法中选择部门、人员的同一个页面出现两个autoComplete的情况
 * @author luk 
 * @date 2017-3-6 下午7:45:38
 */
public class InspectUtilAction  extends ActionSupport implements 
ModelDriven<InspectUtil>,ServletRequestAware,ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;
	InspectUtil bean = new InspectUtil();
	InspectUtilImpl impl  = new InspectUtilImpl();
	
	/*获取部门和人员*/
	public String InspectUtil_companyList() {
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			UserInfoBean userInfo = (UserInfoBean) request.getSession()
					.getAttribute("userInfoBean");
			String deptId = userInfo.getEmployee().getOrgId();
			String result = impl.getCompanyAndPerson(deptId);
			pw.print(result);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*获取抽样机构部门和人员*/
	public String InspectUtil_handleDeptList() {
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			String result = impl.getSampleCompanyAndPerson(bean.getHandleDeptId());
			pw.print(result);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//获取下一步骤信息
	public String InspectUtil_getStep(){
		Map<String ,Object> map =  new HashMap<String, Object>();
		PrintWriter pw = null;
		try {
			response.setCharacterEncoding("UTF-8");
			pw = response.getWriter();
			if(bean.getStepStr().equals("1")){
				map = impl.getMyInfo(bean.getTranId(),FlowConstant.CC_STEP_ARR_ID[Integer.parseInt(bean.getIndex())]);
			}else if(bean.getStepStr().equals("2")){
				map = impl.getMyInfo(bean.getTranId(),FlowConstant.CY_STEP_ARR_ID[Integer.parseInt(bean.getIndex())]);
			}else if(bean.getStepStr().equals("3")){
				map = impl.getMyInfo(bean.getTranId(),FlowConstant.CJ_STEP_ARR_ID[Integer.parseInt(bean.getIndex())]);
			}
			JSONObject ob = new JSONObject();
			ob.putAll(map);
			pw.print(ob.toString());
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public InspectUtil getModel() {
		return bean;
	}

}
