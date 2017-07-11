package com.dhcc.inspect.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.dhcc.inspect.domain.JdSample;
import com.dhcc.inspect.impl.JdSampleShowBillImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/***********************
 * @author yangrc    
 * @version 1.0        
 * @created 2017-3-24    
 ***********************
 */
public class JdSampleShowBillAction extends ActionSupport implements ModelDriven<JdSample>,
ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JdSample bean = new JdSample();

	List<JdSample> dataRows = new ArrayList<JdSample>();

	HttpServletRequest request;

	HttpServletResponse response;

	JdSampleShowBillImpl impl = new JdSampleShowBillImpl();
	
	public String JdSampleShowBill_list() {
		
		int oper = bean.getOper();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		System.out.println(bean.getSampleId());
		if (bean.getOper() == 0) {
			return "JdSampleShowBill_list";
		} else if (oper == 1) {
			try {
				pw = response.getWriter();
				Map<String, Object> map = impl.getBillCount(bean.getCurPage(), bean.getPerNumber(), bean.getOrderByField(),
						bean.getOrderByType(), bean.getSearchField(), bean.getSearchValue(),bean.getOptId(),bean.getSampleId());
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
