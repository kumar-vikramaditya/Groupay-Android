package com.vikram.groupay;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";


    public ServerRequest() {

    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {


        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            //Log.e("getJSONFromUrl111",url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            //Log.e("getJSONFromUr22222222222",url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //Log.e("getJSONFromUr33333333333333",httpPost.toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            //Log.e("getJSONFromUr44444444444444444444",httpPost.toString());
            is = httpEntity.getContent();
            //Log.e("InputStream", is.toString());

        } catch (UnsupportedEncodingException e) {
        	 Log.e("UnsupportedEncodingException",e.toString());
        } catch (ClientProtocolException e) {
        	 Log.e("ClientProtocolException",e.toString());
        } catch (IOException e) {
        	 Log.e("IOException",e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }
    JSONObject jobj;
    public JSONObject getJSON(String url, List<NameValuePair> params) {
    	Log.e("ServerRequest",params.toString());
        Params param = new Params(url,params);
        Request myTask = new Request();
        try{
         jobj= myTask.execute(param).get();
        }catch (InterruptedException e) {
        	Log.e("InterruptedException",e.toString());
        }catch (ExecutionException e){
        	Log.e("ExecutionException",e.toString());
        }
        return jobj;
    }


    private static class Params {
        String url;
        List<NameValuePair> params;


        Params(String url, List<NameValuePair> params) {
            this.url = url;
            this.params = params;

        }
    }

    private class Request extends AsyncTask<Params, String, JSONObject> {

            @Override
        protected JSONObject doInBackground(Params... args) {

            ServerRequest request = new ServerRequest();
            //Log.e("RequestRequest",args[0].params.toString());
            JSONObject json = request.getJSONFromUrl(args[0].url,args[0].params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            super.onPostExecute(json);

        }

    }
    }