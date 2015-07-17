
package com.zhibinw.myandroidgames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    
    static String TAG="myandroidgames";
    //static String ADDRESS_BASE="https://github.com/myandroidgames/myandroidgames.github.io/blob/master/";
    public static String ADDRESS_BASE = "https://raw.githubusercontent.com/myandroidgames/myandroidgames.github.io/master/";
    static String ADDRESS_JSON = "apps/com.king.candycrushsaga.json";
    
    static String JSON_KEY_NAME = "name";
    static String JSON_KEY_SIZE = "size";
    static String JSON_KEY_ICON = "iconlink";
    static String jSON_KEY_LINK = "downloadlink";
    static String JSON_KEY_VERSION = "version";
    private ListView mListView;
    private GameListAdapter mAdapter;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        List<GameItem> items = new ArrayList<GameItem>();
        mAdapter = new GameListAdapter(this, 0, items);
        mListView.setAdapter(mAdapter);
//        mAdapter.add(new GameItem());
        Log.d(TAG, "onCreate..");
        if (networkAvailable()) {
            HttpWorker worker = new HttpWorker();
            worker.start();
        } else {
            //report network not available...
            Toast.makeText(getApplicationContext(), "Network Not Available", Toast.LENGTH_SHORT).show();
        }
        
    }
    
    public String getJson(String address) {
        StringBuilder sBuilder = new StringBuilder();
        try {
            URL url = new URL(address.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    sBuilder.append(line);
                }
                Log.d(TAG, sBuilder.toString());
                return sBuilder.toString();
            } else {
                // report and an error....
                Log.d(TAG, "response is: " + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "exception : " + e);
        }
        return null;
//        StringBuilder builder = new StringBuilder();
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(address);
//        HttpResponse response = null;
//        try {
//            response = client.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            Log.d(TAG, "statusCode:" + statusCode);
//            if (statusCode == HttpStatus.SC_OK) {
//                HttpEntity entity = response.getEntity();
//                InputStream content = entity.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//            } else {
//                Log.d(TAG, "failed to get json file");
//            }
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Log.d(TAG, builder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private boolean networkAvailable(){
        return true;
    }

    private class ItemAdder implements Runnable {
        GameItem mItem;
        public ItemAdder(GameItem item) {
            mItem = item;
        }
        @Override
        public void run() {
            mAdapter.add(mItem);
        }
    }
    private class  HttpWorker extends Thread {
        public void run (){
            String str=getJson(ADDRESS_BASE + ADDRESS_JSON);
            try {
                JSONObject jsonObject= new JSONObject(str);
                String name = jsonObject.getString(JSON_KEY_NAME);
                Log.d(TAG, "name :" + name);
                String icon = jsonObject.getString(JSON_KEY_ICON);
                Log.d(TAG, "icon :" + icon);
                int size = jsonObject.getInt(JSON_KEY_SIZE);
                Log.d(TAG, "size :" + size);
                String link = jsonObject.getString(jSON_KEY_LINK);
                Log.d(TAG, "link :" + link);
                String ver = jsonObject.getString(JSON_KEY_VERSION);
                Log.d(TAG, "version :" + ver);
                mHandler.post( new ItemAdder(new GameItem(name,ver,size,icon,link)));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
}
