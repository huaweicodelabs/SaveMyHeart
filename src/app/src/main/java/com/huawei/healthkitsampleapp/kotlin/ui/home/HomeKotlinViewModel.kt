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
package com.huawei.healthkitsampleapp.kotlin.ui.home

import android.view.View
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.healthkitsampleapp.R
import com.huawei.healthkitsampleapp.kotlin.adapter.ActivityAdapter
import com.huawei.healthkitsampleapp.kotlin.model.ActivityDetails
import com.huawei.healthkitsampleapp.kotlin.model.ActivityList

class HomeKotlinViewModel : ViewModel() {
    var dogBreeds: ActivityList? = null
    var adapter: ActivityAdapter? = null
    var selected: MutableLiveData<ActivityDetails?>? = null
    var images: ObservableArrayMap<String, String>? = null

    @JvmField
    var loading: ObservableInt? = null

    @JvmField
    var showEmpty: ObservableInt? = null
    fun init() {
        dogBreeds = ActivityList()
        selected = MutableLiveData()
        adapter = ActivityAdapter(R.layout.activity_details_kt_list, this)
        images = ObservableArrayMap()
        loading = ObservableInt(View.GONE)
        showEmpty = ObservableInt(View.GONE)
    }

    fun fetchList() {
        dogBreeds!!.fetchList()
    }

    val breeds: MutableLiveData<List<ActivityDetails?>?>?
        get() = dogBreeds?.breeds

    fun setDogBreedsInAdapter(breeds: List<ActivityDetails>?) {
        adapter!!.setDogBreeds(breeds)
        adapter!!.notifyDataSetChanged()
    }

    fun onItemClick(index: Int?) {
        val db = getDogBreedAt(index)
        selected!!.value = db
    }

    fun getDogBreedAt(index: Int?): ActivityDetails? {
        return if (dogBreeds?.breeds?.value != null && index != null && dogBreeds?.breeds?.value!!.size > index) {
            dogBreeds?.breeds?.value?.get(index)
        } else null
    }
}