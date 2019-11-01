package com.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class Test {
	public static boolean writeWordFile() {

		boolean w = false;
		String path = "e:/";

		try {
			if (!"".equals(path)) {

				// 检查目录是否存在
				File fileDir = new File(path);
				if (fileDir.exists()) {

					// 生成临时文件名称
					String fileName = "a.doc";
					String content = "<html>" + "<head>你好</head>" + "<body>" + "<table>" + "<tr>" + "<td>信息1</td>"
							+ "<td>信息2</td>" + "<td>t3</td>" + "<tr>" + "</table>" + ""
									+ "<img src=\"e:/aaa.jpeg\"/>"
									+ "</body>" + "</html>";

					byte b[] = content.getBytes();
					ByteArrayInputStream bais = new ByteArrayInputStream(b);
					POIFSFileSystem poifs = new POIFSFileSystem();
					DirectoryEntry directory = poifs.getRoot();
					DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
					FileOutputStream ostream = new FileOutputStream(path + fileName);
					poifs.writeFilesystem(ostream);
					bais.close();
					ostream.close();

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return w;
	}

	public static void main(String[] args) {
		writeWordFile();
	}
}
