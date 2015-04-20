package com.example.chuanzhu.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jsonparser.JSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {
    DecimalFormat df=new DecimalFormat("#.###");
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final double TEMPLE_LAT=39.982094,
                                TEMPLE_LONG=-75.154679;
    private static final float DEFAULTZOOM=15;
    Marker marker;
    Marker mylocation_marker;
    String URL_connect="http://mikekorostelev.com/~bits/Xu/login/all_report.php";
    private static String json = "";
    private ProgressDialog pDialog;
    private GoogleMap.OnMyLocationChangeListener myLocationChangelistener= new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            //mylocation_marker = mMap.addMarker(new MarkerOptions().position(loc));
            setMyLocationMarker(loc.latitude,loc.longitude);

            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };
    private GoogleMap.OnMyLocationButtonClickListener myLocationButton= new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() {
            Location location=mMap.getMyLocation();
            if(location!=null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                setMyLocationMarker(loc.latitude, loc.longitude);
            }
            else{
                Toast.makeText(getApplicationContext(), "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setLogo(R.mipmap.ic_launcher1);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_maps);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        if(initMap()){
            Toast.makeText(this,"ready to map!",Toast.LENGTH_SHORT).show();
           /* try {
                setcrime(1);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            new asynclogin().execute();
            gotoLocation(TEMPLE_LAT,TEMPLE_LONG,DEFAULTZOOM);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            //mMap.setOnMyLocationChangeListener(myLocationChangelistener);
            mMap.setOnMyLocationButtonClickListener(myLocationButton);
            //Location mylocation = mMap.getMyLocation();

            //Log.e("My location", "Latittude "+mylocation.getLatitude() );
            //setMyLocationMarker(mylocation.getLatitude(),mylocation.getLongitude());
        }
        else{
            Toast.makeText(this,"map is not affable!",Toast.LENGTH_SHORT).show();
        }
        setUpMapIfNeeded();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private boolean initMap(){
        if(mMap==null) {
            SupportMapFragment mapFrag=
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mMap=mapFrag.getMap();
        }
        if(mMap!=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v= getLayoutInflater().inflate(R.layout.infor_window, null);
                    TextView tvLocality= (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat= (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng= (TextView) v.findViewById(R.id.tv_lng);
                    TextView tvSnippet= (TextView) v.findViewById(R.id.tv_snippet);
                    List<Address>list=null;
                    Geocoder gc=new Geocoder(MapsActivity.this);
                    LatLng ll=marker.getPosition();
                    try{
                        list=gc.getFromLocation(ll.latitude,ll.longitude,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                   // LatLng ll=marker.getPosition();
                    Address add=list.get(0);
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Location: "+df.format(ll.latitude)+","+df.format(ll.longitude));
                    tvLng.setText("Address: "+add.getAddressLine(1));
                    //tvLng.setText("Address: "+marker.getSnippet());
                    tvSnippet.setText(marker.getSnippet());
                    return v;
                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String msg=marker.getPosition().latitude+","+marker.getPosition().longitude;
                    //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                        Geocoder gc=new Geocoder(MapsActivity.this);
                        List<Address>list=null;
                        LatLng ll=marker.getPosition();
                        try{
                            list=gc.getFromLocation(ll.latitude,ll.longitude,1);
                        }catch (IOException e){
                            e.printStackTrace();
                            return;
                        }
                        Address add=list.get(0);
                        marker.setTitle("My Location");
                       //marker.setSnippet(add.getAddressLine(1));
                        marker.showInfoWindow();


                }
            });
        }
        return (mMap!=null);
    }
    private void gotoLocation(double lat,double lng){

            LatLng ll=new LatLng(lat,lng);
            CameraUpdate update= CameraUpdateFactory.newLatLng(ll);
            mMap.moveCamera(update);

    }
    private void gotoLocation(double lat,double lng,float zoom){

        LatLng ll=new LatLng(lat,lng);
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mMap.moveCamera(update);

    }
    public void reportcrime(View v){
        //Intent intent= new Intent(this, ReportCrime.class);
        //startActivity(intent);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View  myLoginView= layoutInflater.inflate(R.layout.activity_report_crime, null);

        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("Reprot Crime").
                setIcon(R.mipmap.ic_launcher2).
                setView(myLoginView).
                setPositiveButton("Report", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                //spinner = (Spinner) findViewById(R.id.spinner);

                        create();


        final Spinner spinner,spinner1;
        final ArrayAdapter<CharSequence> adapter,adapter1;

        spinner= (Spinner) myLoginView.findViewById(R.id.spinner);
        adapter= ArrayAdapter.createFromResource(this,R.array.crimetype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position)+"selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1= (Spinner) myLoginView.findViewById(R.id.spinner2);
        adapter1= ArrayAdapter.createFromResource(this,R.array.reportertype,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position)+"selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        alertDialog.show();

    }
    public void filtercrime(View v){
        //Intent intent= new Intent(this, ReportCrime.class);
        //startActivity(intent);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View  myLoginView= layoutInflater.inflate(R.layout.activity_report_crime, null);

        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("Filter Crime").
                setIcon(R.mipmap.ic_launcher2).
                setView(myLoginView).

                setPositiveButton("Filter", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                //spinner = (Spinner) findViewById(R.id.spinner);

                        create();


        final Spinner spinner,spinner1;
        final ArrayAdapter<CharSequence> adapter,adapter1;

        spinner= (Spinner) myLoginView.findViewById(R.id.spinner);
        adapter= ArrayAdapter.createFromResource(this,R.array.crimetype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position) + "selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1= (Spinner) myLoginView.findViewById(R.id.spinner2);
        adapter1= ArrayAdapter.createFromResource(this,R.array.reportertype,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position)+"selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        alertDialog.show();
    }

    public void Geogo(View view) throws IOException {
        EditText editText=(EditText)findViewById(R.id.editText2);
        String location=editText.getText().toString();
        Geocoder gc=new Geocoder(this);
        List<Address>list=gc.getFromLocationName(location,1);
        Address add=list.get(0);
        String locality=add.getLocality();
        Toast.makeText(this,locality,Toast.LENGTH_LONG).show();
        double lat=add.getLatitude();
        double lng=add.getLongitude();

        gotoLocation(lat,lng,DEFAULTZOOM);
        setMarker(locality, add.getCountryName(), lat, lng);
        /*MarkerOptions options=new MarkerOptions()
                //.title(street)
                .position(new LatLng(lat,lng));
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmaker_blue));
        mMap.addMarker(options);*/



    }

    public void gosetting(View view){
        Intent intent=new Intent(this,setting1.class);
        startActivity(intent);

    }
    public void goprofile(View view){
        Intent intent=new Intent(this,profile.class);
        startActivity(intent);

    }
    private void setMarker(String locality, String country, double lat, double lng) {

        /*if (marker != null) {
            marker.remove();
        }*/

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmaker_blue));
        if (country.length() > 0) {
            options.snippet(country);
        }

        marker = mMap.addMarker(options);

    }
    private void setMarker(String locality, String country,LatLng ll, String datatime) {

        /*if (marker != null) {
            marker.remove();
        }*/

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(ll)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmaker_blue));
        if (country.length() > 0) {
            options.snippet("Detail: "+country+"\nTime: "+datatime);
            //options.snippet(datatime);
        }

        marker = mMap.addMarker(options);

    }
    private void setMyLocationMarker( double lat, double lng) {

        if (mylocation_marker != null) {
            mylocation_marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .title("My Location")
                .position(new LatLng(lat, lng))
                .draggable(true)

                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmker_organge));

        mylocation_marker = mMap.addMarker(options);

    }
    private void setcrime(int d) throws IOException {
        //Intent intent=new Intent(this,MainActivity.class);
        //Toast.makeText(this,"Thank for using Gotcha",Toast.LENGTH_LONG).show();
        //startActivity(intent);
        String[] adds = new String[5];
        adds[0]="1000 diamond street Philadelphia";
        adds[1]="Temple University";
        adds[2]="13th Philadelphia";
        adds[3]="Cecil B Moore ";
        adds[4]="city hall Phiadelphia";
        for(int i=0;i<5;i++) {
            String location = adds[i];

            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(location, 1);
            Address add = list.get(0);
            String locality = add.getLocality();
            //Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
            double lat = add.getLatitude();
            double lng = add.getLongitude();

            gotoLocation(lat, lng, DEFAULTZOOM);
            setMarker(locality, add.getAddressLine(1), lat, lng);
        }

    }
  /*  public void gomain(View view) throws IOException {
        Intent intent=new Intent(this,MainActivity.class);
        Toast.makeText(this,"Thank for using Gotcha",Toast.LENGTH_LONG).show();
        startActivity(intent);


    }*/
    //4.19 change is start from here to 589 line--------------------------------------------------------------------------------------------------------------------------------------------------------
    public void gomain(View view) throws IOException {
        //use logout button as the test button to test the json from all_report.php
        Intent intent=new Intent(this,MainActivity.class);
        Toast.makeText(this,"Thank for using Gotcha",Toast.LENGTH_LONG).show();
        startActivity(intent);



    }
    class asynclogin extends AsyncTask< String, String, String > {


        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Loading the crime....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //pDialog.dismiss();
            try {
                String reports_jsonstring=mapstatus();
                return reports_jsonstring;
            } catch (JSONException e) {
                e.printStackTrace();
                return  "error";
            }

        }


        protected void onPostExecute(String result) {

            pDialog.dismiss();
            Log.e("onPostExecute=",""+result);

            if (result.equals("error")){
                // new intent
                //Intent i=new Intent(login.this, MapsActivity.class);
                //i.putExtra("user",user);
                //startActivity(i);
                Log.d("onPost","Error");
            }else{
                try {
                    JSONObject jdata= new JSONObject(result);
                    JSONArray reports=jdata.getJSONArray("dataList");
                    for(int i=0;i<reports.length();i++){
                        JSONObject single_report= (JSONObject) reports.opt(i);
                        String detail=single_report.getString("report");
                        double lag=single_report.getDouble("lat");
                        double logitude=single_report.getDouble("lon");
                        String type= single_report.getString("type");
                        String time=single_report.getString("datetime");
                        //Log.d("onPostdetail_location_lat", String.valueOf(lag));
                        //Log.d("onPostdetail_location_lon", String.valueOf(logitude));
                        Log.d("onPostdetail_report",detail);
                        Log.d("onPostdetail_type",type);
                        Log.d("onPostdedate_time",time);
                        //setMarker(type, detail, lag, logitude);
                        setMarker(type,detail,new LatLng(lag,logitude),time);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public String mapstatus( ) throws JSONException {
        int logstatus1;
        JSONObject jdata=null;
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        //ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();


        jdata= JSONParser.makeHttpRecieve(URL_connect);
        JSONArray reports=jdata.getJSONArray("dataList");
        String reprot_json=jdata.toString();
        for(int i=0;i<2;i++){
            JSONObject single_report= (JSONObject) reports.opt(i);
            String detail=single_report.getString("report");
            double lag=single_report.getInt("lat");
            double logitude=single_report.getInt("lon");
            String type= single_report.getString("type");
            Log.d("detail_location_lat", String.valueOf(lag));
            Log.d("detail_location_lon", String.valueOf(logitude));
            Log.d("detail_report",detail);
            Log.d("detail_type",type);
            Log.d("fuck_string",reprot_json);
            //setMarker(type, detail, lag, logitude);

        }
        SystemClock.sleep(950);
        return reprot_json;

       /* if (jdata!=null && jdata.length() > 0){

            //JSONObject json_data; //creamos un objeto JSON
            try {
                //json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                logstatus=jdata.getInt("logstatus");//accedemos al valor
                Log.e("mapstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //validamos el valor obtenido
            if (logstatus==0){// [{"logstatus":"0"}]
                Log.e("mapstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("mapstatus ", "valido");
                return true;
            }

        }

        else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR111");
            return false;
        }*/

    }
    public void err_login(){
        //Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Wrong password or username", Toast.LENGTH_LONG);
        toast1.show();
    }


}
