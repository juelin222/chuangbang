package chuangbang.activity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import chuangbang.entity.ServiceAccounting;
import chuangbang.entity.User;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MakeAccount extends Activity implements OnClickListener{
	private EditText etName,etPhoneNumber,etCompanyName,etCompanyNature,etBusinessScore;
	private String name,phoneNumber,companyName,companyNature,businessScore;
	private Button btnCommit;
	private ImageButton ivBack;
	private User currentUser;
	
	private void setView(){
		etName=(EditText)findViewById(R.id.et_set_mine_nico);
		etPhoneNumber=(EditText)findViewById(R.id.et_set_phone_number);
		etCompanyName=(EditText)findViewById(R.id.et_set_company_name);
		etCompanyNature=(EditText)findViewById(R.id.et_account_set_nature_of_company);
		etBusinessScore=(EditText)findViewById(R.id.tv_set_acount_business_scope);
		btnCommit=(Button)findViewById(R.id.btn_make_acount_com);
		ivBack=(ImageButton)findViewById(R.id.ib_acount_back);
	}
	
	private void setOnClick(){
		btnCommit.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_account);
		setView();
		setOnClick();
		currentUser=BmobUser.getCurrentUser(MakeAccount.this,User.class);
	}
	
	
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ib_acount_back:
			finish();
			break;

		case R.id.btn_make_acount_com:
			commit();
			break;
		}
		
	}
	
	private void commit(){
		name=etName.getText().toString();
		phoneNumber=etPhoneNumber.getText().toString();
		companyName=etCompanyName.getText().toString();
		companyNature=etCompanyNature.getText().toString();
		businessScore=etBusinessScore.getText().toString();
		ServiceAccounting acout=new ServiceAccounting();
		acout.setCompanyName(companyName);
		acout.setContactName(name);
		acout.setContactPhone(phoneNumber);
		acout.setBusinessScope(businessScore);
		acout.setApplicant(currentUser);
		acout.setState(1);
		acout.save(MakeAccount.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(MakeAccount.this, "提交成功！后台正在处理。", Toast.LENGTH_LONG).show();
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(MakeAccount.this,arg1, Toast.LENGTH_LONG).show();
				Log.i("sp", arg1);
				
			}
		});
	}

}
