package bjtu.group4.mealplanner.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Order;
import bjtu.group4.mealplanner.model.Restaurant;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AllOrderList extends Activity implements OnItemClickListener{
	private CustomAdapter lAdapter;
	private ListView mListView;
	private List<Map<String, Object>> mData;
	private ProgressDialog progress;
	private List<Order> orderList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_all_order);
		super.onCreate(savedInstanceState);
		
		bindViewAndSetData();
	}
	
	private void bindViewAndSetData() {
		mData = new ArrayList<Map<String, Object>>();
		mListView = (ListView)findViewById(R.id.OrderListView);
		mListView.setOnItemClickListener(this);
		
		GetOrderListTask task = new GetOrderListTask();
		task.execute();
		
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("«Î…‘µ»");
		progress.setMessage("≈¨¡¶º”‘ÿ÷–°£°£°£");
		progress.setIndeterminate(false);
		progress.setCancelable(true);
		progress.show();
	}
	
	public void onClick(View v) {
		
	}
	
	private class GetOrderListTask extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			int userId = SharedData.USERID;

			orderList = new ConnectServer().getOrdersAll(userId);
			if(orderList.size() != 0) {
				for(int i = 0; i < orderList.size(); ++i) {
					Order order = orderList.get(i);
					Date date = new Date(order.getMealTime());
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(date);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", order.getOrderId());
					map.put("title", order.getRestName());
					map.put("info", dateString);
					map.put("more", order.getStatusString());

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
			//≥…π¶
			case 1:
				if(mData.size() > 0)
				{
					if(lAdapter == null) {
						lAdapter = new CustomAdapter(mData, AllOrderList.this);
						mListView.setAdapter(lAdapter);
					}
					else {
						lAdapter.updateData(mData);
						lAdapter.notifyDataSetChanged();
					}	
				}
				break;
				// ß∞‹
			case 0:
				Toast.makeText(AllOrderList.this, "Õ¯¬Á∑±√¶£¨«Î…‘∫Û‘Ÿ ‘", Toast.LENGTH_LONG).show();
				break;
			}
			super.onPostExecute(result);
		}


	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Log.d("AllOrderList", "onItemClick");
		if(position > orderList.size()) return;
		Order order = orderList.get(position);
		Intent intent = new Intent(this, OrderDetailActivity.class);
		Bundle mBundle = new Bundle();  
		mBundle.putSerializable("order",order);  
		intent.putExtras(mBundle);  
		startActivity(intent);
	}
}
