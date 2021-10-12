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
package com.huawei.healthkitsampleapp.kotlin

import android.Manifest
import android.app.PendingIntent
import android.content.*
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.healthkitsampleapp.R
import com.huawei.healthkitsampleapp.kotlin.ui.dashboard.AlertHeartRateDialog
import com.huawei.healthkitsampleapp.kotlin.ui.dashboard.DashboardKotlinViewModel
import com.huawei.healthkitsampleapp.kotlin.ui.home.HomeKotlinFragment
import com.huawei.healthkitsampleapp.kotlin.utility.LocationBroadcastReceiver
import com.huawei.hihealth.error.HiHealthError
import com.huawei.hihealthkit.auth.HiHealthAuth
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType
import com.huawei.hihealthkit.data.store.HiHealthDataStore
import com.huawei.hihealthkit.data.store.HiRealTimeListener
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class HomePageActivity : AppCompatActivity() {
    private var mHeartRate = 0
    private var mPendingIntent: PendingIntent? = null
    private var mActivityIdentificationService: ActivityIdentificationService? = null
    private var mSettingsClient: SettingsClient? = null
    var mDashboardKotlinViewModel: DashboardKotlinViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_kt)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view_kt)
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_home_kt, R.id.navigation_dashboard_kt, R.id.navigation_notifications)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_kt)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        mDashboardKotlinViewModel = ViewModelProvider(this).get(DashboardKotlinViewModel::class.java)
        mDashboardKotlinViewModel!!.init()
        mDashboardKotlinViewModel!!.sendData(getString(R.string._00))
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_PROCESS_LOCATION))
        mSettingsClient = LocationServices.getSettingsClient(this@HomePageActivity)
        checkLocationPermission()
        permissionList
        signIn()
    }

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent != null) {
                val action = intent.action
                if (ACTION_PROCESS_LOCATION == action) {
                    updateActivity(intent.getIntExtra(NAME, DEFAULT_VALUE), intent.getIntExtra(VALUE, DEFAULT_VALUE))
                }
            }
        }
    }

    fun updateActivity(name: Int, value: Int) {
        try {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_kt) as NavHostFragment?
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment_kt)
            if (navController.currentDestination!!.id == R.id.navigation_home_kt) {
                var i = 0
                while (i
                        < (navHostFragment!!.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel!!.dogBreeds!!.activityDetailsList.size) {
                    if ((navHostFragment.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel!!.dogBreeds!!.activityDetailsList[i]
                                    ?.getmName()
                            == name) {
                        (navHostFragment.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel!!.dogBreeds!!.activityDetailsList[i]
                        (navHostFragment.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel!!.dogBreeds!!.activityDetailsList[i]?.isActive = true
                    } else {
                        (navHostFragment.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel!!.dogBreeds!!.activityDetailsList[i]?.isActive = false
                    }
                    i++
                }
                (navHostFragment.childFragmentManager.fragments[0] as HomeKotlinFragment).mHomeKotlinViewModel
                        ?.adapter
                        ?.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun signIn() {
        val scopeList: MutableList<Scope> = ArrayList()
        scopeList.add(Scope(getString(R.string.URL)))
        val authParamsHelper = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
        val authParams = authParamsHelper.setIdToken()
                .setAccessToken()
                .setScopeList(scopeList)
                .createParams()
        val authService = HuaweiIdAuthManager.getService(this.applicationContext, authParams)
        val authHuaweiIdTask = authService.silentSignIn()
        authHuaweiIdTask
                .addOnSuccessListener { getHeartRate }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        val signInIntent = authService.signInIntent
                        this@HomePageActivity.startActivityForResult(signInIntent, com.huawei.healthkitsampleapp.kotlin.HomePageActivity.REQUEST_SIGN_IN_LOGIN)
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleSignInResult(requestCode, data)
    }

    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data        Authorization result response.
     */
    private fun handleSignInResult(requestCode: Int, data: Intent?) {
        if (requestCode != REQUEST_SIGN_IN_LOGIN) {
            return
        }
        val result = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data)
        if (result.isSuccess) {
            getHeartRate
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        setLocationSettings()
    }

    fun setLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
        val mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        mSettingsClient
                ?.checkLocationSettings(locationSettingsRequest)
                ?.addOnSuccessListener {
                    mActivityIdentificationService = ActivityIdentification.getService(this@HomePageActivity)
                    mPendingIntent = pendingIntent
                    activityIdentification
                }
                ?.addOnFailureListener { e ->
                    val statusCode = (e as ApiException).statusCode
                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@HomePageActivity, 0)
                        } catch (ignored: SendIntentException) {
                            Log.i(TAG, getString(R.string.createActivityIdentificationUpdates))
                        }
                    }
                }
    }

    fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                            != PackageManager.PERMISSION_GRANTED)) {
                val strings = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_BACKGROUND_LOCATION")
                            != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                            != PackageManager.PERMISSION_GRANTED)) {
                val strings = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            } else {
                mActivityIdentificationService = ActivityIdentification.getService(this@HomePageActivity)
                mPendingIntent = pendingIntent
                activityIdentification
            }
        }
    }

    private val pendingIntent: PendingIntent
        private get() {
            val intent = Intent(this, LocationBroadcastReceiver::class.java)
            intent.action = LocationBroadcastReceiver.Companion.ACTION_PROCESS_LOCATION
            return PendingIntent.getBroadcast(this, RESULTCODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    val activityIdentification: Unit
        get() {
            mActivityIdentificationService
                    ?.createActivityIdentificationUpdates(RESULTCODEIDENTIFICATION.toLong(), mPendingIntent)
                    ?.addOnSuccessListener { Log.i(TAG, getString(R.string.success)) }
                    ?.addOnFailureListener { e ->
                        if (e.message.equals(getString(R.string.result_code_10803), ignoreCase = true)) {
                            Log.i(TAG, getString(R.string.fail))
                        }
                    }
        }
    val permissionList: Unit
        get() {
            val writeTypes = intArrayOf()

            val readTypes = intArrayOf(
                    HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE
            )
            HiHealthAuth.getDataAuthStatusEx(
                    this@HomePageActivity,
                    writeTypes,
                    readTypes
            ) { resultCode, resultMsg, writeList, readList ->
                if (resultCode == HiHealthError.SUCCESS) {
                    getHeartRate
                } else {
                    access
                }
            }
        }
    val access: Unit
        get() {
            val userAllowTypesToRead = intArrayOf(
                    HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE
            )

            val userAllowTypesToWrite = intArrayOf()
            HiHealthAuth.requestAuthorization(
                    this@HomePageActivity,
                    userAllowTypesToWrite,
                    userAllowTypesToRead
            ) { resultCode, `object` ->
                if (resultCode == HiHealthError.SUCCESS) {
                    getHeartRate
                }
            }
        }
    val getHeartRate: Unit
        get() {
            HiHealthDataStore.startReadingHeartRate(
                    this@HomePageActivity,
                    object : HiRealTimeListener {
                        override fun onResult(state: Int) {}
                        override fun onChange(resultCode: Int, value: String) {
                            if (resultCode == HiHealthError.SUCCESS) {
                                try {
                                    val jsonObject = JSONObject(value)
                                    mHeartRate = jsonObject.getInt(getString(R.string.hr_info))
                                    if (mHeartRate > HRTMAX) {
                                        stopService()
                                        val ft = supportFragmentManager.beginTransaction()
                                        val newFragment = AlertHeartRateDialog(
                                                getString(R.string.heart_rate_at_risk_msg),
                                                getString(R.string.heart_rate_at_risk_high))
                                        newFragment.show(ft, getString(R.string.dialog))
                                    } else if (mHeartRate < HRTMIN) {
                                        stopService()
                                        val ft = supportFragmentManager.beginTransaction()
                                        val newFragment = AlertHeartRateDialog(
                                                getString(R.string.heart_rate_at_risk_msg),
                                                getString(R.string.heart_rate_at_risk_low))
                                        newFragment.show(ft, getString(R.string.dialog))
                                    }
                                    try {
                                        runOnUiThread { mDashboardKotlinViewModel!!.sendData("" + mHeartRate) }
                                    } catch (e: Exception) {
                                        Log.i(TAG, e.message)
                                    }
                                } catch (e: JSONException) {
                                    Log.e(TAG, getString(R.string.mess) + e.message)
                                }
                            } else if (resultCode == 4 && value.equals(getString(R.string.deivce_exception), ignoreCase = true)) {
                                val ft = supportFragmentManager.beginTransaction()
                                val newFragment = AlertHeartRateDialog(
                                        getString(R.string.someting_went_wrong), getString(R.string.wear_band))
                                newFragment.show(ft, getString(R.string.dialog))
                            }
                        }
                    })
        }

    fun stopService() {
        HiHealthDataStore.stopReadingHeartRate(
                this@HomePageActivity,
                object : HiRealTimeListener {
                    override fun onResult(i: Int) {}
                    override fun onChange(i: Int, s: String) {}
                })
    }

    companion object {
        private const val RESULTCODEIDENTIFICATION = 5000
        private const val RESULTCODE = 0
        private const val HRTMAX = 100
        private const val HRTMIN = 50
        private const val DEFAULT_VALUE = 0
        private const val REQUEST_SIGN_IN_LOGIN = 1002
        private val TAG = HomePageActivity::class.java.simpleName
        const val ACTION_PROCESS_LOCATION = "ACTIVITY_RECOGNITION"
        private const val NAME = "Name"
        private const val VALUE = "Value"
    }
}