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
package com.huawei.healthkitsampleapp.kotlin.model

import androidx.databinding.BaseObservable

class ActivityDetails : BaseObservable() {
    var title: String? = null
    var minHeartRate: String? = null
    var maxHeartRate: String? = null
    var image = 0
    var heartRate = 0
    private var mName = 0
    private var mValue = 0
    var isActive = false
    fun getmName(): Int {
        return mName
    }

    fun setmName(name: Int) {
        mName = name
    }

    fun setmValue(value: Int) {
        mValue = value
    }

    fun getmValue(): Int {
        return mValue
    }
}