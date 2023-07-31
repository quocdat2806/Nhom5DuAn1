package com.example.foodapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodapp.R;
import com.example.foodapp.fragment.CartFragment;
import com.example.foodapp.modal.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Food>list;

    public IClick clickItem;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    int amount;
    public CartAdapter(IClick clickItem) {
        this.clickItem = clickItem;
    }

    public  interface IClick{
        void delete(Food food,int position);
    }
    public  void  setData(List<Food>getList){
        this.list=getList;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Food food=list.get(position);
        if(food==null){
            return;
        }
        int discount=food.getDiscount();

        if(discount==0){
            Picasso.get().load(food.getImage()).into(holder.img_Food);
            holder.tv_Amount.setText(+food.getAmountBuy()+"");
            holder.tv_Price.setText("Giá : "+food.getPrice()+"");
            holder.tv_Title.setText(food.getTitle());
        }else {
            Picasso.get().load(food.getImage()).into(holder.img_Food);
            holder.tv_Amount.setText(+food.getAmountBuy()+"");
            holder.tv_Price.setText("Giá : "+food.totalMoney()+"");
            holder.tv_Title.setText(food.getTitle());
        }
        amount=food.getAmountBuy();
        holder.tv_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.delete(food,holder.getAdapterPosition());

            }
        });
        ///ho trinh
        holder.img_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = list.get(holder.getAdapterPosition()).getAmountBuy();
                if (amount>1){
                    --amount;
                    list.get(holder.getAdapterPosition()).setAmountBuy(amount);
                    holder.tv_Amount.setText(list.get(holder.getAdapterPosition()).getAmountBuy()+"");
                    if(list.get(holder.getAdapterPosition()).getDiscount()!=0){
                        if(discount==0){
                            holder.tv_Price.setText("Giá : "+food.getPrice()*amount);
                        }else {
                            holder.tv_Price.setText("Giá : "+food.totalMoney()*amount);
                        }
                        CartFragment.total = CartFragment.total - list.get(holder.getAdapterPosition()).totalMoney();


                    }   else {
                        CartFragment.total = CartFragment.total - list.get(holder.getAdapterPosition()).getPrice();

                    }
                    CartFragment.tv_Tong_Tien.setText("Tổng Tiền : "+CartFragment.total+"VND");
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference=firebaseDatabase.getReference("list cart");
                    String itemId = String.valueOf(list.get(position).getIdDelete());
                    reference.child(itemId).child("amountBuy").setValue(amount);
                    if(discount==0){
                        reference.child(itemId).child("price").setValue(list.get(position).getPrice()*amount);
                    }else {
                        reference.child(itemId).child("price").setValue(list.get(position).totalMoney()*amount);
                    }
                }else {
                    Toast.makeText(v.getContext(), "không thể thực hiện", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.img_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = list.get(holder.getAdapterPosition()).getAmountBuy();
                ++amount;
                list.get(holder.getAdapterPosition()).setAmountBuy(amount);
                holder.tv_Amount.setText(list.get(holder.getAdapterPosition()).getAmountBuy()+"");
                if(list.get(holder.getAdapterPosition()).getDiscount()!=0){
                    if(discount==0){
                        holder.tv_Price.setText("Giá : "+food.getPrice()*amount);
                    }else {
                        holder.tv_Price.setText("Giá : "+food.totalMoney()*amount);
                    }
                    CartFragment.total = CartFragment.total + list.get(holder.getAdapterPosition()).totalMoney();
                }   else {
                    CartFragment.total = CartFragment.total + list.get(holder.getAdapterPosition()).getPrice();
                }
                CartFragment.tv_Tong_Tien.setText("Tổng Tiền : "+CartFragment.total+"VND");
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference=firebaseDatabase.getReference("list cart");
                String itemId = String.valueOf(list.get(position).getIdDelete());
                reference.child(itemId).child("amountBuy").setValue(amount);
                if(discount==0){
                    reference.child(itemId).child("price").setValue(list.get(position).getPrice()*amount);
                }else {
                    reference.child(itemId).child("price").setValue(list.get(position).totalMoney()*amount);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return  list.size();
        }
        return 0;
    }

    public  class  CartViewHolder extends RecyclerView.ViewHolder{
        TextView tv_Title,tv_Price,tv_Amount,tv_Xoa;
        ImageView img_Food,img_Plus,img_Minus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Price=itemView.findViewById(R.id.tv_price_food);
            tv_Title=itemView.findViewById(R.id.tv_title_food);
            tv_Amount=itemView.findViewById(R.id.tv_amount);
            img_Food=itemView.findViewById(R.id.img_food_detail);
            tv_Xoa=itemView.findViewById(R.id.tv_xoa);
            img_Minus = itemView.findViewById(R.id.img_minus);
            img_Plus = itemView.findViewById(R.id.img_plus);
        }
    }
}
