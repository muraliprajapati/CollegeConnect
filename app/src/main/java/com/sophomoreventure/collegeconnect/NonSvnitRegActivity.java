package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

/**
 * Created by Murali on 19/01/2016.
 */
public class NonSvnitRegActivity extends AppCompatActivity implements View.OnClickListener {
    String userName, password;
    EditText fullName, collegeName, mobileNo;
    Button registerButton;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_non_svnit_reg_layout);
        userName = getIntent().getStringExtra("user_name");
        password = getIntent().getStringExtra("password");
        fullName = (EditText) findViewById(R.id.name_edit_text);
        collegeName = (EditText) findViewById(R.id.college_name_edit_text);
        mobileNo = (EditText) findViewById(R.id.mobile_number_edit_text);
        registerButton = (Button) findViewById(R.id.reg_button);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_button) {
//            RequestorPost.requestJsonData(requestQueue,API.USER_REG_API,userName,password,this);
        }
    }

//    private JSONObject getJsonBody(String name,String collegeName,String mobNo){
//        return new JSONObject().put();
//    }
}
