package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("HandlerLeak")
public class Welcome extends Activity {

	//	private Button testBtn;//testBtn
	//	private EditText testText;
	//  private ConnectServer conn=  new ConnectServer();
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ImageView iv = (ImageView)this.findViewById(R.id.wpic);
		iv.setImageResource(R.drawable.welcome);
		welcome();

		//		testBtn = (Button) findViewById(R.id.testBtn);
		//		testText = (EditText) findViewById(R.id.testText);
		//
		//		testBtn.setOnClickListener(new OnClickListener() {
		//
		//			public void onClick(View v) {
		//				String str = conn.test();
		//				testText.setText(str.substring(0, 20));
		//			}
		//		});
	}

	/**
	 * ��ӭ����,2���Ӻ��л�
	 * @param 
	 * @return
	 */
	public void welcome() {
		new Thread(new Runnable() {//�����߳�
			public void run() {//ʵ��Runnable��run���������߳���
				try {
					Thread.sleep(2000);//��ӭ������ͣ2����
					Message m = new Message();//����Message����
					logHandler.sendMessage(m);//����Ϣ�ŵ���Ϣ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();//�����߳�
	}

	//ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н��У����Ƚ��ȳ�
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			toLogin();
		}
	};

	public void toLogin() {		
		Intent it=new Intent();//ʵ����Intent
		it.setClass(Welcome.this, Login.class);//����Class
		//it.setClass(Welcome.this, MainActivity.class);//����Class
		startActivity(it);//����Activity
		Welcome.this.finish();//����Welcome Activity
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
