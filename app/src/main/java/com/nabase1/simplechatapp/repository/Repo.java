package com.nabase1.simplechatapp.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabase1.simplechatapp.Dao;
import com.nabase1.simplechatapp.model.MessageDetails;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Repo {
    static Repo instance;
    static Context sContext;
    private List<MessageDetails> mMessageList;
    private static Dao mDao;
    static Fragment mFragment;

    public static Repo getInstance(Fragment fragment) {
       // sContext = context;
        mFragment = fragment;
        if(instance==null){
            instance = new Repo();
        }

        mDao = (Dao) mFragment;
        return instance;
    }

    public MutableLiveData<List<MessageDetails>> getMessageList(){

        mMessageList = new ArrayList<>();
        loadGroupMessage();

        MutableLiveData<List<MessageDetails>> messageDetails = new MutableLiveData<>();
        messageDetails.setValue(mMessageList);

        return  messageDetails;
    }

    public void saveGroupChat(MessageDetails messageDetails, String string){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child(string);
        databaseReference.push().setValue(messageDetails);
    }

    private void loadGroupMessage() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot : dataSnapshot1.getChildren()){
                        MessageDetails messageDetails = dataSnapshot.getValue(MessageDetails.class);
                        messageDetails.setId(dataSnapshot.getKey());
                        mMessageList.add(messageDetails);
                    }
                }

                mDao.loadGroupMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
