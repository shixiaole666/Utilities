package com.android.turntable.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.turntable.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class WallPaperAdapter extends RecyclerView.Adapter<WallPaperAdapter.ViewHolder> {
    public static final String TAG = "WallPaperAdapter";
    private Context mContext;
    private List<Wallpaper> mWallPaperList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView wallpaperImage;
        TextView wallpaperName;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            wallpaperImage = (ImageView) view.findViewById(R.id.wallpaper_image);
            wallpaperName = (TextView) view.findViewById(R.id.wallpaper_name);
        }
    }

    public WallPaperAdapter(List<Wallpaper> wallpaperList) {
        mWallPaperList = wallpaperList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallpaper_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");

            }
        });
        holder.wallpaperImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wallpaper wallpaper = mWallPaperList.get(position);
        holder.wallpaperName.setText(wallpaper.getName());
        Glide.with(mContext).load(wallpaper.getImageId()).into(holder.wallpaperImage);
    }

    @Override
    public int getItemCount() {
        return mWallPaperList.size();
    }
}
