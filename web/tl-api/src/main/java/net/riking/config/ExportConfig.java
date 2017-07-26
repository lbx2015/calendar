package net.riking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by bing.xun on 2017/6/7.
 */
@Component("exportConfig")
@ConfigurationProperties(prefix = "export")
public class ExportConfig {

    private String exportBigamountXmlDir;

    private String exportBigamountZipDir;

    private String exportBigamountNowDir;

    private String exportBigamountNXmlDir;

    private String feedBackBigamountDir;

    private String feedBacktempBigamountDir;

    private Integer size;

    private String exportShadinessXmlDir;

    private String exportShadinessZipDir;

    private String exportShadinessNXmlDir;

    private String exportShadinessNowDir;

    public String getExportBigamountXmlDir() {
        return exportBigamountXmlDir;
    }

    public void setExportBigamountXmlDir(String exportBigamountXmlDir) {
        this.exportBigamountXmlDir = exportBigamountXmlDir;
    }

    public String getExportBigamountZipDir() {
        return exportBigamountZipDir;
    }

    public void setExportBigamountZipDir(String exportBigamountZipDir) {
        this.exportBigamountZipDir = exportBigamountZipDir;
    }

    public String getExportBigamountNowDir() {
        return exportBigamountNowDir;
    }

    public void setExportBigamountNowDir(String exportBigamountNowDir) {
        this.exportBigamountNowDir = exportBigamountNowDir;
    }

    public String getExportBigamountNXmlDir() {
        return exportBigamountNXmlDir;
    }

    public void setExportBigamountNXmlDir(String exportBigamountNXmlDir) {
        this.exportBigamountNXmlDir = exportBigamountNXmlDir;
    }

    public String getFeedBackBigamountDir() {
        return feedBackBigamountDir;
    }

    public void setFeedBackBigamountDir(String feedBackBigamountDir) {
        this.feedBackBigamountDir = feedBackBigamountDir;
    }

    public String getFeedBacktempBigamountDir() {
        return feedBacktempBigamountDir;
    }

    public void setFeedBacktempBigamountDir(String feedBacktempBigamountDir) {
        this.feedBacktempBigamountDir = feedBacktempBigamountDir;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getExportShadinessXmlDir() {
        return exportShadinessXmlDir;
    }

    public void setExportShadinessXmlDir(String exportShadinessXmlDir) {
        this.exportShadinessXmlDir = exportShadinessXmlDir;
    }

    public String getExportShadinessZipDir() {
        return exportShadinessZipDir;
    }

    public void setExportShadinessZipDir(String exportShadinessZipDir) {
        this.exportShadinessZipDir = exportShadinessZipDir;
    }

    public String getExportShadinessNXmlDir() {
        return exportShadinessNXmlDir;
    }

    public void setExportShadinessNXmlDir(String exportShadinessNXmlDir) {
        this.exportShadinessNXmlDir = exportShadinessNXmlDir;
    }

    public String getExportShadinessNowDir() {
        return exportShadinessNowDir;
    }

    public void setExportShadinessNowDir(String exportShadinessNowDir) {
        this.exportShadinessNowDir = exportShadinessNowDir;
    }
}
