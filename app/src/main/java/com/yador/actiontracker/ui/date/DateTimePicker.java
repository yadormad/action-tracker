package com.yador.actiontracker.ui.date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;

public class DateTimePicker {

    public interface DateTimePickerCallback {
        void onDatePicked(Date date);
    }

    private Calendar dateAndTime;
    private Context context;

    private TimePickerDialog.OnTimeSetListener timeListener;

    private DatePickerDialog.OnDateSetListener dateListener;

    public DateTimePicker(Context context, final DateTimePickerCallback dateTimePickerCallback) {
        this.context = context;
        dateAndTime = Calendar.getInstance();

        timeListener = (view, hourOfDay, minute) -> {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            dateTimePickerCallback.onDatePicked(dateAndTime.getTime());
        };

        dateListener = (view, year, monthOfYear, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showTimeDialog();
        };
    }

    public void showDateDialog() {
        new DatePickerDialog(context, dateListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    private void showTimeDialog() {
        new TimePickerDialog(context, timeListener,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
}
