## SaveMyHeart


## Table of Contents

 * [Introduction](#introduction)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample Code](#Sample-Code)
 * [License](#license)


## Introduction

    SaveMyHeart App will give insight about how HMS Kits can be easily integrated into your existing health app to help you to alert to the users when user’s heart rate reaches to maximum level. Here maximum heart rate can be calculated based on the user activity and the activity is capture either from Manual user section or from location activity recognition feature. In this code lab we are implementing below features with HMS kits.

    Health Kit 	:    Sample code of obtaining Runtime heartrate from Health App
    Location kit:    Sample code of obtaining Activity recognition.
    Ads			:    Sample code of Banner and Native Ads.


## Supported Environments
	Android SDK Version >= 15 and JDK version >= 1.8 is recommended.


## Configuration
Before running the app, you need to:
1. If you do not have a HUAWEI Developer account, you need to register an account and pass identity verification.
2. Use your account to sign in to AppGallery Connect, create an app, and set Package type to APK (Android app).

3. Enable the API permission for below kits from Project Setting > Manage APIs and enable the API permission.
	Health Kit
	Location kit
	Ads Kit            
	
	For Huawei Health Kit follow the link to enable this feature.
	See details: [https://developer.huawei.com/consumer/en/doc/health-introduce-0000001053684429-V5 ]

4. For Location kit and Ads
 	See details: [https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050706106]
	See details: [https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/publisher-service-introduction-0000001050064960 ]

5. After enabling service Apply for additional permissions for Health kit 
See details: [https://developer.huawei.com/consumer/en/doc/apply-kitservice-0000001050071707-V5]

4. Download the agconnect-services.json file from AppGallery Connect and replace place it in the application-level root directory.
Before compiling the APK, please make sure that the project includes the agconnect-services.json file, otherwise a compilation error will occur.


## Sample Code
  See details: 	[HUAWEI Health kit Introduction]
				(https://developer.huawei.com/consumer/en/codelab/HMSHealthKit-StepCount/index.html#5)
				[HUAWEI Ads Kit Introduction]
				(https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/publisher-service-introduction-0000001050064960)
				[HUAWEI Location kit]
				(https://developer.huawei.com/consumer/en/codelab/HMSLocationKit/index.html#8 )

##  License
  SaveMyHeart sample is licensed under the: [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

