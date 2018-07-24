package bnlive.in.lictmonitor.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

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
        return view;
    }
    List<BatchStatusModel> batchList;
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
                            List<BatchStatusModel> list=new ArrayList<>();
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
