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

package com.huawei.healthkitsampleapp.java.ui.home;

import android.view.View;

import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huawei.healthkitsampleapp.R;
import com.huawei.healthkitsampleapp.java.adapter.ActivityAdapter;
import com.huawei.healthkitsampleapp.java.model.ActivityDetails;
import com.huawei.healthkitsampleapp.java.model.ActivityList;

import java.util.List;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {
    }

    public ActivityList dogBreeds;
    public ActivityAdapter adapter;
    public MutableLiveData<ActivityDetails> selected;
    public ObservableArrayMap<String, String> images;
    public ObservableInt loading;
    public ObservableInt showEmpty;

    public void init() {
        dogBreeds = new ActivityList();
        selected = new MutableLiveData<>();
        adapter = new ActivityAdapter(R.layout.activity_details_list, this);
        images = new ObservableArrayMap<>();
        loading = new ObservableInt(View.GONE);
        showEmpty = new ObservableInt(View.GONE);
    }

    public void fetchList() {
        dogBreeds.fetchList();
    }

    public MutableLiveData<List<ActivityDetails>> getBreeds() {
        return dogBreeds.getBreeds();
    }

    public ActivityAdapter getAdapter() {
        return adapter;
    }

    public void setDogBreedsInAdapter(List<ActivityDetails> breeds) {
        this.adapter.setDogBreeds(breeds);
        this.adapter.notifyDataSetChanged();
    }

    public MutableLiveData<ActivityDetails> getSelected() {
        return selected;
    }

    public void onItemClick(Integer index) {
        ActivityDetails db = getDogBreedAt(index);
        selected.setValue(db);
    }

    public ActivityDetails getDogBreedAt(Integer index) {
        if (dogBreeds.getBreeds().getValue() != null
                && index != null
                && dogBreeds.getBreeds().getValue().size() > index) {
            return dogBreeds.getBreeds().getValue().get(index);
        }
        return null;
    }
}
