package com.intelimina.feedbacksample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.AsyncTask;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.util.Log;


public class MyActivity extends Activity {
    String doctor_id = "", user_id = "", feedback ="";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Spinner spinner = (Spinner)findViewById(R.id.feedback_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.input_feedback, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                feedback = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }           
        }); 
    }

    @Override
    public void onResume(){
        super.onResume();

        Context edetailingContext;
        SharedPreferences testPrefs;

        try {
            edetailingContext = createPackageContext("com.intelimina.unilab", 0);

            testPrefs = edetailingContext.getSharedPreferences(BuildConfig.FEEDBACK_TOKEN, Context.MODE_MULTI_PROCESS); 
            user_id = testPrefs.getString("user_id", getString(R.string.user_id));
            doctor_id = testPrefs.getString("doctor_id", getString(R.string.doctor_id));
        } catch (Exception e) {
        }

        // Create the text view
        TextView userIdView = (TextView) findViewById(R.id.user_id);
        userIdView.setText("User ID:" + user_id);

        TextView doctorIdView = (TextView) findViewById(R.id.doctor_id);
        doctorIdView.setText("Doctor ID:" + doctor_id);
    }

    public void sendFeedback(View view){
        JSONObject params = new JSONObject();
        JSONObject feedback_data = new JSONObject();
        try {
            feedback_data.put("rating", feedback);
            feedback_data.put("comment", "Some Comment Here");

            params.put("apk_token", BuildConfig.FEEDBACK_TOKEN);
            params.put("user_id", user_id);
            params.put("doctor_id", doctor_id);
            params.put("data", feedback_data);
        } catch (JSONException ex) {
            Log.e("intelimina", "JSON Error");
        }

        FeedbackTask runner = new FeedbackTask();
        runner.execute(params.toString());
    }

    private class FeedbackTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(BuildConfig.SERVER_URL + BuildConfig.FEEDBACK_PATH);
            
            try{
                StringEntity se = new StringEntity(params[0]);
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                httppostreq.setEntity(se);
            }catch(UnsupportedEncodingException e){
                Log.e("intelimina", "UnsupportedEncodingException - "  + e.getMessage());
            }

            try{
                HttpResponse httpresponse = httpclient.execute(httppostreq);
                publishProgress();
            }catch (IOException e) {
                Log.e("intelimina", "IOException - " + e.getMessage());   
            }

            return null;
        }

        protected void onProgressUpdate() {
            Log.i("intelimina", "FeedbackTask : Sending");
        }

        protected void onPostExecute() {
        }
    }
}


