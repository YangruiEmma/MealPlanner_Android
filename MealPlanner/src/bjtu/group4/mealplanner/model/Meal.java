package bjtu.group4.mealplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.maps.MapsInitializer;

import android.R.integer;
import android.R.string;

public class Meal implements Serializable {

	private static final long serialVersionUID = -5124350915356873375L;
	private int mealId;
	private int restId;
	private int mealState;
	private int organizerID;
	private String organizer;
	private String restName;
	private long mealTime;
	private long organizationtime;
	Map<Integer, Friend> mealFriendList = new HashMap<Integer, Friend>();
	
	public int getMealId() {
		return mealId;
	}
	public void setMealId(int mealId) {
		this.mealId = mealId;
	}
	public int getRestId() {
		return restId;
	}
	public void setRestId(int restId) {
		this.restId = restId;
	}

	public int getMealState() {
		return mealState;
	}
	public void setMealState(int mealState) {
		this.mealState = mealState;
	}
	public int getOrganizerID() {
		return organizerID;
	}
	public void setOrganizerID(int organizerID) {
		this.organizerID = organizerID;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public String getRestName() {
		return restName;
	}
	public void setRestName(String restName) {
		this.restName = restName;
	}
	public long getMealTime() {
		return mealTime;
	}
	public void setMealTime(long mealTime) {
		this.mealTime = mealTime;
	}
	public long getOrganizationtime() {
		return organizationtime;
	}
	public void setOrganizationtime(long organizationtime) {
		this.organizationtime = organizationtime;
	}
	public Map<Integer, Friend> getMealFriendList() {
		return mealFriendList;
	}
	
	public String getStatusString() {
		String s = "";
		switch (mealState) {
		case ONGOING:
			s = "进行中";
			break;
		case EXPIRE:
			s = "过期";
			break;
		case SUCCESS:
			s = "审核通过";
			break;
		default:
			break;
		}
		return s;
	}
	
	public static final int ONGOING = 0;
	public static final int SUCCESS = 1;
	public static final int EXPIRE = 2;
	
	
}
