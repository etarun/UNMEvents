package com.example.tarun.unmevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tarun on 3/16/2016.
 */
public class EventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView eventdate = (TextView) findViewById(R.id.eventDate1);
        TextView eventtime = (TextView) findViewById(R.id.eventDate2);
        TextView eventDescription = (TextView) findViewById(R.id.eventDesc);
        TextView eventDesc = (TextView) findViewById(R.id.eventDesc1);
        TextView eventCategory = (TextView) findViewById(R.id.eventCategory1);
        TextView eventLoc = (TextView) findViewById(R.id.eventLoc1);
        TextView eventClass = (TextView) findViewById(R.id.eventClass1);
        TextView eventenddate = (TextView) findViewById(R.id.eventEndDate1);
        TextView eventEndtime = (TextView) findViewById(R.id.eventEndDate2);
        Button cal = (Button) findViewById(R.id.filter);

        Bundle data = getIntent().getExtras();
        final Event event = (Event) data.getParcelable("event");
        if(event!=null) {
            SimpleDateFormat eDate = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat eDate1 = new SimpleDateFormat("MM-dd-yyyy");
            eventName.setText(event.getSummary());
            eventdate.setText(eDate1.format(event.getStartTime()));
            String time = "All Day";
            if(eDate.format(event.getStartTime()).matches("00:00:00")){
                eventtime.setText(time);
            }else {
                eventtime.setText(eDate.format(event.getStartTime()));
            }
            eventenddate.setText(eDate1.format(event.getEndTime()));
            if(eDate.format(event.getEndTime()).matches("00:00:00")){
                eventEndtime.setText(time);
            }else {
                eventEndtime.setText(eDate.format(event.getEndTime()));
            }
            String delimiter = "\\\\n";
            String[] desc = event.getDescription().split(delimiter);
            if(desc[1].matches("")){
                eventDescription.setVisibility(View.GONE);
                eventDesc.setVisibility(View.GONE);
            }else{
                eventDesc.setText(desc[1]);
            }
            eventCategory.setText(event.getCategory());
            eventLoc.setText(event.getLocation());
            eventClass.setText(event.getEventClass());
            cal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", event.getStartTime().getTime());
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=DAILY");
                    intent.putExtra("endTime", event.getEndTime().getTime());
                    intent.putExtra("title", event.getSummary());
                    startActivity(intent);
                }
            });
        }
    }
}
