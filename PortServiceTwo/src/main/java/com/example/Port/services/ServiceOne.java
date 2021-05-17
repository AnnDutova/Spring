package com.example.Port.services;

import com.example.Port.schedule.TimeTable;

public class ServiceOne
{
    private TimeTable timeTable = new TimeTable();

    public void start()
    {
        timeTable.generateTimeTable();
        timeTable.print();
    }

    public TimeTable giveSchedule()
    {
        return this.timeTable;
    }

    public String giveScheduleJSON()
    {
        return timeTable.returnJsonObject();
    }
}
