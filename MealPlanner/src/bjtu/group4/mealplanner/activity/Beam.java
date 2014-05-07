package bjtu.group4.mealplanner.activity;

import java.io.IOException;

import bjtu.group4.mealplanner.R;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class Beam extends Activity {

	NfcAdapter nfcAdapter;  
	TextView promt; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam);

		promt = (TextView) findViewById(R.id.promt);  
		// 获取默认的NFC控制器  
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);  
		if (nfcAdapter == null) {  
			promt.setText("设备不支持NFC！");  
			return;  
		}  
		if (!nfcAdapter.isEnabled()) {
			promt.setText("请在系统设置中先启用NFC功能！");  
			return;  
		}  
	}

	@Override  
	protected void onResume() {  
		super.onResume();  
		//得到是否检测到ACTION_TECH_DISCOVERED触发  
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			//处理该intent  
			//writeTag(getIntent());
			readTag(getIntent());  
		}  
	}  
	/** 
	 * Parses the NDEF Message from the intent and prints to the TextView 
	 */  
	private void readTag(Intent intent) {  
		//取出封装在intent中的TAG  
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		boolean auth = false;  
		String restaurantID = "";  

		//读取TAG  
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
				// 读取扇区中的块  餐厅ID存储在Sector 0 的Block 1 里
				byte[] data = mfc.readBlock(blockIndex);  
				restaurantID = (new String(data)).trim();
			} else {  
				restaurantID += "获取信息失败\n";  
			}  
			promt.setText(restaurantID);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  


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
				Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
				promt.setText("写入成功");  
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
}
