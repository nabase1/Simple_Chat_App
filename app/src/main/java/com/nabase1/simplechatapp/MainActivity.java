package com.nabase1.simplechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabase1.simplechatapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    List<AuthUI.IdpConfig> providers;
    String uid;
    String userName;
    users mUsers;
    private static final int My_Code = 1403;
    List<MessageDetails> mMessageDetailsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseUtils.openFirebaseUtils("users",this);
        mFirebaseDatabase = FirebaseUtils.firebaseDatabase;
        mDatabaseReference = FirebaseUtils.databaseReference;
        mUsers = new users();

        mMessageDetailsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        checkUser();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void checkUser(){
        mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null){
            uid = mFirebaseUser.getUid();
            userName = mFirebaseUser.getDisplayName();
            mUsers.setName(userName);
            mUsers.setContact(mFirebaseUser.getPhoneNumber());

           // Toast.makeText(this, uid , Toast.LENGTH_SHORT).show();
           // initialize(mBinding.recylclerView);
        }
        else {
            displaySignInButtons();
        }
    }

    private void registerUser(){
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot key", snapshot.getKey());
                Log.d("uid", uid);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().equals(uid)){
                        if(mUsers.getName().equals("")){
                            mUsers.setName("anonymous");
                        }
                        if(mUsers.getContact().equals("")){
                            mUsers.setContact("");
                        }

                        mUsers.setEmail(mFirebaseUser.getEmail());
                        mUsers.setId(uid);
                        mUsers.setStatus("");
                        mUsers.setImageUrl("");

                        mDatabaseReference.child(uid).setValue(mUsers);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

//private void getChats(){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child("technical issues");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        messages messages = snapshot.getValue(messages.class);
//                        mMessagesList.add(messages);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == My_Code && resultCode == RESULT_OK){
            registerUser();
            checkUser();
        }
    }
}