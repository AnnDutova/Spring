package com.example.Port.schedule;

import com.example.Port.comparators.DataComparator;
import com.example.Port.ships.Ship;
import com.example.Port.ships.ShipFactory;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class TimeTable
{
    private List<Ship> timeTable = new ArrayList<>();
    private ShipFactory fabrics = new ShipFactory();
    public final int MAX_OF_SHIPS = 80;

    public TimeTable(){
        generateTimeTable();
    }

    public void generateTimeTable()
    {
        fillTimeTable(fabrics.generateShipList(MAX_OF_SHIPS));
    }

    private void fillTimeTable(LinkedList<Ship> ships){
        timeTable.addAll(ships);
        timeTable.sort(new DataComparator());
    }

    public void print()
    {
        for (Ship ship: timeTable)
        {
            System.out.println(ship.toString());
            System.out.println();
        }
    }

    public String returnJsonObject()
    {
        return new Gson().toJson(timeTable);
    }


}

