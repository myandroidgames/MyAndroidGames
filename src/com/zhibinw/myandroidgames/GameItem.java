
package com.zhibinw.myandroidgames;

public class GameItem {
    private CharSequence mName;
    private CharSequence mVersion;
    private int mSize;
    private CharSequence mIcon;
    private CharSequence mDownload;

    static CharSequence NAME_TEST = "name-test";
    static CharSequence VERSION_TEST = "version-test";
    static int SIZE_TEST = 1024 * 1024;
    static CharSequence ICON_TEST = "icon-test";
    static CharSequence DOWNLOAD_TEST = "download-test";

    public GameItem() {
        mName = NAME_TEST;
        mVersion = VERSION_TEST;
        mSize = SIZE_TEST;
        mIcon = ICON_TEST;
        mDownload = DOWNLOAD_TEST;
    }

    public GameItem(CharSequence name, CharSequence version, int size, CharSequence icon,
            CharSequence download) {
        mName = name;
        mVersion = version;
        mSize = size;
        mIcon = icon;
        mDownload = download;
    }

    public CharSequence getName() {
        return mName;
    }

    public void setName(CharSequence name) {
        mName = name;
    }

    public CharSequence getVersion() {
        return mVersion;
    }

    public void setVersion(CharSequence version) {
        mVersion = version;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public CharSequence getIcon() {
        return mIcon;
    }

    public void setIcon(CharSequence icon) {
        mIcon = icon;
    }

    public CharSequence getDownload() {
        return mDownload;
    }

    public void setDownload(CharSequence download) {
        mDownload = download;
    }

}
