package com.example.foodapp.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class FeedBackFragment extends Fragment {
    EditText edt_Ten, edt_Sdt, edt_Email, edt_Binh_Luan;
    TextView tv_Gui_Phan_Hoi;
    String ten, sdt, email, binhLuan;


    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_fragment, container, false);

        edt_Ten = view.findViewById(R.id.edt_ten);
        edt_Sdt = view.findViewById(R.id.edt_sdt);
        edt_Email = view.findViewById(R.id.edt_email);
        edt_Binh_Luan = view.findViewById(R.id.edt_binh_luan);
        tv_Gui_Phan_Hoi = view.findViewById(R.id.tv_gui_phan_hoi);


        tv_Gui_Phan_Hoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_Ten.length() == 0) {
                    edt_Ten.requestFocus();
                    edt_Ten.setError("vui lòng nhập họ và tên !");
                } else if (edt_Sdt.length() == 0) {
                    edt_Sdt.requestFocus();
                    edt_Sdt.setError("vui lòng nhập số điện thoại !");
                } else if (!isValidPhoneNumber(edt_Sdt.getText().toString())) {
                    edt_Sdt.requestFocus();
                    edt_Sdt.setError("không đúng định dạng số điện thoại !");
                } else if (edt_Email.length() == 0) {
                    edt_Email.requestFocus();
                    edt_Email.setError("vui lòng nhập email !");
                } else if (!isValidEmail(edt_Email.getText().toString())) {
                    edt_Email.requestFocus();
                    edt_Email.setError("không đúng định dạng email !");
                } else if (edt_Binh_Luan.length() == 0) {
                    edt_Binh_Luan.requestFocus();
                    edt_Binh_Luan.setError("vui lòng nhập bình luận !");
                } else {
                    getData();

                    edt_Ten.setText("");
                    edt_Sdt.setText("");
                    edt_Email.setText("");
                    edt_Binh_Luan.setText("");
                }

            }
        });

        return view;
    }

    private void getData() {
        ten = edt_Ten.getText().toString().trim();
        sdt = edt_Sdt.getText().toString().trim();
        email = edt_Email.getText().toString().trim();
        binhLuan = edt_Binh_Luan.getText().toString().trim();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("feedback");

        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();

//        hashMap.put("id_ph", "" +timestamp);
        hashMap.put("1_ten_ph", "" + ten);
        hashMap.put("2_sdt_ph", "" + sdt);
        hashMap.put("3_email_ph", "" + email);
        hashMap.put("4_binhluan", "" + binhLuan);

        reference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Cảm Ơn Những Đóng Góp Của Bạn", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Đóng góp thất bại vui lòng thử lại sau !", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //check định dạng email
    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //check định dạng sdt
    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

}
