package com.vikram.groupay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CreateeventActivity extends Activity {
	
	JSONObject user;
	Button btnSelectDate, btnSelectTime,createEvent,cancelCreateEvent;
	EditText et_eventname,et_eventdesc,et_eventvenue;
	List<NameValuePair> params;
	ListView group_name_list;
	String[] values_group_name_list;
	ArrayList<JSONArray> eventMembers= new ArrayList<JSONArray>();
	ArrayList<JSONArray> eventMembersName= new ArrayList<JSONArray>();
	
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	public int year, month, day, hour, minute;
	private int mYear, mMonth, mDay, mHour, mMinute;
	
	int globalPosition;

	public CreateeventActivity() {
		// Assign current Date and Time Values to Variables
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createevent);

		btnSelectDate = (Button) findViewById(R.id.btnDate);
		btnSelectTime = (Button) findViewById(R.id.btnTime);
		createEvent = (Button) findViewById(R.id.createEvent);
		cancelCreateEvent= (Button) findViewById(R.id.cancelCreateEvent);
		et_eventname=(EditText) findViewById(R.id.et_eventname);
		et_eventdesc=(EditText) findViewById(R.id.et_eventdesc);
		et_eventvenue=(EditText) findViewById(R.id.et_eventvenue);
		group_name_list=(ListView)findViewById(R.id.group_name_list);
		
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
			values_group_name_list= new String[user.getJSONArray("groups").length()];
			Log.e("user.getJSONArra",new Integer(user.getJSONArray("groups").length()).toString());
			for(int i = 0; i < user.getJSONArray("groups").length(); i++) {
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("groupToken", user.getJSONArray("groups").getString(i)));
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getGroupDetails",params);
	           Log.e("jsonjsonjson",json.toString());
	            if(json != null){
	            	values_group_name_list[i]=json.getString("groupName");
	            	
	            	eventMembers.add(i, json.getJSONArray("groupMembers"));
	            	eventMembersName.add(i, json.getJSONArray("groupMembersName"));
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            }
				
			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnSelectDate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Show the DatePickerDialog
				showDialog(DATE_DIALOG_ID);
			}
		});

		btnSelectTime.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Show the TimePickerDialog
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		createEvent.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Show the TimePickerDialog
				//showDialog(TIME_DIALOG_ID);
				try{
				ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
	             params.add(new BasicNameValuePair("eventName", et_eventname.getText().toString()));
	             params.add(new BasicNameValuePair("eventDescription", et_eventdesc.getText().toString()));
	             params.add(new BasicNameValuePair("venue", et_eventvenue.getText().toString()));
	             params.add(new BasicNameValuePair("eventDate", month+"/"+day+"/"+ year));
	             params.add(new BasicNameValuePair("eventTime", hour+":"+minute));
	             
	             params.add(new BasicNameValuePair("eventAdminEmail", user.getString("email")));
	             params.add(new BasicNameValuePair("eventAdminName", user.getString("userName")));
	             params.add(new BasicNameValuePair("groupName", values_group_name_list[globalPosition]));
	             
	             params.add(new BasicNameValuePair("eventMembers", user.getString("email")));
	             
	             for(int i=0;i<eventMembers.get(globalPosition).length();i++){
	            	 params.add(new BasicNameValuePair("eventRequests", eventMembers.get(globalPosition).getString(i)));
	            	 params.add(new BasicNameValuePair("eventRequestsName", eventMembersName.get(globalPosition).getString(i)));
	             }
	             
	             
	            JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/createEvent",params);
	            if(json != null){
	            	//groupsAdded[i]=json.getString("groupName")+"  ("+ json.getString("groupAdminName")+")";
	            	//groupsAdmin[i]=json.getString("groupAdmin");
	            	Log.e("EVENT CREATION SUCCESS","EVENT CREATED");
	            	Toast.makeText(getApplication(),"Event created successfully",Toast.LENGTH_LONG).show();
	            	finish();
	            }
				}catch(JSONException e){
					
				}
			}
		});
		
		cancelCreateEvent.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Show the TimePickerDialog
				//showDialog(TIME_DIALOG_ID);
				finish();
			}
		});
		
		Log.e("values_group_name_list",values_group_name_list.toString());
		//values_group_name_list= new String[]{"event1","event2","event3"};
		ArrayAdapter<String> adapter_group_list = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values_group_name_list);
		
		group_name_list.setAdapter(adapter_group_list);
		
		group_name_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("list", "itemclicked");
				globalPosition=position;
				//getPopupWindow_upcoming();
			//	getDialogWindow();
			}

		});

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// the callback received when the user "sets" the Date in the
		// DatePickerDialog
		@Override
		public void onDateSet(DatePicker view, int yearSelected,
				int monthOfYear, int dayOfMonth) {
			year = yearSelected;
			month = monthOfYear;
			day = dayOfMonth;
			// Set the Selected Date in Select date Button
			btnSelectDate.setText("Date selected :   " + day + "/" + month + "/"
					+ year);
		}

	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		// the callback received when the user "sets" the TimePickerDialog in
		// the dialog

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int min) {
			hour = hourOfDay;
			minute = min;
			// Set the Selected Date in Select date Button
			btnSelectTime.setText("Time selected :   " + hour + ":" + minute);
		}

	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// create a new DatePickerDialog with values you want to show
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
			// create a new TimePickerDialog with values you want to show
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);

		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
}
