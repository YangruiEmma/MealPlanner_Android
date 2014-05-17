/**
 * Fragment UI to show userInformation
 */
package bjtu.group4.mealplanner.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.utils.SharedData;

/**
 * @author lcmm
 *
 */
public class MyInfoFragment extends Fragment implements OnClickListener {

	private TextView myNameTextView;
	private TextView myEmailTextView;
	private TextView myPhoneTextView;
	
	private TextView myFriendTextView;
	private TextView myInvitationTextView;
	private TextView myOrderTextView;
	private TextView myMealsTextView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_myinfo,
				container, false);
		bindView(messageLayout);
		return messageLayout;
	}

	private void bindView(View v) {
		myFriendTextView = (TextView)v.findViewById(R.id.myFriendTextView);
		myFriendTextView.setOnClickListener(this);
		myInvitationTextView = (TextView)v.findViewById(R.id.myInvitationTextView);
		myInvitationTextView.setOnClickListener(this);
		myOrderTextView = (TextView)v.findViewById(R.id.myOrderTextView);
		myOrderTextView.setOnClickListener(this);
		myMealsTextView = (TextView)v.findViewById(R.id.MyMealsTextView);
		myMealsTextView.setOnClickListener(this);
		
		myNameTextView = (TextView)v.findViewById(R.id.userName);
		myEmailTextView = (TextView)v.findViewById(R.id.email);
		myPhoneTextView = (TextView)v.findViewById(R.id.phoneNum);
		myNameTextView.setText(SharedData.USERNAME);
		myEmailTextView.setText(SharedData.EMAIL);
		myPhoneTextView.setText(SharedData.PHONE);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.myFriendTextView:
			intent = new Intent(getActivity(), AllFriendsList.class);
			startActivity(intent);
			break;
		case R.id.myInvitationTextView:
			intent = new Intent(getActivity(), AllMealRequestList.class);
			startActivity(intent);
			break;
		case R.id.myOrderTextView:
			intent = new Intent(getActivity(), AllOrderList.class);
			startActivity(intent);
			break;
		case R.id.MyMealsTextView:
			intent = new Intent(getActivity(), AllMealList.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
