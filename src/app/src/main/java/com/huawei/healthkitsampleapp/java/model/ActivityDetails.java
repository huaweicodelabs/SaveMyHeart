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

public class ActivityDetails extends BaseObservable {
    private String mTitle;
    private String minHeartRate;
    private String maxHeartRate;
    private int mImage;
    private int mHeartRate;
    private int mName = 0;
    private int mValue = 0;

    public boolean active = false;

    public String getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMinHeartRate(String minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public String getMinHeartRate() {
        return minHeartRate;
    }

    public void setMaxHeartRate(String maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public int getmName() {
        return mName;
    }

    public void setmName(int name) {
        this.mName = name;
    }

    public void setmValue(int value) {
        this.mValue = value;
    }

    public int getmValue() {
        return mValue;
    }

    public boolean isActive() {
        return active;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        this.mImage = image;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getHeartRate() {
        return mHeartRate;
    }

    public void setHeartRate(int heartRate) {
        this.mHeartRate = heartRate;
    }
}
