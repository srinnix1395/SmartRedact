package com.example.smartredact.common.di.component

import com.example.smartredact.common.di.module.ApplicationModule
import com.example.smartredact.common.di.module.RepositoryModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by TuHA on 6/6/2019.
 */
@Singleton
@Component(modules = [
    ApplicationModule::class,
    RepositoryModule::class]
)
interface AppComponent {

    fun activityComponentBuilder(): ActivityComponent.Builder
}