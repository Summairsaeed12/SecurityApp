package my.hoi.aiman.Pac

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast

class ConstantClass {
companion object{
    var dataListvalue : List<DataModel>?=null
    fun SetList(datalist : List<DataModel>){
        dataListvalue = datalist;
    }
    var EM  = ""
    var PS  = ""
    fun sendMsg(i: Int, dataModelList: List<DataModel>, link: String, context: Context) {
        val smsManager = SmsManager.getDefault()
        val string =
            """Hi  ${dataModelList[i].name} Here is link of your vechiles${dataModelList[i].vehNo}Link $link   Visit anytime for latest quotation and instant renewal 

 Thanks """
        Toast.makeText(context, "$string", Toast.LENGTH_SHORT).show()
        smsManager.sendTextMessage(
            dataModelList[i].mobileNo,
            null,
            string,
            null,
            null
        )
    }
}
}