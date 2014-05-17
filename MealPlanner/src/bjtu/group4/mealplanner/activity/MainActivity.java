package bjtu.group4.mealplanner.activity;

import java.util.Calendar;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.PushMesgUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	MealApplication application;
	private String lineUpInfo = "";
	private boolean eatTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		application = (MealApplication) getApplication();
		setContentView(R.layout.activity_main);
		fragmentManager = getFragmentManager();  
		initViews();  

		if (!application.getIsLogin()) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Welcome.class);
			startActivity(intent);
			MainActivity.this.finish();
		}
		else if(!application.hasBind()) {
			// Push: 无账号初始化，用api key绑定
			Log.d("Meal", "before start work at " + Calendar.getInstance().getTimeInMillis());
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, 
					PushMesgUtils.getMetaValue(MainActivity.this, "api_key"));
			Log.d("Meal", "after start work at " + Calendar.getInstance().getTimeInMillis());

			setTabSelection(0);
		}
		else {

			if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("LineUp")) {
				eatTime = false;
				lineUpInfo = this.getIntent().getExtras().getString("lineUpInfo");
				setTabSelection(2);
			}else if (this.getIntent().getExtras() != null &&(this.getIntent().getExtras().getBoolean("FreeSeat")||
					this.getIntent().getExtras().getBoolean("HasQueue") || this.getIntent().getExtras().getBoolean("QueueFail"))) {
				eatTime = false;
				setTabSelection(2);
			}
			else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("EatComing")) {
				eatTime = false;
				lineUpInfo = this.getIntent().getExtras().getString("lineUpInfo");
				setTabSelection(2);
			}
			else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("EatTime")) {
				eatTime = true;
				lineUpInfo = this.getIntent().getExtras().getString("lineUpInfo");
				setTabSelection(2);
			}
			else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("Invitation")) {
				Intent intent = new Intent(MainActivity.this, AllMealRequestList.class);
				startActivity(intent);
			}
			else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("InvitFeedback")) {
				Intent intent = new Intent(MainActivity.this, AllMealList.class);
				startActivity(intent);
			}
			else if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("OrderConfirmed")) {
				Intent intent = new Intent(MainActivity.this, AllOrderList.class);
				startActivity(intent);
			}
			if (this.getIntent().getExtras() != null && this.getIntent().getExtras().getBoolean("RestAll")) {
				setTabSelection(1);
			}

			else 
				setTabSelection(0);  
		}


	}

	@Override
	public void onResume() {
		super.onResume();  
		
	}
	
	@Override
	public void onStart() {
		super.onResume();  
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getExtras() != null && intent.getExtras().getBoolean("bindInfo")) {
			bindPushMesgInfo task = new bindPushMesgInfo();
			task.execute(application.getUserId(),application.getBaiduUserId(),application.getPushMesgChannelId());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//super.onCreateOptionsMenu(menu); 
		return true;
	}
	/** 
	 * fragments 
	 */  
	private FragmentManager fragmentManager;  
	private MyInfoFragment userInfoFragment;  
	private PlanMealFragment planMealFragment;  
	private StartPageFragment startPageFragment;  
	private LineUpFragment lineUpFragment;  
	/** 
	 * Tab上的 view
	 */   
	private View userInfoLayout;
	private View planMealLayout;  
	private View startPageLayout;  
	private View lineUpLayout;

	/** 
	 * 在Tab布局上显示动态图标的控件 
	 */  
	private ImageView userInfoImg;  
	private ImageView planMealImg;  
	private ImageView startPageImg;   
	private ImageView lineUpImg;  

	/** 
	 * 在Tab布局上显示字体
	 */  
	private TextView userInfoText;  
	private TextView planMealText;  
	private TextView startPageText;   
	private TextView lineUpText;  

	private void initViews() {  
		startPageLayout = findViewById(R.id.startPage_layout);  
		planMealLayout = findViewById(R.id.planMeal_layout);  
		lineUpLayout = findViewById(R.id.lineUp_layout);  
		userInfoLayout = findViewById(R.id.myInfo_layout);  

		startPageImg = (ImageView) findViewById(R.id.startPage_image);  
		planMealImg = (ImageView) findViewById(R.id.planMeal_image);  
		lineUpImg = (ImageView) findViewById(R.id.lineUp_image);  
		userInfoImg = (ImageView) findViewById(R.id.myInfo_image);  

		userInfoText = (TextView) findViewById(R.id.myInfo_text);
		planMealText = (TextView) findViewById(R.id.planMeal_text);
		startPageText = (TextView) findViewById(R.id.startPage_text);
		lineUpText = (TextView) findViewById(R.id.lineUp_text);

		userInfoLayout.setOnClickListener(this);  
		planMealLayout.setOnClickListener(this);  
		startPageLayout.setOnClickListener(this);  
		lineUpLayout.setOnClickListener(this);  
	}  

	@Override  
	public void onClick(View v) {  
		switch (v.getId()) {  
		case R.id.startPage_layout:    
			setTabSelection(0);  
			break;  
		case R.id.planMeal_layout:  
			setTabSelection(1);  
			break;  
		case R.id.lineUp_layout:  
			setTabSelection(2);  
			break;  
		case R.id.myInfo_layout:   
			setTabSelection(3);  
			break;  
		default:  
			break;  
		}  
	}  

	/** 
	 * 根据传入的index参数来设置选中的tab页。 
	 *  
	 * @param index 
	 * 每个tab页对应的下标。0表示首页，1表示组饭局，2表示排排坐，3表示我的信息。 
	 */  
	private void setTabSelection(int index) {  
		clearSelection();  
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		hideFragments(transaction); 

		switch (index) {  
		case 0:  
			startPageImg.setImageResource(R.drawable.startpage_selected);  
			startPageText.setTextColor(Color.WHITE);  
			if (startPageFragment == null) {  
				startPageFragment = new StartPageFragment();  
				transaction.add(R.id.content, startPageFragment);  
			} else {  
				transaction.show(startPageFragment);  
			}  
			break;  
		case 1:  
			planMealImg.setImageResource(R.drawable.planmeal_selected);  
			planMealText.setTextColor(Color.WHITE);  
			if (planMealFragment == null) {  
				planMealFragment = new PlanMealFragment();  
				transaction.add(R.id.content, planMealFragment);  
			} else {  
				transaction.show(planMealFragment);  
			}  
			break;  
		case 2:  
			lineUpImg.setImageResource(R.drawable.lineup_selected);
			lineUpText.setTextColor(Color.WHITE);  
			if (lineUpFragment == null) {   
				lineUpFragment = new LineUpFragment();  
				transaction.add(R.id.content, lineUpFragment);  
			} else {  
				transaction.show(lineUpFragment);  
			}  
			break;  
		case 3:  
		default:  
			userInfoImg.setImageResource(R.drawable.myinfo_selected);  
			userInfoText.setTextColor(Color.WHITE);  
			if (userInfoFragment == null) {  
				userInfoFragment = new MyInfoFragment();  
				transaction.add(R.id.content, userInfoFragment);  
			} else {  
				transaction.show(userInfoFragment);  
			}  
			break;  
		}  
		transaction.commit();  
	}  

	private void clearSelection() {
		startPageImg.setImageResource(R.drawable.startpage_unselected);  
		planMealImg.setImageResource(R.drawable.planmeal_unselected);  
		lineUpImg.setImageResource(R.drawable.lineup_unselected);  
		userInfoImg.setImageResource(R.drawable.myinfo_unselected);  
	}  

	private void hideFragments(FragmentTransaction transaction) {  
		if (startPageFragment != null) {  
			transaction.hide(startPageFragment);  
		}  
		if (planMealFragment != null) {  
			transaction.hide(planMealFragment);  
		}  
		if (lineUpFragment != null) {  
			transaction.hide(lineUpFragment);  
		}  
		if (userInfoFragment != null) {  
			transaction.hide(userInfoFragment);  
		}  
	}

	public String getLineUpInfo() {
		return lineUpInfo;
	}

	public void setLineUpInfo(String lineUpInfo) {
		this.lineUpInfo = lineUpInfo;
	}

	public boolean isEatTime() {
		return eatTime;
	}

	public void setEatTime(boolean eatTime) {
		this.eatTime = eatTime;
	}

	@SuppressLint("UseValueOf")
	private class bindPushMesgInfo extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String userId = params[0].toString();
			String baiduUserId =  (String)params[1];
			String channelId = (String)params[2];

			boolean result = new ConnectServer().bindBaiduUser(userId, baiduUserId, channelId);

			if(result){
				return new Integer(0);
			}else{
				return new Integer(1);

			}
		}

		@SuppressLint("UseValueOf")
		@Override
		protected void onPostExecute(Integer result) {
			int response = result.intValue();
			switch(response){
			case 0:
				Toast.makeText(MainActivity.this, "向服务器发送推送消息ID成功", Toast.LENGTH_LONG).show();
				break;

			case 1:
				Toast.makeText(MainActivity.this, "向服务器发送推送消息ID失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
}
