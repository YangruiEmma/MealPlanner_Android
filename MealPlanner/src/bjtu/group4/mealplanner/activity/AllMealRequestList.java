package bjtu.group4.mealplanner.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Meal;
import bjtu.group4.mealplanner.model.MealFriend;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.CustomAdapter;
import bjtu.group4.mealplanner.utils.SharedData;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AllMealRequestList extends Activity implements OnItemClickListener {
	private CustomAdapter lAdapter;
	private ListView mListView;
	private List<Map<String, Object>> mData;
	private ProgressDialog progress;
	private List<Meal> mealList;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_all_order);
		super.onCreate(savedInstanceState);

		bindViewAndSetData();
	}

	private void bindViewAndSetData() {
		mData = new ArrayList<Map<String, Object>>();
		mButton = (Button)findViewById(R.id.sure);
		mButton.setVisibility(View.INVISIBLE);
		mListView = (ListView)findViewById(R.id.OrderListView);
		mListView.setOnItemClickListener(this);

		GetMealRequestListTask task = new GetMealRequestListTask();
		task.execute();

		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("请稍等");
		progress.setMessage("努力加载中...");
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.show();
	}
	
	private class GetMealRequestListTask extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			int userId = SharedData.USERID;

			mealList = new ConnectServer().getMealRequestsAll(userId);
			if(mealList.size() != 0) {
				for(int i = 0; i < mealList.size(); ++i) {
					Meal meal = mealList.get(i);
					Date date = new Date(meal.getMealTime());
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(date);
					String statusString = "";
					if(meal.getMealFriendList().size() > 0) {
						MealFriend mf = meal.getMealFriendList().get(0);
						statusString = mf.getStatusString();
					}

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", meal.getMealId());
					map.put("title", meal.getRestName());
					map.put("info", dateString);
					map.put("more", statusString);

					mData.add(map);
				}
				return 1;
			}
			else {
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			//成功
			case 1:
				if(mData.size() > 0)
				{
					if(lAdapter == null) {
						lAdapter = new CustomAdapter(mData, AllMealRequestList.this);
						mListView.setAdapter(lAdapter);
					}
					else {
						lAdapter.updateData(mData);
						lAdapter.notifyDataSetChanged();
					}	
				}
				break;
				//失败
			case 0:
				Toast.makeText(AllMealRequestList.this, "您还没有被邀请过~", Toast.LENGTH_LONG).show();
				break;
			}
			super.onPostExecute(result);
		}


	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Log.d("AllOrderList", "onItemClick");
		if(position > mealList.size()) return;
		Meal meal = mealList.get(position);
		Intent intent = new Intent(this, InvitationDetailActivity.class);
		Bundle mBundle = new Bundle();  
		mBundle.putSerializable("invitation",meal);  
		intent.putExtras(mBundle);  
		startActivity(intent);
	}
}
