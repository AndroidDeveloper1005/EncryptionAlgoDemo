package com.kamana.aesalgorithmdemo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtEnterInput;
    private TextView mTvAesEncodedOutput;
    private TextView mTvAesDecodedOutput;

    private Button mBtnEncryption, mBtnDecryption;

    private SecretKeySpec sks =null;

    private byte[] encodedByte = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtEnterInput = (EditText)findViewById(R.id.etInputText);
        mTvAesEncodedOutput = (TextView)findViewById(R.id.tvAesEncodedOutput);
        mTvAesDecodedOutput = (TextView)findViewById(R.id.tvAesDecodedOutput);
        mBtnEncryption = (Button)findViewById(R.id.btnEncode);
        mBtnDecryption = (Button)findViewById(R.id.btnDecode);

        mBtnEncryption.setOnClickListener(this);
        mBtnDecryption.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnEncode:
                //method to encrypt the input string
                encodeData();
                break;

            case R.id.btnDecode:
                //method to decrypt the encoded output
                decodeData();
                break;
        }
    }

    private void decodeData() {

        byte[] decodeBytes = null;
        try{

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);

            decodeBytes = cipher.doFinal(encodedByte);
        }catch (Exception e){
            Log.e("decrypted", "AES decryption error");
        }

        mTvAesDecodedOutput.setText("[DECODED]:\n" + new String(decodeBytes) + "\n");
    }

    private void encodeData() {

        try{

            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, secureRandom);

            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        }catch (Exception e){

        }

        // Encode the original data with AES
        try{

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            encodedByte = cipher.doFinal(mEtEnterInput.getText().toString().getBytes());
        }catch (Exception e){

        }

        mTvAesEncodedOutput.setText("[ENCODED]:\n" +
                Base64.encodeToString(encodedByte, Base64.DEFAULT) + "\n");
    }
}
