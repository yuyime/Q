package com.example.qr_codescan;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	private static String QRString="你已经成功接入..." ;
	private  TextView resultshow ;
	private ImageView mImageView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Button mBack = (Button) findViewById(R.id.title_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ResultActivity.this.finish();
				
			}
		});
		resultshow = (TextView) findViewById(R.id.access_result);
		resultshow.setText(QRString);
		mImageView = (ImageView) findViewById(R.id.access_result_image);
		
	}
	
}
