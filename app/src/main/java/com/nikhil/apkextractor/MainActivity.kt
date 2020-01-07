package com.nikhil.apkextractor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    // Storage Permissions
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )
    private val APK_DIRECTORY =
        Environment.getExternalStorageDirectory().absolutePath + "/ApkExtractor"

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rv_apps)
        recyclerView.layoutManager = LinearLayoutManager(this)

        verifyStoragePermissions(this)
        //
        getListOfInstalledApps()
        //
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // create dir if not created
            createDir()
        } else {
            //Log permission denied
        }
    }

    private fun getListOfInstalledApps() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(mainIntent, 0)
        val packageNameList = mutableListOf<String>()
        for (app in apps) {
            if (!isSystemPackage(app))
                packageNameList.add(app.activityInfo.packageName)
        }
        recyclerView.adapter =
            AppsAdapter(this, packageNameList) { packageName -> downloadAPK(packageName) }
    }

    private fun createDir() {
        val myDir: String = APK_DIRECTORY
        Log.d("ApkExtractor", "dir = $APK_DIRECTORY")
        val dir = File(myDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    @Throws(IOException::class)
    fun copyFile(src: File, dst: File) {
        val var2 = FileInputStream(src)
        val var3 = FileOutputStream(dst)
        val var4 = ByteArray(1024)

        var var5: Int
        /* while ((var5 = var2.read(var4)) > 0) {
             var3.write(var4, 0, var5);
         }*/
        while (var2.read(var4).let { var5 = it; it != -1 }) {
            var3.write(var4, 0, var5);
        }

        var2.close()
        var3.close()
    }

    private fun downloadAPK(packageName: String) {
        Toast.makeText(this, "Apk clicked", Toast.LENGTH_SHORT).show()
        val info = this.packageManager.getApplicationInfo(packageName, 0)
        val appName = getAppName(this, packageName)
        copyFile(
            File(info.publicSourceDir),
            File("$APK_DIRECTORY/$appName.apk")
        )
    }
}
