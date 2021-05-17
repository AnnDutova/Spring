package com.example.Port.threads;

import com.example.Port.enums.CargoType;
import com.example.Port.enums.ShipState;
import com.example.Port.ships.Task;
import com.example.Port.time.Timer;

import java.util.concurrent.CyclicBarrier;

public class Crane extends Thread implements IObserver
{
    public static final int MAX_WEIGHT_LIQUID_CRANE = 10;
    public static final int MAX_WEIGHT_CONTAINER_CRANE = 4;
    public static final int MAX_WEIGHT_LOOSE_CRANE = 100;

    public static final int WAIT_FOR_CONTAINER = 5;
    public static final int WAIT_FOR_LOOSE = 20;
    public static final int WAIT_FOR_LIQUID = 8;

    protected BlockingQueue queue = null;
    private int currentTime;
    private CargoType type;
    private CyclicBarrier barrier;
    private Port manager;
    private boolean simulationEnd;
    private Task currentTask = null;
    private int currentWaitingTime;
    private int currentUnloadWaitingTime;
    private Timer timer;
    private Statistic statistic = null;

    public Crane(BlockingQueue queue, CargoType type, CyclicBarrier barrier, Port manager, Timer timer)
    {
        this.queue = queue;
        this.currentTime = 0;
        this.currentWaitingTime = 0;
        this.currentUnloadWaitingTime = 0;
        this.type = type;
        this.barrier = barrier;
        this.manager = manager;
        this.simulationEnd = false;
        this.timer = timer;
        start();
    }

    public void run()
    {
        while(!simulationEnd)
        {
            currentTask = queue.get(this);
            if (!timer.isStop() && currentTask != null && currentTask.getShip().getState() == ShipState.FLOAT)
            {
                printCurrentStatus(Thread.currentThread().getName(),
                        currentTask.getShip().getCargoWeight(),
                        currentTask.getShip().getName(),
                        "get from queue and set waiting time",
                        currentTime);
                addUnloadWait(currentTask.getShip().getWaitingForUnloading().getTimeInSeconds());
                currentTask.getShip().setWaitingState();
            }
            //else
            if (!timer.isStop() && currentTask != null && currentTask.getShip().getState() == ShipState.WAIT)
            {
                if (currentTime >= currentUnloadWaitingTime)
                {
                    printCurrentStatus(Thread.currentThread().getName(),
                            currentTask.getShip().getCargoWeight(),
                            currentTask.getShip().getName(),
                            "get from CurrentTask, end waiting time add unloadingTimeStart",
                            currentTime);
                    currentUnloadWaitingTime = 0;
                    currentTask.setUnloadingTimeStart(currentTime);
                    currentTask.unload(getMaxWeight());
                    addWaitTimeToTimer();
                }
            }
            else if (!timer.isStop() && currentTask != null && currentTask.getShip().getState() == ShipState.UNLOAD)
            {
                printCurrentStatus(Thread.currentThread().getName(),
                        currentTask.getShip().getCargoWeight(),
                        currentTask.getShip().getName(),
                        "get from currentTask in status UNLOAD",
                        currentTime );
                if ( currentTime >= currentWaitingTime)
                {
                    currentTask.unload(getMaxWeight());
                    addWaitTimeToTimer();
                }
                if (currentTask.getShip().getState() == ShipState.EMPTY && !timer.isStop())
                {
                    currentTask.setEmptyTimeInt(currentTime);
                    printCurrentStatus(Thread.currentThread().getName(),
                            currentTask.getShip().getCargoWeight(),
                            currentTask.getShip().getName(),
                            "set empty time",
                            currentTime );
                    manager.addStatisticElement(currentTask, type);
                    currentWaitingTime = 0;
                    currentTask = null;
                }
            }
            if (!timer.isStop())
            {
                //printBarrierAwait(Thread.currentThread().getName()," meet barrier", currentTime);
                try {
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //printBarrierAwait(Thread.currentThread().getName()," done barrier", currentTime);
            }
            else end();
        }
        remove();
        System.out.println(Thread.currentThread().getName()+ " end work");
    }

    public void end()
    {
        simulationEnd = true;
    }

    private void remove(){
        manager.removeListener(this);
    }

    private void addWaitTimeToTimer()
    {
        this.currentWaitingTime = currentTime + getWait();
        timer.add(currentWaitingTime);
    }

    private void addUnloadWait(int wait)
    {
        this.currentUnloadWaitingTime = currentTime + wait;
        timer.add(currentUnloadWaitingTime);
    }

    private synchronized void printCurrentStatus(String threadName, int weight, String ship, String message, int time)
    {
        System.out.format("%15s%15s%10s%10s%10s%40s\n", threadName, type.toString(), weight, ship, time, message);
    }

    private synchronized void printBarrierAwait(String threadName, String message, int time)
    {
        System.out.format("%15s%15s%30s\n", threadName, time, message);
    }

    private int getWait()
    {
        int output = 0;
        switch(type)
        {
            case CONTAINER:
                output = WAIT_FOR_CONTAINER;
                break;
            case LOOSE:
                output = WAIT_FOR_LOOSE;
                break;
            case LIQUID:
                output = WAIT_FOR_LIQUID;
                break;
        }
        return output;
    }

    private int getMaxWeight()
    {
        int output = 0;
        switch(type)
        {
            case CONTAINER:
                output = MAX_WEIGHT_CONTAINER_CRANE;
                break;
            case LOOSE:
                output = MAX_WEIGHT_LOOSE_CRANE;
                break;
            case LIQUID:
                output = MAX_WEIGHT_LIQUID_CRANE;
                break;
        }
        return output;
    }
    @Override
    public void update(int currentTime)
    {
        this.currentTime = currentTime;
    }

}
