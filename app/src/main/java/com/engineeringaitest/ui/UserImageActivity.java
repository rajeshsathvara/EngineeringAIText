package com.engineeringaitest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.engineeringaitest.R;
import com.engineeringaitest.api.RestApiClient;
import com.engineeringaitest.databinding.ActivityMainBinding;
import com.engineeringaitest.model.APIResponse;
import com.engineeringaitest.ui.adapter.ImageListAdapter;
import com.engineeringaitest.util.EndLessRecyclerViewScrollListener;
import com.engineeringaitest.util.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserImageActivity extends AppCompatActivity {

    private int offset = 0;
    private APIResponse apiResponse = null;

    private ActivityMainBinding binding = null;

    private ArrayList<APIResponse.Data.Users> usersArrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = null;
    private EndLessRecyclerViewScrollListener rvScrollListener;
    private ImageListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeLayout.setRefreshing(false);
                offset = 0;
                if (usersArrayList != null && usersArrayList.size() > 0) {
                    usersArrayList.clear();
                }
                callApiForGetUserImageList();
            }
        });

        callApiForGetUserImageList();
        setScrollListener();
    }

    private void setScrollListener() {
        rvScrollListener = new EndLessRecyclerViewScrollListener(getLayoutManger()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (offset == 0) {
                    removeLoadMore();
                } else {
                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        callApiForGetUserImageList();
                    } else {
                        Utils.dismissProgress();
                        showToast(R.string.no_internet_connection);
                    }
                }
            }
        };
    }

    private void callApiForGetUserImageList() {
        if (offset > 1) {
            setFooterVisibility(View.VISIBLE);
        } else {
            Utils.startProgress(UserImageActivity.this, false);
        }

        if (Utils.isNetworkAvailable(UserImageActivity.this)) {
            Call<APIResponse> apiCall = RestApiClient.getApiInterface().getUserImageList(offset, 10);
            apiCall.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    Utils.dismissProgress();
                    apiResponse = response.body();
                    removeLoadMore();
                    setUserAdapter();
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Utils.dismissProgress();
                    removeLoadMore();
                }
            });
        } else {
            Utils.dismissProgress();
            showToast(R.string.no_internet_connection);
        }
    }

    private void showToast(int message) {
        Toast.makeText(UserImageActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setUserAdapter() {
        if (apiResponse != null && apiResponse.getData().getUsers() != null) {
            // store offset for next api call
            offset = offset + apiResponse.getData().getUsers().size();
            usersArrayList.addAll(apiResponse.getData().getUsers());
            if (userListAdapter == null) {
                //First time list is empty to set the adapter
                binding.rvUser.setLayoutManager(getLayoutManger());

                userListAdapter = new ImageListAdapter(this, usersArrayList);
                binding.rvUser.setAdapter(userListAdapter);
                binding.rvUser.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                binding.rvUser.addOnScrollListener(rvScrollListener);

            } else {
                //add the list to adapter
                userListAdapter.setData(usersArrayList);
                userListAdapter.notifyDataSetChanged();
            }
        } else {
            offset = 0;
        }
    }

    private void setFooterVisibility(int visibility) {
        if (binding.relLoadingMore != null) {
            binding.relLoadingMore.setVisibility(visibility);
        }
    }

    private void removeLoadMore() {
        if (binding.relLoadingMore != null)
            setFooterVisibility(View.GONE);
    }

    private LinearLayoutManager getLayoutManger() {
        if (linearLayoutManager == null) {
            linearLayoutManager = new LinearLayoutManager(this);
        }
        return linearLayoutManager;
    }
}
