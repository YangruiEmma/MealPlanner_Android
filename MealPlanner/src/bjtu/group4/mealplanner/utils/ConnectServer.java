package bjtu.group4.mealplanner.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.internal.is;

import android.R.integer;
import android.util.Log;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Friend;
import bjtu.group4.mealplanner.model.Meal;
import bjtu.group4.mealplanner.model.MealFriend;
import bjtu.group4.mealplanner.model.Order;
import bjtu.group4.mealplanner.model.PushMessageBindInfo;
import bjtu.group4.mealplanner.model.QueueInfo;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.model.User;

public class ConnectServer {
	public static String path = "http://59.64.4.63:8090/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1
	//public static String path = "http://172.28.34.69:8090/mealplanner/";
	//public static String path = "http://59.64.4.63:8090/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1
	//public static String path = "http://172.28.34.136:8090/mealplanner/";
	//public static String path = "http://192.16.137.1:8090/mealplanner/";
	//public static String path = "http://172.28.12.93:8090/mealplanner/";

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

		try {
			String str = HttpUtils.postData(url, map);
			if(str == null)
				return null;
			JSONObject obj = new JSONObject(str);
			user = new User();
			if ((Boolean) obj.get("success")) {
				JSONObject userinfo = (JSONObject) obj
						.getJSONObject("data");

				user.setId(userinfo.getInt("userid"));
				user.setUsername(userinfo.getString("username"));
				user.setPassword(userinfo.getString("password"));
				user.setEmail(userinfo.getString("email"));
				user.setPhone(userinfo.getString("phonenum"));
				user.setUserType(userinfo.getString("usertype"));
				user.setloginCorrect(true);
			}else {
				user.setloginCorrect(false);
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
		map.put("limit", end + "");
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
						food.setFoodTypeName(objFood.getString("foodTypeName"));
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


	public Restaurant getRestInfo(String restID) {
		Restaurant rest = null;
		String url = path + "app/rest/getRest?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("restId", restID);
		String str = HttpUtils.postData(url, map);

		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				JSONObject restInfo = (JSONObject) obj
						.getJSONObject("data");

				rest = new Restaurant();
				rest.setId(restInfo.getInt("restid"));
				rest.setName(restInfo.getString("restname"));
				rest.setPhoneNum(restInfo.getString("restphone"));
				rest.setCity(restInfo.getInt("restcity"));
				rest.setPosition(restInfo.getString("restaddress"));
				rest.setRestType(restInfo.getInt("resttype"));
				rest.setIsHot(restInfo.getInt("hot"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rest;
	}

	public QueueInfo sendQueueMesg(String restID, String userId, String peopleNum) {
		QueueInfo queueInfo = null;
		String url = path + "app/seq/insertSeq?"; 
		Map<String, String> map = new HashMap<String, String>();
		//restId=3&userId=1&peopleNum=6
		map.put("restId", restID);
		map.put("userId", userId);
		map.put("peopleNum", peopleNum);
		String str = HttpUtils.postData(url, map);

		//"data":{"userId":1,"restId":3,"seqNo":3,"seqNow":0,"seatType":6,"peopleBefore":2,"peopleNum":6}
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				JSONObject queueMsg = (JSONObject) obj
						.getJSONObject("data");

				queueInfo = new QueueInfo();
				queueInfo.setUserId(queueMsg.getInt("userId"));
				queueInfo.setRestId(queueMsg.getInt("restId"));
				queueInfo.setSeqNo(queueMsg.getInt("seqNo"));
				queueInfo.setSeqNow(queueMsg.getInt("seqNow"));
				queueInfo.setSeatType(queueMsg.getInt("seatType"));
				queueInfo.setPeopleBefore(queueMsg.getInt("peopleBefore"));
				queueInfo.setPeopleNum(queueMsg.getInt("peopleNum"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queueInfo;
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

	//http://localhost:8090/mealplanner/app/userBinding?userId=2&baiduUserId=924401985&channelId=4236885180925384783
	public boolean bindBaiduUser(String userId, String baiduUserId, String channelId) {
		PushMessageBindInfo bindInfo = null;
		String url = path + "app/userBinding?"; 
		Map<String, String> map = new HashMap<String, String>();

		map.put("userId", userId);
		map.put("baiduUserId", baiduUserId);
		map.put("channelId", channelId);
		String str = HttpUtils.postData(url, map);

		//		{
		//			"success": true,
		//			"message": "Register userId=2 into baidu userId=924401985 and channel=4236885180925384783 success!",
		//			"data": null
		//			}
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


	public boolean sendOrder(int restId, String dateTime, String dishesIds, String peopleNum, int mealId, String phoneNum) {
		int userId = SharedData.USERID;
		String url = path + "app/order/createOrder?";
		Map<String, String> map = new HashMap<String, String>();

		map.put("restId", restId+"");
		map.put("date",dateTime);
		map.put("userId", userId+"");
		map.put("menuIds",dishesIds);
		map.put("peopleNum",peopleNum+"");
		map.put("phoneNum",phoneNum);
		if(mealId != -1)
			map.put("mealId",mealId +"");

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

	public boolean sendInvitationReply(int mealId, boolean isAccept) {
		int userId = SharedData.USERID;
		String url = "";
		if(isAccept) {
			url = path + "app/meal/acceptMeal?";
		}else {
			url = path + "app/meal/rejectMeal?";
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId+"");
		map.put("mealId",mealId+"");
		
		String str = HttpUtils.postData(url, map);
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("sendInvitationReply", "success");
				return true;
			}
			else {
				Log.d("sendInvitationReply", "send sendInvitation data fail");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;		
	}
	public List<Order> getOrdersAll(int userId) {
		List<Order> orders = new ArrayList<Order>();
		String url = path + "app/order/getOrderByUser?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");

		String str = HttpUtils.postData(url, map);
		try {
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("getOrdersAll", "success");
				JSONArray arrayData = obj.getJSONArray("data");
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject dataObj = arrayData.getJSONObject(i);
					JSONObject orderObj = dataObj.getJSONObject("orderInfo");
					Order order = new Order();
					order.setRestName(dataObj.getString("restName"));

					order.setOrderId(orderObj.getInt("orderid"));
					order.setMealId(orderObj.getInt("mealid"));
					order.setRestId(orderObj.getInt("restid"));
					order.setOrderState(orderObj.getInt("status"));
					order.setPeopleNum(orderObj.getInt("actualpeoplenum"));
					order.setPhoneNum(orderObj.getString("contactinfo"));
					order.setMealTime(orderObj.getLong("mealtime"));

					JSONArray menuArray = dataObj.getJSONArray("menuInfos");
					List<Food> foods = order.getDishes();
					for(int j = 0; j < menuArray.length(); ++j) {
						JSONObject foodObj = menuArray.getJSONObject(j);
						if(foodObj == null) break;
						Food food = new Food();
						food.setFoodId(foodObj.getInt("menuid"));
						food.setFoodName(foodObj.getString("menuname"));
						food.setFoodPrice(foodObj.getDouble("menuprice"));
						food.setFoodTypeName(foodObj.getString("foodTypeName"));
						food.setIsHot(foodObj.getInt("hot"));
						foods.add(food);
					}
					orders.add(order);
				}
			}
			else {
				Log.d("getOrdersAll", "can not get friendsAll data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	public List<Meal> getMealsAll(int userId) {
		List<Meal> meals = new ArrayList<Meal>();
		String url = path + "app/meal/getMealInfo?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");
		map.put("start", 0+"");//start=0&limit=2
		map.put("limit", 5+"");

		String str = HttpUtils.postData(url, map);
		try { 
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("getMealsAll", "success");
				JSONArray arrayData = obj.getJSONArray("data");
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject dataObj = arrayData.getJSONObject(i);
					JSONObject mealObj = dataObj.getJSONObject("mealInfo");
					Meal meal = new Meal();
					meal.setRestName(mealObj.getString("restname"));
					meal.setMealId(mealObj.getInt("mealid"));
					meal.setRestId(mealObj.getInt("restid"));
					meal.setMealState(mealObj.getInt("mealstatus"));
					meal.setOrganizer(mealObj.getString("mealorganizeusername"));
					meal.setOrganizerID(mealObj.getInt("mealorganizeuserid"));
					meal.setOrganizationtime(mealObj.getLong("organizationtime"));
					meal.setMealTime(mealObj.getLong("mealtime"));

					JSONArray mealFriends = dataObj.getJSONArray("mealFriendWithStatusList");
					List<MealFriend> friendStatusList = meal.getMealFriendList();
					for(int j = 0; j < mealFriends.length(); ++j) {
						JSONObject friendObj = mealFriends.getJSONObject(j);
						MealFriend f = new MealFriend();
						f.setStatus(friendObj.getInt("status"));
						JSONObject fObj = friendObj.getJSONObject("friendInfo");
						f.getmFriend().setFriendId(fObj.getInt("userid"));
						f.getmFriend().setFriendEmail(fObj.getString("email"));
						f.getmFriend().setFriendNameString(fObj.getString("username"));
						f.getmFriend().setFriendPhone(fObj.getString("phonenum"));
						
						friendStatusList.add(f);
					}
					meals.add(meal);
				}
			}
			else {
				Log.d("getMealsAll", "can not get friendsAll data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return meals;
	}
	
	public List<Meal> getMealRequestsAll(int userId) {
		List<Meal> meals = new ArrayList<Meal>();
		String url = path + "app/meal/getMealRequest?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");

		String str = HttpUtils.postData(url, map);
		try { 
			JSONObject obj = new JSONObject(str);
			if ((Boolean) obj.get("success")) {
				Log.d("getMealRequestsAll", "success");
				JSONArray arrayData = obj.getJSONArray("data");
				for (int i = 0; i < arrayData.length(); i++) {
					JSONObject dataObj = arrayData.getJSONObject(i);
					Meal meal = new Meal();
					meal.setRestName(dataObj.getString("restname"));
					meal.setMealId(dataObj.getInt("mealid"));
					meal.setRestId(dataObj.getInt("restid"));
					meal.setMealState(dataObj.getInt("mealstatus"));
					meal.setOrganizer(dataObj.getString("mealorganizeusername"));
					meal.setOrganizerID(dataObj.getInt("mealorganizeuserid"));
					meal.setOrganizationtime(dataObj.getLong("organizationtime"));
					meal.setMealTime(dataObj.getLong("mealtime"));
					MealFriend mf = new MealFriend();
					mf.setStatus(dataObj.getInt("status"));
					meal.getMealFriendList().add(mf);
					meals.add(meal);
				}
			}
			else {
				Log.d("getMealRequestsAll", "can not get friendsAll data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return meals;
	}

	public List<Restaurant> getNearbyRest(String url, double lat, double lont) {
		List<Restaurant> restList = new ArrayList<Restaurant>();
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder(url);
		sb.append("location="+lat+","+lont);
		sb.append("&radius=500");
		sb.append("&types=food");
		sb.append("&language=zh_CN");
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyAZbSZ3PPodBqqxzJKsyFtitoprzIRIQWI");
		String str = HttpUtils.postData(sb.toString(), map);
		try {
			JSONObject obj = new JSONObject(str);
			JSONArray jPlaces = obj.getJSONArray("results");
			int placesCount = jPlaces.length();	
			String latString = "";
			String lngString = "";
			for(int i=0; i<placesCount;i++) {
				JSONObject objPlace = jPlaces.getJSONObject(i);
				Restaurant restaurant = new Restaurant();
				if(!objPlace.isNull("name")) 		
					restaurant.setName(objPlace.getString("name"));
				if(!objPlace.isNull("vicinity")) 		
					restaurant.setPosition(objPlace.getString("vicinity"));
				latString = objPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
				lngString = objPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
				restaurant.setLatitude(Double.parseDouble(latString));
				restaurant.setLongtitude(Double.parseDouble(lngString));
				restList.add(restaurant);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restList;
	}

}
