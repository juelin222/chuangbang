package chuangbang.fragment.server;

import chuangbang.activity.R;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfessionFragment extends Fragment implements OnClickListener{
	private Button btnApplicatServer;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_profession, null);
		btnApplicatServer = (Button) view.findViewById(R.id.btn_applicat_server);
		btnApplicatServer.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View arg0) {
		//TODO弹出对话框
		Toast.makeText(getActivity(), "该功能未实现", Toast.LENGTH_SHORT).show();
	}
}
