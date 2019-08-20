package com.base.modules.customizesys.helper;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内容维护相关工具类
 * @ClassName:  ContentUtils   
 * @Description:内容维护相关工具类
 * @author:  huanw
 * @date:   2019年1月24日 上午9:44:02
 */
public class ContentUtils {
	/**
	 * 获取编辑内容中的图片信息
	 * @Title: getContentImgs   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @author: huanw
	 * @param: @param str
	 * @param: @param idList      
	 * @return: void
	 * @date:   2019年1月24日 上午9:47:25       
	 * @throws
	 */
	public static void getContentImgs(String str,List<String> idList) {
		if(idList==null) {
			idList =new ArrayList<String>();
		}
		try {
			Pattern pat = Pattern.compile("(<img\\s+src\\s*=\\s*\"https?://\\S+/)(\\w+)(\\.\\w+\")");  
			Matcher mat = pat.matcher(str);
			while (mat.find()) {
		    	idList.add(mat.group(2));
		    }
		}catch(Exception e) {
			
		}
	}
	/**
	 * 获取编辑内容中的视频信息
	 * @Title: getContentVideo   
	 * @Description: 获取编辑内容中的视频信息
	 * @author: huanw
	 * @param: @param str
	 * @param: @param idList      
	 * @return: void
	 * @date:   2019年2月27日 下午3:08:27       
	 * @throws
	 */
	public static void getContentVideo(String str,List<String> idList) {
		if(idList==null) {
			idList =new ArrayList<String>();
		}
		try {
			Pattern pat = Pattern.compile("(<source\\s+src\\s*=\\s*\"https?://\\S+/)(\\w+)(\\.\\w+\")");  
			Matcher mat = pat.matcher(str);
			while (mat.find()) {
		    	idList.add(mat.group(2));
		    }
		}catch(Exception e) {
			
		}
	}
	/**
	 * 获取编辑内容中的附件信息
	 * @Title: getContentVideo   
	 * @Description: 获取编辑内容中的附件信息
	 * @author: huanw
	 * @param: @param str
	 * @param: @param idList      
	 * @return: void
	 * @date:   2019年2月27日 下午3:08:27       
	 * @throws
	 */
	public static void getContentFile(String str,List<String> idList) {
		if(idList==null) {
			idList =new ArrayList<String>();
		}
		try {
			//这个正则有问题
			Pattern pat = Pattern.compile("(<a\\s+[^>]+href\\s*=\\s*\"https?://\\S+/)(\\w+)(\\.\\w+\")");  
			Matcher mat = pat.matcher(str);
			while (mat.find()) {
		    	idList.add(mat.group(2));
		    }
		}catch(Exception e) {
			
		}
		
	}
	/**
	 * 获取封面图名称,不带扩展名
	 * @Title: getCoverNames   
	 * @Description: 获取封面图名称,不带扩展名
	 * @author: huanw
	 * @param: @param str  封面图可访问地址 例如：http://127.0.0.1:8088/files/53906ca31d904809b7fbc0320c065db6.png
	 * @param: @return     
	 * @return: List<String> 此方法本来仅会返回一字符串，返回list是以为 修改图片状态接口可扩展
	 * @date:   2019年1月25日 下午2:36:33       
	 * @throws
	 */
	public static List<String> getCoverNames(String str) {
		//封面图 图片名称，即是 图片管理表的唯一标识uuid
		List<String> coverNames = new ArrayList<>();
		if(StringUtils.isNotBlank(str)) {
			int lastIndexOf = str.lastIndexOf("/");
			String newStr = str.substring(lastIndexOf+1);
			String[] split = newStr.split("\\.");
			if(split!=null && split.length==2) {
				coverNames.add(split[0]);
			}
		}
		return coverNames;
	}
	/**
	 * 按格式获取当前时间
	 * @Title: getDate   
	 * @Description:  按格式获取当前时间
	 * @author: huanw
	 * @param: @param format
	 * @param: @return      
	 * @return: String
	 * @date:   2019年2月15日 下午2:11:24       
	 * @throws
	 */
	public static String getDate(String format) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);//显示2017-10-27 10:00:00格式
		String str  = null;
		try {
			str  = sdf.format(new Date());
		}catch (Exception e){
		    e.printStackTrace();
		}
		return str;
	}
	/**
	 * 获取随机数
	 * @Title: getRandom   
	 * @Description: 获取随机数
	 * @author: huanw
	 * @param: @param num  获取的的位数
	 * @param: @return      
	 * @return: String
	 * @date:   2019年3月25日 下午4:20:44       
	 * @throws
	 */
	public static String getRandom(int num) {
		Random random = new Random();
		String result="";
		for (int i=0;i<num;i++)
		{
			result+=random.nextInt(10);
		}
		return result;
	}
	/**
	 * 将Date转成string串
	 * @Title: getDateToString   
	 * @Description: 将Date转成string串
	 * @author: huanw
	 * @param: @param date
	 * @param: @return      
	 * @return: String
	 * @date:   2019年4月20日 下午6:45:18       
	 * @throws
	 */
	public static String getDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
	}
	
	/**
	 * 将Date转成string串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateToString(Date date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
	}
	/**
	 * 字符串时间转date对象
	 * @param dateString
	 * @return
	 */
	public static Date getStringToDate(String dateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date =null;
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException("字符串时间转date对象异常",e);
		}
		return date;
	}
	public static String getNickname() {
//		List<String> nicknameList = new ArrayList<String>();
//		nicknameList.add("一梦繁华ら");
//		nicknameList.add("一个人的义无反顾");
//		nicknameList.add("心随风飞");
//		nicknameList.add("灰色ф涟漪");
//		nicknameList.add("坚辛岁月");
//		Random random = new Random();
//		int nextInt = random.nextInt(10);
//		if(nextInt>4) {
//			nextInt=nextInt-5;
//		}
		String nickname="HKAOM-"+getRandom(4);
		return nickname;
	}
	public static void main(String[] args) {
//		List<String> coverNames = ContentUtils.getCoverNames("http://127.0.0.1:8088/files/53906ca31d904809b7fbc0320c065db6.png");
//		System.out.println(coverNames.toString());
//		System.out.println(ContentUtils.getDate("yyyy-MM-dd HH:mm:ss"));
//		String str="<p><img src=\"http://127.0.0.1:8088/files/3f2936e112544d9185c740c71dfc8683.png\" title=\"小程序授权登录流程.png\" alt=\"小程序授权登录流程.png\"/></p><p>调度</p><p><video class=\"edui-upload-video  vjs-default-skin video-js\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"http://127.0.0.1:8088/files/72468f71cb644745ad1ce4df42f87a33.mp4\" data-setup=\"{}\"><source src=\"http://127.0.0.1:8088/files/72468f71cb644745ad1ce4df42f87a33.mp4\" type=\"video/mp4\"/></video></p><p><br/></p><p style=\"line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"http://localhost:8080/admin/statics/plugins/ueditor/1_4_3_3-utf8-jsp/dialogs/attachment/fileTypeImages/icon_rar.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"http://127.0.0.1:8088/files/eec08c6b06f34eefb871b475cdea22d7.zip\" title=\"支付文档22221.zip\">支付文档22221.zip</a></p><p style=\"line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"http://localhost:8080/admin/statics/plugins/ueditor/1_4_3_3-utf8-jsp/dialogs/attachment/fileTypeImages/icon_doc.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"http://127.0.0.1:8088/files/891783fcbc3f4e25afa14f6eb4143f3f.docx\" title=\"支付文档 - 22222222222.docx\">支付文档 - 22222222222.docx</a></p><p style=\"line-height: 16px;\"><img style=\"vertical-align: middle; margin-right: 2px;\" src=\"http://localhost:8080/admin/statics/plugins/ueditor/1_4_3_3-utf8-jsp/dialogs/attachment/fileTypeImages/icon_doc.gif\"/><a style=\"font-size:12px; color:#0066cc;\" href=\"http://127.0.0.1:8088/files/0c7f7ed1861a43028b842ee6d7b63c7f.docx\" title=\"支付文档.docx\">支付文档.docx</a></p><p><video class=\"edui-upload-video  vjs-default-skin video-js\" controls=\"\" preload=\"none\" width=\"420\" height=\"280\" src=\"http://127.0.0.1:8088/files/83016c9e236648b49697e82973badf3f.mp4\" data-setup=\"{}\"><source src=\"http://127.0.0.1:8088/files/83016c9e236648b49697e82973badf3f.mp4\" type=\"video/mp4\"/></video></p><p><br/></p>";
//		ContentUtils.getContentFile(str, new ArrayList<String>());
	}
}
