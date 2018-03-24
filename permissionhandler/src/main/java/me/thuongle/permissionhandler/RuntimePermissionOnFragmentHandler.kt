package me.thuongle.permissionhandler

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

import java.lang.ref.WeakReference

//we split activity and fragment because of this: https://stackoverflow.com/questions/32714787/android-m-permissions-onrequestpermissionsresult-not-being-called
@TargetApi(Build.VERSION_CODES.M)
class RuntimePermissionOnFragmentHandler(fragment: Fragment) : RuntimePermissionHandler() {

    private val mFragmentRef: WeakReference<Fragment> = WeakReference(fragment)

    override fun isGranted(vararg permissions: String): Boolean {
        return permissions.none { permission ->
            mFragmentRef.get()?.context?.let {
                ContextCompat.checkSelfPermission(it, permission)
            } != PackageManager.PERMISSION_GRANTED
        }
    }

    override fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean {
        return permissions.any { mFragmentRef.get()?.shouldShowRequestPermissionRationale(it)!! }
    }

    override fun performRequest(vararg permissions: String) {
        mFragmentRef.get()?.requestPermissions(permissions, RuntimePermissionHandler.Companion.REQUEST_PERMISSION_CODE)
    }

    override fun doesReferenceStillExist(): Boolean = mFragmentRef.get() != null
}

