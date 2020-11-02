package com.nabase1.simplechatapp.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nabase1.simplechatapp.model.MessageDetails;
import com.nabase1.simplechatapp.repository.Repo;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessageDetails>> mMessageList;
    private Fragment mFragment;

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Fragment context){
        mFragment = context;
        if(mMessageList!=null){
            return;
        }
        mMessageList = Repo.getInstance(mFragment).getMessageList();
    }

    public LiveData<List<MessageDetails>> getMessage(){
        return mMessageList;
    }

    public void saveGroupChat(MessageDetails messageDetails, String s){
          Repo.getInstance(mFragment).saveGroupChat(messageDetails, s);
    }
}
