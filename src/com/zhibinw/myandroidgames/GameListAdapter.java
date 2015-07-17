package com.zhibinw.myandroidgames;

import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import android.content.Context;
import android.graphics.Bitmap;
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
    private Context mContext;
    public GameListAdapter(Context context, int textViewResourceId, List<GameItem> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            getImage(iconView,MainActivity.ADDRESS_BASE + item.getIcon());
        } else {
            view = (LinearLayout) convertView;
        }
        Log.d(MainActivity.TAG, "getview " + position);
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

    private void getImage(final ImageView image, String url) {
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                // image.setImageResource(R.drawable.image_load_error);
            }
        });
        DownloadController.getInstance(mContext).addToRequestQueue(request);
    }
}
