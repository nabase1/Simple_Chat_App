package com.nabase1.simplechatapp.adapter;

import android.content.Context;
import android.util.Log;
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
import com.nabase1.simplechatapp.util.FirebaseUtils;
import com.nabase1.simplechatapp.model.MessageDetails;
import com.nabase1.simplechatapp.R;
import com.nabase1.simplechatapp.databinding.ChatItemsBinding;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.FoodViewHolder>{


    private FirebaseAuth mAuth;
    private List<MessageDetails> mMessageDetails;

    public MyAdapter(List<MessageDetails> messageDetails){

        mAuth = FirebaseAuth.getInstance();
        mMessageDetails = messageDetails;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        ChatItemsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.chat_items, parent, false);


        return new FoodViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

       MessageDetails messageDetails = mMessageDetails.get(position);
       String uid = mAuth.getCurrentUser().getUid();
       if(!uid.equals(messageDetails.getSenderId())){
           holder.mBinding.cardViewReceiver.setVisibility(View.GONE);
           holder.mBinding.cardViewFrom.setVisibility(View.VISIBLE);
       }else {
           holder.mBinding.cardViewReceiver.setVisibility(View.VISIBLE);
           holder.mBinding.cardViewFrom.setVisibility(View.GONE);
       }

        holder.mBinding.setChat(messageDetails);
        holder.mBinding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return mMessageDetails.size();
    }

    public void update(List<MessageDetails> messageDetails){
        this.mMessageDetails = messageDetails;
        notifyDataSetChanged();
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
