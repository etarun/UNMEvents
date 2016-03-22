package com.example.tarun.unmevents;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tarun on 3/16/2016.
 */
public class EventActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    Intent mShareIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.logoicon1);
        menu.setDisplayUseLogoEnabled(true);
        TextView eventName = (TextView) findViewById(R.id.eventName);
        TextView eventdate = (TextView) findViewById(R.id.eventDate1);
        TextView eventtime = (TextView) findViewById(R.id.eventDate2);
        TextView eventDescription = (TextView) findViewById(R.id.eventDesc);
        TextView eventDesc = (TextView) findViewById(R.id.eventDesc1);
        TextView eventCategory = (TextView) findViewById(R.id.eventCategory1);
        TextView location = (TextView) findViewById(R.id.eventLoc);
        TextView eventLoc = (TextView) findViewById(R.id.eventLoc1);
        TextView eventClass = (TextView) findViewById(R.id.eventClass1);
        TextView eClass = (TextView) findViewById(R.id.eventClass);
        TextView eventenddate = (TextView) findViewById(R.id.eventEndDate1);
        TextView eventEndtime = (TextView) findViewById(R.id.eventEndDate2);
        TextView org = (TextView) findViewById(R.id.org);
        Button cal = (Button) findViewById(R.id.filter);
        ImageButton mail = (ImageButton) findViewById(R.id.mail);

        Bundle data = getIntent().getExtras();
        final Event event = (Event) data.getParcelable("event");
        final String atletic = data.getString("athletic");
        if(event!=null) {
            String time = "All Day";
            SimpleDateFormat eDate = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat eDate1 = new SimpleDateFormat("MM-dd-yyyy");
            if(event.getSummary()!= null) {
                eventName.setText(event.getSummary());
            }
            if(event.getStartTime() != null) {
                eventdate.setText(eDate1.format(event.getStartTime()));
                if(atletic != null && atletic.matches("no")) {
                    if (eDate.format(event.getStartTime()).matches("00:00:00")) {
                        eventtime.setText(time);
                    } else {
                        eventtime.setText(eDate.format(event.getStartTime()));
                    }
                }else{
                    eventtime.setText(event.getDescription().split("-")[1]);
                }
            }
            if(event.getEndTime() != null) {
                eventenddate.setText(eDate1.format(event.getEndTime()));
                if(atletic != null && atletic.matches("no")) {
                    if (eDate.format(event.getEndTime()).matches("00:00:00")) {
                        eventEndtime.setText(time);
                    } else {
                        eventEndtime.setText(eDate.format(event.getEndTime()));
                    }
                }
            }
            String delimiter = "\\\\n";
            String [] desc = event.getDescription().split(delimiter);
            if(atletic != null && atletic.matches("yes")){
                eventDesc.setText(event.getDescription().split("-")[0]);
            }else {
                if (desc[1].matches("")) {
                    eventDescription.setVisibility(View.GONE);
                    eventDesc.setVisibility(View.GONE);
                } else {
                    eventDesc.setText(desc[1]);
                }
            }
            eventCategory.setText(event.getCategory());
            if(atletic != null && atletic.matches("no")) {
                eventLoc.setText(event.getLocation());
                eventClass.setText(event.getEventClass());
                mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {event.getOrganizer().split(":")[1]});
                        intent.putExtra(Intent.EXTRA_SUBJECT, event.getSummary());
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    }
                });
            }else{
                mail.setVisibility(View.GONE);
                org.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                eClass.setVisibility(View.GONE);
                eventLoc.setVisibility(View.GONE);
                eventClass.setVisibility(View.GONE);
            }
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
            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_SUBJECT, event.getSummary());
            if(atletic != null && atletic.matches("yes")){
                mShareIntent.putExtra(Intent.EXTRA_TEXT, event.getDescription().split("-")[0] + "\n\n"+
                          "Start Date :" + eDate1.format(event.getStartTime())
                        + "\n" + "Start Time :" + event.getDescription().split("-")[1]);
            }else {
                mShareIntent.putExtra(Intent.EXTRA_TEXT, desc[1] + "\n\n" + "Start Date :" + eDate1.format(event.getStartTime())
                        + "\n" + "Start Time :" + eDate.format(event.getStartTime()));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = new ShareActionProvider(this);
        MenuItemCompat.setActionProvider(item, mShareActionProvider);
        mShareActionProvider.setShareIntent(mShareIntent);
        // Return true to display menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                //onShareAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void onShareAction(){
        // Create the share Intent
        // Set the share Intent
        if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(mShareIntent);
        }
    }
}
