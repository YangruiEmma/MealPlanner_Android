package bjtu.group4.mealplanner.model;

import java.io.Serializable;

public class Friend implements Serializable {
	private static final long serialVersionUID = 7021466275877739420L;
	
	private int friendId;
	private String friendNameString;
	private String friendPhone;
	private String friendEmail;
	
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public String getFriendNameString() {
		return friendNameString;
	}
	public void setFriendNameString(String friendNameString) {
		this.friendNameString = friendNameString;
	}
	public String getFriendPhone() {
		return friendPhone;
	}
	public void setFriendPhone(String friendPhone) {
		this.friendPhone = friendPhone;
	}
	public String getFriendEmail() {
		return friendEmail;
	}
	public void setFriendEmail(String friendEmail) {
		this.friendEmail = friendEmail;
	}
	
	
	

}
