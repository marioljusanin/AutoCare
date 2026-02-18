package ba.sum.fsre.autocare

import android.app.Application

class AutoCareApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }

}