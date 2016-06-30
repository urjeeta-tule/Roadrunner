package com.mystuff.signin2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zzc;
import com.google.android.gms.common.api.zzl;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity {

    GoogleMap googleMap;
    LocationListener l;
    ArrayList <LatLng> loc= new ArrayList<>();
    PolylineOptions pol=new PolylineOptions();
    double latitude,longitude;
    String check,lo;
    Intent i,i2;
    //EditText et=(EditText) findViewById(R.id.et01);
    int intens;
    //=Integer.parseInt(et.getText().toString());
    int tid;
    public void updatemap() {
        l = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                TextView textView = (TextView) findViewById(R.id.tv01);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                LatLng x1=new LatLng(latitude - 0.000001, longitude - 0.000001);
                LatLng y1=new LatLng(latitude + 0.000001, longitude + 0.000001);
                //loc.add(latLng);


                if (googleMap != null) {
                    if (tid == 0) {
                        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.cnstr)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        lo = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                        textView.setText("Location is: " + lo + " type=" + check.split(" ")[0] + " intensity=" + check.split(" ")[1]);

                    }else {
                        googleMap.addMarker(new MarkerOptions().position(latLng));
                        if (intens == 3)
                            pol.color(Color.RED).width(5).visible(true);
                        else if (intens == 2)
                            pol.color(Color.YELLOW).width(5).visible(true);
                        else if (intens == 1)
                            pol.color(Color.GREEN).width(5).visible(true);
                        pol.add(x1,y1);
                        //pol.add(latLng);
                        googleMap.addPolyline(pol);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        lo = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                        textView.setText("Location is: " + lo + " type=" + check.split(" ")[0] + " intensity=" + check.split(" ")[1]);
                        Log.d("status", "updated");
                        Log.d("loc",lo);

                    }

                /*googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                lo=Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                textView.setText("Location is: " + lo + " type=" + check.split(" ")[0] + " intensity=" + check.split(" ")[1]);*/


                }
            }


            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
            }


        };
        lmanage();
    }

    public void showmap(){
        l=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                LatLng latLng=new LatLng(latitude,longitude);
                lo = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());

                googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                Log.d("status","shown");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };lmanage();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }


        setContentView(R.layout.activity_maps);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);

        /*if(tid==1)
        {
            //pol.addAll(loc);
            googleMap.addPolyline(pol);
        }
*/


        //i2=new Intent(this,MainActivity2.class);

        i = getIntent();
        check = i.getStringExtra("data");
        if (check != null) {

            String temp[] = check.split(" ");
            tid = Integer.parseInt(temp[0]);
            intens = Integer.parseInt(temp[1]);

            updatemap();
            //i2.putExtra("location",lo);
        } else
            showmap();

        }

    public void lmanage() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
//             Toast.makeText(getApplicationContext(),"Location is:"+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();


            l.onLocationChanged(location);

        }


        locationManager.requestLocationUpdates(bestProvider, 20000, 0, l);

    }


    public void showdetails(View t) {
        if (t.getId() == R.id.goahead) {
            Intent i1 = new Intent(this, MainActivity2.class);
            startActivity(i1);
            Log.d("loc", lo);
            makeGetRequest();
        }
    }

    private void makeGetRequest() {
        // replace with your url

        Thread background = new Thread(new Runnable() {

            private final HttpClient Client = new DefaultHttpClient();
            //private String URL = "http://128.0.0.15/myip.php";

            //           String URL ;
            String AAURL = "";

            //String URL = "http://roadrush.vacau.com/checkapplogin.php?u="+un+"&p="+pwd;
            String URL= "http://roadrush.vacau.com/insertlocation.php?location="+lo;

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

                    String aResponse = msg.getData().getString("message").trim();

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
                            Toast.makeText(getApplicationContext(),"Enter details to be added to the map", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(MapsActivity.this, MainActivity2.class);
                            i1.putExtra("data", lo);
                            Log.d("loginsuccess","yay");
                            startActivity(i1);

                        }
                    } else {
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


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this,0).show();
            return false;
        }
    }


}