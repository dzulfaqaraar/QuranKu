package com.dzulfaqar.quranku.murattal

import android.content.Context
import com.dzulfaqar.quranku.di.RecitationModuleDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [RecitationModuleDependencies::class])
interface RecitationComponent {

    fun inject(activity: RecitationActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(recitationModuleDependencies: RecitationModuleDependencies): Builder
        fun build(): RecitationComponent
    }
}