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
		Log.d("�Ŷ�", "onCreateView");
		View lineUpView = inflater.inflate(R.layout.fragment_lineup,
				container, false);
		fatherActivity = (MainActivity)getActivity();
		bingViews(lineUpView);
		application = (MealApplication) getActivity().getApplication();

		if ("".equals(fatherActivity.getLineUpInfo())) {
			getRestInfo();
		}
		//��Ϣ��ʾ-�Ͳ�ʱ������
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String time = df.format(new Date()).toString();
		txt_mesgGetTime.setText(time);
		layout_mesgGetTime.setVisibility(View.VISIBLE);
		btn_lineUpSee.setEnabled(true);
		btn_lineUpCancel.setEnabled(true);

	}
	private void noLineUp() {
		img_noLineUpMesg.setBackgroundResource(R.drawable.sorry_big);
		txt_lineupMesg.setText("��ǸŶ�רq(�s3�t)�r������û�в��������Ŷӻ��Ŷ��ѽ�����ͨ���͹�NFC���������������Ŷӵ���ȤŶO(��_��)O~");
		layout_mesgGetTime.setVisibility(View.INVISIBLE);
		btn_lineUpSee.setEnabled(false);
		btn_lineUpCancel.setEnabled(false);

	}

	private void processEatTime() {
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle("�Ŷ�����")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("�Ͳ�ʱ���ѵ�Ŷ����").setPositiveButton("��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				noLineUp();
			}
		}).show();

	}

	private void getRestInfo() {
		GetLineUpInfoTask task = new GetLineUpInfoTask();
		//�첽��ȡ������Ϣ
		task.execute(application.getUserId());
	}

	private void cancelLineUp() {
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle("ȷ��ȡ��")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("�Ƿ�ȷ��ȡ����ǰ�Ŷ�?").setPositiveButton("��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				CancelLineUpTask task = new CancelLineUpTask();
				progress = new ProgressDialog(getActivity());
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setTitle("���Ե�");
				progress.setMessage("����ȡ����ǰ�Ŷ�...");
				progress.setIndeterminate(false);
				progress.setCancelable(true);
				progress.show();

				//�첽��ȡ������Ϣ
				task.execute(application.getUserId());
			}
		}).setNegativeButton("��", null).create()
		.show();
	}

	private class GetLineUpInfoTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * ִ���첽����ʱ�ص�����Ui�߳���ִ��
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String userID =  params[0].toString();

			queueInfo = new ConnectServer().getLineUpInfo(userID);

			if(queueInfo!=null){
				//���Ŷ�
				if (queueInfo.hasQueue())
					return 1;
				//���Ŷ�
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
				Toast.makeText(getActivity(), "��ȡ�Ŷ���Ϣ�ɹ������Ŷ�", Toast.LENGTH_LONG).show();
				//				//ǰ�������Ŷ�
				//				if (queueInfo.getPeopleBefore()==0) {
				//					String  lineUpInfo= "��O(��_��)O����ӭ��"+queueInfo.getRestName()+"�Ͳͣ�ǰ���������Ŷ������ĵȴ������ϱ���òͣ�";
				//					lineUpInfoShow(lineUpInfo);
				//				}else {
				String lineUpInfo = "��O(��_��)O������"+queueInfo.getPeopleNum()+"�˵�"+queueInfo.getRestName()+"�����Ͳͣ�Ϊ���ṩ��"+
						queueInfo.getSeatType()+"������ǰ�滹��"+queueInfo.getPeopleBefore()+"λ�Ŷ�"+
						queueInfo.getSeatType()+"����,�����ĵȴ�(*^__^*)";
				lineUpInfoShow(lineUpInfo);
				//				}
				break;
			case 2:
				Toast.makeText(getActivity(), "��ȡ�Ŷ���Ϣ�ɹ�����ǰ���Ŷ�", Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(getActivity(), "��ȡ�Ŷ���Ϣʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	private class CancelLineUpTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * ִ���첽����ʱ�ص�����Ui�߳���ִ��
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
				Toast.makeText(getActivity(), "ȡ���Ŷӳɹ�", Toast.LENGTH_LONG).show();
				noLineUp();
				break;
			case 0:
				Toast.makeText(getActivity(), "ȡ���Ŷ�ʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

}
