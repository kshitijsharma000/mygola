package com.myday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kshitij on 11/7/2015.
 */
public class Adapter_ActivityView extends RecyclerView.Adapter<Adapter_ActivityView.ViewHolder> {

    private ArrayList<ActivityData> activityDatas;
    private Context context;
    Integer[] images = {R.drawable.m_black,
            R.drawable.m_blue,
            R.drawable.m_brown,
            R.drawable.m_green,
    };

    public Adapter_ActivityView(ArrayList<ActivityData> activityDatas, Context context) {
        this.activityDatas = activityDatas;
        this.context = context;
    }

    @Override
    public Adapter_ActivityView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_activity_name.setText(activityDatas.get(position).getName());
        holder.text_activity_price.setText(String.valueOf(activityDatas.get(position).getActual_price()));
        holder.imageView.setImageResource(images[position%4]);
    }

    @Override
    public int getItemCount() {
        return activityDatas.size();
    }

    public void setItemList(ArrayList<ActivityData> itemList) {
        this.activityDatas = itemList;
    }

    public ActivityData getItem(int position){
        return activityDatas.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_activity_name;
        TextView text_activity_price;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            text_activity_name = (TextView) itemView.findViewById(R.id.activity_name);
            text_activity_price = (TextView) itemView.findViewById(R.id.activity_price);
            imageView = (ImageView) itemView.findViewById(R.id.activity_card_image1);
        }
    }
}
