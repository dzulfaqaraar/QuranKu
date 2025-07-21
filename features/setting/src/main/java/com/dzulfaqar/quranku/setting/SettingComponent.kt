package com.dzulfaqar.quranku.setting

import android.content.Context
import com.dzulfaqar.quranku.di.SettingModuleDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [SettingModuleDependencies::class])
interface SettingComponent {

    fun inject(activity: SettingActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(settingModuleDependencies: SettingModuleDependencies): Builder
        fun build(): SettingComponent
    }
}