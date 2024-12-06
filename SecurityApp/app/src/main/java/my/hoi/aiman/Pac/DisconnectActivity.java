package my.hoi.aiman.Pac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import my.hoi.aiman.FloatingWidgetServiceS;
import com.hoi.aiman.R;

public class DisconnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnect);
        sendMsg();
    }
    void
    sendMsg()
    {
        String MyPREFERENCES = "MyPrefs";
        SharedPreferences sharedpreferences  =   getSharedPreferences(MyPREFERENCES,
                MODE_PRIVATE);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sharedpreferences.getString("number",""), null, sharedpreferences.getString("message2",""), null, null);

        Toast.makeText(this, "Message is send to "+  FloatingWidgetServiceS.CSEt.getStr().toString() , Toast.LENGTH_SHORT).show();
    }
}