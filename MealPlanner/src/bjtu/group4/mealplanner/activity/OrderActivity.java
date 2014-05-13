package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;

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
	private ArrayList<Integer> mDishIds = new ArrayList<Integer>();
	private ArrayList<String> mDishNames = new ArrayList<String>();
	
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
	
	public ArrayList<Integer> getDishIds() {
		return mDishIds;
	}
	
	public ArrayList<String> getDishNames() {
		return mDishNames;
	}

}
