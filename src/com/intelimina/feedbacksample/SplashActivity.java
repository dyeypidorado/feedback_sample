package com.intelimina.feedbacksample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ActivityTask().execute("");
        finish();
    }

    private class ActivityTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... context) {
            Intent intent = new Intent(SplashActivity.this, MyActivity.class);
            startActivity(intent);
            return null;
        }
    }
}