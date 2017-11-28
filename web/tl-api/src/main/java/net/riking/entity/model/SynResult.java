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
	private List<SysDays> days;
	private List<QueryReport> reportList;

	public SynResult() {
	}

	public SynResult(List<Remind> remind,  List<Todo> todo, List<SysDays> days,List<QueryReport> reportList) {
		this.remind = remind;
		this.todo = todo;
		this.days = days;
		this.reportList = reportList;
	}

	public List<SysDays> getDays() {
		return days;
	}

	public void setDays(List<SysDays> days) {
		this.days = days;
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

	public List<QueryReport> getReportList() {
		return reportList;
	}

	public void setReportList(List<QueryReport> reportList) {
		this.reportList = reportList;
	}

}
