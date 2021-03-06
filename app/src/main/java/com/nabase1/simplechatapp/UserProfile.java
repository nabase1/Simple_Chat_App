package com.nabase1.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.nabase1.simplechatapp.databinding.ActivityUserProfileBinding;
import com.nabase1.simplechatapp.model.Users;

public class UserProfile extends AppCompatActivity {

    private ActivityUserProfileBinding mProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        Users users = getIntent().getParcelableExtra("users");
        mProfileBinding.setUser(users);
    }
}