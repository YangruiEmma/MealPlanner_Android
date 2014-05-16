package bjtu.group4.mealplanner.activity;

import java.io.IOException;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.QueueInfo;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.ConnectServer;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseValueOf")
public class Beam extends Activity {

	private NfcAdapter nfcAdapter;  
	private ProgressDialog progress;

	private TextView promt; 
	private EditText personNum;
	private LinearLayout lineLayout1;
	private LinearLayout lineLayout2;
	private Button btnLine;
	private Button btnHome;

	private String restaurantID = "";
	MealApplication application;
	Restaurant rest;
	QueueInfo queueInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);
		application = (MealApplication) getApplication();
		
		bindViews();
		// 获取默认的NFC控制器  
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);  
		if (nfcAdapter == null) {  
			promt.setText("设备不支持NFC！");  
			return;  
		}  
		if (!nfcAdapter.isEnabled()) {
			promt.setText("请在系统设置中先启用NFC功能！");  
			return;  
		}  

		setListener();
	}



	private void bindViews() {
		promt = (TextView) findViewById(R.id.promt); 
		personNum = (EditText) findViewById(R.id.personNum); 
		lineLayout1 = (LinearLayout)findViewById(R.id.nfcLineLayout1); 
		lineLayout2 = (LinearLayout)findViewById(R.id.nfcLineLayout2); 
		lineLayout1.setVisibility(View.INVISIBLE);
		lineLayout2.setVisibility(View.INVISIBLE);
		btnLine = (Button)findViewById(R.id.nfcBtnLine);
		btnHome = (Button)findViewById(R.id.nfcBtnHome);
	}

	private void setListener() {
		btnLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String numString = personNum.getText().toString();
				if ("".equals(numString)) {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("请先输入用餐人数哦，亲！").create()
					.show();
				} else if (Integer.parseInt(numString) > 10) {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("提示")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("抱歉，用餐人数过多，请在服务台预定！").create()
					.show();
				} else {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("确认排队")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("是否确认现在排队用餐?").setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 准备发送排队服务
							sendQueueMesg();
						}
					}).setNegativeButton("否", null).create()
					.show();
				}
			}
		});

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Beam.this, MainActivity.class);
				startActivity(intent);
				Beam.this.finish();
			}
		});
	}

	@Override  
	protected void onResume() {  
		super.onResume();
		//to see if user login
		if (application != null && !application.getIsLogin()) {
			promt.setText("未登录，请先登录！");  
			waitToLogin();
		}//得到是否检测到ACTION_TECH_DISCOVERED触发  
		else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			//处理该intent  
			//writeTag(getIntent());
			readTag(getIntent());  
		}  
	}  
	/** 
	 * Parses the NDEF Message from the intent and prints to the TextView 
	 */  
	private void readTag(Intent intent) {  
		//取出封装在intent中的TAG  
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		boolean auth = false;  

		//读取TAG  
		MifareClassic mfc = MifareClassic.get(tagFromIntent);  
		try {  

			int sectorIndex = 0;
			int blockIndex = 1; 
			//Enable I/O operations to the tag from this TagTechnology object.  
			mfc.connect();  
			//Authenticate a sector with key A.  
			auth = mfc.authenticateSectorWithKeyA(sectorIndex,  
					MifareClassic.KEY_DEFAULT);  
			if (auth) {  
				// 读取扇区中的块  餐厅ID存储在Sector 0 的Block 1 里
				byte[] data = mfc.readBlock(blockIndex);  
				restaurantID = (new String(data)).trim();
				//promt.setText("获取信息成功！");
				getRestInfo();
			} else {  
				promt.setText("获取信息失败！");
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  


	private void getRestInfo() {
		RestInfoTask task = new RestInfoTask();
		progress = new ProgressDialog(Beam.this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("请稍等");
		progress.setMessage("正在获取餐厅信息");
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		progress.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回键取消
		progress.setCancelable(true);
		progress.show();

		//异步获取餐厅信息
		task.execute(restaurantID);
	}


	private class RestInfoTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String restID = (String)params[0];

			rest = new ConnectServer().getRestInfo(restID);

			if(rest!=null){
				return new Integer(1);
			}else{
				return new Integer(0);

			}
		}

		@SuppressLint("UseValueOf")
		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			case 1:
				promt.setText("欢迎到"+rest.getName()+"餐厅用餐");
				lineLayout1.setVisibility(View.VISIBLE);
				lineLayout2.setVisibility(View.VISIBLE);
				//					line_txt.setTextColor(android.graphics.Color.BLACK);
				//					person_txt.setTextColor(android.graphics.Color.BLACK);
				//					personNum.setFilters(new InputFilter[] {  
				//				            new InputFilter() {  
				//								@Override
				//								public CharSequence filter(CharSequence source,
				//										int start, int end, Spanned dest,
				//										int dstart, int dend) {
				//									return null;
				//								}  
				//				            }  
				//				        });  
				break;

			case 0:
				Toast.makeText(Beam.this, "获取服务器餐厅信息失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	/**
	 * 向服务器发送排队消息
	 * 
	 */
	private void sendQueueMesg() {
		SendQueueMesgTask task = new SendQueueMesgTask();
		progress = new ProgressDialog(Beam.this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("请稍等");
		progress.setMessage("正在发送排队消息");
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		progress.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回键取消
		progress.setCancelable(true);
		progress.show();

		//异步获取餐厅信息
		task.execute(restaurantID, application.getUserId(), personNum.getText().toString());//restId=3&userId=1&peopleNum=6
	}

	private class SendQueueMesgTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String restID = (String)params[0];
			String userID =  params[1].toString();
			String peopleNum = (String)params[2];

			queueInfo = new ConnectServer().sendQueueMesg(restID,userID,peopleNum);

			if(queueInfo!=null){
				return new Integer(1);
			}else{
				return new Integer(0);

			}
		}

		@SuppressLint("UseValueOf")
		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			case 1:
				Toast.makeText(Beam.this, "您已经开始排队啦", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(Beam.this, MainActivity.class);
				intent.putExtra("LineUp", true); 
				startActivity(intent);
				Beam.this.finish();
				break;

			case 0:
				Toast.makeText(Beam.this, "排队失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@SuppressWarnings("unused")
	private void writeTag(Intent intent) {
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); 
		MifareClassic mfc = MifareClassic.get(tagFromIntent);  

		try {
			mfc.connect();
			boolean auth = false;  
			short sectorAddress = 0;  
			auth = mfc.authenticateSectorWithKeyA(sectorAddress,  
					MifareClassic.KEY_DEFAULT);  
			if (auth) {  
				// the last block of the sector is used for KeyA and KeyB cannot be overwritted  
				mfc.writeBlock(1, "               1".getBytes("GBK"));
				mfc.writeBlock(2, "  RestaurantName".getBytes());  
				//Sector 1
				//				mfc.writeBlock(4, "MealPlannerGroup".getBytes());  
				//				mfc.writeBlock(5, "MealPlannerGroup".getBytes());  
				mfc.close();  
				Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
				promt.setText("写入成功");  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				mfc.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
	}

	public void waitToLogin() {
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

	void toLogin() {		
		Intent it=new Intent();//实例化Intent
		it.setClass(Beam.this, Login.class);//设置Class
		startActivity(it);//启动Activity
		Beam.this.finish();//结束Welcome Activity
	}

	//执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			toLogin();
		}
	};

}
