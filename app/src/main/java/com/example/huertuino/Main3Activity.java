package com.example.huertuino;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class Main3Activity extends AppCompatActivity {

    private int indiceHorasLLuvia;
    private TextView doscho,chodos,dosvocho,vochodos,tor1,tor2,tor3,tor4,temper,niev1,niev2,niev3,niev4,humetat,terma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        doscho=(TextView)findViewById(R.id.dosaocho);
        chodos=(TextView)findViewById(R.id.ochoados);
        dosvocho=(TextView)findViewById(R.id.dosavocho);
        vochodos=(TextView)findViewById(R.id.vochoados);
        tor1=(TextView)findViewById(R.id.torm28);
        tor2=(TextView)findViewById(R.id.torm814);
        tor3=(TextView)findViewById(R.id.torm1420);
        tor4=(TextView)findViewById(R.id.torm2002);
        temper=(TextView)findViewById(R.id.tempera);
        niev1=(TextView)findViewById(R.id.niev28);
        niev2=(TextView)findViewById(R.id.niev814);
        niev3=(TextView)findViewById(R.id.niev1420);
        niev4=(TextView)findViewById(R.id.niev2002);
        humetat=(TextView)findViewById(R.id.humi);
        terma=(TextView)findViewById(R.id.termica);

        Intent intent = getIntent();
        String raizArray = intent.getStringExtra("raiz");
        int hora=Integer.valueOf(intent.getStringExtra("hora"));


        try {
            JSONArray raiz = new JSONArray(raizArray);
            JSONArray probPreci = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("probPrecipitacion");
            JSONArray probTormen = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("probTormenta");
            JSONArray temperaturas = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("temperatura");
            JSONArray humedad = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("humedadRelativa");
            JSONArray probNieve = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("probNieve");
            JSONArray sensTerm = raiz.getJSONObject(0).getJSONObject("prediccion").getJSONArray("dia").getJSONObject(0).getJSONArray("sensTermica");
            for (int i=0; i<probPreci.length();i++){

                String p=probPreci.getJSONObject(i).getString("value");

                String tor=probTormen.getJSONObject(i).getString("value");
                String nie=probNieve.getJSONObject(i).getString("value");

                if (p.equalsIgnoreCase("")){
                    p="0";

                }
                if (tor.equalsIgnoreCase("")){
                    tor="0";

                }
                if (nie.equalsIgnoreCase("")){
                    nie="0";

                }
                int pint=Integer.valueOf(p);
                int intor=Integer.valueOf(tor);
                int intnie=Integer.valueOf(nie);
                p=p+"%";
                tor=tor+"%";
                nie=nie+"%";
                Spannable spannablep=new SpannableString(p);
                Spannable spannabletor=new SpannableString(tor);
                Spannable spannablenie=new SpannableString(nie);
                if (Integer.valueOf(pint)>=80){
                    spannablep.setSpan(new ForegroundColorSpan(Color.GREEN),0,p.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if (Integer.valueOf(pint)<80 && Integer.valueOf(pint)>=50){
                    spannablep.setSpan(new ForegroundColorSpan(Color.BLUE),0,p.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if(Integer.valueOf(pint)<50 && Integer.valueOf(pint)>=25){

                    spannablep.setSpan(new ForegroundColorSpan(Color.YELLOW),0,p.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else{

                    spannablep.setSpan(new ForegroundColorSpan(Color.RED),0,p.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (Integer.valueOf(intor)>=80){
                    spannabletor.setSpan(new ForegroundColorSpan(Color.RED),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if (Integer.valueOf(intor)<80 && Integer.valueOf(intor)>=50){
                    spannabletor.setSpan(new ForegroundColorSpan(Color.YELLOW),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if(Integer.valueOf(intor)<50 && Integer.valueOf(intor)>=25){

                    spannabletor.setSpan(new ForegroundColorSpan(Color.BLUE),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else{

                    spannabletor.setSpan(new ForegroundColorSpan(Color.GREEN),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (Integer.valueOf(intnie)>=80){
                    spannablenie.setSpan(new ForegroundColorSpan(Color.RED),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if (Integer.valueOf(intnie)<80 && Integer.valueOf(intnie)>=50){
                    spannablenie.setSpan(new ForegroundColorSpan(Color.YELLOW),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else if(Integer.valueOf(intnie)<50 && Integer.valueOf(intnie)>=25){

                    spannablenie.setSpan(new ForegroundColorSpan(Color.BLUE),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else{

                    spannablenie.setSpan(new ForegroundColorSpan(Color.GREEN),0,tor.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                switch (i){

                    case 0:
                        doscho.setText(spannablep);
                        tor1.setText(spannabletor);
                        niev1.setText(spannablenie);
                        break;
                    case 1:
                        chodos.setText(spannablep);
                        tor2.setText(spannabletor);
                        niev2.setText(spannablenie);
                        break;
                    case 2:
                        dosvocho.setText(spannablep);
                        tor3.setText(spannabletor);
                        niev3.setText(spannablenie);
                        break;
                    case 3:
                        vochodos.setText(spannablep);
                        tor4.setText(spannabletor);
                        niev4.setText(spannablenie);
                        break;
                }


            }


            for (int j=0;j<humedad.length();j++){

                String periodohume=humedad.getJSONObject(j).getString("periodo")+"h: ";
                String valuehume=humedad.getJSONObject(j).getString("value");
                int valorehume=Integer.valueOf(valuehume);
                String textTemp=periodohume+valuehume+"% \t\t\t";
                Spannable spannableh = new SpannableString(textTemp);

                if(valorehume>=75){

                    spannableh.setSpan(new ForegroundColorSpan(Color.GREEN), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (valorehume>=25 && valorehume<75){

                    spannableh.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else {

                    spannableh.setSpan(new ForegroundColorSpan(Color.RED),5,9,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }



                humetat.append(spannableh);


            }
            for (int j=0;j<sensTerm.length();j++){

                String periodoterm=sensTerm.getJSONObject(j).getString("periodo")+"h: ";
                String valueterm=sensTerm.getJSONObject(j).getString("value");
                int valoreterm=Integer.valueOf(valueterm);
                String textSens=periodoterm+valueterm+"ºC\t\t\t";
                Spannable spannables = new SpannableString(textSens);

                if(valoreterm>=35){

                    spannables.setSpan(new ForegroundColorSpan(Color.RED), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (valoreterm>=30 && valoreterm<35){

                    spannables.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (valoreterm>=15 &&valoreterm<30){
                    spannables.setSpan(new ForegroundColorSpan(Color.GREEN), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else {

                    spannables.setSpan(new ForegroundColorSpan(Color.BLUE),5,9,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }



                terma.append(spannables);


            }

            for (int j=0;j<temperaturas.length();j++){

                String periodo=temperaturas.getJSONObject(j).getString("periodo")+"h: ";
                String value=temperaturas.getJSONObject(j).getString("value");
                int valore=Integer.valueOf(value);
                String textTemp=periodo+value+"ºC\t\t\t";
                Spannable spannable = new SpannableString(textTemp);

                if(valore>=35){

                    spannable.setSpan(new ForegroundColorSpan(Color.RED), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (valore>=30 && valore<35){

                    spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else if (valore>=15 &&valore<30){
                    spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }else {

                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE),5,9,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }



                temper.append(spannable);


            }






        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
