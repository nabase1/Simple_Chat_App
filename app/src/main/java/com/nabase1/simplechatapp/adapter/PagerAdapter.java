package com.nabase1.simplechatapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nabase1.simplechatapp.fragments.ChatListFragment;
import com.nabase1.simplechatapp.fragments.GroupChatFragment;
import com.nabase1.simplechatapp.fragments.SearchFragment;

public class PagerAdapter extends FragmentStateAdapter {

    int numOfTabs;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, int numOfTabs) {
        super(fragmentActivity);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return  new ChatListFragment();
            case 1:
                return new GroupChatFragment();
            case 2:
                return new SearchFragment();

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
