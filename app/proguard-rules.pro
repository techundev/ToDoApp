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


-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <methods>;
}
-keep,includedescriptorclasses class com.techun.dev.todoapp.**$$serializer { *; }
-keepclassmembers class com.techun.dev.todoapp.** {
    *** Companion;
}
-keepclasseswithmembers class com.techun.dev.todoapp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-dontwarn org.koin.**
-keep class org.koin.core.** { *; }

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

-dontwarn javax.annotation.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.Nonnull
-dontwarn javax.annotation.concurrent.**