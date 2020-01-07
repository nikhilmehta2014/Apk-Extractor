package com.nikhil.apkextractor

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

 fun getAppName(context: Context, apkPackageName: String): String {
    var name = ""
    var applicationInfo: ApplicationInfo
    var packageManager: PackageManager = context.packageManager
    try {
        applicationInfo = packageManager.getApplicationInfo(apkPackageName, 0)
        if (applicationInfo != null) {
            name = packageManager.getApplicationLabel(applicationInfo) as String
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return name
}

 fun getAppIconByPackageName(context: Context, apkPackageName: String): Drawable {
    val drawable: Drawable
    drawable = try {
        context.packageManager.getApplicationIcon(apkPackageName)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ContextCompat.getDrawable(context, R.mipmap.ic_launcher)!!
    }
    return drawable
}

fun isSystemPackage(resolveInfo: ResolveInfo): Boolean {
    return resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}