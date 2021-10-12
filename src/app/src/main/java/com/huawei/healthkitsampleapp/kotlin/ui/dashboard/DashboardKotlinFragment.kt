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
package com.huawei.healthkitsampleapp.kotlin.ui.dashboard

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huawei.healthkitsampleapp.R
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.banner.BannerView

class DashboardKotlinFragment : Fragment() {
    var dashboardKotlinViewModel: DashboardKotlinViewModel? = null
    var statusTypeView: TextView? = null
    var minHeartRateView: TextView? = null
    var maxHeartRateView: TextView? = null
    private var bottomBannerView: BannerView? = null
    var root: View? = null
    var heart_rate_text: TextView? = null
    var ANIM_DUR = 310
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardKotlinViewModel = ViewModelProvider(this).get(DashboardKotlinViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_dashboard_kt, container, false)
        val iv = root?.findViewById<View>(R.id.my_imageview) as ImageView
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f))
        scaleDown.duration = ANIM_DUR.toLong()
        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.interpolator = FastOutSlowInInterpolator()
        scaleDown.start()
        initializer(root)
        initializeListener()
        loadBanner()
        return root
    }

    fun initializer(root: View?) {
        HwAds.init(activity)
        statusTypeView = root!!.findViewById(R.id.statusType)
        minHeartRateView = root.findViewById(R.id.minHeartRate)
        maxHeartRateView = root.findViewById(R.id.maxHeartRate)
        bottomBannerView = root.findViewById(R.id.bannerview)
        heart_rate_text = root.findViewById<View>(R.id.heart_rate_text) as TextView
        heart_rate_text!!.setText(R.string.heartRateInitialValue)
    }

    fun initializeListener() {
        dashboardKotlinViewModel
                ?.text
                ?.observe(
                        viewLifecycleOwner,
                        { s ->
                            statusTypeView?.setText(s?.title)
                            minHeartRateView?.setText(s?.minHeartRate)
                            maxHeartRateView?.setText(s?.maxHeartRate)
                        })
    }

    fun loadBanner() {
        val adParam = AdParam.Builder().build()
        bottomBannerView!!.loadAd(adParam)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dashboardKotlinViewModel = ViewModelProvider(requireActivity()).get(DashboardKotlinViewModel::class.java)
        dashboardKotlinViewModel
                ?.message
                ?.observe(
                        requireActivity(),
                        Observer { s -> heart_rate_text!!.text = s })
    }
}