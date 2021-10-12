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

package com.huawei.healthkitsampleapp.java.model;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

import com.huawei.healthkitsampleapp.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityList extends BaseObservable {
    public List<ActivityDetails> activityDetailsList = new ArrayList<>();
    public MutableLiveData<List<ActivityDetails>> activityDetailsMutableList = new MutableLiveData<>();
    ActivityDetails activityDetails;
    private static final int HR = 220;
    private static final int AGE = 27;
    private static final double RUN_MAX_PERCENTAGE = 0.7;
    private static final double RUN_MIN_PERCENTAGE = 0.6;

    private static final double WALK_MAX_PERCENTAGE = 0.6;
    private static final double WALK_MIN_PERCENTAGE = 0.5;

    private static final double DRIVE_MAX_PERCENTAGE = 0.8;
    private static final double DRIVE_MIN_PERCENTAGE = 0.7;

    private static final int ACTIVITY_MIN = 0;
    private static final int ACTIVITY_RUN = 108;
    private static final int ACTIVITY_WALK = 107;
    private static final int ACTIVITY_DRIVE = 100;

    public MutableLiveData<List<ActivityDetails>> getBreeds() {
        return activityDetailsMutableList;
    }

    public void fetchList() {
        final String RUNNING = "Running";
        final String DRIVING = "Driving";
        final String WALKING = "Walking";
        final String MAXHR = "Max HR : ";
        activityDetails = new ActivityDetails();
        activityDetails.setTitle(RUNNING);
        activityDetails.setMaxHeartRate(MAXHR + (HR - AGE) * RUN_MAX_PERCENTAGE);
        activityDetails.setMinHeartRate(MAXHR + (HR - AGE) * RUN_MIN_PERCENTAGE);
        activityDetails.setImage(R.drawable.running);
        activityDetails.setmValue(ACTIVITY_MIN);
        activityDetails.setmName(ACTIVITY_RUN);
        activityDetailsList.add(activityDetails);

        activityDetails = new ActivityDetails();
        activityDetails.setTitle(WALKING);
        activityDetails.setMaxHeartRate(MAXHR + (HR - AGE) * WALK_MAX_PERCENTAGE);
        activityDetails.setMinHeartRate(MAXHR + (HR - AGE) * WALK_MIN_PERCENTAGE);
        activityDetails.setImage(R.drawable.walking);
        activityDetails.setmValue(ACTIVITY_MIN);
        activityDetails.setmName(ACTIVITY_WALK);
        activityDetailsList.add(activityDetails);

        activityDetails = new ActivityDetails();
        activityDetails.setTitle(DRIVING);
        activityDetails.setMaxHeartRate(MAXHR + (HR - AGE) * DRIVE_MAX_PERCENTAGE);
        activityDetails.setMinHeartRate(MAXHR + (HR - AGE) * DRIVE_MIN_PERCENTAGE);
        activityDetails.setImage(R.drawable.driving);
        activityDetails.setmValue(ACTIVITY_MIN);
        activityDetails.setmName(ACTIVITY_DRIVE);
        activityDetailsList.add(activityDetails);

        activityDetailsMutableList.setValue(activityDetailsList);
    }
}
