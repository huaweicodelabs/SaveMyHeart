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
package com.huawei.healthkitsampleapp.kotlin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.huawei.healthkitsampleapp.R
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.nativead.*
import java.util.*

class AdsAdapter(var context: Context?, var pager: ArrayList<Int>) : PagerAdapter() {
    override fun getCount(): Int {
        return pager.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.ads_item, container, false)
        loadAd(view, context!!.resources.getString(R.string.ad_id_native))
        container.addView(view)
        return view
    }

    private fun loadAd(view: View, adId: String) {
        val builder = NativeAdLoader.Builder(context, adId)
        builder.setNativeAdLoadedListener { nativeAd ->
            showNativeAd(nativeAd, view)
            nativeAd.setDislikeAdListener { }
        }
                .setAdListener(
                        object : AdListener() {
                            override fun onAdFailed(errorCode: Int) {
                                Log.i(TAG, context!!.resources.getString(R.string.onAdFailed) + errorCode)
                            }
                        })
        val adConfiguration = NativeAdConfiguration.Builder() // Set custom attributes.
                .build()
        val nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build()
        nativeAdLoader.loadAd(AdParam.Builder().build())
    }

    private fun showNativeAd(nativeAd: NativeAd, view: View) {
        val nativeView = view.findViewById<View>(R.id.native_video_view) as NativeView
        initNativeAdView(nativeAd, nativeView)
    }

    private fun initNativeAdView(nativeAd: NativeAd, nativeView: NativeView) {
        nativeView.titleView = nativeView.findViewById(R.id.ad_title)
        nativeView.mediaView = nativeView.findViewById<View>(R.id.ad_media) as MediaView
        nativeView.adSourceView = nativeView.findViewById(R.id.ad_source)
        nativeView.callToActionView = nativeView.findViewById(R.id.ad_call_to_action)
        (nativeView.titleView as TextView).text = nativeAd.title
        nativeView.mediaView.setMediaContent(nativeAd.mediaContent)
        if (null != nativeAd.adSource) {
            (nativeView.adSourceView as TextView).text = nativeAd.adSource
        }
        nativeView.adSourceView.visibility = if (null != nativeAd.adSource) View.VISIBLE else View.INVISIBLE
        if (null != nativeAd.callToAction) {
            (nativeView.callToActionView as Button).text = nativeAd.callToAction
        }
        nativeView
                .callToActionView.visibility = if (null != nativeAd.callToAction) View.VISIBLE else View.INVISIBLE
        nativeView.setNativeAd(nativeAd)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    companion object {
        private val TAG = AdsAdapter::class.java.simpleName
    }
}