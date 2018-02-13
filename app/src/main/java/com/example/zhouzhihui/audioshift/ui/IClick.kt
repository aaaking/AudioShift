package com.example.zhouzhihui.audioshift.ui

/**
 * Created by 周智慧 on 13/02/2018.
 */
interface IClick {
    fun onClick(position: Int, payload: Any?) {}
    fun onLongPress(position: Int, payload: Any?): Boolean = true
    fun onDelete(position: Int, payload: Any?) {}
    fun onOther(position: Int, payload: Any?) {}
}