package bjtu.group4.mealplanner.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.User;
import bjtu.group4.mealplanner.utils.ConnectServer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Regist extends Activity {

	private EditText regis_name;
	private EditText regis_phone;
	private EditText regis_email;
	private EditText regis_pass;
	private EditText regis_pass2;
	private Button btnRegist; 
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		bindViews();
		setListeners();
	}

	private void bindViews() {
		regis_name = (EditText)this.findViewById(R.id.registname);
    	regis_phone = (EditText)this.findViewById(R.id.registPhone);
    	regis_email = (EditText)this.findViewById(R.id.registemail);
    	regis_pass = (EditText)this.findViewById(R.id.registpwd);
    	regis_pass2 = (EditText)this.findViewById(R.id.registpwdsure);
    	btnRegist = (Button)this.findViewById(R.id.btnRegist);
	}

	public void setListeners(){
    	btnRegist.setOnClickListener(regist_action);
    }

	/**
     * 定义注册的监听器
     */
    private Button.OnClickListener regist_action = new Button.OnClickListener(){

		public void onClick(View v) {
			String username = regis_name.getText().toString().trim();
			String phonenum = regis_phone.getText().toString().trim();
			String email = regis_email.getText().toString().trim();
			String password = regis_pass.getText().toString().trim();
			String password2 = regis_pass2.getText().toString().trim();
			
			if(!checkEmail(email)){
				regis_email.requestFocus();
				Toast.makeText(Regist.this, "邮箱不正确", Toast.LENGTH_LONG).show();
			}else if(!checkPwd(password, password2)){
				regis_pass.requestFocus();
				Toast.makeText(Regist.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
			}else{
				
				//调用注册的异步任务对象
				RegistTask task = new RegistTask();
				
				//注册的进度条
				progress = new ProgressDialog(Regist.this);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("请稍等");
				progress.setMessage("正在注册");
				// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
				progress.setIndeterminate(false);
				// 设置ProgressDialog 是否可以按退回键取消
				progress.setCancelable(true);
				progress.show();
				
				//执行异步注册
				task.execute(username,phonenum, email,password);
				
			}
		}
    };
    
    /**
   	 * 向服务器发送注册信息的异步任务类
   	 * */
   	private class RegistTask extends AsyncTask<Object, Integer, Integer> {

   		@SuppressLint("UseValueOf")
		@Override
   		protected Integer doInBackground(Object... params) {
   			String username = (String)params[0];
   			String phonenum = (String)params[1];
   			String email = (String)params[2];
   			String password = (String)params[3];
   			
   			User user = new ConnectServer().regist(username,phonenum, email,password);
   			
   			if(user != null){
   				//注册成功
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
   				//注册成功
   				case 1:
   					//显示注册成功的信息
   					Toast.makeText(Regist.this, "注册成功", Toast.LENGTH_LONG).show();
   					//注册成功 进行页面跳转
   					Intent intent = new Intent();
   					intent.setClass(Regist.this, Login.class);
   					startActivity(intent);
   					Regist.this.finish();
   					break;
   				//注册失败
   				case 0:
   					Toast.makeText(Regist.this, "注册失败", Toast.LENGTH_LONG).show();
   					break;
   			}
   		}
   	}
       
       
       /**
        * 用正则表达式判断邮箱是否合法
        * @param email
        * @return
        */
       private boolean checkEmail(String email) {
   		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
   		Pattern regex = Pattern.compile(check);
   		Matcher matcher = regex.matcher(email);
   		boolean isMatched = matcher.matches();
   		return isMatched;
   	}
       
       /**
        * 判断两次输入的密码是否一致
        * @param pwd1
        * @param pwd2
        * @return
        */
       private boolean checkPwd(String pwd1, String pwd2) {
   		return pwd1.equals(pwd2);
   	}

}
