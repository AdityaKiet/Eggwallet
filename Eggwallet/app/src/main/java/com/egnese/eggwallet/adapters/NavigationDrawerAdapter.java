package com.egnese.eggwallet.adapters;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.dto.NavDrawerItem;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        switch(position){
            case 0:
                holder.imageView.setImageResource(R.mipmap.abc_ic_drawer_dashboard);
                break;
            case 1:
                holder.imageView.setImageResource(R.mipmap.abc_ic_drawer_profile);
                break;
            case 2:
                holder.imageView.setImageResource(R.mipmap.abc_ic_drawer_settings);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        com.neopixl.pixlui.components.textview.TextView title;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (com.neopixl.pixlui.components.textview.TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.imgDrawerRow);
        }
    }
}
