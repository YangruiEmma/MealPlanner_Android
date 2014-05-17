package bjtu.group4.mealplanner.utils;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import bjtu.group4.mealplanner.activity.MainActivity;
import bjtu.group4.mealplanner.activity.MealApplication;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;

public class PushMessageReceiver  extends FrontiaPushMessageReceiver {

	MealApplication application;
	/**
	 * 调用PushManager.startWork后，sdk将对push server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。
	 * 如果您需要用单播推送，需要把这里获取的channel id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *          BroadcastReceiver的执行Context
	 * @param errorCode
	 *          绑定接口返回值，0 - 成功
	 * @param appid 
	 *          应用id。errorCode非0时为null
	 * @param userId
	 *          应用user id。errorCode非0时为null
	 * @param channelId
	 *          应用channel id。errorCode非0时为null
	 * @param requestId
	 *          向服务端发起的请求id。在追查问题时有用；
	 * @return
	 *     none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, 
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			PushMesgUtils.setBind(context, true);
		}
		application = (MealApplication) context.getApplicationContext();
		if (!application.hasBind()) {
			application.setBaiduUserId(userId);
			application.setPushMesgChannelId(channelId);
			application.setBind(true);
			sendPushInfotoServer(context);
		}
		Log.d("Meal", responseString);
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context 上下文
	 * @param message 推送的消息
	 * @param customContentString 自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		String messageString = "透传消息 message=\"" + message + "\" customContentString="
				+ customContentString;
		Log.d("Meal", messageString);

	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context 上下文
	 * @param title 推送的通知的标题
	 * @param description 推送的通知的描述
	 * @param customContentString 自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title, 
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d("Meal", notifyString);

		updateLineUpMesg(context, title, description);
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context 上下文
	 * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId 分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d("Meal", responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			PushMesgUtils.setBind(context, false);
		}
	}

	private void updateLineUpMesg(Context context, String title, String info) {
		Log.d("Meal", "updateContent");

		Intent intent = new Intent();
		if ("用餐时间正在靠近".equals(title)) {
			intent.putExtra("EatComing", true); 
			intent.putExtra("lineUpInfo",info);
		}
		else if ("用餐时刻".equals(title)) {
			intent.putExtra("EatTime", true); 
			intent.putExtra("lineUpInfo",info);
		}
		else if ("饭局邀请".equals(title)) {
			intent.putExtra("Invitation", true); 
		}
		else if ("饭局邀请反馈".equals(title)) {
			intent.putExtra("InvitFeedback", true);
		}
		else if ("订单已确认".equals(title)) {
			intent.putExtra("OrderConfirmed", true);
		}
		intent.setClass(context.getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.getApplicationContext().startActivity(intent);
	}


	private void sendPushInfotoServer(Context context) {
		Intent intent = new Intent();
		intent.setClass(context.getApplicationContext(), MainActivity.class);
		intent.putExtra("bindInfo",true); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplicationContext().startActivity(intent);

	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
	}


	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
	}

}
