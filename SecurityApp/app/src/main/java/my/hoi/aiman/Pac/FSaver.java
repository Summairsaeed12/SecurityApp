package my.hoi.aiman.Pac;

import android.content.Context;
import android.content.SharedPreferences;

public class FSaver {
private Context context;
    SharedPreferences sharedpreferences ;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public  void Write(String number , String msg, Context context1  )
    {
        context=  context1;
         sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(number, "n");
        editor.putString(msg, "msg");
        editor.commit();
        editor.apply();
    }

   public String getNumber()
        {
            return sharedpreferences.getString("n","");
        }

    public String getMSg(){
        {
            return sharedpreferences.getString("msg","");
        }


}
}
