package com.example.foodapp.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.adapter.CartAdapter;
import com.example.foodapp.modal.Food;
import com.example.foodapp.modal.History;
import com.example.foodapp.modal.Notify;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartFragment extends Fragment {

    CartAdapter cartAdapter;
    RecyclerView rcv_Cart;
    List<Food> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase_History;
    DatabaseReference reference_History;
    FirebaseDatabase firebaseDatabase_Notify;
    DatabaseReference reference_Notify;
    public static   TextView tv_Tong_Tien,tv_Dat_Hang,tv_Empty;
    int userId;
    SharedPreferences sharedPreferences;
    public static int total=0;
    private BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout layout_Bottom_Sheet;
    TextView tv_Ten, tv_So_Luong, tv_Tong_Tien_Tat_Ca;
    TextView tv_Dat_Hang_Sheet, tv_Huy_Bo_Sheet;
    EditText edt_Ten, edt_Sdt, edt_Dia_Chi;
    Spinner spinner_phtt;
    String phuongThuc, ten, sdt, diaChi;
    ImageView img_Back;
    Boolean isLogin = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);
        unitUi(view);
        checkData();
        sharedPreferences = getActivity().getSharedPreferences("info", getActivity().MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", 0);
        isLogin = sharedPreferences.getBoolean("login",false);
            if(isLogin){
                loadData();
            }
        tv_Dat_Hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    tv_Tong_Tien_Tat_Ca.setText(total + " " + "VND");
                    String title = "";
                    String quantity = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getTitle().length() > 25) {
                            title += list.get(i).getTitle() + "(" + list.get(i).getPrice() + ")" + "\n";
                            quantity += "- Số Lượng:" + " " + list.get(i).getAmountBuy() + "\n" + "\n";
                        } else {
                            title += list.get(i).getTitle() + "(" + list.get(i).getPrice() + ")" + "\n";
                            quantity += "- Số Lượng:" + " " + list.get(i).getAmountBuy() + "\n";
                        }
                    }
                    tv_Ten.setText(title);
                    tv_So_Luong.setText(quantity);
                    tv_Dat_Hang_Sheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getData();
                            if (sdt.length() == 0 || diaChi.length() == 0 || ten.isEmpty()) {
                                Toast.makeText(getActivity(), "Vui Lòng Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String phoneRegex = "^(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";
                            Pattern pattern = Pattern.compile(phoneRegex);
                            Matcher matcher = pattern.matcher(sdt);
                            if(!matcher.matches()){
                                Toast.makeText(getContext(), "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            firebaseDatabase_History = FirebaseDatabase.getInstance();
                            reference_History = firebaseDatabase_History.getReference("list history");getInformation();
                            Toast.makeText(getActivity(), "Đặt Hàng Thành Công Vui Lòng Kiểm Tra Trong Lịch Sử", Toast.LENGTH_LONG).show();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            //code pay here
                            for (int i = 0; i < list.size(); i++) {
                                reference.child(list.get(i).getIdDelete() + "").removeValue();
                            }
                            list.clear();
                            cartAdapter.notifyDataSetChanged();
                            total = 0 ;

                            tv_Tong_Tien.setText(total+"");
                            tv_Dat_Hang.setEnabled(false);
                            tv_Empty.setVisibility(View.VISIBLE);
                            tv_Dat_Hang.setBackground(getResources().getDrawable(R.drawable.cs_huy_bo));
                        }
                    });
                    tv_Huy_Bo_Sheet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


        return view;


    }

    private void unitUi(View view) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("list cart");
        cartAdapter = new CartAdapter(new CartAdapter.IClick() {
            @Override
            public void delete(Food food, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn Có Muốn Chắc Chắn Xóa Sản Phẩm " + " " + food.getTitle() + "Này Không").setTitle("Xóa")
                        .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //code delete solution
                                if (food.getDiscount() != 0) {
                                    total = total - food.totalMoneyDiscount();
                                } else {
                                    total = total - food.total();}
                                tv_Tong_Tien.setText("Tổng Tiền :" + " " + total + "VND");
                                if (list.isEmpty()) {
                                    tv_Empty.setVisibility(View.VISIBLE);
                                }
                                if (total == 0) {
                                    tv_Tong_Tien.setText("000000VND");
                                    tv_Dat_Hang.setEnabled(false);
                                    tv_Dat_Hang.setBackground(getResources().getDrawable(R.drawable.cs_huy_bo));
                                }
                                reference.child(food.getIdDelete() + "").removeValue();
                                list.remove(position);
                                cartAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Xóa Sản Phẩm Thành Công", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });
        rcv_Cart = view.findViewById(R.id.rcv_cart);
        tv_Dat_Hang = view.findViewById(R.id.tv_dat_hang);
        layout_Bottom_Sheet = view.findViewById(R.id.layout_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout_Bottom_Sheet);
        tv_Tong_Tien = view.findViewById(R.id.tv_tong_tien);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_Cart.setLayoutManager(linearLayoutManager);
        rcv_Cart.setAdapter(cartAdapter);
        tv_Ten = view.findViewById(R.id.tv_ten);
        tv_So_Luong = view.findViewById(R.id.tv_so_luong);
        tv_Tong_Tien_Tat_Ca = view.findViewById(R.id.tv_tong_tien_tat_ca);
        tv_Huy_Bo_Sheet = view.findViewById(R.id.tv_huy_bo_sheet);
        tv_Dat_Hang_Sheet = view.findViewById(R.id.tv_dat_hang_sheet);
        edt_Ten = view.findViewById(R.id.edt_ten);
        edt_Dia_Chi = view.findViewById(R.id.edt_dia_chi);
        edt_Sdt = view.findViewById(R.id.edt_sdt);
        spinner_phtt = view.findViewById(R.id.spinner_phuong_thuc);
        tv_Empty = view.findViewById(R.id.tv_empty);
        img_Back = view.findViewById(R.id.img_back);
        ArrayList<String> dataListspinner = new ArrayList<>();
        dataListspinner.add("Tiền Mặt");
        dataListspinner.add("Thẻ Tín Dụng");
        dataListspinner.add("Smart Banking");
        dataListspinner.add("VNPay");
        dataListspinner.add("ZaloPay");
        dataListspinner.add("MoMo");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dataListspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_phtt.setAdapter(adapter);
    }

    private void getData() {
        phuongThuc = (String) spinner_phtt.getSelectedItem();
        ten = edt_Ten.getText().toString().trim();
        sdt = edt_Sdt.getText().toString().trim();
        diaChi = edt_Dia_Chi.getText().toString().trim();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    private void getInformation() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa");
        String time;
        time = ("" + sdf3.format(date));
        for (int i = 0; i < list.size(); i++) {
            long timestamp = System.currentTimeMillis();
            firebaseDatabase_Notify = FirebaseDatabase.getInstance();
            reference_Notify = firebaseDatabase_Notify.getReference("list notify");
            History history = new History("" + timestamp, ten, sdt, diaChi, list.get(i).getAmountBuy(), list.get(i).getTitle(), time, list.get(i).getPrice() * list.get(i).getAmountBuy(), phuongThuc, userId, 0);
            reference_History.child("" + timestamp).setValue(history);
            Notify notify = new Notify("" + timestamp, "Bạn vừa đặt hàng" + " " + list.get(i).getTitle(), time, userId);
            reference_Notify.child("" + timestamp).setValue(notify);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void loadData() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Food food = dataSnapshot.getValue(Food.class);
                if (food == null || list == null || cartAdapter == null) {
                    return;
                }
                if (food.getUserId() == userId) {
                    list.add(0, food);
                    if (food.getDiscount() != 0) {
                        total += food.totalMoneyDiscount();
                    } else {
                        total += food.total();
                    }
                    tv_Tong_Tien.setText("Tổng Tiền" + " " + total);
                    tv_Dat_Hang.setEnabled(true);
                    tv_Dat_Hang.setClickable(true);
                    tv_Dat_Hang.setFocusable(true);
                    tv_Dat_Hang.setBackground(getResources().getDrawable(R.drawable.cs_them_gio_hang));
                    tv_Empty.setVisibility(View.GONE);
                    cartAdapter.setData(list);
                    Log.d("LIST",list.toString());
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void checkData() {
        if (list.size() == 0) {
            tv_Tong_Tien.setText("000000 VND");
            tv_Dat_Hang.setBackground(getResources().getDrawable(R.drawable.cs_huy_bo));
            tv_Dat_Hang.setClickable(false);
            tv_Dat_Hang.setFocusable(false);
            tv_Dat_Hang.setEnabled(false);
            tv_Empty.setVisibility(View.VISIBLE);
        }

    }
}
