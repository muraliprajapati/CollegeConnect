package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Murali on 23/01/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button changeEmailButton, changeNameButton, changePasswordButton, saveButton;
    EditText emailEditText, nameEditText;
    boolean isVisible = false;
    boolean isSaveClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nameEditText = (EditText) findViewById(R.id.fullNameEditText);
        changeEmailButton = (Button) findViewById(R.id.emailChangeButton);
        changeNameButton.setVisibility(View.GONE);
        changeNameButton = (Button) findViewById(R.id.nameChangeButton);
        changeNameButton.setVisibility(View.GONE);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setVisibility(View.GONE);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_info_edit) {
            if (!isVisible && !isSaveClicked) {
                changeNameButton.setVisibility(View.VISIBLE);
                changeEmailButton.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                isVisible = true;
            } else {
//                changeNameButton.setVisibility(View.GONE);
//                changeEmailButton.setVisibility(View.GONE);
//                changePasswordButton.setVisibility(View.GONE);
//                saveButton.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "You have not saved info", Toast.LENGTH_SHORT).show();
                isVisible = false;

            }


        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                isSaveClicked = true;
                isVisible = false;
                changeNameButton.setVisibility(View.GONE);
                changeEmailButton.setVisibility(View.GONE);
                changePasswordButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                Toast.makeText(UserProfileActivity.this, "Info saved", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nameChangeButton:

                break;
        }
    }
}
