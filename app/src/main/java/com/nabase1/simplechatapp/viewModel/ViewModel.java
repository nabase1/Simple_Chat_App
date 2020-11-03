package com.nabase1.simplechatapp.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.nabase1.simplechatapp.model.MessageDetails;
import com.nabase1.simplechatapp.model.Users;
import com.nabase1.simplechatapp.repository.Repo;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessageDetails>> mMessageList;
    private MutableLiveData<List<Users>> mUserList;
    private Fragment mFragment;
    private Repo mRepo;

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Fragment context){
        mFragment = context;
        if(mMessageList!=null){
            return;
        }
        mRepo = new Repo(mFragment);
        mMessageList = mRepo.getMessageList();
        mUserList = mRepo.getContactList();
    }

    public void checkUser(){
        mRepo.checkUser();
    }

    public LiveData<List<MessageDetails>> getMessage(){
        return mMessageList;
    }

    public LiveData<List<Users>> getContactList(){
        return mUserList;
    }

    public void saveGroupChat(MessageDetails messageDetails, String msg, String chatName){
         mRepo.saveGroupChat(messageDetails, msg, chatName);
    }
}
