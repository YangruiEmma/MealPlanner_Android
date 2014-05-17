package bjtu.group4.mealplanner.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.QueueInfo;
import bjtu.group4.mealplanner.utils.ConnectServer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "UseValueOf", "SimpleDateFormat" })
public class LineUpFragment extends Fragment  implements OnClickListener {

	private ImageView img_noLineUpMesg; 
	private TextView txt_lineupMesg;
	private LinearLayout layout_mesgGetTime;
	private TextView txt_mesgGetTime;
	private Button btn_lineUpSee;
	private Button btn_lineUpCancel; 
	private ProgressDialog progress;

	private MainActivity fatherActivity;
	private QueueInfo queueInfo;
	private MealApplication application;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("排队", "onCreateView");
		View lineUpView = inflater.inflate(R.layout.fragment_lineup,
				container, false);
		fatherActivity = (MainActivity)getActivity();
		bingViews(lineUpView);
		application = (MealApplication) getActivity().getApplication();

		if ("".equals(fatherActivity.getLineUpInfo())) {
			getRestInfo();
		}
		//消息提示-就餐时间提醒
		else if(fatherActivity.isEatTime()) {
			lineUpInfoShow(fatherActivity.getLineUpInfo());
			processEatTime();
		}
		else if(!fatherActivity.isEatTime()) {
			lineUpInfoShow(fatherActivity.getLineUpInfo());
		}
		return lineUpView;
	}

	private void bingViews(View v) {
		img_noLineUpMesg = (ImageView)v.findViewById(R.id.img_noLineUpMesg);
		txt_lineupMesg = (TextView)v.findViewById(R.id.txt_lineupMesg);
		layout_mesgGetTime = (LinearLayout)v.findViewById(R.id.layout_mesgGetTime);
		txt_mesgGetTime = (TextView)v.findViewById(R.id.txt_mesgGetTime);
		btn_lineUpSee = (Button)v.findViewById(R.id.btn_lineUpSee);
		btn_lineUpCancel = (Button)v.findViewById(R.id.btn_lineUpCancel);

		btn_lineUpSee.setOnClickListener(this);
		btn_lineUpCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
		case R.id.btn_lineUpSee:    
			getRestInfo();
			break;
		case R.id.btn_lineUpCancel:    
			cancelLineUp();
			break; 
		default:  
			break;  
		}  

	}
	private void lineUpInfoShow(String lineUpInfo) {
		img_noLineUpMesg.setBackgroundResource(R.drawable.smell_big);
		txt_lineupMesg.setText(lineUpInfo);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date()).toString();
		txt_mesgGetTime.setText(time);
		layout_mesgGetTime.setVisibility(View.VISIBLE);
		btn_lineUpSee.setEnabled(true);
		btn_lineUpCancel.setEnabled(true);

	}
	private void noLineUp() {
		img_noLineUpMesg.setBackgroundResource(R.drawable.sorry_big);
		txt_lineupMesg.setText("抱歉哦亲╭(╯3╰)╮，您还没有参与线下排队或排队已结束！通过餐馆NFC卡即可体验线下排队的乐趣哦O(∩_∩)O~");
		layout_mesgGetTime.setVisibility(View.INVISIBLE);
		btn_lineUpSee.setEnabled(false);
		btn_lineUpCancel.setEnabled(false);

	}

	private void processEatTime() {
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle("排队提醒")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("就餐时间已到哦！亲").setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				noLineUp();
			}
		}).show();

	}

	private void getRestInfo() {
		GetLineUpInfoTask task = new GetLineUpInfoTask();
		//异步获取餐厅信息
		task.execute(application.getUserId());
	}

	private void cancelLineUp() {
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle("确认取消")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("是否确认取消当前排队?").setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				CancelLineUpTask task = new CancelLineUpTask();
				progress = new ProgressDialog(getActivity());
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("请稍等");
				progress.setMessage("正在取消当前排队...");
				progress.setIndeterminate(false);
				progress.setCancelable(true);
				progress.show();

				//异步获取餐厅信息
				task.execute(application.getUserId());
			}
		}).setNegativeButton("否", null).create()
		.show();
	}

	private class GetLineUpInfoTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String userID =  params[0].toString();

			queueInfo = new ConnectServer().getLineUpInfo(userID);

			if(queueInfo!=null){
				//有排队
				if (queueInfo.hasQueue())
					return 1;
				//无排队
				else 
					return 2;
			}else{
				return 0;

			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			int response = result.intValue();
			switch(response){
			case 1:
				Toast.makeText(getActivity(), "获取排队信息成功，有排队", Toast.LENGTH_LONG).show();
				//				//前面无人排队
				//				if (queueInfo.getPeopleBefore()==0) {
				//					String  lineUpInfo= "亲O(∩_∩)O，欢迎到"+queueInfo.getRestName()+"就餐，前面已无人排队您耐心等待，马上便可用餐！";
				//					lineUpInfoShow(lineUpInfo);
				//				}else {
				String lineUpInfo = "亲O(∩_∩)O，您有"+queueInfo.getPeopleNum()+"人到"+queueInfo.getRestName()+"餐厅就餐，为您提供了"+
						queueInfo.getSeatType()+"人桌，前面还有"+queueInfo.getPeopleBefore()+"位排队"+
						queueInfo.getSeatType()+"人桌,请耐心等待(*^__^*)";
				lineUpInfoShow(lineUpInfo);
				//				}
				break;
			case 2:
				Toast.makeText(getActivity(), "获取排队信息成功，当前无排队", Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(getActivity(), "获取排队信息失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	private class CancelLineUpTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * 执行异步任务时回调，在Ui线程外执行
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String userID =  params[0].toString();
			boolean result = new ConnectServer().cancelLineUp(userID);

			if(result)
				return 1;
			else
				return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			case 1:
				Toast.makeText(getActivity(), "取消排队成功", Toast.LENGTH_LONG).show();
				noLineUp();
				break;
			case 0:
				Toast.makeText(getActivity(), "取消排队失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

}
