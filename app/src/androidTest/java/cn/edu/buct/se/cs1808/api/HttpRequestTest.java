package cn.edu.buct.se.cs1808.api;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequestTest extends TestCase {

    public void testGet() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        JSONObject param = new JSONObject();
        try {
            param.put("token", "this a token which have '&','=','你'");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.getInstance(ctx).get("http://imessay.cn:8828/get", param, null, (JSONObject response) -> {
            try {
                Log.i("RepData", response.getString("message"));
                Log.i("RepData", response.getString("status"));
                Log.i("RepData", response.getJSONObject("data").getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (VolleyError error) -> {
            Log.e("Error", error.toString());
        });
    }

    public void testPost() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        JSONObject param = new JSONObject();
        try {
            param.put("username", "1234567890");
            param.put("password", "stuadmin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.getInstance(ctx).post("http://imessay.cn:8828/post", param, null, (JSONObject response) -> {
            try {
                Log.i("RepData", response.getString("message"));
                Log.i("RepData", response.getString("status"));
                Log.i("RepData", response.getJSONObject("data").getString("username"));
                Log.i("RepData", response.getJSONObject("data").getString("password"));
                Log.i("RepData", response.getJSONObject("data").getString("method"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (VolleyError error) -> {
            Log.e("Error", error.toString());
        });
    }

    public void testPut() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        JSONObject param = new JSONObject();
        try {
            param.put("username", "1234567890");
            param.put("password", "stuadmin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.getInstance(ctx).put("http://imessay.cn:8828/put", param, null, (JSONObject response) -> {
            try {
                Log.i("RepData", response.getString("message"));
                Log.i("RepData", response.getString("status"));
                Log.i("RepData", response.getJSONObject("data").getString("username"));
                Log.i("RepData", response.getJSONObject("data").getString("password"));
                Log.i("RepData", response.getJSONObject("data").getString("method"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (VolleyError error) -> {
            Log.e("Error", error.toString());
        });
    }
    public void testDelete() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        JSONObject param = new JSONObject();
        try {
            param.put("username", "1234567890");
            param.put("password", "stuadmin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.getInstance(ctx).delete("http://imessay.cn:8828/delete", param, null, (JSONObject response) -> {
            try {
                Log.i("RepData", response.getString("message"));
                Log.i("RepData", response.getString("status"));
                Log.i("RepData", response.getJSONObject("data").getString("username"));
                Log.i("RepData", response.getJSONObject("data").getString("password"));
                Log.i("RepData", response.getJSONObject("data").getString("method"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (VolleyError error) -> {
            Log.e("Error", error.toString());
        });
    }
}
