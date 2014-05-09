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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

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
        
        //自动登录代码
//        if(shared.getBoolean("auto", false)){
//        	//向SharedData静态类中写数据
//			SharedData.USERID = shared.getInt("userId", 0);
//			SharedData.USERNAME = shared.getString("username", "");
//			SharedData.NICKNAME = shared.getString("nickname", "");
//			SharedData.PASSWORD = shared.getString("password", "");
//			SharedData.GENDER = shared.getString("gender", "");
//			SharedData.EMAIL = shared.getString("email", "");
//        	
//        	Intent i = new Intent();
//        	i.setClass(Login.this, MainActivity.class);
//        	startActivity(i);
//        	Login.this.finish();
//        }

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
//			Intent intent = new Intent();
//			intent.setClass(Login.this, Regist.class);
//			startActivity(intent);
//			Login.this.finish();
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
				//登陆成功
				SharedPreferences shared = getSharedPreferences("user_info", MODE_PRIVATE);
				//清空之前的数据
				Editor editor = shared.edit();
				editor.clear();
				editor.commit();
				//向SharedPreferences中增加数据
				editor.putBoolean("auto", true);
				editor.putInt("userId", user.getId());
				editor.putString("username", user.getUsername());
				editor.putString("nickname", user.getNickname());
				editor.putString("password", user.getPassword());
				editor.putString("gender", user.getGender());
				editor.putString("email", user.getEmail());
				editor.commit();
				
				//向SharedData静态类中写数据
				SharedData.USERID = user.getId();
				SharedData.USERNAME = user.getUsername();
				SharedData.NICKNAME = user.getNickname();
				SharedData.PASSWORD = user.getPassword();
				SharedData.GENDER = user.getGender();
				SharedData.EMAIL = user.getEmail();
				
				//Application 记录登录状态
				MealApplication application = (MealApplication) getApplication();
				application.setIsLogin(true);
				
				return new Integer(1);
			}else{
				return new Integer(0);
				
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
				//登陆成功
				case 1:
					//显示登陆成功的信息
					Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG).show();
					
					//登录成功 进行页面跳转
					Intent intent = new Intent();
					intent.setClass(Login.this, MainActivity.class);
					startActivity(intent);
					Login.this.finish();
					
					break;
				//登陆失败
				case 0:
					Toast.makeText(Login.this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
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
