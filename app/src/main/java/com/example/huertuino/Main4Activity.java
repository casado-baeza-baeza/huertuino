package com.example.huertuino;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main4Activity extends AppCompatActivity {

    private EditText huerto;
    private String huer;
    private String[]huertos={"","","","","","","","","",""};

    DatabaseReference bbdd;
    private MediaPlayer huertuinito,yes,nos;
    private boolean isHuerto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        bbdd= FirebaseDatabase.getInstance().getReference();
        huerto=(EditText)findViewById(R.id.huerti);
        yes=MediaPlayer.create(this,R.raw.yeah);
        nos=MediaPlayer.create(this,R.raw.prr);
        huertuinito=MediaPlayer.create(this,R.raw.iniciohuertu);
        huertuinito.start();




    }

    public void inicio(View view){
        huer=huerto.getText().toString();


        bbdd.child("Huertos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (int i=0;i<10;i++){
                if (snapshot.hasChild(huer)) {
                    isHuerto=true;
                    iniciodos();
                }else{
                    noinicio();

                }

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    public void noinicio(){
        nos.start();

    }
    public void iniciodos(){




            yes.start();
            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("huer", huer);
            startActivity(intent);





    }
}
