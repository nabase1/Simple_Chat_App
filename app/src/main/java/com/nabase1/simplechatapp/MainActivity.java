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
    MessageDetails mMessageDetails;
    private static final int My_Code = 1403;
    List<MessageDetails> mMessageDetailsList;
    private RecyclerView.LayoutManager mLinearLayoutManager;
    private ChatAdapter mChatAdapter;
    private MyAdapter mMyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseUtils.openFirebaseUtils("users",this);
        mFirebaseDatabase = FirebaseUtils.firebaseDatabase;
        mDatabaseReference = FirebaseUtils.databaseReference;
        mUsers = new users();
        mMessageDetails = new MessageDetails();
        mMessageDetailsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        checkUser();
        initialize(mBinding.recylclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /* initialize recyclerView */
    private void initialize(RecyclerView recyclerView){

        FirebaseUtils.openFirebaseUtils("chat room", this);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if(layoutManager == null){
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }

        displayChats(mBinding.recylclerView);

    }


    /* display chats on recyclerView */
    private void displayChats(RecyclerView recyclerView){
        mMyAdapter = (MyAdapter) recyclerView.getAdapter();

        if(mMyAdapter == null){
            mMyAdapter = new MyAdapter();
            recyclerView.setAdapter(mMyAdapter);
        }
    }

    private void checkUser(){
        mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null){
            uid = mFirebaseUser.getUid();
            userName = mFirebaseUser.getDisplayName();
            mUsers.setName(userName);

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
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
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

public void saveGroupChat(View view){
        mMessageDetails.setSenderId(uid);
        mMessageDetails.setSenderName(userName);
        mMessageDetails.setMessage(mBinding.editTextChart.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child("technical issues");
        if(!mBinding.editTextChart.getText().toString().isEmpty()){
            databaseReference.push().setValue(mMessageDetails);

            mBinding.editTextChart.setText("");

            mMyAdapter.notifyDataSetChanged();
        }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == My_Code && resultCode == RESULT_OK){
            registerUser();
            checkUser();
        }
    }
}