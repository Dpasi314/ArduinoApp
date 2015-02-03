package com.atomic.ArduinoApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import com.atomic.ArduinoApp.Handlers.DatabaseHandler;
import com.atomic.ArduinoApp.Handlers.DatabaseHelper;
import com.atomic.ArduinoApp.Intelligence.IntelligenceCore;
import com.atomic.ArduinoApp.Managers.DeviceManager;
import com.atomic.ArduinoApp.Values.Action;

public class BaseActivity extends Activity implements CompoundButton.OnCheckedChangeListener{
    /**
     * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
     */

    DeviceManager dm = new DeviceManager(this);
    String TAG = "HEYDANTELOOKHERE";
    static DatabaseHandler db = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SeekBar bar = (SeekBar) findViewById(R.id.temp_val);
        Switch s = (Switch) findViewById(R.id.lights);

        if(s != null) s.setOnCheckedChangeListener(this);

        s.setTextOff("OFF");
        s.setTextOn("ON");

        final TextView value_temp = (TextView) findViewById(R.id.temp_status);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value_temp.setText(String.valueOf(progress) + " F");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dm.findDevice();



        db = new DatabaseHandler(getApplicationContext(), this);
        DatabaseHelper dh = new DatabaseHelper();

        //dh.addRandomValues();


        IntelligenceCore core = new IntelligenceCore(Action.MOTION);
        core.scoreConfidence();
        Log.d(TAG, core.getScore() + "%");


    }

    public static DatabaseHandler getDatabaseHandler() {
        return db;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public void onCheckedChanged(CompoundButton button, boolean checked) {
        Toast.makeText(this, "Lights: " + (checked ? "Enabled" : "Disabled"),
                Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.version:
                showToast(getString(R.string.version));
                break;
            case R.id.license:
                showPopup("ArduinoApp", "ArduinoApp is protected under the GNUv3 license. Full license is provided in the assets portion of the code." +
                        "\n\n (c) Atomic Development - 2015", "OK");
                break;
            default:
                super.onOptionsItemSelected(item);

        }

        return true;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();
    }

    private void showPopup(String title, String message, String button_name) {
        final AlertDialog a = new AlertDialog.Builder(this).create();
        a.setTitle(title);
        a.setMessage(message);


        a.setButton(button_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                a.dismiss();
            }
        });

        a.setIcon(R.drawable.ic_launcher);
        a.show();
    }
}



