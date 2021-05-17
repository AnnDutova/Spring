package com.example.Port.services;

import com.example.Port.schedule.TimeTable;
import com.example.Port.schedule.TimeTableJSONParser;
import com.example.Port.ships.DockedShip;
import com.example.Port.ships.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class ServiceTwo {
    private TimeTable timeTable;
    private TimeTableJSONParser parser = new TimeTableJSONParser();

    public ServiceTwo(TimeTable timeTable)
    {
        this.timeTable = timeTable;
    }

    public void start()
    {
        try {
            parser.writeJSON(timeTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String giveScheduleFileJSON(String path)
    {
        return returnJsonObject(parser.readJSON(path));
    }

    public String giveScheduleJSON()
    {
        return returnJsonObject(parser.readJSON(null));
    }

    private String returnJsonObject(List<DockedShip> tasker)
    {
        return new Gson().toJson(tasker);
    }
}
