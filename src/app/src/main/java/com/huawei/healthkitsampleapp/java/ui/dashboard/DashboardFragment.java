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

package com.huawei.healthkitsampleapp.java.ui.dashboard;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.huawei.healthkitsampleapp.R;
import com.huawei.healthkitsampleapp.java.model.ActivityDetails;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;

public class DashboardFragment extends Fragment {
    public DashboardViewModel dashboardViewModel;
    public TextView statusTypeView, minHeartRateView, maxHeartRateView;
    private BannerView bottomBannerView;
    View root;
    public TextView heart_rate_text;
    int ANIM_DUR = 310;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ImageView iv = (ImageView) root.findViewById(R.id.my_imageview);

        ObjectAnimator scaleDown =
                ObjectAnimator.ofPropertyValuesHolder(
                        iv, PropertyValuesHolder.ofFloat("scaleX", 1.2f), PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(ANIM_DUR);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.start();

        initializer(root);
        initializeListener();
        loadBanner();
        return root;
    }

    public void initializer(View root) {
        HwAds.init(getActivity());
        statusTypeView = root.findViewById(R.id.statusType);
        minHeartRateView = root.findViewById(R.id.minHeartRate);
        maxHeartRateView = root.findViewById(R.id.maxHeartRate);
        bottomBannerView = root.findViewById(R.id.bannerview);
        heart_rate_text = (TextView) root.findViewById(R.id.heart_rate_text);
        heart_rate_text.setText(R.string.heartRateInitialValue);
    }

    public void initializeListener() {
        dashboardViewModel
                .getText()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<ActivityDetails>() {
                            @Override
                            public void onChanged(@Nullable ActivityDetails s) {
                                statusTypeView.setText(s.getTitle());
                                minHeartRateView.setText(s.getMinHeartRate());
                                maxHeartRateView.setText(s.getMaxHeartRate());
                            }
                        });
    }

    public void loadBanner() {
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dashboardViewModel = new ViewModelProvider(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel
                .getMessage()
                .observe(
                        getActivity(),
                        new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                heart_rate_text.setText(s);
                            }
                        });
    }
}
