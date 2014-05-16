package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineUpFragment extends Fragment {
	
	private ImageView img_noLineUpMesg; //û���Ŷ�ʱ��ͼƬ��û���Ŷ������أ��Ŷ�ʱ��ʾ����������ȡ���Ͳ��ѵ���Ϣʱ��ʾ
	private TextView txt_lineupMesg;
	private LinearLayout layout_lineupTime;
	private TextView txt_mesgGetTime;
	private Button btn_lineUpSee;
	private Button btn_lineUpCancel;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_lineup,
				container, false);
		return messageLayout;
	}

}
