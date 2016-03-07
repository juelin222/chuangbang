package chuangbang.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.callback.Callback;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import chuangbang.entity.InvestorInfo;
import chuangbang.entity.Meeting;
import chuangbang.entity.Project;
import chuangbang.entity.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InvestorDetailsActivity extends Activity implements OnClickListener {
	
	private Button btnSend;
	private List<Project> pro;
	private InvestorInfo ii;
	private TextView tvName,tvCompanyName,tvPosition,tvDomain,tvInvestmentstage,tvDescription;
	private ImageView ivAvater;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_investor_detail);
		initView();
		Intent intent = getIntent();
		ii = (InvestorInfo) intent.getSerializableExtra("investor");
		Log.i("investor", "ii:"+ii.getOwner().toString());
		user=ii.getOwner();
		pro = new ArrayList<Project>();
		setInvestorView();
		queryProData();
		
	}
	
	private void setInvestorView(){
		tvName.setText(user.getNickName());
		tvCompanyName.setText(user.getWorkingCompany());
		tvPosition.setText(user.getWorkingPosition());
		tvDomain.setText(ii.getInvestmentDomain());
		tvInvestmentstage.setText(ii.getInvestmentStage());
		tvDescription.setText(user.getDescription());
	}

	private void initView() {
		btnSend = (Button) findViewById(R.id.btn_activity_investor_send);
		tvName=(TextView)findViewById(R.id.tv_activity_investor_name);
		tvCompanyName=(TextView)findViewById(R.id.tv_activity_investor_companyname);
		tvPosition=(TextView)findViewById(R.id.tv_activity_investor_position);
		tvDomain=(TextView)findViewById(R.id.tv_activity_investor_investmentdomain);
		tvDescription=(TextView)findViewById(R.id.tv_activity_investor_description);
		tvInvestmentstage=(TextView)findViewById(R.id.tv_activity_investor_investmentstage);
		ivAvater=(ImageView)findViewById(R.id.iv_activity_investor_avater);
		btnSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_activity_investor_send:
			doSQLQuery();
			break;

		}
		
	}
	
	/**
	 * 查询当前用户发起约谈的次数
	 */
	private void doSQLQuery(){
		BmobQuery<Meeting> query = new BmobQuery<Meeting>();
		List<BmobQuery<Meeting>> and = new ArrayList<BmobQuery<Meeting>>();
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf0.format(new Date());
		
		//大于00：00：00
		BmobQuery<Meeting> q1 = new BmobQuery<Meeting>();
		String start = time+" 00:00:00";  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date  = null;
		try {
			date = sdf.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
		and.add(q1);
		//小于23：59：59
		BmobQuery<Meeting> q2 = new BmobQuery<Meeting>();
		String end = time+" 23:59:59"; 
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date1  = null;
		try {
			date1 = sdf1.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
		and.add(q2);
		//申请人是当前用户
		BmobQuery<Meeting> q3 = new BmobQuery<Meeting>();
		
		User currentUser = BmobUser.getCurrentUser(this, User.class);
		q3.addWhereEqualTo("applyUser", currentUser);
		and.add(q3);
		//添加复合与查询
		query.and(and);
		query.findObjects(this, new FindListener<Meeting>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("send", "查询失败："+arg0);
			}

			@Override
			public void onSuccess(List<Meeting> mes) {
				// TODO Auto-generated method stub
				Log.i("send", "查询成功："+mes.size());
				if(mes.size()<3){
					sendMeeting();
					
				}else{
					Toast.makeText(InvestorDetailsActivity.this, "今天你的约谈已经3次了，等明天在约谈", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	protected void sendMeeting() {
		// TODO Auto-generated method stub
		final User user = BmobUser.getCurrentUser(this, User.class);
		LinearLayout ll = (LinearLayout) LayoutInflater.from(InvestorDetailsActivity.this).inflate(R.layout.dialog_view, null);
		TextView tvInvestor1 = (TextView) ll.findViewById(R.id.tv_dialog_text01);
		TextView tvInvestor2 = (TextView) ll.findViewById(R.id.tv_dialog_text02);
		TextView tvInvestor3 = (TextView) ll.findViewById(R.id.tv_dialog_text03);
		if(pro.size()==1){
			tvInvestor1.setText(pro.get(0).getName());
			tvInvestor2.setVisibility(View.GONE);
			tvInvestor3.setVisibility(View.GONE);
		}
		if(pro.size()==2){
			tvInvestor1.setText(pro.get(0).getName());
			tvInvestor2.setText(pro.get(1).getName());
			tvInvestor3.setVisibility(View.GONE);
		}
			
		if(pro.size()==3){
			tvInvestor1.setText(pro.get(0).getName());
			tvInvestor2.setText(pro.get(1).getName());
			tvInvestor3.setText(pro.get(2).getName());
		}
		final Dialog dialog = new AlertDialog.Builder(InvestorDetailsActivity.this)
		.create();
		dialog.show();
		dialog.getWindow().setContentView(ll);
		tvInvestor1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setMeeting(user,ii.getOwner(),pro.get(0));
				dialog.dismiss();
			}
		});
		tvInvestor2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setMeeting(user,ii.getOwner(),pro.get(1));
				dialog.dismiss();
			}
		});
		tvInvestor3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setMeeting(user,ii.getOwner(),pro.get(2));
				dialog.dismiss();
			}
		});
	}

	/**
	 * 约谈
	 * @param user
	 * @param owner
	 * @param project
	 */
	protected void setMeeting(User user, User owner, Project project) {
		Meeting meeting = new Meeting();
		meeting.setApplyUser(user);
		meeting.setInviteUser(owner);
		meeting.setProject(project);
		meeting.setApplyText("");
		meeting.setState(1);
		meeting.setCreateAt(new Date());
		meeting.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.i("meeting", "已发起约谈");
				Toast.makeText(InvestorDetailsActivity.this, "已发起约谈", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("meeting", "发起约谈失败："+arg0+":"+arg1);
				Toast.makeText(InvestorDetailsActivity.this, "约谈发送失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void queryProData(){
		BmobUser user = BmobUser.getCurrentUser(this, User.class);
		BmobQuery<Project> project = new BmobQuery<Project>();
		project.addWhereEqualTo("owner", user);
		project.include("owner");
		project.setLimit(3);
		project.findObjects(this, new FindListener<Project>() {
			
			@Override
			public void onSuccess(List<Project> pros) {
				// TODO Auto-generated method stub
				Log.i("investor", "查询结果："+pros.toString());
				pro.clear();
				pro.addAll(pros);
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("investor", "查询失败："+arg0);
			}
		});
		
	}
	
}
