/**
 * Fragment UI to show userInformation
 */
package bjtu.group4.mealplanner.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import bjtu.group4.mealplanner.R;

/**
 * @author lcmm
 *
 */
public class MyInfoFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_myinfo,
				container, false);
		return messageLayout;
	}
}
