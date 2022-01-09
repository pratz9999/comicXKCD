package com.shortcut.data.di

import android.content.Context
import android.content.SharedPreferences
import com.shortcut.data.local.pref.PrefDataSourceImp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SHARED_PREF = "sharedPref"

//Adding qualifier to be ready to add Secure Shared Pref if required
val prefModule = module {
    single(qualifier = named(SHARED_PREF)) { provideSharedPref(androidApplication()) }
}

fun provideSharedPref(context: Context): SharedPreferences {
    return context.getSharedPreferences(PrefDataSourceImp.PREF_NAME, Context.MODE_PRIVATE)
}
