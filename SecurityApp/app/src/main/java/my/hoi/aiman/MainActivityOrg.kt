package my.hoi.aiman

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hoi.aiman.BuildConfig
import my.hoi.aiman.Adds.loadNativeAd
import my.hoi.aiman.Pac.ConstantClass
import com.hoi.aiman.R
import my.hoi.aiman.Pac.DataModel
import my.hoi.aiman.Pac.FSaver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.native_ad_splash_skeleton.*
import my.hoi.aiman.Pac.FourgroundService
import org.json.JSONException
import org.json.JSONObject


class MainActivityOrg : AppCompatActivity() {

    var fSaver : FSaver?=null
    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Screen Security App")
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Screen Security App")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage = "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    fun rateApp(context: Context) {
        val uri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "mpossible to find an application for the market\"", Toast.LENGTH_SHORT).show()

        }
    }


var strinlist :  ArrayList<String>?=null

    fun getCall(context: Context?,listView: ListView) {
        val url = "https://api.hoi.my/v1/account/authenticate"
//        val username = "prof.shafiq@gmail.com"
//        val password = "123456"
//        val authString = "$username:$password"
//        val authEncBytes = Base64.encode(authString.toByteArray(), Base64.NO_WRAP)
//        val authStringEnc = String(authEncBytes)
        val requestBody = JSONObject()
        try {
            requestBody.put("email", ConstantClass.EM)
            requestBody.put("password", ConstantClass.PS)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, requestBody,
            Response.Listener { response ->
                // handle response
                try {
                    //                            Toast.makeText(context, "s"+response.getString("token").toString(), Toast.LENGTH_SHORT).show();
                        getData(response.getString("token"), this,listView)

                    //                            getDataLink(response.getString("token"),context,"808");
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //                           Toast.makeText(context, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            },
            Response.ErrorListener {
                // handle error
                //                     Toast.makeText(context, "Error1 "+error.toString(), Toast.LENGTH_SHORT).show();
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
    private fun getData(token: String, context: Context,listView: ListView) {
        val requestBody = JSONObject()
        try {
            requestBody.put("token", token)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, "https://api.hoi.my/v1/agent/getSmsList", null,
            Response.Listener { response ->
                // handle response
                try {strinlist = ArrayList<String>()
                    strinlist!!.clear()
                    val gson = Gson()
                    val type = object : TypeToken<List<DataModel?>?>() {}.type
                    val dataModelList1 = gson.fromJson<List<DataModel>>(response.toString(), type)
                    for (i in 0..dataModelList1.size-1) {
                    strinlist!!.add( "message will be send to " + dataModelList1.get(i).mobileNo)
                    }
//                    Toast.makeText(this, dataModelList1.get(0).affcode.getJSONObject("agent").toString()+"", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, strinlist!!.size.toString(), Toast.LENGTH_SHORT).show()
                  var   arrayAdapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1, strinlist!!
                    )
                    rec.adapter = arrayAdapter

                    // Toast.makeText(MainActivity.this, "s"+response.getString("name").toString(), Toast.LENGTH_SHORT).show();
                } catch (e: java.lang.Exception) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { e -> // handle error
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = java.util.HashMap()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer " +
                        token
                return headers
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private var installStateUpdatedListener: InstallStateUpdatedListener? = null
    private val FLEXIBLE_APP_UPDATE_REQ_CODE = 123
    private var appUpdateManager: AppUpdateManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            getCall(this,rec)
            startFloatingWidgetService()

        }catch (e : Exception){}
//        val MyPREFERENCES = "MyPrefs"
//        var sharedpreferences: SharedPreferences =   getSharedPreferences(MyPREFERENCES,
//            MODE_PRIVATE)
//         message2.setText(sharedpreferences.getString("message2",""))
//         message.setText(sharedpreferences.getString("message",""))
//         number.setText(sharedpreferences.getString("number",""))

//        loadNativeAd(shimmerEffect,frame!!,
//            R.layout.main_layout_ads,"ca-app-pub-9548763434048084/1830491451", true)
//


        try {


//            share.setOnClickListener {
//                shareApp(this)
//            }
//            ratingsbtn.setOnClickListener {
//                rateApp(this)
//            }

//            appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());


//            installStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
//                if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                    popupSnackBarForCompleteUpdate()
//                } else if (state.installStatus() == InstallStatus.INSTALLED) {
//                    removeInstallStateUpdateListener()
//                } else {
//                    Toast.makeText(
//                        applicationContext,
//                        "InstallStateUpdatedListener: state: " + state.installStatus(),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }



            out.setOnClickListener {
                finish()
            }
            cardcircle.visibility = View.GONE

            cardcircle.setOnClickListener {

                val MyPREFERENCES = "MyPrefs"

                try {
//                    Toast.makeText(this, number.text.toString(), Toast.LENGTH_SHORT).show()
                    var sharedpreferences: SharedPreferences =   getSharedPreferences(MyPREFERENCES,
                        MODE_PRIVATE)
                    val editor = sharedpreferences.edit()
                    editor.putString("number", number.text.toString())
                    editor.putString( "message" ,message.text.toString())
                    editor.putString( "message2" ,message2.text.toString())
                    editor.apply()
                    editor.commit()
                    Toast.makeText(this, "Configured succeed", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, sharedpreferences.getString("number","0").toString() + sharedpreferences.getString("message","0") , Toast.LENGTH_SHORT).show()

                }catch (e :  Exception)
                {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }

//                createFloatingWidget()
            }

        } catch (e: Exception) {

        }

//
//        sharedPreferences = this.getSharedPreferences("Filesave", Context.MODE_PRIVATE)
//        var shape: RelativeLayout = findViewById(R.id.cardcircle);
//        var c = sharedPreferences!!.getString("active", "false")
//        if (c.equals("true")) {
//            shape.setBackgroundResource(R.drawable.circle_shape_green)
//        } else {
//            shape.setBackgroundResource(R.drawable.shape)
//        }
    }

    private fun removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager!!.unregisterListener(installStateUpdatedListener)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1222) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) //If permission granted start floating widget service
                startFloatingWidgetService() else  //Permission is not available then display toast
                Toast.makeText(
                    this,
                    resources.getString(R.string.draw_other_app_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    getApplicationContext(),
                    "Update canceled by user! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    getApplicationContext(),
                    "Update success! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Update Failed! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
                checkUpdate();
            }
        }

    }

    private fun startFloatingWidgetService() {

//        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
//        editor.putString("active", "true")
//        editor.apply()
//        editor.commit()
//        editor.apply()







        FloatingWidgetServiceS.CSEt.str  =""
        var i = Intent(this, FloatingWidgetServiceS::class.java)
        startService(Intent(this, FloatingWidgetServiceS::class.java))
        var iv = Intent(this , FourgroundService::class.java);
        iv.putExtra("data" , "Bacground")
        startService(iv)
    }

    /*  start floating widget service  */
//    fun createFloatingWidget() {
//
//        var c = false;
//        //Check if the application has draw over other apps permission or not?
//        //This permission is by default available for API<23. But for API > 23
//        //you have to ask for the permission in runtime.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//            //If the draw over permission is not available open the settings screen
//            //to grant the permission.
//            val intent = Intent(
//                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:$packageName")
//            )
//            startActivityForResult(intent, 1222)
//        } else  //If permission is granted start floating widget service
//
//            c = true
////        if (c == true) {
////            shape.setBackgroundResource(R.drawable.circle_shape_green)
////        } else {
////            shape.setBackgroundResource(R.drawable.shape)
////        }
//
//     //   startFloatingWidgetService()
//    }

    override fun onDestroy() {
        super.onDestroy()

        try{

            var sharedPreferences = this.getSharedPreferences("Filesave", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString("active", "false")
            editor.apply()
            editor.commit()
            editor.apply()
        }catch (e :Exception){

        }

    }

    private fun checkUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.installStatus() === InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                FLEXIBLE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            findViewById<View>(R.id.cardcircle).rootView,
            "New app is ready!",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Install") { view: View? ->
                if (appUpdateManager != null) {
                    appUpdateManager!!.completeUpdate()
                }
            }
            .setActionTextColor(resources.getColor(R.color.colorAccent))
            .show()
    }

}