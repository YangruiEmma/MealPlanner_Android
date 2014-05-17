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
	 * ����PushManager.startWork��sdk����push server�������������������첽�ġ�������Ľ��ͨ��onBind���ء�
	 * �������Ҫ�õ������ͣ���Ҫ�������ȡ��channel id��user id�ϴ���Ӧ��server�У��ٵ���server�ӿ���channel id��user id�������ֻ������û����͡�
	 * 
	 * @param context
	 *          BroadcastReceiver��ִ��Context
	 * @param errorCode
	 *          �󶨽ӿڷ���ֵ��0 - �ɹ�
	 * @param appid 
	 *          Ӧ��id��errorCode��0ʱΪnull
	 * @param userId
	 *          Ӧ��user id��errorCode��0ʱΪnull
	 * @param channelId
	 *          Ӧ��channel id��errorCode��0ʱΪnull
	 * @param requestId
	 *          �����˷��������id����׷������ʱ���ã�
	 * @return
	 *     none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, 
			String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		// �󶨳ɹ��������Ѱ�flag��������Ч�ļ��ٲ���Ҫ�İ�����
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
	 * ����͸����Ϣ�ĺ�����
	 * 
	 * @param context ������
	 * @param message ���͵���Ϣ
	 * @param customContentString �Զ�������,Ϊ�ջ���json�ַ���
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		String messageString = "͸����Ϣ message=\"" + message + "\" customContentString="
				+ customContentString;
		Log.d("Meal", messageString);

	}

	/**
	 * ����֪ͨ����ĺ�����ע������֪ͨ���û����ǰ��Ӧ���޷�ͨ���ӿڻ�ȡ֪ͨ�����ݡ�
	 * 
	 * @param context ������
	 * @param title ���͵�֪ͨ�ı���
	 * @param description ���͵�֪ͨ������
	 * @param customContentString �Զ������ݣ�Ϊ�ջ���json�ַ���
	 */
	@Override
	public void onNotificationClicked(Context context, String title, 
			String description, String customContentString) {
		String notifyString = "֪ͨ��� title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d("Meal", notifyString);

		updateLineUpMesg(context, title, description);
	}

	/**
	 * PushManager.stopWork() �Ļص�������
	 * 
	 * @param context ������
	 * @param errorCode �����롣0��ʾ�������ͽ�󶨳ɹ�����0��ʾʧ�ܡ�
	 * @param requestId ������������͵������id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d("Meal", responseString);

		// ��󶨳ɹ�������δ��flag��
		if (errorCode == 0) {
			PushMesgUtils.setBind(context, false);
		}
	}

	private void updateLineUpMesg(Context context, String title, String info) {
		Log.d("Meal", "updateContent");

		Intent intent = new Intent();
		if ("�ò�ʱ�����ڿ���".equals(title)) {
			intent.putExtra("EatComing", true); 
			intent.putExtra("lineUpInfo",info);
		}
		else if ("�ò�ʱ��".equals(title)) {
			intent.putExtra("EatTime", true); 
			intent.putExtra("lineUpInfo",info);
		}
		else if ("��������".equals(title)) {
			intent.putExtra("Invitation", true); 
		}
		else if ("�������뷴��".equals(title)) {
			intent.putExtra("InvitFeedback", true);
		}
		else if ("������ȷ��".equals(title)) {
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
