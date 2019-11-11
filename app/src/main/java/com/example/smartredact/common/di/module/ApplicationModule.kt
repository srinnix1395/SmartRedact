package com.example.smartredact.common.di.module

import android.app.Application
import android.content.Context
import com.example.smartredact.common.di.annotation.ApplicationContext
import com.example.smartredact.common.facerecoginition.ObjectDetectionUtils
import com.example.smartredact.common.utils.VideoUtils
import dagger.Module
import dagger.Provides

/**
 * Created by TuHA on 6/6/2019.
 */
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    fun provideVideoUtils(): VideoUtils {
        return VideoUtils(application)
    }

    @Provides
    fun provideObjectDetectionUtils(): ObjectDetectionUtils {
        return ObjectDetectionUtils(application)
    }
}
