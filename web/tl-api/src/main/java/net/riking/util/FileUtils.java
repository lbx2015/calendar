package net.riking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils extends org.apache.commons.lang3.time.DateUtils {
	private static final Logger logger = LogManager.getLogger("FileUtils");

	/**
	 * 复制文件到指定路径
	 * @param oldPath String
	 * @param newPath String
	 * @return boolean
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath) throws Exception {

		int byteread = 0;
		File oldfile = new File(oldPath);
		String path = newPath.substring(0, newPath.lastIndexOf("/") + 1);
		File newfile = new File(path);

		if (!newfile.exists()) {
			newfile.mkdirs();
		}
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(oldPath); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024 * 1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			fs.close();
		}

	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				logger.info("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				logger.info("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			logger.info("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * @param path String 文件夹路径 如
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 删除文件夹
	 * @param filePathAndName String 文件夹路径及名称 如
	 * @param fileContent String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();

		}

	}

}
