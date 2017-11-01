package com.riking.calendar.pojo.synch;

import com.riking.calendar.pojo.AppUserReportCompleteRel;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;

import java.util.List;

/**
 * 同步信息封装的类
 *
 * @author lucky.liu
 * @version crateTime：2017年8月10日 下午2:50:09
 * @used TODO
 */
public class SynResult {
    public List<ReminderModel> remind;
    public List<TaskModel> todo;
    public List<AppUserReportCompleteRel> appUserReportCompleteRel;

}
