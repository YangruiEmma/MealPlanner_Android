package bjtu.group4.mealplanner.utils;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import bjtu.group4.mealplanner.model.User;

public class ConnectServer {
	public static String path = "http://59.64.4.63:8080/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1

	/**
	 * ÓÃ»§µÇÂ¼
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
	public String test() {
		String url = path + "userinfo?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", "1");
		String str = HttpUtils.postData(url, map);

		return str;
	}
}
