package com.example.foodapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodapp.R;


public class DatHangThanhCongFragment extends Fragment {
    AppCompatButton quaylaitrangchu, xemdonhang;


    public DatHangThanhCongFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dat_hang_thanh_cong, container, false);
        quaylaitrangchu = view.findViewById(R.id.quaylaitrangchu);
        xemdonhang = view.findViewById(R.id.xemdonmua);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quaylaitrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewpager_2, homeFragment).addToBackStack(null).commit();
            }
        });
        xemdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryFragment historyFragment = new HistoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewpager_2, historyFragment).addToBackStack(null).commit();
            }
        });
    }

}