package net.riking.entity.model;

import java.util.List;

public class SynResult {

	private List<Remind> remind ;
	private List<RemindHis> remindHis ;
	private List<Todo> todo ;
	public SynResult() {
	}

	public SynResult(List<Remind> remind, List<RemindHis> remindHis, List<Todo> todo) {
		this.remind = remind;
		this.remindHis = remindHis;
		this.todo = todo;
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
