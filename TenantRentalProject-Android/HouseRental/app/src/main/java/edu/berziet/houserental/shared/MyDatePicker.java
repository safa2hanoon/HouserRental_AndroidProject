package edu.berziet.houserental.shared;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyDatePicker {

    private Context context;
    private int titleResId;
    private String dateFormat = "yyyy-MM-dd";
    public MyDatePicker(Context context, int titleResId) {
        this.context = context;
        this.titleResId = titleResId;
    }
    public void selectDate(final Calendar calendar, final EditText editText) {
        DatePickerDialog.OnDateSetListener odpl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                String dateStr = sdf.format(calendar.getTime());
                editText.setText(dateStr);
            }
        };
        DatePickerDialog dpd = new DatePickerDialog(context, odpl,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dpd.setTitle(titleResId);
        dpd.show();
    }

}
