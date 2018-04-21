package bnlive.in.lictmonitor.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.model.BatchStatusModel;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;
import bnlive.in.lictmonitor.model.TrainerDetailsModel;
import bnlive.in.lictmonitor.model.UniversityDetailsModel;

import static android.content.ContentValues.TAG;

/**
 * Created by Sk Faisal on 3/26/2018.
 */

public class CustomStatusAdapter extends RecyclerView.Adapter<CustomStatusAdapter.ViewHolder>{
    private String TAG="batchstatusadapter";
    private List<BatchStatusModel> mDataset;
    private List<BatchStatusModel> itemsCopy;
    private static Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView batchCodeText;
        public TextView statusText;
        public TextView dateText;
        public TextView trainerText;
        public TextView idBatch;
        public TextView universityText;
        public CardView cardView;
        public ViewHolder(View itemView) {

            super(itemView);
            batchCodeText=itemView.findViewById(R.id.batchcode);
            statusText=itemView.findViewById(R.id.status);
            dateText=itemView.findViewById(R.id.date);
            trainerText=itemView.findViewById(R.id.trainername);
            idBatch=itemView.findViewById(R.id.idbatch);
            universityText=itemView.findViewById(R.id.universityname);
            cardView=itemView.findViewById(R.id.carditem);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,BatchDetailsActivity.class);
                    MergeSheduleUniversity mn=new MergeSheduleUniversity();
                    BatchStatusModel bm=new BatchStatusModel();
                    bm.setId(idBatch.getText().toString());
                    mn.setStatusModel(bm);
                    mn.setUniversity(new UniversityDetailsModel());
                    mn.setTrainerDetailsModel(new TrainerDetailsModel());
                    Gson gson=new Gson();
                    String data=gson.toJson(mn);
                    Log.d("checkSingle",data);
                    intent.putExtra("data",data);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);
                   // Toast.makeText(context,"Batch Code: "+idBatch.getText().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public CustomStatusAdapter(List<BatchStatusModel> myDataset,Context context)
    {

        mDataset=myDataset;
        itemsCopy=new ArrayList<>();
        itemsCopy.addAll(myDataset);
        this.context=context;

        Log.d(TAG,"DatasetSize: "+mDataset.size());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statusrecycleitem, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        Log.d(TAG,"Debugger in viewholder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    holder.batchCodeText.setText(""+mDataset.get(position).getBatch_code());
    holder.statusText.setText("Status: "+mDataset.get(position).getStatus());
    holder.idBatch.setText(mDataset.get(position).getId());
    holder.dateText.setText("Date: "+mDataset.get(position).getDate());
    holder.trainerText.setText("Trainer Name: "+mDataset.get(position).getTrainer_name());
    holder.universityText.setText("University Name: "+mDataset.get(position).getUniversity_name());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"Size: "+mDataset.size());
        return mDataset.size();
    }
    public void filter(String text) {
        mDataset.clear();
        if(text.isEmpty()){
            mDataset.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(BatchStatusModel item: itemsCopy){
                if(item.getBatch_code().toLowerCase().contains(text) || item.getTrainer_name().toLowerCase().contains(text)||item.getUniversity_name().toLowerCase().contains(text)||item.getStatus().toLowerCase().contains(text)){
                    mDataset.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

}
