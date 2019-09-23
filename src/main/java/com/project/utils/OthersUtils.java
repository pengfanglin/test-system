package com.project.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bean.others.HtmlStyleBean;
import com.project.bean.others.ZipFile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 功能简单的工具类方法
 */
@Component
public class OthersUtils {
	private static ObjectMapper objectMapper;
	public OthersUtils(ObjectMapper objectMapper) {
		OthersUtils.objectMapper = objectMapper;
	}

	/**
	 * 功能:压缩多个文件成一个zip文件
	 * @param srcFiles：源文件列表
	 * @param zipFile：压缩后的文件
	 */
	public static void zipFiles(List<ZipFile> srcFiles, String zipFile){
		byte[] buf=new byte[1024];
		Set<String> overFiles=new HashSet<>();
		ZipOutputStream out=null;
		try {
			//ZipOutputStream类：完成文件或文件夹的压缩
			String path= getFileSaveParentPath()+zipFile.substring(0,zipFile.lastIndexOf("/"));
			File zip=new File(path);
			if(!zip.exists()){
				zip.mkdirs();
			}
			zip=new File(getFileSaveParentPath()+zipFile);
			if(!zip.exists()){
				zip.createNewFile();
			}
			String basePath=getFileSaveParentPath();
			out=new ZipOutputStream(new FileOutputStream(zip));
			for(ZipFile file:srcFiles){
				File localFile=new File(basePath+file.getPath());
				if(!overFiles.contains(file.getName())&&localFile.exists()){
					out.putNextEntry(new ZipEntry(file.getName()));
					overFiles.add(file.getName());
					int len;
					FileInputStream in=new FileInputStream(basePath+file.getPath());
					while((len=in.read(buf))>0){
						out.write(buf,0,len);
					}
					out.closeEntry();
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("压缩失败");
		}finally {
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 处理含有文件的表单 返回所有字段的map集合
	 */
	public static HashMap<String, Object> dealWithFormHaveFiles(HttpServletRequest request){
		//存放表单类型数据
		HashMap<String, Object> mapString = new HashMap<>();
		//存放图片保存的路径
		List<String> mapFile = new ArrayList<>();
		//接口返回信息
		HashMap<String, Object> result = new HashMap<>();
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		//获取表单的name值列表
		Enumeration<String> parameterNames = params.getParameterNames();
		//遍历name值列表，取出对应的值放到表单map中
		for (; parameterNames.hasMoreElements(); ) {
			String key = parameterNames.nextElement();
			mapString.put(key, params.getParameter(key));
		}
		//存放文件列表
		List<MultipartFile> files = new LinkedList<>();
		//获取文件的name值列表
		Iterator<String> fileNames = params.getFileNames();
		//遍历列表，取出name对应的文件列表合并到总文件列表
		for (; fileNames.hasNext(); ) {
			String key = fileNames.next();
			List<MultipartFile> files1 = ((MultipartHttpServletRequest) request).getFiles(key);
			if (files1.size() > 0) {
				files.addAll(files1);
			}
		}
		//文件保存默认位置
		String path = setDefaultPath(params);
		path += "/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/";
		result.put("state", "0");
		MultipartFile file;
		BufferedOutputStream stream = null;
		//遍历文件总表，将文件储存到本地
		for (MultipartFile file1 : files) {
			file = file1;
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					String fileName = String.valueOf(System.currentTimeMillis())
									+ String.valueOf(new Random().nextInt(Integer.MAX_VALUE))
									+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					String basePath = getFileSaveParentPath();
					File f = new File(basePath + path);
					if (!f.exists()) {
						boolean success = f.mkdirs();
						if (!success) {
							throw new RuntimeException("创建文件或目录失败");
						}
					}
					stream = new BufferedOutputStream(new FileOutputStream(new File(basePath + path + fileName)));
					stream.write(bytes);
					stream.flush();
					mapFile.add(path + fileName);
				} catch (Exception e) {
					result.put("state", "0");
					e.printStackTrace();
					return result;
				}finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		try {
			result.put("string", objectMapper.writeValueAsString(mapString));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		result.put("file", mapFile);
		result.put("state", "1");
		return result;
	}

	/**
	 * 设置默认上传路径
	 */
	public static String setDefaultPath(MultipartHttpServletRequest params){
		String path;
		if (params.getParameter("path") != null) {
			path = params.getParameter("path");
			if (path == null || "".equals(path)) {
				path = "/images/others/";
			} else if (path.contains("%")) {
				try {
					path = URLDecoder.decode(path, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException("路径参数有误");
				}
			}
		} else {
			path = "/images/others/";
		}
		return path;
	}

	/**
	 * 上传文件
	 */
	public static Map<String, Object> uploadFileForm(HttpServletRequest request) {
		//接口返回信息
		Map<String, Object> result = new LinkedHashMap<>();
		//文件保存地址
		String filePath = "";
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		String path = setDefaultPath(params);
		path += "/" + TimeUtils.getCurrentTime("yyyyMMdd") + "/";
		result.put("state", "1");
		//文件列表
		List<MultipartFile> files = new LinkedList<>();
		//获取文件的name值列表
		Iterator<String> fileNames = params.getFileNames();
		//遍历name值列表，取对应的文件列表合并到总文件
		for (; fileNames.hasNext(); ) {
			String key = fileNames.next();
			List<MultipartFile> files1 = ((MultipartHttpServletRequest) request).getFiles(key);
			if (files1.size() > 0) {
				files.addAll(files1);
			}
		}
		BufferedOutputStream stream = null;
		for (MultipartFile multipartFile : files) {
			if (!multipartFile.isEmpty()) {
				try {
					byte[] bytes = multipartFile.getBytes();
					String fileName = String.valueOf(System.currentTimeMillis())
									+ String.valueOf(new Random().nextInt(Integer.MAX_VALUE))
									+ multipartFile.getOriginalFilename()
									.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
					String basePath = getFileSaveParentPath();
					File f = new File(basePath + path);
					if (!f.exists()) {
						boolean success = f.mkdirs();
						if (!success) {
							throw new RuntimeException("创建文件或目录失败");
						}
					}
					stream = new BufferedOutputStream(new FileOutputStream(new File(basePath + path + fileName)));
					stream.write(bytes);
					stream.close();
					filePath = path + fileName;
					//单文件上传接口只保存一个文件，成功后直接返回
					break;
				} catch (Exception e) {
					try {
						if (stream != null) {
							stream.close();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					result.put("state", "0");
					e.printStackTrace();
					return result;
				}
			}
		}
		result.put("file", filePath);
		return result;
	}

	/**
	 * 从请求中读取数据
	 */
	public static String readDataFromRequest(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 向html文件中写入内容
	 */
	public static boolean writeHtml(String fileName, String desc, HtmlStyleBean htmlStyleBean) {
		try {
			String basePath = getFileSaveParentPath();
			// 模板路径
			String filePath = basePath + "/" + fileName;
			File pathFile = new File(filePath.substring(0, filePath.lastIndexOf("/")));
			if (!pathFile.exists()) {
				boolean success = pathFile.mkdirs();
				if (!success) {
					throw new RuntimeException("创建目录失败");
				}
			}
			File file = new File(filePath);
			if (!file.exists()) {
				boolean success = file.createNewFile();
				if (!success) {
					throw new RuntimeException("创建文件失败");
				}
			}
			FileOutputStream fileoutputstream = new FileOutputStream(filePath);// 建立文件输出流
			OutputStreamWriter writer = new OutputStreamWriter(fileoutputstream, "utf-8");
			String style;
			if (htmlStyleBean == null) {
				style = desc;
			} else {
				style = htmlStyleBean.getStyle_desc();
				int start = desc.indexOf("<content>");
				int end = desc.indexOf("</content>");
				if (start > 0 && end > 0) {
					style = style.replace("<content>", desc.substring(start + 9, end));
				} else {
					style = style.replace("<content>", desc);
				}
			}
			byte tag_bytes[] = style.getBytes();
			fileoutputstream.write(tag_bytes);
			writer.flush();
			fileoutputstream.close();
			writer.close();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("html内容写入异常");
		}
	}

	/**
	 * 读取html内容
	 */
	public static String readHtml(String fileName) {
		try {
			String basePath = getFileSaveParentPath();
			// 本地html文件路径
			String filePath = basePath + "/" + fileName;
			String templateContent;
			FileInputStream fileinputstream = new FileInputStream(filePath);
			int length = fileinputstream.available();
			byte bytes[] = new byte[length];
			int read_size = fileinputstream.read(bytes);
			if (read_size <= 0) {
				return "";
			}
			fileinputstream.close();
			templateContent = new String(bytes);
			return templateContent;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取项目的根路径
	 */
	public static String getClassRootPath() {
		return Objects.requireNonNull(OthersUtils.class.getClassLoader().getResource("")).getPath();
	}

	/**
	 * 获取文件默认保存路径，windows和linux保存的路径不一样
	 */
	public static String getFileSaveParentPath() {
		String parentPath;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			parentPath = request.getSession().getServletContext().getRealPath("/");
		} else {
			parentPath = ConfigUtils.static_dir;
		}
		return parentPath;
	}

	/**
	 * nginx转发的location /里面要加入如下配置 否则ip会获取为127.0.0.1
	 * proxy_set_header x-forwarded-for  $remote_addr;
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
	 * 192.168.1.100
	 * 用户真实IP为： 192.168.1.110
	 */
	public static String getRequestIp() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(Object object) {
		if(object==null){
			return true;
		}else if(object instanceof String){
			return "".equals(object);
		}else if(object.getClass().isArray()){
			return Array.getLength(object)==0;
		}else if(object instanceof List){
			return ((List)object).size()==0;
		}else if(object instanceof Map){
			return ((Map)object).size()==0;
		}else {
			return false;
		}
	}

	/**
	 * xml转map
	 */
	public static Map<String, Object> xmlToMap(String xml){
		Map<String, Object> data = new HashMap<>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		InputStream stream;
		Document doc;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			doc = documentBuilder.parse(stream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析失败");
		}
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				data.put(element.getNodeName(), element.getTextContent());
			}
		}
		try {
			stream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}

	/**
	 * map转xml
	 */
	public static String mapToXml(Map<String, Object> data) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("解析失败");
		}
		Document document = documentBuilder.newDocument();
		Element root = document.createElement("xml");
		document.appendChild(root);
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				Element filed = document.createElement(entry.getKey());
				filed.appendChild(document.createTextNode(entry.getValue().toString()));
				root.appendChild(filed);
			}
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("解析失败");
		}
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException("解析失败");
		}
		String output = writer.getBuffer().toString();
		try {
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return output;
	}

	/**
	 * 产生随机数(纯数字)
	 */
	public static String createRandom(int length) {
		return createRandom(true, length);
	}

	/**
	 * 产生随机数(字母+数字)
	 */
	public static String createRandom(boolean numberFlag, int length) {
		StringBuilder retStr;
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = new StringBuilder();
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr.append(strTable.charAt(intR));
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr.toString();
	}

	/**
	 * 字符串转Integer
	 */
	public static Integer toInteger(String value) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.intValue();
	}

	/**
	 * 字符串转Integer
	 */
	public static Integer toInteger(Float value) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.intValue();
	}

	/**
	 * 字符串转指定长度的Float
	 */
	public static Float toFloat(String value, int length) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * Float转指定长度的Float
	 */
	public static Float toFloat(Float value, int length) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 字符串转指定长度的Double
	 */
	public static Double toDouble(String value, int length) {
		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
