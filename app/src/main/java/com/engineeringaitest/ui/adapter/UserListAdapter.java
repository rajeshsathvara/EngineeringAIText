package com.engineeringaitest.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.engineeringaitest.R;
import com.engineeringaitest.databinding.RowItemUserBinding;
import com.engineeringaitest.model.APIResponse;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ItemViewHolder> {

    private ArrayList<APIResponse.Data.Users> userlist;
    private Activity mActivity;
    private GridPostAdapter gridPostAdapter;

    public UserListAdapter(Activity activity, ArrayList<APIResponse.Data.Users> list) {
        this.mActivity = activity;
        this.userlist = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // databinding for use view directly
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

            gridPostAdapter = new GridPostAdapter(mActivity, items);
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
                        .load(users.getImage())
                        .into(binding.ivUserPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setPostDataAdapter(users.getItems(), binding.rvPost);
        }
    }
}
