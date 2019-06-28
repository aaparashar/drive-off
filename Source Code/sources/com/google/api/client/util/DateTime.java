package com.google.api.client.util;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class DateTime implements Serializable {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final long serialVersionUID = 1;
    private final boolean dateOnly;
    private final Integer tzShift;
    private final long value;

    public DateTime(Date date, TimeZone zone) {
        long value = date.getTime();
        this.dateOnly = false;
        this.value = value;
        this.tzShift = Integer.valueOf(zone.getOffset(value) / ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS);
    }

    public DateTime(long value) {
        this(false, value, null);
    }

    public DateTime(Date value) {
        this(value.getTime());
    }

    public DateTime(long value, Integer tzShift) {
        this(false, value, tzShift);
    }

    public DateTime(boolean dateOnly, long value, Integer tzShift) {
        this.dateOnly = dateOnly;
        this.value = value;
        this.tzShift = tzShift;
    }

    public long getValue() {
        return this.value;
    }

    public boolean isDateOnly() {
        return this.dateOnly;
    }

    public Integer getTimeZoneShift() {
        return this.tzShift;
    }

    public String toStringRfc3339() {
        StringBuilder sb = new StringBuilder();
        Calendar dateTime = new GregorianCalendar(GMT);
        long localTime = this.value;
        Integer tzShift = this.tzShift;
        if (tzShift != null) {
            localTime += tzShift.longValue() * 60000;
        }
        dateTime.setTimeInMillis(localTime);
        appendInt(sb, dateTime.get(1), 4);
        sb.append('-');
        appendInt(sb, dateTime.get(2) + 1, 2);
        sb.append('-');
        appendInt(sb, dateTime.get(5), 2);
        if (!this.dateOnly) {
            sb.append('T');
            appendInt(sb, dateTime.get(11), 2);
            sb.append(':');
            appendInt(sb, dateTime.get(12), 2);
            sb.append(':');
            appendInt(sb, dateTime.get(13), 2);
            if (dateTime.isSet(14)) {
                sb.append('.');
                appendInt(sb, dateTime.get(14), 3);
            }
        }
        if (tzShift != null) {
            if (tzShift.intValue() == 0) {
                sb.append('Z');
            } else {
                int absTzShift = tzShift.intValue();
                if (tzShift.intValue() > 0) {
                    sb.append('+');
                } else {
                    sb.append('-');
                    absTzShift = -absTzShift;
                }
                int tzMinutes = absTzShift % 60;
                appendInt(sb, absTzShift / 60, 2);
                sb.append(':');
                appendInt(sb, tzMinutes, 2);
            }
        }
        return sb.toString();
    }

    public String toString() {
        return toStringRfc3339();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DateTime)) {
            return false;
        }
        DateTime other = (DateTime) o;
        if (this.dateOnly == other.dateOnly && this.value == other.value) {
            return true;
        }
        return false;
    }

    public static DateTime parseRfc3339(String str) throws NumberFormatException {
        try {
            int tzIndex;
            Calendar dateTime = new GregorianCalendar(GMT);
            int year = Integer.parseInt(str.substring(0, 4));
            int month = Integer.parseInt(str.substring(5, 7)) - 1;
            int day = Integer.parseInt(str.substring(8, 10));
            int length = str.length();
            boolean dateOnly = length <= 10 || Character.toUpperCase(str.charAt(10)) != 'T';
            if (dateOnly) {
                dateTime.set(year, month, day);
                tzIndex = 10;
            } else {
                dateTime.set(year, month, day, Integer.parseInt(str.substring(11, 13)), Integer.parseInt(str.substring(14, 16)), Integer.parseInt(str.substring(17, 19)));
                if (str.charAt(19) == '.') {
                    dateTime.set(14, Integer.parseInt(str.substring(20, 23)));
                    tzIndex = 23;
                } else {
                    dateTime.set(14, 0);
                    tzIndex = 19;
                }
            }
            Integer tzShiftInteger = null;
            long value = dateTime.getTimeInMillis();
            if (length > tzIndex) {
                int tzShift;
                if (Character.toUpperCase(str.charAt(tzIndex)) == 'Z') {
                    tzShift = 0;
                } else {
                    tzShift = (Integer.parseInt(str.substring(tzIndex + 1, tzIndex + 3)) * 60) + Integer.parseInt(str.substring(tzIndex + 4, tzIndex + 6));
                    if (str.charAt(tzIndex) == '-') {
                        tzShift = -tzShift;
                    }
                    value -= (long) (ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS * tzShift);
                }
                tzShiftInteger = Integer.valueOf(tzShift);
            }
            return new DateTime(dateOnly, value, tzShiftInteger);
        } catch (StringIndexOutOfBoundsException e) {
            throw new NumberFormatException("Invalid date/time format.");
        }
    }

    private static void appendInt(StringBuilder sb, int num, int numDigits) {
        if (num < 0) {
            sb.append('-');
            num = -num;
        }
        int x = num;
        while (x > 0) {
            x /= 10;
            numDigits--;
        }
        for (int i = 0; i < numDigits; i++) {
            sb.append('0');
        }
        if (num != 0) {
            sb.append(num);
        }
    }
}
