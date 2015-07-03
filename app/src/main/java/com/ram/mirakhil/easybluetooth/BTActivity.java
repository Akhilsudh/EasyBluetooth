package com.ram.mirakhil.easybluetooth;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class BTActivity extends ActionBarActivity {

    TextView connectStatusText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        connectStatusText = (TextView) findViewById(R.id.connectedDeviceName);
        BTConnectThread bt = new BTConnectThread(this, false);
        Thread btThread = new Thread(bt);
        btThread.start();
        updateConnectionStatus();
    }




    public void updateConnectionStatus(){
        BTConnectThread bt2 = new BTConnectThread(this, true);
        Thread btThread2 = new Thread(bt2);
        btThread2.start();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bt, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(this, Help.class);
                startActivity(intent);
                // Help option clicked.
                return true;

            case R.id.action_exit:
                finish();
                System.exit(0);
                // Exit option clicked.
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void onPriorityButtonClick(View v) {
        Intent intent = new Intent(this, PriorityActivity.class);
        startActivity(intent);
    }



    public void onFAButtonClick(View v) throws InterruptedException {
        BTConnectThread obj=new BTConnectThread(this,false);
        Thread btThread = new Thread(obj);
        btThread.start();
        updateConnectionStatus();
    }
}
