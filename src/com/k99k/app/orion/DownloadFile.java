package com.k99k.app.orion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadFile
 */
public class DownloadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String to = "k99kwall.apk";
		if(request.getParameter("pk")!=null){
			String pk = request.getParameter("pk");
			//com.k99k.app.orion
			if(pk.toLowerCase().indexOf("app")>-1){
				
				to = "orionwall.apk";
				//RequestDispatcher rd = request.getRequestDispatcher(to);
				//rd.forward(request, response);
			}
		}else if(request.getParameter("lang")!=null ){
			String lang = request.getParameter("lang");
			if(lang.toUpperCase().equals("CN")){
				to = "orionwall.apk";
			}
			//RequestDispatcher rd = request.getRequestDispatcher(to);
			//rd.forward(request, response);
		}
		
		
		String contentType = getServletContext().getMimeType(to);

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		response.setContentType(contentType);

		// 设置response的头信息 ----这里是指的下载文件名?会产品多次下载的问题

		response.setHeader("Content-disposition", "attachment;filename=\"" + to + "\"");
		String filePath = this.getServletContext().getRealPath("/")+"WEB-INF/"+to;
		
		InputStream is = null;
		OutputStream os = null;
		try {
			
			is = new BufferedInputStream(new FileInputStream(filePath));
			// 定义输出字节流
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 定义response的输出流
			os = new BufferedOutputStream(response.getOutputStream());
			// 定义buffer
			byte[] buffer = new byte[4 * 1024]; // 4k Buffer
			int read = 0;
			// 从文件中读入数据并写到输出字节流中
			while ((read = is.read(buffer)) != -1) {
				baos.write(buffer, 0, read);
			}

			// 将输出字节流写到response的输出流中
			os.write(baos.toByteArray());
		} catch (IOException e) {
			//e.printStackTrace();

		} finally {
			// 关闭输出字节流和response输出流
			os.close();
			is.close();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
