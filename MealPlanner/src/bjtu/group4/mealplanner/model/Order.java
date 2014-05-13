package bjtu.group4.mealplanner.model;

import java.io.Serializable;
import java.security.PublicKey;

import android.R.integer;

public class Order implements Serializable{

	private static final long serialVersionUID = 3568643101894525737L;
	private int orderId;
	private int mealId;
	private int restId;
	private int peopleNum;
	private Status OrderState;
	private String phoneNum;
	
	public enum Status {
		SUCCESSE, FAIL, PASS, CANCLE, COMPLETE
	}

}
