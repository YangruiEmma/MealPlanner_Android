package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.List;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Friend;
import android.R.integer;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PlanMealActivity extends Activity {

	private FragmentManager fragmentManager;  
	private ChooseFriendFragment chooseFriendFragment;
	private InvitationInfoFragment invitationInfoFragment;
	private final static int contentID = R.id.planMealContent;
	private int mRestId;
	private String mRestName;
	private String mRestAddr;
	private ArrayList<Integer> friendIds = new ArrayList<Integer>(); 
	private ArrayList<String> friendNames = new ArrayList<String>(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planmeal);
		
		fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		chooseFriendFragment = new ChooseFriendFragment();
		transaction.replace(contentID, chooseFriendFragment); 
		transaction.commit();
		
		mRestId = getIntent().getIntExtra("restId",0);
		mRestName = getIntent().getStringExtra("restName");
		mRestAddr = getIntent().getStringExtra("restAddr");
		
	}
	
	public void replaceContent() {
		invitationInfoFragment = new InvitationInfoFragment();
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		transaction.replace(contentID, invitationInfoFragment); 
		transaction.commit();
	}

	public ArrayList<Integer> getFriends() {
		return friendIds;
	}
	
	public ArrayList<String> getFriendsName() {
		return friendNames;
	}

	public int getmRestId() {
		return mRestId;
	}

	public String getmRestName() {
		return mRestName;
	}

	public String getmRestAddr() {
		return mRestAddr;
	}
	
	
	


}
