package com.example.qr_codescan;


import com.http.HttpHelper;
import com.user.UserOperator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	//account 
	private EditText iaccount ;
	//pass
	private EditText ipass ;
	//okButton
	private Button okButton ;
	//okButton
	private CheckBox remember_option ;
	//二维码字符串值
	private String QRString ;
	private static boolean responsed=false ;
	
	//private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_main2);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		    StrictMode.setThreadPolicy(policy);
//		}
		
		iaccount = (EditText) findViewById(R.id.account);
		ipass = (EditText) findViewById(R.id.pass);
		okButton = (Button) findViewById(R.id.okbutton);
		remember_option=(CheckBox)findViewById(R.id.remember_option);
		if(iaccount.getText().toString().equals("")||ipass.getText().equals("")){
			okButton.setEnabled(false);
		}
		
		iaccount.addTextChangedListener(new accountInputTextWatcher() );
		ipass.addTextChangedListener(new accountInputTextWatcher() );
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		//mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
//		
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(remember_option.isChecked()){
					UserOperator uoper=new UserOperator(MainActivity.this);
					uoper.CreateDatabase();
					uoper.InsertOnly(1, iaccount.getText().toString(), ipass.getText().toString());
					uoper=uoper.Query(1);
				}else {
					//Toast.makeText(getApplicationContext(), "你没有勾选记住密码!",Toast.LENGTH_SHORT).show();
					Toast toast = Toast.makeText(getApplicationContext(),"你没有勾选记住密码!", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}
	
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        Toast toast = Toast.makeText(getApplicationContext(),QRString, Toast.LENGTH_LONG);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.setText(val);
			toast.show();
	    }
	};
	
	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        HttpHelper httpHelper=new HttpHelper();
			if(httpHelper.switchParams(QRString, MainActivity.this.iaccount.getText().toString(), ipass.getText().toString())){
				MainActivity.responsed=true;
				if(httpHelper.requestQRAccessServer()){
					data.putString("value","已经成功接入！！！");
				}else{
					data.putString("value","接入失败！！！");
				}
			}else{
				data.putString("value","参数已经被篡改！！！");
			}
	        msg.setData(data);
	        handler.sendMessage(msg);
	        
	    }
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//获得扫描到的内容
				QRString=	bundle.getString("result");
				//显示
				Toast toast = Toast.makeText(getApplicationContext(),QRString, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				//toast.show();
				if(QRString.trim().equals("")){
					toast.setText("无效的数据！！！");
					toast.show();
					break;
				}
				MainActivity.responsed=false;
				new Thread(runnable).start();
				while(!responsed){
					toast.setText("正在接入，请稍等...");
					toast.show();
				}
				//mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		}
    }	
	
	class accountInputTextWatcher implements TextWatcher{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String iaccountString=iaccount.getText().toString();
				String ipassString=ipass.getText().toString();
				if(iaccountString.equals("")||ipassString.equals("")){
					okButton.setEnabled(false);
				}else {
					okButton.setEnabled(true);
				}
				
			}
		}
}
