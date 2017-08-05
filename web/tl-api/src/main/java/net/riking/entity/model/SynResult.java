package net.riking.entity.model;

import java.util.List;

public class SynResult {
	
	/**同步信息封装的类
	 * @author Lucky.Liu on 2017/8/05.
	 */

	private List<Remind> remind ;
	private List<RemindHis> remindHis ;
	private List<Todo> todo ;
	private List<BusinessDay> businessDay;
	public SynResult() {
	}

	public SynResult(List<Remind> remind, List<RemindHis> remindHis, List<Todo> todo,List<BusinessDay> businessDay) {
		this.remind = remind;
		this.remindHis = remindHis;
		this.todo = todo;
		this.businessDay = businessDay;
	}

	public List<BusinessDay> getBusinessDay() {
		return businessDay;
	}

	public void setBusinessDay(List<BusinessDay> businessDay) {
		this.businessDay = businessDay;
	}

	public List<Remind> getRemind() {
		return remind;
	}

	public void setRemind(List<Remind> remind) {
		this.remind = remind;
	}

	public List<RemindHis> getRemindHis() {
		return remindHis;
	}

	public void setRemindHis(List<RemindHis> remindHis) {
		this.remindHis = remindHis;
	}

	public List<Todo> getTodo() {
		return todo;
	}

	public void setTodo(List<Todo> todo) {
		this.todo = todo;
	}
	
	
}
