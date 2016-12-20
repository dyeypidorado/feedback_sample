package com.intelimina.feedbacksample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MyActivity extends Activity {
    String doctor_id = "", user_id = "";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }


    public void setText(){
        Context edetailingContext;
        SharedPreferences testPrefs;

        try {
            edetailingContext = createPackageContext("com.intelimina.unilab", 0);

            testPrefs = edetailingContext.getSharedPreferences(BuildConfig.FEEDBACK_TOKEN, Context.MODE_MULTI_PROCESS);
            user_id = testPrefs.getString("user_id", getString(R.string.user_id));
            doctor_id = testPrefs.getString("doctor_id", getString(R.string.doctor_id));

            Log.d("FEEDBACK", "FEEDBACK USER: " + user_id);
            Log.d("FEEDBACK", "FEEDBACK USER: " + doctor_id);
        } catch (Exception e) {
        }

        // Create the text view
        TextView userIdView = (TextView) findViewById(R.id.user_id);
        userIdView.setText("User ID:" + user_id);

        TextView doctorIdView = (TextView) findViewById(R.id.doctor_id);
        doctorIdView.setText("Doctor ID:" + doctor_id);
    }

    // Send data to Server
    public void sendFeedback(View view){
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Integer feedback = ratingBar.getNumStars();

        EditText remarksText = (EditText) findViewById(R.id.remarks);
        String remarks = remarksText.getText().toString();

        JSONObject params = new JSONObject();
        JSONObject feedback_data = new JSONObject();
        try {
            feedback_data.put("rating", feedback);
            feedback_data.put("comments", remarks);

            params.put("apk_token", BuildConfig.FEEDBACK_TOKEN);
            params.put("user_id", user_id);
            params.put("doctor_id", doctor_id);
            params.put("data", feedback_data);

            Log.d("FEEDBACK", "SERVER DATA(user): " + user_id);
            Log.d("FEEDBACK", "SERVER DATA(doctor): " + doctor_id);
            Log.d("FEEDBACK", "SERVER DATA(rating): " + remarks);
            Log.d("FEEDBACK", "SERVER DATA(comments): " + feedback);
        } catch (JSONException ex) {
            Log.e("intelimina", "JSON Error");
        }
        if (user_id.equals("") || doctor_id.equals("")) {
            Toast toast = Toast.makeText( getApplicationContext(), "No User/Doctor found.", Toast.LENGTH_SHORT );
            toast.show();
        } else {
            FeedbackTask runner = new FeedbackTask();
            runner.execute(params.toString());
        }
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
    }
}


