package com.vikram.groupay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class EventsActivity extends Activity {
	JSONObject user;
	Button btnevents,okbtn,btnEventSettle,btnCreateEvent;
	ListView list_upcoming_events,list_past_events;
	PopupWindow pwindow;
	String[] eventsAdded;
	String[] eventsAdmin;
	String[] eventName;
	String[] eventAdmin;
	String[] eventDesc;
	String[] eventGroupName;
	String[] eventVenue;
	String[] eventDate;
	String[] eventTime;
	//String[] eventMembers;
	
	ArrayList<JSONArray> eventMembers;
	int globalPosition;
	
	ArrayList<String> values_events_upcoming=new ArrayList<String>();
	ArrayList<String> values_events_past=new ArrayList<String>();
	ArrayList<String> adapter_list_event_members=new ArrayList<String>();
	ArrayList<String> member_List_event=new ArrayList<String>();
	List<NameValuePair> params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		btnevents = (Button) findViewById(R.id.btnCreateEvent);
		btnCreateEvent=(Button) findViewById(R.id.btnCreateEvent);
		list_upcoming_events = (ListView) findViewById(R.id.list_upcoming_events);
		list_past_events= (ListView) findViewById(R.id.list_past_events);
		
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
			eventsAdded= new String[user.getJSONArray("events").length()];
			eventsAdmin=new String[user.getJSONArray("events").length()];
			eventName= new String[user.getJSONArray("events").length()];
			eventAdmin= new String[user.getJSONArray("events").length()];
			eventDesc= new String[user.getJSONArray("events").length()];
			eventGroupName= new String[user.getJSONArray("events").length()];
			eventVenue= new String[user.getJSONArray("events").length()];
			eventDate= new String[user.getJSONArray("events").length()];
			eventTime= new String[user.getJSONArray("events").length()];
			
			//eventMembers
			for(int i = 0; i < user.getJSONArray("events").length(); i++) {
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("eventToken", user.getJSONArray("events").getString(i)));
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getEventDetails",params);
	            if(json != null){
	            	eventsAdded[i]=json.getString("eventName")+"  ("+ json.getString("eventAdminName")+")";
	            	eventDate[i]=json.getString("eventDate");
	            	eventTime[i]=json.getString("eventTime");
	            	eventName[i]=json.getString("eventName");
	            	eventAdmin[i]=json.getString("eventAdmin");
	            	eventDesc[i]=json.getString("eventDescription");
	            	eventGroupName[i]=json.getString("groupName");
	            	eventVenue[i]=json.getString("venue");
	            	eventMembers.add(i, json.getJSONArray("eventMembersName"));
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            }
				
			}
		
		Log.e("eventDateeventDateeventDate",new Integer(eventDate.length).toString());
		for(int j=0;j<eventDate.length;j++){
			if(new Date().compareTo(new Date(eventDate[j]+eventTime[j]))>0){
				values_events_upcoming.add(eventsAdded[j]);
			}else{
				values_events_past.add(eventsAdded[j]);
			}
		}
			
	

		ArrayAdapter<String> adapter_events_upcoming = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values_events_upcoming);
		
		Log.e("list_upcoming_events",list_upcoming_events.toString());
		list_upcoming_events.setAdapter(adapter_events_upcoming);
		
		list_upcoming_events.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("list", "itemclicked");
				globalPosition=position;
				getPopupWindow_upcoming();
			//	getDialogWindow();
			}

		});

		
		/*---------------------------------------------------------------------*/
		
		
		
		
		ArrayAdapter<String> adapter_events_past = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values_events_past);

		list_past_events.setAdapter(adapter_events_past);

		list_past_events.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("list", "itemclicked");
				globalPosition=position;
				getPopupWindow_past();
			//	getDialogWindow();
			}

		});
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnCreateEvent.setOnClickListener(btn_Create_Event);
	}
	
	private OnClickListener btn_Create_Event = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			Intent regactivity = new Intent(EventsActivity.this,CreateeventActivity.class);
            regactivity.putExtra("userdata", user.toString());
            startActivity(regactivity);
			//pwindow.dismiss();

		}
	};
		
	private void getPopupWindow_upcoming() {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = (LayoutInflater) EventsActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(
					R.layout.activity_eventdetails,
					(ViewGroup) findViewById(R.id.rel_popup_eventdetails));
			
			// Log.e("grpmembers", layout.toString());
			
			
			TextView tv_disp_event_name= (TextView) layout.findViewById(R.id.tv_disp_event_name);
			tv_disp_event_name.setText(eventName[globalPosition]);
			TextView tv_disp_event_desc= (TextView) layout.findViewById(R.id.tv_disp_event_desc);
			tv_disp_event_desc.setText(eventDesc[globalPosition]);
			TextView tv_disp_event_grpname= (TextView) layout.findViewById(R.id.tv_disp_event_grpname);
			tv_disp_event_grpname.setText(eventGroupName[globalPosition]);
			ListView list_eventmembers = (ListView) layout.findViewById(R.id.list_eventmembers);
			
			TextView tv_disp_event_venue= (TextView) layout.findViewById(R.id.tv_disp_event_venue);
			tv_disp_event_venue.setText(eventVenue[globalPosition]);
			TextView tv_disp_event_date= (TextView) layout.findViewById(R.id.tv_disp_event_date);
			tv_disp_event_date.setText(eventDate[globalPosition]);
			TextView tv_disp_event_time= (TextView) layout.findViewById(R.id.tv_disp_event_time);
			tv_disp_event_time.setText(eventTime[globalPosition]);
			
			
			for(int j=0;j<eventMembers.get(globalPosition).length();j++){
				adapter_list_event_members.add(eventMembers.get(globalPosition).getString(j));
			}
			ArrayAdapter<String> adapter2= new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, adapter_list_event_members);
			
			list_eventmembers.setAdapter(adapter2);
			
			pwindow = new PopupWindow(layout, 300, 250, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			okbtn = (Button) layout
					.findViewById(R.id.okbtn);
			okbtn.setOnClickListener(ok_btn);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnClickListener ok_btn = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			pwindow.dismiss();

		}
	};

	private void getPopupWindow_past() {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = (LayoutInflater) EventsActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(
					R.layout.activity_eventsettle,
					(ViewGroup) findViewById(R.id.rel_popup_eventsettle));
			TextView tv_event_name= (TextView) layout.findViewById(R.id.tv_event_name);
			tv_event_name.setText(eventName[globalPosition]);
			TextView tv_event_desc= (TextView) layout.findViewById(R.id.tv_event_desc);
			tv_event_desc.setText(eventDesc[globalPosition]);
			TextView tv_grp_name= (TextView) layout.findViewById(R.id.tv_grp_name);
			tv_grp_name.setText(eventGroupName[globalPosition]);
			//ListView list_eventmembers = (ListView) layout.findViewById(R.id.list_eventmembers);
			
			TextView tv_event_venue= (TextView) layout.findViewById(R.id.tv_event_venue);
			tv_event_venue.setText(eventVenue[globalPosition]);
			TextView tv_event_date= (TextView) layout.findViewById(R.id.tv_event_date);
			tv_event_date.setText(eventDate[globalPosition]);
			TextView tv_event_time= (TextView) layout.findViewById(R.id.tv_event_time);
			tv_event_time.setText(eventTime[globalPosition]);
			// Log.e("grpmembers", layout.toString());
			
			TextView tv_event_amt= (TextView) layout.findViewById(R.id.tv_event_amt);
			tv_event_amt.setText(eventTime[globalPosition]);
			
			pwindow = new PopupWindow(layout, 300, 250, true);
			pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
			btnEventSettle = (Button) layout
					.findViewById(R.id.btnEventSettle);
			btnEventSettle.setOnClickListener(btn_Event_Settle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private OnClickListener btn_Event_Settle = new OnClickListener() {

		public void onClick(View view) {
			// TODO Auto-generated method stub
			pwindow.dismiss();

		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_logout:
			return true;

		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(EventsActivity.this);
			return true;

		}
		return super.onOptionsItemSelected(item);

	}

}
