package com.dzulfaqar.quranku.bookmark

import android.content.Context
import com.dzulfaqar.quranku.di.BookmarkModuleDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [BookmarkModuleDependencies::class])
interface BookmarkComponent {

    fun inject(activity: BookmarkActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(bookmarkModuleDependencies: BookmarkModuleDependencies): Builder
        fun build(): BookmarkComponent
    }
}