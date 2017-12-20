package net.riking.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	public static String getProjectPath(String url) {
		if (StringUtils.isBlank(url)) {
			return url;
		}
		String projectName = url.split("//")[1].split("/")[0];
		String projectPath = url.substring(0, url.indexOf(projectName)) + projectName;
		return projectPath;
	}
}
