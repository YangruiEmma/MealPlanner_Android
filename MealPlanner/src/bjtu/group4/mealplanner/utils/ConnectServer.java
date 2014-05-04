package bjtu.group4.mealplanner.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import bjtu.group4.mealplanner.model.User;

public class ConnectServer {
	public static String path = "http://59.64.4.63:8080/mealplanner/";//http://localhost:8080/mealplanner/userinfo?userId=1

	// 访问网站数据库获取数据
	private String connWeb(String url) {
		String str = "";
		try {
			HttpGet request = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return str;
	}
	
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
	public String test() {
		String url = path + "userinfo?";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", "1");
		String str = HttpUtils.postData(url, map);

		return str;
	}
}
