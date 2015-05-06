package com.vikram.groupay;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GroupsActivity extends Activity{
	JSONObject user;
	String[] groupsAdded;
	String[] groupsAdmin;
	Button createGroup;
	ArrayAdapter<String> groupAdapter;
	List<NameValuePair> params;
	ListView list_usergroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		list_usergroups=(ListView) findViewById(R.id.list_usergroups);
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
			groupsAdded= new String[user.getJSONArray("groups").length()];
			groupsAdmin=new String[user.getJSONArray("groups").length()];
			
			for(int i = 0; i < user.getJSONArray("groups").length(); i++) {
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groups").getString(i)));
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
	            if(json != null){
	            	groupsAdded[i]=json.getString("groupName")+"  ("+ json.getString("groupAdminName")+")";
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            }
				
			}
			//Log.e("NEWLOG",groupsAdded.toString());
			groupAdapter= new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, groupsAdded);
			//groupAdapter.
			list_usergroups.setAdapter(groupAdapter);
			//groupNotifications=(user.getJSONArray("groupRequests").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case android.R.id.home:
			//Log.e("GroupActivity", "YESS");
			NavUtils.navigateUpFromSameTask(GroupsActivity.this);
			return true;

		}
		return super.onOptionsItemSelected(item);

	}

	 @Override
	   public void onBackPressed() {
	      moveTaskToBack(true); 
	      GroupsActivity.this.finish();
	   }
}
