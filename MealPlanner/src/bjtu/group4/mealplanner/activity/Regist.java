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
     * ����ע��ļ�����
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
				Toast.makeText(Regist.this, "���䲻��ȷ", Toast.LENGTH_LONG).show();
			}else if(!checkPwd(password, password2)){
				regis_pass.requestFocus();
				Toast.makeText(Regist.this, "������������벻һ��", Toast.LENGTH_LONG).show();
			}else{
				
				//����ע����첽�������
				RegistTask task = new RegistTask();
				
				//ע��Ľ�����
				progress = new ProgressDialog(Regist.this);
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("���Ե�");
				progress.setMessage("����ע��");
				// ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
				progress.setIndeterminate(false);
				// ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
				progress.setCancelable(true);
				progress.show();
				
				//ִ���첽ע��
				task.execute(username,phonenum, email,password);
				
			}
		}
    };
    
    /**
   	 * �����������ע����Ϣ���첽������
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
   				//ע��ɹ�
   				SharedPreferences shared = getSharedPreferences("user_info", MODE_PRIVATE);
				//���֮ǰ������
				Editor editor = shared.edit();
				editor.clear();
				editor.commit();
				//��SharedPreferences����������
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
   				//ע��ɹ�
   				case 1:
   					//��ʾע��ɹ�����Ϣ
   					Toast.makeText(Regist.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
   					//ע��ɹ� ����ҳ����ת
   					Intent intent = new Intent();
   					intent.setClass(Regist.this, Login.class);
   					startActivity(intent);
   					Regist.this.finish();
   					break;
   				//ע��ʧ��
   				case 0:
   					Toast.makeText(Regist.this, "ע��ʧ��", Toast.LENGTH_LONG).show();
   					break;
   			}
   		}
   	}
       
       
       /**
        * ��������ʽ�ж������Ƿ�Ϸ�
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
        * �ж���������������Ƿ�һ��
        * @param pwd1
        * @param pwd2
        * @return
        */
       private boolean checkPwd(String pwd1, String pwd2) {
   		return pwd1.equals(pwd2);
   	}

}
