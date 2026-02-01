package com.pktech.newapp

import android.app.Application
import com.pktech.newapp.data.local.AppDatabase

class App : Application() {

    companion object {
        lateinit var db: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
    }
}