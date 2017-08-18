package com.kokutouda.dnote.ui

import android.app.Application
import android.content.Context
import com.kokutouda.dnote.extension.DelegateExt

/**
 * Created by apple on 2017/7/5.
 */

class App : Application() {

    companion object {
        var instance :App by DelegateExt.notNullSingleValueVar()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}