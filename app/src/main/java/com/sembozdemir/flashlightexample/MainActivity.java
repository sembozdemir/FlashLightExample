package com.sembozdemir.flashlightexample;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    private ImageButton buttonOnOff;
    private Camera mCamera;
    private boolean isLight;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.layout);

        isLight = false;

        buttonOnOff = (ImageButton) findViewById(R.id.buttonOnOff);
        buttonOnOff.setImageResource(R.drawable.button);

        PackageManager packageManager = this.getPackageManager();

        if(!hasCameraFlash(packageManager)) {
            // TODO : kamera flaşı yoksa yapılacaklar
            Log.e("err", "Kamera flas ozelligini desteklemiyor.");
        } else {
            // Kamera Flaş özelliği var
            mCamera = Camera.open();
            final Camera.Parameters p = mCamera.getParameters();

            buttonOnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLight) { // açıksa
                        turnOff(p);
                    } else if(!isLight) { // kapalıysa
                        turnOn(p);
                    }

                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mCamera != null)
            mCamera.release();
    }

    private void turnOn(Camera.Parameters p) {
        Log.i("turnedOn", "turnedOn() a girildi.");
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); // flaşı aç
        mCamera.setParameters(p);
        mCamera.startPreview();
        isLight = true; // flag'i guncelle
        layout.setBackgroundColor(Color.rgb(238,238,238)); // arka planı açık renk yap
    }

    private void turnOff(Camera.Parameters p) {
        Log.i("turnedOff", "turnedOff() a girildi.");
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); // flaşı kapat
        mCamera.setParameters(p);
        mCamera.stopPreview();
        isLight = false; // flag'i guncelle
        layout.setBackgroundColor(Color.rgb(68, 68, 68)); // arka planı kapalı renk yap
    }

    private boolean hasCameraFlash(PackageManager packageManager) {
        Log.i("hasCameraFlash", "hasCameraFlash() a girildi.");
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH); // kamera özelligi varsa true dönderir.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
