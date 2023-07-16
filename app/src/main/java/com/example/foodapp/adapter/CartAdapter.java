package com.example.foodapp.adapter;

import android.text.NoCopySpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.modal.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Food>list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public IClick clickItem;
    // list nay dung de luu tru so luong cho moi item

    SparseArray<Integer> amountPro = new SparseArray<>();

    int amount;
    int gia;
    int originalPrice;
    public CartAdapter(IClick clickItem) {
        this.clickItem = clickItem;
    }

    public  interface IClick{
        void delete(Food food,int position);
    }
    public  void  setData(List<Food> getList){
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
        amount = food.getAmountBuy();
        int discount = food.getDiscount();
        if(discount==0){
            Picasso.get().load(food.getImage()).into(holder.img_Food);
            holder.tv_Amount.setText(food.getAmountBuy()+"");
            holder.tv_Price.setText(food.getPrice()*food.getAmountBuy()+"");
            holder.tv_Title.setText(food.getTitle());
        }else {
            Picasso.get().load(food.getImage()).into(holder.img_Food);
            holder.tv_Amount.setText(food.getAmountBuy()+"");
            holder.tv_Price.setText(food.totalMoney()+"");
            holder.tv_Title.setText(food.getTitle());
        }

        holder.tv_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.delete(food,holder.getAdapterPosition());

            }
        });
//        for (int i = 0; i < list.size(); i++) {
//            amountPro.put(i,0); // Khởi tạo tất cả số lượng ban đầu là 0
//        }
//
        holder.img_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                amountPro = new SparseArray<>();
                // lay vi tri
                int position = holder.getAdapterPosition();
                int currentAmount = amountPro.get(position,0);

                currentAmount++; //tăng số lượng
                amountPro.put(position,currentAmount);
                holder.tv_Amount.setText(String.valueOf(currentAmount));
                // giá ban đầu originalPrice
                 originalPrice = food.getPrice();
                // gia nhan so luong
                 gia = food.getPrice() * currentAmount;
                holder.tv_Price.setText("Giá:"+" "+gia);

                // cap nhat so luong len databaase
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference=firebaseDatabase.getReference("list cart");
                String itemId = String.valueOf(list.get(position).getIdDelete());
                reference.child(itemId).child("amountBuy").setValue(currentAmount);
               Toast.makeText(v.getContext(), "giá ban đầu là " + originalPrice, Toast.LENGTH_SHORT).show();
            }
        });

        holder.img_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                int currentAmount = amountPro.get(position,0);
            if (currentAmount > 1){
                currentAmount--;
                amountPro.put(position,currentAmount);
                holder.tv_Amount.setText(String.valueOf(currentAmount));
                // giá ban đầu originalPrice
                originalPrice = food.getPrice();
                // gia nhan so luong
                gia = food.getPrice() * currentAmount;
                holder.tv_Price.setText("Giá:"+" "+gia);

                // cap nhat so luong len databaase
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference=firebaseDatabase.getReference("list cart");
                String itemId = String.valueOf(list.get(position).getIdDelete());
                reference.child(itemId).child("amountBuy").setValue(currentAmount);
            }else {
                Toast.makeText(v.getContext(), "không thể thực hiện", Toast.LENGTH_SHORT).show();
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
        ImageView img_Food;
        ImageView img_Plus,img_Minus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Price=itemView.findViewById(R.id.tv_price_food);
            tv_Title=itemView.findViewById(R.id.tv_title_food);
            tv_Amount=itemView.findViewById(R.id.tv_amount);
            img_Food=itemView.findViewById(R.id.img_food_detail);
            tv_Xoa=itemView.findViewById(R.id.tv_xoa);
            img_Minus=itemView.findViewById(R.id.img_minus);
            img_Plus=itemView.findViewById(R.id.img_plus);
        }

    }

}
