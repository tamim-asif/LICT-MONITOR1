package bnlive.in.lictmonitor.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bnlive.in.lictmonitor.R;
import bnlive.in.lictmonitor.RegistrationLogin;

public class BlankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        Intent intent=new Intent(BlankActivity.this, RegistrationLogin.class);
        startActivity(intent);
    }
}
