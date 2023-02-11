package com.cyclesystem.bliss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class EndResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_results);
        Button k = (Button) findViewById(R.id.button4);
        k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        float Shower = 0;

        Double View = null;
        try {
            FileInputStream fin = openFileInput("endfile.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }
            Shower = (float) Double.parseDouble(temp.toString());
            TextView pop = (TextView) findViewById(R.id.textView2);
            View = (0.5 * Shower) + 100;
            View = View * 100;
            View = Math.floor(View);
            View = View / 100;
            pop.setText("THANKS FOR USING ! YOU DROVE " + Shower + " metres in this ride !\n It means PKR " + View + "/- PayAble");
            fin.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////.............................................
        String F="";
        try {
            FileInputStream fin = openFileInput("Records.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            F = temp.toString();
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Date h = new Date();
        String A = String.valueOf(h.getDate());
        String B = String.valueOf(h.getMonth());
        if(F.equals("")){
            F = F + "On, "+A+"-"+B+"-"+"2023 : You Travelled "+Shower+" metres";
        }
        else {
            F = F + "|On, " + A + "-" + B + "-" + "2023 : You Travelled " + Shower + " metres";
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("Records.txt", Context.MODE_PRIVATE);
            fos.write(F.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////////
        Button bn = (Button) findViewById(R.id.button4);
        Double finalView = View;
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Uri uri;
                uri = new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "BCR2DN4TXLUNTMKH")       // virtual ID
                        .appendQueryParameter("pn", "HarrisBasra")          // name
                        .appendQueryParameter("tn", "BLISS CYCLE SYSTEM")       // any note about payment
                        .appendQueryParameter("am", String.valueOf(finalView))           // amount
                        .appendQueryParameter("cu", "PKR")                         // currency
                        .build();
                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                upiPayIntent.setData(uri);
// will always show a dialog to user to choose an app
                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
// check if intent resolves
                if(null != chooser.resolveActivity(getPackageManager())) {
                    int UPI_PAYMENT = 0;
                    startActivityForResult(chooser, UPI_PAYMENT);
                } else {
                    Toast.makeText(EndResults.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
                }




            }
        });

    }
}
