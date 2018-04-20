package bnlive.in.lictmonitor.admin;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;

import bnlive.in.lictmonitor.MainActivity;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;

/**
 * Created by Sk Faisal on 4/19/2018.
 */

public class MyItem implements ClusterItem {
    private  LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;

private ClusterManager<MyItem> clusterManager;


    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet=null;

    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }



    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }


}
