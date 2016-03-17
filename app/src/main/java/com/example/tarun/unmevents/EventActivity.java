package com.example.tarun.unmevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Tarun on 3/16/2016.
 */
public class EventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        EditText text1 = (EditText) findViewById(R.id.text1);
        Bundle data = getIntent().getExtras();
        Event event = (Event) data.getParcelable("event");
        if(event!=null) {
            text1.setText(event.getId());
        }else{
            text1.setText("empty");
        }
    }
}
