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

		//��SharedPreferences�л���û���һ��������û���������
        @SuppressWarnings("deprecation")
		SharedPreferences shared = this.getSharedPreferences("user_info",  Context.MODE_WORLD_READABLE);
        String name = shared.getString("username", "");
        String pass = shared.getString("password", "");
        //���ý����ϵ��û���������
        username.setText(name);
        password.setText(pass);
        
        //�Զ���¼����
//        if(shared.getBoolean("auto", false)){
//        	//��SharedData��̬����д����
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
     * ���������
     */
    private Button.OnClickListener login_action = new Button.OnClickListener(){

		public void onClick(View v) {
			
			if(username.getText().toString().trim().equals("")){
				Toast.makeText(Login.this, "�������½�˺�", Toast.LENGTH_LONG).show();
			}else if(password.getText().toString().trim().equals("")){
				Toast.makeText(Login.this, "�������½����", Toast.LENGTH_LONG).show();
			}else if(!HttpUtils.IsHaveInternet(Login.this)){
				Toast.makeText(Login.this, "�޷����ӵ����磬������������", Toast.LENGTH_LONG).show();
			}else{
				//�����ύ��¼��Ϣ���첽�������
				LoginTask task = new LoginTask();
				
				progress = new ProgressDialog(Login.this);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("���Ե�");
				progress.setMessage("���ڵ�¼");
				// ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
				progress.setIndeterminate(false);
				// ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
				progress.setCancelable(true);
				progress.show();
				
				//�첽��½����
				task.execute(username.getText().toString().trim(), password.getText().toString().trim());
			}
		}
    };
    
    /**
     * ������ת��ע��ҳ��ļ�����
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
	 * ����������͵�¼��Ϣ���첽������
	 * */
    @SuppressLint("UseValueOf")
	private class LoginTask extends AsyncTask<Object, Integer, Integer>{

    	/**
		 * ִ���첽����ʱ�ص�����Ui�߳���ִ��
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String name = (String)params[0];
			String pwd = (String)params[1];
			
			User user = new ConnectServer().userLogin(name, pwd);
			
			if(user!=null){
				//��½�ɹ�
				SharedPreferences shared = getSharedPreferences("user_info", MODE_PRIVATE);
				//���֮ǰ������
				Editor editor = shared.edit();
				editor.clear();
				editor.commit();
				//��SharedPreferences����������
				editor.putBoolean("auto", true);
				editor.putInt("userId", user.getId());
				editor.putString("username", user.getUsername());
				editor.putString("nickname", user.getNickname());
				editor.putString("password", user.getPassword());
				editor.putString("gender", user.getGender());
				editor.putString("email", user.getEmail());
				editor.commit();
				
				//��SharedData��̬����д����
				SharedData.USERID = user.getId();
				SharedData.USERNAME = user.getUsername();
				SharedData.NICKNAME = user.getNickname();
				SharedData.PASSWORD = user.getPassword();
				SharedData.GENDER = user.getGender();
				SharedData.EMAIL = user.getEmail();
				
				//Application ��¼��¼״̬
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
				//��½�ɹ�
				case 1:
					//��ʾ��½�ɹ�����Ϣ
					Toast.makeText(Login.this, "��¼�ɹ�", Toast.LENGTH_LONG).show();
					
					//��¼�ɹ� ����ҳ����ת
					Intent intent = new Intent();
					intent.setClass(Login.this, MainActivity.class);
					startActivity(intent);
					Login.this.finish();
					
					break;
				//��½ʧ��
				case 0:
					Toast.makeText(Login.this, "�û��������������", Toast.LENGTH_LONG).show();
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
