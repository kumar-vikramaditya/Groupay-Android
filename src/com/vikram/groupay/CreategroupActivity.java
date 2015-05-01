package com.vikram.groupay;

import java.util.ArrayList;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroup);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		btnadd = (Button)findViewById(R.id.btnAddMembers);
		
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
				
				String[] val_list = new String[] { "notification e 1",
						"notification e 2", "notification e 2", "notification e 2",
						"notification e 2" };
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
					// TODO Auto-generated method stub
					/*SparseBooleanArray checked = listmembers.getCheckedItemPositions();
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
			 
			        Intent intent = new Intent(getApplicationContext(),
			        		CreategroupActivity.class);
			 
			        // Create a bundle object
			        Bundle b = new Bundle();
			        b.putStringArray("selectedItems", outputStrArr);
			 
			        // Add the bundle to the intent.
			        intent.putExtras(b);
			 
			        // start the ResultActivity
			        startActivity(intent);*/
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
