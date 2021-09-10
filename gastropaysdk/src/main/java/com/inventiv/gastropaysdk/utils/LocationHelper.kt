package com.inventiv.gastropaysdk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.CommonInfoDialogFragment
import com.inventiv.gastropaysdk.utils.blankj.utilcode.constant.PermissionConstants
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils

class LocationHelper @JvmOverloads constructor(
    private val activity: FragmentActivity,
    private val fragment: Fragment? = null,
    private var callback: GlobalLocationCallback,
    private val lifecycle: Lifecycle
) {

    private val DEFAULT_REQUEST_CODE = 9999

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(activity)
    private val locationRequest = LocationRequest.create().apply {
        interval = 10_000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        PermissionUtils.permission(PermissionConstants.LOCATION)
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    val locationSettingsRequest = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                        .build()

                    settingsClient.checkLocationSettings(locationSettingsRequest)
                        .addOnSuccessListener {
                            requestLocationUpdates()
                        }
                        .addOnFailureListener { locationSettingFailureException ->
                            if (locationSettingFailureException is ResolvableApiException) {
                                try {
                                    fragment?.let {
                                        fragment.startIntentSenderForResult(
                                            locationSettingFailureException.resolution.intentSender,
                                            DEFAULT_REQUEST_CODE,
                                            null,
                                            0,
                                            0,
                                            0,
                                            null
                                        )
                                        return@addOnFailureListener
                                    }

                                    locationSettingFailureException.startResolutionForResult(
                                        activity,
                                        DEFAULT_REQUEST_CODE
                                    )
                                } catch (sendEx: SendIntentException) {
                                    // Ignore the error.
                                }
                            }
                        }
                } else if (deniedForever.isNotEmpty()) {
                    CommonInfoDialogFragment.newInstance(
                        R.drawable.ic_location_not_found_gastropay_sdk,
                        StringUtils.getString(R.string.merchants_warning_location_permission_title_gastropay_sdk),
                        StringUtils.getString(R.string.merchants_warning_location_permission_description_gastropay_sdk),
                        StringUtils.getString(R.string.merchants_restaurants_permission_dialog_button_text_gastropay_sdk),
                        {
                        }
                    ) {
                        val action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        activity.startActivity(
                            Intent(
                                action,
                                Uri.parse("package:${activity.packageName}")
                            )
                        )
                        it.dismiss()
                    }.show(activity.supportFragmentManager, null)
                }
            }.request()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        LogUtils.d("Location Helper requestLocationUpdates lifecycle:${lifecycle.currentState}")
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            return
        }
        callback.onLocationRequest()
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                LogUtils.d("Location Helper onSuccess lifecycle:${lifecycle.currentState}")
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    callback.onLocationResult(it)
                }
            }.addOnFailureListener {
                LogUtils.d("Location Helper onFail lifecycle:${lifecycle.currentState}")
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    callback.onLocationFailed()
                }
            }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == DEFAULT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            requestLocationUpdates()
            return true
        }
        return false
    }

    interface GlobalLocationCallback {
        fun onLocationResult(location: Location)
        fun onLocationFailed()
        fun onLocationRequest()
    }
}