package com.k99k.app.orion;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.k99k.tools.IO;
import com.k99k.tools.StringUnit;
import com.k99k.tools.encrypter.Encrypter;
import org.stringtree.json.*;

/**
 * Servlet Filter implementation class AppFilter
 */
public class AppFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AppFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		fwall.exit();
	}
	
	private static FWall fwall;// = new FWall("/WEB-INF/fw_ini.json");
	
//	private boolean test = false;
	
//	private final static JSONReader jsonReader = new JSONValidatingReader();
	
//	/**
//	 * 加密用的key
//	 * TODO 未实现密钥网络更新机制
//	 */
//	static final String encryptKey = "htHunter01_!(!)";
	
	/**
	 * 加密器
	 */
	static final Encrypter desEncrypt = createEncrypter();
	
	private final static Encrypter createEncrypter(){
		try {
			return new Encrypter();
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 标记使用中国服务器的区域
	 */
	static final HashMap<String, String> CNMap = createCNMap();
	
	private final static HashMap<String, String> createCNMap(){
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("CN", "CN");
		m.put("TW", "TW");
		//m.put("JP", "JP");
		return m;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
//		Enumeration heads = req.getHeaderNames();
//		System.out.println(req.getRequestURL());
//		System.out.println(req.getRemoteAddr());
//		StringBuilder sb = new StringBuilder();
//		while(heads.hasMoreElements())
//		{
//		   String item = (String)heads.nextElement();
//		   sb.append(item);
//		   sb.append(":");
//		   sb.append(req.getHeader(item)).append("\n");
//		}
//		System.out.println(sb);
		resp.setHeader("obj_id", "objectId");
		
		String url = req.getRequestURI();
		
		String pic_oid = "";
		String imei = "";
		String type = "";
		String lang = "CN";
		pic_oid = (req.getHeader("pic_oid") == null)?"":req.getHeader("pic_oid");
		imei = (req.getHeader("imei") == null || req.getHeader("imei").length()<5)?"":req.getHeader("imei");
		type = (req.getHeader("type") == null)?"":req.getHeader("type");
		lang = (req.getHeader("lang") == null)?"CN":req.getHeader("lang");
		
//		System.out.println(url);
		//-----------------------------------
		boolean isNewReq = (req.getParameter("wall")!= null);
		//新的请求处理
		if (isNewReq) {
			//处理登录
			if (url.indexOf("fw_ini")>0) {
				try {
					String reqStr = (String)req.getParameter("wall");
					String deStr = desEncrypt.decrypt(reqStr);
					HashMap<String,Object> loginTask = (HashMap<String, Object>) (new JSONValidatingReader().read(deStr));
					loginTask.put("user-agent", req.getHeader("user-agent"));
					loginTask.put("ip", req.getRemoteAddr());
					fwall.addTask(loginTask);
					String la = loginTask.get("lang").toString();
					if (CNMap.containsKey(la)) {
						lang = la;
						response.getWriter().print(fw_ini_html);
					}else{
						response.getWriter().print(fw_ini_html_us);
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
				chain.doFilter(request, response);
				return;
			}
			//处理fw_index
			if (url.indexOf("fw_index")>0) {
				try {
					String reqStr = (String)req.getParameter("wall");
					String deStr = desEncrypt.decrypt(reqStr);
					HashMap<String,Object> jsonReq = (HashMap<String, Object>) (new JSONValidatingReader().read(deStr));
					String la = jsonReq.get("lang").toString();
					if (CNMap.containsKey(la)) {
						lang = la;
						response.getWriter().print(fwall.getWallconfig());
					}else{
						response.getWriter().print(fwall.getWallconfig_us());
					}
				} catch (Exception e) {
					e.printStackTrace();
					chain.doFilter(request, response);
					return;
				}
				return;
				
//				if (fwall.getWallconfig().length() > 3) {
//					if (CNMap.containsKey(lang)) {
//						response.getWriter().print(fwall.getWallconfig());
//					}else{
//						response.getWriter().print(fwall.getWallconfig_us());
//					}
//					return;
//				}else{
//					chain.doFilter(request, response);
//					return;
//				}
			}
			//处理广告
			if (url.indexOf("getadtype")>0) {
				String ks = "s";
				if (req.getParameter("acti") != null) {
					ks = req.getParameter("acti");
				}
				//System.out.println(req.getParameter("acti"));
				String ad = "youmi";
				//按随机比率取广告
				if (StringUnit.getRandomInt(0, 100) < fwall.getWoobooADcent(ks)) {
					ad = "wooboo";
				}else{
					ad = "youmi";
				}
				response.getWriter().print(ad);
				return;
			}
//			//处理图片请求--在后面单独处理
//			if (url.indexOf(".jpg")>0) {
//				
//			}
			//确定pic_oid和imei
			if (req.getParameter("pic_oid")!=null && req.getParameter("pic_oid").length()>2) {
				pic_oid = req.getParameter("pic_oid").toString();
			}
			if (req.getParameter("type")!=null&& req.getParameter("type").length()>0) {
				type = req.getParameter("type").toString();
			}
			String reqStr = (String)req.getParameter("wall");
			String deStr = "";
			try {
				deStr = desEncrypt.decrypt(reqStr);
				String s = ((HashMap<String,Object>)(new JSONValidatingReader().read(deStr))).get("imei").toString();
				if (s != null && s.length() >5) {
					imei = s;
				}
			} catch (Exception e) {
				System.out.println("reqStr:"+reqStr);
				System.out.println("deStr:"+deStr);
				e.printStackTrace();
				imei = "";
			}
			//此处不return,未匹配时向下走
		}
		
		//处理登录(老)
		if (url.indexOf("fw_index")>0 && !isNewReq) {
			if (!imei.equals("")) {
				HashMap<String,Object> loginTask = new HashMap<String,Object>();
				loginTask.put("imei",imei);
				loginTask.put("userName", (req.getHeader("userName")==null)?"Noname":req.getHeader("userName"));
				loginTask.put("imsi", (req.getHeader("imsi")==null)?"":req.getHeader("imsi"));
				loginTask.put("width", (req.getHeader("width")==null)?"0":req.getHeader("width"));
				loginTask.put("height", (req.getHeader("height")==null)?"0":req.getHeader("height"));
				loginTask.put("dpi", (req.getHeader("dpi")==null)?"0":req.getHeader("dpi"));
				loginTask.put("display", req.getHeader("display"));
				loginTask.put("board", req.getHeader("board"));
				loginTask.put("brand", req.getHeader("brand"));
				loginTask.put("fingerprint", req.getHeader("fingerprint"));
				loginTask.put("device", req.getHeader("device"));
				loginTask.put("host", req.getHeader("host"));
				loginTask.put("id", req.getHeader("id"));
				loginTask.put("model", req.getHeader("model"));
				loginTask.put("product", req.getHeader("product"));
				loginTask.put("tags", req.getHeader("tags"));
				loginTask.put("type", req.getHeader("type"));
				loginTask.put("user", req.getHeader("user"));
				loginTask.put("user-agent", req.getHeader("user-agent"));
				loginTask.put("appVersion", (req.getHeader("appVersion")==null)?"":req.getHeader("appVersion"));
				loginTask.put("lang", lang);
				loginTask.put("ip", req.getRemoteAddr());
				fwall.addTask(loginTask);
			}
			
			if (fwall.getWallconfig().length() > 3) {
				if (CNMap.containsKey(lang)) {
					response.getWriter().print(fwall.getWallconfig());
				}else{
					response.getWriter().print(fwall.getWallconfig_us());
				}
				return;
			}else{
				chain.doFilter(request, response);
				return;
			}
		}
		//老的
		if (url.indexOf("fw_ini")>0) {
			if (CNMap.containsKey(lang)) {
				response.getWriter().print(fw_ini_html);
			}else{
				response.getWriter().print(fw_ini_html_us);
			}
			return;
		}
		
		//-----------------------------------
		if (url.indexOf(".jpg")>0) {
			
			try {
				//由pic_oid直接到真实path,路径中的图片名以_或b__开头，后接objectId
				String picFileName = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
				if (picFileName.charAt(0) == '_' || picFileName.charAt(2) == '_') {
					String[] picArr = fwall.getPicPathByOid(picFileName);
					if (picArr == null || picArr.length < 2) {
						System.out.println("getPicPathByOid failed:"+url);
						chain.doFilter(request, response);
						return;
					}
					resp.setHeader("pic_oid", picArr[0]);
					RequestDispatcher dispatcher = req.getRequestDispatcher(picArr[1]);
					dispatcher.forward(request, resp);
					return;
				}
				String sortBy = "time";
				String sortType = "0";
//			System.out.println("====test imei========:"+req.getHeader("imei"));
//			if (req.getParameter("wall")!= null && req.getParameter("sortBy")!= null && req.getParameter("sortType")!= null) {
//				sortBy = req.getParameter("sortBy");
//				sortType = req.getParameter("sortType");
//				
//			}else{
				//处理图片重定向
					sortBy = (req.getHeader("sortBy") == null)?"time":req.getHeader("sortBy");
					sortType = (req.getHeader("sortType") == null || (!req.getHeader("sortType").matches("\\d+"))) ? "0" : req.getHeader("sortType");
//			System.out.println("sortBy:"+sortBy+" sortType:"+sortType);
//			}
				
				String[] toPic = fwall.getPicFromUrl(url, sortBy, Integer.parseInt(sortType));
				if (toPic != null && toPic.length == 2) {
					//System.out.println(toPic);
					resp.setHeader("pic_oid", toPic[0]);
//				if (test) {
//					String s = "http://202.102.113.204"+toPic[1];
//					resp.sendRedirect(s);
//					return;
//				}
					RequestDispatcher dispatcher = req.getRequestDispatcher(toPic[1]);
					dispatcher.forward(request, resp);
					return;
				}else{
					//System.out.println("ERROR URL:"+url);
					chain.doFilter(request, response);
					//response.getWriter().print("404");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//-----------------------------------
		//处理增加下载量请求
		if (url.indexOf("adddown") >0) {
			//String pic_oid = req.getHeader("pic_oid");
			if (imei.equals("") || pic_oid.equals("") || pic_oid.equals("null")) {
				//非手机请求,不处理
				response.getWriter().print("");
				return;
			}else{
				
				if(fwall.addDown(pic_oid, imei)){
					response.getWriter().print("ok");
					return;
				}
				response.getWriter().print("fail");
				return;
			}
		}
		//-----------------------------------
		//处理星星请求
		if (url.indexOf("addstar") >0) {
			//String type = req.getHeader("type");
			//pic_oid = req.getHeader("pic_oid");
			//System.out.println("type:"+type+" pic_oid:"+pic_oid+" imei:"+req.getHeader("imei"));
			if (imei.equals("")|| pic_oid.equals("") || pic_oid.equals("null")) {
				//非手机请求,不处理
				response.getWriter().print("");
				return;
			}else{
				
				if (type.equals("add")) {
					if(fwall.addStar(pic_oid, imei)){
						if (isNewReq) {
							response.getWriter().print(pic_oid);
							return;
						}
						response.getWriter().print(fwall.getCatePicId(pic_oid));
						return;
					}
				}else if(type.equals("del")){
					if(fwall.cancelStar(pic_oid, imei)){
						if (isNewReq) {
							response.getWriter().print(pic_oid);
							return;
						}
						response.getWriter().print(fwall.getCatePicId(pic_oid));
						return;
					}
				}
				response.getWriter().print("fail");
				return;
			}
		}
		if (url.indexOf("getstars") >0) {
			if (imei.equals("")) {
				//非手机请求,不处理
				response.getWriter().print("[]");
				return;
			}else{
				if (isNewReq) {
					response.getWriter().print(fwall.getStarIndexByUser(imei));
					return;
				}
				String index = fwall.getStarIndexByUserOld(imei);
				//无论是否为空，直接返回
				response.getWriter().print(index);
				return;
			}
		}
//		//查看index文件
//		if (url.indexOf("lookforindex_us") > -1) {
//			response.getWriter().print(fwall.getWallconfig_us());
//			return;
//		}
		//-----------------------------------
		//查看index文件
		if (url.indexOf("lookforindex") > -1) {
			response.getWriter().print(fwall.getWallconfig());
			return;
		}

		//-----------------------------------
		//重新初始化
		if (url.indexOf("initfwall") > -1) {
			response.getWriter().print(fwall.init());
			return;
		}
		//-----------------------------------
		//addNewPicsTask
		if (url.indexOf("addnewpics") > -1) {
			String oid = fwall.addNewPicsTask();
			resp.sendRedirect("/fws/newpictask.jsp?oid="+oid);
			return;
		}
		//-----------------------------------
		//setnewpicsoneday
		if (url.indexOf("setnewpicsoneday") > -1) {
			String re = "";
			String cc = req.getParameter("setnewpicsoneday");
			if (cc != null && cc.matches("\\d")) {
				int cday = Integer.parseInt(cc);
				fwall.setNewPicsOneDay(cday);
				re = "Update OK.";
			}
			re += "NewPicsOneDay:"+fwall.getNewPicsOneDay()+" nextUpdateTime:"+fwall.getNextDayUpdate();
			response.getWriter().print(re);
			return;
		}		
		//-----------------------------------
		//updatenewpicsnow
		if (url.indexOf("updatenewpicsnow") > -1) {
			response.getWriter().print( fwall.updateNewPicsNow());
			return;
		}
		//-----------------------------------
		//getMongoConState
		if (url.indexOf("getmongostate") > -1) {
			StringBuilder sb = new StringBuilder();
			sb.append("server:").append(fwall.getMongoCol().getIp());
			sb.append("<br /> port:").append(fwall.getMongoCol().getPort());
			response.getWriter().print(sb.toString());
			return;
		}
		//-----------------------------------
		//重新初始化Fwall
		if (url.indexOf("reloadfwallini") > -1) {
			this.reloadIni();
			response.getWriter().print("fwall reloaded.");
			return;
		}
		//-----------------------------------
		//初始化数据库
		if (url.indexOf("createmongodbdata") > -1) {
			MongoConfig m = new MongoConfig();
			response.getWriter().print("MongoConfig create:"+m.createDB());
			return;
		}		
		//-----------------------------------
		//设置wooboo的广告比
		if (url.indexOf("setwooboocent")>0) {
			String ks = (req.getParameter("ks") == null)?"s":req.getParameter("ks");
			if (req.getParameter("setwooboocent") !=null ) {
				String s = req.getParameter("setwooboocent");
				if (s.matches("[1-9]?[0-9]")) {
					boolean isK = ks.equals("k");
					fwall.setWoobooADcent(Integer.parseInt(s),isK);
				}
				
			}
			response.getWriter().print("wooboocent_"+ks+":"+fwall.getWoobooADcent(ks));
			return;
		}
		//-----------------------------------
		//处理广告
		if (url.indexOf("getadtype")>0) {
			String ks = (req.getParameter("acti") == null)?"s":req.getParameter("acti");
			String ad = "youmi";
			//按随机比率取广告
			if (StringUnit.getRandomInt(0, 100) < fwall.getWoobooADcent(ks)) {
				ad = "wooboo";
			}else{
				ad = "youmi";
			}
			response.getWriter().print(ad);
			return;
		}
		//-----------------------------------
		//默认回复404
//		response.getWriter().print("404");
//		return;
		chain.doFilter(request, response);
	}
	
	private static String iniPath;
	
	private void reloadIni(){
		if(fwall.readIni(iniPath)){
			fwall.init();
		}
		reloadFwIni();
	}
	
	public static FWall getFwall(){
		return fwall;
	}

	static String fw_ini_html = "";
	static String fw_ini_path = "";
	static String fw_ini_html_us = "";
	static String fw_ini_path_us = "";
	
//	/**
//	 * 国内版下载地址的fw_ini.htm
//	 */
//	private static String fwini_orionapk;
//	
//	/**
//	 * 国内版下载地址的fw_ini.htm
//	 */
//	private static String fwini_us_orionapk;
//	
//	
//	private final static String fw_ini(HttpServletRequest req,String lang){
//		//区分中英两个版本
//		if (lang.equals("CN")) {
//			if (CNMap.containsKey(lang)) {
//				return fwini_orionapk;
//			}else{
//				return fwini_us_orionapk;
//			}
//		}else{
//			if (CNMap.containsKey(lang)) {
//				return fw_ini_html;
//			}else{
//				return fw_ini_html_us;
//			}
//		}
//	}
	
	private static void reloadFwIni(){
		String s = "",s_us="";
		try {
			s = IO.readTxt(fw_ini_path, "utf-8");
			s_us = IO.readTxt(fw_ini_path_us, "utf-8");
			
		} catch (IOException e) {
			e.printStackTrace();
			s_us = "";
			s = "";
		}
		if (s.length() > 10) {
			fw_ini_html = s;
			fw_ini_html_us = s_us;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//处理FWall线程并初始化
		iniPath = fConfig.getServletContext().getRealPath("/")+"WEB-INF/fw_ini.json";
		//System.out.println(iniPath);
		fw_ini_path = fConfig.getServletContext().getRealPath("/")+"WEB-INF/fw_ini.htm";
		fw_ini_path_us = fConfig.getServletContext().getRealPath("/")+"WEB-INF/fw_ini_us.htm";
		try {
			fw_ini_html = IO.readTxt(fw_ini_path, "utf-8");
			fw_ini_html_us = IO.readTxt(fw_ini_path_us, "utf-8");
//			HashMap<String, Object> m_fwini = (HashMap<String, Object>) new JSONReader().read(fw_ini_html);
//			HashMap<String, Object> m_fwini_us = (HashMap<String, Object>) new JSONReader().read(fw_ini_html_us);
//			m_fwini.put("newAPK", m_fwini.get("newAPK")+"?pk=com.k99k.app.orion");
//			m_fwini_us.put("newAPK", m_fwini.get("newAPK")+"?pk=com.k99k.app.orion");
//			fwini_orionapk = new JSONWriter().write(m_fwini);
//			fwini_us_orionapk = new JSONWriter().write(m_fwini_us);
		} catch (IOException e) {
			System.out.println("fw_ini_html ERROR!"+fw_ini_html);
			System.out.println("fw_ini_html_us ERROR!"+fw_ini_html_us);
			e.printStackTrace();
			
		}
		if (fw_ini_html.length() < 10 || fw_ini_html_us.length() < 10) {
			System.out.println("fw_ini_html ERROR!"+fw_ini_html);
			System.out.println("fw_ini_html_us ERROR!"+fw_ini_html_us);
		}
		fwall = new FWall(iniPath);
		Thread fw = new Thread(fwall,"fwall");
		fw.start();
	}

}
