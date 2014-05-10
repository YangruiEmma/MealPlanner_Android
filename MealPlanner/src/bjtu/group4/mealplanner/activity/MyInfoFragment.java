/**
 * Fragment UI to show userInformation
 */
package bjtu.group4.mealplanner.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import bjtu.group4.mealplanner.R;

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
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.myFriendTextView:
			Log.d("MyInfoFragment","onClickmyFriendTextView");
			break;
		case R.id.myInvitationTextView:
			Log.d("MyInfoFragment","onClickmyInvitationTextView");
			break;
		case R.id.myOrderTextView:
			Log.d("MyInfoFragment","onClickmyOrderTextView");
			break;
		default:
			break;
		}
	}
}
