package project.oqha.wowo;
/**
 * Created by oqha on 7/14/15.
 */

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private boolean asem;
    XMLParser parser;
    Document doc;
    String xml;
    ListView lv;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> menuItems;
    ProgressDialog pDialog;

    private String URL = "http://api.androidhive.info/list_paging/?page=1";

    // XML node keys
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";

    // Flag for current page
    int current_page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button)findViewById(R.id.button1);
        startButton.setOnClickListener(this);

        Log.i("OqhaJect","Oject - Oqha Project@2015");

      lv = (ListView) findViewById(R.id.listView);

        menuItems = new ArrayList<HashMap<String, String>>();

        parser = new XMLParser();
        xml = parser.getXmlFromUrl(URL); // getting XML
        doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        // looping through all item nodes <item>
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_ID, parser.getValue(e, KEY_ID)); // id not using any where
            map.put(KEY_NAME, parser.getValue(e, KEY_NAME));

            // adding HashList to ArrayList
            menuItems.add(map);
        }


        // Getting adapter
//        adapter = new ListViewAdapter(this, menuItems);
  //      lv.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.button1)
        {

            Button startButton = (Button)findViewById(R.id.button1);

            if (startButton.getText().equals("START") ){

                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                startButton.setText("EXIT");
                asem=true;

            }
            else if (startButton.getText().equals("EXIT") ){
                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    asem=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (asem){
            Button startButton = (Button)findViewById(R.id.button1);
            startButton.setText("EXIT");
            asem=false;
        }


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
