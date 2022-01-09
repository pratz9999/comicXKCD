package com.shortcut

import android.app.Application
import com.shortcut.data.di.databaseModule
import com.shortcut.data.di.networkModule
import com.shortcut.data.di.prefModule
import com.shortcut.data.di.repositoryModule
import com.shortcut.xkcd.BuildConfig
import com.shortcut.di.utilsModule
import com.shortcut.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AppController : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AppController)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            fragmentFactory()
            modules(
                networkModule,
                prefModule,
                databaseModule,
                repositoryModule,
                viewModelModule,
                utilsModule)
        }
    }
}