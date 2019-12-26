package com.beingdev.magicprint;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.beingdev.magicprint.usersession.UserSession;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    private static String TAG = "SplashActivity";

    //to get user session data
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        printSignature();

        session = new UserSession(SplashActivity.this);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);

        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    void printSignature(){
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final byte[] encode = Base64.encode(md.digest(), 0);
                String something = String.valueOf(encode);
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.d(TAG + "hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d(TAG + "name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG + "no such algorithm", e.toString());
        } catch (Exception e) {
            Log.d(TAG + "exception", e.toString());
        }
    }
}
