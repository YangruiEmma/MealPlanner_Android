package bjtu.group4.mealplanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Restaurant implements Serializable {
	private static final long serialVersionUID = -318131827668128307L;
	private int restId;
	private int restCity;
	private int restType;
	private int isHot;
	private String restName;
	private String restPhoneNum;
	private String restAddress;
	private double latitude;	// Î³¶È
	private double longtitude;	// ¾­¶È
	
	private List<Food> dishes;
	
	public Restaurant() {
		dishes = new ArrayList<Food>();
	}
	public List<Food> getDishes() {
		return dishes;
	}
	public int getId() {
		return restId;
	}
	public void setId(int id) {
		this.restId = id;
	}
	public int getCity() {
		return restCity;
	}
	public void setCity(int city) {
		this.restCity = city;
	}
	public String getName() {
		return restName;
	}
	public void setName(String name) {
		this.restName = name;
	}
	public String getPhoneNum() {
		return restPhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.restPhoneNum = phoneNum;
	}
	public String getPosition() {
		return restAddress;
	}
	public void setPosition(String position) {
		this.restAddress = position;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public int getRestType() {
		return restType;
	}
	public void setRestType(int restType) {
		this.restType = restType;
	}
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	

}
