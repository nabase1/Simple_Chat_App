package com.nabase1.simplechatapp.repository;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.nabase1.simplechatapp.FbDataSource;
import com.nabase1.simplechatapp.model.MessageDetails;
import com.nabase1.simplechatapp.model.Users;

import java.util.List;

public class Repo {
    private MutableLiveData<List<MessageDetails>> mMessageList;
    private MutableLiveData<List<Users>> mUsersList;
    private final FbDataSource mDataSource;

    public Repo (Fragment fragment) {
        mDataSource = FbDataSource.getInstance(fragment);
        mMessageList = mDataSource.getMessageList();
        mUsersList = mDataSource.getContactList();
    }

    public void checkUser(){
        mDataSource.checkUser();
    }

    public MutableLiveData<List<MessageDetails>> getMessageList(){
        return  mMessageList;
    }

    public MutableLiveData<List<Users>> getContactList(){
        return mUsersList;
    }

    public void saveGroupChat(MessageDetails messageDetails, String msg, String chatName){
        mDataSource.saveGroupChat(messageDetails, msg, chatName);
    }


}
