package com.isitopen;

import java.util.Calendar;
import java.util.HashMap;

import static com.isitopen.ClosedReason.FREE_SUNDAY;
import static com.isitopen.ClosedReason.HOLIDAY;

public class ClosedDays {
    private final static HashMap<ClosedDay, ClosedReason> closedDays = new HashMap<>();

    static {
        closedDays.put(new ClosedDay(1, 1, 2018), HOLIDAY);
        closedDays.put(new ClosedDay(1, 7, 2018), FREE_SUNDAY);
    }

    public static HashMap<ClosedDay, ClosedReason> getClosedDays(){
        return closedDays;
    }
}

class ClosedDay{
    private final int day;
    private final int month;
    private final int year;

    public ClosedDay(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static ClosedDay of(Calendar calendar) {
        return new ClosedDay(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClosedDay closedDay = (ClosedDay) o;

        if (day != closedDay.day) return false;
        if (month != closedDay.month) return false;
        return year == closedDay.year;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + month;
        result = 31 * result + year;
        return result;
    }
}

enum ClosedReason {
    HOLIDAY("Swięto"), FREE_SUNDAY("Wolna niedziela");

    private final String info;

    ClosedReason(String info) {
        this.info = info;
    }
}
