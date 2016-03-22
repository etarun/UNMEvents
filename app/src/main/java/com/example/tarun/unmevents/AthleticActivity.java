package com.example.tarun.unmevents;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class AthleticActivity extends AppCompatActivity {


    ArrayList<Event> eventsList = new ArrayList<>();

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String urlString = "http://datastore.unm.edu/events/unm-athletics.rss";
    Button filter;
    TableLayout t1;
    ImageView noConn;
    TextView noConn1;
    Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletic);
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.logoicon1);
        menu.setDisplayUseLogoEnabled(true);
        //TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        // create spinner list elements
        noConn = (ImageView)findViewById(R.id.noConn);
        noConn1 = (TextView)findViewById(R.id.noconn1);
        //filter = (Button) findViewById(R.id.filter);
        retry = (Button) findViewById(R.id.retry);
        if(isNetworkStatusAvialable (getApplicationContext())) {
            new DownloadXmlTask(this).execute(urlString);
            noConn.setVisibility(View.GONE);
            noConn1.setVisibility(View.GONE);
            retry.setVisibility(View.GONE);
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            noConn.setVisibility(View.VISIBLE);
            noConn1.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkStatusAvialable (getApplicationContext())) {
                        new DownloadXmlTask(AthleticActivity.this).execute(urlString);
                        noConn.setVisibility(View.GONE);
                        noConn1.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);
                        //filter.setVisibility(View.VISIBLE);
                    }
                }
            });
            //filter.setVisibility(View.GONE);
        }



    }
    class DownloadXmlTask extends AsyncTask<String, Object, List<Event>> {
        NodeList nodelist;
        ArrayList<Event> events = new ArrayList<>();
        public AthleticActivity activity;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        public DownloadXmlTask(AthleticActivity a)
        {
            this.activity = a;
        }

        @Override
        protected ArrayList<Event> doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("item");

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> result) {
            SimpleDateFormat eDate1 = new SimpleDateFormat("MM/dd/yyyy");
            ArrayList<Event> events = new ArrayList<>();
            for (int i = 0; i < nodelist.getLength(); i++) {
                Event aEvent = new Event();
                Element event = (Element) nodelist.item(i);

                //Title
                NodeList titleNode = event.getElementsByTagName("event:title");
                Element TitleEle = (Element) titleNode.item(0);
                String name = TitleEle.getFirstChild().getNodeValue();

                aEvent.setSummary(TitleEle.getFirstChild().getNodeValue());

                //StartTime
                NodeList startDate = event.getElementsByTagName("event:startdate");
                Element startDateEle = (Element) startDate.item(0);
                try {
                    if(startDateEle!= null)
                    aEvent.setStartTime(eDate1.parse(startDateEle.getFirstChild().getNodeValue()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //EndTime
                NodeList endDate = event.getElementsByTagName("event:enddate");
                Element endDateEle = (Element) endDate.item(0);
                try {
                    if(startDateEle!= null)
                        aEvent.setEndTime(eDate1.parse(endDateEle.getFirstChild().getNodeValue()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                NodeList desc = event.getElementsByTagName("description");
                Element descEle = (Element) desc.item(0);
                if(descEle != null) {
                    String description = descEle.getFirstChild().getNodeValue();
                    description = description.replace("<p>","");
                    description = description.replace("</p>","");
                    aEvent.setDescription(description);
                }
                NodeList catname = event.getElementsByTagName("category");
                Element catnameEle = (Element) catname.item(0);
                if(catnameEle != null)
                    aEvent.setCategory(catnameEle.getFirstChild().getNodeValue());
                events.add(aEvent);
            }
            CustomListAdapter adapter=new CustomListAdapter(activity, events);
            ListView list =(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(AthleticActivity.this, EventActivity.class);
                    i.putExtra("event", (Parcelable) parent.getItemAtPosition(position));
                    i.putExtra("athletic","yes");
                    startActivity(i);
                }
            });
        }
        // getNode function
        private String getNode(String sTag, Element eElement) {
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue = (Node) nlList.item(1);
            Node nText = (Node) nValue.getLastChild();
            return nText.getNodeValue();
        }
        public Date getdate(String sTag, Element eElement) {
            SimpleDateFormat eDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat eDate1 = new SimpleDateFormat("yyyy-MM-dd");
            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue = (Node) nlList.item(1);
            Node nText = (Node) nValue.getLastChild();
            try {
            if(nValue.getNodeName().matches("date-time")) {
                return eDate.parse(nText.getNodeValue());
            }else{
                return eDate1.parse(nText.getNodeValue());
            }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void setList(List<Event> events) {
        for (Event e: events){
            eventsList.add(e);
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && data != null) {
            ArrayList<Event> filteredEvents = new ArrayList<>();
            filteredEvents = data.getParcelableArrayListExtra("filteredEvents");
            CustomListAdapter adapter=new CustomListAdapter(AthleticActivity.this, filteredEvents);
            ListView list =(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);
        }
    }*/
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
}
