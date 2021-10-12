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
import androidx.lifecycle.MutableLiveData
import com.huawei.healthkitsampleapp.R
import java.util.*

class ActivityList : BaseObservable() {
    var activityDetailsList: MutableList<ActivityDetails?> = ArrayList()
    var breeds = MutableLiveData<List<ActivityDetails?>?>()
    var activityDetails: ActivityDetails? = null
    fun fetchList() {
        val RUNNING = "Running"
        val DRIVING = "Driving"
        val WALKING = "Walking"
        val MAXHR = "Max HR : "
        activityDetails = ActivityDetails()
        activityDetails?.title = RUNNING
        activityDetails?.maxHeartRate = (MAXHR + (HR - AGE) * RUN_MAX_PERCENTAGE)
        activityDetails?.minHeartRate = (MAXHR + (HR - AGE) * RUN_MIN_PERCENTAGE)
        activityDetails?.image = R.drawable.running
        activityDetails!!.setmValue(ACTIVITY_MIN)
        activityDetails!!.setmName(ACTIVITY_RUN)
        activityDetailsList.add(activityDetails)
        activityDetails = ActivityDetails()
        activityDetails?.title = WALKING
        activityDetails?.maxHeartRate = (MAXHR + (HR - AGE) * WALK_MAX_PERCENTAGE)
        activityDetails?.minHeartRate = (MAXHR + (HR - AGE) * WALK_MIN_PERCENTAGE)
        activityDetails?.image = R.drawable.walking
        activityDetails!!.setmValue(ACTIVITY_MIN)
        activityDetails!!.setmName(ACTIVITY_WALK)
        activityDetailsList.add(activityDetails)
        activityDetails = ActivityDetails()
        activityDetails?.title = DRIVING
        activityDetails?.maxHeartRate = (MAXHR + (HR - AGE) * DRIVE_MAX_PERCENTAGE)
        activityDetails?.minHeartRate = (MAXHR + (HR - AGE) * DRIVE_MIN_PERCENTAGE)
        activityDetails?.image = R.drawable.driving
        activityDetails!!.setmValue(ACTIVITY_MIN)
        activityDetails!!.setmName(ACTIVITY_DRIVE)
        activityDetailsList.add(activityDetails)
        breeds.setValue(activityDetailsList)
    }

    companion object {
        private const val HR = 220
        private const val AGE = 27
        private const val RUN_MAX_PERCENTAGE = 0.7
        private const val RUN_MIN_PERCENTAGE = 0.6
        private const val WALK_MAX_PERCENTAGE = 0.6
        private const val WALK_MIN_PERCENTAGE = 0.5
        private const val DRIVE_MAX_PERCENTAGE = 0.8
        private const val DRIVE_MIN_PERCENTAGE = 0.7
        private const val ACTIVITY_MIN = 0
        private const val ACTIVITY_RUN = 108
        private const val ACTIVITY_WALK = 107
        private const val ACTIVITY_DRIVE = 100
    }
}