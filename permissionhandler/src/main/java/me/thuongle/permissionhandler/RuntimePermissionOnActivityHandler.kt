package me.thuongle.permissionhandler

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.lang.ref.WeakReference

class RuntimePermissionOnActivityHandler(activity: Activity) : RuntimePermissionHandler() {

    private val mActivityRef: WeakReference<Activity> = WeakReference(activity)

    override fun isGranted(vararg permissions: String): Boolean {
        return permissions.none { permission ->
            mActivityRef.get()?.let {
                ContextCompat.checkSelfPermission(it, permission)
            } != PackageManager.PERMISSION_GRANTED
        }
    }

    override fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean {
        return permissions.any { permission ->
            mActivityRef.get()?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
            }!!
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun performRequest(vararg permissions: String) {
        mActivityRef.get()?.requestPermissions(permissions, RuntimePermissionHandler.Companion.REQUEST_PERMISSION_CODE)
    }

    override fun doesReferenceStillExist(): Boolean = mActivityRef.get() != null
}

