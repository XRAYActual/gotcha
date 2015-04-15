package com.example.chuanzhu.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MapBar extends ActionBarActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent=null;
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                intent=new Intent(this,setting1.class);
                startActivity(intent);
                break;
            case R.id.action_profile:
                intent=new Intent(this,profile.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


        private GoogleMap mMap; // Might be null if Google Play services APK is not available.
        private static final double TEMPLE_LAT=39.982094,
                TEMPLE_LONG=-75.154679;
        private static final float DEFAULTZOOM=15;
        //mMap=(Ma)
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map_bar, container, false);
            //initMap();
            //if(initMap()){
                //Toast.makeText(this, "ready to map!", Toast.LENGTH_SHORT).show();
            //    gotoLocation(TEMPLE_LAT,TEMPLE_LONG,DEFAULTZOOM);
            //}
            //else{
                //Toast.makeText(this,"map is not affable!",Toast.LENGTH_SHORT).show();
            //}
            return rootView;
        }

        private boolean initMap(){
            if(mMap==null) {
                SupportMapFragment mapFrag=
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                mMap=mapFrag.getMap();
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

    }
    public void reportcrime(View v){
        //Intent intent= new Intent(this, ReportCrime.class);
        //startActivity(intent);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View  myLoginView= layoutInflater.inflate(R.layout.activity_report_crime, null);

        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("Reprot Crime").
                setIcon(R.mipmap.ic_launcher1).
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
                setIcon(R.mipmap.ic_launcher1).
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



}
