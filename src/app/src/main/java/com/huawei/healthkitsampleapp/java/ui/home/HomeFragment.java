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

package com.huawei.healthkitsampleapp.java.ui.home;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.huawei.healthkitsampleapp.R;
import com.huawei.healthkitsampleapp.java.adapter.AdsAdapter;
import com.huawei.healthkitsampleapp.databinding.FragmentHomeBinding;
import com.huawei.healthkitsampleapp.java.model.ActivityDetails;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public HomeViewModel mHomeViewModel;
    private static final String HTMLCODE = "&#9673;";
    private static final int TEXTSIZE = 25;
    ViewPager mViewPager;
    private ArrayList<Integer> mAarrayList;
    private LinearLayout mLayoutDot;
    TextView[] dot;
    FragmentHomeBinding mActivityBinding;
    private static final int MARGIN20 = 20;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        if (savedInstanceState == null) {
            mHomeViewModel.init();
        }
        mActivityBinding.setModel(mHomeViewModel);
        setupListUpdate();

        mViewPager = (ViewPager) mActivityBinding.getRoot().findViewById(R.id.viewpager);
        mLayoutDot = (LinearLayout) mActivityBinding.getRoot().findViewById(R.id.layout_dot);
        mAarrayList = new ArrayList<>();

        mAarrayList.add(R.color.red);
        mAarrayList.add(R.color.green);
        mAarrayList.add(R.color.colorPrimary);
        mAarrayList.add(R.color.colorAccent);

        AdsAdapter pagerAdapter = new AdsAdapter(getActivity(), mAarrayList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageMargin(MARGIN20);
        addDot(0);

        // whenever the page changes
        mViewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int i) {
                        addDot(i);
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });

        return mActivityBinding.getRoot();
    }

    public void addDot(int page_position) {
        dot = new TextView[mAarrayList.size()];
        mLayoutDot.removeAllViews();
        for (int i = 0; i < dot.length; i++) {
            ;
            dot[i] = new TextView(getActivity());
            dot[i].setText(Html.fromHtml(HTMLCODE));
            dot[i].setTextSize(TEXTSIZE);
            dot[i].setTextColor(getResources().getColor(R.color.darker_gray));
            mLayoutDot.addView(dot[i]);
        }
        dot[page_position].setTextColor(getResources().getColor(R.color.red));
    }

    private void setupListUpdate() {
        mHomeViewModel.loading.set(View.VISIBLE);
        mHomeViewModel.fetchList();
        mHomeViewModel
                .getBreeds()
                .observe(
                        getActivity(),
                        new Observer<List<ActivityDetails>>() {
                            @Override
                            public void onChanged(List<ActivityDetails> dogBreeds) {
                                mHomeViewModel.loading.set(View.GONE);
                                if (dogBreeds.size() == 0) {
                                    mHomeViewModel.showEmpty.set(View.VISIBLE);
                                } else {
                                    mHomeViewModel.showEmpty.set(View.GONE);
                                    mHomeViewModel.setDogBreedsInAdapter(dogBreeds);
                                }
                            }
                        });
        setupListClick();
    }

    private void setupListClick() {
        mHomeViewModel
                .getSelected()
                .observe(
                        getActivity(),
                        new Observer<ActivityDetails>() {
                            @Override
                            public void onChanged(ActivityDetails activityDetails) {
                            }
                        });
    }
}
