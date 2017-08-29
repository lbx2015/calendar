package net.riking.entity.model;

import java.util.List;

/**
 * 同步信息封装的类
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月10日 下午2:50:09
 * @used TODO
 */
public class SynResult {

	private List<Remind> remind;
	private List<Todo> todo;
	private List<AppUserReportCompleteRel> appUserReportCompleteRel;

	public SynResult() {
	}

	public SynResult(List<Remind> remind,  List<Todo> todo, List<AppUserReportCompleteRel> appUserReportCompleteRel) {
		this.remind = remind;
		this.todo = todo;
		this.appUserReportCompleteRel = appUserReportCompleteRel;
	}

	

	public List<AppUserReportCompleteRel> getAppUserReportCompleteRel() {
		return appUserReportCompleteRel;
	}

	public void setAppUserReportCompleteRel(List<AppUserReportCompleteRel> appUserReportCompleteRel) {
		this.appUserReportCompleteRel = appUserReportCompleteRel;
	}

	public List<Remind> getRemind() {
		return remind;
	}

	public void setRemind(List<Remind> remind) {
		this.remind = remind;
	}

	public List<Todo> getTodo() {
		return todo;
	}

	public void setTodo(List<Todo> todo) {
		this.todo = todo;
	}

}
