package chuangbang.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import chuangbang.entity.ServiceIncubator;
import chuangbang.entity.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.lidroid.xutils.DbUtils.DaoConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ApplyIncubator extends Activity implements OnClickListener{
	private TableRow trFounderName;//创始人姓名
	private TableRow trBirthDay;//出生日期
	private TableRow trCellPhone;//手机号码
	private TableRow trLocation;//所在地
	private TableRow trSchool;
	private TableRow trGradualtedTime;//毕业时间

	private TableRow trCompany;//公司
	private TableRow trTermNumberCount;
	private TableRow trSettledExpectTime;//期望入驻时间
	private TableRow trRemarks;//备注

	private TextView tvFounderName,tvBirthDay,tvCellPhone,tvLocation,tvComany,tvSchool,tvGradualtendTime,tvSettledExpectTime,tvRemarks;
	private String founderName,birthDay,cellPhone,position,school,company,gradualtedTime,settledExpectTime,remarks;
	private EditText etTermNumberCount;
	private Integer termNumberCount;
	private int year,monthOfYear,dayOfMonth,hourOfDay,minute;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String result;
	private Handler handler;
	private DatePickerDialog dateDialog;
	private OnDateSetListener callback;
	private TimePickerDialog timeDialog;
	private Button btSend;
	private Date birthDate,gradDate,excepteDate;
	private ImageButton ivBack;







	private final int CELLPHONE=117;
	private final int LOCATION=118;
	private final int SCHOOL=119;
	private final int COMPANY=120;
	private final int COUNT=121;
	private final int REMARKS=122;
	private final int FOUNDERNAME=123;
	private final int BIRTHDAY=124;
	private final int GRADUATIME=125;//毕业时间
	private final int EXPECTIONTIME=126;//期望进入孵化器时间



	private User currentUser;



	private void setView(){
		trFounderName=(TableRow)findViewById(R.id.tr_set_founder_name);
		trBirthDay=(TableRow)findViewById(R.id.tr_set_year_of_birth);
		trCellPhone=(TableRow)findViewById(R.id.tr_set_phone_number);
		trLocation=(TableRow)findViewById(R.id.tr_set_now_address);
		trSchool=(TableRow)findViewById(R.id.tr_set_graduate_school);
		trGradualtedTime=(TableRow)findViewById(R.id.tr_set_graduate_time);
		trCompany=(TableRow)findViewById(R.id.tr_set_company_name);
		trTermNumberCount=(TableRow)findViewById(R.id.tr_set_team_number);
		trSettledExpectTime=(TableRow)findViewById(R.id.tr_set_expected_time_to_be_settled);

		tvFounderName=(TextView)findViewById(R.id.tv_set_mine_nico);
		tvBirthDay=(TextView)findViewById(R.id.tv_set_year_of_birth);
		tvCellPhone=(TextView)findViewById(R.id.tv_set_phone_number);
		tvLocation=(TextView)findViewById(R.id.tv_set_now_address);
		tvSchool=(TextView)findViewById(R.id.tv_set_graduate_school);
		tvGradualtendTime=(TextView)findViewById(R.id.tv_set_graduate_time);
		tvComany=(TextView)findViewById(R.id.tv_set_company_name);
		etTermNumberCount=(EditText)findViewById(R.id.et_set_team_number);
		tvSettledExpectTime=(TextView)findViewById(R.id.tv_set_expected_time_to_be_settled);
		btSend=(Button)findViewById(R.id.btn_commit);
		ivBack=(ImageButton)findViewById(R.id.ib_incubator_back);
	}
	private void setOnClick(){
		trFounderName.setOnClickListener(this);
		trBirthDay.setOnClickListener(this);
		trCellPhone.setOnClickListener(this);
		trLocation.setOnClickListener(this);
		trSchool.setOnClickListener(this);
		trGradualtedTime.setOnClickListener(this);
		trCompany.setOnClickListener(this);
		trTermNumberCount.setOnClickListener(this);
		trSettledExpectTime.setOnClickListener(this);
		btSend.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	private void setDatePickDialog(String title,final int point){
		dateDialog=new DatePickerDialog(ApplyIncubator.this, callback, year, monthOfYear, dayOfMonth);
		dateDialog.setTitle(title);
		dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				Log.i("time", "日期"+dateDialog.getDatePicker().getYear());
				Calendar ca=Calendar.getInstance();
				ca.set(Calendar.YEAR,dateDialog.getDatePicker().getYear());
				ca.set(Calendar.MONTH,dateDialog.getDatePicker().getMonth());
				ca.set(Calendar.DAY_OF_MONTH, dateDialog.getDatePicker().getDayOfMonth());
				String da=sdf.format(ca.getTime());
				handler.obtainMessage(point,da).sendToTarget();
				//	handler.obtainMessage(1221, ca.getTime()).sendToTarget();
			}
		});
		dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dateDialog.cancel();

			}
		});

		callback=new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {


			}
		};
		dateDialog.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_incubator);
		setView();
		setOnClick();
		getCurrentDate();
		currentUser=BmobUser.getCurrentUser(ApplyIncubator.this,User.class);
		//setDatePickDialog();

		handler=new InnerHandler();
	}

	/**
	 * 消息处理
	 * @author Administrator
	 *
	 */
	private class InnerHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			//创始人姓名
			case FOUNDERNAME:
				founderName=msg.obj.toString();
				tvFounderName.setText(founderName);
				break;
				//接受手机号码
			case CELLPHONE:

				cellPhone=msg.obj.toString();
				tvCellPhone.setText(cellPhone);
				break;
				//所在地
			case LOCATION:
				position=msg.obj.toString();
				tvLocation.setText(position);
				break;
			case SCHOOL:
				school=msg.obj.toString();
				tvSchool.setText(school);
				break;
			case COMPANY:

				company=msg.obj.toString();
				tvComany.setText(company);
				break;
				//团队人数
