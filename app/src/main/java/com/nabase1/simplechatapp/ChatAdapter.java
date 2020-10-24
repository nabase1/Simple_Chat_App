package com.nabase1.simplechatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nabase1.simplechatapp.databinding.ChatItemsBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    List<MessageDetails> mMessageDetailsList;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mDatabaseReference;
    private Context mContext;

    public ChatAdapter() {
        mMessageDetailsList = new ArrayList<>();
        mDatabaseReference = FirebaseUtils.databaseReference;

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Log.d("snapshot1 key", dataSnapshot1.getKey());
                            MessageDetails messageDetails = dataSnapshot1.getValue(MessageDetails.class);
                            messageDetails.setId(dataSnapshot1.getKey());
                            mMessageDetailsList.add(messageDetails);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        ChatItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.chat_items, parent, false);

        return new ChatViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        MessageDetails messageDetails = mMessageDetailsList.get(position);

        holder.bind(messageDetails);
        holder.mChatItemsBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mMessageDetailsList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        ChatItemsBinding mChatItemsBinding;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatItemsBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(MessageDetails messageDetails){
            Log.d("message", messageDetails.getMessage());
            mChatItemsBinding.textViewName.setText(messageDetails.getSenderName());
            mChatItemsBinding.textViewMessage.setText(messageDetails.getMessage());

        }
    }
}
