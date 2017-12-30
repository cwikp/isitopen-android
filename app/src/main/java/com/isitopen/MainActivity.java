package com.isitopen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final Locale plLocale = new Locale("pl_PL");
    private Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        currentCalendar = Calendar.getInstance(plLocale);
        setCurrentDate();
        setOpenClosedInfo();
    }

    private void setCurrentDate() {
        TextView dateText = (TextView) findViewById(R.id.dateText);
        TextView weekDayText = (TextView) findViewById(R.id.weekdayText);
        dateText.setText(getCurrentDate(currentCalendar, plLocale));
        weekDayText.setText(getCurrentWeekday(currentCalendar, plLocale));
    }

    //    https://stackoverflow.com/questions/14832151/how-to-get-month-name-from-calendar
    private String getCurrentDate(Calendar calendar, Locale locale) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        int year = calendar.get(Calendar.YEAR);
        return String.format(locale, "%d %s %d", day, month, year);
    }

    private String getCurrentWeekday(Calendar calendar, Locale locale) {
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
    }

    private void setOpenClosedInfo() {
        String mainText = isOpen() ? getString(R.string.open) : getString(R.string.closed);
        TextView mainInfoText = (TextView) findViewById(R.id.mainInfoText);
        mainInfoText.setText(mainText);
    }

    private boolean isOpen() {
        HashMap<ClosedDay, ClosedReason> closedDays = ClosedDays.getClosedDays();
        ClosedDay possibleClosedDay = ClosedDay.of(currentCalendar);
        return !closedDays.keySet().contains(possibleClosedDay);
    }

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

    public void onDatePlusClick(View view) {
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentDay + 1);
        setCurrentDate();
    }

    public void onDateMinusClick(View view) {
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentDay - 1);
        setCurrentDate();
    }
}
