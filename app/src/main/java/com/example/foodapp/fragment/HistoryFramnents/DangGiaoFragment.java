package com.example.foodapp.fragment.HistoryFramnents;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.adapter.HistoryAdapter;
import com.example.foodapp.modal.History;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class DangGiaoFragment extends Fragment {
    RecyclerView rcv_History;
    HistoryAdapter historyAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<History> list=new ArrayList<>();
    SharedPreferences sharedPreferences;

    int userIdd;
    TextView tv_Empty;


    public DangGiaoFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_giao, container, false);

        rcv_History=view.findViewById(R.id.rcv_history_dang_gh);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcv_History.setLayoutManager(linearLayoutManager);
        historyAdapter=new HistoryAdapter();

        tv_Empty=view.findViewById(R.id.tv_empty_dang_gh);
        if(list.isEmpty()){
            tv_Empty.setVisibility(View.VISIBLE);
        }

        rcv_History.setAdapter(historyAdapter);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference("list history");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History userId = snapshot.getValue(History.class);
                if (userId == null || list == null || historyAdapter == null) {
                    return;
                }
                if(userIdd==(userId.getUserId()) && userId.getStatus()==2){
                    list.add(0, userId);
                    tv_Empty.setVisibility(View.GONE);
                }else {
                    return;
                }
                historyAdapter.setData(list);
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
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences=getActivity().getSharedPreferences("info",getActivity().MODE_PRIVATE);
        userIdd=sharedPreferences.getInt("userId",0);
    }
}