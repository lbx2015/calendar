package net.riking.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JZip {

	static final int BUFFER = 2048;

	public static int iCompressLevel;// 压缩比 取值范围为0~9

	public static boolean bOverWrite;// 是否覆盖同名文件 取值范围为True和False

	private static ArrayList<File> AllFiles = new ArrayList<File>();

	public static String sErrorMessage;

	// ============2008-08-01frank
	private String zipFilePath;

	public List<File> srcMap;

	public JZip() {

		iCompressLevel = 9;
		// bOverWrite=true;
	}

	// ============2008-08-01frank
	public JZip(String zipFilePath) throws FileNotFoundException, IOException {

		this.zipFilePath = zipFilePath;

	}

	public static ArrayList<String> extract(String sZipPathFile, String sDestPath) {

		ArrayList<String> AllFileName = new ArrayList<String>();
		FileInputStream fins = null;
		ZipInputStream zins = null;
		try {

			// 先指定压缩档的位置和档名,建立FileInputStream对象
			fins = new FileInputStream(sZipPathFile);
			// 将fins传入ZipInputStream中
			zins = new ZipInputStream(fins);
			ZipEntry ze = null;
			byte ch[] = new byte[256];
			while ((ze = zins.getNextEntry()) != null) {
				File zfile = new File(sDestPath + ze.getName());
				File fpath = new File(zfile.getParentFile().getPath());
				if (ze.isDirectory()) {
					if (!zfile.exists())
						zfile.mkdirs();
					zins.closeEntry();
				} else {
					if (!fpath.exists())
						fpath.mkdirs();
					FileOutputStream fouts = new FileOutputStream(zfile);
					int i;
					AllFileName.add(zfile.getAbsolutePath());
					while ((i = zins.read(ch)) != -1)
						fouts.write(ch, 0, i);
					zins.closeEntry();
					fouts.close();
				}
			}
			
			sErrorMessage = "OK";
		} catch (Exception e) {
			System.err.println("Extract error:" + e.getMessage());
			sErrorMessage = e.getMessage();
		}finally{
			try {
				if(fins!=null){
					fins.close();
				}
				if(zins!=null){
					zins.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		AllFiles.clear();
		return AllFileName;
	}

	public static void compress(String sPathFile, boolean isForder, String sZipPathFile) {
		BufferedOutputStream bos = null;
		ZipOutputStream out = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(sZipPathFile));
			out = new ZipOutputStream(bos);
			if (isForder) {
				// f:/yx 是一个目录，下面有一些文件和目录
				File f = new File(sPathFile);
				put(f, out, "");
				
			} else {
				compressFile(sPathFile, sZipPathFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(out!=null)
					out.close();
				
				if(bos!=null)
					bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void compressFile(String pathFile, String zipPathFile) {
		// 创建文件输入流对象
		FileInputStream in = null;
		FileOutputStream out = null;
		ZipOutputStream zipOut = null;
		try {
			in = new FileInputStream(pathFile); // 0
			// 创建文件输出流对象
		    out = new FileOutputStream(zipPathFile); // 1
			// 创建ZIP数据输出流对象
			zipOut = new ZipOutputStream(out);
			// 创建指向压缩原始文件的入口
			if (pathFile.indexOf("/") != -1) {
				pathFile = pathFile.substring(pathFile.lastIndexOf("/") + 1, pathFile.length());
			} else {
				pathFile = pathFile.substring(pathFile.lastIndexOf("\\") + 1, pathFile.length());
			}
			ZipEntry entry = new ZipEntry(pathFile); // 0
			zipOut.putNextEntry(entry);
			// 向压缩文件中输出数据
			int nNumber;
			byte[] buffer = new byte[512];
			while ((nNumber = in.read(buffer)) != -1)
				zipOut.write(buffer, 0, nNumber);
			// 关闭创建的流对象
			

		} catch (IOException e) {
			System.out.println(e);
		}finally{
			try {
				if(null!=zipOut)
				zipOut.close();
				if(out!=null)
				out.close();
				if(in!=null)
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void put(File f, ZipOutputStream out, String dir) throws IOException {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			dir = dir + (dir.length() == 0 ? "" : "/") + f.getName();
			for (File file : files) {
				put(file, out, dir);
			}
		} else {
			byte[] data = new byte[BUFFER];
			FileInputStream fi = new FileInputStream(f);
			BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
			dir = dir.length() == 0 ? "" : f.getName();
			ZipEntry entry = new ZipEntry(dir);
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
	}

	// public static void compress(String sPathFile, boolean bIsPath,
	// String sZipPathFile) {
	//
	// try {
	// String sPath;
	// // 先指定压缩档的位置及档名，建立一个FileOutputStream
	// FileOutputStream fos = new FileOutputStream(sZipPathFile);
	// // 建立ZipOutputStream并将fos传入
	// ZipOutputStream zos = new ZipOutputStream(fos);
	// // 设置压缩比
	// zos.setLevel(iCompressLevel);
	// if (bIsPath == true) {
	// searchFiles(sPathFile);
	// sPath = sPathFile;
	// } else {
	// File myfile = new File(sPathFile);
	// sPath = sPathFile.substring(0, sPathFile
	// .lastIndexOf(myfile.separator) + 1);
	// AllFiles.add(myfile);
	// }
	// Object[] myobject = AllFiles.toArray();
	// ZipEntry ze = null;
	// // 每个档案要压缩，都要透过ZipEntry来处理
	//
	// FileInputStream fis = null;
	// byte[] ch = new byte[256];
	// for (int i = 0; i < myobject.length; i++) {
	// File myfile = (File) myobject[i];
	// if (myfile.isFile()) {
	// // 以档案的名字当Entry，也可以自己再加上额外的路径
	// // 例如 ze=new ZipEntry("test\\"+myfiles[i].getName());
	// // 如此压缩档内的每个档案都会加test这个路径
	// ze = new ZipEntry(myfile.getPath().substring(
	// (sPath).length()));
	// // 将ZipEntry透过ZipOutputStream的putNextEntry的方式送进去处理
	// fis = new FileInputStream(myfile);
	// zos.putNextEntry(ze);
	//
	// // 开始将原始档案读进ZipOutputStream
	// int len;
	// while ((len = fis.read(ch)) != -1)
	// zos.write(ch, 0, len);
	// fis.close();
	// zos.closeEntry();
	// }
	// }
	// zos.close();
	// fos.close();
	// AllFiles.clear();
	// sErrorMessage = "OK";
	// } catch (Exception e) {
	// System.err.println("Compress error:" + e.getMessage());
	// sErrorMessage = e.getMessage();
	// }
	//
	// }

	/*
	 * 这是一个递归过程，功能是检索出所有的文件名称 dirstr:目录名称
	 */
	@SuppressWarnings( { "unused" })
	private static void searchFiles(String dirstr) {

		File tempdir = new File(dirstr);
		if (tempdir.exists()) {
			if (tempdir.isDirectory()) {
				File[] tempfiles = tempdir.listFiles();
				for (int i = 0; i < tempfiles.length; i++) {
					if (tempfiles[i].isDirectory())
						searchFiles(tempfiles[i].getPath());
					else {
						AllFiles.add(tempfiles[i]);
					}
				}
			} else {
				AllFiles.add(tempdir);
			}
		}
	}

	public String getZipFilePath() {
		return zipFilePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	/**
	 * 解析zip文件得到文件名
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean parserZip() throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(zipFilePath);

		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

		ZipEntry entry;

		try {

			srcMap = new ArrayList<File>();

			while ((entry = zis.getNextEntry()) != null) {

				File file = new File(zipFilePath + File.separator + entry.getName());
				srcMap.add(file);

			}

			zis.close();

			fis.close();

			return true;

		} catch (IOException e) {

			return false;

		}

	}

}
