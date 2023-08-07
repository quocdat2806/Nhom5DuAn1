package com.example.foodapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodapp.R;
import com.example.foodapp.fragment.HistoryFramnents.DangXuLyFragment;
import com.example.foodapp.fragment.HistoryFramnents.HistoryViewPageAdapter;
import com.example.foodapp.modal.Food;
import com.example.foodapp.modal.History;
import com.example.foodapp.modal.Notify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHoler> {
    List<History> list;
    SharedPreferences sharedPreferences;
    int userId;
    DatabaseReference reference_Notify;
    FirebaseDatabase firebaseDatabase_Notify;

    public void setData(List<History> historyList) {
        this.list = historyList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public HistoryViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history, parent, false);

        return new HistoryViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHoler holder, int position) {
        History history = list.get(position);

        if (history == null) {
            return;
        }

        if (history.getStatus() == 0 || history.getStatus() == 1) {
            holder.btn_chucnang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Bạn có muốn hủy đơn hay không?");

                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("status", 4);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("list history");
                            ref.child("" + history.getMaDonHang())
                                    .updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(v.getContext(), "Đã hủy đơn thành công ", Toast.LENGTH_SHORT).show();
                                            list.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(), "Đã hủy đơn thất bại ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });

        } else if (history.getStatus() == 2 || history.getStatus() == 3) {
            holder.btn_chucnang.setVisibility(View.GONE);

        } else if (history.getStatus() == 4) {
            holder.btn_chucnang.setText("Đặt lại");
            holder.btn_chucnang.setBackgroundResource(R.color.green);
            holder.btn_chucnang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Bạn có muốn đặt lại đơn hay không?");

                    long timestamp = System.currentTimeMillis();
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Calendar cal = Calendar.getInstance();
                            Date date = cal.getTime();
                            SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa");
                            String time;
                            time = ("" + sdf3.format(date));
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("status", 0);
                            hashMap.put("ngayDatHang", "" + time);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("list history");
                            ref.child("" + history.getMaDonHang())
                                    .updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(v.getContext(), "Đặt lại đơn thành công ", Toast.LENGTH_SHORT).show();
                                            list.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(v.getContext(), "Đặt lại đơn thất bại ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            sharedPreferences = v.getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
                            userId = sharedPreferences.getInt("userId", 0);


                            firebaseDatabase_Notify = FirebaseDatabase.getInstance();
                            reference_Notify = firebaseDatabase_Notify.getReference("list notify");

                            Notify notify = new Notify("" + timestamp, "Bạn vừa đặt lại đơn hàng" + " " + history.getThucDon(), time, userId);
                            reference_Notify.child("" + timestamp).setValue(notify);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }


        holder.tv_Ma_Don_Hang.setText(history.getMaDonHang());
        holder.tv_Ten.setText(history.getHoTen());
        holder.tv_Thanh_Toan.setText(history.getThanhToan());
        holder.tv_Tong_Tien.setText(history.getTongTien() + "");
        holder.tv_Ngay_Dat_Hang.setText(history.getNgayDatHang());
        holder.tv_Dia_Chi.setText(history.getDiaChiNhan());
        holder.tv_Sdt.setText(history.getSoDienThoai());
        holder.tv_Thuc_Don.setText(history.getThucDon());
        holder.tv_So_Luong.setText(history.getSoLuongMua() + "");
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class HistoryViewHoler extends RecyclerView.ViewHolder {
        TextView tv_Ma_Don_Hang, tv_Ten, tv_Sdt, tv_Dia_Chi, tv_Thuc_Don, tv_Ngay_Dat_Hang,
                tv_Tong_Tien, tv_Thanh_Toan, tv_So_Luong;
        AppCompatButton btn_chucnang;

        public HistoryViewHoler(@NonNull View itemView) {
            super(itemView);
            tv_Dia_Chi = itemView.findViewById(R.id.tv_dia_chi);
            tv_Sdt = itemView.findViewById(R.id.tv_sdt);
            tv_Thuc_Don = itemView.findViewById(R.id.tv_thuc_don);
            tv_Ngay_Dat_Hang = itemView.findViewById(R.id.tv_ngay_dat_hang);
            tv_Ma_Don_Hang = itemView.findViewById(R.id.tv_ma_don_hang);
            tv_Tong_Tien = itemView.findViewById(R.id.tv_tong_tien);
            tv_Thanh_Toan = itemView.findViewById(R.id.tv_thanh_toan);
            tv_Ten = itemView.findViewById(R.id.tv_ten);
            tv_So_Luong = itemView.findViewById(R.id.tv_so_luong);

            btn_chucnang = itemView.findViewById(R.id.btn_chucnang);

        }
    }

}
