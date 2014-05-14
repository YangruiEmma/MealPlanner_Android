package bjtu.group4.mealplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Order implements Serializable{

	private static final long serialVersionUID = 3568643101894525737L;
	
	private int orderId;
	private int mealId;
	private int restId;
	private int peopleNum;
	private int orderState;
	private String phoneNum;
	private String restName;
	private long mealTime;
	private List<Food> dishes = new ArrayList<Food>();
	
	public long getMealTime() {
		return mealTime;
	}

	public void setMealTime(long mealTime) {
		this.mealTime = mealTime;
	}

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public List<Food> getDishes() {
		return dishes;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

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

	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public String getStatusString() {
		String s = "";
		switch (orderState) {
		case SUCCESSE:
			s = "成功";
			break;
		case FAIL:
			s = "失败";
			break;
		case PASS:
			s = "审核通过";
			break;
		case COMPLETE:
			s = "已完成";
			break;
		case CANCLE:
			s = "已取消";
			break;
		default:
			break;
		}
		return s;
	}
	
	public static final int SUCCESSE = 0;
	public static final int FAIL = 1;
	public static final int PASS = 2;
	public static final int CANCLE = 3;
	public static final int COMPLETE = 4;

}
