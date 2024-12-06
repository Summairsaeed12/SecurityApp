package my.hoi.aiman.Adds

import android.app.Application
import com.google.android.gms.ads.MobileAds


class ApplicationClass : Application() {
    var appOpenAd: OpenApp? = null
    override fun onCreate() {
        super.onCreate()
        appOpenAd = OpenApp(this)
        InterstitialAdUpdated.getInstance().loadInterstitialAd(this)
//        startKoin {
//            androidContext(this@ApplicationClass)
//            modules(listOf(allModules, viewModelModule))
//        }
        MobileAds.initialize(this) {}

    }

    override fun onTerminate() {
        appOpenAd = null
        super.onTerminate()
    }
}