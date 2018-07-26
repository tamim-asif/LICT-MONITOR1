package bnlive.in.lictmonitor.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.BatchStatusModel;

public class HistoryFragment extends Fragment {
    View view;
    private Spinner batchSpinner;
    private FirebaseFirestore db;
    private Spinner dateSpinner;
    private ImageView imageView;
    FirebaseStorage storage;
    StorageReference reference;
    private Button getImageBtn;
    boolean flag;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private TextView attendence;
    private TextView startTime;
    private TextView endTime;
    private TextView trainerName;
    List<BatchStatusModel> batchList;
    AdminNav parent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_history,container,false);
        batchSpinner=view.findViewById(R.id.batchcode);
        dateSpinner=view.findViewById(R.id.date);
        imageView=view.findViewById(R.id.displayImage);
        db=FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        getImageBtn=view.findViewById(R.id.getImageBtn);
        layout1=view.findViewById(R.id.layout1);
        layout2=view.findViewById(R.id.layout2);
        layout3=view.findViewById(R.id.layout3);
        layout4=view.findViewById(R.id.layout4);
        parent=(AdminNav) getActivity();
        attendence=view.findViewById(R.id.attendenceText);
        startTime=view.findViewById(R.id.txtBatchStarted);
        endTime=view.findViewById(R.id.txtBatchFinished);
        trainerName=view.findViewById(R.id.txtTrainerName);
        flag=true;
        getImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String batch=batchSpinner.getSelectedItem().toString();
                String date=dateSpinner.getSelectedItem().toString();
                SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy");
                String outText="";
                try {
                    Date inText=input.parse(date);
                     outText=output.format(inText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String imageName=batch+" "+""+outText+" "+".jpg";
                StorageReference imgRef=reference.child(imageName);
                StorageReference pathReference = reference.child("/"+imageName);
                Log.d("path",""+pathReference.getPath());
                Log.d("path",pathReference.getDownloadUrl().toString());
                Glide.with(getActivity().getBaseContext())
                        .using(new FirebaseImageLoader())
                        .load(pathReference)
                        .into(imageView);
            }
        });
        reference=storage.getReference();
        realtimeUpdate();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
selectImage();
            }
        });
        return view;
    }



    public void selectImage()
    {
        if(flag==true)
        {
            dateSpinner.setVisibility(View.GONE);
            batchSpinner.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
            Display display = getActivity().getWindowManager().getDefaultDisplay();

            int width = display.getWidth(); // ((display.getWidth()*20)/100)
            int height = display.getHeight();// ((display.getHeight()*30)/100)
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            imageView.setLayoutParams(parms);

            flag=false;
        }
        else
        {
            dateSpinner.setVisibility(View.VISIBLE);
            batchSpinner.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);


            int width = 400; // ((display.getWidth()*20)/100)
            int height = 400;// ((display.getHeight()*30)/100)
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            imageView.setLayoutParams(parms);
            flag=true;
        }

    }
    public void setDataText(BatchStatusModel batchStatusModel)
    {
        trainerName.setText(batchStatusModel.getTrainer_name());
        startTime.setText(batchStatusModel.getStart());
        endTime.setText(batchStatusModel.getEnd());
        attendence.setText(batchStatusModel.getAttendance());
    }
    List<BatchStatusModel> list;
    public void realtimeUpdate()
    {
        Calendar calendar=Calendar.getInstance();
        String timestamp=new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        db.collection("batch_status")
              //  .whereEqualTo("date",timestamp)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        if(e!=null)
                        {
                         //   Log.d(TAG,"Error: "+e);
                        }
                        if(querySnapshot!=null)
                        {
                      list=new ArrayList<>();
                            String[] strArray=new String[2];
                            int i=0;
                            for(DocumentSnapshot snapshot:querySnapshot)
                            {
                                String id=snapshot.getId();
                                BatchStatusModel model=snapshot.toObject(BatchStatusModel.class);
                                model.setId(id);
                                list.add(model);
//                               strArray[i]=""+model.getBatch_code();
//                               i++;
                         //       Log.d(TAG,"Data: "+model.toString());
                            }
Log.d("batchStatusModel",list.toString());
                          //  setDataList(list);
                            Log.d("Data","Data add success!");
                          //  setDataUpdated(true);
                        //    searchView();
                            setSpinners(list);
                        }
                    }
                });
    }
    public void setSpinners(List<BatchStatusModel> list)
    {
        String[] batchList=new String[list.size()];
        String[] dateList=new String[list.size()];
        int i=0;
        for(BatchStatusModel model:list)
        {
            batchList[i]=""+model.getBatch_code();
            dateList[i]=""+model.getDate();
i++;
        }
        ArrayAdapter adapter=new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,batchList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchSpinner.setAdapter(adapter);
        ArrayAdapter adapter2=new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter2);
    }
}
