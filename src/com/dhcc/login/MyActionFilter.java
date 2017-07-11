package com.dhcc.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class MyActionFilter extends StrutsPrepareAndExecuteFilter {

	public boolean isInit(String requestUrl) {
		boolean flag = false;
		if (requestUrl.indexOf("/Menu_init.action") == -1
				&& requestUrl.indexOf("/Menu_tree.action") == -1
				&& requestUrl.indexOf("/Menu_list.action") == -1
				&& requestUrl.indexOf("/MenuJson.action") == -1
				&& requestUrl.indexOf("/Menu_addMenu.action") == -1
				&& requestUrl.indexOf("/Menu_editMenu.action") == -1
				&& requestUrl.indexOf("/Menu_addFunc.action") == -1
				&& requestUrl.indexOf("/Menu_editFunc.action") == -1
				&& requestUrl.indexOf("/Menu_setFunc.action") == -1
				&& requestUrl.indexOf("/Menu_delFunc.action") == -1
				&& requestUrl.indexOf("/Code_init.action") == -1
				&& requestUrl.indexOf("/Upload_file.action") == -1
				&& requestUrl.indexOf("/UploadOut_file.action") == -1
				&& requestUrl.indexOf("/Download_file.action") == -1) {
			flag = true;
		}
		return flag;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 这三个是去除缓冲的，不确定是否可用，需要测试
		response.setHeader("Pragma", "No-Cache");
		response.setHeader("Cache-Control", "No-Cache");
		response.setDateHeader("Expires", 0);

		String requestUrl = processPath(request, response);
		
		if (requestUrl.endsWith(".action")) {
			if (requestUrl.indexOf("/login_in.action") == -1
					&& requestUrl.indexOf("/login_out.action") == -1
					&& requestUrl.indexOf("/login_menu.action") == -1
					&& requestUrl.indexOf("/PdInit_list.action") == -1
					&& requestUrl.indexOf("/PdInit_init.action") == -1
					&& requestUrl.indexOf("/PdInit_add.action") == -1
					&& requestUrl.indexOf("/PdInit_code.action") == -1
					&& requestUrl.indexOf("/PdInitJson.action") == -1
					&& requestUrl.indexOf("/login_register.action") == -1
					&& requestUrl.indexOf("/User_upload.action") == -1
					&& isInit(requestUrl)) {
				HttpSession session = request.getSession(true);
				UserInfoBean bean = (UserInfoBean) session
						.getAttribute("userInfoBean");
				if (bean == null) {
					try {
						response.setCharacterEncoding("UTF-8");
						PrintWriter out = response.getWriter();
						out.println("<html><head>");
						out.println("<title></title></head>");
						out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
						out.println("<body onload=\"pageDirect()\">");
						out.println("<script language=\"JavaScript\" type=\"text/JavaScript\">");
						out.println("<!--");
						out.println("function pageDirect() {");
						out.println("alert('登录过期或者用户已经被强制登录，将跳转到登录页面！');");
						out.println("window.top.location.replace(\""
								+ request.getContextPath() + "/login/index.jsp"
								+ "\");");
						out.println("}");
						out.println("//-->");
						out.println("</script>");
						out.println("</body></html>");
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		
		super.doFilter(req, res, chain);
	}

	private String processPath(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String path = null;

		// For prefix matching, match on the path info (if any)
		path = (String) request.getAttribute("javax.servlet.include.path_info");
		if (path == null) {
			path = request.getPathInfo();
		}
		if ((path != null) && (path.length() > 0)) {
			return (path);
		}

		// For extension matching, strip the module prefix and extension
		path = (String) request
				.getAttribute("javax.servlet.include.servlet_path");
		if (path == null) {
			path = request.getServletPath();
		}

		return (path);

	}

}
