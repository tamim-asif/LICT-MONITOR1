package bnlive.in.lictmonitor.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;

/**
 * Created by Sk Faisal on 3/26/2018.
 */

public class MainMapFragment extends MapFragment{
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Marker placeMarker(Context context,MergeSheduleUniversity eventInfo,GoogleMap googleMap) {
        String ltln=eventInfo.getUniversity().getLat_long();
        String[] str=ltln.split(",");
        MarkerOptions markerOptions=new MarkerOptions()

                .position(new LatLng(Double.parseDouble(str[0]),Double.parseDouble(str[1])))

                .title(eventInfo.getBatchCode())
                .snippet(eventInfo.getStatus());
        if(eventInfo.getStatus().equals("scheduled"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
        else if(eventInfo.getStatus().equals("ongoing"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        else if(eventInfo.getStatus().equals("late"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        else if(eventInfo.getStatus().equals("cancelled"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        else if(eventInfo.getStatus().equals("completed"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        else if(eventInfo.getStatus().equals("started on time"))
        {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }

//.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

//        if(eventInfo.getStatus().equals("ongoing"))
//        {
//            markerOptions.icon(bitmapDescriptorFromVector(context,android.R.drawable.ic_media_play));
//        }
////        else
//        if (eventInfo.getStatus().equals("completed") || eventInfo.getStatus().equals("completed successfully"))
//        {
//            markerOptions.icon(bitmapDescriptorFromVector(context,android.R.drawable.star_on));
//        }
//        //else
//        if (eventInfo.getStatus().equals("cancelled"))
//        {
//            markerOptions.icon(bitmapDescriptorFromVector(context,android.R.drawable.stat_notify_error));
//        }
       // else
// if (eventInfo.getStatus().equals("missed"))
//        {
//            markerOptions.icon(bitmapDescriptorFromVector(context,R.drawable.ic_ss));
//        }
//      //  else
//            if (eventInfo.getStatus().equals("delay"))
//        {
//            markerOptions.icon(bitmapDescriptorFromVector(context,R.drawable.ic_002_map));
//        }
        Marker m  = googleMap.addMarker(markerOptions);
m.showInfoWindow();

        return m;

    }
}
