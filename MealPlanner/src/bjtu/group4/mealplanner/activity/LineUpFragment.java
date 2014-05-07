package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LineUpFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_lineup,
				container, false);
		return messageLayout;
	}

}
