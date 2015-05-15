package com.vikram.groupay;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;

public class CreategroupActivity extends Activity {
	// JSONObject user;
	ListView listmembers, selected_members;
	Button btnadd, btncancel;
	Button createGrp, cancelCreateGrp;
	EditText et_grpname;
	PopupWindow pwindow;
	ArrayAdapter<String> adapter_list;
	JSONObject user;
	List<NameValuePair> params;
	String[] val_list;
	String[] val_list_email;
	ArrayAdapter<String> adapter_selected;
	String[] outputStrArr;
	String[] outputStrArrEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);


		btnadd = (Button) findViewById(R.id.btnAddMembers);
		selected_members = (ListView) findViewById(R.id.selected_members);
		createGrp = (Button) findViewById(R.id.createGrp);
		cancelCreateGrp = (Button) findViewById(R.id.cancelCreateGrp);
		et_grpname = (EditText) findViewById(R.id.et_grpname);

		try {
			user = new JSONObject(getIntent().getStringExtra("userdata"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Bundle b = getIntent().getExtras(); String[] resultArr =
		 * b.getStringArray("selectedItems"); selected_members = (ListView)
		 * findViewById(R.id.selected_members);
		 * 
		 * ArrayAdapter<String> adapter_selected = new
		 * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
		 * resultArr); selected_members.setAdapter(adapter_selected);
		 */
		btnadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				getPopupWindow();

			}

		});

		cancelCreateGrp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();

			}

		});

		createGrp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.e("groupName", et_grpname.getText().toString());
				// Log.e("groupMemberRequestName",outputStrArr.toString());
				try {
					ServerRequest sr = new ServerRequest();
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("groupName", et_grpname
							.getText().toString()));
					params.add(new BasicNameValuePair("groupAdminEmail", user
							.getString("email")));
					params.add(new BasicNameValuePair("groupAdminName", user
							.getString("firstName")
							+ " "
							+ user.getString("lastName")));
					for (int j = 0; j < outputStrArrEmail.length; j++) {
						Log.e("groupMemberRequestNameinloopss",
								outputStrArrEmail[j]);
						params.add(new BasicNameValuePair(
								"groupMemberRequestEmail", outputStrArrEmail[j]));
						params.add(new BasicNameValuePair(
								"groupMemberRequestName", outputStrArr[j]));
					}

					JSONObject json = sr.getJSON(AppSettings.SERVER_IP
							+ "/createGroup", params);
					if (json != null) {
						// groupsAdded[i]=json.getString("groupName")+"  ("+
						// json.getString("groupAdminName")+")";
						// groupsAdmin[i]=json.getString("groupAdmin");
						Log.e("GroupCreatedSuccess", json.toString());
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	private void getPopupWindow() {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = (LayoutInflater) CreategroupActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.activity_memberslist,
					(ViewGroup) findViewById(R.id.rel_popup1));
			listmembers = (ListView) layout.findViewById(R.id.member_list);

			ServerRequest sr = new ServerRequest();
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupToken", ""));
			JSONObject json = sr.getJSON(
					AppSettings.SERVER_IP + "/getAllUsers", params);
			int counter = 0;
			if (json != null) {
				JSONArray users = json.getJSONArray("users");
				val_list = new String[users.length() - 1];
				val_list_email = new String[users.length() - 1];

				for (int i = 0; i < users.length(); i++) {
					// Log.e("RequestGroupMembers",users.getJSONObject(i).getString("userName")+";;;;;;"+user.getString("userName"));
					if (!users.getJSONObject(i).getString("userName")
							.equals(user.getString("userName"))) {
						// Log.e("inffffffffffffforrr",new
						// Integer(counter).toString());
						val_list[counter] = users.getJSONObject(i).getString(
								"userName");
						val_list_email[counter] = users.getJSONObject(i)
								.getString("email");
						counter++;
					}

				}
			}

			adapter_list = new ArrayAdapter<String>(CreategroupActivity.this,
					android.R.layout.simple_list_item_multiple_choice, val_list);
			listmembers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listmembers.setAdapter(adapter_list);
			Display disp = getWindowManager().getDefaultDisplay();
			int width = disp.getWidth();
			int height = disp.getHeight();

			pwindow = new PopupWindow(layout, width - width / 4, height
					- height / 4, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

			btncancel = (Button) layout.findViewById(R.id.btnCancelSelection);
			btncancel.setOnClickListener(button_ignore);

			btnadd = (Button) layout.findViewById(R.id.btnConfirmSelection);
			btnadd.setOnClickListener(button_accept);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener button_ignore = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			pwindow.dismiss();

		}
	};

	private OnClickListener button_accept = new OnClickListener() {

		public void onClick(View view) {

			String[] resultArr = val_list;

			// TODO Auto-generated method stub
			SparseBooleanArray checked = listmembers.getCheckedItemPositions();
			ArrayList<String> selectedItems = new ArrayList<String>();
			ArrayList<String> selectedItemsEmail = new ArrayList<String>();
			for (int i = 0; i < checked.size(); i++) {
				// Item position in adapter
				int position = checked.keyAt(i);
				// Add sport if it is checked i.e.) == TRUE!
				if (checked.valueAt(i))
					selectedItems.add(adapter_list.getItem(position));
				selectedItemsEmail.add(val_list_email[position]);
			}

			outputStrArr = new String[selectedItems.size()];
			outputStrArrEmail = new String[selectedItems.size()];
			for (int i = 0; i < selectedItems.size(); i++) {
				outputStrArr[i] = selectedItems.get(i);
				outputStrArrEmail[i] = selectedItemsEmail.get(i);
				Log.e("SelectedItemName", outputStrArr[i]);
				Log.e("SelectedItemEmail", outputStrArrEmail[i]);
			}
			// // Log.e("SelectedItemName",outputStrArr.toString());
			// .e("SelectedItemEmail",outputStrArrEmail.toString());
			adapter_selected = new ArrayAdapter<String>(
					CreategroupActivity.this,
					android.R.layout.simple_list_item_1, outputStrArr);
			selected_members.setAdapter(adapter_selected);
			/*
			 * Intent intent = new Intent(getApplicationContext(),
			 * CreategroupActivity.class);
			 * 
			 * // Create a bundle object Bundle b = new Bundle();
			 * b.putStringArray("selectedItems", outputStrArr);
			 * 
			 * // Add the bundle to the intent. intent.putExtras(b);
			 */

			// start the ResultActivity
			// startActivity(intent);
			pwindow.dismiss();

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// Log.e("GroupayMenu", "error");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {

		case R.id.action_logout:
			// SharedPreferences.Editor edit = pref.edit();
			// Storing Data using SharedPreferences
			// edit.putString("token", "");
			// edit.commit();
			Intent mainactivity = new Intent(CreategroupActivity.this,
					MainActivity.class);

			startActivity(mainactivity);
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);

	}

}
