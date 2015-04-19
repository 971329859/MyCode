package com.example.checkin02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.HttpUtils;
import util.JsonTools;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * @author ���ʽ� 4.7
 */
public class LoginPage extends Activity implements OnClickListener{
	private AutoCompleteTextView usernameTextView;
	private EditText passwordEditText;
	private Spinner courseSpinner;
	private Button loginButton;
	private Button resetButton;
	private ProgressDialog dialog;
	private ArrayAdapter<String> adapter;// ����������
	private ArrayAdapter<String> adapter2;// ����������
	public final static String CHECK_URL=HttpUtils.BASE_URL+"AndroidLoginAction";
	public final static String COMPLETE_URL=HttpUtils.BASE_URL+"AndroidFindUsername";
	public final static String SPINNER_URL=HttpUtils.BASE_URL+"AndroidFindCourse";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		usernameTextView = (AutoCompleteTextView) this.findViewById(R.id.auto);
		passwordEditText = (EditText) this.findViewById(R.id.passwordEdittext);
		courseSpinner = (Spinner) this.findViewById(R.id.courseSpinner);
		loginButton = (Button) this.findViewById(R.id.signinButton);
		resetButton = (Button) this.findViewById(R.id.clearButton);
		loginButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		dialog = new ProgressDialog(this);
		dialog.setTitle("��ʾ");
		dialog.setMessage("��������,���Ժ�....");
		// ��Ӱ�ȷ����ֱ�ӿ�ʼ��֤�˺�����
		passwordEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO �Զ����ɵķ������
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					login();
				}
				return false;
			}
		});
	
			new AutoTask().execute(COMPLETE_URL);
			
	}
	
	

	private void login() {
		String username = usernameTextView.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if (username == null || username.equals("")) {
			usernameTextView.setError("������ѧ��");
		} else if (password == null || password.equals("")) {
			passwordEditText.setError("����������");
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", username);
			params.put("password", password);
			try {
				String result = new myAsyTask().execute(params).get();
				System.out.println("------>>result"+result);
				String code = JsonTools.getLoginCode(result);
				System.out.println("------>>code"+code);

				if (code.trim().equals("1")) {
					Toast.makeText(LoginPage.this, "��½�ɹ�", 1)
							.show();//��ʾ��½�ɹ�
					Intent intent = new Intent(LoginPage.this, TakePhoto.class);
					/* ͨ��Bundle����洢��װ��Ҫ���ݵ����� */  
					Bundle bundle = new Bundle();  
					/*�ַ����ַ������������ֽ����顢�������ȵȣ������Դ�*/ 
					bundle.putString("username", username);
					bundle.putString("course", courseSpinner.getSelectedItem().toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}else {
					Toast.makeText(LoginPage.this, "�˺��������", 1).show();
				}

			} catch (Exception e) {
				// TODO: handle exception
				// if�е���������Ҳ����catch��ִ��
//				Toast.makeText(LoginPage.this, "�˺��������", 1).show();
				e.printStackTrace();
			}

		}
	}

	// �����˺�������֤���첽�����Toast��ʾ
	class myAsyTask extends AsyncTask<Map<String, String>, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO �Զ����ɵķ������
			super.onPreExecute();
			
			dialog.show();
		}

		@Override
		protected String doInBackground(Map<String, String>... params) {
			// TODO �Զ����ɵķ������
			System.out.println("-------->>doInBackground");
			Map<String, String> map = params[0];
			//��ʽת��Map<String, String>----->>Map<String, Object>
			Map<String, Object> map2=new HashMap<String, Object>();
			map2.put("username", map.get("username"));
			map2.put("password", map.get("password"));
			
			String result = HttpUtils.sendPostMethod(CHECK_URL, map2,
					"utf-8");
			System.out.println("-----doInBackground---->>result"+result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO �Զ����ɵķ������
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

	class AutoTask extends AsyncTask<String, Void, Map<String, List<String>>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Map<String, List<String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<String> list,list2 = null;

			String jsonString = HttpUtils.PostToComplete(COMPLETE_URL, "utf-8");
			 System.out.println("----->>"+jsonString);
			 list = JsonTools.usernameList(jsonString);
			System.out.println("---list-->>"+list);
			
			String jsonString2 = HttpUtils.PostToComplete(SPINNER_URL, "utf-8");
			 System.out.println("----->>"+jsonString2);
			 list2 = JsonTools.CourseList(jsonString2);
			System.out.println("---list-->>"+list2);
			Map<String, List<String>> map=new HashMap<String, List<String>>();
			map.put("list", list);
			map.put("list2", list2);
			return map;
		}

		@Override
		protected void onPostExecute(Map<String, List<String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			List<String>list,list2=null;
			list=(List<String>)result.get("list");
			list2=(List<String>)result.get("list2");
			adapter = new ArrayAdapter<String>(LoginPage.this, android.R.layout.simple_list_item_1, list);
			usernameTextView.setAdapter(adapter);//����������Զ���䴰��������Դ�İ�
			adapter2 = new ArrayAdapter<String>(LoginPage.this, android.R.layout.simple_spinner_dropdown_item, list2);
			courseSpinner.setAdapter(adapter2);
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id .signinButton:
			login();
			break;
		case R.id.clearButton:
			usernameTextView.setText("");
			passwordEditText.setText("");
			break;
		}
		
	}
}

