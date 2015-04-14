package com.example.chuanzhu.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.jsonparser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


public class signup extends ActionBarActivity {

    EditText password;
    EditText password2;
    AutoCompleteTextView usename_signup;
    AutoCompleteTextView email_signup;
    AutoCompleteTextView phone_signup;
    AutoCompleteTextView address_signup;
    CheckBox checkbox_signup;
    Button button_signup;
    String URL_connect="http://mikekorostelev.com/~bits/Xu/login/signup.php";
    private static InputStream is = null;
    private static String json = "";
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usename_signup=(AutoCompleteTextView)findViewById(R.id.username_signup);
        email_signup=(AutoCompleteTextView)findViewById(R.id.email_sign);
        password=(EditText) findViewById(R.id.password_sign);
        password2=(EditText)findViewById(R.id.password2_sign);
        phone_signup=(AutoCompleteTextView)findViewById(R.id.phone_signup);
        address_signup=(AutoCompleteTextView)findViewById(R.id.address_signup);
        checkbox_signup=(CheckBox) findViewById(R.id.checkBox_signup);
        button_signup=(Button) findViewById(R.id.signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_str=usename_signup.getText().toString();
                String password_str=password.getText().toString();
                String password2_str=password2.getText().toString();
                String phone_string=phone_signup.getText().toString();
                String add_str=address_signup.getText().toString();
                String email_str=email_signup.getText().toString();
                if(check_name(username_str)){
                    if(check_password(password_str,password2_str)) {
                        if(checkbox_signup.isChecked()) {
                            Log.d("signup ", "sucess" + username_str + password2_str);
                            //Toast.makeText(getApplicationContext(),"Successful signup", Toast.LENGTH_LONG).show();
                            //connect to the php
                            new asynclogin().execute(username_str , password_str,add_str,phone_string,email_str);
                        }else{
                            Log.d("signup ", "checkbox error");
                            Toast.makeText(getApplicationContext(),"Error:click the checkbox", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Log.d("signup ", "password error");
                        Toast.makeText(getApplicationContext(),"Error:password error", Toast.LENGTH_LONG).show();
                    }
                }else{

                    Toast.makeText(getApplicationContext(),"Error:Wrong username", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean check_password(String password_str, String password2_str) {
        if(password_str.equals(password2_str) &&password_str.length()!=0){
            return true;
        }
        else{
            return false;
        }

    }

    private boolean check_name(String username) {
        if(username.length()!=0 ){
            return true;
        }else{
            return false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
    /*
    public void Loginfuction(View v){
        Toast.makeText(this,"Finish Sign up!",Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, login.class);
        startActivity(intent);

    }*/

    class asynclogin extends AsyncTask< String, String, String > {

        String user,pass,add,phone,email;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(signup.this);
            pDialog.setMessage("Authoring....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            user=params[0];
            pass=params[1];
            add=params[2];
            phone=params[3];
            email=params[4];

            //enviamos y recibimos y analizamos los datos en segundo plano.
            if (loginstatus(user,pass,add,phone,email)==true){
                return "ok"; //login valido
            }else{
                return "err"; //login invalido
            }

        }


        protected void onPostExecute(String result) {

            pDialog.dismiss();
            Log.e("onPostExecute=",""+result);

            if (result.equals("ok")){
                // new intent
                Intent i=new Intent(signup.this, login.class);
                i.putExtra("user",user);
                startActivity(i);

            }else{
                err_login();
            }

        }

    }

    public void err_login(){
        //Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(200);
        Toast toast1 = Toast.makeText(getApplicationContext(),"Please use different username", Toast.LENGTH_LONG);
        toast1.show();
    }

    public boolean loginstatus(String username ,String password,String address, String phone, String email ) {
        int logstatus=-1;
        JSONObject jdata=null;

        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("username",username));
        postparameters2send.add(new BasicNameValuePair("password",password));
        postparameters2send.add(new BasicNameValuePair("address",address));
        postparameters2send.add(new BasicNameValuePair("phone",phone));
        postparameters2send.add(new BasicNameValuePair("email",email));

        jdata= JSONParser.makeHttpRequest(URL_connect, "POST", postparameters2send);

        SystemClock.sleep(950);

        //si lo que obtuvimos no es null

        if (jdata!=null && jdata.length() > 0){

            //JSONObject json_data; //creamos un objeto JSON
            try {
                //json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
                logstatus=jdata.getInt("logstatus");//accedemos al valor
                Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //validamos el valor obtenido
            if (logstatus==0){// [{"logstatus":"0"}]
                Log.e("loginstatus ", "insucess");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "sucuess");
                return true;
            }

        }

        else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR111");
            return false;
        }

    }



}
