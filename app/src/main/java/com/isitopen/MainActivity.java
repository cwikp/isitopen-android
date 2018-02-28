package com.isitopen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final Locale polishLocale = new Locale("pl");
    private Calendar calendarToday;
    private Calendar calendarTomorrow;
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
        calendarToday = Calendar.getInstance(polishLocale);
        currentCalendar = Calendar.getInstance(polishLocale);
        calendarTomorrow = Calendar.getInstance(polishLocale);
        calendarTomorrow.set(Calendar.DAY_OF_MONTH, calendarToday.get(Calendar.DAY_OF_MONTH) + 1);
        updateAllDateInfo();
        if (currentCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            setClosestFutureSunday();
        }
    }

    private void setCurrentDate() {
        TextView dateText = (TextView) findViewById(R.id.dateText);
        TextView weekDayText = (TextView) findViewById(R.id.weekdayText);
        dateText.setText(getCurrentDate(currentCalendar, polishLocale));
        weekDayText.setText(getCurrentWeekday(currentCalendar, polishLocale));
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

    private void setOpenClosed() {
        boolean isOpen = isOpen();
        setInfo(isOpen);
        setBackgroundColor(isOpen);
        setImage(isOpen);
    }

    private void setInfo(boolean isOpen) {
        TextView openInfoText = (TextView) findViewById(R.id.openInfoText);
        String openInfo = isToday() ? getString(R.string.infoToday)
                : isTomorrow() ? getString(R.string.infoTomorrow)
                : isPastDate() ? getString(R.string.infoPast)
                : getString(R.string.infoFuture);
        openInfoText.setText(openInfo);

        TextView mainInfoText = (TextView) findViewById(R.id.mainInfoText);
        String mainText = isOpen ? getString(R.string.open) : getString(R.string.closed);
        mainInfoText.setText(mainText);
    }

    private boolean isTomorrow() {
        return ClosedDay.of(currentCalendar).equals(ClosedDay.of(calendarTomorrow));
    }

    private boolean isToday() {
        return ClosedDay.of(currentCalendar).equals(ClosedDay.of(calendarToday));
    }

    private boolean isPastDate() {
        return currentCalendar.before(calendarToday);
    }

    private void setBackgroundColor(boolean isOpen) {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);
        int backgroundColor = isOpen ? Color.parseColor("#0099cc") : Color.parseColor("#be3a41");
        layout.setBackgroundColor(backgroundColor);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setNavigationBarColor(backgroundColor);
            window.setStatusBarColor(backgroundColor);
        }
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setBackgroundDrawable(new ColorDrawable(backgroundColor));
        }
    }

    private void setImage(boolean isOpen) {
        ImageView image = (ImageView) findViewById(R.id.faceImage);
        int imageResource = isOpen ? R.drawable.happyface : R.drawable.sadface;
        image.setImageResource(imageResource);
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
        setClosestFutureSunday();
    }

    private void setClosestFutureSunday() {
        int difference = getDifferenceToSunday();
        currentCalendar.add(Calendar.DAY_OF_MONTH, difference);
        updateAllDateInfo();
    }

    public void onDateMinusClick(View view) {
        int difference = getDifferenceToSunday();
        currentCalendar.add(Calendar.DAY_OF_MONTH, -difference);
        updateAllDateInfo();
    }

    private int getDifferenceToSunday() {
        int difference = Calendar.SUNDAY - currentCalendar.get(Calendar.DAY_OF_WEEK);
        if (difference <= 0) {
            difference += 7;
        }
        return difference;
    }

    public void onMapInfoClicked(View view) {
        Toast.makeText(getApplicationContext(), R.string.mapInfoText, Toast.LENGTH_LONG).show();
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSelected(int year, int month, int day) {
        currentCalendar.set(year, month, day);
        updateAllDateInfo();
    }

    private void updateAllDateInfo() {
        setCurrentDate();
        setOpenClosed();
    }
}
