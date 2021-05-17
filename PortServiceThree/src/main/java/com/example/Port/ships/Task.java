package com.example.Port.ships;

import com.example.Port.enums.ShipState;
import com.example.Port.threads.Crane;
import com.example.Port.time.Time;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.Port.time.Time.MAX_HOURS;

public class Task {
    private final static int COST = 100;

    private final DockedShip ship;
    private Time unloadingTimeStart;
    private Time comingTime;
    private Time emptyTime;
    private Time delay;
    private boolean done;
    private ArrayList<Crane> executors = new ArrayList<>();

    private final Random random = new Random();

    public Task(DockedShip ship)
    {
        this.ship=ship;
        if (random.nextBoolean())
        {
            this.comingTime = ship.getComingTime().plus(ship.getDeviationFromTheSchedule());
        }
        else {
            this.comingTime = ship.getComingTime().minus(ship.getDeviationFromTheSchedule());
        }
        this.unloadingTimeStart = this.comingTime.plus(ship.getWaitingForUnloading());
        this.done = false;
    }

    Task(DockedShip ship, Time comingTime, Time unloadingTimeStart)
    {
        this.ship = ship;
        this.comingTime = comingTime;
        this.unloadingTimeStart = unloadingTimeStart;
        this.done = false;
    }

    Task(DockedShip ship, Time comingTime, Time unloadingTimeStart,Time emptyTime, boolean wasDone, Time delay, ArrayList<Crane> executors)
    {
        this.ship = ship;
        this.comingTime = comingTime;
        this.unloadingTimeStart = unloadingTimeStart;
        this.emptyTime = emptyTime;
        this.done = wasDone;
        this.delay = delay;
        this.executors = executors;
    }

    public DockedShip getShip()
    {
        return this.ship;
    }

    public Time getComingTime()
    {
        return this.comingTime;
    }

    public Time getUnloadingTimeStart()
    {
        return unloadingTimeStart;
    }

    public synchronized int getDelay()
    {
        if (delay!=null)
        {
            return delay.getTimeInSeconds();
        }
        return 0;
    }

    public Time getEmptyTime()
    {
        return this.emptyTime;
    }

    public int getExpenses()
    {
        if(delay!=null)
        {
            return delay.getHour() * COST + delay.getDay() * COST * MAX_HOURS;
        }
        else return 0;
    }

    public synchronized void unload(int weight)
    {
        if (!ship.getState().equals(ShipState.EMPTY))
        {
            ship.takePeaceOfCargo(weight);
            if (ship.getState().equals(ShipState.EMPTY))
            {
                this.done = true;
            }
        }
    }

    public synchronized boolean isDone()
    {
        return done;
    }

    public synchronized void setEmptyTime(Time time)
    {
        this.emptyTime = time;
        this.delay = emptyTime.getDifference(comingTime.plus(ship.getParkingTime()));
    }

    public synchronized void setEmptyTimeInt(int time)
    {
        Time newTime = new Time();
        try {
            newTime = newTime.convertSecondsToTime(time);
        } catch (InvalidAlgorithmParameterException invalidValue) {
            invalidValue.printStackTrace();
        }
        this.emptyTime = newTime;
        this.delay = emptyTime.getDifference(comingTime.plus(ship.getParkingTime()));
    }

    public synchronized void setUnloadingTimeStart(int newUnloadingStart){
        Time newTime = new Time();
        try {
            newTime = newTime.convertSecondsToTime(newUnloadingStart);
        } catch (InvalidAlgorithmParameterException invalidValue) {
            invalidValue.printStackTrace();
        }
        this.unloadingTimeStart = newTime;
    }

    public Task copy()
    {
        return new Task(this.ship.copy(), this.comingTime, this.unloadingTimeStart);
    }

    public Task copyForStatistic(int weight)
    {
        return new Task(this.ship.copyForStatistic(weight), comingTime, unloadingTimeStart, emptyTime, done, delay, executors);
    }

    public void setExecutors(Crane crane){
        executors.add(crane);
    }

    public int countOfExecutors(){
        return executors.size();
    }

    public boolean isExecutor(Crane crane){
        if (executors.contains(crane)){
            return true;
        }
        return false;
    }
    private String printExecutorNames(){
        if (executors.size() > 0)
        {
            if (executors.size() == 2)
            {
                return  executors.get(0).getName() + " "+ executors.get(1).getName();
            }
            else return executors.get(0).getName();
        }
        else return "No one";
    }

    @Override
    public String toString()
    {
        if (emptyTime != null)
        {
            return ship.toString() +
                    "\nDeviation: " + ship.getDeviationFromTheSchedule().toString()+
                    "\nComing time: (coming + deviation) " + comingTime.toString() +
                    "\nUnloading time start: (coming + CraneWait) " + unloadingTimeStart.toString() +
                    "\nCraneWait "+ship.getWaitingForUnloading().toString()+
                    "\nEmpty time: " + emptyTime.toString() +
                    "\nNew parking time: (empty - unloading) "  + emptyTime.minus(unloadingTimeStart) +
                    "\nCost: " + getExpenses() + "\nDelay: " + getDelay() + "\nWho work: " + printExecutorNames();

        }
        else return ship.toString() +
                "\nDeviation: " + ship.getDeviationFromTheSchedule().toString()+
                "\nComing time: (coming + deviation) " + comingTime.toString() +
                "\nUnloading time start: (coming + CraneWait) " + unloadingTimeStart.toString() +
                "\nCraneWait "+ship.getWaitingForUnloading().toString()+
                "\nCost: " + getExpenses() + "\nNot unload " + "\nDelay: " + getDelay();
    }

}
