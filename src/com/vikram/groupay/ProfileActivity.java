package com.vikram.groupay;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.os.Handler;
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
import android.widget.Toast;

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
	Button btnIgnoreEvent,btnAcceptEvent;
	
	String[] groupName;
	String[] groupAdmin;
	String[] groupMembers;
	
	String[] eventName;
	String[] eventAdmin;
	String[] eventDesc;
	String[] eventGroupName;
	String[] eventVenue;
	String[] eventDate;
	String[] eventTime;
	
	ArrayAdapter<String> eventAdapter;
	int eventGlobalPosition;
	
	ArrayAdapter<String> groupMembersAdapter;
	ArrayAdapter<String> groupAdapter;
	List<NameValuePair> params1;
	List<NameValuePair> params;
	int globalPosition;
	Handler handler;
	Timer myTimer;
	TimerTask myTimerTask;
	boolean timerFlag=true;
	
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
		myTimer= new Timer();
        handler = new Handler(); 
        myTimerTask = new TimerTask() {
            public void run() { 
                handler.post(new Runnable() {
                    public void run() {
                    	if(timerFlag){
                    		//Toast.makeText(ProfileActivity.this, "test", Toast.LENGTH_SHORT).show();
                            refreshNotificationList();
                    	}
                        
                    }

                });


            }
        };
        
        myTimer.schedule(myTimerTask, 10000, 10000); 
        
		
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
			
			fisrtName.setText(user.getString("firstName").toUpperCase());
			//Log.e("UserDetails",(new Integer(user.getJSONArray("groupRequests").length())).toString());
			getGroupRequests();
			getEventRequests();
			
			//groupNotifications=(user.getJSONArray("groupRequests").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(ProfileActivity.this,GroupsActivity.class);
                regactivity.putExtra("userdata", user.toString());
                startActivity(regactivity);
                myTimer.cancel();
                //finish();
            }
        });
		
		events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(ProfileActivity.this,EventsActivity.class);
                regactivity.putExtra("userdata", user.toString());
                startActivity(regactivity);
                myTimer.cancel();
                //finish();
            }
        });
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.e("onProfileOnResume","Yes");
		updateMainView();
		
		
	}
	
	private void updateMainView(){
		myTimer= new Timer();
        handler = new Handler(); 
        myTimerTask = new TimerTask() {
            public void run() { 
                handler.post(new Runnable() {
                    public void run() {
                    	if(timerFlag){
                    		//Toast.makeText(ProfileActivity.this, "test", Toast.LENGTH_SHORT).show();
                            refreshNotificationList();
                    	}
                        
                    }

                });


            }
        };
        
        myTimer.schedule(myTimerTask, 10000, 10000); 
        
		
		try {
			ServerRequest newsr = new ServerRequest();
           	
			 	params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
		         JSONObject newjson = newsr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
		         user=newjson.getJSONObject("user");
			
			fisrtName.setText(user.getString("firstName").toUpperCase());
			//Log.e("UserDetails",(new Integer(user.getJSONArray("groupRequests").length())).toString());
			getGroupRequests();
			getEventRequests();
			
			//groupNotifications=(user.getJSONArray("groupRequests").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(ProfileActivity.this,GroupsActivity.class);
                regactivity.putExtra("userdata", user.toString());
                startActivity(regactivity);
                //finish();
            }
        });
		
		events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(ProfileActivity.this,EventsActivity.class);
                regactivity.putExtra("userdata", user.toString());
                startActivity(regactivity);
                //finish();
            }
        });
	}
	private void getPopupWindow() {
		// TODO Auto-generated method stub
		try {
			
			timerFlag=false;
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
	
	private void getEventPopupWindow() {
		// TODO Auto-generated method stub
		try {
			
			timerFlag=false;
			LayoutInflater inflater = (LayoutInflater) ProfileActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(
					R.layout.activity_eventnotification,
					(ViewGroup) findViewById(R.id.rel_popup_events));
			// Log.e("grpmembers", layout.toString());
			TextView eventsName=(TextView)layout.findViewById(R.id.tv_event_name);
			eventsName.setText(eventName[eventGlobalPosition]);
			
			TextView tv_event_desc=(TextView)layout.findViewById(R.id.tv_event_desc);
			tv_event_desc.setText(eventDesc[eventGlobalPosition]);
			
			TextView tv_grp_name=(TextView)layout.findViewById(R.id.tv_grp_name);
			tv_grp_name.setText(eventGroupName[eventGlobalPosition]);
			
			TextView tv_event_venue=(TextView)layout.findViewById(R.id.tv_event_venue);
			tv_event_venue.setText(eventVenue[eventGlobalPosition]);
			
			TextView tv_event_date=(TextView)layout.findViewById(R.id.tv_event_date);
			tv_event_date.setText(eventDate[eventGlobalPosition]);
			
			TextView tv_event_time=(TextView)layout.findViewById(R.id.tv_event_time);
			tv_event_time.setText(eventTime[eventGlobalPosition]);
			
			Display disp=getWindowManager().getDefaultDisplay();
			int width=disp.getWidth();
			int height=disp.getHeight();
			//layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			pwindow = new PopupWindow(layout, width-width/4,height-height/8, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
		
			btnIgnoreEvent = (Button) layout
					.findViewById(R.id.btnIgnoreEvent);
			btnIgnoreEvent.setOnClickListener(btn_Ignore_Event);
			
			btnAcceptEvent = (Button) layout
					.findViewById(R.id.btnAcceptEvent);
			btnAcceptEvent.setOnClickListener(btn_Accept_Event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private OnClickListener btn_Accept_Event = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("eventName", eventName[eventGlobalPosition]));
	           params.add(new BasicNameValuePair("eventAdminEmail", eventAdmin[eventGlobalPosition]));
	           params.add(new BasicNameValuePair("updateAction", "Join Event"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateEvent",params);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refreshNotificationList();
			pwindow.dismiss();
			timerFlag=true;
		}
	};
	
	private OnClickListener btn_Ignore_Event = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("eventName", eventName[eventGlobalPosition]));
	           params.add(new BasicNameValuePair("eventAdminEmail", eventAdmin[eventGlobalPosition]));
	           params.add(new BasicNameValuePair("updateAction", "Ignore Event"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateEvent",params);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refreshNotificationList();
			pwindow.dismiss();
			timerFlag=true;
		}
	};
	
	private OnClickListener btnAccept = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("groupName", groupName[globalPosition]));
	           params.add(new BasicNameValuePair("groupAdminEmail", groupAdmin[globalPosition]));
	           params.add(new BasicNameValuePair("updateAction", "Join Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refreshNotificationList();
			pwindow.dismiss();
			timerFlag=true;
		}
	};
	
	private OnClickListener button_ignore = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			
			try {
				
				ServerRequest sr = new ServerRequest();
			 	params = new ArrayList<NameValuePair>();
			 	params.add(new BasicNameValuePair("userEmail", user.getString("email")));
			 	params.add(new BasicNameValuePair("userName", user.getString("userName")));
	           params.add(new BasicNameValuePair("groupName", groupName[globalPosition]));
	           params.add(new BasicNameValuePair("groupAdminEmail", groupAdmin[globalPosition]));
	           params.add(new BasicNameValuePair("updateAction", "Delete Group"));
	           JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/updateGroup",params);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	           
	           
			refreshNotificationList();
           
			pwindow.dismiss();
			timerFlag=true;

		}
	};
	
	 void refreshNotificationList(){
		try {
        	ServerRequest sr = new ServerRequest();
        	
			 	params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", user.getString("email")));
		         JSONObject newjson = sr.getJSON(AppSettings.SERVER_IP+"/refreshUser",params);
		         user=newjson.getJSONObject("user");
		         
		         getGroupRequests();
		         getEventRequests();
	           //groupAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
	}
	


	
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
   	   groupMembers= new String[json.getJSONArray("groupMembersName").length()];
   	   for(int j=0;j<json.getJSONArray("groupMembersName").length();j++){
   		   groupMembers[j]=json.getJSONArray("groupMembersName").getString(j);
   	   }
      }
	}
	
	private void getEventRequests() throws JSONException{
		eventName= new String[user.getJSONArray("eventRequests").length()];
		eventAdmin=new String[user.getJSONArray("eventRequests").length()];
		 eventDesc=new String[user.getJSONArray("eventRequests").length()];;
		eventGroupName=new String[user.getJSONArray("eventRequests").length()];;
			eventVenue=new String[user.getJSONArray("eventRequests").length()];;
		 eventDate=new String[user.getJSONArray("eventRequests").length()];;
			eventTime=new String[user.getJSONArray("eventRequests").length()];;
		//converJsonArrayToString(user.getJSONArray("groupRequests"),groupNotifications);
		
		
		for(int i = 0; i < user.getJSONArray("eventRequests").length(); i++) {
			ServerRequest sr = new ServerRequest();
			 params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("eventToken", user.getJSONArray("eventRequests").getString(i)));
            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getEventDetails",params);
            if(json != null){
           // try{
            	//String jsonstr = json.getString("response");
            	eventName[i]=json.getString("eventName");
            	eventAdmin[i]=json.getString("eventAdminEmail");
            	eventDesc[i]=json.getString("eventDescription");
            	eventGroupName[i]=json.getString("groupName");
            	eventVenue[i]=json.getString("venue");
            	eventDate[i]=json.getString("eventDate");
            	eventTime[i]=json.getString("eventTime");
            	
            //}catch (JSONException e) {
              //  e.printStackTrace();
            //}
            }
			
		}
		
		eventAdapter= new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, eventName);
		//groupAdapter.
		list_events.setAdapter(eventAdapter);
		list_events.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("eventlist", new Integer(position).toString());
				eventGlobalPosition=position;
				getEventPopupWindow();
			}
		});
		//Log.e("UserDetails",groupNotifications[0]);
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

