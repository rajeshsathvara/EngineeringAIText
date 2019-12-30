package com.engineeringaitest.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.engineeringaitest.R;
import com.engineeringaitest.databinding.RowItemImageBinding;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ItemViewHolder> {

    private Activity mContext;
    private List<String> imgList;

    GridAdapter(Activity mContext, List<String> imgList) {
        this.mContext = mContext;
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public GridAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // data-binding for use view directly
        RowItemImageBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.row_item_image, viewGroup, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final String imageUrl = imgList.get(position);
        holder.bind(imageUrl);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RowItemImageBinding binding;

        ItemViewHolder(@NonNull RowItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String imgUrl) {
            // set values to view
            try {
                Glide.with(mContext).load(imgUrl).placeholder(R.drawable.ic_loader).into(binding.postImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
