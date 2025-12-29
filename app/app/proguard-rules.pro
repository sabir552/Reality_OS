# app/proguard-rules.pro
-dontwarn kotlinx.coroutines.**
-dontwarn androidx.compose.**
-keep class androidx.room.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static volatile ** INSTANCE;
}
-keep class com.android.billingclient.** { *; }
