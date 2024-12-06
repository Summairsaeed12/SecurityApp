package my.hoi.aiman.Pac;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClass {

    private  List<DataModel> dataModelList;
    private  List<String> affcode;

    public List<String> getAffcode() {
        return affcode;
    }

    public void setAffcode(List<String> affcode) {
        this.affcode = affcode;
    }

    private String Link="" ;

    public void setDataModelList(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    public List<DataModel> getDataModelList() {
        return dataModelList;
    }

   public void getCall(Context context ){
        String url = "https://api.hoi.my/v1/account/authenticate";
        String username = ConstantClass.Companion.getEM();
        String password = ConstantClass.Companion.getPS();
        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
        String authStringEnc = new String(authEncBytes);


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", ConstantClass.Companion.getEM());
            requestBody.put("password", ConstantClass.Companion.getPS());
        } catch (JSONException e) {
            e.printStackTrace();
        }







        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response

                        try {
//                            Toast.makeText(context, "s"+response.getString("token").toString(), Toast.LENGTH_SHORT).show();


                            getData(response.getString("token") ,context);
//                            getDataLink(response.getString("token"),context,"808");

                        } catch (JSONException e) {
                            e.printStackTrace();
 //                           Toast.makeText(context, "Error"+e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
   //                     Toast.makeText(context, "Error1 "+error.toString(), Toast.LENGTH_SHORT).show();


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

    public void getCallFor(Context context,int i  ){




        String url = "https://api.hoi.my/v1/account/authenticate";
        String username = ConstantClass.Companion.getEM();
        String password = ConstantClass.Companion.getPS();
        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
        String authStringEnc = new String(authEncBytes);


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", ConstantClass.Companion.getEM());
            requestBody.put("password", ConstantClass.Companion.getPS());
        } catch (JSONException e) {
            e.printStackTrace();
        }







        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response

                        try {
//                            Toast.makeText(context, "s"+response.getString("token").toString(), Toast.LENGTH_SHORT).show();


//                            getData(response.getString("token") ,context);
                            getDataLink(response.getString("token"),context,i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //                           Toast.makeText(context, "Error"+e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle error
                        //                     Toast.makeText(context, "Error1 "+error.toString(), Toast.LENGTH_SHORT).show();


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

    public String getLink() {
        return Link;
    }

    private void getData(String token , Context context ) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://api.hoi.my/v1/agent/getSmsList", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // handle response


                        try {


                            Gson gson = new Gson();
                            Type type = new TypeToken<List<DataModel>>() {
                            }.getType();
                            List<DataModel> dataModelList1 = gson.fromJson(response.toString(), type);
                            dataModelList = dataModelList1;
                            ConstantClass.Companion.setDataListvalue(dataModelList);

                            affcode = new ArrayList<>();
                            affcode.clear();
                            for(int i =  0 ; i<response.length();i++){
                           //     Toast.makeText(context, dataModelList1.get(i).getMobileNo(), Toast.LENGTH_SHORT).show();
                                String jsonObject= response.getJSONObject(i).getString("agent");
                                affcode.add(jsonObject)
                            ;}
                        }catch (Exception e){
//                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // handle error
//                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " +
                        token)
                ; return headers;
            }


        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);



     }
    private void getDataLink(String token , Context context,int i   )  {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.hoi.my/v1/agent/getSmsLink",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
//                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                        Link = response.toString();
                       ConstantClass.Companion.sendMsg(i,dataModelList,response.toString(), context);

//                        Toast.makeText(context, "message is sending", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
//                        Toast.makeText(context, error.fillInStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cusId", dataModelList.get(i).getId()+"");
                return params;
            }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");

                    headers.put("Authorization", "Bearer " +
                            token);
                return headers;
            }
        };

// Add request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }




    void  sendMsg( int i  , List<DataModel> dataModelList , String link ) {

        SmsManager smsManager = SmsManager.getDefault();
        String string = "Hi  "+            dataModelList.get(i).getName()+" Here is link of your vechiles" + dataModelList.get(i).getVehNo()+"Link " + link+ "   Visit anytime for latest quotation and instant renewal \n\n Thanks ";
        smsManager.sendTextMessage(
                dataModelList.get(i).getMobileNo(),
                null,
                string,
                null,
                null
        )
   ; }
}
