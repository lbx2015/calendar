package net.riking.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class FileOperate {
	private static FileOperate fo = null;
	private static int bufferSize = 1024;
	//private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 设置缓冲区大小，必须大于0
	 * 
	 * @param size 缓冲区大小
	 * @throws IllegalArgumentException 当size小于0时，抛出异常
	 */
	public void setBufferSize(int size) {
		if (size < 1) {
			throw new IllegalArgumentException("bufferSize must greater than 0");
		}
		bufferSize = size;
	}
    private FileOperate() {
    }

    /**
     * 获得文件帮助类唯一实例
     * 
     * @return 获得的实例
     */
    public static synchronized FileOperate getInstance() {
        if (fo == null) {
            fo = new FileOperate();
        }
        return fo;
    }

    /**
     * 新建目录
     * 
     * @param folderPath
     *            文件夹路径，如c:/fqf
     */
    public void newFolder(String folderPath) {
        try {
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            if (!myFilePath.exists() && !myFilePath.isDirectory()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
        	//log.error("新建目录操作出错",e);
            System.out.println("新建目录操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 新建文件
     * 
     * @param filePathAndName
     *            文件路径及名称，如c:/fqf.txt
     * @param fileContent
     *            文件内容
     */
    public void newFile(String filePathAndName, String fileContent) {

        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.getParentFile().exists()) {
                myFilePath.getParentFile().mkdirs();
            }
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
            resultFile.close();
        } catch (Exception e) {
        	//log.error("新建目录操作出错",e);
            System.out.println("新建目录操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * 
     * @param filePathAndName
     *            文件路径及名称，如c:/fqf.txt
     */
    public void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            java.io.File myDelFile = new java.io.File(filePath);
            myDelFile.delete();

        } catch (Exception e) {
        	//log.error("删除文件操作出错",e);
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     * 
     * @param folderPath
     *            文件夹路径及名称，如c:/fqf
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹

        } catch (Exception e) {
        	//log.error("删除文件夹操作出错",e);
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹里面的所有文件
     * 
     * @param path
     *            文件夹路径,如c:/fqf
     */
    public void delAllFile(String path) {
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
     * 复制单个文件
     * 
     * @param oldPath
     *            原文件路径，如c:/fqf.txt
     * @param newPath
     *            复制后路径，如f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                File newFile = new File(newPath);
                if(!newFile.exists()){
                	newFile.createNewFile();
                }
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1444];

                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
        	//log.error("复制单个文件操作出错",e);
            System.err.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     * 
     * @param oldPath
     *            原文件路径，如：c:/fqf
     * @param newPath
     *            复制后路径，如：f:/fqf/ff
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) { // 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
//        	log.error("复制整个文件夹内容操作出错",e);
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定目录
     * 
     * @param oldPath
     *            如：c:/fqf.txt
     * @param newPath
     *            如：d:/fqf.txt
     */
    public void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动文件到指定目录
     * 再删除源文件夹
     * @param oldPath
     *            如：c:/fqf.txt
     * @param newPath
     *            如：d:/fqf.txt
     */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }
    /**
     * 移动文件到指定目录
     * 删除源文件，不删除源文件夹
     * @param oldPath
     *            如：c:/fqf.txt
     * @param newPath
     *            如：d:/fqf.txt
     */
    public void moveFolderSrc(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delAllFile(oldPath);
    }
    
    public void uploadFile(File upload,String path){
		try {
			File sFile = new File(path);
			if(!sFile.exists()){
				File parent = sFile.getParentFile();
				if(!parent.exists()){
					parent.mkdirs();
				}
				sFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(sFile);
			FileInputStream fis=new FileInputStream(upload); 
	        byte[] buffer=new byte[1024]; 
	        int len=0; 
	        while((len=fis.read(buffer))>0){ 
	            fos.write(buffer, 0, len); 
	        } 
	        fos.close();
	        fis.close();
		} catch (Exception e) {
		}
    }
    
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte   
	private static ZipFile zf;

	/**  
	 * * 批量压缩文件（夹） *   
	 * * @param resFileList 要压缩的文件（夹）列表   
	 * * @param zipFile 生成的压缩文件   
	 * * @throws  
	 * IOException 当压缩过程出错时抛出  
	 */
	public static void zipFiles(List<File> resFileList, File zipFile)
			throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.close();
	}

	/**  
	 * * 批量压缩文件（夹） *   
	 * * @param resFileList 要压缩的文件（夹）列表   
	 * * @param zipFile 生成的压缩文件   
	 * * @param comment 压缩文件的注释   
	 * * @throws IOException 当压缩过程出错时抛出  
	 */
	public static void zipFiles(List<File> resFileList, File zipFile,
			String comment) throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.setComment(comment);
		zipout.close();
	}

	/**                                                            
	 * 描述 : <将文件写入压缩包中>. <br>  
	 *<p>                                                   
	                                                                                                                                                                                                        
	 * @param resFile  
	 * @param zipout  
	 * @param rootpath  
	 * @throws IOException                                                                                                    
	 */
	private static void zipFile(File resFile, ZipOutputStream zipout,
			String rootpath) throws IOException {
		rootpath = rootpath
				+ (rootpath.trim().length() == 0 ? "" : File.separator)
				+ resFile.getName();
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			for (File file : fileList) {
				zipFile(file, zipout, rootpath);
			}
		} else {
			byte buffer[] = new byte[BUFF_SIZE];
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(resFile), BUFF_SIZE);
			zipout.putNextEntry(new ZipEntry(rootpath));
			int realLength;
			while ((realLength = in.read(buffer)) != -1) {
				zipout.write(buffer, 0, realLength);
			}
			in.close();
			zipout.flush();
			zipout.closeEntry();
		}
	}

	/**  
	 * * 解压缩一个文件 *   
	 * * @param zipFile 压缩文件   
	 * * @param folderPath 解压缩的目标目录   
	 * * @throws  
	 * IOException 当压缩过程出错时抛出  
	 */
	@SuppressWarnings("rawtypes")
	public static void upZipFile(File zipFile, String folderPath)
			throws IOException {
		zf = new ZipFile(zipFile);
		for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			OutputStream out = new FileOutputStream(folderPath + File.separator + entry.getName());
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * 将指定字符串追加到文件末尾
	 * 
	 * @param file 目标文件
	 * @param data 追加的字符串
	 * @param newline 是否在字符串末尾后追加\r\n
	 * @throws IOException
	 */
	public static void append(File file, String data, boolean newline) throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file, true));
			bos.write(data.getBytes());
			if (newline) {
				bos.write('\r');
				bos.write('\n');
			}
		} finally {
			if (bos != null) { bos.flush(); bos.close();}
		}
	}
	
	public static void append(File file, byte[] data) throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file, true));
			bos.write(data);
		} finally {
			if (bos != null) { bos.flush(); bos.close();}
		}
	}
	
	/**
	 * 将源文件与目标文件合并，合并后将删除源文件
	 * 
	 * @param dest 目标文件
	 * @param src 源文件
	 * @throws IOException
	 */
	public static void join(File dest, File src) throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		boolean complete = false;
		try {
			bis = new BufferedInputStream(new FileInputStream(src));
			bos = new BufferedOutputStream(new FileOutputStream(dest, true));
			byte[] buff = new byte[bufferSize];
			int len = -1;
			while ((len = bis.read(buff)) != -1) {
				bos.write(buff, 0, len);
			}
			complete = true;
		} finally {
			if (bos != null) { bos.flush(); bos.close();}
			if (bis != null) { bis.close();}
			if (complete) { src.delete(); }
		}
	}
	
	/**
	 * 分割文件，分割后的子文件的命名如下所示：
	 * <ul>
	 * 	<li><code>FileUtils.split(<i>file</i>, 1024, null)</code> &gt; document.txt &gt; document1.txt, document2.txt, ...</li>
	 *  <li><code>FileUtils.split(<i>file</i>, 1024, "mydoc")</code> &gt; document.txt &gt; mydoc1.txt, mydoc2.txt, ...</li>
	 * </ul>
	 * 
	 * @param file 待分割文件
	 * @param size 分割大小
	 * @param newName 分割后的文件的名称
	 * @throws IOException
	 */
	public static void split(File file, long size, String newName) throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		String extName = "";
		int dot = newName.lastIndexOf('.');
		if (dot != -1) {
			extName = newName.substring(dot);
			newName = newName.substring(0, dot);
		}
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			long length = file.length();
			for (int i = 0; i < (int) Math.ceil(length * 1.0 / size); i++) {
				try {
					bos = new BufferedOutputStream(new FileOutputStream(file.getParent() + File.separator + newName + (i+1) + extName));
					byte[] buff = new byte[bufferSize];
					int len = -1;
					long buffCount = size / buff.length;
					for (int j = 0; j < buffCount; j += len) {
						len = bis.read(buff);
						bos.write(buff);
					}
					len = bis.read(buff, 0, (int) size % buff.length);
					bos.write(buff, 0, len);
				} finally {
					if (bos != null) { bos.flush(); bos.close();}
				}
			}
		} finally {
			if (bis != null) { bis.close();}
		}
	}
	
	/**
	 * 移动文件或目录
	 * 
	 * @param file 待移动文件或目录
	 * @param path 目标路径
	 * @throws IOException
	 */
	public static void move(File file, String path) throws IOException {
		copy(file, path, true);
	}
	
	/**
	 * 复制文件或目录
	 * 
	 * @param file 待复制的文件或目录
	 * @param path 目标路径
	 * @throws IOException
	 */
	public static void copy(File file, String path) throws IOException {
		copy(file, path, false);
	}

	/**
	 * 移动文件到指定目录
	 * @param files 文件
	 * @param path 目标目录
	 * @throws IOException
	 */
	public static void move(File[] files, String path) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			if (dir.mkdirs()) {
//System.out.println("create directory " + dir.getAbsolutePath());
			}
		}
		for (int i = 0; i < files.length; i++) {
			move(files[i], path + File.separator + files[i].getName());
		}
	}
	
	private static void copy(File file, String path, boolean deleteSrc) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File target = new File(path);
		try {
			if (file.isDirectory()) {
				if (!target.exists()) {
					if (target.mkdirs()) {
//System.out.println("create directory " + target.getAbsolutePath());
					}
				}
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					move(files[i], target.getAbsolutePath() + File.separator + files[i].getName());
				}
			} else {
				bis = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(new FileOutputStream(target));
				byte[] buff = new byte[bufferSize];
				int len = -1;
				while ((len = bis.read(buff)) != -1) {
					bos.write(buff, 0, len);
				}
				bos.flush();
//System.out.println("copy file " + file.getAbsolutePath() + " > " + target.getAbsolutePath());
			}
		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
				if (deleteSrc && target.exists() && file.delete()) {
					System.out.println("delete file " + file.getAbsolutePath());
				}
			} catch (IOException e) { throw e; }
		}
	}
	
	/**
	 * 删除文件或目录

	 * @param file 要删除的文件或目录

	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				delete(files[i]);
			}
		}
		file.delete();
	}
	/**
	 *  返回文件格式
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}
	/**
	 *  返回文件名前缀
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(0,pos);
	}
    public static void main(String args[]){
    	FileOperate fo = FileOperate.getInstance();
    	String path ="D:/riking/importFssDatabak/2014/09";
    	System.out.println(path.split("/")[2]);
    	//String filePath = null;
    	for(int i=0;i<path.split("/").length;i++){
    		//filePath = path.substring(0,path.split("/")[2];
    	}
    	fo.newFolder(path);
    	//fo.uploadFile(new File("d://setupData.xls"), "e://abc//edf//set.xls");
    }
}
