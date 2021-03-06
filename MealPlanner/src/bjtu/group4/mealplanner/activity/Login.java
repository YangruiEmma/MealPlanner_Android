package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.User;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.HttpUtils;
import bjtu.group4.mealplanner.utils.SharedData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "NewApi", "UseValueOf" })
public class Login extends Activity {

	private EditText username;
	private EditText password;
	private Button btnLogin;
	private Button btnRegist;
	//private Button btnSee;
	private ProgressDialog progress;
	MealApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		application = (MealApplication) getApplication();
		bindViews();
		setListeners();
	}

	@SuppressLint("WorldReadableFiles")
	private void bindViews() {
		username=(EditText)this.findViewById(R.id.username);
		password=(EditText)this.findViewById(R.id.password);
		btnLogin=(Button)this.findViewById(R.id.btnLogin);
		btnRegist=(Button)this.findViewById(R.id.btnRegist);

		//从SharedPreferences中获得用户上一次输入的用户名和密码
		@SuppressWarnings("deprecation")
		SharedPreferences shared = this.getSharedPreferences("user_info",  Context.MODE_WORLD_READABLE);
		String name = shared.getString("username", "");
		String pass = shared.getString("password", "");
		//设置界面上的用户名和密码
		username.setText(name);
		password.setText(pass);

	}

	private void setListeners() {
		btnLogin.setOnClickListener(login_action);
		btnRegist.setOnClickListener(regist_action);

	}

	/**
	 * 定义监听器
	 */
	private Button.OnClickListener login_action = new Button.OnClickListener(){

		public void onClick(View v) {

			if(username.getText().toString().trim().equals("")){
				Toast.makeText(Login.this, "请输入登陆账号", Toast.LENGTH_LONG).show();
			}else if(password.getText().toString().trim().equals("")){
				Toast.makeText(Login.this, "请输入登陆密码", Toast.LENGTH_LONG).show();
			}else if(!HttpUtils.IsHaveInternet(Login.this)){
				Toast.makeText(Login.this, "无法连接到网络，请检查网络配置", Toast.LENGTH_LONG).show();
			}else{
				//创建提交登录信息的异步任务对象
				LoginTask task = new LoginTask();

				progress = new ProgressDialog(Login.this);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("请稍等");
				progress.setMessage("正在登录");
				// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
				progress.setIndeterminate(false);
				// 设置ProgressDialog 是否可以按退回键取消
				progress.setCancelable(true);
				progress.show();

				//异步登陆进行
				task.execute(username.getText().toString().trim(), password.getText().toString().trim());
			}
		}
	};

	/**
	 * 定义跳转到注册页面的监听器
	 */
	private Button.OnClickListener regist_action = new Button.OnClickListener(){

		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Login.this, Regist.class);
			startActivity(intent);
			Login.this.finish();
		}
	};

	/**
	 * 向服务器发送登录信息的异步任务类
	 * */
	@SuppressLint("UseValueOf")
	private class LoginTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String name = (String)params[0];
			String pwd = (String)params[1];

			User user = new ConnectServer().userLogin(name, pwd);

			if(user!=null){

				if (user.isloginCorrect()) {
					//登陆成功
					SharedPreferences shared = getSharedPreferences("user_info", MODE_PRIVATE);
					//清空之前的数据
					Editor editor = shared.edit();
					editor.clear();
					editor.commit();
					//向SharedPreferences中增加数据
					editor.putBoolean("auto", false);
					editor.putInt("userId", user.getId());
					editor.putString("username", user.getUsername());
					editor.putString("password", user.getPassword());
					editor.putString("email", user.getEmail());
					editor.commit();

					//向SharedData静态类中写数据
					SharedData.USERID = user.getId();
					SharedData.USERNAME = user.getUsername();
					SharedData.NICKNAME = user.getNickname();
					SharedData.PASSWORD = user.getPassword();
					SharedData.GENDER = user.getGender();
					SharedData.EMAIL = user.getEmail();
					SharedData.PHONE = user.getPhone();

					//Application 记录登录状态
					application.setIsLogin(true);
					application.setLoginName(user.getUsername());
					application.setUserId(user.getId());

					return new Integer(0);
				} else {
					return new Integer(1);
				}
			}else{
				return new Integer(2);

			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			//登陆成功
			case 0:
				//显示登陆成功的信息
				Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG).show();

				//登录成功 进行页面跳转
				Intent intent = new Intent();
				intent.setClass(Login.this, MainActivity.class);
				startActivity(intent);
				Login.this.finish();

				break;
				//登陆失败
			case 1:
				Toast.makeText(Login.this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(Login.this, "网络连接失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
