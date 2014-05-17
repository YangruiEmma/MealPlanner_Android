package bjtu.group4.mealplanner.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Meal;
import bjtu.group4.mealplanner.model.MealFriend;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.ConnectServer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MealDetailActivity extends Activity{
	private TextView dateTextView;
	private TextView restInfoTextView;
	private TextView mealIdTextView;
	private TextView friendsTextView;
	private Button goOrderButton;
	
	private Meal mMeal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_meal_detail);
		super.onCreate(savedInstanceState);
		mMeal = (Meal)getIntent().getSerializableExtra("meal"); 
		bindViewAndSetData();
	}
	
	private void bindViewAndSetData() {
		dateTextView = (TextView)findViewById(R.id.textDate);
		restInfoTextView = (TextView)findViewById(R.id.textViewAddr);
		mealIdTextView = (TextView)findViewById(R.id.textMealId);
		friendsTextView = (TextView)findViewById(R.id.textViewFriends);
		goOrderButton = (Button)findViewById(R.id.btnGoOrder);
		
		mealIdTextView.setText("饭局ID：" + mMeal.getMealId() + "");
		restInfoTextView.setText(mMeal.getRestName());
		friendsTextView.setText(getFriendsString());
		
		Date date = new Date(mMeal.getMealTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTextView.setText(formatter.format(date));
		if(mMeal.getMealState() == Meal.ORDERED) {
			goOrderButton.setVisibility(View.INVISIBLE);
		}
		
		
	}
	
	private String getFriendsString() {
		String tempString = "";
		List<MealFriend> list = mMeal.getMealFriendList();
		for(int i = 0; i < list.size(); ++i) {
			MealFriend mf = list.get(i);
			tempString += mf.getmFriend().getFriendNameString() + 
					"                            "+ 
					mf.getStatusString()+"\n";
		}
		return tempString;
	}
	
	public void onClick(View v) {
		GetRestInfoAsyncTask task = new GetRestInfoAsyncTask();
		task.execute(mMeal.getRestId());
	}
	
	private class GetRestInfoAsyncTask extends AsyncTask<Integer, Void, Restaurant>{
		@Override
		protected Restaurant doInBackground(Integer... arg0) {
			int restId = arg0[0];
			try {
				Restaurant rest = new ConnectServer().getRestaurantDetailById(restId);
				
				return rest;
			} catch (Exception e) {
				Log.d("GetRestInfoAsyncTask ",e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Restaurant rest) {
			super.onPostExecute(rest);
			if(rest != null) {
				Intent intent = new Intent(MealDetailActivity.this, OrderActivity.class);
				Bundle mBundle = new Bundle();  
				mBundle.putSerializable("restInfo",rest);  
				mBundle.putSerializable("mealInfo",mMeal);  
				intent.putExtras(mBundle);  
				startActivity(intent);
			}
			else {
				Toast.makeText(MealDetailActivity.this, "获取餐厅详情失败 +_+ ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
}
