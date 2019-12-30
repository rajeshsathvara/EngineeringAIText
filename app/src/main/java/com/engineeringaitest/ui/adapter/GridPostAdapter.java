package com.engineeringaitest.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.engineeringaitest.R;
import com.engineeringaitest.databinding.RowItemPostBinding;

import java.util.List;

public class GridPostAdapter extends RecyclerView.Adapter<GridPostAdapter.ItemViewHolder> {

    private Activity mContext;
    private List<String> imgList;

    GridPostAdapter(Activity mContext, List<String> imgList) {
        this.mContext = mContext;
        this.imgList = imgList;
    }

    @NonNull
    @Override
    public GridPostAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // databinding for use view directly
        RowItemPostBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.row_item_post, viewGroup, false);
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
        RowItemPostBinding binding;

        ItemViewHolder(@NonNull RowItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String imgUrl) {
            // set values to view
            try {
                Glide.with(mContext).load(imgUrl)
                        .into(binding.postImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
