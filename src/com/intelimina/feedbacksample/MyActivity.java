package com.intelimina.feedbacksample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.intelimina.feedbacksample.api.PostFeedback;

import java.util.HashMap;
import java.util.Map;


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
            //  Replace value for createPackageContext if the launcher changes
            //  e.g.
            //      For Biofemme launcher, use 'com.intelimina.biofemme'
            //      For LRI launcher, use 'com.intelimina.lritherapharma'
            //      Value below defaults for -dev instance.
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
    public void sendFeedback(View view) {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        Float feedback = ratingBar.getRating();

        EditText remarksText = (EditText) findViewById(R.id.remarks);
        String remarks = remarksText.getText().toString();

        // Build the params to be sent on the server.
        // Fill the value of the corresponding Hash map keys with
        // inputs from your own app.

        Map<String, String> params = new HashMap<>();
        params.put("apk_token", BuildConfig.FEEDBACK_TOKEN);
        params.put("user_id", user_id);
        params.put("doctor_id", doctor_id);
        params.put("data[rating]", feedback.toString());
        params.put("data[comments]", remarks);

        if (user_id.equals("") || doctor_id.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "No User/Doctor found.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // Previous prepared params will then used by the PostFeedback
            // class to send inputs to the server.
            //
            // For advance features, you can save the feedback inputs in a file/DB in a meantime
            // then use this when an internet connection is already available.
            new PostFeedback(getApplicationContext(), params);
        }
    }
}


