package com.example.chuanzhu.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Loading extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        onLoading();


    }

    private void onLoading() {
        Thread thread=new Thread(){
            public void run(){
                try {
                    Thread.sleep(2000);
                    SharedPreferences logpre= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Boolean islog=logpre.getBoolean("Islogin",false);
                    String  user=logpre.getString("userName","");
                    if(islog) {

                        Intent intend1 = new Intent(Loading.this, MapsActivity.class);
                        intend1.putExtra("user",user);
                        startActivity(intend1);
                    }
                    else{
                        Intent intend = new Intent(Loading.this, MainActivity.class);
                        startActivity(intend);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("loading","error");
                }
            }
        };
        thread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
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
