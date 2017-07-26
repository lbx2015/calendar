//package net.riking.util;
//
//
//import java.io.IOException;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import net.riking.config.ExportConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//
///**
// * 邮件服务器信息基类，用于实例化邮件服务器信息
// */
//public class SystemPropertyInfo {
//
//	@Autowired
//	ExportConfig exportConfig;
//
//	static Properties properties = new Properties();
//
//
//	private String exportBigamountXmlDir;
//	private String exportBigamountNXmlDir;
//	private String exportBigamountZipDir;
//	private String feedBackBigamountDir;
//	private String feedBacktempBigamountDir;
//    private String exportBigamountNowDir;
//
//	private String exportShadinessXmlDir;
//	private String exportShadinessNXmlDir;
//	private String exportShadinessZipDir;
//	private String exportShadinessNowDir;
//
//	private Integer size;
//
//
//	/** 配置文件信息信息实例 */
//    private static SystemPropertyInfo instance = null;
//
//    /**
//     * 实例化配置文件信息
//     *
//     * @return 配置文件信息实例
//
//     */
//    public static synchronized SystemPropertyInfo getInstance() {
//        if (instance == null) {
//            instance = new SystemPropertyInfo();
//            instance.init();
//        }
//        return instance;
//    }
//
//    public void init() {
//
//        try {
//			properties.load(new ClassPathResource("application.properties").getInputStream());
//
//			this.setExportBigamountXmlDir(exportConfig.getExportBigamountXmlDir());
//			this.setExportBigamountZipDir(exportConfig.getExportBigamountZipDir());
//			this.setExportBigamountNXmlDir(exportConfig.getExportBigamountNXmlDir());
//			this.setFeedBackBigamountDir(exportConfig.getFeedBackBigamountDir());
//			this.setFeedBacktempBigamountDir(exportConfig.getFeedBacktempBigamountDir());
//			this.setExportBigamountNowDir(exportConfig.getExportBigamountNowDir());
//
//			this.setExportShadinessXmlDir(exportConfig.getExportShadinessXmlDir());
//			this.setExportShadinessNXmlDir(exportConfig.getExportShadinessNXmlDir());
//			this.setExportShadinessZipDir(exportConfig.getExportShadinessZipDir());
//			this.setExportShadinessNowDir(exportConfig.getExportShadinessNowDir());
//			this.setSize(exportConfig.getSize());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//    }
//
//
//
//	public String getExportBigamountNXmlDir() {
//		return exportBigamountNXmlDir;
//	}
//
//	public void setExportBigamountNXmlDir(String exportBigamountNXmlDir) {
//		this.exportBigamountNXmlDir = exportBigamountNXmlDir;
//	}
//
//
//	public String getExportBigamountXmlDir() {
//		return exportBigamountXmlDir;
//	}
//
//	public void setExportBigamountXmlDir(String exportBigamountXmlDir) {
//		this.exportBigamountXmlDir = exportBigamountXmlDir;
//	}
//
//	public String getExportBigamountZipDir() {
//		return exportBigamountZipDir;
//	}
//
//	public void setExportBigamountZipDir(String exportBigamountZipDir) {
//		this.exportBigamountZipDir = exportBigamountZipDir;
//	}
//
//	public String getExportShadinessXmlDir() {
//		return exportShadinessXmlDir;
//	}
//
//	public void setExportShadinessXmlDir(String exportShadinessXmlDir) {
//		this.exportShadinessXmlDir = exportShadinessXmlDir;
//	}
//
//	public String getExportShadinessNXmlDir() {
//		return exportShadinessNXmlDir;
//	}
//
//	public void setExportShadinessNXmlDir(String exportShadinessNXmlDir) {
//		this.exportShadinessNXmlDir = exportShadinessNXmlDir;
//	}
//
//	public String getExportShadinessZipDir() {
//		return exportShadinessZipDir;
//	}
//
//	public void setExportShadinessZipDir(String exportShadinessZipDir) {
//		this.exportShadinessZipDir = exportShadinessZipDir;
//	}
//
//
//	public String getFeedBackBigamountDir() {
//		return feedBackBigamountDir;
//	}
//
//	public void setFeedBackBigamountDir(String feedBackBigamountDir) {
//		this.feedBackBigamountDir = feedBackBigamountDir;
//	}
//
//	public String getFeedBacktempBigamountDir() {
//		return feedBacktempBigamountDir;
//	}
//
//	public void setFeedBacktempBigamountDir(String feedBacktempBigamountDir) {
//		this.feedBacktempBigamountDir = feedBacktempBigamountDir;
//	}
//
//	public String getExportBigamountNowDir() {
//		return exportBigamountNowDir;
//	}
//
//	public void setExportBigamountNowDir(String exportBigamountNowDir) {
//		this.exportBigamountNowDir = exportBigamountNowDir;
//	}
//
//
//
//	public Integer getSize() {
//		return size;
//	}
//
//	public void setSize(Integer size) {
//		this.size = size;
//	}
//
//	public String getExportShadinessNowDir() {
//		return exportShadinessNowDir;
//	}
//
//	public void setExportShadinessNowDir(String exportShadinessNowDir) {
//		this.exportShadinessNowDir = exportShadinessNowDir;
//	}
//
//	private static boolean isEmpty(String value) {
//        return (value == null || value.trim().equals("")) ? true : false;
//    }
//}
