package com.example.foodapp.fragment.HistoryFramnents;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.fragment.HistoryFragment;

public class HistoryViewPageAdapter extends FragmentStateAdapter {
    public HistoryViewPageAdapter(@NonNull HistoryFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DangXuLyFragment();
            case 1:
                return new ChoXacNhanFragment();
            case 2:
                return new DangGiaoFragment();
            case 3:
                return new DaGiaoFragment();
            case 4:
                return new DaHuyFragment();

            default:
                return new DangXuLyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
