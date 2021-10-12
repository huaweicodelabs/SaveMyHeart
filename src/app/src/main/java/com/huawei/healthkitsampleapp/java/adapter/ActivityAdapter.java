/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.healthkitsampleapp.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.healthkitsampleapp.BR;
import com.huawei.healthkitsampleapp.R;
import com.huawei.healthkitsampleapp.java.model.ActivityDetails;
import com.huawei.healthkitsampleapp.java.ui.home.HomeViewModel;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.GenericViewHolder> {
    private final int layoutId;
    private List<ActivityDetails> breeds;
    private final HomeViewModel viewModel;
    private static final int ITEM_COUNT = 0;
    private Context context;

    public ActivityAdapter(@LayoutRes int layoutId, HomeViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return breeds == null ? ITEM_COUNT : breeds.size();
    }

    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        context = parent.getContext();
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setDogBreeds(List<ActivityDetails> breeds) {
        this.breeds = breeds;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(HomeViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();

            TextView activeText = (TextView) binding.getRoot().findViewById(R.id.active_button);

            if (breeds.get(position).isActive()) {
                activeText.setText(context.getResources().getString(R.string.active));
            } else {
                activeText.setText(context.getResources().getString(R.string.in_active));
            }
        }
    }
}
