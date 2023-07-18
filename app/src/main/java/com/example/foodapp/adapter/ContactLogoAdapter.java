package com.example.foodapp.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.modal.ContactLogo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactLogoAdapter extends RecyclerView.Adapter<ContactLogoAdapter.ContactLogoViewHolder> {
    List<ContactLogo> list;


    public void setData(List<ContactLogo> logoList) {
        this.list = logoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactLogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact, parent, false);
        return new ContactLogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactLogoViewHolder holder, int position) {
        ContactLogo contactLogo = list.get(position);
        if (contactLogo == null) {
            return;
        }
        holder.img_Logo.setImageResource(contactLogo.getImage());
        holder.tv_Logo_Name.setText(contactLogo.getLogoName());


        holder.img_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("phone_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String PHONE_ADMIN = dataSnapshot.getValue(String.class);
                        // Do something with the phone number

                        if(holder.tv_Logo_Name.getText().equals("Facebook")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Bạn có muốn chuyển tới facebook admin ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String facebookUrl = "https://www.facebook.com/hn.hai.9210/";
                                    try {
                                        int versionCode = v.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

                                        // nếu ứng dụng Facebook chính thức (nếu được cài đặt). Nếu ứng dụng Facebook chính thức không được cài đặt trên thiết bị của bạn, nó sẽ mở trang Facebook trong trình duyệt web.
                                        if (versionCode >= 3002850) {
                                            facebookUrl = "fb://facewebmodal/f?href=" + facebookUrl;
                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        // Facebook is not installed
                                    }
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                                    v.getContext().startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();


                        }
                        else if (holder.tv_Logo_Name.getText().equals("Call Phone")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("số điện thoại : "+PHONE_ADMIN );
                            builder.setTitle("Bạn có muốn gọi đến admin ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("gọi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri uri = Uri.parse("tel:" + PHONE_ADMIN);
                                    intent.setData(uri);
                                    v.getContext().startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Bạn có muốn chuyển tới zalo admin ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String zaloUrl = "https://zalo.me/" + PHONE_ADMIN+ "/";
                                    try {
                                        int versionCode = v.getContext().getPackageManager().getPackageInfo("com.zing.zalo", 0).versionCode;
                                        if (versionCode >= 470) {
                                            zaloUrl = "zalo://chat?zaloid=" + PHONE_ADMIN;
                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        // Zalo is not installed
                                    }
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(zaloUrl));
                                    v.getContext().startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors here
                    }
                });




            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ContactLogoViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_Logo;
        TextView tv_Logo_Name;

        public ContactLogoViewHolder(@NonNull View itemView) {

            super(itemView);
            tv_Logo_Name = itemView.findViewById(R.id.tv_logo_name);
            img_Logo = itemView.findViewById(R.id.img_logo);
        }
    }

}
