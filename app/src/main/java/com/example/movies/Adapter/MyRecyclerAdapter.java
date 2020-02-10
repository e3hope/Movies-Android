package com.example.movies.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.Util.ChangeRating;
import com.example.movies.Util.Data;
import com.example.movies.Util.MyData;
import com.example.movies.R;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<MyData> listData = new ArrayList<MyData>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView Rimage;
        protected TextView Rtext;
        protected RatingBar RatingBar;

        public ViewHolder(@NonNull final View View) {
            super(View);
            Rimage = itemView.findViewById(R.id.Rimage);
            Rtext = itemView.findViewById(R.id.Rtext);
            RatingBar = itemView.findViewById(R.id.RatingBar);
        }
        public void onBind(final MyData data) {
            Rimage.setImageBitmap(data.getRdataimage());
            Rtext.setText(data.getRdatatext());
            RatingBar.setRating(data.getRdatarating());
            RatingBar.setEnabled(false);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public void addItem(MyData mydata) { listData.add(mydata); }

}


