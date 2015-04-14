package com.example.chuanzhu.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //add comment here
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        //
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
        //return false;
    }
    public void Signfuction(View v){
        Intent intent= new Intent(this, signup.class);
        startActivity(intent);

    }
    public void Loginfuction(View v){
        Intent intent= new Intent(this, login.class);
        startActivity(intent);

    }
    public void Guest(View v){
        Intent intent= new Intent(this, MapBar.class);
        startActivity(intent);

    }





}
