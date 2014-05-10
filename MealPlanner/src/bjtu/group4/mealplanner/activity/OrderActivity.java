package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Restaurant;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class OrderActivity extends Activity {
	private FragmentManager fragmentManager;  
	private ChooseDishFragment chooseDishFragment;
	private OrderInfoFragment orderInfoFragment;
	private final static int contentID = R.id.planMealContent;
	private Restaurant mRestaurant;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planmeal);
		mRestaurant = (Restaurant)getIntent().getSerializableExtra("restInfo"); 
		
		fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		chooseDishFragment = new ChooseDishFragment();
		transaction.replace(contentID, chooseDishFragment); 
		transaction.commit();
	}
	
	public void replaceContent() {
		orderInfoFragment = new OrderInfoFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		transaction.replace(contentID, orderInfoFragment); 
		transaction.commit();
	}
	
	public Restaurant getRestaurant() {
		return mRestaurant;
	}

}
