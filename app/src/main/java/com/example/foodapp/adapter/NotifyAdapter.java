package com.example.foodapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.activity.NotifyActivity;
import com.example.foodapp.modal.Notify;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    List<Notify> list;
    NotifyActivity notifyActivity;

    public NotifyAdapter(NotifyActivity notifyActivity) {
        this.notifyActivity = notifyActivity;
    }

    public void setData(List<Notify> notifyList) {
        this.list = notifyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notify, parent, false);

        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        Notify notify = list.get(position);
        if (notify == null) {
            return;
        }
        holder.tv_notify.setText(notify.getTitle());
        notifyActivity.handlerTimes(notify.getTimes());
        if (notifyActivity.diffHours >= 24) {
            int day = notifyActivity.setday((int) notifyActivity.diffHours);
            Log.d("quanquan", "day adapter " + day);
            holder.tv_times.setText(day + "" + " Ngày trước");

        } else if (notifyActivity.diffHours < 1) {
            holder.tv_times.setText(notifyActivity.diffMinutes + " " + " Phút Trước");


        } else {
            holder.tv_times.setText(notifyActivity.diffHours + "" + " Giờ " + notifyActivity.diffMinutes + " " + " Phút Trước");
        }


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notify, tv_times;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_notify = itemView.findViewById(R.id.tv_notify);
            tv_times = itemView.findViewById(R.id.tv_times);

        }
    }
}
