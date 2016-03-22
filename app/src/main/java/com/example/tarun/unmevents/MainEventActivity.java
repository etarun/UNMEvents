package com.example.tarun.unmevents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thomashaertel.widget.MultiSpinner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainEventActivity extends AppCompatActivity {


    ArrayList<Event> eventsList = new ArrayList<>();

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String urlString = "http://datastore.unm.edu/events/events.xml";
    Button unmEvents;
    Button abqEvents;
    Button athletics;
    TableLayout t1;
    ImageView noConn;
    TextView noConn1;
    Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_eventactivity);
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.logoicon1);
        menu.setDisplayUseLogoEnabled(true);
        //TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        // create spinner list elements
        unmEvents = (Button) findViewById(R.id.unmevents);
        abqEvents = (Button) findViewById(R.id.abqevents);
        athletics = (Button) findViewById(R.id.atheltics);
        unmEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainEventActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        abqEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainEventActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        athletics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainEventActivity.this, AthleticActivity.class);
                startActivity(i);
            }
        });
    }
}
