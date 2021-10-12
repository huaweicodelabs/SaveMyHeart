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

package com.huawei.healthkitsampleapp.java;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.healthkitsampleapp.R;
import com.huawei.healthkitsampleapp.java.ui.dashboard.AlertHeartRateDialog;
import com.huawei.healthkitsampleapp.java.ui.dashboard.DashboardViewModel;
import com.huawei.healthkitsampleapp.java.ui.home.HomeFragment;
import com.huawei.healthkitsampleapp.java.utility.LocationBroadcastReceiver;
import com.huawei.hihealth.error.HiHealthError;
import com.huawei.hihealth.listener.ResultCallback;
import com.huawei.hihealthkit.auth.HiHealthAuth;
import com.huawei.hihealthkit.auth.HiHealthOpenPermissionType;
import com.huawei.hihealthkit.auth.IAuthorizationListener;
import com.huawei.hihealthkit.auth.IDataAuthStatusListener;
import com.huawei.hihealthkit.data.HiHealthExtendScope;
import com.huawei.hihealthkit.data.store.HiHealthDataStore;
import com.huawei.hihealthkit.data.store.HiRealTimeListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.location.ActivityIdentification;
import com.huawei.hms.location.ActivityIdentificationService;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private static final int RESULTCODEIDENTIFICATION = 5000;
    private static final int RESULTCODE = 0;
    private static final int HRTMAX = 100;
    private static final int HRTMIN = 50;
    private static final int DEFAULT_VALUE = 0;
    private static final int REQUEST_SIGN_IN_LOGIN = 1002;
    private static final String TAG = HomePageActivity.class.getSimpleName();
    public static final String ACTION_PROCESS_LOCATION = "ACTIVITY_RECOGNITION";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";
    private int mHeartRate = 0;
    private PendingIntent mPendingIntent;
    private ActivityIdentificationService mActivityIdentificationService;
    private SettingsClient mSettingsClient;

    DashboardViewModel mDashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                        .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mDashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        mDashboardViewModel.init();
        mDashboardViewModel.sendData(getString(R.string._00));
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_PROCESS_LOCATION));
        mSettingsClient = LocationServices.getSettingsClient(HomePageActivity.this);
        checkLocationPermission();
        getPermissionList();
        signIn();
        getApiLevel();
    }

    private void getApiLevel() {
        HiHealthDataStore.getApiLevel(HomePageActivity.this, new ResultCallback() {
            @Override
            public void onResult(int resultCode, Object message) {
                if (resultCode == HiHealthError.SUCCESS) {
                    Log.i(TAG, "getApiLevel success");
                    if (message instanceof Integer) {
                        int apiLevel = (int) message;
                        Log.i(TAG, " apiLevel:" + apiLevel);
                    }
                }
            }
        });
    }

    BroadcastReceiver broadcastReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        final String action = intent.getAction();
                        if (ACTION_PROCESS_LOCATION.equals(action)) {
                            updateActivity(intent.getIntExtra(NAME, DEFAULT_VALUE), intent.getIntExtra(VALUE, DEFAULT_VALUE));
                        }
                    }
                }
            };

    public void updateActivity(int name, int value) {
        try {
            NavHostFragment navHostFragment =
                    (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            if (navController.getCurrentDestination().getId() == R.id.navigation_home) {
                for (int i = 0;
                     i < ((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                             .mHomeViewModel.dogBreeds.activityDetailsList.size();
                     i++) {
                    if (((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                            .mHomeViewModel
                            .dogBreeds
                            .activityDetailsList
                            .get(i)
                            .getmName()
                            == name) {
                        ((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                                .mHomeViewModel.dogBreeds.activityDetailsList.get(i);
                        ((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                                .mHomeViewModel.dogBreeds.activityDetailsList.get(i).active =
                                true;
                    } else {
                        ((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                                .mHomeViewModel.dogBreeds.activityDetailsList.get(i).active =
                                false;
                    }
                }

                ((HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0))
                        .mHomeViewModel
                        .getAdapter()
                        .notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void signIn() {
        List<Scope> scopeList = new ArrayList<>();
        scopeList.add(new Scope(getString(R.string.URL)));
        HuaweiIdAuthParamsHelper authParamsHelper =
                new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM);
        HuaweiIdAuthParams authParams = authParamsHelper.setIdToken()
                .setAccessToken()
                .setScopeList(scopeList)
                .createParams();
        final HuaweiIdAuthService authService =
                HuaweiIdAuthManager.getService(this.getApplicationContext(), authParams);
        Task<AuthHuaweiId> authHuaweiIdTask = authService.silentSignIn();
        authHuaweiIdTask
                .addOnSuccessListener(
                        new OnSuccessListener<AuthHuaweiId>() {
                            @Override
                            public void onSuccess(AuthHuaweiId huaweiId) {
                                getHeartRate();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
                                if (exception instanceof ApiException) {
                                    Intent signInIntent = authService.getSignInIntent();
                                    HomePageActivity.this.startActivityForResult(signInIntent, REQUEST_SIGN_IN_LOGIN);
                                }
                            }
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleSignInResult(requestCode, data);
    }

    /**
     * Method of handling authorization result responses
     *
     * @param requestCode Request code for displaying the authorization screen.
     * @param data        Authorization result response.
     */
    private void handleSignInResult(int requestCode, Intent data) {
        if (requestCode != REQUEST_SIGN_IN_LOGIN) {
            return;
        }

        HuaweiIdAuthResult result = HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
        if (result.isSuccess()) {
            getAccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setLocationSettings();
    }

    public void setLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        LocationRequest mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        mSettingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                mActivityIdentificationService =
                                        ActivityIdentification.getService(HomePageActivity.this);
                                mPendingIntent = getPendingIntent();
                                getActivityIdentification();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                int statusCode = ((ApiException) e).getStatusCode();
                                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                    try {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(HomePageActivity.this, 0);
                                    } catch (IntentSender.SendIntentException ignored) {
                                        Log.i(TAG, getString(R.string.createActivityIdentificationUpdates));
                                    }
                                }
                            }
                        });
    }

    public void checkLocationPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                };
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_BACKGROUND_LOCATION")
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACTIVITY_RECOGNITION
                };
                ActivityCompat.requestPermissions(this, strings, 2);
            } else {
                mActivityIdentificationService = ActivityIdentification.getService(HomePageActivity.this);
                mPendingIntent = getPendingIntent();
                getActivityIdentification();
            }
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationBroadcastReceiver.class);
        intent.setAction(LocationBroadcastReceiver.ACTION_PROCESS_LOCATION);
        return PendingIntent.getBroadcast(this, RESULTCODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void getActivityIdentification() {
        mActivityIdentificationService
                .createActivityIdentificationUpdates(RESULTCODEIDENTIFICATION, mPendingIntent)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, getString(R.string.success));
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                if (e.getMessage().equalsIgnoreCase(getString(R.string.result_code_10803))) {
                                    Log.i(TAG, getString(R.string.fail));
                                }
                            }
                        });
    }

    public void getPermissionList() {
        int[] writeTypes = new int[]{};

        int[] readTypes =
                new int[]{
                        HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE
                };
        HiHealthAuth.getDataAuthStatusEx(
                HomePageActivity.this,
                writeTypes,
                readTypes,
                new IDataAuthStatusListener() {
                    @Override
                    public void onResult(int resultCode, String resultMsg, int[] writeList, int[] readList) {
                        if (resultCode == HiHealthError.SUCCESS) {
                            getHeartRate();
                        } else {
                            getAccess();
                        }
                    }
                });
    }

    public void getAccess() {
        int[] userAllowTypesToRead =
                new int[]{
                        HiHealthOpenPermissionType.HEALTH_OPEN_PERMISSION_TYPE_READ_REALTIME_HEARTRATE
                };

        int[] userAllowTypesToWrite =
                new int[]{};
        HiHealthAuth.requestAuthorization(
                HomePageActivity.this,
                userAllowTypesToWrite,
                userAllowTypesToRead,
                new IAuthorizationListener() {
                    @Override
                    public void onResult(int resultCode, Object object) {
                        if (resultCode == HiHealthError.SUCCESS) {
                            getHeartRate();
                        }
                    }
                });
    }

    public void getHeartRate() {
        HiHealthDataStore.startReadingHeartRate(HomePageActivity.this, new HiRealTimeListener() {
            @Override
            public void onResult(int state) {
            }

            @Override
            public void onChange(int resultCode, String value) {
                if (resultCode == HiHealthError.SUCCESS) {
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        mHeartRate = jsonObject.getInt(getString(R.string.hr_info));
                        if (mHeartRate > HRTMAX) {
                            stopService();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            AlertHeartRateDialog newFragment =
                                    new AlertHeartRateDialog(
                                            getString(R.string.heart_rate_at_risk_msg),
                                            getString(R.string.heart_rate_at_risk_high));
                            newFragment.show(ft, getString(R.string.dialog));
                        } else if (mHeartRate < HRTMIN) {
                            stopService();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            AlertHeartRateDialog newFragment =
                                    new AlertHeartRateDialog(
                                            getString(R.string.heart_rate_at_risk_msg),
                                            getString(R.string.heart_rate_at_risk_low));
                            newFragment.show(ft, getString(R.string.dialog));
                        }

                        try {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            mDashboardViewModel.sendData("" + mHeartRate);
                                        }
                                    });
                        } catch (Exception e) {
                            Log.i(TAG, e.getMessage());
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException e" + e.getMessage());
                    }
                }
            }
        });
    }

    public void stopService() {
        HiHealthDataStore.stopReadingHeartRate(
                HomePageActivity.this,
                new HiRealTimeListener() {
                    @Override
                    public void onResult(int i) {
                    }

                    @Override
                    public void onChange(int i, String s) {
                    }
                });
    }
}
