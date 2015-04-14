package com.example.chuanzhu.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Vibrator;
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

import com.android.jsonparser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


public class login extends ActionBarActivity {

    EditText username;
    EditText password;
    Button blogin;
    String URL_connect="http://mikekorostelev.com/~bits/Xu/login/return_row.php";
    private static InputStream is = null;
    private static String json = "";
    private ProgressDialog pDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher2);
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
                Log.d("login input",username_str);
                Log.d("login password",password_str);

                if( checklogindata( username_str , password_str )==true){

                    //si pasamos esa validacion ejecutamos el asynctask pasando el usuario y clave como parametros

                   new asynclogin().execute(username_str , password_str);


                }else{
                    //si detecto un error en la primera validacion vibrar y mostrar un Toast con un mensaje de error.
                    err_login();
                }

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



    public boolean checklogindata(String username ,String password ) {

        if (username.equals("") || password.equals("")) {
            Log.e("Login in", "checklogindata user or pass error");
            return false;

        } else {

            return true;
        }
    }

    class asynclogin extends AsyncTask< String, String, String > {

        String user,pass;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(login.this);
            pDialog.setMessage("Authoring....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            user=params[0];
            pass=params[1];

            //enviamos y recibimos y analizamos los datos en segundo plano.
            if (loginstatus(user,pass)==true){
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
               Intent i=new Intent(login.this, MapBar.class);
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
        Toast toast1 = Toast.makeText(getApplicationContext(),"Error:Wrong password or username", Toast.LENGTH_LONG);
        toast1.show();
    }

    public boolean loginstatus(String username ,String password ) {
        int logstatus=-1;
        JSONObject jdata=null;
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/
        ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

        postparameters2send.add(new BasicNameValuePair("usuario",username));
        postparameters2send.add(new BasicNameValuePair("password",password));

        //realizamos una peticion y como respuesta obtenes un array JSON
        //JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
        /*
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL_connect);
            httpPost.setEntity(new UrlEncodedFormEntity(postparameters2send));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            jdata = new JSONObject(json);
        }catch( JSONException e){
            e.printStackTrace();
        }
        Log.d("Login attempt", json.toString());
        Log.e("Login attempt", String.valueOf(jdata));
        */
        //jdata= JSONParser.makeHttpRequest(URL_connect,"POST", postparameters2send);
      	jdata= JSONParser.makeHttpRequest(URL_connect,"POST", postparameters2send);
      			/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      		 * para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      		 * observar el progressdialog
      		 * la podemos eliminar si queremos
      		 */
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
                Log.e("loginstatus ", "invalido");
                return false;
            }
            else{// [{"logstatus":"1"}]
                Log.e("loginstatus ", "valido");
                return true;
            }

        }

        else{	//json obtenido invalido verificar parte WEB.
            Log.e("JSON  ", "ERROR111");
            return false;
        }

    }


}
