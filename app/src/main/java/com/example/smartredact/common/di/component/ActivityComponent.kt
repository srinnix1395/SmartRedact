package com.example.smartredact.common.di.component

import com.example.smartredact.common.di.annotation.ActivityScope
import com.example.smartredact.common.di.module.ActivityModule
import com.example.smartredact.view.editor.image.EditorImageFragment
import com.example.smartredact.view.editor.video.EditorVideoFragment
import com.example.smartredact.view.home.HomeFragment
import dagger.Subcomponent

/**
 * Created by TuHA on 6/6/2019.
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(editorVideoFragment: EditorVideoFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(editorImageFragment: EditorImageFragment)

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(module: ActivityModule): Builder

        fun build(): ActivityComponent
    }
}