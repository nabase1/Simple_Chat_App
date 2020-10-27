package com.nabase1.simplechatapp;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabase1.simplechatapp.databinding.FragmentGroupChatListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentGroupChatListBinding mChatBinding;
    private MyAdapter mMyAdapter;
    MessageDetails mMessageDetails;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    String uid;
    String userName;
    users mUsers;
    private View item;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatFragment newInstance(String param1, String param2) {
        GroupChatFragment fragment = new GroupChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        mMessageDetails = new MessageDetails();
        mUsers = new users();

        checkUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mChatBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_group_chat_list, container, false);
        item = mChatBinding.getRoot();

        initialize(mChatBinding.recylclerView);

        mChatBinding.sendButton.setOnClickListener(View -> saveGroupChat());

        return item;
    }

    private void checkUser(){
        mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null){
            uid = mFirebaseUser.getUid();
        }

    }

    /* initialize recyclerView */
    private void initialize(RecyclerView recyclerView){

        FirebaseUtils.openFirebaseUtils("chat room", getActivity());
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if(layoutManager == null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }

        displayChats(mChatBinding.recylclerView);

    }

    /* display chats on recyclerView */
    private void displayChats(RecyclerView recyclerView){
        mMyAdapter = (MyAdapter) recyclerView.getAdapter();
        if(mMyAdapter == null){
            mMyAdapter = new MyAdapter();
            recyclerView.setAdapter(mMyAdapter);
        }
    }

    public void saveGroupChat(){
        mMessageDetails.setSenderId(uid);
        mMessageDetails.setSenderName(userName);
        mMessageDetails.setMessage(mChatBinding.editTextChart.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chat room").child("technical issues");
        if(!mChatBinding.editTextChart.getText().toString().isEmpty()) {
            if(!uid.equals("")){
                databaseReference.push().setValue(mMessageDetails);
            }
            mChatBinding.editTextChart.setText("");
            displayChats(mChatBinding.recylclerView);
        }
    }
}