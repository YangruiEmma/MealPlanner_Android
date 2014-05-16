package bjtu.group4.mealplanner.activity;

import java.io.IOException;

import bjtu.group4.mealplanner.R;
import bjtu.group4.mealplanner.model.QueueInfo;
import bjtu.group4.mealplanner.model.Restaurant;
import bjtu.group4.mealplanner.utils.ConnectServer;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseValueOf")
public class Beam extends Activity {

	private NfcAdapter nfcAdapter;  
	private ProgressDialog progress;

	private TextView promt; 
	private EditText personNum;
	private LinearLayout lineLayout1;
	private LinearLayout lineLayout2;
	private Button btnLine;
	private Button btnHome;

	private String restaurantID = "";
	MealApplication application;
	Restaurant rest;
	QueueInfo queueInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);
		application = (MealApplication) getApplication();
		
		bindViews();
		// ��ȡĬ�ϵ�NFC������  
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);  
		if (nfcAdapter == null) {  
			promt.setText("�豸��֧��NFC��");  
			return;  
		}  
		if (!nfcAdapter.isEnabled()) {
			promt.setText("����ϵͳ������������NFC���ܣ�");  
			return;  
		}  

		setListener();
	}



	private void bindViews() {
		promt = (TextView) findViewById(R.id.promt); 
		personNum = (EditText) findViewById(R.id.personNum); 
		lineLayout1 = (LinearLayout)findViewById(R.id.nfcLineLayout1); 
		lineLayout2 = (LinearLayout)findViewById(R.id.nfcLineLayout2); 
		lineLayout1.setVisibility(View.INVISIBLE);
		lineLayout2.setVisibility(View.INVISIBLE);
		btnLine = (Button)findViewById(R.id.nfcBtnLine);
		btnHome = (Button)findViewById(R.id.nfcBtnHome);
	}

	private void setListener() {
		btnLine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String numString = personNum.getText().toString();
				if ("".equals(numString)) {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("��ʾ")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("���������ò�����Ŷ���ף�").create()
					.show();
				} else if (Integer.parseInt(numString) > 10) {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("��ʾ")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("��Ǹ���ò��������࣬���ڷ���̨Ԥ����").create()
					.show();
				} else {
					AlertDialog.Builder builder= new AlertDialog.Builder(Beam.this);
					builder.setTitle("ȷ���Ŷ�")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("�Ƿ�ȷ�������Ŷ��ò�?").setPositiveButton("��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ׼�������Ŷӷ���
							sendQueueMesg();
						}
					}).setNegativeButton("��", null).create()
					.show();
				}
			}
		});

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Beam.this, MainActivity.class);
				startActivity(intent);
				Beam.this.finish();
			}
		});
	}

	@Override  
	protected void onResume() {  
		super.onResume();
		//to see if user login
		if (application != null && !application.getIsLogin()) {
			promt.setText("δ��¼�����ȵ�¼��");  
			waitToLogin();
		}//�õ��Ƿ��⵽ACTION_TECH_DISCOVERED����  
		else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			//�����intent  
			//writeTag(getIntent());
			readTag(getIntent());  
		}  
	}  
	/** 
	 * Parses the NDEF Message from the intent and prints to the TextView 
	 */  
	private void readTag(Intent intent) {  
		//ȡ����װ��intent�е�TAG  
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		boolean auth = false;  

		//��ȡTAG  
		MifareClassic mfc = MifareClassic.get(tagFromIntent);  
		try {  

			int sectorIndex = 0;
			int blockIndex = 1; 
			//Enable I/O operations to the tag from this TagTechnology object.  
			mfc.connect();  
			//Authenticate a sector with key A.  
			auth = mfc.authenticateSectorWithKeyA(sectorIndex,  
					MifareClassic.KEY_DEFAULT);  
			if (auth) {  
				// ��ȡ�����еĿ�  ����ID�洢��Sector 0 ��Block 1 ��
				byte[] data = mfc.readBlock(blockIndex);  
				restaurantID = (new String(data)).trim();
				//promt.setText("��ȡ��Ϣ�ɹ���");
				getRestInfo();
			} else {  
				promt.setText("��ȡ��Ϣʧ�ܣ�");
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  


	private void getRestInfo() {
		RestInfoTask task = new RestInfoTask();
		progress = new ProgressDialog(Beam.this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("���Ե�");
		progress.setMessage("���ڻ�ȡ������Ϣ");
		// ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
		progress.setIndeterminate(false);
		// ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
		progress.setCancelable(true);
		progress.show();

		//�첽��ȡ������Ϣ
		task.execute(restaurantID);
	}


	private class RestInfoTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * ִ���첽����ʱ�ص�����Ui�߳���ִ��
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String restID = (String)params[0];

			rest = new ConnectServer().getRestInfo(restID);

			if(rest!=null){
				return new Integer(1);
			}else{
				return new Integer(0);

			}
		}

		@SuppressLint("UseValueOf")
		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			case 1:
				promt.setText("��ӭ��"+rest.getName()+"�����ò�");
				lineLayout1.setVisibility(View.VISIBLE);
				lineLayout2.setVisibility(View.VISIBLE);
				//					line_txt.setTextColor(android.graphics.Color.BLACK);
				//					person_txt.setTextColor(android.graphics.Color.BLACK);
				//					personNum.setFilters(new InputFilter[] {  
				//				            new InputFilter() {  
				//								@Override
				//								public CharSequence filter(CharSequence source,
				//										int start, int end, Spanned dest,
				//										int dstart, int dend) {
				//									return null;
				//								}  
				//				            }  
				//				        });  
				break;

			case 0:
				Toast.makeText(Beam.this, "��ȡ������������Ϣʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	/**
	 * ������������Ŷ���Ϣ
	 * 
	 */
	private void sendQueueMesg() {
		SendQueueMesgTask task = new SendQueueMesgTask();
		progress = new ProgressDialog(Beam.this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setTitle("���Ե�");
		progress.setMessage("���ڷ����Ŷ���Ϣ");
		// ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ
		progress.setIndeterminate(false);
		// ����ProgressDialog �Ƿ���԰��˻ؼ�ȡ��
		progress.setCancelable(true);
		progress.show();

		//�첽��ȡ������Ϣ
		task.execute(restaurantID, application.getUserId(), personNum.getText().toString());//restId=3&userId=1&peopleNum=6
	}

	private class SendQueueMesgTask extends AsyncTask<Object, Integer, Integer>{

		/**
		 * ִ���첽����ʱ�ص�����Ui�߳���ִ��
		 * */
		@Override
		protected Integer doInBackground(Object... params) {
			String restID = (String)params[0];
			String userID =  params[1].toString();
			String peopleNum = (String)params[2];

			queueInfo = new ConnectServer().sendQueueMesg(restID,userID,peopleNum);

			if(queueInfo!=null){
				return new Integer(1);
			}else{
				return new Integer(0);

			}
		}

		@SuppressLint("UseValueOf")
		@Override
		protected void onPostExecute(Integer result) {
			progress.cancel();
			int response = result.intValue();
			switch(response){
			case 1:
				Toast.makeText(Beam.this, "���Ѿ���ʼ�Ŷ���", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(Beam.this, MainActivity.class);
				intent.putExtra("LineUp", true); 
				startActivity(intent);
				Beam.this.finish();
				break;

			case 0:
				Toast.makeText(Beam.this, "�Ŷ�ʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@SuppressWarnings("unused")
	private void writeTag(Intent intent) {
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); 
		MifareClassic mfc = MifareClassic.get(tagFromIntent);  

		try {
			mfc.connect();
			boolean auth = false;  
			short sectorAddress = 0;  
			auth = mfc.authenticateSectorWithKeyA(sectorAddress,  
					MifareClassic.KEY_DEFAULT);  
			if (auth) {  
				// the last block of the sector is used for KeyA and KeyB cannot be overwritted  
				mfc.writeBlock(1, "               1".getBytes("GBK"));
				mfc.writeBlock(2, "  RestaurantName".getBytes());  
				//Sector 1
				//				mfc.writeBlock(4, "MealPlannerGroup".getBytes());  
				//				mfc.writeBlock(5, "MealPlannerGroup".getBytes());  
				mfc.close();  
				Toast.makeText(this, "д��ɹ�", Toast.LENGTH_SHORT).show();
				promt.setText("д��ɹ�");  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				mfc.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
	}

	public void waitToLogin() {
		new Thread(new Runnable() {//�����߳�
			public void run() {//ʵ��Runnable��run���������߳���
				try {
					Thread.sleep(2000);//��ӭ������ͣ2����
					Message m = new Message();//����Message����
					logHandler.sendMessage(m);//����Ϣ�ŵ���Ϣ������
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();//�����߳�
	}

	void toLogin() {		
		Intent it=new Intent();//ʵ����Intent
		it.setClass(Beam.this, Login.class);//����Class
		startActivity(it);//����Activity
		Beam.this.finish();//����Welcome Activity
	}

	//ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н��У����Ƚ��ȳ�
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			toLogin();
		}
	};

}
