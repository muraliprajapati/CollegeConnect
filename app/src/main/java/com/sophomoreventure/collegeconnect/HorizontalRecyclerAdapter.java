package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Murali on 10/01/2016.
 */
public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.ViewHolder> {

    Context context;

    public HorizontalRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HorizontalRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            int layoutId = R.layout.single_column;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(HorizontalRecyclerAdapter.ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.drawable.gradient_blue);
        } else {
            holder.layout.setBackgroundResource(R.drawable.gradient_orange);
        }

        holder.clubNameTextView.setText("IEEE");
    }


    @Override
    public int getItemCount() {
        return 6;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        TextView clubNameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            clubNameTextView = (TextView) itemView.findViewById(R.id.club_name_text_view);
            layout = (LinearLayout) itemView.findViewById(R.id.single_column_linear_layout);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), EventView.class);
            i.putExtra("clubName", "IEEE");
            context.startActivity(i);

        }
    }
}
