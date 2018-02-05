package com.example.zhouzhihui.audioshift.dagger;

import android.content.Context;

import com.example.zhouzhihui.audioshift.AudioApp;
import com.example.zhouzhihui.audioshift.play.Player;
import com.example.zhouzhihui.audioshift.record.Recorder;
import com.example.zhouzhihui.audioshift.ui.AudioRecorderPlayer;
import com.example.zhouzhihui.audioshift.ui.MediaToolsProvider;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private static final int DURATION_IN_MILLIS = 5 * 1000;
    private static final String AUDIO_FILENAME = "audio_shift";

    private final AudioApp application;

    public MainModule(AudioApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return application;
    }

    @Provides
    @Singleton
    MediaToolsProvider providesMediaToolsProvider() {
        return new MediaToolsProvider();
    }

    @Provides
    int providesDurationInMillis() {
        return DURATION_IN_MILLIS;
    }

    @Provides
    File providesAudioFile(Context context) {
        return new File(context.getFilesDir(), AUDIO_FILENAME);
    }

    @Provides
    @Singleton
    AudioRecorderPlayer providesAudioRecorderPlayer(MediaToolsProvider mediaToolsProvider, File audioFile) {
        return new AudioRecorderPlayer(mediaToolsProvider, audioFile);

    }

    @Provides
    Recorder providesRecorder(AudioRecorderPlayer audioRecorderPlayer) {
        return audioRecorderPlayer;
    }

    @Provides
    Player providesPlayer(AudioRecorderPlayer audioRecorderPlayer) {
        return audioRecorderPlayer;
    }
}
