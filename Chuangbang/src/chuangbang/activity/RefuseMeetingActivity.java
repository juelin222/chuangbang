package chuangbang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class RefuseMeetingActivity extends Activity{
	
	private CheckBox  cbRefuseOne,cbRefuseTwo,cbRefuseThree,cbRefuseFour,cbRefuseFive,cbRefuseSix,cbRefuseSeven,cbRefuseEight,cbRefuseOther;
	private EditText etRefuseOther;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refuse_meeting);
		setupView();
		
	}
	/**
	 * 初始化控件
	 */
	private void setupView() {
		// TODO Auto-generated method stub
		cbRefuseOne = (CheckBox) findViewById(R.id.cb_refuse_meet_01);
		cbRefuseTwo = (CheckBox) findViewById(R.id.cb_refuse_meet_02);
		cbRefuseThree = (CheckBox) findViewById(R.id.cb_refuse_meet_03);
		cbRefuseFour = (CheckBox) findViewById(R.id.cb_refuse_meet_04);
		cbRefuseFive = (CheckBox) findViewById(R.id.cb_refuse_meet_05);
		cbRefuseSix = (CheckBox) findViewById(R.id.cb_refuse_meet_06);
		cbRefuseSeven = (CheckBox) findViewById(R.id.cb_refuse_meet_07);
		cbRefuseEight = (CheckBox) findViewById(R.id.cb_refuse_meet_08);
		cbRefuseOther = (CheckBox) findViewById(R.id.cb_refuse_meet_09);
		etRefuseOther = (EditText) findViewById(R.id.et_refuse_meet_other);
		
	}
}
