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
	 * 欢迎界面,2秒钟后切换
	 * @param 
	 * @return
	 */
	public void welcome() {
		new Thread(new Runnable() {//创建线程
			public void run() {//实现Runnable的run方法，即线程体
				try {
					Thread.sleep(2000);//欢迎界面暂停2秒钟
					Message m = new Message();//创建Message对象
					logHandler.sendMessage(m);//将消息放到消息队列中
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();//启动线程
	}

	//执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			toLogin();
		}
	};

	public void toLogin() {		
		Intent intent=new Intent();//实例化Intent
		intent.setClass(Welcome.this, Login.class);//设置Class
		startActivity(intent);//启动Activity
		Welcome.this.finish();//结束Welcome Activity
	}

}
