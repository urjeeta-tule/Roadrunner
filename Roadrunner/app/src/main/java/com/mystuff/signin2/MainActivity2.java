package com.mystuff.signin2;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mystuff.signin2.MapsActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.mystuff.signin2.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity2 extends Activity
{
    Button addd,update;

    int intens, tid;
    Intent i1, i2;
    String a,ts="LOCATION EDITING LEFT";
    String aResponse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //addd = (Button) findViewById(R.id.addd);

        i2=getIntent();
        ts=i2.getStringExtra("data");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("MAP");
        tabSpec.setContent(R.id.mapTab);
        tabSpec.setIndicator("Map");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("ADD");
        tabSpec.setContent(R.id.add);
        tabSpec.setIndicator("Add");
        tabHost.addTab(tabSpec);
*/
        TextView tv2=(TextView) findViewById(R.id.textView2);
        tv2.setText(ts);
        final Button addBtn = (Button) findViewById(R.id.buttonAdd);
        /*tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabHost.getCurrentTabTag().equals("ADD")==true) {

                    i2 = getIntent();
                    if(null!=i2) {
                        TextView t = (TextView) findViewById(R.id.textView2);
                        t.setText(i2.getStringExtra("location"));
                    }

                }
            }
        });
*
        tabHost.getChildAt(2).setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }}


        );
*/


        final Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Traffic", "Construction"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() >= 33.33) {
                    seekBar.setBackgroundColor(Color.YELLOW);
                    intens = 2;
                }
                if (seekBar.getProgress() >= 66.66) {
                    seekBar.setBackgroundColor(Color.RED);
                    intens = 3;
                }
                if (seekBar.getProgress() < 33.33) {
                    seekBar.setBackgroundColor(Color.GREEN);
                    intens = 1;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdown.getSelectedItem() == "Traffic")
                    tid = 1;
                else if (dropdown.getSelectedItem() == "Construction")
                    tid = 0;

                makeGetRequest();
            }
        });


    }/*
    public void getcam(View t) {
        if (t.getId() == R.id.addd) {
            Intent i = new Intent(this, cam.class);
            startActivity(i);
        }

    }*/


    /*public void showmap(View s) {
        if (s.getId() == R.id.show) {
            i1 = new Intent(this, MapsActivity.class);
            startActivity(i1);
        }
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText(""+getIntent().getExtras().getString("location"));
    }
*/


    private void makeGetRequest() {
        // replace with your url

        Thread background = new Thread(new Runnable() {

            private final HttpClient Client = new DefaultHttpClient();
            //private String URL = "http://128.0.0.15/myip.php";

            //           String URL ;
            String AAURL = "";
            //String URL = "http://roadrush.vacau.com/checkapplogin.php?u="+un+"&p="+pwd;
            String URL= "http://roadrush.vacau.com/insertapptraffic.php?location="+ts+"&type="+Integer.toString(tid)+"&intensity="+Integer.toString(intens);
            //String URL = "http://128.0.0.15:1111/Icon3_Test/Icon3_Function.php?IconFnGrp=Matrix&IconFn=connect";
            //String URL = "http://roadrush.vacau/insertapptraffic.php?location="+ts+"&type="+tid+"&intensity="+intens;

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

                    if ((null != aResponse)) {
                        Log.d("hi","hello");
                        //ALERT MESSAGE
                        /*Toast.makeText(
                                getBaseContext(),
                                "Server Response: " + aResponse,
                                Toast.LENGTH_LONG).show();
*/

                        if(aResponse.contains("success"))
                        {
                            Toast.makeText(getApplicationContext(),"Your input has been added to the map", Toast.LENGTH_SHORT).show();
                            i1 = new Intent(MainActivity2.this, MapsActivity.class);
                            a = "";
                            a = a + Integer.toString(tid) + " " + Integer.toString(intens);
                            i1.putExtra("data", a);
                            Log.d("loginsuccess","yay");
                            startActivity(i1);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Please retry adding", Toast.LENGTH_SHORT).show();
                        Log.d("b","bye");
                        //ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Not Got Response From Server.",
                                Toast.LENGTH_SHORT).show();
                        Log.d("loginfailure","boo");
                    }

                }

            };

        });

        background.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public void onResume() {
        super.onResume();
/*
        i2 = getIntent();
        if (null != i2) {
            TextView t = (TextView) findViewById(R.id.textView2);
            String ts=i2.getStringExtra("location");
            Log.d("location",ts);
            t.setText(ts);
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}