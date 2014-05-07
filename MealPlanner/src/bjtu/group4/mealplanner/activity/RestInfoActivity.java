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
		mRestaurant = (Restaurant)getIntent().getSerializableExtra("restInfo"); 
		Intent intent = getIntent();
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
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (mRestaurant != null) {
			restNameTextView.setText(mRestaurant.getName());
			restLocationTextView.setText(mRestaurant.getPosition());
			restLocationTextView.setText(mRestaurant.getPhoneNum());
		}
	}
	
	public void bindView() {
		restNameTextView = (TextView)findViewById(R.id.restName);
		restLocationTextView = (TextView)findViewById(R.id.restAddr);
		restPhoneTextView = (TextView)findViewById(R.id.restPhone);
	}
	
	public void onClick(View v) {
		if ( v.getId() == R.id.buttonPlanMeal ) {
			
		}
		
		else if ( v.getId() == R.id.buttonOrder ) {
			
		}
	}
	

}
