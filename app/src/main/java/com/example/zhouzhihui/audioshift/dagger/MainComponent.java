package com.example.zhouzhihui.audioshift.dagger;

import com.example.zhouzhihui.audioshift.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = MainModule.class)
@Singleton
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
