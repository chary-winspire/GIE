package com.example.extarc.androidpushnotification;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),this, year, month, date);

        long today = System.currentTimeMillis();
        final long oneDay = 24 * 60 * 60 * 1000L;

        dpd.getDatePicker().setMinDate(today - 7 * oneDay);
//        dpd.getDatePicker().setMaxDate((today + 30 * oneDay));

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
//        dpd.getDatePicker().setMinDate(System.currentTimeMillis());

        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Toast.makeText(getActivity(), "Selected Date: " + view.getYear() + "/" + (view.getMonth() + 1) + "/" + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Selected Date: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();

    }
}
