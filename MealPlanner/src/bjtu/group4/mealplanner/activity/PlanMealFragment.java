/**
 * 
 */
package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.anim;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.R.id;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.CustomAdapter;

/**
 * @author lcmm
 *
 */
public class PlanMealFragment extends ListFragment {

	private ProgressDialog progress;
	private CustomAdapter restListAdapter;
	private List<Map<String, Object>> mData;
	private String TAG = PlanMealFragment.class.getName();
	private int startIndex = 0;
	private List<Restaurant> restList;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View messageLayout = inflater.inflate(R.layout.fragment_planmeal,
				container, false);
		bindViewAndGetData();
		return messageLayout;
	}

	private void bindViewAndGetData() {
		mData = new ArrayList<Map<String, Object>>();
		GetRestListTask task = new GetRestListTask();
		task.execute(0, 10);
		progress = new ProgressDialog(getActivity());
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("请稍等");
		progress.setMessage("努力加载中。。。");
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		progress.setIndeterminate(false);
		// 设置ProgressDialog 是否可以按退回键取消
		progress.setCancelable(true);
		progress.show();
	}
	
	// **************需要重新考虑*************
	public void AddDataToListView() {	
		startIndex += 10;
		GetRestListTask task = new GetRestListTask();
		task.execute(startIndex + 1 , startIndex + 10);
	}

	@Override  
	public void onListItemClick(ListView l, View v, int position, long id) {  
		super.onListItemClick(l, v, position, id);  

		Restaurant restaurant = restList.get(position);
		Intent intent = new Intent(getActivity(), RestInfoActivity.class);
		Bundle mBundle = new Bundle();  
		mBundle.putSerializable("restInfo",restaurant);  
		intent.putExtras(mBundle);  
		startActivity(intent);

	}  


	private class GetRestListTask extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			int start = (Integer)params[0];
			int end = (Integer)params[1];

			restList = new ConnectServer().getRestaurantsAll(start, end);

			if(restList.size() != 0) {
				for(int i = 0; i < restList.size(); ++i) {
					Restaurant rest = restList.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", rest.getId());
					map.put("title", rest.getName());
					map.put("info", rest.getPosition());

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
					if(restListAdapter == null) {
						restListAdapter = new CustomAdapter(mData, getActivity());
						setListAdapter(restListAdapter);
					}
					else {
						restListAdapter.updateData(mData);
						restListAdapter.notifyDataSetChanged();
					}	
				}
				break;
				//失败
			case 0:
				Toast.makeText(getActivity(), "网络繁忙", Toast.LENGTH_LONG).show();
				break;
			}
			super.onPostExecute(result);
		}


	}



}
