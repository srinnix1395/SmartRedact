package com.example.smartredact.common.di.module

import androidx.appcompat.app.AppCompatActivity
import com.example.smartredact.common.di.annotation.ActivityContext
import dagger.Module
import dagger.Provides

/**
 * Created by TuHA on 6/6/2019.
 */
@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @ActivityContext
    fun provideActivityContext(): AppCompatActivity {
        return activity
    }
}