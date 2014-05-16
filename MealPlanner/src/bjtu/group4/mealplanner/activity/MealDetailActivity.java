package bjtu.group4.mealplanner.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Meal;
import bjtu.group4.mealplanner.model.MealFriend;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MealDetailActivity extends Activity{
	private TextView dateTextView;
	private TextView restInfoTextView;
	private TextView mealIdTextView;
	private TextView friendsTextView;
	
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
		
		mealIdTextView.setText("·¹¾ÖID£º" + mMeal.getMealId() + "");
		restInfoTextView.setText(mMeal.getRestName());
		friendsTextView.setText(getFriendsString());
		
		Date date = new Date(mMeal.getMealTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTextView.setText(formatter.format(date));
		
		
	}
	
	private String getFriendsString() {
		String tempString = "";
		List<MealFriend> list = mMeal.getMealFriendList();
		for(int i = 0; i < list.size(); ++i) {
			MealFriend mf = list.get(i);
			tempString += mf.getmFriend().getFriendNameString() + "            "+ mf.getStatusString()+"\n";
		}
		return tempString;
	}
}
