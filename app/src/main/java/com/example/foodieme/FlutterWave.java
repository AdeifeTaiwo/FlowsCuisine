package com.example.foodieme;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.*;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;


import java.util.UUID;

public class FlutterWave extends AppCompatActivity  {
    Button btnOne, btnTwo;
    final int amount_1 = 5000;
    final int amount_2 = 2500;
    String email = "adeifetaiwo50@gmail.com";
    String fName = "FirstName";
    String lName = "LastName";
    String narration = "payment for food";
    String txRef;
    String country = "NG";
    String currency = "NGN";

    final String publicKey = "FLWPUBK_TEST-8ad3b86f4e9fed3abc0d1f0621cc4f52-X"; //Get your public key from your account
    final String encryptionKey = "FLWSECK_TEST6a2aa15ad86b"; //Get your encryption key from your account


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_wave);

        btnOne = findViewById(R.id.btn_one);

        btnTwo = findViewById(R.id.btn_two);

    }



    public void makePayment(int amount){
        txRef = email +" "+  UUID.randomUUID().toString();

        /*
        Create instance of RavePayManager
         */
       RaveUiManager raveUiManager = new RaveUiManager(this);
       raveUiManager.setAmount(amount)
                .setCurrency(currency)
                .setEmail(email)
                .setfName(fName)
                .setlName(lName)
                .setNarration(narration)
                .setPublicKey(publicKey)
                .setEncryptionKey(encryptionKey)
                .setTxRef(txRef)

                    .acceptAccountPayments(true)
                    .acceptCardPayments(true)
                    .acceptMpesaPayments(true)
                    .acceptAchPayments(true)
                    .acceptGHMobileMoneyPayments(true)
                    .acceptUgMobileMoneyPayments(true)
                    .acceptSaBankPayments(true)
                    .acceptUkPayments(true)
                    .acceptBankTransferPayments(true)
                    .acceptUssdPayments(false)
                    .acceptBarterPayments(false)
                    .shouldDisplayFee(true)
                    .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
    }
    

    public void button1(View view) {
        makePayment(amount_1);
    }

    public void button2(View view) {
        makePayment(amount_2);
    }
}