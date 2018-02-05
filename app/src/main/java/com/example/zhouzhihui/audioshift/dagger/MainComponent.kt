package com.example.zhouzhihui.audioshift.dagger

import com.example.zhouzhihui.audioshift.MainActivity

import javax.inject.Singleton

import dagger.Component

@Component(modules = arrayOf(MainModule::class))
@Singleton
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}
