package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Restaurant;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RestInfoActivity extends Activity {
	Restaurant mRestaurant;
	
	TextView restNameTextView;
	TextView restLocationTextView;
	TextView restPhoneTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_restinfo);
		mRestaurant = (Restaurant)getIntent().getSerializableExtra("restInfo"); 
		bindView();
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mRestaurant != null) {
			restNameTextView.setText(mRestaurant.getName());
			restLocationTextView.setText(mRestaurant.getPosition());
			restPhoneTextView.setText(mRestaurant.getPhoneNum());
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	private void bindView() {
		restNameTextView = (TextView)findViewById(R.id.restName);
		restLocationTextView = (TextView)findViewById(R.id.restAddr);
		restPhoneTextView = (TextView)findViewById(R.id.restPhone);
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
			
		}
	}
	

}
