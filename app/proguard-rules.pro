# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Hilt ProGuard rules
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
#-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelCreator { *; }
#-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelMap$KeySet { *; }

# Core Module Classes
-keep class com.dzulfaqar.quranku.core.** { *; }
-keep class com.dzulfaqar.quranku.model.** { *; }

# Dynamic Feature Modules
-keep class com.dzulfaqar.quranku.bookmark.** { *; }
-keep class com.dzulfaqar.quranku.murattal.** { *; }
-keep class com.dzulfaqar.quranku.setting.** { *; }

# Keep EntryPoint interfaces for dynamic features
-keep interface * extends dagger.hilt.EntryPoint
-keep class * implements dagger.hilt.EntryPoint

# Hilt Generated Components
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.modules.ApplicationContextModule { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Retrofit and Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.dzulfaqar.quranku.core.data.source.remote.response.** { <fields>; }
-keep class com.dzulfaqar.quranku.model.** { <fields>; }