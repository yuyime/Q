package com.example.qr_codescan;


import org.json.JSONException;
import org.json.JSONObject;

import com.http.HttpHelper;
import com.user.UserOperator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private String uaccount;
	//pass
	private EditText ipass ;
	private String uname;
	//okButton
	private Button okButton ;
	//okButton
	private CheckBox remember_option ;
	//二维码字符串值
	private String QRString ;
	private static boolean responsed=false ;
	private Toast toast=null;
	//private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_main2);
		toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		iaccount = (EditText) findViewById(R.id.account);
		ipass = (EditText) findViewById(R.id.pass);
		okButton = (Button) findViewById(R.id.okbutton);
		remember_option=(CheckBox)findViewById(R.id.remember_option);
		
		if(iaccount.getText().toString().equals("")||ipass.getText().equals("")){
			okButton.setEnabled(false);
		}
		
		iaccount.addTextChangedListener(new accountInputTextWatcher() );
		ipass.addTextChangedListener(new accountInputTextWatcher() );
		//mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		
		//载入密码
		UserOperator uoper=new UserOperator(MainActivity.this);
		String rememberPass=uoper.getConfigByName("rememberPass");
		System.out.println("--------"+rememberPass);
		if(rememberPass==null){
			this.remember_option.setChecked(true);
		}else if(rememberPass.equals("true")){
			this.remember_option.setChecked(true);
			uoper=uoper.Query(1);
			if(null!=uoper){
				iaccount.setText(uoper.getUasno());
				ipass.setText(uoper.getUapass());
			}
		}else if(rememberPass.equals("false")){
			this.remember_option.setChecked(false);
		}
		
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserOperator uoper=new UserOperator(MainActivity.this);
				uoper.configByName(1, "rememberPass", remember_option.isChecked()?"true":"false");
				if(!remember_option.isChecked()){
					toast.setText("你没有勾选记住密码!");
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
	        MainActivity.responsed=true;
			if(httpHelper.switchParams(QRString, MainActivity.this.iaccount.getText().toString(), ipass.getText().toString())){
				if(httpHelper.requestQRAccessServer()){
					toast.cancel();
					MainActivity.this.dealResult(httpHelper.getResult());
					return;
				}else{
					data.putString("value","接入失败！！！");
				}
			}else{
				data.putString("value","无效的二维码："+QRString);
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
	
	public void dealResult(String result) {
		try {
			JSONObject jsonObject=new JSONObject(result);
			String msg=jsonObject.optString("msg");
			if(null==msg||"".equals(msg)){
				toast.setText("网络和服务器忙，请重新扫描");
				toast.show();
				return;
			}else if("QRAccess".equals(msg)){
				UserOperator uoper=new UserOperator(MainActivity.this);
				uoper.configByName(1, "rememberPass", remember_option.isChecked()?"true":"false");
				if(remember_option.isChecked()){
					uoper.InsertOnly(1, iaccount.getText().toString(), ipass.getText().toString());
					uoper=uoper.Query(1);
					if(null==uoper){
						toast.setText("记住密码失败!");
						toast.show();
					}
				}else {
					toast.setText("你没有勾选记住密码!");
					toast.show();
				}
				this.uaccount=jsonObject.optString("usersno");
				this.uname=jsonObject.optString("username");
			}
			Intent intent = new Intent();
			intent.putExtra("msg", msg);
			intent.setClass(MainActivity.this, ResultActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	public void showResult() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
}
