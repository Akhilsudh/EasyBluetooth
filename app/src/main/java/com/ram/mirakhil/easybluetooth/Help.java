package com.ram.mirakhil.easybluetooth;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;


public class Help extends ActionBarActivity {

    WebView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tv = (WebView) findViewById(R.id.textView);

        loadText();
    }

    private void loadText() {
         String customHtml ="<html><body><h3>Prioritising paired Bluetooth devices</h3><p>Click on the \n" +
                 "&quot;Manage Bluetooth Priority&quot; button on the main screen, you will be taken to \n" +
                 "an interactive list of paired devices. To rearrange the order, you have to long press \n" +
                 "the device in the list and drag the device name over other devices of the list and \n" +
                 "place them on the required order.</p><h3>Connecting to devices based on \n" +
                 "priority</h3><p>After setting the priority of your paired devices, you can connect to \n" +
                 "the device with the highest priority in range by clicking the interactive button on \n" +
                 "the lower right hand corner of the main screen. The app also does this process \n" +
                 "automatically during start-up.</p><h3>Adding New devices to the priority \n" +
                 "list</h3><p>In the priority screen you can add new unpaired devices to the list by \n" +
                 "clicking the &quot;+&quot; button on the lower right hand corner of the screen. You \n" +
                 "will be directed to the Bluetooth Settings page where you can pair new devices. NOTE- \n" +
                 "In case the newly added device doesn't show up in your list, then please click the \n" +
                 "&quot;Reset List&quot; option in the menu options, this resets the list's priority \n" +
                 "order to system defaults.</p><hr><p>Any other queries or requests can be mailed to akhil.sudh@gmail.com</p></body></html>\n";
        tv.loadData(customHtml, "text/html", "UTF-8");
    }
}
