
package com.zhibinw.myandroidgames;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {

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
        Util.createMyDir();
        getJson(Constants.ADDRESS_BASE + Constants.ADDRESS_JSON);
    }

    public void getJson(String address) {
        if (networkAvailable()) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, address,
                    null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Util.log("json:" + response.toString());
                                String name = response.getString(Constants.JSON_KEY_NAME);
                                String icon = response.getString(Constants.JSON_KEY_ICON);
                                int size = response.getInt(Constants.JSON_KEY_SIZE);
                                String link = response.getString(Constants.JSON_KEY_LINK);
                                String ver = response.getString(Constants.JSON_KEY_VERSION);
                                String packageName = response.getString(Constants.jSON_KEY_PACKAGE);
                                mAdapter.add(new GameItem(name, packageName, ver, size, icon, link));
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(),
                                        Toast.LENGTH_SHORT).show();
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
