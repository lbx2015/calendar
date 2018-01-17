package net.riking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.utils.UuidUtils;
/**
 * 文件操作工具
 * @author you.fei
 * @version crateTime：2018年1月17日 上午11:44:46
 * @used TODO
 */
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
	
	/**
	 * 单个文件剪切
	 * @used TODO
	 * @param srcFilePath
	 * @param folderPath
	 * @return
	 */
	public static String cutFile(String srcFilePath, String folderPath) {
		File srcFile = null;
		File destFile = null;
		InputStream is = null;
		FileOutputStream os = null;
		try {
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			srcFile = new File(srcFilePath);
			destFile = new File(folderPath+"\\"+ srcFile.getName());
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(destFile);
			int len ;
			byte[] buffer = new byte[1024 * 1024];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(is!=null){
					is.close();
				}
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(srcFile!=null){
			srcFile.delete();
		}
		if(destFile != null){
			return destFile.getName();
		}
		return null;
	}
	
	/**
	 * 上传文件保存
	 * @used TODO
	 * @param mFile
	 * @param folderPath
	 * @return 文件名
	 */
	public static String saveMultipartFile(MultipartFile mFile, String folderPath) {
		String fileName = UuidUtils.random() + "." + mFile.getOriginalFilename().split("\\.")[1];
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			File dir = new File(folderPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String filePath = folderPath + fileName;
			fos = new FileOutputStream(filePath);
			int len = 0;
			byte[] buf = new byte[1024 * 1024];
			while ((len = is.read(buf)) > -1) {
				fos.write(buf, 0, len);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(CodeDef.EMP.GENERAL_ERR + "");
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				logger.error(e);
				throw new RuntimeException(CodeDef.EMP.GENERAL_ERR + "");
			}
		}
		return fileName;
	}
	
	
	/**
	 * 默认放在本项目资源路径下
	 * @used TODO
	 * @param mFile
	 * @param subFolderPath
	 * @return 文件资源路径
	 */
	public static String saveMultipartFileInStatic(MultipartFile mFile, String subFolderPath) {
		String folderPath = getAbsolutePathByProject(subFolderPath);
		return folderPath +"/"+ saveMultipartFile(mFile,folderPath);
	}
	
	
	/**
	 * 获取项目的绝对路径(D:\git\tl\web\tl-api\target\classes) + "static" + subFolderPath  
	 * @used TODO
	 * @param subFolderPath
	 * @return
	 */
	public static String getAbsolutePathByProject(String subFolderPath){
		String classesPath = null;
		try {
			classesPath = new String(FileUtils.class.getResource("/").getPath().getBytes("iso-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classesPath + Const.TL_STATIC_PATH + subFolderPath;
	}
	
	/**
	 * 获取 当前请求的 HttpServletRequest对象
	 * @used TODO
	 * @return
	 * @throws IllegalStateException
	 */
    public static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return attrs.getRequest();
    }
	
	/**
	 * 获取到  项目名级别  的 url地址 格式为：http://ip:port/project
	 * @used TODO
	 * @param clazz
	 * @return 
	 */
	public static String getProjectUrl(Class<?> clazz){
		 String reqUrl = getCurrentRequest().getRequestURL().toString();
		 String[] mappingVals = clazz.getAnnotation(RequestMapping.class).value();
		 StringBuilder sb = new StringBuilder();
		 for (int i = 0; i < mappingVals.length; i++) {
			sb.append(mappingVals[i]);
		}
		String patternStr = "(.+?)(?="+sb.toString()+")";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(reqUrl);
		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}
	
	/**
	 * 获取图片   url访问路径
	 * @used TODO
	 * @param clazz
	 * @return
	 */
	public static String getPhotoUrl(String subFilePath, Class<?> clazz){
		return getProjectUrl(clazz) + subFilePath;
	}

}
