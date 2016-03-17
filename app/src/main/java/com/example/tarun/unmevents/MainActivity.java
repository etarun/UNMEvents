package com.example.tarun.unmevents;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thomashaertel.widget.MultiSpinner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {
    private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;
    Set<String> categorySet = new ArraySet<>();
    String currentDate = "";
    List<Event> eventsList = new ArrayList<>();
    Button dateSelect;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String urlString = "http://datastore.unm.edu/events/events.xml";
    TableLayout t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TableLayout tl = (TableLayout) findViewById(R.id.main_table);
        // create spinner list elements
        new DownloadXmlTask(this).execute(urlString);

       /* dateSelect = (Button) findViewById(R.id.dateSelect);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        currentDate = sdf.format(new Date());
        dateSelect.setText(currentDate);
        dateSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                DialogFragment dialogFragment = new DateDialog(v);
                dialogFragment.show(getSupportFragmentManager(), "start_date_picker");
            }
        });*/
    }
    class DownloadXmlTask extends AsyncTask<String, Object, List<Event>> {
        NodeList nodelist;
        List<Event> events = new ArrayList<>();
        public MainActivity activity;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        public DownloadXmlTask(MainActivity a)
        {
            this.activity = a;
        }

        @Override
        protected List<Event> doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("vevent");

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> result) {

            for (int temp = 0; temp < nodelist.getLength(); temp++) {
                Event event = new Event();
                Node nNode = nodelist.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    event.setId(getNode("uid", eElement));
                    event.setDescription(getNode("description", eElement));
                    event.setSummary(getNode("summary", eElement));
                    event.setCategory(getNode("categories", eElement));
                    event.setEventClass(getNode("class", eElement));
                    event.setLocation(getNode("location", eElement));
                    event.setOrganizer(getNode("organizer", eElement));
                    //Time
                    event.setStartTime(getdate("dtstart", eElement));
                    event.setEndTime(getdate("dtend", eElement));
                    event.setCreateTime(getdate("dtstamp", eElement));
                }
                events.add(event);
            }
            CustomListAdapter adapter=new CustomListAdapter(activity, events);
            ListView list =(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this, EventActivity.class);
                    i.putExtra("event", (Parcelable) parent.getItemAtPosition(position));
                    startActivity(i);
                }
            });

           /* TextView[] textArray = new TextView[events.size()];
            TextView[] textArray2 = new TextView[events.size()];
            TableRow[] tr_head = new TableRow[events.size()];

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            int i=0;
            for(Event e:events){
                tr_head[i] = new TableRow(getApplicationContext());
                tr_head[i].setId(i + 1);
                tr_head[i].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // Here create the TextView dynamically

                textArray[i] = new TextView(getApplicationContext());
                textArray[i].setId(i + 111);
                textArray[i].setText(e.getSummary());
                textArray[i].setTextSize(16);
                textArray[i].setTextColor(Color.BLACK);
                textArray[i].setPadding(5, 20, 5, 20);

                Date date = null;
                String d = "";
                if(e.getStartTime()!=null) {
                    date = e.getStartTime();
                    d = df.format(date);
                }

                textArray2[i] = new TextView(getApplicationContext());
                textArray2[i].setId(i + 222);
                textArray2[i].setText(d);
                textArray2[i].setTextColor(Color.BLACK);
                textArray2[i].setTextSize(16);
                textArray2[i].setPadding(5, 20, 5, 20);
                tr_head[i].addView(textArray[i]);
                tr_head[i].addView(textArray2[i]);
                tr_head[i].setBackgroundResource(R.drawable.row_border);
                tl.addView(tr_head[i], new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

            }*/

            /*adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item);
            for (Event e: events){
                categorySet.add(e.getCategory());
            }
            adapter.addAll(categorySet);
            activity.setList(events);
            // get spinner and set adapter
            spinner = (MultiSpinner) findViewById(R.id.spinnerMulti);
            spinner.setAdapter(adapter, false, onSelectedListener);
            // set initial selection
            boolean[] selectedItems = new boolean[adapter.getCount()];
            selectedItems[1] = true; // select second item
            spinner.setSelected(selectedItems);*/
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
            if(nText.getNodeName() == "date-time") {
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
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items
        }
    };
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
