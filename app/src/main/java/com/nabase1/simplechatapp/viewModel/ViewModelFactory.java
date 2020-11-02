package com.nabase1.simplechatapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    public ViewModelFactory(@NonNull Application application) {
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)  new com.nabase1.simplechatapp.viewModel.ViewModel(mApplication);
    }
}
