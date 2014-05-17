package bjtu.group4.mealplanner.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bjtu.group4.mealplanner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import bjtu.group4.mealplanner.model.Food;
import bjtu.group4.mealplanner.model.Order;
import bjtu.group4.mealplanner.utils.ConnectServer;

public class OrderDetailActivity extends Activity {

	private TextView dateTextView;
	private TextView restInfoTextView;
	private TextView phonetTextView;
	private TextView numTextView;
	private TextView orderIdTextView;
	private TextView dishesTextView;
	private Button CancelButton;
	
	private Order mOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.fragment_detail);
		super.onCreate(savedInstanceState);
		mOrder = (Order)getIntent().getSerializableExtra("order"); 
		bindViewAndSetData();
	}
	
	private void bindViewAndSetData() {
		dateTextView = (TextView)findViewById(R.id.textDate);
		restInfoTextView = (TextView)findViewById(R.id.textViewAddr);
		phonetTextView = (TextView)findViewById(R.id.textOrganizer);
		numTextView = (TextView)findViewById(R.id.textNum);
		orderIdTextView = (TextView)findViewById(R.id.textOrderId);
		dishesTextView = (TextView)findViewById(R.id.textViewDishes);
		CancelButton = (Button)findViewById(R.id.btnCancle);
		
		orderIdTextView.setText("订单ID：" + mOrder.getOrderId() + "");
		numTextView.setText(mOrder.getPeopleNum() +"");
		restInfoTextView.setText(mOrder.getRestName());
		phonetTextView.setText(mOrder.getPhoneNum());
		dishesTextView.setText(getDishString());
		
		Date date = new Date(mOrder.getMealTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTextView.setText(formatter.format(date));
		
		if(mOrder.getOrderState() != Order.SUCCESSE) {
			CancelButton.setVisibility(View.INVISIBLE);
		}
		
	}
	
	private String getDishString() {
		String tempString = "";
		List<Food> list = mOrder.getDishes();
		for(int i = 0; i < list.size(); ++i) {
			tempString += list.get(i).getFoodName() + "    1份"+"\n";
		}
		return tempString;
	}
	
	public void onClick(View v) {
		CancleOrderTask task = new CancleOrderTask();
		task.execute(mOrder.getOrderId());
	}
	
	private class CancleOrderTask extends AsyncTask<Object, Integer, Integer>{

		@Override
		protected Integer doInBackground(Object... params) {
			Integer orderId = (Integer)params[0];
			Boolean result = new ConnectServer().sendCancleOrder(orderId);
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
				Toast.makeText(OrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();;

				Intent intent = new Intent(OrderDetailActivity.this, AllOrderList.class);
				startActivity(intent);
				OrderDetailActivity.this.finish();
				break;
			case 0://失败
				Toast.makeText(OrderDetailActivity.this, "取消订单失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
	
}
