package bjtu.group4.mealplanner.activity;

import java.util.Calendar;

import com.baidu.frontia.FrontiaApplication;

import android.util.Log;

public class MealApplication  extends FrontiaApplication {

	String loginName;

	String loginPwd;

	int userId;

	Boolean isLogin = false;
	
	Boolean hasBind = false;
	
	String baiduUserId;

	String pushMesgChannelId;


	@Override
	public void onCreate() {
		Log.d("Meal", "start application at " + Calendar.getInstance().getTimeInMillis());
		super.onCreate();
		Log.d("Meal", "end application at " + Calendar.getInstance().getTimeInMillis());
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	
	public String getBaiduUserId() {
		return baiduUserId;
	}

	public void setBaiduUserId(String baiduUserId) {
		this.baiduUserId = baiduUserId;
	}

	public String getPushMesgChannelId() {
		return pushMesgChannelId;
	}

	public void setPushMesgChannelId(String pushMesgChannelId) {
		this.pushMesgChannelId = pushMesgChannelId;
	}

	public Boolean hasBind() {
		return hasBind;
	}

	public void setBind(Boolean hasBind) {
		this.hasBind = hasBind;
	}

}
