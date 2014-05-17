package bjtu.group4.mealplanner.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Meal;
import bjtu.group4.mealplanner.model.MealFriend;
import bjtu.group4.mealplanner.utils.ConnectServer;
import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InvitationDetailActivity extends Activity{

	private TextView dateTextView;
	private TextView restInfoTextView;
	private TextView mealIdTextView;
	private TextView organizerTextView;
	private Button btnAccept;
	private Button btnRefuse;

	private Meal mMeal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_invite_friends);
		super.onCreate(savedInstanceState);
		mMeal = (Meal)getIntent().getSerializableExtra("invitation"); 
		bindViewAndSetData();
	}

	private void bindViewAndSetData() {
		dateTextView = (TextView)findViewById(R.id.textDate);
		restInfoTextView = (TextView)findViewById(R.id.textViewAddr);
		mealIdTextView = (TextView)findViewById(R.id.textMealId);
		organizerTextView = (TextView)findViewById(R.id.textOrganizer);
		btnAccept = (Button)findViewById(R.id.btnAccept);
		btnRefuse = (Button)findViewById(R.id.btnRefuse);

		mealIdTextView.setText("饭局ID：" + mMeal.getMealId() + "");
		restInfoTextView.setText(mMeal.getRestName());
		organizerTextView.setText(mMeal.getOrganizer());

		Date date = new Date(mMeal.getMealTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTextView.setText(formatter.format(date));
		
		if(mMeal.getMealFriendList().size() > 0) {
			MealFriend mf = mMeal.getMealFriendList().get(0);
			if(mf.getStatus() != MealFriend.WAITING) {
				btnAccept.setVisibility(View.INVISIBLE);
				btnRefuse.setVisibility(View.INVISIBLE);
			}
		}


	}

	//	private String getFriendsString() {
	//		String tempString = "";
	//		List<MealFriend> list = mMeal.getMealFriendList();
	//		for(int i = 0; i < list.size(); ++i) {
	//			MealFriend mf = list.get(i);
	//			tempString += mf.getmFriend().getFriendNameString() + "            "+ mf.getStatusString()+"\n";
	//		}
	//		return tempString;
	//	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRefuse:
			InvitationReplayTask iR =  new InvitationReplayTask();
			iR.execute(mMeal.getMealId(), false);
			break;
		case R.id.btnAccept:
			InvitationReplayTask iA =  new InvitationReplayTask();
			iA.execute(mMeal.getMealId(), true);
			break;
		default:
			break;
		}
	}

	private class InvitationReplayTask extends AsyncTask<Object, Integer, Integer>{

		@Override
		protected Integer doInBackground(Object... params) {
			Integer mealId = (Integer)params[0];
			Boolean isAccept = (Boolean)params[1];
			Boolean result = new ConnectServer().sendInvitationReply(mealId, isAccept);
			if(result){
				return 1;
			}else{
				return 0;			
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			int response = result.intValue();
			switch(response){
			case 1://成功
				Toast.makeText(InvitationDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();;

				Intent intent = new Intent(InvitationDetailActivity.this, AllMealRequestList.class);
				startActivity(intent);
				InvitationDetailActivity.this.finish();
				break;
			case 0://失败
				Toast.makeText(InvitationDetailActivity.this, "操作失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
}
