package com.base;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import sun.misc.BASE64Encoder;

public class WordUtils {
	/*
	 * 转ascii码需要 分享到微博
	 * 
	 * @author zhouw
	 */
	public static int char2ASCII(char c) {
		return c;
	}

	/*
	 * 将字符串转换为10进制ASCII码
	 * 
	 * @author zhouw
	 */
	public static String string2ASCII(String s) {// 字符串转换为十进制ASCII码
		if (s == null || "".equals(s)) {
			return null;
		}
		char[] chars = s.toCharArray();
		StringBuffer asciistring = new StringBuffer();
		int n = 0;
		for (char c : chars) {
			n = c;
			String a = "";
			if ((19968 <= n && n < 40623)) {
				a = "&#" + n + ";";
			} else {
				a = c + "";
			}
			asciistring.append(a);
		}
		return asciistring.toString();
	}

	/*
	 * 读取mth文件到字符串
	 * 
	 * @return ethrows Exception
	 **/
	public static String readFile(String filepath) throws Exception {
		StringBuffer buffer = new StringBuffer("");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			buffer = new StringBuffer();
			while (br.ready()) {
				buffer.append((char) br.read());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return buffer.toString();
	}

	/**
	 * 图片转为base64
	 * 
	 * @author zhouw eparam imagePath
	 * @return
	 */
	public static String getImagestr(String imagepath) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imagepath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}

	/*
	 * 将htm1转化为word的主方法
	 **/
	public static boolean writeWordFile(String content, String path, String fileName) throws Exception {
		boolean flag = false;
		FileOutputStream fos = null;
		try {
			if (!"".equals(path)) {
				byte b[] = content.getBytes("UTF-8");
				fos = new FileOutputStream(path + fileName + ".doc");
				fos.write(b);
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();

			}
		}
		return flag;
	}
	
	public static void main(String[] args) throws Exception {
		//读取 mht
		String path = "e:/a.mht";
		String readFile = readFile(path); //得到mth字符串 ${img} 为需要替换的图片 base64 编码
		// 获取图片base64编码
		String imgPath = "e:/aaa.jpeg";
		String imagestr = getImagestr(imgPath);
		System.out.println(imagestr);
		// 用base64 替换 ${img}
		String content = readFile.replace("${img}", imagestr);
		// 进行转码
		content = string2ASCII(content);
		// 写入word
		writeWordFile(content , "E:/" , "imgTest");
	}

}
