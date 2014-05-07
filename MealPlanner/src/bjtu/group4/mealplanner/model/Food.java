package bjtu.group4.mealplanner.model;

import java.io.Serializable;


public class Food implements Serializable {
	private static final long serialVersionUID = -1413660098525712681L;
	private int foodId;
	private int isHot;
	private int foodType;
	private String foodName;
	private double foodPrice;
	public int getFoodId() {
		return foodId;
	}
	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}
	public int getIsHot() {
		return isHot;
	}
	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	public int getFoodType() {
		return foodType;
	}
	public void setFoodType(int foodType) {
		this.foodType = foodType;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public double getFoodPrice() {
		return foodPrice;
	}
	public void setFoodPrice(double foodPrice) {
		this.foodPrice = foodPrice;
	}
	
	
}
