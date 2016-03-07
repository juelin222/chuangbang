package chuangbang.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import chuangbang.adapter.MyFavoriteAdapter;
import chuangbang.entity.Project;
import chuangbang.entity.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class MyProject extends Activity{
	private List<Project> data;
	private ListView lvProject;
	private User currentUser;
	private BaseAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myproject);
		currentUser=BmobUser.getCurrentUser(this,User.class);
		lvProject=(ListView)findViewById(R.id.lv_project);
		data=new ArrayList<Project>();
		adapter=new MyFavoriteAdapter(data, MyProject.this);
		lvProject.setAdapter(adapter);
		queryMyPro();
		
	}
	private void queryMyPro(){

		BmobQuery<Project> query=new BmobQuery<Project>();
		query.addWhereEqualTo("owner",new BmobPointer(currentUser)); 
		
		query.findObjects(this, new FindListener<Project>() {
			
			@Override
			public void onSuccess(List<Project> arg0) {
				Log.i("myPro", "个数"+arg0.toString());
				data.addAll(arg0);
				//adapter.notifyDataSetChanged();
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i("favo", "错误"+arg0+arg1);
				
			}
		});
	
	}
	
	
	
	public void doClick(View v){
		Intent intent=null;
		switch (v.getId()) {
		case R.id.bt_new_project:
			intent=new Intent(MyProject.this,NewMyProject.class);
			startActivity(intent);
			break;

		case R.id.bt_back:
			finish();
			break;
		}
	}
}
