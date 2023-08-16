package com.example.foodapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.R;
import com.example.foodapp.database.UserDatabase;
import com.example.foodapp.modal.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.text.Regex;

public class SingUpActivity extends AppCompatActivity {
    EditText edt_Email,edt_Mat_Khau;
    TextView tv_Dang_Ky;
    String email,matKhau;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        img_back=findViewById(R.id.img_back);
        edt_Email=findViewById(R.id.edt_email);
        edt_Mat_Khau=findViewById(R.id.edt_mat_khau);
        tv_Dang_Ky=findViewById(R.id.tv_dang_ky);
        tv_Dang_Ky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if(matKhau.length()==0||email.length()==0){
                    Toast.makeText(SingUpActivity.this,"Vui Lòng Nhập Đầy Đủ Thông Tin",Toast.LENGTH_LONG).show();
                    return;
                }
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(email);
                if(!matcher.matches()){
                    Toast.makeText(SingUpActivity.this,"Không đúng định dạng email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(matKhau.length() <6){
                    Toast.makeText(SingUpActivity.this, "Mật khẩu không được nhỏ hơn 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                List<User> list = UserDatabase.getInstance(getApplicationContext()).userDAO().getList();
                if(list.size()!=0){
                    for(User user :list) {
                        if(user.getEmail().equals(email)){
                            Toast.makeText(SingUpActivity.this,"Email đã tồn tại",Toast.LENGTH_LONG).show();
                            break;
                        }
                        else{
                            Toast.makeText(SingUpActivity.this,"Đăng Ký Tài Khoản Thành Công",Toast.LENGTH_LONG).show();
                            edt_Email.setText("");
                            edt_Mat_Khau.setText("");
                            User user1=new User(email,md5(matKhau));
                            Log.d("mahoa", "dang ky 1 "+md5(matKhau));
                            UserDatabase.getInstance(getApplicationContext()).userDAO().insert(user1);

                        }
                    }

                }else {
                    Toast.makeText(SingUpActivity.this,"Đăng Ký Tài Khoản Thành Công",Toast.LENGTH_LONG).show();
                    edt_Email.setText("");
                    edt_Mat_Khau.setText("");
                    User user1=new User(email,md5(matKhau));
                    Log.d("mahoa", "dang ky 2 "+md5(matKhau));
                    UserDatabase.getInstance(getApplicationContext()).userDAO().insert(user1);

                }

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private  void getData(){
        email=edt_Email.getText().toString().trim();
        matKhau=edt_Mat_Khau.getText().toString().trim();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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