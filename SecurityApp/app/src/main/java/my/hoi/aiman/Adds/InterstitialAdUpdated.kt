package my.hoi.aiman.Adds

import android.app.Activity
import android.content.Context
import my.hoi.aiman.Adds.OpenApp.Companion.isInterstitialShown
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


open class InterstitialAdUpdated {
    var mInterstitialAd: InterstitialAd? = null
    companion object {
        @Volatile private var instance: InterstitialAdUpdated? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdUpdated().also { instance = it }
        }
        var count = 0
    }

    fun loadInterstitialAd(context: Context) {
        mInterstitialAd = null
        context.let {
            InterstitialAd.load(it,"",
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(ad: LoadAdError) {
                        count++
                        if (count < 3) loadInterstitialAd(context)
                        isInterstitialShown = false
                    }

                    override fun onAdLoaded(ad: InterstitialAd) {
                        mInterstitialAd = ad
                    }
                })
        }
    }

    fun showInterstitialAdNew(activity: Activity) {
        activity.let {
            mInterstitialAd?.show(it)
        }
    }

    fun getInter(): InterstitialAd? {
        return mInterstitialAd
    }

    fun setListener(context: Context, afterAdWork: () -> Unit) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadInterstitialAd(context)
                isInterstitialShown = false
                afterAdWork()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError?) {
                super.onAdFailedToShowFullScreenContent(p0)
                isInterstitialShown = false
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                isInterstitialShown = true
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                super.onAdImpression()
                isInterstitialShown = true
            }
        }
    }
}
