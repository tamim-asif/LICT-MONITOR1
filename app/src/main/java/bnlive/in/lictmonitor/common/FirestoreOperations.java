package bnlive.in.lictmonitor.common;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bnlive.in.lictmonitor.model.BatchStatusModel;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;
import bnlive.in.lictmonitor.model.TrainerDetailsModel;
import bnlive.in.lictmonitor.model.UniversityDetailsModel;

/**
 * Created by Sk Faisal on 4/19/2018.
 */

public class FirestoreOperations {
    FirebaseFirestore db;
    public FirestoreOperations()
    {
        db=FirebaseFirestore.getInstance();
    }
    List<BatchStatusModel> batchStatusModelList;
    List<UniversityDetailsModel> universityDetailsModelList;
    List<MergeSheduleUniversity> mergeSheduleUniversityList;
    List<TrainerDetailsModel> trainerDetailsModelList;
    private TrainerDetailsModel trainerDetailsModel;
    private UniversityDetailsModel universityDetailsModel;
    private BatchStatusModel batchStatusModel;
    boolean isBatchUpdated;
    boolean isUniversityUpdated;
    boolean isTrainerUpdated;
    boolean isMergeUpdated;
    public void setBatchStatusModelList()
    {
        batchStatusModelList=new ArrayList<>();
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        db.collection("batch_status")
                .whereEqualTo("date",timeStamp)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot doc:querySnapshot)
                        {

                            String id=doc.getId();
                            BatchStatusModel model=doc.toObject(BatchStatusModel.class);
                            model.setId(id);
                            batchStatusModelList.add(model);
                            Log.d("myTest","Batch Status "+batchStatusModelList.size()+" : "+model.toString());
                        }
                        isBatchUpdated=true;
                    }
                })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(Exception e) {

                   }
               });
    }
    public void setUniversityDetailsModelList()
    {
        universityDetailsModelList=new ArrayList<>();
        db.collection("university_details")

                .get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        for(DocumentSnapshot doc:querySnapshot)
                        {

                            UniversityDetailsModel universityDetailsModel= doc.toObject(UniversityDetailsModel.class);
                          universityDetailsModelList.add(universityDetailsModel);
                        Log.d("myTest","university "+universityDetailsModelList.size()+" : "+universityDetailsModel.toString());
                        }

isUniversityUpdated=true;

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("myTest","Failed University Details Model "+e);
                    }
                });

    }
    public void setTrainerDetailsModelList()
    {
        trainerDetailsModelList=new ArrayList<>();
        db.collection("trainer_details")

                .get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        for(DocumentSnapshot doc:querySnapshot)
                        {

                            TrainerDetailsModel trainerDetailsModel= doc.toObject(TrainerDetailsModel.class);

                            trainerDetailsModelList.add(trainerDetailsModel);
                            Log.d("myTest","Trainer "+trainerDetailsModelList.size()+" : "+trainerDetailsModel.toString());
                        }
isTrainerUpdated=true;


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("myTest","Failed University Details Model "+e);
                    }
                });
    }
    public void setMergeSheduleUniversityList()
    {
        mergeSheduleUniversityList=new ArrayList<>();
        for(BatchStatusModel batchStatusModel:batchStatusModelList)
        {
            UniversityDetailsModel u=null;
            TrainerDetailsModel t=null;
            boolean uflag=false;
            for(UniversityDetailsModel universityDetailsModel:universityDetailsModelList)
            {

                if(batchStatusModel.getUniversity_name().equals(universityDetailsModel.getUniversity_name()))
                {
                    u=universityDetailsModel;
                    uflag=true;
                    break;
                }


            }
            if(uflag==false)
            {
                u=new UniversityDetailsModel();
                u.setUniversity_name("University Data Empty");
            }
            boolean tflag = false;
            for(TrainerDetailsModel trainerDetailsModel:trainerDetailsModelList) {

                if (batchStatusModel.getTrainer_name().equals(trainerDetailsModel.getName())) {
                    t = trainerDetailsModel;
                    tflag = true;
                    break;
                }

            }
            if (tflag == false) {
                t = new TrainerDetailsModel();
                t.setName("Trainer Data Empty");
            }
            if(!t.getName().equals("Trainer Data Empty")&&(!u.getUniversity_name().equals("University Data Empty"))&&u.getLat_long()!=null)
            {
                MergeSheduleUniversity m=new MergeSheduleUniversity(batchStatusModel,t,u);
                mergeSheduleUniversityList.add(m);
                // if(m.getStatusModel().getTrainer_name().equals("Shaikh Faisal Hossain"))
                Log.d("myTest","Merge "+""+mergeSheduleUniversityList.size()+" : "+m.toString());
            }

        }

//        double offset=0.002;
//      //  int j=0;
//        for(int i=0;i<mergeSheduleUniversityList.size();i++)
//        {
//            //int i=0;
//            for(int j=0;j<mergeSheduleUniversityList.size();j++)
//            {
//                if(m.getUniversity().getLat_long().equals(m2.getUniversity().getLat_long()))
//                {
//                    if(m2!=m)
//                    {
//                       String ltln=m2.getUniversity().getLat_long();
//                       String[] ltlnArray=ltln.split(",");
//                       double modifyLat=Double.parseDouble(ltlnArray[0])+offset;
//                       String modifyLatLong=""+modifyLat+","+ltlnArray[1];
//                       offset=offset+offset;
//                      // m2.getUniversity().setLat_long(modifyLatLong);
//                        Log.d("duperror","Previouse Uni: "+m2.getUniversity().getUniversity_name()+" LatLong: "+m2.getUniversity().getLat_long());
//                       this.mergeSheduleUniversityList.get(j).getUniversity().setLat_long(""+modifyLatLong);
//
//                        Log.d("duperror","Present Uni: "+mergeSheduleUniversityList.get(i).getUniversity().getUniversity_name()+" LatLong: "+mergeSheduleUniversityList.get(j).getUniversity().getLat_long());
//
//                    }
//                }
//                //i++;
//            }
//            //j++;
//        }
      //  int k = 0;
        double offset = 0.002;
        List<MergeSheduleUniversity> list2=mergeSheduleUniversityList;
        for (int k=0;k<mergeSheduleUniversityList.size();k++) {
            MergeSheduleUniversity mu=mergeSheduleUniversityList.get(k);
            String latLong = mu.getUniversity().getLat_long();
           // int j = 0;
            for(int j=k+1;j<list2.size();j++) {
            MergeSheduleUniversity mu2=list2.get(j);
              // if (mu2 != mu) {
                    if (latLong.equals(mu2.getUniversity().getLat_long())) {
                        String lalo = mu2.getUniversity().getLat_long();
                        String[] str = lalo.split(",");
                        double dt=Double.parseDouble(str[0]);
                        double dt1=Double.parseDouble(str[1]);
                        double dd = dt1 + offset;
                        offset=offset+0.002;
                        //offset = offset + 0.02;
                        //mu2.getUniversity()
                        Log.d("ddetect","Previouse Batch Code: "+mu2.getStatusModel().getBatch_code()+" LatLong: "+ mu2.getUniversity().getLat_long());
                      //  mu.getUniversity().setLat_long(str[0] + "," + dd);
                      //  MergeSheduleUniversity mmm=mu;
                       // mmm.getUniversity().setLat_long(dt+","+dd);
                        UniversityDetailsModel um=new UniversityDetailsModel();
                                //um.setLat_long();
                        um.setLat_long(str[0]+","+dd);
                        um.setAddress(mu.getUniversity().getAddress());
                        um.setLocation(mu.getUniversity().getLocation());
                        um.setUniversity_name(mu.getUniversity().getUniversity_name());
//                        BatchStatusModel bm=new BatchStatusModel();
//                        bm.setBatch_code();
                               // mu2.getStatusModel();
                       // TrainerDetailsModel tn=mu2.getTrainerDetailsModel();
                       mergeSheduleUniversityList.set(k,new MergeSheduleUniversity(mu.getStatusModel(),mu.getTrainerDetailsModel(),um));
                        Log.d("ddetect","Changed Batch Code: "+ mergeSheduleUniversityList.get(k).getStatusModel().getBatch_code()+" LatLong: "+ mergeSheduleUniversityList.get(k).getUniversity().getLat_long());

                        //  }
                }
                //j++;
            }
           // k++;
        }
        for(MergeSheduleUniversity m:mergeSheduleUniversityList)
        {
            Log.d("ddetect","All Batch: "+m.getStatusModel().getBatch_code()+" LatLong: "+ m.getUniversity().getLat_long());
        }
    isMergeUpdated=true;
    }
