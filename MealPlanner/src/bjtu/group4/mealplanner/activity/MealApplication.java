package bjtu.group4.mealplanner.activity;

import android.app.Application;

public class MealApplication  extends Application {

	 String loginName;

	 String loginPwdString;

	 Boolean isLogin = false;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwdString() {
		return loginPwdString;
	}

	public void setLoginPwdString(String loginPwdString) {
		this.loginPwdString = loginPwdString;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}


}
