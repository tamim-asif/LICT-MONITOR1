package bnlive.in.lictmonitor.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.HashMap;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;

/**
 * Created by Sk Faisal on 4/19/2018.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem>{
    private Context mContext;
    private MainMapFragment mapFragment;
    HashMap<Marker, MergeSheduleUniversity> markermap;
    private MergeSheduleUniversity eventInfo;

    public MergeSheduleUniversity getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(MergeSheduleUniversity eventInfo) {
        this.eventInfo = eventInfo;
    }

    public void initSetup()
    {
        markermap = new HashMap<>();
        mapFragment=new MainMapFragment();
    }
    public void setMarker(Context context, MergeSheduleUniversity mergeSheduleUniversity, GoogleMap mMap)
    {
        Marker marker = mapFragment.placeMarker(context, mergeSheduleUniversity, mMap);
        markermap.put(marker, mergeSheduleUniversity);
    }
    public CustomClusterRenderer(Context context, GoogleMap map,
                        ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        initSetup();
    }

    @Override protected void onBeforeClusterItemRendered(MyItem item,
                                                         MarkerOptions markerOptions) {
        markerOptions

                .title(eventInfo.getStatusModel().getBatch_code())
                .snippet(eventInfo.getStatusModel().getStatus());
        Drawable drawable;

        if (eventInfo.getStatusModel().getStatus().equals("scheduled")) {
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                drawable = mContext.getResources().getDrawable(R.drawable.ic_scheduled, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            } else {
                drawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_scheduled, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            }
        } else if (eventInfo.getStatusModel().getStatus().equals("ongoing")) {
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                drawable = mContext.getResources().getDrawable(R.drawable.ic_ongoing, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            } else {
                drawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_ongoing, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            }
        } else if (eventInfo.getStatusModel().getStatus().equals("late")) {
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                drawable = mContext.getResources().getDrawable(R.drawable.ic_late, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            } else {
                drawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_late, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            }
        } else if (eventInfo.getStatusModel().getStatus().equals("cancelled")) {

            //Canvas canvas=new Canvas();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                drawable = mContext.getResources().getDrawable(R.drawable.ic_003_location_1, mContext.getTheme());
//              Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
//              canvas.setBitmap(bitmap);
//              drawable.draw(canvas);
//               BitmapDescriptor markerIcon=BitmapDescriptorFactory.fromBitmap(bitmap);
                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            } else {
                drawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_003_location_1, mContext.getTheme());
//                Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
//
//                canvas.setBitmap(bitmap);
//                drawable.draw(canvas);
//                BitmapDescriptor markerIcon=BitmapDescriptorFactory.fromBitmap(bitmap);
                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
                //  Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show();
            }
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if (eventInfo.getStatusModel().getStatus().equals("completed")) {
            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                drawable = mContext.getResources().getDrawable(R.drawable.ic_completed, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            } else {
                drawable = VectorDrawableCompat.create(mContext.getResources(), R.drawable.ic_completed, mContext.getTheme());

                markerOptions.icon(bitmapDescriptorFromVector(mContext, drawable));
            }
        } else if (eventInfo.getStatusModel().getStatus().equals("started on time")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }

//        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//
//        markerOptions.icon(markerDescriptor).snippet(item.getTitle());
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, Drawable vectorDrawable) {
        //Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