public void getSingleBatchData(String batchCode)
{
    db.collection("batch_status").document(batchCode)

            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //   bm=null;
                    TrainerDetailsModel tn=null;
                    UniversityDetailsModel un=null;

                        BatchStatusModel  bm =documentSnapshot.toObject(BatchStatusModel.class);
                        bm.setId(documentSnapshot.getId());
                        setBatchStatusModel(bm);
                        Log.d("checkSingle",bm.toString());

                    // setBatchStatusModel(getBatchStatusModel());

                    if(getBatchStatusModel()!=null)
                    {
                        db.collection("trainer_details")
                                .whereEqualTo("name",getBatchStatusModel().getTrainer_name())
                                .get()

                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot documentSnapshots) {
                                        for(DocumentSnapshot documentSnapshot:documentSnapshots)
                                        {
                                            TrainerDetailsModel tn=documentSnapshot.toObject(TrainerDetailsModel.class);
                                            setTrainerDetailsModel(tn);
                                        }
                                        setTrainerUpdated(true);
                                        Log.d("checkSingle",getTrainerDetailsModel().toString());
                                    }
                                });
                        db.collection("university_details")
                                .whereEqualTo("university_name",getBatchStatusModel().getUniversity_name())
                                //.whereEqualTo("location",bm.getUniversity_name())
                                .get()

                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot documentSnapshots) {
                                        for(DocumentSnapshot documentSnapshot:documentSnapshots)
                                        {
                                            UniversityDetailsModel tn=documentSnapshot.toObject(UniversityDetailsModel.class);
                                            setUniversityDetailsModel(tn);
                                        }
                                        setUniversityUpdated(true);
                                        Log.d("checkSingle",getUniversityDetailsModel().toString());
                                    }
                                });
                    }
                }


            });
}
    public boolean isMergeUpdated() {
        return isMergeUpdated;
    }

    public void setMergeUpdated(boolean mergeUpdated) {
        isMergeUpdated = mergeUpdated;
    }

    public List<BatchStatusModel> getBatchStatusModelList() {
        return batchStatusModelList;
    }

    public List<UniversityDetailsModel> getUniversityDetailsModelList() {
        return universityDetailsModelList;
    }

    public List<MergeSheduleUniversity> getMergeSheduleUniversityList() {
        return mergeSheduleUniversityList;
    }

    public List<TrainerDetailsModel> getTrainerDetailsModelList() {
        return trainerDetailsModelList;
    }

    public boolean isBatchUpdated() {
        return isBatchUpdated;
    }

    public boolean isUniversityUpdated() {
        return isUniversityUpdated;
    }

    public boolean isTrainerUpdated() {
        return isTrainerUpdated;
    }

    public void setBatchUpdated(boolean batchUpdated) {
        isBatchUpdated = batchUpdated;
    }

    public void setUniversityUpdated(boolean universityUpdated) {
        isUniversityUpdated = universityUpdated;
    }

    public void setTrainerUpdated(boolean trainerUpdated) {
        isTrainerUpdated = trainerUpdated;
    }

    public TrainerDetailsModel getTrainerDetailsModel() {
        return trainerDetailsModel;
    }

    public void setTrainerDetailsModel(TrainerDetailsModel trainerDetailsModel) {
        this.trainerDetailsModel = trainerDetailsModel;
    }

    public UniversityDetailsModel getUniversityDetailsModel() {
        return universityDetailsModel;
    }

    public void setUniversityDetailsModel(UniversityDetailsModel universityDetailsModel) {
        this.universityDetailsModel = universityDetailsModel;
    }

    public BatchStatusModel getBatchStatusModel() {
        return batchStatusModel;
    }

    public void setBatchStatusModel(BatchStatusModel batchStatusModel) {
        this.batchStatusModel = batchStatusModel;
    }
}
