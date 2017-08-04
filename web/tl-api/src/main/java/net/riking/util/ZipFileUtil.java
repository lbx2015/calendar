package net.riking.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileUtil {

	public static void parseZip(String src, String desc) throws IOException {
		parseZip(new File(src), desc);
	}

	@SuppressWarnings({"resource", "rawtypes"})
	public static void parseZip(File zipFile, String descDir)
			throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		InputStream in = null;
		OutputStream out = null;
		ZipFile zip = null;
		zip = new ZipFile(zipFile, Charset.forName("GBK"));
		Enumeration entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			zipEntryName = zipEntryName
					.substring(zipEntryName.lastIndexOf("/"));
			in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		zip.close();
		zipFile.delete();
	}

	public static void main(String[] args) {
		try {
			parseZip(
					"D:\\git\\tl\\web\\tl-api\\target\\classes\\static\\icon\\后台_切图.zip",
					"D:\\国家icon\\xxxxx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
