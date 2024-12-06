package my.hoi.aiman.Adds

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.facebook.shimmer.ShimmerFrameLayout


private var counter = 0
private var counterList = 0


fun Activity.showAdWithCounter(afterAdWork: () -> Unit) {

    if (counter == 1) {
        counter = 0
        if (isInterstitialLoaded()) {
            InterListener(afterAdWork)
            showInterstitial()
        } else {
            afterAdWork()
        }
    } else {
        counter++
        afterAdWork()
    }

}

fun Activity.showAdAndGo(afterAdWork: () -> Unit) {

    if (isInterstitialLoaded()) {
        InterListener(afterAdWork)
        showInterstitial()
    } else {
        afterAdWork()
    }

}

fun Activity.showInterstitial() {
    InterstitialAdUpdated.getInstance().showInterstitialAdNew(this)
}

fun Activity.isInterstitialLoaded(): Boolean {
    return InterstitialAdUpdated.getInstance().getInter() != null
}

fun Activity.InterListener(afterAdWork: () -> Unit) {
    InterstitialAdUpdated.getInstance().setListener(this, afterAdWork)
}

//For Fragment



fun Fragment.showAdWithCounter(afterAdWork: () -> Unit) {

    if (counterList == 1) {
        counterList = 0
        if (isInterstitialLoaded()) {
            InterListener(afterAdWork)
            showInterstitial()
        } else {
            afterAdWork()
        }
    } else {
        counterList++
        afterAdWork()
    }

}

fun Fragment.showAdAndGo(afterAdWork: () -> Unit) {

    if (isInterstitialLoaded()) {
        InterListener(afterAdWork)
        showInterstitial()
    } else {
        afterAdWork()
    }

}

fun Fragment.showInterstitial() {
    InterstitialAdUpdated.getInstance().showInterstitialAdNew(requireActivity())
}

fun Fragment.isInterstitialLoaded(): Boolean {
    return InterstitialAdUpdated.getInstance().getInter() != null
}

fun Fragment.InterListener(afterAdWork: () -> Unit) {
    InterstitialAdUpdated.getInstance().setListener(requireActivity(), afterAdWork)
}
fun Context.loadNativeAd(shimmerViewContainer: ShimmerFrameLayout? = null,
                         frameLayout: FrameLayout,
                         layoutId: Int,
                         sdIs: String? ,
boolean: Boolean) {
    NativeAdsHelper(this).setNativeAd(shimmerViewContainer, frameLayout, layoutId, sdIs , boolean)
}