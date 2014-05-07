package bjtu.group4.mealplanner.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.model.User;

public class ConnectServer {
	//public static String path = "http://59.64.4.63:8080/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1
	public static String path = "http://172.28.9.168:8080/mealplanner/";
	/**
	 * �û���¼
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	//http://localhost:8080/mealplanner/app/login?loginName=minxin&password=123456
	public User userLogin(String username, String password) {
		User user = null;
		String url = path + "app/login?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginName", username);
		map.put("password", password);

		String str = HttpUtils.postData(url, map);

		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				JSONObject userinfo = (JSONObject) obj
						.getJSONObject("data");

				user = new User();
				user.setId(userinfo.getInt("userid"));
				user.setUsername(userinfo.getString("username"));
				user.setPassword(userinfo.getString("password"));
				user.setEmail(userinfo.getString("email"));
				user.setPhone(userinfo.getString("phonenum"));
				user.setUserType(userinfo.getString("usertype"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * ���в���
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Restaurant> getRestaurantsAll(int start, int end) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		String url = path + "app/rest/getRest?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("start", start + "");
		map.put("end", end + "");
		
		String str = HttpUtils.postData(url, map);
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("getRestaurantsAll", "success");
				JSONArray restInfos = obj.getJSONArray("data");
				for (int i = 0; i < restInfos.length(); i++) {
					Restaurant rest = new Restaurant();
					JSONObject dataObj = restInfos.getJSONObject(i);
					JSONObject objRestInfo = dataObj.getJSONObject("restaurantInfo");
					rest.setId(objRestInfo.getInt("restid"));
					rest.setName(objRestInfo.getString("restname"));
					rest.setPhoneNum(objRestInfo.getString("restphone"));
					rest.setCity(objRestInfo.getInt("restcity"));
					rest.setPosition(objRestInfo.getString("restaddress"));
					rest.setRestType(objRestInfo.getInt("resttype"));
					rest.setIsHot(objRestInfo.getInt("hot"));
					
					List<Food> foods = rest.getDishes();
					JSONArray foodsArray = dataObj.getJSONArray("menuInfos");
					for(int j = 0; j < foodsArray.length(); j++) {
						Food food = new Food();
						JSONObject objFood = foodsArray.getJSONObject(j);
						
						food.setFoodId(objFood.getInt("menuid"));
						food.setFoodName(objFood.getString("menuname"));
						food.setFoodPrice(objFood.getDouble("menuprice"));
						food.setIsHot(objFood.getInt("hot"));
						food.setFoodType(objFood.getInt("foodtype"));
						foods.add(food);
					}
					restaurants.add(rest);
				}
			}
			else {
				Log.d("getRestaurantsAll", "can not get restAll data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restaurants;
	}
	
	public String test() {
		String url = path + "userinfo?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", "1");
		String str = HttpUtils.postData(url, map);

		return str;
	}
}
