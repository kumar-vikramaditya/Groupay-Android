package com.vikram.groupay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileActivity extends Activity{
	JSONObject user;
	TextView fisrtName;
	Button groups,events;
	SharedPreferences pref;
	
	ListView list_groups;
	ListView list_events;
	PopupWindow pwindow;
	Button btnIgnoreGrp;
	ArrayAdapter<String> groupAdapter;
	String[] groupNotifications;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		groups=(Button)findViewById(R.id.btnGroups); 
		events=(Button)findViewById(R.id.btnEvents);
		fisrtName=(TextView) findViewById(R.id.profileName);
		list_groups = (ListView) findViewById(R.id.list_groups);
		list_events = (ListView) findViewById(R.id.list_events);
		
		pref = getSharedPreferences("AppPref", MODE_PRIVATE);
		 
		
		groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(ProfileActivity.this,GroupsActivity.class);
                startActivity(regactivity);
                finish();
            }
        });
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
			fisrtName.setText(user.getString("firstName"));
			//Log.e("UserDetails",(new Integer(user.getJSONArray("groupRequests").length())).toString());
			groupNotifications= new String[user.getJSONArray("groupRequests").length()];
			//converJsonArrayToString(user.getJSONArray("groupRequests"),groupNotifications);
			
			
			for(int i = 0; i < user.getJSONArray("groupRequests").length(); i++) {
				groupNotifications[i] = user.getJSONArray("groupRequests").getString(i);
			}
			//Log.e("UserDetails",groupNotifications[0]);
			groupAdapter= new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, groupNotifications);
			//groupAdapter.
			list_groups.setAdapter(groupAdapter);
			list_groups.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.e("list", "itemclicked");
					getPopupWindow();
				}
			});
			//groupNotifications=(user.getJSONArray("groupRequests").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getPopupWindow() {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = (LayoutInflater) ProfileActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(
					R.layout.activity_groupnotification,
					(ViewGroup) findViewById(R.id.rel_popup));
			// Log.e("grpmembers", layout.toString());
			ListView grpmembers = (ListView) layout
					.findViewById(R.id.list_grpmembers);
			Log.e("grpmembers", grpmembers.toString());
			String[] values2 = new String[] { "notification e 1",
					"notification e 2", "notification e 2",
					"notification e 2" };
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
					ProfileActivity.this,
					android.R.layout.simple_list_item_1, values2);
			// Log.e(tag, msg);
			grpmembers.setAdapter(adapter2);
			pwindow = new PopupWindow(layout, 300, 250, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			btnIgnoreGrp = (Button) layout
					.findViewById(R.id.btnIgnoreGrp);
			btnIgnoreGrp.setOnClickListener(button_ignore);
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

	
	private void converJsonArrayToString(JSONArray jsonArray, String[] groupNotifications){
		String jsonString = jsonArray.toString();
		jsonString.replace("},{", " ,");
		groupNotifications = jsonString.split(" ");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.e("GroupayMenu", "error");
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
			SharedPreferences.Editor edit = pref.edit();
            //Storing Data using SharedPreferences
            edit.putString("token", "");
            edit.commit();
            Intent mainactivity = new Intent(ProfileActivity.this,MainActivity.class);

            startActivity(mainactivity);
            finish();
			return true;

		}
		return super.onOptionsItemSelected(item);

	}

}
