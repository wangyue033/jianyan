package com.dhcc.uploadFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.dhcc.utils.ResizeImage;
import com.opensymphony.xwork2.ActionSupport;

public class UploadFileAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 598168317711085422L;

	private String[] myfileFileName;

	private String[] myfileFileType;

	private File[] myfile;

	private File file;

	private String fileFileName;

	HttpServletRequest request;

	HttpServletResponse response;

	private String modulePath;

	private String attFiles;

	private String removeAtt;

	private String toRemove;

	List<FileInfo> selectList = new ArrayList<FileInfo>();
	
	private String index;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String result = sdf.format(date);
		return result;
	}

	public static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = sdf.format(date);
		String count = "" + (int) (Math.random() * 1000000);
		if (count.length() < 6) {
			for (int i = 0; i < 6 - count.length(); i++) {
				result = result + "0";
			}
		}
		return result + count;
	}

	public String execute() {
		String oper = request.getParameter("oper");
		String result = "";
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.lastIndexOf("\\"));
		String date = getDate();
		if (oper == null || "0".equals(oper)) {
			FileInfo info = null;
			String[] str = attFiles.split(";;");
			for (int i = 0; i < str.length; i++) {
				if (i % 2 == 1) {
					info.setFilePath(str[i]);
					selectList.add(info);
				} else {
					info = new FileInfo();
					info.setFileName(str[i]);
				}
			}
		} else if ("1".equals(oper)) {

			if (myfile != null && myfile.length != 0) {
				for (int i = 0; i < myfile.length; i++) {
					File file = myfile[i];

					if (file.length() > 1024 * 1024 * 5) {
						result = "文件 " + myfileFileName[i] + " 超过限制大小5M，上传失败！"; // 文件超过限制大小
					} else {
						FileInfo info = new FileInfo();
						info.setFileName(myfileFileName[i]);
						try {
							InputStream in = new FileInputStream(file);
							String filePath = path + "/upload/" + modulePath;

							filePath = filePath + "/" + date;
							if (!new File(filePath).exists()) {
								new File(filePath).mkdirs();
							}

							String dateTime = getDateTime();
							filePath = filePath + "/" + dateTime + "_"
									+ myfileFileName[i];

							info.setFilePath("/upload/" + modulePath + "/"
									+ date + "/" + dateTime + "_"
									+ myfileFileName[i]);
							OutputStream out = new FileOutputStream(filePath);
							int len = 0;
							byte[] by = new byte[1024];
							while ((len = in.read(by)) > 0) {
								out.write(by, 0, len);
							}
							in.close();
							out.close();

						} catch (Exception e) {
							e.printStackTrace();
						}
						selectList.add(info);
					}

				}
			}
		} else if ("2".equals(oper)) {
			int index = Integer.parseInt(toRemove.substring(toRemove
					.lastIndexOf(".") + 1, toRemove.length()));
			FileInfo info = selectList.get(index);
			String filePath = info.getFilePath();
			File file = new File(path + filePath);
			if (file.exists()) {
				file.delete();
			}
			selectList.remove(index);
		}

		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < selectList.size(); i++) {
			FileInfo info = selectList.get(i);
			if (i == selectList.size() - 1) {
				sbf.append(info.getFileName() + ";;" + info.getFilePath());
			} else {
				sbf.append(info.getFileName() + ";;" + info.getFilePath()
						+ ";;");
			}
		}
		attFiles = sbf.toString();

		request.setAttribute("result", result);
		return SUCCESS;
	}

	public String Drop_upload() {
		String oper = request.getParameter("oper");
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.lastIndexOf("\\"));
		String date = getDate();
		if (oper == null || "0".equals(oper)) {
			FileInfo info = null;
			String[] str = attFiles.split(";;");
			for (int i = 0; i < str.length; i++) {
				if (i % 2 == 1) {
					info.setFilePath(str[i]);
					selectList.add(info);
				} else {
					info = new FileInfo();
					info.setFileName(str[i]);
				}
			}

		} else if ("1".equals(oper)) {
			FileInfo info = new FileInfo();
			info.setFileName(fileFileName);
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();

				InputStream in = new FileInputStream(file);
				String filePath = path + "/upload/" + modulePath;

				filePath = filePath + "/" + date;
				if (!new File(filePath).exists()) {
					new File(filePath).mkdirs();
				}

				String dateTime = getDateTime();
				filePath = filePath + "/" + dateTime + "_" + fileFileName;

				info.setFilePath("/upload/" + modulePath + "/" + date + "/"
						+ dateTime + "_" + fileFileName);
				OutputStream out = new FileOutputStream(filePath);
				int len = 0;
				byte[] by = new byte[1024];
				while ((len = in.read(by)) > 0) {
					out.write(by, 0, len);
				}
				in.close();
				out.close();
				selectList.add(info);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("fileName", info.getFileName());
				map.put("filePath", info.getFilePath());
				map.put("newFile", dateTime + "_" + fileFileName);

				if (fileFileName.endsWith(".jpg")
						|| fileFileName.endsWith(".png")
						|| fileFileName.endsWith(".gif")
						|| fileFileName.endsWith(".bmp")
						|| fileFileName.endsWith(".jpeg")) {
					ResizeImage r = new ResizeImage();
					BufferedImage image = r.getImage(filePath);

					r.writeHighQuality(r.zoomImage(image), filePath.replace(
							"_", "_sl"));
				}

				JSONObject ob = new JSONObject();
				ob.putAll(map);

				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else if ("2".equals(oper)) {
			response.setCharacterEncoding("UTF-8");
			String fileName = path + fileFileName;
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			File fileSl = new File(fileName.replace("_", "_sl"));
			if (fileSl.exists()) {
				fileSl.delete();
			}
			return null;
		}
		return SUCCESS;
	}
	
	public String Sign_upload() {
		String oper = request.getParameter("oper");
		String path = request.getSession().getServletContext().getRealPath("");
		path = path.substring(0, path.lastIndexOf("\\"));
		String date = getDate();
		if (oper == null || "0".equals(oper)) {
			FileInfo info = null;
			String[] str = attFiles.split(";;");
			for (int i = 0; i < str.length; i++) {
				if (i % 2 == 1) {
					info.setFilePath(str[i]);
					selectList.add(info);
				} else {
					info = new FileInfo();
					info.setFileName(str[i]);
				}
			}

		} else if ("1".equals(oper)) {
			FileInfo info = new FileInfo();
			info.setFileName(fileFileName);
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();

				InputStream in = new FileInputStream(file);
				String filePath = path + "/sign";

				filePath = filePath + "/" + date;
				if (!new File(filePath).exists()) {
					new File(filePath).mkdirs();
				}

				String dateTime = getDateTime();
				filePath = filePath + "/" + dateTime + "_" + fileFileName;

				info.setFilePath("/sign/" + date + "/"
						+ dateTime + "_" + fileFileName);
				OutputStream out = new FileOutputStream(filePath);
				int len = 0;
				byte[] by = new byte[1024];
				while ((len = in.read(by)) > 0) {
					out.write(by, 0, len);
				}
				in.close();
				out.close();
				selectList.add(info);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("fileName", info.getFileName());
				map.put("filePath", info.getFilePath());
				map.put("newFile", dateTime + "_" + fileFileName);

				if (fileFileName.endsWith(".jpg")
						|| fileFileName.endsWith(".png")
						|| fileFileName.endsWith(".gif")
						|| fileFileName.endsWith(".bmp")
						|| fileFileName.endsWith(".jpeg")) {
					ResizeImage r = new ResizeImage();
					BufferedImage image = r.getImage(filePath);

					r.writeHighQuality(r.zoomImage(image), filePath.replace(
							"_", "_sl"));
				}

				JSONObject ob = new JSONObject();
				ob.putAll(map);

				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else if ("2".equals(oper)) {
			response.setCharacterEncoding("UTF-8");
			String fileName = path + fileFileName;
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			File fileSl = new File(fileName.replace("_", "_sl"));
			if (fileSl.exists()) {
				fileSl.delete();
			}
			return null;
		}
		return SUCCESS;
	}
	
	public String Report_upload() {
		String path = request.getSession().getServletContext().getRealPath("");
		String oper = request.getParameter("oper");
		path = path.substring(0, path.lastIndexOf("\\"));
		System.out.println(path);
		if (oper == null || "0".equals(oper)) {
			FileInfo info = null;
			String[] str = attFiles.split(";;");
			for (int i = 0; i < str.length; i++) {
				if (i % 2 == 1) {
					info.setFilePath(str[i]);
					selectList.add(info);
				} else {
					info = new FileInfo();
					info.setFileName(str[i]);
				}
			}

		} else if ("1".equals(oper)) {
			FileInfo info = new FileInfo();
			info.setFileName(fileFileName);
			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();

				InputStream in = new FileInputStream(file);
				String filePath = path + "\\report";

				if (!new File(filePath).exists()) {
					new File(filePath).mkdirs();
				}

				String dateTime = getDateTime();
				filePath = filePath + "\\" + dateTime + "-" + fileFileName;

				info.setFilePath("\\report\\"+ dateTime + "-" + fileFileName);
				OutputStream out = new FileOutputStream(filePath);
				int len = 0;
				byte[] by = new byte[1024];
				while ((len = in.read(by)) > 0) {
					out.write(by, 0, len);
				}
				in.close();
				out.close();
				selectList.add(info);
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("fileName", info.getFileName());
				map.put("filePath", path+info.getFilePath());
				map.put("newFile", dateTime + "-" + fileFileName);
				

				JSONObject ob = new JSONObject();
				ob.putAll(map);

				pw.print(ob.toString());
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else if ("2".equals(oper)) {
			response.setCharacterEncoding("UTF-8");
			String fileName = path + fileFileName;
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
			File fileSl = new File(fileName.replace("_", "_sl"));
			if (fileSl.exists()) {
				fileSl.delete();
			}
			return null;
		}
		return SUCCESS;
	}

	public List<FileInfo> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<FileInfo> selectList) {
		this.selectList = selectList;
	}

	public String getRemoveAtt() {
		return removeAtt;
	}

	public void setRemoveAtt(String removeAtt) {
		this.removeAtt = removeAtt;
	}

	public String getToRemove() {
		return toRemove;
	}

	public void setToRemove(String toRemove) {
		this.toRemove = toRemove;
	}

	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	public String[] getMyfileFileType() {
		return myfileFileType;
	}

	public void setMyfileFileType(String[] myfileFileType) {
		this.myfileFileType = myfileFileType;
	}

	public String[] getMyfileFileName() {
		return myfileFileName;
	}

	public void setMyfileFileName(String[] myfileFileName) {
		this.myfileFileName = myfileFileName;
	}

	public File[] getMyfile() {
		return myfile;
	}

	public void setMyfile(File[] myfile) {
		this.myfile = myfile;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public String getAttFiles() {
		return attFiles;
	}

	public void setAttFiles(String attFiles) {
		this.attFiles = attFiles;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

}
