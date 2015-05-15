package com.vikram.groupay;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupsActivity extends Activity{
	JSONObject user;
	String[] groupsAdded;
	String[] groupsName;
	String[] groupsAdmin;
	String[] groupMembers;
	String groupAdminName;
	String groupAdminEmail;
	Button createGroup;
	ArrayAdapter<String> groupAdapter;
	List<NameValuePair> params;
	ListView list_usergroups;
	int globalPosition;
	private ArrayAdapter<String> groupMembersAdapter;
	PopupWindow pwindow;
	Button btnDeleteGrp,btnLeaveGrp;
	
	LayoutInflater inflater;
	View layout;
	TextView grpName;
	ListView grpmembers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		list_usergroups=(ListView) findViewById(R.id.list_usergroups);
		try {
			//if(user!=null)
			user=new JSONObject(getIntent().getStringExtra("userdata"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateUserLists();
		createGroup=(Button) findViewById(R.id.btnCreateGroup);
		createGroup.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                Intent creategroupactivity = new Intent(GroupsActivity.this,CreategroupActivity.class);
	                creategroupactivity.putExtra("userdata", user.toString());
	                startActivity(creategroupactivity);
	                //finish();
	            }
	        });
	}
	
	private void updateUserLists(){
		try {
			
			groupsAdded= new String[user.getJSONArray("groups").length()];
			groupsAdmin=new String[user.getJSONArray("groups").length()];
			groupsName= new String[user.getJSONArray("groups").length()];
			for(int i = 0; i < user.getJSONArray("groups").length(); i++) {
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groups").getString(i)));
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
	            if(json != null){
	            	groupsAdded[i]=json.getString("groupName")+"  ("+ json.getString("groupAdminName")+")";
	            	groupsName[i]=json.getString("groupName");
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            }
				
			}
			//Log.e("NEWLOG",groupsAdded.toString());
			groupAdapter= new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, groupsAdded);
			//groupAdapter.
			list_usergroups.setAdapter(groupAdapter);
			
			list_usergroups.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.e("list", new Integer(position).toString());
					globalPosition=position;
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
			
			inflater = (LayoutInflater) GroupsActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(
					R.layout.activity_groupleave,
					(ViewGroup) findViewById(R.id.rel_grpleave));
			// Log.e("grpmembers", layout.toString());
			grpName =(TextView)layout.findViewById(R.id.disp_grpname);
			btnDeleteGrp=(Button)layout.findViewById(R.id.btnDeleteGrp);
			btnLeaveGrp=(Button)layout.findViewById(R.id.btnLeaveGrp);
			grpName.setText(groupsAdded[globalPosition]);
			
			grpmembers = (ListView) layout
					.findViewById(R.id.list_members);
			
			//getGroupMembers();
			//groupsAdded[globalPosition]
		
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groups").getString(globalPosition)));
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
	            if(json != null){
	            	//groupsAdded[i]=json.getString("groupName")+"  ("+ json.getString("groupAdminName")+")";
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            	groupAdminName=json.getString("groupAdminName");
	            	groupAdminEmail=json.getString("groupAdmin");
	            	groupMembers= new String[json.getJSONArray("groupMembersName").length()];
	            	   for(int j=0;j<json.getJSONArray("groupMembersName").length();j++){
	            		   groupMembers[j]=json.getJSONArray("groupMembersName").getString(j);
	            	   }
	            }
				
           groupMembersAdapter = new ArrayAdapter<String>(
					GroupsActivity.this,
					android.R.layout.simple_list_item_1, groupMembers);
			// Log.e(tag, msg);
			grpmembers.setAdapter(groupMembersAdapter);
			Display disp=getWindowManager().getDefaultDisplay();
			int width=disp.getWidth();
			int height=disp.getHeight();
			//layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			pwindow = new PopupWindow(layout, width-width/4,height-height/8, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			
			if(groupAdminName.equals(user.getString("userName"))){
				btnDeleteGrp.setVisibility(android.view.View.VISIBLE);
				btnLeaveGrp.setVisibility(android.view.View.INVISIBLE);
				btnDeleteGrp.setOnClickListener(deleteGroup);
			}else{
				btnDeleteGrp.setVisibility(android.view.View.INVISIBLE);
				btnLeaveGrp.setVisibility(android.view.View.VISIBLE);
				btnLeaveGrp.setOnClickListener(leaveGroup);
			}
			/*btnIgnoreGrp = (Button) layout
					.findViewById(R.id.btnIgnoreGrp);
			btnIgnoreGrp.setOnClickListener(button_ignore);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void refreshLists() throws JSONException{
		
	
			ServerRequest sr = new ServerRequest();
			 params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groups").getString(globalPosition)));
            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
            if(json != null){
            	//groupsAdded[i]=json.getString("groupName")+"  ("+ json.getString("groupAdminName")+")";
            	//groupsAdmin[i]=json.getString("groupAdmin");
            	groupAdminName=json.getString("groupAdminName");
            	groupAdminEmail=json.getString("groupAdmin");
            	groupMembers= new String[json.getJSONArray("groupMembersName").length()];
            	   for(int j=0;j<json.getJSONArray("groupMembersName").length();j++){
            		   groupMembers[j]=json.getJSONArray("groupMembersName").getString(j);
            	   }
            }
			
       groupMembersAdapter = new ArrayAdapter<String>(
				GroupsActivity.this,
				android.R.layout.simple_list_item_1, groupMembers);
		// Log.e(tag, msg);
		grpmembers.setAdapter(groupMembersAdapter);
	}*/
	
	private OnClickListener deleteGroup = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("groupName", groupsName[globalPosition]));
	           params.add(new BasicNameValuePair("groupAdminEmail", groupAdminEmail));
	           params.add(new BasicNameValuePair("updateAction", "Delete Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
	           
	           	ServerRequest newsr = new ServerRequest();
	           	
   			 	params = new ArrayList<NameValuePair>();
   				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
   		         JSONObject newjson = newsr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
   		         user=newjson.getJSONObject("user");
	   		      //refreshLists();
   		      updateUserLists();
	   		       
	   			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*try {
				refreshLists();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			pwindow.dismiss();
		}
	};
	
	private OnClickListener leaveGroup = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("groupName", groupsName[globalPosition]));
	           params.add(new BasicNameValuePair("groupAdminEmail", groupAdminEmail));
	           params.add(new BasicNameValuePair("updateAction", "Leave Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
	           
	           ServerRequest newsr = new ServerRequest();
	           	
  			 	params = new ArrayList<NameValuePair>();
  				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
  		         JSONObject newjson = newsr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
  		         user=newjson.getJSONObject("user");
	   		      //refreshLists();
  		       updateUserLists();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pwindow.dismiss();
		}
	};
	 
	 @Override
		public void onResume() {
		 super.onResume();
			Log.e("OnResumeGroupsActivity","Yess1");
		 try {
				//if(user!=null)
				//user=new JSONObject(getIntent().getStringExtra("userdata"));
			 	ServerRequest newsr = new ServerRequest();
	           	
			 	params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
		         JSONObject newjson = newsr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
		         user=newjson.getJSONObject("user");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateUserLists();
			
		}
	 
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			//Log.e("GroupayMenu", "error");
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
				//SharedPreferences.Editor edit = pref.edit();
	            //Storing Data using SharedPreferences
	            //edit.putString("token", "");
	            //edit.commit();
	            Intent mainactivity = new Intent(GroupsActivity.this,MainActivity.class);

	            startActivity(mainactivity);
	            finish();
				return true;

			}
			return super.onOptionsItemSelected(item);

		}
	 
	 
}
