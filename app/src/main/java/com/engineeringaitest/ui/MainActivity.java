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
import com.engineeringaitest.ui.adapter.UserListAdapter;
import com.engineeringaitest.util.EndLessRecyclerViewScrollListener;
import com.engineeringaitest.util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private APIResponse apiResponse = null;
    private int offset = 0;

    private ActivityMainBinding binding = null;

    private ArrayList<APIResponse.Data.Users> usersArrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager = null;
    private EndLessRecyclerViewScrollListener rvScrollListener;
    private UserListAdapter userListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private void init() {
        callGetUserListAPI();
        swipeRefreshListener();
        setRecyclerViewScrollListener();
    }

    private void setRecyclerViewScrollListener() {
        rvScrollListener = new EndLessRecyclerViewScrollListener(getLayoutManger()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (offset == 0) {
                    removeLoadMore();
                } else {
                    if (Util.checkInternetConnection(getApplicationContext())) {
                        callGetUserListAPI();
                    } else {
                        Util.cancelProgress();
                        showToast(R.string.no_internet_connection);
                    }
                }
            }
        };
    }

    private void swipeRefreshListener() {
        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeLayout.setRefreshing(false);
                offset = 0;
                if (usersArrayList != null && usersArrayList.size() > 0) {
                    usersArrayList.clear();
                }
                callGetUserListAPI();
            }
        });
    }

    private void callGetUserListAPI() {
        if (offset > 1) {
            setFooterVisibility(View.VISIBLE);
        } else {
            Util.showProgress(this, false);
        }

        if (Util.checkInternetConnection(getApplicationContext())) {
            Call<APIResponse> apiCall = RestApiClient.getApiInterface().getUserImageList(offset, 10);
            apiCall.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    Util.cancelProgress();
                    apiResponse = response.body();
                    removeLoadMore();
                    setUserAdapter();
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Util.cancelProgress();
                    removeLoadMore();
                }
            });
        } else {
            Util.cancelProgress();
            showToast(R.string.no_internet_connection);
        }
    }

    private void showToast(int message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void setUserAdapter() {
        if (apiResponse != null && apiResponse.getData().getUsers() != null) {
            offset = offset + apiResponse.getData().getUsers().size();
            usersArrayList.addAll(apiResponse.getData().getUsers());
            if (userListAdapter == null) {
                //First time list is empty to set the adapter
                binding.rvUser.setLayoutManager(getLayoutManger());

                userListAdapter = new UserListAdapter(this, usersArrayList);
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
