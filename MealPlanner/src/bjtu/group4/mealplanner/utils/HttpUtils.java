package bjtu.group4.mealplanner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class HttpUtils {
	
	public static String postData(String path, Map<String, String> map) {
	      
	       InputStream inputStream = null;
	       OutputStream outputStream = null;
	       try {
	    	   //��������
	           URL url = new URL(path);
	           HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	           
	           //������������
	           //ʹ��URL���ӽ������
	           connection.setDoInput(true);
	           //ʹ��URL���ӽ�������
	           connection.setDoOutput(true);
	           //���Ի���
	           connection.setUseCaches(false);
	           //����URL���󷽷�
	           connection.setRequestMethod("POST");
	           
	           StringBuffer buffer = new StringBuffer();
	           if (map != null && !map.isEmpty()) {
	              for (Map.Entry<String, String> entry : map.entrySet()) {
	                  buffer.append(entry.getKey())
	                         .append("=")
	                         .append(URLEncoder.encode(entry.getValue(), "utf-8"))
	                         .append("&");
	              }
	              buffer.deleteCharAt(buffer.length() - 1);
	           }
	           
	           byte[] data = buffer.toString().getBytes();
	           connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	           connection.setRequestProperty("Content-Length", String.valueOf(data.length));
	           connection.setRequestProperty("Charset", "UTF-8");
	           //�������������д������
	           outputStream = connection.getOutputStream();
	           outputStream.write(data);
	           
	           //�����Ӧ״̬
	           if (connection.getResponseCode() == 200) {
	              inputStream = connection.getInputStream();
	              return getContext(inputStream, "utf-8");
	           }
	       } catch (MalformedURLException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       } finally {
	           try {
	              inputStream.close();
	              outputStream.close();
	           } catch (IOException e) {
	               // TODO Auto-generated catch block
	              e.printStackTrace();
	           }
	 
	       }
	       return null;
	}
	
	public static String getContext(InputStream is, String encode){
		
		if(is != null){
			StringBuilder sb = new StringBuilder();
			String line;
			
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
				while((line = reader.readLine())!= null){
					sb.append(line);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return sb.toString();
		}else{
			return "";
		}
	}
	
	//�Ƿ���������
    public static boolean IsHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info!=null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }
}
