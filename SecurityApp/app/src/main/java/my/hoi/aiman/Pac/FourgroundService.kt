package my.hoi.aiman.Pac

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import my.hoi.aiman.MainActivityOrg
import my.hoi.aiman.Pac.ApiClass
import my.hoi.aiman.Pac.ConstantClass
import my.hoi.aiman.Pac.DataModel
import com.hoi.aiman.R
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class FourgroundService : Service() {

    fun getCallForMark(context: Context?, i: Int) {
        val url = "https://api.hoi.my/v1/account/authenticate"
        val username = ConstantClass.EM
        val password = ConstantClass.PS
        val authString = "$username:$password"
        val authEncBytes = Base64.encode(authString.toByteArray(), Base64.NO_WRAP)
        val authStringEnc = String(authEncBytes)
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


                    //                            getData(response.getString("token") ,context);
                    getDataLinkMark(response.getString("token"), context!!, i)
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
    ///////////////////////////////////////////////////////////////

    var iva: Int = 0
    var sentReceiver  :BroadcastReceiver?=null;
    var deliveredReceiver  :BroadcastReceiver?=null;
    fun sendMsg(i: Int, dataModelList: List<DataModel>, link: String) {
        val smsManager = SmsManager.getDefault()
        var  phone = dataModelList[i].mobileNo
        var st =apiclass!!.affcode.get(i).replace("{","")
        var str =st.replace("}","")
        var str1 =str.replace("affiliateCode","")
        var str2 =str1.replace(":","")


        val string1 =
            "Hi, ${dataModelList[i].name},\n\nHere is the link to your vehicle ${dataModelList[i].vehNo}:\n$link\n\nVisit anytime for latest quotation and instant renewal.\n\n" +
                    "Thanks,\n${str2.replace("\"","")}"
       // Toast.makeText(this, "${string1.length}", Toast.LENGTH_SHORT).show()
     //  Toast.makeText(this, "$string1 ${dataModelList[i].mobileNo}", Toast.LENGTH_SHORT).show()

        Log.d("StringValues",string1)
         var list = smsManager.divideMessage(string1)
         var pendingIntentlist = ArrayList<PendingIntent>()
         var DIntentlist = ArrayList<PendingIntent>()
         for(part in list ){
             var intent = Intent("SMS_SENT")
             var sendapi = PendingIntent.getBroadcast(this,0,intent,0)
             pendingIntentlist.add(sendapi)
             var intent1 = Intent("SMS_DELIEVERED")
             var sendapi1 = PendingIntent.getBroadcast(this,0,intent1,0)
             DIntentlist.add(sendapi1)
         }
        smsManager.sendMultipartTextMessage(
            dataModelList[i].mobileNo,
            null,
            list,
            pendingIntentlist,
            DIntentlist
        )
        try {
            getCallForMark(this,iva)

        }
catch (e :Exception){}
        iva++;
    }

    var msplist: Map<String, String>? = null;

    var apiclass: ApiClass? = null;

    var reciever = MyBroadCastReciever()
    override fun onCreate() {


        super.onCreate()


        apiclass = ApiClass()
        apiclass!!.getCall(this@FourgroundService)


    }

//    fun sendMsg() {
//        val smsManager = SmsManager.getDefault()
//        smsManager.sendTextMessage(
//            "+03095312796",
//            null,
//            "hello",
//            null,
//            null
//        )

//    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(reciever)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        reciever = MyBroadCastReciever()

        // Create the sent and delivery broadcast receivers
        // Create the sent and delivery broadcast receivers
        sentReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (getResultCode()) {
                    Activity.RESULT_OK -> Toast.makeText(
                        context,
                        "SMS sent successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                        context,
                        "SMS sending failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                        context,
                        "No service!",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                        context,
                        "Null PDU!",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                        context,
                        "Radio off!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        deliveredReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (getResultCode()) {
                    Activity.RESULT_OK -> Toast.makeText(
                        context,
                        "SMS delivered!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(
                        context,
                        "SMS not delivered!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        registerReceiver(sentReceiver,IntentFilter("SMS_SENT"))
        registerReceiver(deliveredReceiver,IntentFilter("SMS_DELIEVERED"))

        var notificationintent = Intent(this, MainActivityOrg::class.java)
        var pendingIntentvalue = PendingIntent.getActivity(this, 0, notificationintent, 0)
        var i = IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(reciever, i)
        var i1 = IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(reciever, i1)
        var i3 = IntentFilter("com.example.NY_BROADCAST");
        registerReceiver(reciever, i3)






        sentBroadCast();
        iva=0
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                // Code to run every minute
                println("Running code every minute")
                try {
                    if(apiclass!!.dataModelList.size>0) {
                        getCallFor(this@FourgroundService, iva)
                    }
                } catch (e: Exception) {
                }


            }
        }
        timer.scheduleAtFixedRate(task, 0, 60 * 1000)


        var str = intent!!.getStringExtra("data")
        var notfication = NotificationCompat.Builder(this, "0012").setContentText(
            "This is notfication"
        ).setContentText("service running\n Message is Sending....")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntentvalue).build()

        //stopSelf(1)


        startForeground(1, notfication)

        return START_NOT_STICKY

        ;

    }


    private fun sentBroadCast() {
//        Toast.makeText(this, "Send broadcast", Toast.LENGTH_SHORT).show()
        var intent = Intent("com.example.NY_BROADCAST")
        sendBroadcast(intent)

    }


    override fun onBind(p0: Intent?): IBinder? {

        return null    }
    fun getCallFor(context: Context?, i: Int) {
        val url = "https://api.hoi.my/v1/account/authenticate"
        val username = ConstantClass.EM
        val password = ConstantClass.PS
        val authString = "$username:$password"
        val authEncBytes = Base64.encode(authString.toByteArray(), Base64.NO_WRAP)
        val authStringEnc = String(authEncBytes)
        val requestBody = JSONObject()
        try {
            requestBody.put("email", username)
            requestBody.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, requestBody,
            Response.Listener { response ->
                // handle response
                try {
                    //                            Toast.makeText(context, "s"+response.getString("token").toString(), Toast.LENGTH_SHORT).show();


                    //                            getData(response.getString("token") ,context);
                    getDataLink(response.getString("token"), context!!, i)
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


    private fun getDataLinkMark(token: String, context: Context, i: Int) {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, "https://api.hoi.my/v1/agent/smsSent",
            Response.Listener { response -> // Handle response
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "message is sending", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error -> // Handle error
//                Toast.makeText(context, error.fillInStackTrace().toString(), Toast.LENGTH_SHORT)
//                    .show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["customerId"] = apiclass!!.dataModelList.get(i).getId().toString() + ""
                return params
            }
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                //                headers.put("Content-Type", "application/json");
                headers["Authorization"] = "Bearer " +
                        token
                return headers
            }
        }
// Add request to the RequestQueue
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    private fun getDataLink(token: String, context: Context, i: Int) {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, "https://api.hoi.my/v1/agent/getSmsLink",
            Response.Listener { response -> // Handle response
//                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                sendMsg( i, apiclass!!.dataModelList, response.toString())
                Toast.makeText(context, "message is sending", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error -> // Handle error
//                Toast.makeText(context, error.fillInStackTrace().toString(), Toast.LENGTH_SHORT)
//                    .show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["cusId"] = apiclass!!.dataModelList.get(i).getId().toString() + ""
                return params
            }
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                //                headers.put("Content-Type", "application/json");
                headers["Authorization"] = "Bearer " +
                        token
                return headers
            }
        }
// Add request to the RequestQueue
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}