package net.riking.task;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.util.FileUtils;

@Component("deleteTempTimerTask")
public class DeleteTempTimerTask extends TimerTask {
	protected final transient Logger logger = LogManager.getLogger(DeleteTempTimerTask.class);

	@Override
	public void run() {
		try {
			// 删除临时文件
			String path = this.getClass().getResource("/").getPath() + Const.TL_STATIC_PATH + Const.TL_TEMP_PHOTO_PATH;
			FileUtils.delAllFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
