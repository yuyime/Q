package com.example.qr_codescan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
	//��ά���ַ���ֵ
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
		
		//iaccount.setKeyListener(new accountInputKeyListener());
//		okButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//			}
//		});
		//mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
//		
		//�����ť��ת����ά��ɨ����棬�����õ���startActivityForResult��ת
		//ɨ������֮������ý���
//		
//		mButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//			}
//		});
	}
	
	
//	@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//		case SCANNIN_GREQUEST_CODE:
//			if(resultCode == RESULT_OK){
//				Bundle bundle = data.getExtras();
//				//��ʾɨ�赽������
//				mTextView.setText(bundle.getString("result"));
//				//��ʾ
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
//			}
//			break;
//		}
//    }	
	
	
	class accountInputKeyListener implements KeyListener{
		
		@Override
		public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onKeyOther(View view, Editable text, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onKeyDown(View view, Editable text, int keyCode,
				KeyEvent event) {
//			if(!iaccount.getText().toString().equals("")&&!ipass.getText().equals("")){
//				okButton.setEnabled(true);
//			}
			return false;
		}
		
		@Override
		public int getInputType() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void clearMetaKeyState(View view, Editable content, int states) {
			// TODO Auto-generated method stub
			
		}
	}
}
