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

package com.huawei.healthkitsampleapp.java.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.huawei.healthkitsampleapp.R;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;

import java.util.ArrayList;

public class AdsAdapter extends PagerAdapter {
    private static final String TAG = AdsAdapter.class.getSimpleName();
    Context context;
    ArrayList<Integer> pager;

    public AdsAdapter(Context context, ArrayList<Integer> pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public int getCount() {
        return pager.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.ads_item, container, false);
        loadAd(view, context.getResources().getString(R.string.ad_id_native));
        container.addView(view);

        return view;
    }

    private void loadAd(View view, String adId) {
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, adId);

        builder.setNativeAdLoadedListener(
                new NativeAd.NativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        showNativeAd(nativeAd, view);

                        nativeAd.setDislikeAdListener(
                                new DislikeAdListener() {
                                    @Override
                                    public void onAdDisliked() {
                                    }
                                });
                    }
                })
                .setAdListener(
                        new AdListener() {
                            @Override
                            public void onAdFailed(int errorCode) {
                                Log.i(TAG, context.getResources().getString(R.string.onAdFailed) + errorCode);
                            }
                        });

        NativeAdConfiguration adConfiguration =
                new NativeAdConfiguration.Builder() // Set custom attributes.
                        .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();

        nativeAdLoader.loadAd(new AdParam.Builder().build());
    }

    private void showNativeAd(NativeAd nativeAd, View view) {
        NativeView nativeView = (NativeView) view.findViewById(R.id.native_video_view);
        initNativeAdView(nativeAd, nativeView);
    }

    private void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) nativeView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(nativeView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(nativeView.findViewById(R.id.ad_call_to_action));
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }
        nativeView.getAdSourceView().setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView
                .getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeView.setNativeAd(nativeAd);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
