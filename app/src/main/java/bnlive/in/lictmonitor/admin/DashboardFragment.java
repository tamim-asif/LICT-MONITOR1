package bnlive.in.lictmonitor.admin;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.BatchStatusModel;

/**
 * Created by Sk Faisal on 3/25/2018.
 */

public class DashboardFragment extends Fragment{
    View view;
    Context context;
    private RecyclerView myListView;
    private CustomStatusAdapter adapter;
    private List<BatchStatusModel> dataList;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore db;
    private String TAG="dashboardfragment";
    private  boolean isDataUpdated;
    private SearchView searchView;
    public boolean isDataUpdated() {
        return isDataUpdated;
    }

    public void setDataUpdated(boolean dataUpdated) {
        isDataUpdated = dataUpdated;
    }

    public List<BatchStatusModel> getDataList() {
        return dataList;
    }

    NotificationManager notif;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_dshboardfragment,container,false);
        context=getActivity().getBaseContext();

       Toast.makeText(context,"Service will be started",Toast.LENGTH_LONG).show();
        db=FirebaseFirestore.getInstance();
        myListView=view.findViewById(R.id.statusRecycle);

        myListView.setHasFixedSize(true);
        dataList=new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        myListView.setLayoutManager(mLayoutManager);
        searchView=view.findViewById(R.id.searchview);
        notif=(NotificationManager)getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        BatchStatusModel statusModel=new BatchStatusModel();
//        statusModel.setDate("03/26/2018");
//        statusModel.setBatch_code("tup-us-40");
//        statusModel.setStatus("Start");
//        dataList.add(statusModel);
//        statusModel=new BatchStatusModel();
//        statusModel.setDate("03/26/2018");
//        statusModel.setBatch_code("tup-us-41");
//        statusModel.setStatus("Start");
       // dataList.add(statusModel);



//        adapter.notifyDataSetChanged();
        realtimeUpdate();

        return view;
    }
public void searchView()
{
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            adapter.filter(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.filter(newText);
            return true;
        }
    });

}
    public void setDataList(List<BatchStatusModel> dataList) {
        this.dataList = dataList;
        adapter=new CustomStatusAdapter(dataList,getActivity().getBaseContext());
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    long currenttime;


    public void realtimeUpdate()
    {
        Calendar calendar=Calendar.getInstance();
        String timestamp=new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        db.collection("batch_status")
             //   .whereEqualTo("date",timestamp)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        if(e!=null)
                        {
                            Log.d(TAG,"Error: "+e);
                        }
                        if(querySnapshot!=null)
                        {
                            List<BatchStatusModel> list=new ArrayList<>();
                            for(DocumentSnapshot snapshot:querySnapshot)
                            {
                                String id=snapshot.getId();
                                BatchStatusModel model=snapshot.toObject(BatchStatusModel.class);
                                model.setId(id);
                                list.add(model);
                                Log.d(TAG,"Data: "+model.toString());
                            }
                            setDataList(list);
                            Log.d("DataCheck","Data add success! "+list.size());
setDataUpdated(true);
                            searchView();
                        }
                        else
                        {
                            Log.d("Data","Data null");
                        }
                    }
                });
    }
}
