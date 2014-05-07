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
	
	private ListView restListView;
	private CustomAdapter restListAdapter;
	private List<Map<String, Object>> mData;
	private String TAG = PlanMealFragment.class.getName();
	private int startIndex = 0;
	private List<Restaurant> restList;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_planmeal,
				container, false);
		restListView = (ListView) messageLayout.findViewById(android.R.id.list);
		mData = new ArrayList<Map<String, Object>>();
		
		return messageLayout;
	}
	@Override
	public void onResume() {
	  //onResume happens after onStart and onActivityCreate
	  GetRestListTask task = new GetRestListTask();
	  task.execute(0, 10);
	  super.onResume() ; 
	}
	
	// **************��Ҫ���¿���*************
	public void AddDataToListView() {	
			startIndex += 10;
			GetRestListTask task = new GetRestListTask();
			task.execute(startIndex + 1 , startIndex + 10);
	}
	
	@Override  
    public void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
          
        Log.d(TAG, "onListItemClick");
        Toast.makeText(getActivity(), "You have selected " + position,
                Toast.LENGTH_SHORT).show();
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
			int response = result.intValue();
			switch(response){
				//�ɹ�
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
				//ʧ��
				case 0:
					Toast.makeText(getActivity(), "���緱æ", Toast.LENGTH_LONG).show();
					break;
			}
			super.onPostExecute(result);
		}
		
		
	}
	
	
	
}
