package com.example.Port.schedule;


import com.example.Port.comparators.DataComparator;
import com.example.Port.comparators.TaskComparator;
import com.example.Port.ships.DockedShip;
import com.example.Port.ships.Ship;
import com.example.Port.ships.Task;
import com.example.Port.threads.Statistic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TimeTableJSONParser
{
    public static final String JSON_FILE_NAME = "timeTable.json";
    public static final String JSON_STATISTIC_FILE_NAME = "statistic.json";

    public void writeJSON(TimeTable timeTable)throws IOException
    {
        List<DockedShip> ships = new ArrayList<>();
        for (int index = 0; index < timeTable.getSize(); index++)
        {
            Ship current = timeTable.get(index);
            ships.add(new DockedShip(current.getName(), current.getCargoType(), current.getCargoWeight(), current.getComingTime()));
        }
        try(FileWriter file = new FileWriter(JSON_FILE_NAME))
        {
            file.write(new Gson().toJson(ships));
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeStringToFile(String timeTable)
    {
        Type listType = new TypeToken<ArrayList<Ship>>(){}.getType();
        ArrayList<Ship> ships = new Gson().fromJson(timeTable, listType);

        List<DockedShip> generatedShips = new ArrayList<>();
        for (int index = 0; index < ships.size(); index++)
        {
            Ship current = ships.get(index);
            generatedShips.add(new DockedShip(current.getName(), current.getCargoType(), current.getCargoWeight(), current.getComingTime()));
        }
        try(FileWriter file = new FileWriter(JSON_FILE_NAME))
        {
            file.write(new Gson().toJson(generatedShips));
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void writeStatistic(String statistic)
    {
        Type listType = new TypeToken<Statistic>(){}.getType();
        Statistic stats = new Gson().fromJson(statistic, listType);

        try(FileWriter file = new FileWriter(JSON_STATISTIC_FILE_NAME))
        {
            file.write(new Gson().toJson(stats));
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public ArrayList<DockedShip> readJSON(String path)
    {
        if (path == null)
        {
            path = JSON_FILE_NAME;
        }
        ArrayList<DockedShip> tasker = new ArrayList<>();
        try (FileReader reader = new FileReader(path))
        {
            Type listType = new TypeToken<ArrayList<DockedShip>>(){}.getType();
            tasker = new Gson().fromJson(reader, listType);
        }
        catch (FileNotFoundException notFoundException)
        {
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        tasker.sort(new DataComparator());
        return tasker;
    }

    public ArrayList<Task> readJSONTask(String path){
        ArrayList<DockedShip> readed = readJSON(path);
        ArrayList<Task> tasker = new ArrayList<>();
        for (DockedShip ship: readed)
        {
            Task task = new Task(ship);
            tasker.add(task);
        }
        tasker.sort(new TaskComparator());
        return tasker;
    }

/*
    private void parseTimeTableObject(Object cell, ArrayList tasker)
    {
        String invalidTimeBySchedule = (String) cell.get("Time");
        Time timeBySchedule = parseString(invalidTimeBySchedule);

        JSONObject shipObject = (JSONObject) cell.get("Ship");

        String shipName = (String) shipObject.get("Name");

        Long invalidCargoWeight = (Long) shipObject.get("CargoWeight");
        int cargoWeight = invalidCargoWeight.intValue();

        String invalidType = (String) shipObject.get("CargoType");
        CargoType type = parseCargoType(invalidType);

        String invalidComingTime = (String) shipObject.get("Coming time");
        Time comingTime = parseString(invalidComingTime);

        String invalidDevastation = (String) shipObject.get("Devastation");
        Time devastation = parseString(invalidDevastation);

        timeBySchedule.plus(devastation);

        String invalidUnloadingDevastation = (String) shipObject.get("Unload wait");
        Time unloadingDevastation = parseString(invalidUnloadingDevastation);

        DockedShip ship = new DockedShip(shipName, type, cargoWeight, comingTime, devastation, unloadingDevastation);
        tasker.add(new Task(ship));
    }

    private Time parseString(String invalidTime)
    {
        String[] numbers = invalidTime.split(":");
        Time time = new Time();
        try {
            int day = Integer.parseInt(numbers[0]);
            int hour = Integer.parseInt(numbers[1]);
            int minutes = Integer.parseInt(numbers[2]);
            time = new Time(day, hour, minutes);
        }
        catch(InvalidAlgorithmParameterException ex)
        {
            ex.fillInStackTrace();
        }
        return time;
    }

    private CargoType parseCargoType(String invalid)
    {
        CargoType output = CargoType.LOOSE;
        switch(invalid){
            case "LOOSE":
                output = CargoType.LOOSE;
                break;
            case "LIQUID":
                output =  CargoType.LIQUID;
                break;
            case "CONTAINER":
                output =  CargoType.CONTAINER;
                break;
        }
        return output;
    }
*/

}
