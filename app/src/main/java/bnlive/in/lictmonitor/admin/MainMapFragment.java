package bnlive.in.lictmonitor.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.Log;

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
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, Drawable vectorDrawable) {
        //Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public Marker placeMarker(Context context,MergeSheduleUniversity eventInfo,GoogleMap googleMap) {
        String ltln=eventInfo.getUniversity().getLat_long();
        String[] str=ltln.split(",");
        Marker marker=null;
        MarkerOptions markerOptions=new MarkerOptions()

                .position(new LatLng(Double.parseDouble(str[0]),Double.parseDouble(str[1])))

                .title(eventInfo.getStatusModel().getBatch_code())
                .snippet(eventInfo.getStatusModel().getStatus());


            Drawable drawable;
            if(eventInfo.getStatusModel().getStatus().equals("scheduled")) {
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    drawable = context.getResources().getDrawable(R.drawable.ic_scheduled, getActivity().getBaseContext().getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                } else {
                    drawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_scheduled, context.getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                }
            }
            else if(eventInfo.getStatusModel().getStatus().equals("ongoing")) {
                // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    drawable = context.getResources().getDrawable(R.drawable.ic_ongoing, getActivity().getBaseContext().getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                } else {
                    drawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_ongoing, context.getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                }
            }
            else if(eventInfo.getStatusModel().getStatus().equals("late"))
            {
                // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    drawable = context.getResources().getDrawable(R.drawable.ic_late, getActivity().getBaseContext().getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                } else {
                    drawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_late, context.getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                }
            }
            else if(eventInfo.getStatusModel().getStatus().equals("cancelled"))
            {

                //Canvas canvas=new Canvas();
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    drawable = context.getResources().getDrawable(R.drawable.ic_003_location_1, getActivity().getBaseContext().getTheme());
//              Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
//              canvas.setBitmap(bitmap);
//              drawable.draw(canvas);
//               BitmapDescriptor markerIcon=BitmapDescriptorFactory.fromBitmap(bitmap);
                    markerOptions.icon(bitmapDescriptorFromVector(context,drawable));
                } else {
                    drawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_003_location_1, context.getTheme());
//                Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
//
//                canvas.setBitmap(bitmap);
//                drawable.draw(canvas);
//                BitmapDescriptor markerIcon=BitmapDescriptorFactory.fromBitmap(bitmap);
                    markerOptions.icon(bitmapDescriptorFromVector(context,drawable));
                    //  Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show();
                }
                // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            else if(eventInfo.getStatusModel().getStatus().equals("completed"))
            {
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    drawable = context.getResources().getDrawable(R.drawable.ic_completed, getActivity().getBaseContext().getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                } else {
                    drawable = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_completed, context.getTheme());

                    markerOptions.icon(bitmapDescriptorFromVector(context, drawable));
                }
            }
            else if(eventInfo.getStatusModel().getStatus().equals("started on time"))
            {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
else
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
//        if(markerOptions.isVisible()==true){
//            String ltln2=eventInfo.getUniversity().getLat_long();
//            Log.d("currentdis",eventInfo.getUniversity().getLat_long());
//            String[] str2=ltln2.split(",");
//            Double lat=Double.parseDouble(str2[0]);
//            Double lon=Double.parseDouble(str2[1]);
//            lon=lon+0.0001;
//            Log.d("currentdis","Modified: "+lat+","+lon);
////            eventInfo.getUniversity().setLat_long(""+lat+","+lon);
////            m=  placeMarker(context,eventInfo,googleMap);
//            markerOptions.position(new LatLng(lat,lon))
//
//                    .title(eventInfo.getStatusModel().getBatch_code())
//                    .snippet(eventInfo.getStatusModel().getStatus());
//
//        }
            Marker m  = googleMap.addMarker(markerOptions);
            m.showInfoWindow();


            return m;



    }
}
