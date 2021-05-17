package com.example.Port.ships;


import com.example.Port.enums.CargoType;
import com.example.Port.time.Time;

import java.security.InvalidAlgorithmParameterException;
import java.util.LinkedList;

public class ShipFactory
{
    public LinkedList<Ship> generateShipList(int maxCountOfShips)
    {
        int number = (int)(Math.random()*(maxCountOfShips-1) + 1);
        LinkedList<Ship> ships = new LinkedList<>();
        for (Integer index = 0; index<number; index++)
        {
            Ship newShip = new Ship(index.toString(),
                    generateCargoType(),
                    generateWeightInRange(4000, 100),
                    generateRandomDate());
            ships.add(newShip);
        }
        return ships;
    }

    public Time generateShipDevastationFromTheCheadle()
    {
        Time time = new Time();
        int randomDay = 0, randomHour = 0, randomMinutes = 0;
        if (generateDecision())
        {
            randomDay = (int)( Math.random()*7);
            randomHour = generateRandom24number();
            randomMinutes = generateRandom60number();
        }
        try
        {
            time =  new Time (randomDay, randomHour, randomMinutes);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    public Time generateShipUnloadDevastation()
    {
        Time time = new Time();
        int randomHour = 0, randomMinutes = 0;
        if (generateDecision())
        {
            randomHour = generateRandom24number();
            randomMinutes = generateRandom60number();
        }
        try
        {
            time = new Time(0, randomHour, randomMinutes);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.fillInStackTrace();
        }
        return time;
    }

    private boolean generateDecision()
    {
        return (int)(Math.random()*2) != 0;
    }

    private CargoType generateCargoType()
    {
        int num = (int)(Math.random()*3);
        CargoType output = CargoType.LOOSE;
        switch (num)
        {
            case 0:
                output = CargoType.LOOSE;
                break;
            case 1:
                output =  CargoType.LIQUID;
                break;
            case 2:
                output =  CargoType.CONTAINER;
                break;
        }
        return output;
    }

    private int generateWeightInRange(int max, int min)
    {
        return (int)((Math.random()*(max-min)) + min);
    }

    private Time generateRandomDate()
    {
        int randomDay = (int)( Math.random()*29 + 1);
        Time newDay = new Time();
        try{
            newDay = new Time(randomDay, generateRandom24number(), generateRandom60number());
        }
        catch(InvalidAlgorithmParameterException ex)
        {
            ex.fillInStackTrace();
        }
        return newDay;
    }

    private int generateRandom60number()
    {
        return (int)( Math.random()*60-1);
    }

    private int generateRandom24number()
    {
        return (int)( Math.random()*24-1);
    }
}
