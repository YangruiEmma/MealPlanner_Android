package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Friend;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.CustomAdapter;
import bjtu.group4.mealplanner.utils.SharedData;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseFriendFragment extends ListFragment {
	
	private List<Friend> friendsList;
	private CustomAdapter lAdapter;
	private List<Map<String, Object>> mData;
	private String TAG = ChooseFriendFragment.class.getName();
	private Button sureButton;
	private PlanMealActivity fatherActivity;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View chooseFriendLayout = inflater.inflate(R.layout.fragment_choosefriend,
				container, false);
		mData = new ArrayList<Map<String, Object>>();
		sureButton = (Button)chooseFriendLayout.findViewById(R.id.sure);
		fatherActivity = (PlanMealActivity)getActivity();
		
		GetFriendsListTask task = new GetFriendsListTask();
		task.execute();
		return chooseFriendLayout;
	}
	
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i = 0; i < mData.size(); ++i) {
					Map<String, Object> m = mData.get(i);
					if((Integer)m.get("status")  == 1) {
						Integer friendId = (Integer) m.get("id");
						String nameString = (String)m.get("title");
						fatherActivity.getFriends().add(friendId);
						fatherActivity.getFriendsName().add(nameString);
					}
				}
				
				if(fatherActivity.getFriends().size() == 0) {
					Toast.makeText(getActivity(), "请至少选择一位好友", Toast.LENGTH_LONG).show();
					return;
				}
				fatherActivity.replaceContent();
			}
		});
    }  


	@Override  
	public void onListItemClick(ListView l, View v, int position, long id) {  
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "onListItemClick");
		
		if(mData.size() < position + 1 ) return;
		
		if((Integer)mData.get(position).get("status")  == -1) {
			v.setBackgroundColor(Color.GRAY);
			mData.get(position).put("status", 1);
		}
		else {
			v.setBackgroundColor(Color.WHITE);
			mData.get(position).put("status", -1);
		}
	}
	
	private class GetFriendsListTask extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			
			int userId = SharedData.USERID;
			friendsList = new ConnectServer().getFriendsAll(userId);

			if(friendsList.size() != 0) {
				for(int i = 0; i < friendsList.size(); ++i) {
					Friend f = friendsList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", f.getFriendId());
					map.put("title", f.getFriendNameString());
					map.put("info", f.getFriendPhone());
					map.put("more", f.getFriendEmail());
					map.put("status", -1);

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
			int response = result.intValue();
			switch(response){
			//成功
			case 1:
				if(mData.size() > 0)
				{
					if(lAdapter == null) {
						lAdapter = new CustomAdapter(mData, getActivity());
						setListAdapter(lAdapter);
					}
					else {
						lAdapter.updateData(mData);
						lAdapter.notifyDataSetChanged();
					}	
				}
				break;
			//失败
			case 0:
				Toast.makeText(getActivity(), "您没有好友，无法组饭局", Toast.LENGTH_LONG).show();
				break;
			}
			super.onPostExecute(result);
		}
	}// AnsyTask Class End

}
