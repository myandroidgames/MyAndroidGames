
package com.zhibinw.myandroidgames;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameListAdapter extends ArrayAdapter<GameItem> {
    private LayoutInflater mInflater;
    private Context mContext;

    public GameListAdapter(Context context, int textViewResourceId, List<GameItem> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout view = null;
        if (convertView == null) {
            view = (RelativeLayout) mInflater.inflate(R.layout.game_item, null);
        } else {
            view = (RelativeLayout) convertView;
        }
        final GameItem item = this.getItem(position);
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        nameTextView.setText(item.getName());
        TextView versionText = (TextView) view.findViewById(R.id.version);
        versionText.setText(item.getVersion());
        TextView sizeText = (TextView) view.findViewById(R.id.size);
        sizeText.setText(Util.sizeFormat(item.getSize()));
        ImageView iconView = (ImageView) view.findViewById(R.id.item_icon);
        getImage(iconView, Constants.ADDRESS_BASE + item.getIcon());
        final Button button = (Button) view.findViewById(R.id.download);
        initialButtonStatus(button, item.getPackageName().toString(),
                item.getDownload().toString(), item.getVersion().toString());

        Util.log("getview " + position);
        return view;
    }
    //TODO move to other
    private void getImage(final ImageView image, String url) {
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
                notifyDataSetChanged();
            }
        }, 0, 0, null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                // image.setImageResource(R.drawable.image_load_error);
            }
        });
        DownloadController.getInstance(mContext).addToRequestQueue(request);
    }
    //TODO move
    private void downloadApk(final Button btn, String url, File apk) {
        if (apk.exists()) {
            apk.delete();
        }
        DownloadTask mDownloadTask = new DownloadTask(mContext, btn, apk);
        mDownloadTask.execute(url);
    }
    //ToDO move
    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = mContext.getApplicationContext().getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            Util.log("Package " + packageName + " not installed");
            e.printStackTrace();
        }
        if (pkgInfo != null) {
            Util.log(packageName + ":" + pkgInfo.versionName);
            return true;
        }
        return false;
    }
    //TODO move
    private boolean isPackageNeedUpdate(String packageName, String version) {
        PackageManager pm = mContext.getApplicationContext().getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            Util.log("Package " + packageName + " not installed");
            e.printStackTrace();
        }
        if (pkgInfo != null) {
            return pkgInfo.versionName.compareToIgnoreCase(version) < 0;
        } else {
            Util.log("can't get pkgInfo:");
        }
        return false;
    }
    //TODO move
    private boolean isPackageArchiveValid(File file) {
        PackageManager pm = mContext.getApplicationContext().getPackageManager();
        PackageInfo pkgInfo = null;
        pkgInfo = pm.getPackageArchiveInfo(file.toString(), 0);
        if (pkgInfo != null) {
            return true;
        }
        return false;
    }
    //TODO move
    private void initialButtonStatus(final Button button, final String packageName,
            final String sDownload, String version) {
        final File apkFile = new File(Constants.APP_LOCATION + "/" + packageName + ".apk");
        if (isPackageInstalled(packageName)) {
            if (isPackageNeedUpdate(packageName, version)) {
                button.setText("Update");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // download file
                        Util.log("onClick.. ");
                        button.setText("Downloading");
                        downloadApk(button, Constants.ADDRESS_BASE + sDownload, apkFile);
                    }
                });
            } else {
                button.setText("Open");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        PackageManager pm = mContext.getApplicationContext().getPackageManager();
                        List<ResolveInfo> mResolveInfos = pm.queryIntentActivities(new Intent(
                                Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                                .setPackage(packageName), 0);
                        if (mResolveInfos != null) {
                            Util.log(mResolveInfos.toString());
                        }
                        ResolveInfo resolveInfo = mResolveInfos.get(0);
                        Intent intent = new Intent();
                        intent.setClassName(packageName, resolveInfo.activityInfo.name);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else {
            if (apkFile.exists()) {
                button.setText("Install");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (!isPackageArchiveValid(apkFile)) {
                            Toast.makeText(mContext.getApplicationContext(),
                                    "apk file invalid, need redownload", Toast.LENGTH_SHORT).show();
                            apkFile.delete();
                            notifyDataSetChanged();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(apkFile), Constants.TYPE_APPLICATION);
                            Activity activity = (Activity) mContext;
                            activity.startActivityForResult(intent, Constants.REQUEST_CODE_INSTALL);
                        }
                    }
                });
            } else {
                button.setText("Download");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // download file
                        Util.log("onClick.. ");
                        button.setText("Downloading");
                        downloadApk(button, Constants.ADDRESS_BASE + sDownload, apkFile);
                    }
                });
            }
        }
    }
    //TODO
    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context mContext;
        private Button mButton;
        private File mApkFile;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context, Button btn, File apkFile) {
            mContext = context;
            mButton = btn;
            mApkFile = apkFile;
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try {
                Util.log("url:" + url.toString());
                connection = (HttpURLConnection) (new URL(url).openConnection());
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int fileLength = connection.getContentLength();
                    inputStream = connection.getInputStream();
                    outputStream = new FileOutputStream(mApkFile);
                    byte[] data = new byte[Constants.BUFF_SIZE];
                    long total = 0;
                    int count = 0;
                    while (((count = inputStream.read(data)) != -1)) {
                        total += count;
                        if (fileLength > 0) {
                            publishProgress((int) (total * 100 / fileLength));
                        }
                        outputStream.write(data, 0, count);
                    }
                } else {
                    // report error
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            mButton.setText("Install");
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(mApkFile), Constants.TYPE_APPLICATION);
                    mContext.startActivity(intent);
                }
            });
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Util.log("progess update: " + values[0]);
            mButton.setText(String.valueOf(values[0]) + "%");
            super.onProgressUpdate(values);
        }

    }
}
