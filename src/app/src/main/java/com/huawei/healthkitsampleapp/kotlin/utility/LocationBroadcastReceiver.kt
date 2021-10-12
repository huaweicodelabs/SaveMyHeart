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
package com.huawei.healthkitsampleapp.kotlin.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.huawei.hms.location.ActivityIdentificationResponse

class LocationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_LOCATION == action) {
                val activityIdentificationResponse = ActivityIdentificationResponse.getDataFromIntent(intent)
                val list = activityIdentificationResponse.activityIdentificationDatas
                val ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION"
                val i = Intent()
                i.putExtra(NAME, list[0].identificationActivity)
                i.putExtra(VALUE, list[0].possibility)
                i.action = ACTIVITY_RECOGNITION
                context.sendBroadcast(i)
            }
        }
    }

    companion object {
        const val ACTION_PROCESS_LOCATION = "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
        private const val NAME = "Name"
        private const val VALUE = "Value"
    }
}