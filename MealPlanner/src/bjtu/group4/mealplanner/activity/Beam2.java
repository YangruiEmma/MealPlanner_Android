package bjtu.group4.mealplanner.activity;

import java.io.IOException;

import bjtu.group4.mealplanner.R;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Beam2 extends Activity {
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
			//finish();  
			return;  
		}  
		if (!nfcAdapter.isEnabled()) {
			promt.setText("请在系统设置中先启用NFC功能！");  
			//finish();  
			return;  
		}  
	}

	@Override  
	protected void onResume() {  
		super.onResume();  
		//得到是否检测到ACTION_TECH_DISCOVERED触发  
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			//处理该intent  
			writeTag(getIntent());
			readTag(getIntent());  
		}  
	}  
	/** 
	 * Parses the NDEF Message from the intent and prints to the TextView 
	 */  
	private void readTag(Intent intent) {  
		//取出封装在intent中的TAG  
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		for (String tech : tagFromIntent.getTechList()) {  
			System.out.println(tech);  
		}  
		boolean auth = false;  
		//读取TAG  
		MifareClassic mfc = MifareClassic.get(tagFromIntent);  
		try {  
			String metaInfo = "";  
			//Enable I/O operations to the tag from this TagTechnology object.  
			mfc.connect();  
			int type = mfc.getType();//获取TAG的类型  
			int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数  
			String typeS = "";  
			switch (type) {  
			case MifareClassic.TYPE_CLASSIC:  
				typeS = "TYPE_CLASSIC";  
				break;  
			case MifareClassic.TYPE_PLUS:  
				typeS = "TYPE_PLUS";  
				break;  
			case MifareClassic.TYPE_PRO:  
				typeS = "TYPE_PRO";  
				break;  
			case MifareClassic.TYPE_UNKNOWN:  
				typeS = "TYPE_UNKNOWN";  
				break;  
			}  
			metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"  
					+ mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";  
			for (int j = 0; j < sectorCount; j++) {  
				//Authenticate a sector with key A.  
				auth = mfc.authenticateSectorWithKeyA(j,  
						MifareClassic.KEY_DEFAULT);  
				int bCount;  
				int bIndex;  
				if (auth) {  
					metaInfo += "Sector " + j + ":验证成功\n";  
					// 读取扇区中的块  
					bCount = mfc.getBlockCountInSector(j);  
					bIndex = mfc.sectorToBlock(j);  
					for (int i = 0; i < bCount; i++) {  
						byte[] data = mfc.readBlock(bIndex);  
						metaInfo += "Block " + bIndex + " : "  
								+ new String(data) + "\n";  
						bIndex++;  
					}  
				} else {  
					metaInfo += "Sector " + j + ":验证失败\n";  
				}  
			}  
			promt.setText(metaInfo);  
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
				mfc.writeBlock(1, "0000000000000001".getBytes("GBK"));
				mfc.writeBlock(2, "girl676888000000".getBytes());  
				mfc.close();  
				Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
				promt.setText("写入成功");  
			}  
		} catch (IOException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		} finally {  
			try {  
				mfc.close();  
			} catch (IOException e) {  
				// TODO Auto-generated catch block  
				e.printStackTrace();  
			}  
		}  
	}
	//字符序列转换为16进制字符串  
	private String bytesToHexString(byte[] src) {  
		StringBuilder stringBuilder = new StringBuilder("0x");  
		if (src == null || src.length <= 0) {  
			return null;  
		}
		char[] buffer = new char[2];  
		for (int i = 0; i < src.length; i++) {  
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);  
		}  
		return stringBuilder.toString();  
	}  

}
