package com.example.foodapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.MainActivity;
import com.example.foodapp.R;
import com.example.foodapp.database.UserDatabase;
import com.example.foodapp.modal.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_Email, edt_PassWord;
    ImageView img_Show_Hide;
    String email, passWord;
    TextView tv_Dang_Nhap;
    ImageView img_Back;
    List<User> list;
    int count = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        list = UserDatabase.getInstance(this).userDAO().getList();
        unitUi();
        img_Show_Hide.setOnClickListener(this);
        tv_Dang_Nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (list.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Đăng Nhập Thất Bại", Toast.LENGTH_LONG).show();
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getEmail().equals(email) && list.get(i).getPassWord().equals(md5(passWord))) {
                        Toast.makeText(LoginActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                        editor.putString("email", email);
                        editor.putString("password", md5(passWord));
                        Log.d("mahoa", "dang nhap 2 "+md5(passWord));
                        editor.putBoolean("login", true);
                        editor.putInt("userId", list.get(i).getId());
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        return;
                    } else {
                        count++;
                    }
                }
                if (count != 0) {
                    Toast.makeText(LoginActivity.this, "Tài Khoản Hoặc Mật Khẩu Không Đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void unitUi() {
        edt_Email = findViewById(R.id.edt_email);
        edt_PassWord = findViewById(R.id.edt_mat_khau);
        img_Show_Hide = findViewById(R.id.img_show_hide);
        tv_Dang_Nhap = findViewById(R.id.tv_dang_nhap);
        img_Back = findViewById(R.id.img_back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    private void getData() {
        email = edt_Email.getText().toString().trim();
        passWord = edt_PassWord.getText().toString().trim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}