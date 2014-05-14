package bjtu.group4.mealplanner.activity;

import java.util.ArrayList;
import java.util.Calendar;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.utils.ConnectServer;
import bjtu.group4.mealplanner.utils.SharedData;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class OrderInfoFragment extends Fragment implements OnClickListener {
	private TextView addrTextView;
	private TextView dishesTextView;
	private EditText phoneEditText;
	private EditText numEditText;
	private static Button pickTimeBtn;
	private static Button pickDateBtn;
	private Button sureBtn;
	private OrderActivity fatherActivity;
	private static String timeString = "";
	private static String dateString = "";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View orderLayout = inflater.inflate(R.layout.fragment_orderinfo,
				container, false);
		fatherActivity = (OrderActivity)getActivity();
		bindView(orderLayout);
		return orderLayout;
	}
	
	private void bindView(View v) {
		addrTextView = (TextView)v.findViewById(R.id.textViewAddr);
		dishesTextView = (TextView)v.findViewById(R.id.textViewDishes);
		phoneEditText = (EditText)v.findViewById(R.id.editPhone);
		numEditText = (EditText)v.findViewById(R.id.editNum);
		pickDateBtn = (Button)v.findViewById(R.id.btnChooseDate);
		pickTimeBtn = (Button)v.findViewById(R.id.btnChooseTime);
		sureBtn = (Button)v.findViewById(R.id.btnSure);

		pickDateBtn.setOnClickListener(this);
		pickTimeBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		
		phoneEditText.setText(SharedData.PHONE);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		String sAddr = fatherActivity.getRestaurant().getName() + "\n" +fatherActivity.getRestaurant().getPosition();
		addrTextView.setText(sAddr);
		String sDish = "";
		for(int i = 0; i < fatherActivity.getDishNames().size(); ++i) {
			sDish += fatherActivity.getDishNames().get(i);
			sDish += "\n";
		}	
		dishesTextView.setText(sDish);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
		case R.id.btnChooseDate:    
			DialogFragment df = new OrderDatePickerFragment();
			df.show(getFragmentManager(), "DatePicker"); 	
			break;  
		case R.id.btnChooseTime:  
			DialogFragment tf = new OrderTimePickerFragment(); 
			tf.show(getFragmentManager(), "TimePicker"); 
			break;  
		case R.id.btnSure:  	
			if(dateString == "" ) {
				Toast.makeText(getActivity(), "请选择日期", Toast.LENGTH_LONG).show();
			}
			else if(timeString == "") {
				Toast.makeText(getActivity(), "请选择时间", Toast.LENGTH_LONG).show();
			}
			else {
				OrderTask task = new OrderTask();
				String dishesIds = getDishIdString();
				task.execute(fatherActivity.getRestaurant().getId(), dateString + " " +timeString + ":00", 
						dishesIds, numEditText.getText().toString().trim(), phoneEditText.getText().toString().trim());
			}
			
			break;  
		default:  
			break;  
		}  
	}
	
	private String getDishIdString() {
		String tempString = "";
		ArrayList<Integer> list = fatherActivity.getDishIds();
		for(int i = 0; i < list.size(); ++i) {
			tempString += list.get(i) + ",";
		}
		int lenth = tempString.length();
		return tempString.substring(0, lenth-1);
	}
	
	private class OrderTask extends AsyncTask<Object, Integer, Integer>{

		@Override
		protected Integer doInBackground(Object... params) {
			Integer restId = (Integer)params[0];
			String dateTime = (String)params[1];
			String dishesIds = (String)params[2];
			String peopleNum = (String)params[3];
			String phoneNum = (String)params[4];
			Boolean result = new ConnectServer().sendOrder(restId, dateTime, dishesIds, peopleNum, -1, phoneNum);
			
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
				//成功
				case 1:
					//显示成功的信息
					Toast.makeText(getActivity(), "创建订单成功", Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent();
					intent.setClass(getActivity(), MainActivity.class);
					startActivity(intent);
					getActivity().finish();
					
					break;
				//失败
				case 0:
					Toast.makeText(getActivity(), "创建订单失败", Toast.LENGTH_LONG).show();
					break;
			}
		}
    }
	
	public static class OrderTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {  
		@Override  
		public Dialog onCreateDialog(Bundle savedInstanceState) {  
			// Use the current time as the default values for the picker  
			final Calendar c = Calendar.getInstance();  
			int hour = c.get(Calendar.HOUR_OF_DAY);  
			int minute = c.get(Calendar.MINUTE);  

			// Create a new instance of TimePickerDialog and return it  
			return new TimePickerDialog(getActivity(), this, hour, minute,  
					DateFormat.is24HourFormat(getActivity()));  
		} 

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
			// Do something with the time chosen by the user  
			timeString = hourOfDay + ":" + minute;
			pickTimeBtn.setText("时间:"+timeString);

		}  
	}  

	public static class OrderDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {  

		@Override  
		public Dialog onCreateDialog(Bundle savedInstanceState) {  
			// Use the current time as the default values for the picker  
			final Calendar c = Calendar.getInstance();  
			int year = c.get(Calendar.YEAR);  
			int month = c.get(Calendar.MONTH);  
			int day = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);  
			DatePicker datePicker = dialog.getDatePicker(); 
			datePicker.setMinDate(c.getTimeInMillis()); 

			c.add(Calendar.DATE,   +7);
			datePicker.setMaxDate(c.getTimeInMillis());  
			return dialog;  
		} 

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			monthOfYear += 1;
			dateString = year + "-" + monthOfYear + "-" + dayOfMonth;
			pickDateBtn.setText("日期：" + dateString);
		} 
	} 
}
