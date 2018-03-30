package bnlive.in.lictmonitor.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.BatchStatusModel;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;
import bnlive.in.lictmonitor.model.TrainerDetailsModel;
import bnlive.in.lictmonitor.model.UniversityDetailsModel;

/**
 * Created by Sk Faisal on 3/26/2018.
 */

public class AdminMapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mapView;
    private UniversityDetailsModel universityDetailsModel;
    private  List<MergeSheduleUniversity> listMerge;
    FirebaseFirestore db;
    private boolean isDataUpdated=false;
    String TAG="adminmapfragment";
    View view;
    Bundle savedInstanceState;
    public boolean isActivityDistroyed=false;
    public int datasize=0;
    Handler handler;
    private boolean isMappUpdated=false;
private TextView cancelledText;
private TextView ongoingText;
private TextView startOnTimeText;
private TextView sheduledTimeText;
private TextView notdisplayingTimeText;
private TextView delayTimeText;
private TextView completedsuccessfullyText;
private TextView totalText;
private TextView totalDisplaying;
String[] summeryData;
public void initStatusText()
{
    cancelledText=view.findViewById(R.id.textView18);
    ongoingText=view.findViewById(R.id.textView23);
    startOnTimeText=view.findViewById(R.id.textView21);
    sheduledTimeText=view.findViewById(R.id.textView22);
    notdisplayingTimeText=view.findViewById(R.id.textView25);
    completedsuccessfullyText=view.findViewById(R.id.textView19);
    delayTimeText=view.findViewById(R.id.textView20);
    totalText=view.findViewById(R.id.textView24);
    totalDisplaying=view.findViewById(R.id.textView26);
}
public void setValue(String[] strings)
{
    cancelledText.setText("Calcelled: "+strings[0]);
    ongoingText.setText("Ongoing: "+strings[1]);
    startOnTimeText.setText("Started On Time: "+strings[2]);
    sheduledTimeText.setText("Scheduled: "+strings[3]);
    notdisplayingTimeText.setText("Not Displaying: "+strings[4]);
    completedsuccessfullyText.setText("Completed Successfully: "+strings[6]);
    delayTimeText.setText("Delay: "+strings[5]);
    totalText.setText("Total: "+strings[7]);
    totalDisplaying.setText("Total Displaying: "+strings[8]);
}
    class MapThread extends Thread{
        int currentSize=0;

     public void run()
     {
         while (true) {
             try {
                 if(isActivityDistroyed==true)
                 {
                     Log.d("mapcheck","Thread distroyed");
                     currentThread().isInterrupted();
                     break;
                 }
                 Thread.sleep(100);
                 Log.d("mapcheck", "Thread Datasize: " + listMerge.size());
                 if(currentSize!=listMerge.size()){
                     handler.post(new Runnable() {
                         @Override
                         public void run() {
                             mapFunc();
                             onResume();
                         }
                     });

                     Log.d("mapcheck", "Changed Datasize: " + listMerge.size());
                     currentSize=listMerge.size();
                 }
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_batch_maps,container,false);
        this.savedInstanceState=savedInstanceState;
        db=FirebaseFirestore.getInstance();
        Log.d(TAG, "Fragment Started ");
        listMerge=new ArrayList<MergeSheduleUniversity>();
        MapThread mapThread=new MapThread();
        handler=new Handler();
        mapThread.start();
        realtimeupdate();
summeryData=new String[8];
        initStatusText();

        return view;
    }
    public void calculateSummeryData(List<MergeSheduleUniversity> mlist)
    {
        int cancelled = 0,ongoing = 0,startOnTime = 0,sheduled = 0,notdisplaying,delay = 0,completedsuccessfully = 0,total;
        for(MergeSheduleUniversity eventInfo:mlist)
        {
            if(eventInfo.getStatusModel().getStatus().equals("scheduled"))
            {
                sheduled++;
            }
            else if(eventInfo.getStatusModel().getStatus().equals("ongoing"))
            {
                ongoing++;
            }
            else if(eventInfo.getStatusModel().getStatus().equals("late"))
            {
                delay++;
            }
            else if(eventInfo.getStatusModel().getStatus().equals("cancelled"))
            {
                cancelled++;
            }
            else if(eventInfo.getStatusModel().getStatus().equals("completed"))
            {
                completedsuccessfully++;
            }
            else if(eventInfo.getStatusModel().getStatus().equals("started on time"))
            {
                startOnTime++;
            }
            notdisplaying=listMerge.size()-(sheduled+ongoing+delay+cancelled+completedsuccessfully+startOnTime);
           total=listMerge.size();
           int totaldisplaying=sheduled+ongoing+delay+cancelled+completedsuccessfully+startOnTime;
           summeryData=new String[9];
//            cancelledText.setText(strings[0]);
//            ongoingText.setText(strings[1]);
//            startOnTimeText.setText(strings[3]);
//            sheduledTimeText.setText(strings[4]);
//            notdisplayingTimeText.setText(strings[5]);
//            completedsuccessfullyText.setText(strings[6]);
//            delayTimeText.setText(strings[7]);
//            totalText.setText(strings[8]);
//            totalDisplaying.setText(strings[9]);
            summeryData[0]=""+cancelled;
            summeryData[1]=""+ongoing;
            summeryData[2]=""+startOnTime;
            summeryData[3]=""+sheduled;
            summeryData[4]=""+notdisplaying;
            summeryData[5]=""+completedsuccessfully;
            summeryData[6]=""+delay;
            summeryData[7]=""+total;
            summeryData[8]=""+totaldisplaying;
            setValue(summeryData);
         //   summeryData[10]=""+totaldisplaying;
        }

    }
void mapFunc()
{
    mapView = (MapView) view.findViewById(R.id.map);
    mapView.onCreate(savedInstanceState);
    mapView.onResume();
    mapView.getMapAsync(this);
    calculateSummeryData(listMerge);
}
    @Override
    public void onResume(){
        super.onResume();

        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();
mapFunc();
            // add markers from database to the map
        }
    }
MainMapFragment mapFragment;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapFragment=new MainMapFragment();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        int mIndex=listMerge.size()/2;

        final HashMap<Marker,MergeSheduleUniversity> markermap=new HashMap<>();
        for(int i=0;i<listMerge.size();i++)
        {
            MergeSheduleUniversity mergeSheduleUniversity=listMerge.get(i);

            String ltln=listMerge.get(i).getUniversity().getLat_long();
            String[] str=ltln.split(",");
//         Marker marker=mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(str[0]),Double.parseDouble(str[1]))).title("Batch: "+mergeSheduleUniversity.getBatchCode()).snippet(""+mergeSheduleUniversity.getStatus()));

Marker marker=mapFragment.placeMarker(getActivity(),mergeSheduleUniversity,mMap);
markermap.put(marker,mergeSheduleUniversity);
            if(i==mIndex)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(str[0]),Double.parseDouble(str[1]))));

            Log.d(TAG,"Lat: "+str[0]+" Long: "+str[1]);
