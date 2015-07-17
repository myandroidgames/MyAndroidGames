
package com.zhibinw.myandroidgames;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {

    static String TAG = "myandroidgames";
    public static String ADDRESS_BASE = "https://raw.githubusercontent.com/myandroidgames/myandroidgames.github.io/master/";
    static String ADDRESS_JSON = "apps/com.king.candycrushsaga.json";

    static String JSON_KEY_NAME = "name";
    static String JSON_KEY_SIZE = "size";
    static String JSON_KEY_ICON = "iconlink";
    static String jSON_KEY_LINK = "downloadlink";
    static String JSON_KEY_VERSION = "version";
    private ListView mListView;
    private GameListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        List<GameItem> items = new ArrayList<GameItem>();
        mAdapter = new GameListAdapter(this, 0, items);
        mListView.setAdapter(mAdapter);
        getJson(ADDRESS_BASE + ADDRESS_JSON);
        Log.d(TAG, "onCreate..");
    }

    public void getJson(String address) {
        if (networkAvailable()) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, address,
                    null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String name;
                            try {
                                Log.d(TAG, "json:" + response.toString());
                                name = response.getString(JSON_KEY_NAME);
                                String icon = response.getString(JSON_KEY_ICON);
                                int size = response.getInt(JSON_KEY_SIZE);
                                String link = response.getString(jSON_KEY_LINK);
                                String ver = response.getString(JSON_KEY_VERSION);
                                mAdapter.add(new GameItem(name, ver, size, icon, link));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            DownloadController.getInstance(this).addToRequestQueue(jsObjRequest);
        } else {
            // report network not available...
            Toast.makeText(getApplicationContext(), "Network Not Available", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public static boolean networkAvailable() {
        return true;
    }
}
