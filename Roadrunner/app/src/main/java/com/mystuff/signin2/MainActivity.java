package com.mystuff.signin2;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.content.ComponentName;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    Button btnSimple;
    String aResponse;
    String un,pwd;
    public static final String USER_NAME = "USERNAME";
    String username;
    String password;
    EditText editTextUserName;
    EditText editTextPassword;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSimple = (Button)findViewById(R.id.btnSimple);
        editTextUserName = (EditText) findViewById(R.id.uname);
        editTextPassword = (EditText) findViewById(R.id.pwd);
        /*btnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.equals("") || pass.equals(""))
                {,
                    Toast.makeText(getApplicationContext(), "Blank Field..Please Enter", Toast.LENGTH_LONG).show();
                }
                else
                {
            }
        }});*/

    }
    //
    private boolean makeGetRequest() {
        // replace with your url

        Thread background = new Thread(new Runnable() {

            private final HttpClient Client = new DefaultHttpClient();
            //private String URL = "http://128.0.0.15/myip.php";

            //           String URL ;
            String AAURL = "";
            //String URL = "http://128.0.0.15:1111/Icon3_Test/Icon3_Function.php?IconFnGrp=Matrix&IconFn=connect";
            //String URL = "http://10.0.2.2/ESDL/checkapplogin.php?u="+un+"&p="+pwd;
            String URL = "http://roadrush.vacau.com/checkapplogin.php?u="+un+"&p="+pwd;
            //String URL = "http://192.168.0.4:8080/ESDL/checkapplogin.php?u="+un+"&p="+pwd;
//31.170.160.107
            // After call for background.start this run method call
            public void run() {
                try {

                    String SetServerString = "";
                    HttpGet httpget = new HttpGet(URL);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    SetServerString = Client.execute(httpget, responseHandler);
                    threadMsg(SetServerString);

                } catch (Throwable t) {
                    // just end the background thread
                    Log.i("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {
                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                    Log.d("in","in");

                }
            }

            // Define the Handler that receives messages from the
            // thread and update the
            // progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    aResponse = msg.getData().getString("message").trim();

                    if ((aResponse!=null)) {
                        Log.d("hi", "hello");
                        //ALERT MESSAGE
                        /*Toast.makeText(
                                getBaseContext(),
                                "Server Response: " + aResponse,
                                Toast.LENGTH_LONG).show();
                                System.out.println(aResponse);
                        */if(aResponse.contains("success"))
                        {
                            Log.d("login success","<3");
                            Intent i=new Intent(MainActivity.this,MapsActivity.class);
                            Toast.makeText(
                                    getBaseContext(),
                                    "Logged in successfully",
                                    Toast.LENGTH_LONG).show();
                            System.out.println(aResponse);

                            //i.putExtra("username",un);
                            startActivity(i);
                        }
                        else if(aResponse.contains("failure"))
                        {
                            Log.d("login failure","<3");
                            Toast.makeText(
                                    getBaseContext(),
                                    "Invalid username or password",
                                    Toast.LENGTH_LONG).show();

                        }
                        else
                            Log.d("aresponse",aResponse);

                    } else {
                        Log.d("b", "bye");
                        //ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Not Got Response From Server.",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            };

        });

        background.start();

        return true;
    }


    public void getme(View t)
    {
        if (t.getId() == R.id.btnSimple){
            un=editTextUserName.getText().toString();
            pwd=editTextPassword.getText().toString();
//            login(un,pwd);

            makeGetRequest();
            Log.d("before","if");
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /*public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSimple :
                startActivity(new Intent(this, MainActivity2.class));
                break;
        }
    }*/



}