//			case COUNT:
//				termNumberCount=(Integer)msg.obj;
//				tvTermNumberCount.setText(termNumberCount);
//				break;
				//备注
			case REMARKS:
				remarks=msg.obj.toString();
				tvRemarks.setText(remarks);
				break;
			case BIRTHDAY:
				birthDay=msg.obj.toString();
				tvBirthDay.setText(birthDay);
				break;
			case GRADUATIME:
				gradualtedTime=msg.obj.toString();
				tvGradualtendTime.setText(gradualtedTime);
				break;
			case EXPECTIONTIME:
				settledExpectTime=msg.obj.toString();
				tvSettledExpectTime.setText(settledExpectTime);
				break;
			}
		}
	}



	/*
	 * 对话框
	 */
	private void dialogShow(String title,final int point){

		LayoutInflater inflater=LayoutInflater.from(this);
		LinearLayout ll=(LinearLayout)inflater.inflate(R.layout.view_dialog, null);
		TextView tv=(TextView)ll.findViewById(R.id.tv_dialog_title);
		tv.setText(title);
		final Dialog dialog=new AlertDialog.Builder(ApplyIncubator.this).create();
		dialog.show();
		dialog.getWindow().setContentView(ll);


		final EditText et=(EditText)ll.findViewById(R.id.et_edit);
		et.setInputType(InputType.TYPE_CLASS_TEXT);
		Button bt1=(Button)ll.findViewById(R.id.bt_left);//左边的按钮
		Button bt2=(Button)ll.findViewById(R.id.bt_right);

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//确定按钮的返回值
				result=et.getText().toString();
				handler.obtainMessage(point, result).sendToTarget();
				dialog.dismiss();

			}
		});

		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();


			}
		});

	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tr_set_founder_name:
			dialogShow("请输入您的姓名", FOUNDERNAME);
			break;

		case R.id.tr_set_year_of_birth:

			setDatePickDialog("请选择出生日期",BIRTHDAY);
			break;
		case R.id.tr_set_phone_number:
			dialogShow("请输入手机号", CELLPHONE);
			break;
		case R.id.tr_set_now_address:
			dialogShow("请输入所在城市", LOCATION);
			break;
		case R.id.tr_set_graduate_school:
			dialogShow("请输入毕业学校", SCHOOL);
			break;
		case R.id.tr_set_graduate_time:
			setDatePickDialog("请选择毕业时间", GRADUATIME);
			break;
		case R.id.tr_set_company_name:
			dialogShow("请输入公司名称", COMPANY);
			break;
		case R.id.tr_set_team_number:
			dialogShow("请输入团队人数", COUNT);
			break;
		case R.id.tr_set_expected_time_to_be_settled:
			setDatePickDialog("请选择期望进驻时间", EXPECTIONTIME);
			break;
		case R.id.btn_commit:
			sendTo();
			break;
		case R.id.ib_incubator_back:
			finish();
			break;
		}

	}
	/*
	 * 获取当前系统的的年月日
	 */
	public void getCurrentDate(){
		Calendar c=Calendar.getInstance();


		year=c.get(Calendar.YEAR);
		monthOfYear=c.get(Calendar.MONTH);
		dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
		hourOfDay=c.get(Calendar.HOUR_OF_DAY);
		minute=c.get(Calendar.MINUTE);


	}

	/*
	 * 发送
	 */
	private void sendTo(){
		ServiceIncubator cub=new ServiceIncubator();
		BmobDate daa=null;
		try {
			
			cub.setFounderName(founderName);
			cub.setContactPhone(cellPhone);
			cub.setCompanyName(company);
			cub.setRemarks(remarks);
			cub.setApplicat(currentUser);
			birthDate=sdf.parse(birthDay);
			daa=new BmobDate(birthDate);
			cub.setFounderBirth(daa);
			gradDate=sdf.parse(gradualtedTime);
			daa=new BmobDate(gradDate);
			cub.setGradualtedTime(daa);
			excepteDate=sdf.parse(settledExpectTime);
			daa=new BmobDate(excepteDate);
			cub.setSettledExpectTime(daa);
			cub.setCurrentLocation(position);
			cub.setGradualtedSchoolName(school);
			termNumberCount=Integer.parseInt(etTermNumberCount.getText().toString());
			cub.setTermNumberCount(termNumberCount);
			cub.setState(1);
			
			
			
			cub.save(ApplyIncubator.this, new SaveListener() {
			
				@Override
				public void onSuccess() {
					
					Toast.makeText(ApplyIncubator.this, "申请成功，后台人员正在处理", Toast.LENGTH_LONG).show();
					finish();

				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Log.i("favo",arg1);
				}
			});
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
