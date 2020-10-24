package com.nabase1.simplechatapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabase1.simplechatapp.databinding.ChatItemsBinding;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.FoodViewHolder>{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    ArrayList<MessageDetails> mMessageDetails;

    public MyAdapter(){

        mMessageDetails = new ArrayList<>();

        mFirebaseDatabase = FirebaseUtils.firebaseDatabase;
        mDatabaseReference = FirebaseUtils.databaseReference;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               // for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Log.d("snapshot1 key", dataSnapshot1.getKey());
                        MessageDetails messageDetails = dataSnapshot1.getValue(MessageDetails.class);
                        messageDetails.setId(dataSnapshot1.getKey());
                        mMessageDetails.add(messageDetails);
                 //   }

                }
                notifyItemChanged(mMessageDetails.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMessageDetails.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.d("snapshot1 key", dataSnapshot1.getKey());
                    MessageDetails messageDetails = dataSnapshot1.getValue(MessageDetails.class);
                    messageDetails.setId(dataSnapshot1.getKey());
                    mMessageDetails.add(messageDetails);
                    //   }

                }
                notifyItemChanged(mMessageDetails.size()-1);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);

    }



    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
//        View itemview = LayoutInflater.from(context)
//                .inflate(R.layout.foodlist, parent, false);
//
        ChatItemsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.chat_items, parent, false);


        return new FoodViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

       MessageDetails messageDetails = mMessageDetails.get(position);
        holder.mBinding.setChat(messageDetails);
        holder.mBinding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return mMessageDetails.size();
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder{

        ChatItemsBinding mBinding;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = DataBindingUtil.bind(itemView);

//            text_price.setPaintFlags(text_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }


    }

}
