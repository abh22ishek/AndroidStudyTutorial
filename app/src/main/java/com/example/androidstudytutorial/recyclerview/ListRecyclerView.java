package com.example.androidstudytutorial.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidstudytutorial.listners.CallFragment;

import com.example.androidstudytutorial.model.Descx;
import com.example.androidstudytutorial.model.Images;
import com.mine.mywallpaper.R;

import java.util.List;

public class ListRecyclerView extends RecyclerView.Adapter<ListRecyclerView.ListHolder> {

    private Context mContext;
    private List<Images> mDescList;
    CallFragment callFragment;
    public ListRecyclerView(Context mContext, List<Images> mDescList , CallFragment callFragment) {
        this.mContext = mContext;
        this.mDescList = mDescList;
        this.callFragment = callFragment;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_bg, parent, false);
        ListHolder listHolder = new ListHolder(view);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
      if(mDescList !=null){
          final  Images descx = mDescList.get(position);
        //  holder.imageView.setImageDrawable(mContext.getDrawable(descx.getImage()));
       //  holder.imageView.setImageResource(descx.getImage());
        Glide.with(mContext)
                .load(mContext.getDrawable(descx.getImage()))
                 .fitCenter()
                .placeholder(R.color.gray)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)


            .into(holder.imageView);


      }

      holder.imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             final int pos = holder.getAdapterPosition();
             if(mDescList!=null && mDescList.get(pos)!=null){
                 callFragment.showFragment(mDescList.get(pos).getImage(),mDescList.get(pos).getText(),"");

             }
          }
      });


    }

    @Override
    public int getItemCount() {
        return mDescList.size();
    }


    public static class ListHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}
