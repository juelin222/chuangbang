package chuangbang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LegalAdvice extends Activity implements OnClickListener{
	private ImageButton ibBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal_advice);
		
		
		ibBack=(ImageButton)findViewById(R.id.ib_legal_back);
		ibBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		finish();
		
	}
	
	
	

}
