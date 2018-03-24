package me.thuongle.permissionhandler

import android.content.pm.PackageManager
import android.os.Build

import java.util.ArrayList

abstract class RuntimePermissionHandler {

    private var mRequestPermissions: Array<PermissionBean>? = null
    private var mOnPermissionsGrantedListener: OnPermissionsGrantedListener? = null

    fun setOnPermissionsGrantedListener(listener: OnPermissionsGrantedListener) {
        this.mOnPermissionsGrantedListener = listener
    }

    fun requestPermissions(vararg permissions: String) {
        if (!doesReferenceStillExist()) return

        mRequestPermissions = Array<>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                val permissionBean = PermissionBean()
                if (isGranted(permission)) {
                    permissionBean.isGranted = true
                } else {
                    permissionBean.isGranted = false
                    permissionBean.permission = permission
                    mRequestPermissions!!.add(permissionBean)
                }
            }

            if (mRequestPermissions!!.isEmpty()) {
                if (mOnPermissionsGrantedListener != null) {
                    mOnPermissionsGrantedListener!!.onPermissionsGranted()
                }
                return
            }
            val requestPermissions = arrayOfNulls<String>(mRequestPermissions!!.size)
            for (i in mRequestPermissions!!.indices) {
                requestPermissions[i] = mRequestPermissions!![i].permission
            }
            performRequest(*requestPermissions)
        } else {
            if (mOnPermissionsGrantedListener != null) {
                mOnPermissionsGrantedListener!!.onPermissionsGranted()
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            var i = 0
            val size = permissions.size
            while (i < size) {
                updatePermissionResult(permissions[i], grantResults[i])
                i++
            }
            checkUpdate()
        }
    }

    abstract fun isGranted(vararg permissions: String): Boolean

    internal abstract fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean

    internal abstract fun performRequest(vararg permissions: String)

    internal abstract fun doesReferenceStillExist(): Boolean

    private fun updatePermissionResult(permission: String, grantResult: Int) {
        for (permissionBean in mRequestPermissions!!) {
            if (permissionBean.permission == permission) {
                permissionBean.isGranted = grantResult == PackageManager.PERMISSION_GRANTED
                break
            }
        }
    }

    private fun checkUpdate() {
        if (!doesReferenceStillExist()) return

        val deniedPermissions = mRequestPermissions!!.filterNot { it.isGranted }

        if (deniedPermissions.isEmpty()) {
            if (mOnPermissionsGrantedListener != null) {
                mOnPermissionsGrantedListener!!.onPermissionsGranted()
            }
        } else {
            if (mOnPermissionsGrantedListener != null) {
                mOnPermissionsGrantedListener!!.onPermissionsDenied()

                //setAlertMessage();
                val shouldShowPermissions = ArrayList<String>()
                for (deniedPermission in deniedPermissions) {
                    if (shouldShowRequestPermissionRationale(deniedPermission.permission!!)) {
                        shouldShowPermissions.add(deniedPermission.permission)

                        mOnPermissionsGrantedListener!!.shouldShowRequestPermissionRationale(shouldShowPermissions)
                    }
                }
            }
        }
    }

    private inner class PermissionBean {
        internal var permission: String? = null
        internal var isGranted: Boolean = false
    }

    interface OnPermissionsGrantedListener {

        fun onPermissionsGranted()

        fun onPermissionsDenied()

        fun shouldShowRequestPermissionRationale(onPermissions: List<String>)
    }

    companion object {

        const val REQUEST_PERMISSION_CODE = 33
    }
}

