package com.kamana.aesalgorithmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

/**
 * Created by kamana on 12/6/18.
 */

public class RSAAlgoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtEnterInput;
    private TextView mTvAesEncodedOutput;
    private TextView mTvAesDecodedOutput;

    private Button mBtnEncryption, mBtnDecryption;

    private Key mPublicKey=null, mPrivateKey=null;

    private KeyPairGenerator kpg;

    private byte[] encodedBytes = null;

    private Button mBtnNavigateRSA, mBtnNavigateAES;

    private String mInputData = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtEnterInput = (EditText)findViewById(R.id.etInputText);
        mTvAesEncodedOutput = (TextView)findViewById(R.id.tvAesEncodedOutput);
        mTvAesDecodedOutput = (TextView)findViewById(R.id.tvAesDecodedOutput);
        mBtnEncryption = (Button)findViewById(R.id.btnEncode);
        mBtnDecryption = (Button)findViewById(R.id.btnDecode);
        mBtnNavigateRSA = (Button)findViewById(R.id.btnNavRSA);
        mBtnNavigateAES = (Button)findViewById(R.id.btnNavAES);

        mBtnEncryption.setOnClickListener(this);
        mBtnDecryption.setOnClickListener(this);
        mBtnNavigateAES.setOnClickListener(this);
        mBtnNavigateRSA.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnEncode:
                if (mInputData != null && !mInputData.equals("")) {
                encodeUsingRSA();
                }else {
                    Toast.makeText(this, "Kindly enter input that needs to be encoded", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnDecode:
                if (encodedBytes != null && !encodedBytes.equals("")) {
                    decodeUsingRSA();
                } else {
                    Toast.makeText(this, "Kindly enter the input data and encode the data first", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnNavAES:
                Intent intent = new Intent(RSAAlgoActivity.this, AesAlgoActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void decodeUsingRSA() {

        // Decode the encoded data with RSA public key
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, mPublicKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("decrypted", "RSA decryption error");
        }

        mTvAesDecodedOutput.setText("[DECODED]:\n" +
                Base64.encodeToString(decodedBytes, Base64.DEFAULT) + "\n");
    }

    private void encodeUsingRSA() {
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair keyPair = kpg.genKeyPair();
            mPublicKey = keyPair.getPublic();
            mPrivateKey = keyPair.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //encode the input data
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, mPrivateKey);
            encodedBytes = cipher.doFinal(mEtEnterInput.getText().toString().getBytes());

        }catch (Exception e){

        }

        mTvAesEncodedOutput.setText("[ENCODED]:\n" +
                Base64.encodeToString(encodedBytes, Base64.DEFAULT) + "\n");
    }
}
