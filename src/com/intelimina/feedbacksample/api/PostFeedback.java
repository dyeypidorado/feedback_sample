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

import org.json.JSONException;
import org.json.JSONObject;

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
                // Display the response string. You can customize an action here like switching
                // to an activity or closing the app.

                Log.d("FEEDBACK", "RESPONSE: " + response);

                // Sample Alert
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Toast toast = Toast.makeText(context, jsonResponse.getString("message"),
                                                        Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    // Do something here.
                }

            }
        };
    }

    private Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Replace this with the desired action.
                Log.d("FEEDBACK", "RESPONSE: " + error.getMessage());
            }
        };
    }
}
