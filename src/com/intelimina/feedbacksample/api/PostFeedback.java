package com.intelimina.feedbacksample.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.intelimina.feedbacksample.BuildConfig;
import java.util.Map;

public class PostFeedback {
    public final static String apiURL = BuildConfig.SERVER_URL + BuildConfig.FEEDBACK_PATH;
    Context context;

    public PostFeedback (Context context, Map<String, String> obj) {
        RequestQueue queue = Volley.newRequestQueue(context);
        this.context = context;
        final Map<String, String> params = obj;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,
                responseListener(), errorListener()){

            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            }

        };

        queue.add(stringRequest);
    }

    private Response.Listener responseListener(){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response string.
                Log.d("FEEDBACK", "RESPONSE: " + response);
                Toast toast = Toast.makeText(context, "Successfully submitted response.", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
    }

    private Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FEEDBACK", "RESPONSE: " + error.getMessage());
            }
        };
    }
}
