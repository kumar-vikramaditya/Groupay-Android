package com.vikram.groupay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class RegisterActivity extends Activity {
	
	ProgressBarHandler mProgressBarHandler;
    EditText firstName,lastName,email,password;
    Button register;
    String firstNametxt,lastNametxt,emailtxt,passwordtxt;
    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        register = (Button)findViewById(R.id.registerbtn);

       
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            	firstNametxt = firstName.getText().toString();
            	lastNametxt = lastName.getText().toString();
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                params = new ArrayList<NameValuePair>();
                
                params.add(new BasicNameValuePair("firstName", firstNametxt));
                params.add(new BasicNameValuePair("lastName", lastNametxt));
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                ServerRequest sr = new ServerRequest();
                //Log.d("HELLLNOOOO",params.toString());
                JSONObject json = sr.getJSON(AppSettings.SERVER_IP+"/register",params);
                
                mProgressBarHandler = new ProgressBarHandler(RegisterActivity.this); 
                mProgressBarHandler.show(); 
                
                if(json != null){
                    try{
                    	mProgressBarHandler.hide();
                        String jsonstr = json.getString("response");
                        Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();
                        if(json.getBoolean("res")){
                    	   Intent regactivity = new Intent(RegisterActivity.this,MainActivity.class);
                           startActivity(regactivity);
                           finish();
                        }
                       // Log.d("Hello", jsonstr);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




}

