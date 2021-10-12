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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.huawei.healthkitsampleapp.BR
import com.huawei.healthkitsampleapp.R
import com.huawei.healthkitsampleapp.kotlin.model.ActivityDetails
import com.huawei.healthkitsampleapp.kotlin.ui.home.HomeKotlinViewModel

class ActivityAdapter(@param:LayoutRes private val layoutId: Int, private val viewModel: HomeKotlinViewModel) : RecyclerView.Adapter<ActivityAdapter.GenericViewHolder>() {
    private var breeds: List<ActivityDetails>? = null
    private var context: Context? = null
    private fun getLayoutIdForPosition(position: Int): Int {
        return layoutId
    }

    override fun getItemCount(): Int {
        return if (breeds == null) ITEM_COUNT else breeds!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        context = parent.context
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    fun setDogBreeds(breeds: List<ActivityDetails>?) {
        this.breeds = breeds
    }

    inner class GenericViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: HomeKotlinViewModel?, position: Int?) {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
            val activeText = binding.root.findViewById<View>(R.id.active_button) as TextView
            if (breeds!![position!!].isActive) {
                activeText.text = context!!.resources.getString(R.string.active)
            } else {
                activeText.text = context!!.resources.getString(R.string.in_active)
            }
        }
    }

    companion object {
        private const val ITEM_COUNT = 0
    }
}