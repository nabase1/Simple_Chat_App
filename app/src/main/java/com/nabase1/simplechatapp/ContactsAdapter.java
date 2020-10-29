package com.nabase1.simplechatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nabase1.simplechatapp.databinding.ContactsItemsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<users> mUsersList;
    private String uid;
    private FirebaseAuth mAuth;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;

    public ContactsAdapter() {

        mAuth = FirebaseAuth.getInstance();
        mUsersList = new ArrayList<>();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        ContactsItemsBinding itemsBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.contacts_items,parent,false);
        return new ContactsViewHolder(itemsBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        users users = mUsersList.get(position);

        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
        }

        holder.mBinding.setUser(users);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{

        ContactsItemsBinding mBinding;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
