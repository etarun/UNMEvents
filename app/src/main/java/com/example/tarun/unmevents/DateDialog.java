package com.example.tarun.unmevents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * Created by etarun on 10/16/2015.
 */
public class DateDialog extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {
    Button dateSelect;

    public DateDialog(View view){
        dateSelect=(Button) view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {


// Use the current date as the default date in the dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }
    public String date ;
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box

        date=day+"-"+(month+1)+"-"+year;

        String date=checkDigit(month+1)+"-"+day+"-"+year;

        dateSelect.setText(date);

    }
    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }

}
