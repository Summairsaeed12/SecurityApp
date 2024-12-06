package my.hoi.aiman.Pac;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import my.hoi.aiman.MainActivityOrg;
import com.hoi.aiman.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity  extends AppCompatActivity {
    EditText Email , Password ;
    Button Login , Signup  ;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHandler = new DBHandler(this);
        Email = findViewById(R.id.editTextTextEmailAddress3);
        Password = findViewById(R.id.editTextTextEmailAddress4);

        Signup = findViewById(R.id.button3);
        Login = findViewById(R.id.button2);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Email.getText().toString().isEmpty())
                {
                    Email.setError("Field is empty");
                    return;
                }
                if(Password.getText().toString().isEmpty())
                {
                    Password.setError("Field is empty");
                    return;
                }
                getCall(LoginActivity.this,Email.getText().toString(),Password.getText().toString());
            }
        });

        Signup.setVisibility(View.GONE);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Email.getText().toString().isEmpty())
                {
                    Email.setError("Field is empty");
                    return;
                }
                if(Password.getText().toString().isEmpty())
                {
                    Password.setError("Field is empty");
                    return;
                }
                dbHandler.addNewCourse(Email.getText().toString(),Password.getText().toString());
                Toast.makeText(LoginActivity.this, "Course has been added.", Toast.LENGTH_SHORT).show();

            }
        });



    }
    public void getCall(Context context ,String em , String p){




        String url = "https://api.hoi.my/v1/account/authenticate";
        String username = em;
        String password = p;
        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
        String authStringEnc = new String(authEncBytes);


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", em);
            requestBody.put("password", p);
        } catch (JSONException e) {
            e.printStackTrace();
        }







        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        ConstantClass.Companion.setEM(Email.getText().toString());
                        ConstantClass.Companion.setPS(Password.getText().toString());
//
//                        try {
//                            Toast.makeText(context, "s"+response.getString("token").toString(), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "s"+, Toast.LENGTH_SHORT).show();
//
//                        }


                        startActivity(new Intent(LoginActivity.this, MainActivityOrg.class));
//                            getDataLink(response.getString("token"),context,"808");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
                                             Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

}
