package com.nikhil.apkextractor

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    // Storage Permissions
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        verifyStoragePermissions(this)
        // create dir if not created
        createDir()
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
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun getListOfInstalledApps() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(mainIntent, 0)
        val packageNameList = mutableListOf<String>()
        for (app in apps) {
            packageNameList.add(app.activityInfo.packageName)
        }
        Log.d("ApkExtractor", "package list = $packageNameList")
        for (info: ResolveInfo in apps) {
            val file = File(info.activityInfo.applicationInfo.publicSourceDir)
            Log.d("ApkExtractor", "dir = ${info.activityInfo.applicationInfo.publicSourceDir}")
//            if (info.activityInfo.applicationInfo.publicSourceDir == "/data/app/com.KCamsApp-mc4xED2hAlV-txbxWQvHUA==/base.apk") {
            if (info.activityInfo.applicationInfo.publicSourceDir == "/data/app/com.transno.app-7GXoRl2HD9M7N5g3fYxbVw==/base.apk") {
                //copy file from /data to /Custom_Folder
                copyFile(
                    File(info.activityInfo.applicationInfo.publicSourceDir),
                    File(Environment.getExternalStorageDirectory().absolutePath + "/aaa/a.apk")
                )
            }
        }
    }

    private fun createDir() {
        val myDir: String = Environment.getExternalStorageDirectory().absolutePath + "/aaa/"
        val walpy = File(myDir)
        if (!walpy.exists()) {
            walpy.mkdirs()
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
}
