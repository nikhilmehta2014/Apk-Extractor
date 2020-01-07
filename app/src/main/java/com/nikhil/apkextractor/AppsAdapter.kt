package com.nikhil.apkextractor

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_app.view.*

class AppsAdapter(
    private val context: Context,
    private val packageNameList: List<String>,
    private val listener: (String) -> Unit
) :
    RecyclerView.Adapter<AppsAdapter.AppsViewHolder>() {

    private var appNamesList = arrayListOf<String>()
    private var appIconsList = arrayListOf<Drawable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        return AppsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_app,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        for (packageName in packageNameList) {
            val appName = getAppName(context, packageName)
            appNamesList.add(appName)
            holder.appName.text = appNamesList[position]
            val appIcon = getAppIconByPackageName(context, packageName)
            appIconsList.add(appIcon)
            holder.appIcon.setImageDrawable(appIconsList[position])
        }

        holder.bind(packageNameList[position], listener)
    }

    override fun getItemCount(): Int {
        return packageNameList.size
    }

    class AppsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon = view.iv_app
        val appName = view.tv_app

        fun bind(packageName: String, listener: (String) -> Unit) {
            itemView.setOnClickListener { listener(packageName) }
        }
    }
}