//marker.showInfoWindow();

        }
mMap.animateCamera(CameraUpdateFactory.zoomTo( 7.0f ));

mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
mMap.getUiSettings().setAllGesturesEnabled(true);
mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {
        Gson gson=new Gson();
        MergeSheduleUniversity mergeSheduleUniversity=markermap.get(marker);
        BatchDetailsActivity batchDetailsActivity=new BatchDetailsActivity();
        batchDetailsActivity.setData(mergeSheduleUniversity);
        String str=gson.toJson(mergeSheduleUniversity);
        Intent intent=new Intent(getActivity().getBaseContext(),batchDetailsActivity.getClass());
        intent.putExtra("data",str);
        startActivity(intent);
        Toast.makeText(getContext(),"Location: "+mergeSheduleUniversity.getUniversity().getAddress(),Toast.LENGTH_LONG).show();
    }
});
        //mMap.setZ
        Log.d("mapcheck","Dataset Size: "+listMerge.size());
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void realtimeupdate()
    {

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

       Log.d("dateCheck",""+timeStamp);

        db.collection("batch_status")

             //  .whereEqualTo("date","29/03/2018")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        if(e!=null)
                        {
                           // Log.d(TAG,"Error: "+e);
                        }
                        if(querySnapshot!=null)
                        {

                            for(DocumentSnapshot snapshot:querySnapshot)
                            {
                                String id=snapshot.getId();
                                BatchStatusModel model=snapshot.toObject(BatchStatusModel.class);
                                model.setId(id);


                                        getUniversity(model,model.getUniversity_name());
                                Log.d("debugcheck","Data Added: "+model.toString());
                               // UniversityDetailsModel umodel=getUniversityDetailsModel();

//                                list.add(model);
                               // Log.d(TAG,"Data: "+model.toString());
                            }
                         //   mapFunc();
                            Log.d("mapcheck","Flag set to true");
                            Log.d("mapcheck","DataSet: "+listMerge.size());
//                            setListMerge(list);
                           // setDataList(list);
                            for (DocumentChange documentChange:querySnapshot.getDocumentChanges())
                            {
                                switch (documentChange.getType())
                                {
                                    case ADDED:
                                        String id=documentChange.getDocument().getId();
                                        BatchStatusModel model=documentChange.getDocument().toObject(BatchStatusModel.class);
                                        model.setId(id);

                                        break;
                                    case MODIFIED:
                                        String id2=documentChange.getDocument().getId();
                                        BatchStatusModel model2=documentChange.getDocument().toObject(BatchStatusModel.class);
                                        model2.setId(id2);
                                       // Log.d(TAG,"Data Modified: "+model2.toString());
//                                            Notification notification=new Notification.Builder
//                                                    (getActivity().getApplicationContext()).setContentTitle("Batch Status Updated!").setContentText("Batch "+model2.getBatch_code()+" has been "+model2.getStatus()).
//                                                    setContentTitle("Date: "+model2.getDate()).setSmallIcon(R.drawable.ic_menu_gallery).build();
//
//                                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                                            notif.notify(1, notification);
                                        break;
                                    case REMOVED:
                                        String id3=documentChange.getDocument().getId();
                                        BatchStatusModel model3=documentChange.getDocument().toObject(BatchStatusModel.class);
                                        model3.setId(id3);
//                                        Log.d(TAG,"Data Modified: "+model3.toString());
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    int iterator=0;
    public UniversityDetailsModel getUniversity(final BatchStatusModel bmodel, final String universityname)
    {

      db.collection("university_details")
        .whereEqualTo("university_name",universityname)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  QuerySnapshot document = task.getResult();
                    if (document != null) {
                        for(DocumentSnapshot doc:document){
                            setUniversityDetailsModel(bmodel,doc.toObject(UniversityDetailsModel.class));
                            iterator++;
                            Log.d(TAG, "DocumentSnapshot data: "+iterator+" " + doc.toObject(UniversityDetailsModel.class).toString());
                           // setUniversityDetailsModel(umodel);
                        }
                    isDataUpdated=true;
                     //  UniversityDetailsModel umodel=new UniversityDetailsModel(""+document.get("address"),""+document.get("lat_long"),""+document.get("location"),""+document.get("university_name"));
                        //umodel=document.getData().to
                       //
                    } else {
                       Log.d(TAG, "No such document named "+universityname);
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return getUniversityDetailsModel();

    }

    public UniversityDetailsModel getUniversityDetailsModel() {
        return universityDetailsModel;
    }

    public void setUniversityDetailsModel(BatchStatusModel bmodel,UniversityDetailsModel universityDetailsModel) {

        this.universityDetailsModel = universityDetailsModel;
        BatchStatusModel batchStatusModel=new BatchStatusModel();
        batchStatusModel.setId(bmodel.getBatch_code());
        batchStatusModel.setStatus(bmodel.getStatus());
      // ,,universityDetailsModel

        MergeSheduleUniversity m=new MergeSheduleUniversity();
        m.setStatusModel(batchStatusModel);
        m.setUniversity(universityDetailsModel);
       // listMerge.add(m);
        getTrainerDetails(bmodel,universityDetailsModel);
        Log.d(TAG,"reached! "+listMerge.size()+" Data: "+m.toString());
//        if(listMerge.size()==5)
//            mapFunc();
    }
    public void getUniversity(final BatchStatusModel bmodel)
    {

        db.collection("university_details")
                .whereEqualTo("university_name",bmodel.getUniversity_name())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (document != null) {
                                DocumentSnapshot doc2=null;
                                for(DocumentSnapshot doc:document){

                                    Log.d(TAG, "DocumentSnapshot data:  " + doc.toObject(UniversityDetailsModel.class).toString());
                                    // setUniversityDetailsModel(umodel);
                                    Log.d("mergedata", "University "+doc.toObject(UniversityDetailsModel.class));
                                    doc2=doc;
                                }
                                getTrainerDetails(bmodel,doc2.toObject(UniversityDetailsModel.class));
                                //  UniversityDetailsModel umodel=new UniversityDetailsModel(""+document.get("address"),""+document.get("lat_long"),""+document.get("location"),""+document.get("university_name"));
                                //umodel=document.getData().to
                                //
                            } else {
                                Log.d(TAG, "No such document named "+bmodel.getUniversity_name());
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


    }

    private void getTrainerDetails(final BatchStatusModel bmodel,final UniversityDetailsModel universityDetailsModel) {
        Log.d("mergedata", "Query should be trainer details and name:  "+bmodel.getTrainer_name());
        Task<QuerySnapshot> c= db.collection("trainer_details")
                .whereEqualTo("name",bmodel.getTrainer_name())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (document != null) {
                                DocumentSnapshot doc2=null;
                                TrainerDetailsModel tmodel=null;
                                for(DocumentSnapshot doc:document){
                                tmodel=doc.toObject(TrainerDetailsModel.class);
                                    Log.d(TAG, "DocumentSnapshot data:  " + doc.toObject(TrainerDetailsModel.class).toString());
                                    // setUniversityDetailsModel(umodel);
                                    Log.d("mergedata1", "Trainer Details "+doc.toObject(TrainerDetailsModel.class).toString());
                                    doc2=doc;

                                }
MergeSheduleUniversity muniversity=new MergeSheduleUniversity(bmodel,tmodel,universityDetailsModel);
                                listMerge.add(muniversity);
                                Log.d("mymodel",muniversity.toString());
                                //addMergeList(new MergeSheduleUniversity(bmodel,doc2.toObject(TrainerDetailsModel.class),universityDetailsModel));
                                //  UniversityDetailsModel umodel=new UniversityDetailsModel(""+document.get("address"),""+document.get("lat_long"),""+document.get("location"),""+document.get("university_name"));
                                //umodel=document.getData().to
                                //
                            } else {
                                Log.d(TAG, "No such document named "+bmodel.getUniversity_name());
                                Log.d("mergedata1", "No such document named "+bmodel.getTrainer_name());
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                            Log.d("mergedata1", "Error in trainer_details "+ task.getException());
                        }
                    }
                });

    }
    public List<MergeSheduleUniversity> getListMerge() {
        return listMerge;
    }

    public void setListMerge(List<MergeSheduleUniversity> listMerge) {
        for(MergeSheduleUniversity university:listMerge)
        {
            UniversityDetailsModel universityDetailsModel=university.getUniversity();
           // Log.d(TAG,""+universityDetailsModel.toString());
        }
        this.listMerge = listMerge;
    }

    public void setDataUpdated(boolean dataUpdated) {
        isDataUpdated = dataUpdated;
    }

    @Override
    public void onDestroy() {
        isActivityDistroyed=true;
        super.onDestroy();
    }
}
