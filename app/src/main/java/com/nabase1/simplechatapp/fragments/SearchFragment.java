package com.nabase1.simplechatapp.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nabase1.simplechatapp.Dao;
import com.nabase1.simplechatapp.adapter.ContactsAdapter;
import com.nabase1.simplechatapp.util.FirebaseUtils;
import com.nabase1.simplechatapp.R;
import com.nabase1.simplechatapp.databinding.FragmentSearchBinding;
import com.nabase1.simplechatapp.viewModel.ViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements Dao {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSearchBinding mSearchBinding;
    private ContactsAdapter mMyAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewModel mViewModel;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);

        mViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ViewModel.class);
        mViewModel.init(this);

        initialize(mSearchBinding.recyclerView);

        return mSearchBinding.getRoot();
    }

    /* initialize recyclerView */
    private void initialize(RecyclerView recyclerView){

        FirebaseUtils.openFirebaseUtils("users", getActivity());
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if(layoutManager == null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }

        displayChats(recyclerView);

    }

    /* display chats on recyclerView */
    private void displayChats(RecyclerView recyclerView){
        mMyAdapter = (ContactsAdapter) recyclerView.getAdapter();
        if(mMyAdapter == null){
            mMyAdapter = new ContactsAdapter(mViewModel.getContactList().getValue());
            recyclerView.setAdapter(mMyAdapter);
        }
    }

    @Override
    public void loadGroupMessages() {

    }

    @Override
    public void loadContactList() {
            mViewModel.getContactList().observe(this, Users -> mMyAdapter.notifyDataSetChanged());
    }
}