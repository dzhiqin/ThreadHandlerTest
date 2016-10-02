package com.example.threadhandletest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
class CallbackUtil{
	public void setCallbackListener(CallbackInterface callbackInterface){
		callbackInterface.callback();
	}
}

public class MainActivity extends Activity implements OnClickListener,CallbackInterface{
	//MainActivityʵ���������ӿ�OnClickListenner��CallbackInterface
	private TextView txView;
	private Button btnThreadAndHandler;
	private Button btnThreadAndCallback;
	private Button btnLooper;
	private CallbackUtil myCallback;
	private int clickCount;
	public final static int MSG_FLAG=1;
	public final static int MSG_FLAG2=2;
	private static Handler handler2;
	/**
	 * ���Ӻ���������ʵ���첽�������̵߳�UIԪ�أ�Ĭ�Ϲ������̵߳�MessageQueue
	 */
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_FLAG:
				txView.setText("Let's meet Handler!");
				break;
			case MSG_FLAG2:
				txView.setText("click count"+clickCount);
				break;
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txView=(TextView)findViewById(R.id.tx_text);
		btnThreadAndHandler=(Button)findViewById(R.id.btn_threadandhandler);
		btnThreadAndCallback=(Button)findViewById(R.id.btn_threadandcall);		
		btnLooper=(Button)findViewById(R.id.btn_looper);
		btnLooper.setOnClickListener(this);
		btnThreadAndHandler.setOnClickListener(this);
		btnThreadAndCallback.setOnClickListener(this);		
		new LooperThread().start();//����������LooperThread
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {	
		switch(v.getId()){
		case R.id.btn_threadandcall:
			//�������߳���ʵ����CallbackUtil������setCallbackListener����
			//��������ʵ���첽���ã����ǲ��ܸ���UIԪ��
			/*new Thread(new Runnable(){
				@Override
				public void run() {					
					new CallbackUtil().setCallbackListener(MainActivity.this);
				}
				
			}).start();*/
			//û�д������̣߳����Ը���UIԪ��
			new CallbackUtil().setCallbackListener(this);
			break;
		case R.id.btn_threadandhandler:
			new Thread (new Runnable(){
				@Override
				public void run() {
					// ��ȻMessage��Ĭ�ϵĹ��췽����������Message.obtain()�����ӿյ�
					//��Ϣ���л�ȡ����Ϣ������Խ�ʡ��Դ��
					//Message message=new Message();
					Message message=Message.obtain();
					message.what=MSG_FLAG;
					mHandler.sendMessage(message);				
				}}).start();
			break;
		case R.id.btn_looper:
			
			/*Message msg=Message.obtain();
			msg.what=MSG_FLAG2;
			handler2.sendMessage(msg);*/
			handler2.obtainMessage(MSG_FLAG2).sendToTarget();
			break;
		default:
			break;
		}
	}

	@Override
	public void callback() {
		// TODO �Զ����ɵķ������
		Log.v("test", "this is callback()");
		txView.setText("Les't meet callback!");
	}
	static class LooperThread extends Thread{
		//�߳���ʵ����Looper������handler2�ͱ��̵߳�MQ�����
		@Override
		public void run(){
			Looper.prepare();
			 handler2=new Handler(){
				public void handleMessage(Message msg){
					switch(msg.what){
					case MSG_FLAG2:
						Log.v("test", "LooperThread");
						break;
					}
				}
			};
			Looper.loop();
		}
	}
}
