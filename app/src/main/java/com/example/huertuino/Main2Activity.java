package com.example.huertuino;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

public class Main2Activity extends AppCompatActivity {

    public int mes,diaMes,dias;
    private String mestring,estado,istring;
    DatabaseReference bbddos;
    private TextView cualmes,lunallena,creciente,nueva,menguante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mes=Integer.valueOf(getIntent().getStringExtra("mes"));

        bbddos= FirebaseDatabase.getInstance().getReference();
        cualmes=(TextView)findViewById(R.id.queMes);
        creciente=(TextView)findViewById(R.id.diasCreci);
        lunallena=(TextView)findViewById(R.id.diasLlena);
        nueva=(TextView)findViewById(R.id.diasNueva);
        menguante=(TextView)findViewById(R.id.diasMengua);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);



        switch (mes){
            case 1:
                mestring="Enero";
                dias=31;
                break;
            case 2:
                mestring="Febrero";
                dias=28;
                break;
            case 3:
                mestring="Marzo";
                dias=31;
                break;
            case 4:
                mestring="Abril";
                dias=30;
                break;
            case 5:
                mestring="Mayo";
                dias=31;
                break;
            case 6:
                mestring="Junio";
                dias=30;
                break;
            case 7:
                mestring="Julio";
                dias=31;
                break;
            case 8:
                mestring="Agosto";
                dias=31;
                break;
            case 9:
                mestring="Septiembre";
                dias=30;
                break;
            case 10:
                mestring="Octubre";
                dias=31;
                break;
            case 11:
                mestring="Noviembre";
                dias=30;
                break;
            case 12:
                mestring="Diciembre";
                dias=31;
                break;

        }
        cualmes.setText(mestring);


        bbddos.child("CalendarioLunar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (int i=1; i<=dias;i++){
                         istring=String.valueOf(i);

                        estado=dataSnapshot.child(mestring).child(String.valueOf(i)).child("estado").getValue(String.class);

                        if (estado.equalsIgnoreCase("luna llena")){

                            lunallena.append(String.valueOf(i));

                        }
                        if (estado.equalsIgnoreCase("cuarto creciente")){

                            creciente.append(String.valueOf(i));

                        }
                        if (estado.equalsIgnoreCase("cuarto menguante")){

                            menguante.append(String.valueOf(i));

                        }
                        if (estado.equalsIgnoreCase("luna nueva")){

                            nueva.append(String.valueOf(i));

                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}