package bjtu.group4.mealplanner.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Friend;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.model.User;

public class ConnectServer {
	//public static String path = "http://59.64.4.63:8080/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1
	//public static String path = "http://172.28.32.75:8080/mealplanner/";
	public static String path = "http://192.16.137.1:8080/mealplanner/";
	/**
	 * 用户登录
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
	 * 所有餐厅
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Restaurant> getRestaurantsAll(int start, int end) {
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		String url = path + "app/rest/getSeveralRestWithMenu?";
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
	
	/**
	 * 所有好友
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Friend> getFriendsAll(int userId) {
		List<Friend> friends = new ArrayList<Friend>();
		String url = path + "app/friend/getAllFriends?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");
		
		String str = HttpUtils.postData(url, map);
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("getFriendsAll", "success");
				JSONObject objData = obj.getJSONObject("data");
				JSONArray objFriendsArray = objData.getJSONArray("userInfos");
				for (int i = 0; i < objFriendsArray.length(); i++) {
					JSONObject dataObj = objFriendsArray.getJSONObject(i);
					
					Friend friend = new Friend();
					friend.setFriendId(dataObj.getInt("userid"));
					friend.setFriendNameString(dataObj.getString("username"));
					friend.setFriendPhone(dataObj.getString("phonenum"));
					//friend.setFriendEmail(objData.getString("email"));
					
					friends.add(friend);
				}
			}
			else {
				Log.d("getFriendsAll", "can not get friendsAll data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friends;
	}

	public boolean sendInvitation(int restId, String dateTime, String friendIds) {
		int userId = SharedData.USERID;
		String url = path + "app/meal/createMeal?";
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("restId", restId+"");
		map.put("datetime",dateTime);
		map.put("userId", userId+"");
		map.put("friendIds",friendIds);
		
		String str = HttpUtils.postData(url, map);
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("sendInvitation", "success");
				return true;
			}
			else {
				Log.d("sendInvitation", "send sendInvitation data fail");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;		
	}
}
