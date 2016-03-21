package com.example.tarun.unmevents;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.thomashaertel.widget.MultiSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Tarun on 3/17/2016.
 */
public class FilterActivity extends AppCompatActivity {
    private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;
    Set<String> categorySet = new ArraySet<>();
    ArrayList<Event> filteredEvents = new ArrayList<>();
    String currentDate = "";
    Boolean searched = false;
    Button dateSelect;
    Button search;
    ArrayList<Event> events;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.logoicon1);
        menu.setDisplayUseLogoEnabled(true);
        Bundle data = getIntent().getExtras();
         events = data.getParcelableArrayList("events");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (Event e: events){
            categorySet.add(e.getCategory());
        }

        adapter.addAll(categorySet);

        // get spinner and set adapter
        spinner = (MultiSpinner) findViewById(R.id.spinnerMulti);
        spinner.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[0] = true; // select second item
        spinner.setSelected(selectedItems);
        //Date
        dateSelect = (Button) findViewById(R.id.dateSelect);

        currentDate = sdf.format(new Date());
        dateSelect.setText("Select Date");
        dateSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searched = true;
                DateDialog dialog = new DateDialog(v);
                DialogFragment dialogFragment = new DateDialog(v);
                dialogFragment.show(getSupportFragmentManager(), "start_date_picker");
            }
        });


        search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = dateSelect.getText().toString();
                if(!selectedDate.matches("Select Date")) {
                    Date date = new Date();
                    try {
                        date = sdf.parse(selectedDate);
                        for (Event e : events) {
                            if (sdf.format(e.getStartTime()).equals(selectedDate)) {
                                filteredEvents.add(e);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(searched == false){
                    filteredEvents = events;
                }
                Intent output = new Intent();
                output.putExtra("filteredEvents", filteredEvents);
                setResult(99, output);
                finish();
            }
        });
    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            searched = true;
            for(int i =0;i<selected.length;i++){
                if(selected[i] == true){
                    String category = adapter.getItem(i);
                    for(Event e:events){
                        if(e.getCategory().matches(category)){
                            filteredEvents.add(e);
                        }
                    }
                }
            }
        }
    };
}
