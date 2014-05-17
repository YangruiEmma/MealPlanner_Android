package bjtu.group4.mealplanner.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Friend;
import bjtu.group4.mealplanner.model.Meal;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AllFriendsList extends Activity implements OnItemClickListener{
	private CustomAdapter lAdapter;
	private ListView mListView;
	private List<Map<String, Object>> mData;
	private ProgressDialog progress;
	private List<Friend> friendList;
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

		GetFriendListTask task = new GetFriendListTask();
		task.execute();

		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("请稍等");
		progress.setMessage("努力加载中...");
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.show();
	}

	public void onClick(View v) {
		
	}

	private class GetFriendListTask extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			int userId = SharedData.USERID;

			friendList = new ConnectServer().getFriendsAll(userId);
			if(friendList.size() != 0) {
				for(int i = 0; i < friendList.size(); ++i) {
					Friend friend = friendList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", friend.getFriendId());
					map.put("title", friend.getFriendNameString());
					map.put("info", friend.getFriendPhone());
					map.put("more", "");

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
						lAdapter = new CustomAdapter(mData, AllFriendsList.this);
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
				Toast.makeText(AllFriendsList.this, "目前没有好友耶~快去添加好友吧", Toast.LENGTH_LONG).show();
				break;
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
//		Log.d("AllOrderList", "onItemClick");
//		if(position > friendList.size()) return;
//		Friend meal = friendList.get(position);
//		Intent intent = new Intent(this, MealDetailActivity.class);
//		Bundle mBundle = new Bundle();  
//		mBundle.putSerializable("meal",meal);  
//		intent.putExtras(mBundle);  
//		startActivity(intent);
	}
}
