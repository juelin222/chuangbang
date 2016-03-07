package chuangbang.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import chuangbang.entity.Project;
import chuangbang.entity.User;
import chuangbang.util.Final;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class NewMyProject extends Activity implements OnClickListener,Final{
	private TableRow trProLogo;
	private ImageView logoImage;
	
	private Handler handler;
	private String result,proName,proState,proDomain,proDescString;
	private EditText etProName,etProState,etProDomain,etProDescription;
	
	private BroadcastReceiver receiver;
	private Bitmap image;
	private Button btBack,btSave;
	private File logoPath;//logo本地地址
	private String logoName;//logo文件名
	private String logoUrl;//logo的在网络上的url
	private User user;
	private BmobFile bmobFile;




	private void setView(){
		etProName=(EditText)findViewById(R.id.et_new_pro_name);
		etProState=(EditText)findViewById(R.id.et_new_pro_statu);
		etProDomain=(EditText)findViewById(R.id.et_new_pro_domain);
		etProDescription=(EditText)findViewById(R.id.et_new_pro_description);
		trProLogo=(TableRow)findViewById(R.id.tr_new_pro_logo);
		btBack=(Button)findViewById(R.id.bt_new_pro_back);
		btSave=(Button)findViewById(R.id.bt_new_pro_save);
		logoImage=(ImageView)findViewById(R.id.iv_new_pro_logo);





	}
	private void setOnClick(){
	
		trProLogo.setOnClickListener(this);
		btSave.setOnClickListener(this);
		btBack.setOnClickListener(this);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		setView();
		setOnClick();
	
		user=BmobUser.getCurrentUser(this,User.class);



		/*
		 * 注册广播接受者
		 */
		receiver=new InnerReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(INTENT_ACTION_CARD);

		registerReceiver(receiver, filter);


	}

	/**
	 * 按钮监听
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
	
		case R.id.tr_new_pro_logo:
			Intent intent=new Intent(NewMyProject.this,MenuDialog.class);

			startActivity(intent);
			break;


			/*
			 * 发布项目按钮
			 */
		case R.id.bt_new_pro_save:
			if(image==null){
				updatePro();
			}else{
				uploadLogo();

			}
			break;
		case R.id.bt_new_pro_back:
			finish();
			break;
		}

	}



	/*
	 * 广播接受
	 */
	private class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(INTENT_ACTION_CARD.equals(action)){
				image=intent.getParcelableExtra(INTENT_EXTRA_CARD);
				Log.i("IMAGES",image.toString());
				logoImage.setImageBitmap(image);
				saveBitmap(image);

			}

		}
	}

	private void saveBitmap(Bitmap bitmap){
		logoName=System.currentTimeMillis()+".jpg";
		logoPath=new File(Environment.getExternalStorageDirectory(),"chuangbang/"+logoName);
		FileOutputStream out=null;
		try {
			File destDir = new File(Environment.getExternalStorageDirectory(),"chuangbang");
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			out=new FileOutputStream(logoPath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

			out.close();
			out.flush();
		}catch (IOException e) {				
			e.printStackTrace();
		}

	}

	/**
	 * 上传logo到bmob服务器
	 */
	private void uploadLogo(){
		String path=logoPath.getAbsolutePath();
		Log.i("Pro", "logo的本地路径"+path);
		//String path=Environment.getExternalStorageDirectory().getPath()+"/chuangbang/"+logoName;
		//Log.i("Avater", "头像在本地的地址"+path);
		BTPFileResponse response = BmobProFile.getInstance(NewMyProject.this).upload(path, new UploadListener() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				//Toast.makeText(NewMyProject.this, "上传成功", Toast.LENGTH_LONG).show();
				bmobFile=arg2;

				updatePro();
			}

			@Override
			public void onProgress(int arg0) {


			}
		});


	}
	/**
	 * 发布项目到BMOB
	 */
	private void updatePro(){
		Project pro=new Project();
		proName=etProName.getText().toString();
		proState=etProState.getText().toString();
		proDescString=etProDescription.getText().toString();
		proDomain=etProDomain.getText().toString();
		pro.setName(proName);
		pro.setDescription(proDescString);
		pro.setDomain(proDomain);
		pro.setState(proState);
		if(bmobFile!=null){

			pro.setLogo(bmobFile);
		}
		pro.setOwner(user);

		pro.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				//finish();
				//logoPath.delete();
				Toast.makeText(NewMyProject.this, "项目发布成功",Toast.LENGTH_LONG).show();
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.i("Pro",arg1);

			}
		});
	}









	private void dialogShow(String title,final int point){

		LayoutInflater inflater=LayoutInflater.from(this);
		LinearLayout ll=(LinearLayout)inflater.inflate(R.layout.view_dialog, null);
		TextView tv=(TextView)ll.findViewById(R.id.tv_dialog_title);
		tv.setText(title);
		final Dialog dialog=new AlertDialog.Builder(NewMyProject.this).create();
		dialog.show();
		dialog.getWindow().setContentView(ll);


		final EditText et=(EditText)ll.findViewById(R.id.et_edit);

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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}



}
