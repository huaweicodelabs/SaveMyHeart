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
package com.huawei.healthkitsampleapp.kotlin.ui.home

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.huawei.healthkitsampleapp.R
import com.huawei.healthkitsampleapp.databinding.FragmentHomeKotlinBinding
import com.huawei.healthkitsampleapp.kotlin.adapter.AdsAdapter
import com.huawei.healthkitsampleapp.kotlin.model.ActivityDetails
import java.util.*

class HomeKotlinFragment : Fragment() {
    var mHomeKotlinViewModel: HomeKotlinViewModel? = null
    var mViewPager: ViewPager? = null
    private var mAarrayList: ArrayList<Int>? = null
    private var mLayoutDot: LinearLayout? = null
    lateinit var dot: Array<TextView?>
    var mActivityBinding: FragmentHomeKotlinBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mHomeKotlinViewModel = ViewModelProvider(this).get(HomeKotlinViewModel::class.java)
        mActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_kotlin, container, false)
        if (savedInstanceState == null) {
            mHomeKotlinViewModel!!.init()
        }
        mActivityBinding?.setModel(mHomeKotlinViewModel)
        setupListUpdate()
        mViewPager = mActivityBinding?.getRoot()?.findViewById<View>(R.id.viewpager) as ViewPager
        mLayoutDot = mActivityBinding?.getRoot()?.findViewById<View>(R.id.layout_dot) as LinearLayout
        mAarrayList = ArrayList()
        mAarrayList!!.add(R.color.red)
        mAarrayList!!.add(R.color.green)
        mAarrayList!!.add(R.color.colorPrimary)
        mAarrayList!!.add(R.color.colorAccent)
        val pagerAdapter = AdsAdapter(activity, mAarrayList!!)
        mViewPager!!.adapter = pagerAdapter
        mViewPager!!.pageMargin = MARGIN20
        addDot(0)

        // whenever the page changes
        mViewPager!!.addOnPageChangeListener(
                object : OnPageChangeListener {
                    override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
                    override fun onPageSelected(i: Int) {
                        addDot(i)
                    }

                    override fun onPageScrollStateChanged(i: Int) {}
                })
        return mActivityBinding?.getRoot()
    }

    fun addDot(page_position: Int) {
        dot = arrayOfNulls(mAarrayList!!.size)
        mLayoutDot!!.removeAllViews()
        for (i in dot.indices) {
            dot[i] = TextView(activity)
            dot[i]!!.text = Html.fromHtml(HTMLCODE)
            dot[i]!!.textSize = TEXTSIZE.toFloat()
            dot[i]!!.setTextColor(resources.getColor(R.color.darker_gray))
            mLayoutDot!!.addView(dot[i])
        }
        dot[page_position]!!.setTextColor(resources.getColor(R.color.red))
    }

    private fun setupListUpdate() {
        mHomeKotlinViewModel!!.loading!!.set(View.VISIBLE)
        mHomeKotlinViewModel!!.fetchList()
        mHomeKotlinViewModel
                ?.breeds
                ?.observe(
                        requireActivity(),
                        Observer { dogBreeds ->
                            mHomeKotlinViewModel!!.loading!!.set(View.GONE)
                            if (dogBreeds?.size == 0) {
                                mHomeKotlinViewModel!!.showEmpty!!.set(View.VISIBLE)
                            } else {
                                mHomeKotlinViewModel!!.showEmpty!!.set(View.GONE)
                                mHomeKotlinViewModel!!.setDogBreedsInAdapter(dogBreeds as List<ActivityDetails>?)
                            }
                        })
        setupListClick()
    }

    private fun setupListClick() {
        mHomeKotlinViewModel
                ?.selected
                ?.observe(
                        requireActivity(),
                        Observer { })
    }

    companion object {
        private const val HTMLCODE = "&#9673;"
        private const val TEXTSIZE = 25
        private const val MARGIN20 = 20
    }
}