package com.example.chuanzhu.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;


public class login extends ActionBarActivity {

    EditText username;
    EditText password;
    Button blogin;
    String URL_connect="http://mikekorostelev.com/~bits/Xu/login/return_row.php";
    private static InputStream is = null;
    private static String json = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher1);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username=(EditText) findViewById(R.id.email_login);
        password=(EditText) findViewById(R.id.password_login);
        blogin=(Button)findViewById(R.id.email_sign_in_button);
        blogin.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                //Extreamos datos de los EditText
                String username_str=username.getText().toString();
                String password_str=password.getText().toString();
                Log.d("input",username_str);
                Log.d("password",password_str);
                //verificamos si estan en blanco
                //if( checklogindata( usuario , passw )==true){

                    //si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros

                  //  new asynclogin().execute(usuario,passw);


                //}else{
                    //si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    //err_login();
                //}

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if (id == R.id.action_settings) {
            intent=new Intent(this,setting1.class);
            startActivity(intent);
        }
        if(id== android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //public void mapfunction(View v){




        //Intent intent= new Intent(this, MapBar.class);
       // startActivity(intent);

    //}





}
