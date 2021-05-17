package com.example.Port.services;

import com.example.Port.enums.CargoType;
import com.example.Port.schedule.TimeTableJSONParser;
import com.example.Port.ships.Task;
import com.example.Port.threads.Port;
import com.example.Port.threads.Statistic;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ServiceThree
{
    private final static int MAX_TRY_NUMBER = 40;
    private Port port;
    private Semaphore mutex;
    private ArrayList<Task> tasker = null;
    private Statistic stat = null;
    private int attemptNumber;

    public ServiceThree(Semaphore mutex)
    {
        this.mutex = mutex;
        attemptNumber = 0;
    }

    public void start()
    {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readJson();
        simulation();
    }

    private void readJson()
    {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        tasker = parser.readJSONTask(null);
    }

    public Statistic sentStatistic()
    {
        stat = port.getStatistic();
        return stat;
    }

    private void simulation()
    {
        this.port = new Port(copyTasker(tasker), this);
        port.simulate();
    }

    private void restart()
    {
        attemptNumber++;
        port.simulate();
    }

    public void setNewStat(Statistic stat)
    {
        this.stat = stat;
        checkStatistic();
    }

    private void checkStatistic()
    {
        if (!stat.isOptimal() && attemptNumber <= MAX_TRY_NUMBER)
        {
            this.port = new Port(copyTasker(tasker),
                    stat.getOptimal(CargoType.LOOSE, port.getCountOfLooseCrane()),
                    stat.getOptimal(CargoType.LIQUID, port.getCountOfLiquidCranes()),
                    stat.getOptimal(CargoType.CONTAINER, port.getCountOfContainerCranes()), this);
            System.out.println(stat.toString());
            restart();
        }
        else{
            mutex.release();
        }
    }

    private ArrayList<Task> copyTasker (ArrayList<Task> tasker)
    {
        ArrayList<Task> newTasker = new ArrayList<>();
        for (Task task: tasker)
        {
            newTasker.add(task.copy());
        }
        return newTasker;
    }
}
