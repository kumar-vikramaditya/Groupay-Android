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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
	
	Button btnAcceptGrp;
	
	
	String[] groupName;
	String[] groupMembers;
	
	
	String[] groupAdmin;
	ArrayAdapter<String> groupMembersAdapter;
	ArrayAdapter<String> groupAdapter;
	List<NameValuePair> params1;
	List<NameValuePair> params;
	int globalPosition;
	
	
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
			fisrtName.setText(user.getString("firstName").toUpperCase());
			//Log.e("UserDetails",(new Integer(user.getJSONArray("groupRequests").length())).toString());
			getGroupRequests();
			
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
			TextView grpName=(TextView)layout.findViewById(R.id.tv_grpname);
			grpName.setText(groupName[globalPosition]);
			
			ListView grpmembers = (ListView) layout
					.findViewById(R.id.list_grpmembers);
			
			getGroupMembers();
			
           groupMembersAdapter = new ArrayAdapter<String>(
					ProfileActivity.this,
					android.R.layout.simple_list_item_1, groupMembers);
			// Log.e(tag, msg);
			grpmembers.setAdapter(groupMembersAdapter);
			Display disp=getWindowManager().getDefaultDisplay();
			int width=disp.getWidth();
			int height=disp.getHeight();
			//layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			pwindow = new PopupWindow(layout, width-width/4,height-height/8, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			btnIgnoreGrp = (Button) layout
					.findViewById(R.id.btnIgnoreGrp);
			btnIgnoreGrp.setOnClickListener(button_ignore);
			
			btnAcceptGrp = (Button) layout
					.findViewById(R.id.btnAcceptGrp);
			btnAcceptGrp.setOnClickListener(btnAccept);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private OnClickListener btnAccept = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			
            try {
        	ServerRequest sr = new ServerRequest();
		 	params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair("groupName", groupName[globalPosition]));
           params.add(new BasicNameValuePair("groupAdminEmail", groupAdmin[globalPosition]));
			params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 params.add(new BasicNameValuePair("updateAction", "Join Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
	           
	           
			 	params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
		         JSONObject newjson = sr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
		         user=newjson.getJSONObject("user");
		         
		         getGroupRequests();
	           //groupAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
			pwindow.dismiss();

		}
	};

	
	private OnClickListener button_ignore = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			
            try {
        	ServerRequest sr = new ServerRequest();
		 	params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair("groupName", groupName[globalPosition]));
           params.add(new BasicNameValuePair("groupAdminEmail", groupAdmin[globalPosition]));
			params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 params.add(new BasicNameValuePair("updateAction", "Ignore Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
	           
	           
			 	params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
		         JSONObject newjson = sr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
		         user=newjson.getJSONObject("user");
		         
		         getGroupRequests();
	           groupAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
			pwindow.dismiss();

		}
	};

	
	private void getGroupRequests() throws JSONException{
		groupName= new String[user.getJSONArray("groupRequests").length()];
		groupAdmin=new String[user.getJSONArray("groupRequests").length()];
		//converJsonArrayToString(user.getJSONArray("groupRequests"),groupNotifications);
		
		
		for(int i = 0; i < user.getJSONArray("groupRequests").length(); i++) {
			ServerRequest sr = new ServerRequest();
			 params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groupRequests").getString(i)));
            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
            if(json != null){
           // try{
            	//String jsonstr = json.getString("response");
            	groupName[i]=json.getString("groupName");
            	groupAdmin[i]=json.getString("groupAdmin");
            //}catch (JSONException e) {
              //  e.printStackTrace();
            //}
            }
			
		}
		
		groupAdapter= new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groupName);
		//groupAdapter.
		list_groups.setAdapter(groupAdapter);
		list_groups.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("list", new Integer(position).toString());
				globalPosition=position;
				getPopupWindow();
			}
		});
		//Log.e("UserDetails",groupNotifications[0]);
	}
	
	private void getGroupMembers() throws JSONException{
		ServerRequest sr = new ServerRequest();
		 params1 = new ArrayList<NameValuePair>();
       params1.add(new BasicNameValuePair("groupToken", user.getJSONArray("groupRequests").getString(globalPosition)));
      JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params1);
      if(json!=null){
   	   groupMembers= new String[json.getJSONArray("groupMembers").length()];
   	   for(int j=0;j<json.getJSONArray("groupMembers").length();j++){
   		   groupMembers[j]=json.getJSONArray("groupMembers").getString(j);
   	   }
      }
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
