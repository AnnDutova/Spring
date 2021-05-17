package com.example.Port.threads;

import com.example.Port.enums.CargoType;
import com.example.Port.services.ServiceThree;
import com.example.Port.ships.Task;
import com.example.Port.time.Timer;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static com.example.Port.time.Time.*;

public class Port implements IObservable
{
    private ArrayList<Task> tasker = new ArrayList<>();
    private ArrayList<Crane> listeners = new ArrayList<>();
    private Statistic statistic = null;
    private ServiceThree thirdService;
    private Timer timer = new Timer(this);

    private CyclicBarrier barrier = null;
    private BlockingQueue tasksLoose = null;
    private BlockingQueue tasksLiquid = null;
    private BlockingQueue tasksContainer = null;

    private int countOfLooseCrane;
    private int countOfLiquidCranes;
    private int countOfContainerCranes;

    private int currentTime;

    public static final int MAX_TIME = MAX_DAYS * MAX_HOURS * MAX_MINUTES;
    private boolean isEnd = false;

    public Port(ArrayList<Task> tasker, ServiceThree thirdService)
    {
        this.tasker = tasker;
        this.thirdService = thirdService;
        this.listeners = new ArrayList<>();

        this.currentTime = 0;

        this.countOfContainerCranes = 1;
        this.countOfLiquidCranes = 1;
        this.countOfLooseCrane = 1;

        this.tasksContainer = new BlockingQueue(countOfContainerCranes);
        this.tasksLiquid = new BlockingQueue(countOfLiquidCranes);
        this.tasksLoose = new BlockingQueue(countOfLooseCrane);

        this.barrier = new CyclicBarrier(4, timer);
        this.statistic = new Statistic(tasker);
    }

    public Port(ArrayList<Task> tasker, int countOfLooseCrane, int countOfLiquidCranes, int countOfContainerCranes, ServiceThree three)
    {
        this.tasker = tasker;
        this.countOfLooseCrane = countOfLooseCrane;
        this.countOfLiquidCranes = countOfLiquidCranes;
        this.countOfContainerCranes = countOfContainerCranes;
        this.thirdService = three;

        this.tasksContainer = new BlockingQueue(countOfContainerCranes);
        this.tasksLiquid = new BlockingQueue(countOfLiquidCranes);
        this.tasksLoose = new BlockingQueue(countOfLooseCrane);

        this.barrier = new CyclicBarrier(countOfContainerCranes+countOfLiquidCranes+countOfLooseCrane+1, timer);
        this.statistic = new Statistic(tasker);
    }

    public void simulate()
    {
        initThreads();
        if (!tasker.isEmpty())
        {
            Task task = tasker.get(0);
            currentTime = task.getUnloadingTimeStart().getTimeInSeconds();
            notifyObservers(currentTime);
            addTask(task);
            System.out.println("Time "+ currentTime + " add task to queue");
            tasker.remove(0);
        }
        Task task =  null;
        while(!tasker.isEmpty() && !timer.isStop())
        {
            task = tasker.get(0);
            timer.add(task.getComingTime().getTimeInSeconds());
            while(currentTime < task.getComingTime().getTimeInSeconds())
            {
                if (!timer.isStop())
                {
                    try {
                        barrier.await();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else break;
            }
            if (!timer.isStop())
            {
                addTask(task);
                System.out.println("Time -" + currentTime + "- add task to queue " + task.getShip().getName());
                tasker.remove(0);
            }
        }
        System.out.println("All task are added");
        while(!timer.isStop())
        {
            System.out.println("Meet barrier");
            try {
                barrier.await();
            } catch (BrokenBarrierException e){
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Done barrier");
        }
        System.out.println("simulate loop end");
    }

    public Statistic getStatistic()
    {
        return statistic;
    }

    public void initThreads()
    {
        System.out.println();
        System.out.println("Loose "+ countOfLooseCrane + " Liquid "+ countOfLiquidCranes + " Container "+ countOfContainerCranes);

        int looseCount = countOfLooseCrane;
        while(looseCount > 0)
        {
            Crane craneLoose = new Crane(tasksLoose, CargoType.LOOSE, barrier, this, timer);
            looseCount--;
            addListener(craneLoose);
        }
        int liquidCount = countOfLiquidCranes;
        while (liquidCount > 0)
        {
            Crane craneLiquid = new Crane(tasksLiquid, CargoType.LIQUID, barrier, this, timer);
            liquidCount--;
            addListener(craneLiquid);
        }
        int containerCranes = countOfContainerCranes;
        while (containerCranes > 0)
        {
            Crane craneContainer = new Crane(tasksContainer, CargoType.CONTAINER, barrier, this, timer);
            containerCranes--;
            addListener(craneContainer);
        }
    }

    public void returnStatistic()
    {
        this.statistic = statistic.collect(this);
        thirdService.setNewStat(statistic);
    }

    private void addTask(Task task)
    {
        CargoType type = task.getShip().getCargoType();
        switch(type){
            case LOOSE:
                tasksLoose.put(task);
                break;
            case LIQUID:
                tasksLiquid.put(task);
                break;
            case CONTAINER:
                tasksContainer.put(task);
                break;
        }
    }

    synchronized protected void addStatisticElement(Task task, CargoType type)
    {
        statistic.addTask(task, type);
    }

    synchronized public int getTime()
    {
        return currentTime;
    }

    synchronized public void update(int newTime)
    {
        this.currentTime = newTime;
        notifyObservers(currentTime);
    }

    public int getCountOfLooseCrane()
    {
        return countOfLooseCrane;
    }

    public int getCountOfLiquidCranes()
    {
        return countOfLiquidCranes;
    }

    public int getCountOfContainerCranes()
    {
        return countOfContainerCranes;
    }

    @Override
    public void addListener(Crane object)
    {
        listeners.add(object);
    }

    @Override
    public synchronized void removeListener(Crane object)
    {
        listeners.remove(object);
        if (listeners.isEmpty())
        {
            System.out.println("Listeners is empty");
            returnStatistic();
        }
    }

    @Override
    public void notifyObservers(int newTime)
    {
        for (Crane listener: listeners)
        {
            listener.update(newTime);
        }
    }
}
