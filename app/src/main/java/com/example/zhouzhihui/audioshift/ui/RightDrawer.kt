package com.example.zhouzhihui.audioshift.ui

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.zhouzhihui.audioshift.R
import com.example.zhouzhihui.audioshift.TAG
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 周智慧 on 07/02/2018.
 */

class RightDrawerAdap : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var curMaxPos = -1
    var shouldUpdateData = false
    var mDatas = ArrayList<Any>()
    var mActivity: Activity? = null
    constructor(datas: List<Any>?, listeners: Any) {
        mDatas.clear()
        if (datas != null) {
            mDatas.addAll(datas)
        }
//        mDatas.addAll(mDatas)
//        mDatas.addAll(mDatas)
//        mDatas.addAll(mDatas)
        mActivity = listeners as? Activity
    }
    override fun getItemCount(): Int = mDatas.size

    fun updateData(datas: List<Any>?) = apply {
        mDatas.clear()
        if (datas != null) {
            mDatas.addAll(datas)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as? RightDrawerVH)?.bind(mDatas[position] as? File, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = RightDrawerVH(LayoutInflater.from(mActivity).inflate(R.layout.item_right_drawer, parent, false))

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.clearAnimation()
        Log.i(TAG, "onViewRecycled ${holder?.adapterPosition} ${holder?.layoutPosition}")
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        holder?.itemView?.clearAnimation()
    }
}

class RightDrawerVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv_file_name = itemView.findViewById<TextView>(R.id.tv_file_name)
    var tv_file_size = itemView.findViewById<TextView>(R.id.tv_file_size)
    var tv_file_time = itemView.findViewById<TextView>(R.id.tv_file_time)
    fun bind(file: File?, adapter: RightDrawerAdap) {
        tv_file_name.text = file?.name
        tv_file_size.text = "${DecimalFormat("#.##").format(((file?.length() ?: 0) / 1000f))}KB"
        tv_file_time.text = SimpleDateFormat("YYYY/MM/dd aa hh:mm").format(file?.lastModified() ?: 0)
        if (adapterPosition > adapter.curMaxPos) {
            Log.i(TAG, "bind ${adapterPosition} ${layoutPosition}")
            itemView.startAnimation(AnimationUtils.loadAnimation(itemView.context, R.anim.item_animation_push_up))
            adapter.curMaxPos = adapterPosition
        }
    }
}