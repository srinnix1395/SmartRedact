package com.example.smartredact.common.di.module

import android.app.Application
import android.content.Context
import com.example.smartredact.common.di.annotation.ApplicationContext
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
}
