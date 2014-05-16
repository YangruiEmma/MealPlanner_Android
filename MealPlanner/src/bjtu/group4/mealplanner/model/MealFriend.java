package bjtu.group4.mealplanner.model;

import java.io.Serializable;

public class MealFriend implements Serializable {

	private static final long serialVersionUID = 4612983462090060586L;
	private int status = -1;
	private Friend mFriend = new Friend();
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Friend getmFriend() {
		return mFriend;
	}

	public static final int ACCEPT = 1;
	public static final int REFUSE = 2;
	public static final int WAITING = 0;

	public String getStatusString() {
		String s = "";
		switch (status) {
		case ACCEPT:
			s = "同意";
			break;
		case REFUSE:
			s = "拒绝";
			break;
		case WAITING:
			s = "等待回复";
			break;
		default:
			break;
		}
		return s;
	}
}
