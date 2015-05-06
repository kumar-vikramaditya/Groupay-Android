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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;

public class CreategroupActivity extends Activity{
	ListView listmembers, selected_members;
	Button btnadd,btncancel;
	PopupWindow pwindow;
	ArrayAdapter<String> adapter_list;
	JSONObject user;
	List<NameValuePair> params;
	String[] val_list;
	String[] val_list_email;
	ArrayAdapter<String> adapter_selected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		btnadd = (Button)findViewById(R.id.btnAddMembers);
		selected_members=(ListView) findViewById(R.id.selected_members);
		try {
			user=new JSONObject(getIntent().getStringExtra("userdata"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Bundle b = getIntent().getExtras();
	        String[] resultArr = b.getStringArray("selectedItems");
	        selected_members = (ListView) findViewById(R.id.selected_members);
	 
	        ArrayAdapter<String> adapter_selected = new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_1, resultArr);
	        selected_members.setAdapter(adapter_selected);
		*/
		btnadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				getPopupWindow();
				
			}	
			
		});
	}
		
		private void getPopupWindow() {
			// TODO Auto-generated method stub
			try {
				LayoutInflater inflater = (LayoutInflater) CreategroupActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.activity_memberslist,(ViewGroup) findViewById(R.id.rel_popup1));
				listmembers = (ListView) layout.findViewById(R.id.member_list);
				
				 ServerRequest sr = new ServerRequest();
				 params = new ArrayList<NameValuePair>();
				 params.add(new BasicNameValuePair("groupToken", ""));
	             JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/getAllUsers",params);
	            if(json != null){
	            	JSONArray users= json.getJSONArray("users");
	            	val_list= new String[users.length()];
	            	val_list_email= new String[users.length()];
	            	for(int i=0;i<users.length();i++){
	            		val_list[i]=users.getJSONObject(i).getString("userName");
	            		val_list_email[i]=users.getJSONObject(i).getString("email");
	            	}
	            }
				
				adapter_list = new ArrayAdapter<String>(CreategroupActivity.this,
						android.R.layout.simple_list_item_multiple_choice, val_list);
				listmembers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				listmembers.setAdapter(adapter_list);
				Display disp = getWindowManager().getDefaultDisplay();
				int width = disp.getWidth();
				int height = disp.getHeight();
				
				pwindow = new PopupWindow(layout, width-width/4, height-height/4, true);
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
			        for (int i = 0; i < checked.size(); i++) {
			            // Item position in adapter
			            int position = checked.keyAt(i);
			            // Add sport if it is checked i.e.) == TRUE!
			            if (checked.valueAt(i))
			                selectedItems.add(adapter_list.getItem(position));
			        }
			 
			        String[] outputStrArr = new String[selectedItems.size()];
			 
			        for (int i = 0; i < selectedItems.size(); i++) {
			            outputStrArr[i] = selectedItems.get(i);
			        }
			        Log.e("SelectedItem",outputStrArr.toString());
			        
			        adapter_selected = new ArrayAdapter<String>(CreategroupActivity.this,
			                android.R.layout.simple_list_item_1, outputStrArr);
			        selected_members.setAdapter(adapter_selected);
			       /*Intent intent = new Intent(getApplicationContext(),
			        		CreategroupActivity.class);
			 
			        // Create a bundle object
			        Bundle b = new Bundle();
			        b.putStringArray("selectedItems", outputStrArr);
			 
			        // Add the bundle to the intent.
			        intent.putExtras(b);*/
			 
			        // start the ResultActivity
			       // startActivity(intent);
					pwindow.dismiss();

				}
			};

	

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
			NavUtils.navigateUpFromSameTask(CreategroupActivity.this);
			return true;

		}
		return super.onOptionsItemSelected(item);

	}
	
	 @Override
	   public void onBackPressed() {
	      moveTaskToBack(true); 
	      CreategroupActivity.this.finish();
	   }
}
