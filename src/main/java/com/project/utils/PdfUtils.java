package com.project.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.project.bean.institution.StudentBean;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfUtils {
	public static void createStudentPdfMany(String template,List<StudentBean> studentBeans, HttpServletResponse response){
		try{
			response.setHeader("Content-type","application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("准考证.pdf","utf-8"));
			String basePath = OthersUtils.getFileSaveParentPath();
			OutputStream fos = response.getOutputStream();
			ByteArrayOutputStream bos[] = new ByteArrayOutputStream[studentBeans.size()];
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat chineseStartFormat=new SimpleDateFormat("MM月dd日 HH:mm");
			SimpleDateFormat chineseEndFormat=new SimpleDateFormat("HH:mm");
      for (int i=0;i<studentBeans.size();i++) {
				StudentBean studentBean=studentBeans.get(i);
				try{
					studentBean.setStart_time(chineseStartFormat.format(format.parse(studentBean.getStart_time()))+"-"+chineseEndFormat.format(format.parse(studentBean.getEnd_time())));
				}catch (ParseException e){
					e.printStackTrace();
				}
				bos[i] = new ByteArrayOutputStream();
				PdfStamper ps = new PdfStamper(new PdfReader(basePath+template),  bos[i]);
				//使用中文字体
				BaseFont bfChinese=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				ArrayList<BaseFont> fontList = new ArrayList<>();
				Font textFont = new Font(bfChinese,12, Font.NORMAL);//正常
				fontList.add(bfChinese);
				AcroFields s = ps.getAcroFields();
				s.setSubstitutionFonts(fontList);
				Map<String,AcroFields.Item> map=s.getFields();
				for(Map.Entry<String,AcroFields.Item> entry:s.getFields().entrySet()){
					String name=entry.getKey();
          Object value;
					if(name.equals("logo")){
            value="/images/others/yin_zhang.png";
          }else{
            //填充文本域
            Field field=StudentBean.class.getDeclaredField(name);
            field.setAccessible(true);
            value=field.get(studentBean);
          }
					if(name.equals("photo")&&!OthersUtils.isEmpty(value)){
						int pageNo = s.getFieldPositions("photo").get(0).page;
						Rectangle signRect = s.getFieldPositions("photo").get(0).position;
						float x = signRect.getLeft();
						float y = signRect.getBottom();
						// 读图片
						Image image;
						try{
							image=Image.getInstance(OthersUtils.getFileSaveParentPath()+value.toString());
						}catch (FileNotFoundException e){
							LogUtils.info(e.getMessage());
							continue;
						}
						// 获取操作的页面
						PdfContentByte under = ps.getOverContent(pageNo);
						// 根据域的大小缩放图片
						image.scaleToFit(signRect.getWidth(), signRect.getHeight());
						// 添加图片
						image.setAbsolutePosition(x, y);
						under.addImage(image);
					}else if(name.equals("logo")){
						int pageNo = s.getFieldPositions("logo").get(0).page;
						Rectangle signRect = s.getFieldPositions("logo").get(0).position;
						float x = signRect.getLeft();
						float y = signRect.getBottom();
						// 读图片
						Image image;
						try{
							image=Image.getInstance(OthersUtils.getFileSaveParentPath()+value.toString());
						}catch (FileNotFoundException e){
							LogUtils.info(e.getMessage());
							continue;
						}
						// 获取操作的页面
						PdfContentByte under = ps.getOverContent(pageNo);
						// 根据域的大小缩放图片
						image.scaleToFit(signRect.getWidth(), signRect.getHeight());
						// 添加图片
						image.setAbsolutePosition(x, y);
						under.addImage(image);
					}else{
						s.setField(name,value==null?"":value.toString());
					}
				}
				//插入表格
				ps.setFormFlattening(true);
				ps.close();
			}
			Document doc = new Document();
			PdfCopy pdfCopy = new PdfCopy(doc, fos);
			doc.open();
			PdfImportedPage impPage;
			/**取出之前保存的每页内容*/
			for (int i = 0; i < studentBeans.size(); i++) {
				impPage = pdfCopy.getImportedPage(new PdfReader(bos[i]
					.toByteArray()), 1);
				pdfCopy.addPage(impPage);
			}
			doc.close();//当文件拷贝  记得关闭doc
			fos.close();
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("PDF文件生成失败");
		}
	}
}

