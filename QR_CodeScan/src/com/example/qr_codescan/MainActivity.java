package com.example.qr_codescan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	//account 
	private EditText iaccount ;
	//pass
	private EditText ipass ;
	//okButton
	private Button okButton ;
	//二维码字符串值
	private String QRString ;
	
	//private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_main2);
		
		iaccount = (EditText) findViewById(R.id.account);
		ipass = (EditText) findViewById(R.id.pass);
		okButton = (Button) findViewById(R.id.okbutton);
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
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
	}
	
	
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
				System.out.println("yuyi111");
				String iaccountString=iaccount.getText().toString();
				String ipassString=ipass.getText().toString();
				if(iaccountString.equals("")||ipassString.equals("")){
					okButton.setEnabled(false);
				}else {
					okButton.setEnabled(true);
					System.out.println("yuyi");
				}
				
			}
		}
}
