package bnlive.in.lictmonitor.admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.common.FirestoreOperations;
import bnlive.in.lictmonitor.model.BatchStatusModel;
import bnlive.in.lictmonitor.model.MergeSheduleUniversity;
import bnlive.in.lictmonitor.model.TrainerDetailsModel;
import bnlive.in.lictmonitor.model.UniversityDetailsModel;

public class BatchDetailsActivity extends AppCompatActivity {
    private TextView university;
    private TextView batchcode;
    private TextView status;
    private TextView location;
    private TextView address;
    private TextView lat_long;
    private TextView trainername;
    private TextView trainercontact;
    private ImageButton callbtn;
    private MergeSheduleUniversity data;
    private TextView trainermail;
    private UniversityDetailsModel umodel;
    FirestoreOperations firestoreOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        batchcode = findViewById(R.id.textView4);
        university = findViewById(R.id.textView6);
        status = findViewById(R.id.textView8);
        address = findViewById(R.id.textView10);
        location = findViewById(R.id.textView12);
        lat_long = findViewById(R.id.textView14);
        trainername = findViewById(R.id.textView15);
        trainercontact = findViewById(R.id.textView16);
        callbtn = findViewById(R.id.imageButton);
        trainermail = findViewById(R.id.textView27);
        Gson gson = new Gson();
        data = gson.fromJson(getIntent().getStringExtra("data"), MergeSheduleUniversity.class);
        Log.d("dataname","Data: "+data);
       getAllData(data.getStatusModel().getId());


        callbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        callbtn.setAlpha(1.0f);
                        requestCallPermission(true, trainercontact.getText().toString());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        callbtn.setAlpha(0.5f);
                        break;
                }
                return false;
            }
        });

    }
public void getAllData(String batchCode)
{
    Log.d("checkSingle","Code: "+batchCode);
    firestoreOperations = new FirestoreOperations();
    firestoreOperations.getSingleBatchData(batchCode);
    final BatchStatusModel bm=firestoreOperations.getBatchStatusModel();
    final TrainerDetailsModel tn=firestoreOperations.getTrainerDetailsModel();
    final UniversityDetailsModel un=firestoreOperations.getUniversityDetailsModel();
    final Handler handler=new Handler();
    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            while(firestoreOperations.isTrainerUpdated()!=true&&firestoreOperations.isUniversityUpdated()!=true)
            {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(firestoreOperations.isTrainerUpdated()==true&&firestoreOperations.isUniversityUpdated()==true)
                {
                    final MergeSheduleUniversity mn=new MergeSheduleUniversity();
                    mn.setStatusModel(firestoreOperations.getBatchStatusModel());
                    mn.setTrainerDetailsModel(firestoreOperations.getTrainerDetailsModel());
                    mn.setUniversity(firestoreOperations.getUniversityDetailsModel());
                    Log.d("mydb","MyData: "+mn.toString());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            fillAllData(mn);
                        }
                    });

                }
               else Log.d("checkSingle","Data not updated! ");
            }
        }
    });
thread.start();
}
private void fillAllData(MergeSheduleUniversity data)
{

            umodel = data.getUniversity();
        batchcode.setText(data.getStatusModel().getBatch_code());
        if (umodel != null)
            university.setText(umodel.getUniversity_name());
        else
            university.setText(data.getStatusModel().getTrainer_name());
        status.setText(data.getStatusModel().getStatus());
        if (umodel != null)
            address.setText(umodel.getAddress());
        else
            address.setVisibility(View.GONE);
        if (umodel != null)
            location.setText(umodel.getLocation());
        else
            location.setVisibility(View.GONE);
        if (umodel != null)
            lat_long.setText(umodel.getLat_long());
        else
            lat_long.setVisibility(View.GONE);
        if (data.getTrainerDetailsModel() != null) {
            trainername.setText(data.getTrainerDetailsModel().getName());
            trainercontact.setText("0" + data.getTrainerDetailsModel().getMobile());
            trainermail.setText(data.getTrainerDetailsModel().getEmail());
         //   Log.d("contact", data.getTrainerDetailsModel().getMobile());
        } else {
            trainername.setText(data.getStatusModel().getTrainer_name());
            trainercontact.setText("Data not set");
            trainermail.setText("Data not set");
        }
}
    private void requestCallPermission(boolean flag, final String number) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            if (flag == true) {
                new AlertDialog.Builder(BatchDetailsActivity.this)
                        .setTitle("Warining!")
                        .setMessage("Are you sure to this number?\n"+number)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + number));
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.

                                    return;
                                }
                                startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        }
    }
    public MergeSheduleUniversity getData() {
        return data;
    }

    public void setData(MergeSheduleUniversity data) {
        this.data = data;
    }
}
