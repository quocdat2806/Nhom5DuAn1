package com.example.foodapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.activity.LoginActivity;
import com.example.foodapp.adapter.HistoryAdapter;
import com.example.foodapp.fragment.HistoryFramnents.HistoryViewPageAdapter;
import com.example.foodapp.modal.History;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private HistoryViewPageAdapter historyViewPageAdapter;

    SharedPreferences sharedPreferences2;
    boolean isLogin;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences2= getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        isLogin=sharedPreferences2.getBoolean("login",false);
        View view = isLogin ? inflater.inflate(R.layout.history_fragment,container,false) : inflater.inflate(R.layout.not_login_in_history_fragment,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isLogin) {
            AppCompatButton loginButton = view.findViewById(R.id.btn_dangnhap_in_htr);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            tabLayout = view.findViewById(R.id.tabthu_layout_new);
            viewPager = view.findViewById(R.id.view_pager2_new);


            historyViewPageAdapter = new HistoryViewPageAdapter(HistoryFragment.this);
            viewPager.setAdapter(historyViewPageAdapter);


            new TabLayoutMediator(tabLayout,viewPager,(tab, position) ->{
                switch (position){
                    case 0:
                        tab.setText("Đang xử lý");
                        break;
                    case 1:
                        tab.setText("Chờ Xác nhận");
                        break;
                    case 2:
                        tab.setText("Đang giao hàng");
                        break;
                    case 3:
                        tab.setText("Đã giao");
                        break;
                    case 4:
                        tab.setText("Đã hủy");
                        break;
                }
            }).attach();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
