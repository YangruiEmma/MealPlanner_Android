package bjtu.group4.mealplanner.utils;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import bjtu.group4.mealplanner.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	
	private List<Map<String, Object>> mData;
	private LayoutInflater mInflater;
	
	public CustomAdapter(List<Map<String, Object>> data, Context context) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.mData = data;
	}
	
	public void updateData(List<Map<String, Object>> data) {
		this.mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int location) {
		Object obj = mData.get(location);
		return obj;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {                    
			holder=new ViewHolder();                      
			convertView = mInflater.inflate(R.layout.list_view_item, null);
			holder.img = (ImageView)convertView.findViewById(R.id.itemImg);
			holder.title = (TextView)convertView.findViewById(R.id.itemTitle);
			holder.info = (TextView)convertView.findViewById(R.id.itemInfo);
			holder.more = (TextView)convertView.findViewById(R.id.itemMore);
			holder.imgStatus = (ImageView)convertView.findViewById(R.id.itemStatusImg);
			
			convertView.setTag(holder);
		}else {
		      holder = (ViewHolder)convertView.getTag();
		}
		
		holder.title.setText((String)mData.get(position).get("title"));
		holder.info.setText((String)mData.get(position).get("info"));
		holder.more.setText((String)mData.get(position).get("more"));
		if(mData.get(position).get("img") != null) {
			holder.img.setImageResource((Integer)mData.get(position).get("img"));
		}
		if(mData.get(position).get("imgStatus") != null) {
			holder.imgStatus.setVisibility(View.VISIBLE);
			holder.imgStatus.setBackgroundResource((Integer)mData.get(position).get("imgStatus"));
		}
			
		return convertView;
	}

	public final class ViewHolder{
		public ImageView img;
		public TextView title;
		public TextView info;
		public TextView more;
		public ImageView imgStatus;
    }
}
