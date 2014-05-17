package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("HandlerLeak")
public class Welcome extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ImageView iv = (ImageView)this.findViewById(R.id.wpic);
		iv.setImageResource(R.drawable.welcome);
		welcome();
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
		Intent intent=new Intent();//ʵ����Intent
		intent.setClass(Welcome.this, Login.class);//����Class
		startActivity(intent);//����Activity
		Welcome.this.finish();//����Welcome Activity
	}

}
