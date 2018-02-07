package com.example.zhouzhihui.audioshift.ui

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 周智慧 on 07/02/2018.
 */
fun setRightDrawer(activity: Activity, recyclerview: CoolRecyclerView, file: File?) = file?.parentFile?.listFiles()?.filter { it.absolutePath != file.absolutePath }?.run {
    var adapter = RightDrawerAdap(this, activity)
    recyclerview.adapter = adapter
}


class RightDrawerAdap : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var mDatas = ArrayList<Any>()
    var mActivity: Activity? = null
    constructor(datas: List<Any>, listeners: Any) {
        mDatas.clear()
        mDatas.addAll(datas)
        mActivity = listeners as? Activity
    }
    override fun getItemCount(): Int = mDatas.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as? RightDrawerVH)?.bind(mDatas[position] as? File)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = RightDrawerVH(LayoutInflater.from(mActivity).inflate(R.layout.item_right_drawer, parent, false))

}

class RightDrawerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv_file_name = itemView.findViewById<TextView>(R.id.tv_file_name)
    var tv_file_size = itemView.findViewById<TextView>(R.id.tv_file_size)
    var tv_file_time = itemView.findViewById<TextView>(R.id.tv_file_time)
    fun bind(file: File?) {
        tv_file_name.text = file?.name
        tv_file_size.text = "${((file?.length() ?: 0) / 1000f)}KB"
        tv_file_time.text = SimpleDateFormat("YYYY/MM/dd aa hh:mm").format(file?.lastModified() ?: 0)
    }
}