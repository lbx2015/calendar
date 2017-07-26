package net.riking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 * 
 * @function introduce<文件工具类>
 * @author Ivor
 * @time May 19, 2008
 * @last modification by Ivor at May 19, 2008
 * @version 1.0
 */
public class FileUtil {

	private static FileOutputStream fs;
	/**
	 * 读取配置文件的属性到Map
	 * 
	 * @param filePath
	 *            配置文件的路径
	 * @return 各种属性配置的Map
	 * @throws ConfigurationException
	 *             解析文件异常
	 */
	public static Map<String, String> getMapByPorpFile(String filePath)
			throws Exception {

		Configuration config;
		config = new PropertiesConfiguration(filePath);
		Iterator<String> iter = config.getKeys();

		Map<String, String> map = new HashMap<String, String>();

		while (iter.hasNext()) {

			String key = iter.next().toString();

			map.put(key, config.getString(key));

		}
		return map;

	}

	/**
	 * 目录不存在，新建目录
	 * 
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public static boolean newFolder(String folderPath) {
		try {
			String splitStr = "";
			if (folderPath.indexOf("/") != -1)
				splitStr = "/";
			else
				splitStr = "\\";
			StringTokenizer st = new StringTokenizer(folderPath, splitStr);
			String folder = st.nextToken() + splitStr;
			String subFolder = folder;
			while (st.hasMoreTokens()) {
				folder = st.nextToken() + splitStr;
				subFolder += folder;
				File myFilePath = new File(subFolder);
				if (!myFilePath.exists())
					myFilePath.mkdir();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递归删除文件夹(删除文件夹,包括path里的最后一个文件夹)
	 * 
	 * @param path
	 * @return true：删除文件夹成功 false：失败
	 * @throws Exception
	 */
	public static boolean delDir(String path) throws Exception {
		boolean bIsSuccess = true;
		File dir = new File(path);
		if (dir.exists()) {
			File[] tmp = dir.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isDirectory()) {
					delDir(path + "/" + tmp[i].getName());
				} else {
					tmp[i].delete();
				}
			}
			bIsSuccess = dir.delete();
		} else {
			bIsSuccess = false;
		}
		return bIsSuccess;
	}

	/**
	 * 递归删除文件夹(删除文件夹,不包括path里的最后一个文件夹)
	 * 
	 * @param path
	 * @return true：删除文件夹成功 false：失败
	 * @throws Exception
	 */
	public static boolean deleteDir(String path) throws Exception {
		boolean bIsSuccess = true;
		File dir = new File(path);
		if (dir.exists()) {
			File[] tmp = dir.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isDirectory()) {
					delDir(path + "/" + tmp[i].getName());
				} else {
					tmp[i].delete();
				}
			}
		} else {
			bIsSuccess = false;
		}
		return bIsSuccess;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(String sourceFilePath, String targetFilePath)
			throws Exception {
		boolean bIsSuccess = true;
		try {
			int byteread = 0;
			File oldfile = new File(sourceFilePath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(sourceFilePath);
				fs = new FileOutputStream(targetFilePath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			bIsSuccess = false;
		}
		return bIsSuccess;
	}

	/**
	 * 对文件夹下所有文件进行压缩
	 * 
	 * @param sourceFolderPath
	 * @param targetFolderPath
	 * @param downFileName
	 */
	public static boolean compressFile(String sourceFolderPath,
			String targetFolderPath, String downFileName) {
		boolean bIsSuccess = true;
		try {
			FileUtil.newFolder(targetFolderPath);
			JZip.compress(sourceFolderPath, true, targetFolderPath
					+ downFileName);
		} catch (Exception e) {
			bIsSuccess = false;
		}
		return bIsSuccess;
	}
	/**
	 * 判断指定的文件或文件夹是否存在
	 * @param filePath 文件或文件夹路径
	 * @param flag true表示文件夹，false表示文件
	 * @return 是否存在
	 */
	public static boolean isExists(String filePath,boolean flag){
		File file=new File(filePath);
		if(file.exists()){
			if(flag){
				if(file.isDirectory())
					return true;
				else
					return false;
			}
			return true;
		}else{
			return false;
		}
	}
	  /**
     * 删除文件夹里面的所有文件
     * 
     * @param path
     *            文件夹路径,如c:/fqf
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
                delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]); // 再删除空文件夹
            }
        }
    }
    /**
     * 删除文件夹
     * 
     * @param folderPath
     *            文件夹路径及名称，如c:/fqf
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
