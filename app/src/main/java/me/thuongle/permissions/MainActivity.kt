package me.thuongle.permissions

import android.Manifest
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.thuongle.permissionhandler.RuntimePermissionHandler
import me.thuongle.permissionhandler.RuntimePermissionOnActivityHandler

class MainActivity : AppCompatActivity(), RuntimePermissionHandler.OnPermissionsGrantedListener {

    private var mRuntimePermissionHandler: RuntimePermissionHandler? = null

    private val mRequiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            mRuntimePermissionHandler = RuntimePermissionOnActivityHandler(this)
            mRuntimePermissionHandler?.setOnPermissionsGrantedListener(this)
            mRuntimePermissionHandler?.requestPermissions(*mRequiredPermissions)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPermissionsGranted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionsDenied() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shouldShowRequestPermissionRationale(onPermissions: List<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
