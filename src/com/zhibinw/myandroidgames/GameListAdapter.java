package com.zhibinw.myandroidgames;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameListAdapter extends ArrayAdapter<GameItem> {
    private LayoutInflater mInflater;
    public GameListAdapter(Context context, int textViewResourceId, List<GameItem> objects) {
        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LinearLayout view = null;
        if (convertView == null) {
            view = (LinearLayout) mInflater.inflate(R.layout.game_item, null);
            GameItem item = this.getItem(position);
            
            TextView nameTextView = (TextView) view.findViewById(R.id.name);
            nameTextView.setText(item.getName());
            
            TextView versionText = (TextView) view.findViewById(R.id.version);
            versionText.setText(item.getVersion());
            
            TextView sizeText = (TextView) view.findViewById(R.id.size);
            sizeText.setText(sizeFormat(item.getSize()));
            ImageView iconView = (ImageView)view.findViewById(R.id.item_icon);
            try {
                new DownloadImageTask(iconView).execute(MainActivity.ADDRESS_BASE+item.getIcon());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            view = (LinearLayout) convertView;
        }
        Log.d(MainActivity.TAG, "getview " + position);
        //need set bitmap in  runnable..
//        iconView.setImageBitmap(bm)
        return view;
    }
    
    private String sizeFormat(int size){
        StringBuilder sBuilder = new StringBuilder();
        float fsize = size/(1024*1024);
        sBuilder.append("Size:");
        sBuilder.append(fsize);
        sBuilder.append(" M");
        return sBuilder.toString();
    }
    
    private class DownloadImageTask extends AsyncTask <String,Integer,Bitmap> {
        ImageView mImageView;
        public DownloadImageTask(ImageView view){
            mImageView = view;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap;
            try {
                Log.d("fuck", "urls:.." + urls[0]);
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStream in = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    return bitmap;
                } else {
                    // report and an error....
                    Log.d("fuck", "response is: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.d("fuck", "e:.." +e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            Log.d("fuck", "onPostExecute..");
            mImageView.setImageBitmap(image);
        }
    }

}
