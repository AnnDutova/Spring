package com.example.Port.time;


import java.security.InvalidAlgorithmParameterException;

public class Time implements Comparable<Time>
{
    public final static int MAX_DAYS = 30;
    public final static int MAX_HOURS = 24;
    public final static int MAX_MINUTES = 60;

    private int milliseconds;

    public Time(){ this.milliseconds = 0; }

    public Time(int day, int hour, int minutes) throws InvalidAlgorithmParameterException
    {
        if (day <= MAX_DAYS && day >= 0 )
        {
            milliseconds += day * MAX_HOURS * MAX_MINUTES;
        }
        else throw new InvalidAlgorithmParameterException("Only 30 days in a month");
        if (hour < MAX_HOURS && hour >= 0)
        {
            milliseconds += hour * MAX_MINUTES;
        }
        else throw new InvalidAlgorithmParameterException("Only 24 hours in a day");
        if (minutes < MAX_MINUTES && minutes >= 0)
        {
            milliseconds += minutes;
        }
        else throw new InvalidAlgorithmParameterException("Only 60 minutes in an hour");
    }

    public Time (int milliseconds) throws InvalidAlgorithmParameterException
    {
        if (milliseconds < MAX_MINUTES * MAX_HOURS * MAX_DAYS)
        {
            this.milliseconds = milliseconds;
        }
        else throw new InvalidAlgorithmParameterException("Invalid count of seconds");
    }

    public int getDay()
    {
        return milliseconds / MAX_MINUTES / MAX_HOURS;
    }

    public int getHour()
    {
        return (milliseconds - getDay() * MAX_MINUTES * MAX_HOURS) / MAX_MINUTES;
    }

    public int getMinutes()
    {
        return milliseconds - getDay() * MAX_MINUTES * MAX_HOURS - getHour() * MAX_MINUTES;
    }

    public int getTimeInSeconds()
    {
        return milliseconds;
    }

    public Time convertSecondsToTime (int milliseconds) throws InvalidAlgorithmParameterException
    {
        return new Time(milliseconds);
    }

    public Time getDifference(Time time2)
    {
        Time time = null;
        try {
            time = new Time(Math.abs(milliseconds - time2.milliseconds));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return time;
    }

    public Time plus(Time time2)
    {
        Time time = new Time();
        try
        {
            time = new Time(milliseconds + time2.milliseconds);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public Time plus(int wait) throws InvalidAlgorithmParameterException
    {
        return new Time(milliseconds + wait);
    }

    public Time minus(Time time2)
    {
        int newSeconds = milliseconds - time2.milliseconds;
        if (newSeconds < 0)
        {
            newSeconds = 0;
        }

        Time time = new Time();
        try
        {
            time = new Time(newSeconds);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public boolean bigger (Time time2) //time1 more if true
    {
        return milliseconds > time2.milliseconds;
    }

    @Override
    public String toString()
    {
        return getDay() + ":" + getHour() + ":" + getMinutes();
    }

    @Override
    public int compareTo(Time time2)
    {
        return Integer.compare(milliseconds, time2.milliseconds);
    }
}
