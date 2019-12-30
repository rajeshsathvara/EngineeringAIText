package com.engineeringaitest.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.engineeringaitest.R;
import com.engineeringaitest.databinding.RowItemUserBinding;
import com.engineeringaitest.model.APIResponse;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ItemViewHolder> {

    private ArrayList<APIResponse.Data.Users> userlist;
    private Activity mActivity;
    private GridAdapter gridPostAdapter;

    public ImageListAdapter(Activity activity, ArrayList<APIResponse.Data.Users> list) {
        this.mActivity = activity;
        this.userlist = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // data-binding for use view directly
        RowItemUserBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.row_item_user, viewGroup, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final APIResponse.Data.Users usersModel = userlist.get(position);
        holder.bind(usersModel);
    }

    private void setPostDataAdapter(final List<String> items, RecyclerView rvPost) {
        if (items != null && items.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (items.size() % 2 == 0)
                        return 1;
                    else if (position == 0)
                        return 2;
                    else
                        return 1;
                }
            });

            gridPostAdapter = new GridAdapter(mActivity, items);
            rvPost.setLayoutManager(gridLayoutManager);
            rvPost.setAdapter(gridPostAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public void setData(ArrayList<APIResponse.Data.Users> newList) {
        this.userlist = newList;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        RowItemUserBinding binding;

        ItemViewHolder(@NonNull RowItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(APIResponse.Data.Users users) {
            // set views
            binding.tvUserName.setText(users.getName());
            try {
                Glide.with(mActivity)
                        .load(users.getImage()).placeholder(R.drawable.ic_account)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.pbLoader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                binding.pbLoader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(binding.ivUserPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setPostDataAdapter(users.getItems(), binding.rvPost);
        }
    }
}
