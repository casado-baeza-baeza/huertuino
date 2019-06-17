package com.example.huertuino;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private JSONArray raiz;
    private MediaPlayer alarmaHumedad,riegoSound,computando;
    final String TAG="TAG";
    private URL url=null;
    private ImageView icoCielo,alerta;
    private String HumedadAire,HumedadTierra,Temperatura,humTie, tempi,peticion, respuesta,miHuerto;
    private Calendar c;
    private int Riego, hora,diaMes, mes, indiceHoras,indiceHorasLLuvia;
    DatabaseReference bbdd;
    private TextView tempa, porTierra,humeAire,aemetT,poblacion,orcaso;
    private ProgressBar barAire, barTierra, barTemp;
    private boolean primera_vez=true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final  FloatingToolbar mFloatingToolbar= (FloatingToolbar) findViewById(R.id.floatingToolbar);
        final FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);

        mFloatingToolbar.attachFab(fab);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        miHuerto=getIntent().getStringExtra("huer");


        alarmaHumedad=MediaPlayer.create(this,R.raw.humedadrobot);
        computando=MediaPlayer.create(this,R.raw.compu);
        riegoSound=MediaPlayer.create(this,R.raw.riego);


        alerta=(ImageView)findViewById(R.id.alertatierra);
        alerta.setVisibility(View.INVISIBLE);
        orcaso=(TextView)findViewById(R.id.ortoocaso);

        aemetT=(TextView)findViewById(R.id.textAemetTemp);
        tempa=(TextView)findViewById(R.id.Temp);
        bbdd= FirebaseDatabase.getInstance().getReference();
        barAire=(ProgressBar)findViewById(R.id.barraire);
        c= Calendar.getInstance();
        hora=c.get(Calendar.HOUR_OF_DAY);
        diaMes=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH)+1;
        icoCielo=(ImageView)findViewById(R.id.iconoCielo);
        barTierra=(ProgressBar)findViewById(R.id.barratierra);
        barTierra.setMax(1024);
        porTierra=(TextView)findViewById(R.id.porcenTierra);
        barTemp=(ProgressBar)findViewById(R.id.barraTemp);
        humeAire=(TextView)findViewById(R.id.humiAire);
        barTemp.setMax(50);
        barAire.setMax(100);

        peticion = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/horaria/46126?api_key=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWN0b3IuYmFlemEuZEBnbWFpbC5jb20iLCJqdGkiOiI5YzBjYzFkZi0zMmFjLTQ4MGMtYmUwYy0xYmI0ZTgzN2U2NmMiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTU1MzU5OTIyMywidXNlcklkIjoiOWMwY2MxZGYtMzJhYy00ODBjLWJlMGMtMWJiNGU4MzdlNjZjIiwicm9sZSI6IiJ9.8j1c9KLJvRKE1JeNA-yMzJ3JlElDYMFSbdY_QFKUn-A";



        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_llovera:

                        llovera();
                        break;

                    case R.id.action_regar:

                        riego();

                        break;


                    case R.id.action_luna:

                        getLuna();

                        break;


                }

            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });

        bbdd.child("Huertos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Riego=dataSnapshot.child(miHuerto).child("Riego").getValue(Integer.class);
                    Temperatura=dataSnapshot.child(miHuerto).child("Temperatura").getValue(String.class);
                    setTemper(Temperatura);


                    HumedadTierra=dataSnapshot.child(miHuerto).child("HumedadTierra").getValue(String.class);
                    setHumTierra(HumedadTierra);

                    HumedadAire=dataSnapshot.child(miHuerto).child("HumedadAire").getValue(String.class);
                    setHumAire(HumedadAire);



                    TareaAsincrona tareaAsincrona = new TareaAsincrona();
                    tareaAsincrona.execute(peticion);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menudos, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info) {
            Toast.makeText(getApplicationContext(), "Desarrollada por:\nIsrael Baeza Dominguez \nDavid Casado Jimenez\nVictor Baeza Dominguez", Toast.LENGTH_LONG).show();
            return true;
        } else  {

            return true;
        }
    }
    public void getLuna(){

        Intent intento= new Intent(this,Main2Activity.class);
        intento.putExtra("mes", String.valueOf(mes));
        startActivity(intento);




    }


    class TareaAsincrona extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {

            respuesta=API_REST(strings[0]);
            return respuesta;
        }


        @Override
        protected void onPostExecute(String respuesta) {

            if (respuesta!=null) {
                try {

                    if (primera_vez) {
                        primera_vez = false;

                        JSONObject jsonObject=new JSONObject(respuesta);
                        String datos=jsonObject.getString("datos");


                        TareaAsincrona tareaAsincrona2=new TareaAsincrona();
                        tareaAsincrona2.execute(datos);

                    } else {
                        primera_vez=true;
                        raiz=new JSONArray(respuesta);
                        JSONObject orig=raiz.getJSONObject(0);
                        String nombre=orig.getString("nombre");
                        poblacion=(TextView)findViewById(R.id.municipio);
                        poblacion.setText(nombre);

                        JSONArray estCielo = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("estadoCielo");

                        String orto=raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString("orto");
                        String ocaso=raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getString("ocaso");
                        orcaso.setText("Salida: "+orto+"\n"+ "Puesta: " +ocaso);


                        for (int i=0; i<estCielo.length();i++){
                            JSONObject horas=estCielo.getJSONObject(i);
                            int horita=horas.getInt("periodo");

                            if(horita==hora){
                                indiceHoras=i;

                            }

                        }




                        String descripcion=raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("estadoCielo").getJSONObject(indiceHoras).getString("descripcion");
                        setIconoTiempo(descripcion);

                        int aemetTemp=raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("temperatura").getJSONObject(indiceHoras).getInt("value");
                        aemetT.setText(String.valueOf(aemetTemp)+"ºC");

                    }

                } catch (JSONException e) {
                    //Toast.makeText( getApplicationContext(), "Problemas en el servicio web de AEMET", Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                    Log.d(TAG, "Problemas decodificando JSON");
                }
            }

        } // onPostExecute


    } // TareaAsincrona

    public void llovera() {

        Intent intent=new Intent(this,Main3Activity.class);
        intent.putExtra("raiz",raiz.toString());
        intent.putExtra("hora",String.valueOf(hora));
        startActivity(intent);


    }

    public String API_REST(String uri){

        StringBuffer response = null;


        try {
            url = new URL(uri);
            Log.d(TAG, "URL: " + uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Detalles de HTTP
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");


            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Codigo de respuesta: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),"ISO-8859-1"));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            } else {
                Log.d(TAG, "responseCode: " + responseCode);
                return null; // retorna null anticipadamente si hay algun problema
            }
        } catch(Exception e) { // Posibles excepciones: MalformedURLException, IOException y ProtocolException
            e.printStackTrace();
            Log.d(TAG, "Error conexión HTTP:" + e.toString());
            return null; // retorna null anticipadamente si hay algun problema
        }

        return new String(response); // de StringBuffer -response- pasamos a String

    } // API_REST




    public void setIconoTiempo(String descrip){

        switch (descrip){

            case "Despejado":
                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.despejado);
                }else{
                    icoCielo.setImageResource(R.drawable.despejadonoche);

                }
                break;

            case "Poco nuboso":
                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.poconuboso);
                }else{
                    icoCielo.setImageResource(R.drawable.poconubosonoche);

                }
                break;

            case "Intervalos nubosos":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.intervalosnubosos);
                }else{
                    icoCielo.setImageResource(R.drawable.intervalosnubososnoche);

                }
                break;

            case "Nuboso":
                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nuboso);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosonoche);

                }
                break;

            case "Muy nuboso":
                icoCielo.setImageResource(R.drawable.muynuboso);
                break;
            case "Cubierto":
                icoCielo.setImageResource(R.drawable.cubierto);
                break;
            case "Nubes altas":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubesaltas);
                }else{
                    icoCielo.setImageResource(R.drawable.nubesaltasnoche);

                }
                break;

            case "Intervalos nubosos con lluvia escasa":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.intervalosnubososconlluviaescasa);
                }else{
                    icoCielo.setImageResource(R.drawable.intervalosnubososconlluviaescasanoche);

                }
                break;


            case "Nuboso con lluvia escasa":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubosoconlluviaescasa);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosoconlluviaescasanoche);

                }
                break;


            case "Muy nuboso con lluvia escasa":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubosoconlluviaescasa);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosoconlluviaescasanoche);

                }
                break;

            case "Cubierto con lluvia escasa":
                icoCielo.setImageResource(R.drawable.cubiertoconlluviaescasa);
                break;
            case "Intervalos nubosos con lluvia":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.intervalosnubososconlluvia);
                }else{
                    icoCielo.setImageResource(R.drawable.intervalosnubososconlluvianoche);

                }
                break;


            case "Nuboso con lluvia":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubosoconlluvia);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosoconlluvianoche);

                }
                break;


            case "Muy nuboso con lluvia":
                icoCielo.setImageResource(R.drawable.muynubosoconlluvia);
                break;
            case "Cubierto con lluvia":
                icoCielo.setImageResource(R.drawable.cubiertoconlluvia);
                break;
            case "Intervalos nubosos con tormenta":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.intervalosnubososcontormenta);
                }else{
                    icoCielo.setImageResource(R.drawable.intervalosnubososcontormentanoche);

                }
                break;



            case "Nuboso con tormenta":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubosocontormenta);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosocontormentanoche);

                }
                break;

            case "Muy nuboso con tormenta":
                icoCielo.setImageResource(R.drawable.muynubosocontormenta);
                break;
            case "Cubierto con tormenta":
                icoCielo.setImageResource(R.drawable.cubiertocontormenta);
                break;
            case "Intervalos nubosos con tormenta y lluvia escasa":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.intervalosnubososcontormentaylluviaescasa);
                }else{
                    icoCielo.setImageResource(R.drawable.intervalosnubososcontormentaylluviaescasanoche);

                }
                break;


            case "Nuboso con tormenta y lluvia escasa":

                if (hora<21 && hora>7) {
                    icoCielo.setImageResource(R.drawable.nubosocontormentaylluviaescasa);
                }else{
                    icoCielo.setImageResource(R.drawable.nubosocontormentaylluviaescasanoche);

                }
                break;

            case "Muy nuboso con tormenta y lluvia escasa":
                icoCielo.setImageResource(R.drawable.muynubosocontormentaylluviaescasa);
                break;
            case "Cubierto con tormenta y lluvia escasa":
                icoCielo.setImageResource(R.drawable.cubiertocontormentaylluviaescasa);
                break;
        }


    }

    public void setHumAire(String aire){

        int hum=Math.round(Float.parseFloat(aire));
        barras(barAire,hum,2);
        barAire.setProgress(hum);
        aire=aire+"%";
        humeAire.setText(aire);


    }

    public void setHumTierra(String ti){

        barras(barTierra,Integer.valueOf(ti),0);
        barTierra.setProgress(1024-Integer.valueOf(ti));
        if ((1024-Integer.valueOf(ti))<=330){

            alerta.setVisibility(View.VISIBLE);
        }else{

            alerta.setVisibility(View.INVISIBLE);
        }
        humTie=Integer.toString(((1024-Integer.valueOf(ti))*100)/1024);
        porTierra.setText(humTie+"%");

    }

    public void setTemper(String t){
        tempi=(t)+"ºC";
        int tem=Math.round(Float.parseFloat(t));

        barras(barTemp,tem,1);
        tempa.setText(tempi);
        barTemp.setProgress(tem);


    }

    public void riego(){


            if(Riego==1){
                Toast.makeText(this,"Se esta regando",Toast.LENGTH_LONG).show();
                computando.start();

            }else {
                riegoSound.start();
                bbdd.child("Huertos").child(miHuerto).child("Riego").setValue(1);
            }


    }


    public void barras(ProgressBar bar, int f, int quinaBarra){


        switch (quinaBarra) {

            case 0: //humedad de la tierra

            if (f >= 666.) {
                bar.getProgressDrawable().setColorFilter(0xFFFC3E04, android.graphics.PorterDuff.Mode.SRC_IN);

                    if (Riego==0) {
                        alarmaHumedad.start();
                    }


            } else if (f >= 400 && f < 666.0) {

                bar.getProgressDrawable().setColorFilter(0xFFFCD304, android.graphics.PorterDuff.Mode.SRC_IN);

            } else if (f < 400) {
                bar.getProgressDrawable().setColorFilter(0xFF63FC04, android.graphics.PorterDuff.Mode.SRC_IN);

            }
            break;

            case 1: //temperatura

                 if (f >= 40) {
                bar.getProgressDrawable().setColorFilter(0xFFE90000, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f >= 30 && f<40) {
                    bar.getProgressDrawable().setColorFilter(0xFFFF8C00, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f >= 20 && f < 30) {

                    bar.getProgressDrawable().setColorFilter(0xFFFCD304, android.graphics.PorterDuff.Mode.SRC_IN);

                }else if (f >= 10 && f < 20) {

                    bar.getProgressDrawable().setColorFilter(0xFFE2C65F, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f < 10&&f>0) {
                    bar.getProgressDrawable().setColorFilter(0xFF5595E0, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f <= 0) {
                bar.getProgressDrawable().setColorFilter(0xFFC4D1F0, android.graphics.PorterDuff.Mode.SRC_IN);

                }
                break;

            case 2: //huemdad aire

                if (f >= 75) {
                    bar.getProgressDrawable().setColorFilter(0xFF63FC04, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f >= 50 && f<75) {
                    bar.getProgressDrawable().setColorFilter(0xFFFCD304, android.graphics.PorterDuff.Mode.SRC_IN);

                } else if (f >= 25 && f < 50) {

                    bar.getProgressDrawable().setColorFilter(0xFFFCA104, android.graphics.PorterDuff.Mode.SRC_IN);

                }else if (f >= 0 && f < 25) {

                    bar.getProgressDrawable().setColorFilter(0xFFFC3E04, android.graphics.PorterDuff.Mode.SRC_IN);

                }
                break;
        }

    }







}
