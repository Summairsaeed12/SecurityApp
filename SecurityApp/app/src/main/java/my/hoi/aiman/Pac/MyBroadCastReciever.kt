  package my.hoi.aiman.Pac

 import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import my.hoi.aiman.Pac.ConnectedActivity

 class MyBroadCastReciever  : BroadcastReceiver(){
    companion object{
        var isconnected = false
    }



    override fun onReceive(context: Context?, intent: Intent?) {
       // Toast.makeText(context, "action", Toast.LENGTH_SHORT).show()





        if(Intent.ACTION_POWER_CONNECTED.equals(intent!!.action)){
         //   Toast.makeText(context , "Connected successfuy!" , Toast.LENGTH_LONG).show()
            MyBroadCastReciever.isconnected = true
           //var v =  FloatingWidgetServiceS()



//            sendMsg()

            val i = Intent(context!!.applicationContext, ConnectedActivity::class.java)
         //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
       //     context!!.startActivity(i)

        }
        if(Intent.ACTION_POWER_DISCONNECTED.equals(intent!!.action)){
//            var notificationintent = Intent(context , DisconnectActivity::class.java)
//            var pendingIntentvalue = PendingIntent.getActivity(context , 0 , notificationintent , 0)
//            MyBroadCastReciever.isconnected = false
//
//
//            val i = Intent(context!!.applicationContext, DisconnectActivity::class.java)
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            context!!.startActivity(i)

//            sendMsg()

           // Toast.makeText(context , "disonnected successfuy!" , Toast.LENGTH_LONG).show()

        }

        }

    fun sendSMSMessage() {
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage("92309531296", null, "message", null, null)
    }
}