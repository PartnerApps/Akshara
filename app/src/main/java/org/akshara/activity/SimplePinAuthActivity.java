package org.akshara.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.akshara.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimplePinAuthActivity extends AppCompatActivity {


    /**
     * PIN value encoding using SHA-1 & Base64
     */
    private static final String PIN = "NzExMGVkYTRkMDllMDYyYWE1ZTRhMzkwYjBhNTcyYWMwZDJjMDIyMA==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pin_auth);

        EditText pinET = (EditText) findViewById(R.id.pin_text);
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    validatePin(s.toString());
                }
            }
        });
    }


    /**
     * Method to validate the PIN given by the User
     * @param pin PIN entered by the user
     */
    private void validatePin(String pin) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(pin.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String tmp = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
                buffer.append(tmp);
            }

            String sha1PIN = buffer.toString();

            Log.i("Pin", "validatePin: " + sha1PIN);

            if (getOrginalValue(PIN).equals(sha1PIN)) {
                Intent mainScreen = new Intent(this, MainActivity.class);
                startActivity(mainScreen);
                finish();
            } else {
                Toast.makeText(this, "PIN is not valid", Toast.LENGTH_SHORT).show();
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the original value from Base64 Encoded data
     * @param input encoded value
     * @return Decode value of <code>input</code>
     */
    private static String getOrginalValue(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
