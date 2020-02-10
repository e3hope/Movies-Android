package com.example.movies.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.movies.Util.ChangeRating;
import com.example.movies.Util.Data;
import com.example.movies.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();
    protected int pos;
    private ChangeRating changeRating;

    public void setChangeRating(ChangeRating changeRating) {
        this.changeRating = changeRating;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView Rimage;
        protected TextView Rtext;
        protected RatingBar RatingBar;
        public ViewHolder(@NonNull final View View) {
            super(View);
            Rimage = itemView.findViewById(R.id.Rimage);
            Rtext = itemView.findViewById(R.id.Rtext);
            RatingBar = itemView.findViewById(R.id.RatingBar);
            RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(android.widget.RatingBar ratingBar, float rating, boolean fromUser) {
                    pos = getAdapterPosition();
                    changeRating.changerating(pos,rating);

                }
            });
        }
        public void onBind(final Data data) {
            Rimage.setImageBitmap(data.getRdataimage());
            Rtext.setText(data.getRdatatext());
            RatingBar.setRating(data.getRdatarating());

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
    public void addItem(Data data) { listData.add(data); }// 외부에서 item을 추가시킬 함수입니다.
    public void removeItem(Data data){
        listData.clear();
    }

}


