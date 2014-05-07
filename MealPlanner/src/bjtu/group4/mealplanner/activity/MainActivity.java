package bjtu.group4.mealplanner.activity;

import bjtu.group4.mealplanner.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	
	ActionBar actionBar; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    initViews();  
	    fragmentManager = getFragmentManager();  
	    setTabSelection(0);  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//super.onCreateOptionsMenu(menu); 
		return true;
	}
	 /** 
     * fragments 
     */  
	private FragmentManager fragmentManager;  
    private MyInfoFragment userInfoFragment;  
    private PlanMealFragment planMealFragment;  
    private StartPageFragment startPageFragment;  
    private LineUpFragment lineUpFragment;  
    /** 
     * Tab上的 view
     */   
    private View userInfoLayout;  
    private View planMealLayout;  
    private View startPageLayout;  
    private View lineUpLayout;  
  
    /** 
     * 在Tab布局上显示动态图标的控件 
     */  
    private ImageView userInfoImg;  
    private ImageView planMealImg;  
    private ImageView startPageImg;   
    private ImageView lineUpImg;  
    
    /** 
     * 在Tab布局上显示字体
     */  
    private TextView userInfoText;  
    private TextView planMealText;  
    private TextView startPageText;   
    private TextView lineUpText;  
  
    private void initViews() {  
    	startPageLayout = findViewById(R.id.startPage_layout);  
        planMealLayout = findViewById(R.id.planMeal_layout);  
        lineUpLayout = findViewById(R.id.lineUp_layout);  
        userInfoLayout = findViewById(R.id.myInfo_layout);  
        
        startPageImg = (ImageView) findViewById(R.id.startPage_image);  
        planMealImg = (ImageView) findViewById(R.id.planMeal_image);  
        lineUpImg = (ImageView) findViewById(R.id.lineUp_image);  
        userInfoImg = (ImageView) findViewById(R.id.myInfo_image);  
        
        userInfoText = (TextView) findViewById(R.id.myInfo_text);
        planMealText = (TextView) findViewById(R.id.planMeal_text);
        startPageText = (TextView) findViewById(R.id.startPage_text);
        lineUpText = (TextView) findViewById(R.id.lineUp_text);
        
        userInfoLayout.setOnClickListener(this);  
        planMealLayout.setOnClickListener(this);  
        startPageLayout.setOnClickListener(this);  
        lineUpLayout.setOnClickListener(this);  
    }  
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.startPage_layout:    
            setTabSelection(0);  
            break;  
        case R.id.planMeal_layout:  
            setTabSelection(1);  
            break;  
        case R.id.lineUp_layout:  
            setTabSelection(2);  
            break;  
        case R.id.myInfo_layout:   
            setTabSelection(3);  
            break;  
        default:  
            break;  
        }  
    }  
  
    /** 
     * 根据传入的index参数来设置选中的tab页。 
     *  
     * @param index 
     * 每个tab页对应的下标。0表示首页，1表示组饭局，2表示排排坐，3表示我的信息。 
     */  
    private void setTabSelection(int index) {  
        clearSelection();  
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        hideFragments(transaction); 
        
        switch (index) {  
        case 0:  
            startPageImg.setImageResource(R.drawable.startpage_selected);  
            startPageText.setTextColor(Color.WHITE);  
            if (startPageFragment == null) {  
            	startPageFragment = new StartPageFragment();  
                transaction.add(R.id.content, startPageFragment);  
            } else {  
                transaction.show(startPageFragment);  
            }  
            break;  
        case 1:  
            planMealImg.setImageResource(R.drawable.planmeal_selected);  
            planMealText.setTextColor(Color.WHITE);  
            if (planMealFragment == null) {  
                planMealFragment = new PlanMealFragment();  
                transaction.add(R.id.content, planMealFragment);  
            } else {  
                transaction.show(planMealFragment);  
            }  
            break;  
        case 2:  
            lineUpImg.setImageResource(R.drawable.lineup_selected);
            lineUpText.setTextColor(Color.WHITE);  
            if (lineUpFragment == null) {   
            	lineUpFragment = new LineUpFragment();  
                transaction.add(R.id.content, lineUpFragment);  
            } else {  
                transaction.show(lineUpFragment);  
            }  
            break;  
        case 3:  
        default:  
            userInfoImg.setImageResource(R.drawable.myinfo_selected);  
            userInfoText.setTextColor(Color.WHITE);  
            if (userInfoFragment == null) {  
                userInfoFragment = new MyInfoFragment();  
                transaction.add(R.id.content, userInfoFragment);  
            } else {  
                transaction.show(userInfoFragment);  
            }  
            break;  
        }  
        transaction.commit();  
    }  

    private void clearSelection() {  
        startPageImg.setImageResource(R.drawable.startpage_unselected);  
        planMealImg.setImageResource(R.drawable.planmeal_unselected);  
        lineUpImg.setImageResource(R.drawable.lineup_unselected);  
        userInfoImg.setImageResource(R.drawable.myinfo_unselected);  
    }  
  
    private void hideFragments(FragmentTransaction transaction) {  
        if (startPageFragment != null) {  
            transaction.hide(startPageFragment);  
        }  
        if (planMealFragment != null) {  
            transaction.hide(planMealFragment);  
        }  
        if (lineUpFragment != null) {  
            transaction.hide(lineUpFragment);  
        }  
        if (userInfoFragment != null) {  
            transaction.hide(userInfoFragment);  
        }  
    }
}
