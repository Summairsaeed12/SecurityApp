package my.hoi.aiman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hoi.aiman.R
import my.hoi.aiman.Adds.loadNativeAd
import my.hoi.aiman.Pac.LoginActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.android.synthetic.main.native_ad_splash_skeleton.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        loadNativeAd(shimmerEffect,frame!!,
            R.layout.custom_ad_splash,getString(R.string.admob_native_ad_for_main) , false)


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.SEND_SMS), 1)

            }
        }

        start.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}