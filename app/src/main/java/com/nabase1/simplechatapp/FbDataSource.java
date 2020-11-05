package com.nabase1.simplechatapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabase1.simplechatapp.model.MessageDetails;
import com.nabase1.simplechatapp.model.Users;
import com.nabase1.simplechatapp.util.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class FbDataSource {

    static FbDataSource instance;
    private List<MessageDetails> mMessageList;
    private List<Users> mUsersList;
    private static Dao mDao;
    static Fragment mFragment;
    String uid;
    String userName;
    private  ChildEventListener mChildEventListener;

    public static FbDataSource getInstance(Fragment fragment){

        mFragment = fragment;

        if(instance == null){
            instance = new FbDataSource();
        }

        mDao = (Dao) mFragment;

        return instance;
    }

    public void checkUser(){
     FirebaseAuth  auth = FirebaseAuth.getInstance();
        FirebaseUser   mFirebaseUser = auth.getCurrentUser();
        if(mFirebaseUser != null){
            uid = mFirebaseUser.getUid();
            userName = mFirebaseUser.getDisplayName();
        }

    }

    public MutableLiveData<List<MessageDetails>> getMessageList(){

        mMessageList = new ArrayList<>();
        loadGroupMessage();

        MutableLiveData<List<MessageDetails>> messageDetails = new MutableLiveData<>();
        messageDetails.setValue(mMessageList);

        return  messageDetails;
    }

    public MutableLiveData<List<Users>> getContactList(){
        loadContactList();
        MutableLiveData<List<Users>> users = new MutableLiveData<>();
        users.setValue(mUsersList);

        return users;
    }

    public void saveGroupChat(MessageDetails messageDetails, String msg, String chatName){

        messageDetails.setSenderId(uid);
        messageDetails.setSenderName(userName);
        messageDetails.setMessage(msg);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child("group chat").child(chatName);
        if(!msg.isEmpty() && !uid.equals("")){
            databaseReference.push().setValue(messageDetails);
        }

    }

    public void saveIndividualChatMsg(MessageDetails messageDetails,String id, String msg){
        messageDetails.setSenderId(uid);
        messageDetails.setSenderName(userName);
        messageDetails.setMessage(msg);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child("one to one").child(uid).child(id);
        if(!msg.isEmpty() && !uid.equals("")) {
            databaseReference.push().setValue(messageDetails);
        }
    }

    private void loadGroupMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot : dataSnapshot1.getChildren()){
                        Log.d("snapshot1 key", dataSnapshot.getKey());
                        MessageDetails messageDetails = dataSnapshot.getValue(MessageDetails.class);
                        messageDetails.setId(dataSnapshot.getKey());
                        mMessageList.add(messageDetails);
                    }

                }

                mDao.loadGroupMessages();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mMessageList.clear();
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot : dataSnapshot1.getChildren()){
                        Log.d("snapshot1 key", dataSnapshot.getKey());
                        MessageDetails messageDetails = dataSnapshot.getValue(MessageDetails.class);
                        messageDetails.setId(dataSnapshot.getKey());
                        mMessageList.add(messageDetails);
                    }

                }
                mDao.loadGroupMessages();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadContactList(){
        mUsersList = new ArrayList<>();
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Users users = snapshot.getValue(Users.class);
                Log.d("users", users.getName());
                mUsersList.add(users);
                mDao.loadContactList();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addChildEventListener(mChildEventListener);


    }

    private void loadIndividualMessages(){

    }


}
