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

package com.huawei.healthkitsampleapp.java.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huawei.hms.location.ActivityIdentificationData;
import com.huawei.hms.location.ActivityIdentificationResponse;

import java.util.List;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_PROCESS_LOCATION = "com.huawei.hms.location.ACTION_PROCESS_LOCATION";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_LOCATION.equals(action)) {
                ActivityIdentificationResponse activityIdentificationResponse =
                        ActivityIdentificationResponse.getDataFromIntent(intent);
                List<ActivityIdentificationData> list = activityIdentificationResponse.getActivityIdentificationDatas();
                final String ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION";
                Intent i = new Intent();
                i.putExtra(NAME, list.get(0).getIdentificationActivity());
                i.putExtra(VALUE, list.get(0).getPossibility());
                i.setAction(ACTIVITY_RECOGNITION);
                context.sendBroadcast(i);
            }
        }
    }
}
