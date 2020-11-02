package com.nabase1.simplechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nabase1.simplechatapp.adapter.PagerAdapter;
import com.nabase1.simplechatapp.databinding.ActivityMainBinding;
import com.nabase1.simplechatapp.model.Users;
import com.nabase1.simplechatapp.util.FirebaseUtils;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    List<AuthUI.IdpConfig> providers;
    String uid;
    String userName;
    Users mUsers;
    private static final int My_Code = 1403;
    private PagerAdapter mPagerAdapter;
    private int tabCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseUtils.openFirebaseUtils("users",this);
        mDatabaseReference = FirebaseUtils.databaseReference;
        mUsers = new Users();

        mAuth = FirebaseAuth.getInstance();
        checkUser();

        tabCount = mBinding.tabLayout.getTabCount();

        mPagerAdapter = new PagerAdapter(this, tabCount);

        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.viewPager.setAdapter(mPagerAdapter);
    }

    private void checkUser(){
        mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null){
            uid = mFirebaseUser.getUid();
            userName = mFirebaseUser.getDisplayName();
            mUsers.setName(userName);
           // mUsers.setContact(mFirebaseUser.getPhoneNumber());

           // Toast.makeText(this, uid , Toast.LENGTH_SHORT).show();
        }
        else {
            displaySignInButtons();
        }
    }

    private void registerUser(){
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { ;
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(!dataSnapshot.getKey().equals(uid)){
                            saveProfile();
                        }
                    }
                }
                else {
                        saveProfile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveProfile(){
        if(mUsers.getName().equals("")){
            mUsers.setName("anonymous");
        }
        mUsers.setEmail(mFirebaseUser.getEmail());
        mUsers.setId(uid);
        mUsers.setStatus("Hy, Im on simple chat app now, lol");
        mUsers.setImageUrl("");
        mUsers.setContact("");

        mDatabaseReference.child(uid).setValue(mUsers);

        Log.d("regiestered", "registered!");

    }

    private void displaySignInButtons() {

        //arrays with the providers of authentication type
        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
               // new AuthUI.IdpConfig.EmailBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(), My_Code
        );

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == My_Code && resultCode == RESULT_OK){
            checkUser();
            registerUser();

        }
    }
}