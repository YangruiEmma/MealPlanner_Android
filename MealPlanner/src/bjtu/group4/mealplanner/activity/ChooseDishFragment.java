package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Friend;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.CustomAdapter;
import bjtu.group4.mealplanner.utils.SharedData;
import android.R.anim;
import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseDishFragment extends ListFragment {
	private List<Food> foodsList;
	private CustomAdapter lAdapter;
	private List<Map<String, Object>> mData;
	private String TAG = ChooseDishFragment.class.getName();
	private Button sureButton;
	private TextView titleTextView;
	private OrderActivity fatherActivity;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View chooseFriendLayout = inflater.inflate(R.layout.fragment_choosefriend,
				container, false);
		mData = new ArrayList<Map<String, Object>>();
		bindView(chooseFriendLayout);
		getData();
		lAdapter = new CustomAdapter(mData, getActivity());
		setListAdapter(lAdapter);

		return chooseFriendLayout;
	}

	private void bindView(View v) {
		sureButton = (Button)v.findViewById(R.id.sure);
		titleTextView = (TextView)v.findViewById(R.id.title);
		fatherActivity = (OrderActivity)getActivity();
		titleTextView.setText("选择菜品");

		sureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i = 0; i < mData.size(); ++i) {
					Map<String, Object> m = mData.get(i);
					if((Integer)m.get("status")  == 1) {
						fatherActivity.getDishIds().add((Integer) m.get("id"));
						fatherActivity.getDishNames().add((String) m.get("title"));
					}
				}
				if(fatherActivity.getDishIds().size() == 0) {
					Builder dlg = new AlertDialog.Builder(getActivity());   
					dlg.setTitle("提示");
					dlg.setMessage("确定不选择菜品了嘛？"); 
					dlg.setPositiveButton("是滴", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							fatherActivity.replaceContent();
						}

					});  
					dlg.setNegativeButton("我要选", null); 
					dlg.show();  
					return;
				}
				fatherActivity.replaceContent();
			}
		});
	}

	private void getData() {
		foodsList = fatherActivity.getRestaurant().getDishes();
		for(int i = 0; i < foodsList.size(); ++i) {
			Food f = foodsList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", f.getFoodId());
			map.put("title", f.getFoodName());
			map.put("info", f.getFoodPrice()+"");
			map.put("more", f.getFoodTypeName());
			map.put("status", -1);
			if(f.getIsHot() == 1) {
				map.put("imgStatus", R.drawable.hot);
			}
			mData.add(map);
		}
	}

	@Override  
	public void onListItemClick(ListView l, View v, int position, long id) {  
		super.onListItemClick(l, v, position, id);

		if(mData.size() < position + 1 ) return;

		if((Integer)mData.get(position).get("status")  == -1) {
			//v.setBackgroundColor(0xDC143C);
			v.setBackgroundColor(Color.GRAY);
			TextView numTextView = (TextView)v.findViewById(R.id.itemNum);
			numTextView.setText("1份");
			mData.get(position).put("status", 1);
		}
		else {
			v.setBackgroundColor(android.R.attr.colorBackground);
			TextView numTextView = (TextView)v.findViewById(R.id.itemNum);
			numTextView.setText("");
			mData.get(position).put("status", -1);
		}
	}
	
	
}
