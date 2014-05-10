package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.CustomAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class RestInfoActivity extends Activity {
	private Restaurant mRestaurant;
	
	private TextView restNameTextView;
	private TextView restLocationTextView;
	private TextView restPhoneTextView;
	private ListView mListView;
	private CustomAdapter lAdapter;
	private List<Map<String, Object>> mData;
	
	private String TAG = RestInfoActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_restinfo);
		mRestaurant = (Restaurant)getIntent().getSerializableExtra("restInfo"); 
		mData = new ArrayList<Map<String, Object>>();
		bindView();
		super.onCreate(savedInstanceState);
		
		setData();
		lAdapter = new CustomAdapter(mData, this);
		mListView.setAdapter(lAdapter);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
	}

	private void setData() {
		if (mRestaurant != null) {
			restNameTextView.setText(mRestaurant.getName());
			restLocationTextView.setText(mRestaurant.getPosition());
			restPhoneTextView.setText(mRestaurant.getPhoneNum());
			
			List<Food> foodList = mRestaurant.getDishes();
			if(foodList != null) {
				for(int i = 0; i < foodList.size(); ++i) {
					Food f = foodList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", f.getFoodId());
					map.put("title", f.getFoodName());
					map.put("info", f.getFoodPrice()+"");

					mData.add(map);
				}
			}
		}
	}
	
	private void bindView() {
		restNameTextView = (TextView)findViewById(R.id.restName);
		restLocationTextView = (TextView)findViewById(R.id.restAddr);
		restPhoneTextView = (TextView)findViewById(R.id.restPhone);
		mListView = (ListView)findViewById(R.id.restInfoListView);
	}
	
	public void onClick(View v) {
		if ( v.getId() == R.id.buttonPlanMeal ) {
			Intent intent = new Intent(RestInfoActivity.this, PlanMealActivity.class);
			intent.putExtra("restId", mRestaurant.getId());  
			intent.putExtra("restName", mRestaurant.getName());
			intent.putExtra("restAddr", mRestaurant.getPosition());
			startActivity(intent);
		}
		
		else if ( v.getId() == R.id.buttonOrder ) {
			Intent intent = new Intent(RestInfoActivity.this, OrderActivity.class);
			Bundle mBundle = new Bundle();  
			mBundle.putSerializable("restInfo",mRestaurant);  
			intent.putExtras(mBundle);  
			startActivity(intent);
		}
	}
	

}
