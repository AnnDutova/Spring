package com.example.Port.threads;


import com.example.Port.enums.CargoType;
import com.example.Port.enums.ShipState;
import com.example.Port.ships.Task;
import com.example.Port.time.Time;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Statistic
{
    private Port port = null;
    private ArrayList<Task> tasker = null;

    private LinkedList<Task> statisticLoose = new LinkedList<>() ;
    private LinkedList<Task> statisticLiquid =  new LinkedList<>();
    private LinkedList<Task> statisticContainer = new LinkedList<>();

    private ArrayList<Task> finalTasker = null;

    public final static int CRANE_COST = 30000;

    private int totalCount;
    private int totalUnloadCount;

    private int looseCost;
    private int liquidCost;
    private int containerCost;

    private int totalCost;

    private int averageQueueLengthLoose;
    private int averageQueueLengthLiquid;
    private int averageQueueLengthContainer;

    private Time averageDeviationLoose;
    private Time averageDeviationLiquid;
    private Time averageDeviationContainer;

    private Time averageWaitingTimeLoose;
    private Time averageWaitingTimeLiquid;
    private Time averageWaitingTimeContainer;

    private Time maxDeviationLoose;
    private Time maxDeviationLiquid;
    private Time maxDeviationContainer;

    private int countOfLooseCranes;
    private int countOfLiquidCranes;
    private int countOfContainerCranes;

    public Statistic(ArrayList<Task> tasker)
    {
        this.totalCount = tasker.size();
        this.totalUnloadCount = 0;
        this.totalCost = 0;
        this.looseCost = 0;
        this.liquidCost = 0;
        this.containerCost = 0;
        ArrayList<Task> newTasker = new ArrayList<>();
        for (Task task: tasker)
        {
            newTasker.add(task.copy());
        }
        this.tasker = newTasker;
    }

    public Statistic(int countOfLooseCranes, int countOfLiquidCranes, int countOfContainerCranes,
                     int totalUnloadCount, int totalCount, int totalCost,
                     int averageQueueLengthLoose, int averageQueueLengthLiquid, int averageQueueLengthContainer,
                     Time averageWaitingTimeLoose, Time averageWaitingTimeLiquid, Time averageWaitingTimeContainer,
                     Time maxDeviationLoose, Time maxDeviationLiquid, Time maxDeviationContainer,
                     Time averageDeviationLoose, Time averageDeviationLiquid, Time averageDeviationContainer,
                     int containerCost, int looseCost, int liquidCost)
    {
        this.countOfContainerCranes = countOfContainerCranes;
        this.countOfLiquidCranes = countOfLiquidCranes;
        this.countOfLooseCranes = countOfLooseCranes;

        this.totalUnloadCount = totalUnloadCount;
        this.totalCount = totalCount;
        this.totalCost = totalCost;
        this.averageQueueLengthLoose = averageQueueLengthLoose;
        this.averageQueueLengthContainer = averageQueueLengthContainer;
        this.averageQueueLengthLiquid = averageQueueLengthLiquid;
        this.averageWaitingTimeContainer = averageWaitingTimeContainer;
        this.averageWaitingTimeLiquid = averageWaitingTimeLiquid;
        this.averageWaitingTimeLoose = averageWaitingTimeLoose;
        this.maxDeviationLoose = maxDeviationLoose;
        this.maxDeviationLiquid = maxDeviationLiquid;
        this.maxDeviationContainer = maxDeviationContainer;
        this.averageDeviationLoose = averageDeviationLoose;
        this.averageDeviationLiquid = averageDeviationLiquid;
        this.averageDeviationContainer = averageDeviationContainer;
        this.containerCost = containerCost;
        this.looseCost = looseCost;
        this.liquidCost = liquidCost;
    }

    Statistic(int countOfLooseCranes, int countOfLiquidCranes, int countOfContainerCranes,
              int totalUnloadCount, int totalCount, int totalCost,
              int averageQueueLengthLoose, int averageQueueLengthLiquid, int averageQueueLengthContainer,
              Time averageWaitingTimeLoose, Time averageWaitingTimeLiquid, Time averageWaitingTimeContainer,
              Time maxDeviationLoose, Time maxDeviationLiquid, Time maxDeviationContainer,
              Time averageDeviationLoose, Time averageDeviationLiquid, Time averageDeviationContainer,
              int containerCost, int looseCost, int liquidCost,
              LinkedList<Task> statisticLoose, LinkedList<Task> statisticLiquid,LinkedList<Task> statisticContainer )
    {
        this.countOfContainerCranes = countOfContainerCranes;
        this.countOfLiquidCranes = countOfLiquidCranes;
        this.countOfLooseCranes = countOfLooseCranes;

        this.totalUnloadCount = totalUnloadCount;
        this.totalCount = totalCount;
        this.totalCost = totalCost;

        this.averageQueueLengthLoose = averageQueueLengthLoose;
        this.averageQueueLengthContainer = averageQueueLengthContainer;
        this.averageQueueLengthLiquid = averageQueueLengthLiquid;
        this.averageWaitingTimeContainer = averageWaitingTimeContainer;
        this.averageWaitingTimeLiquid = averageWaitingTimeLiquid;
        this.averageWaitingTimeLoose = averageWaitingTimeLoose;
        this.maxDeviationLoose = maxDeviationLoose;
        this.maxDeviationLiquid = maxDeviationLiquid;
        this.maxDeviationContainer = maxDeviationContainer;
        this.averageDeviationLoose = averageDeviationLoose;
        this.averageDeviationLiquid = averageDeviationLiquid;
        this.averageDeviationContainer = averageDeviationContainer;
        this.containerCost = containerCost;
        this.looseCost = looseCost;
        this.liquidCost = liquidCost;
        this.statisticLoose = statisticLoose;
        this.statisticLiquid = statisticLiquid;
        this.statisticContainer = statisticContainer;
    }

    public Statistic returnForJson(){
        return new Statistic(countOfContainerCranes, countOfLiquidCranes, countOfLooseCranes,
                totalUnloadCount, totalCount, totalCost,
                averageQueueLengthLoose, averageQueueLengthContainer, averageQueueLengthLiquid,
                averageWaitingTimeContainer, averageWaitingTimeLiquid, averageWaitingTimeLoose,
                maxDeviationLoose, maxDeviationLiquid, maxDeviationContainer,
                averageDeviationLoose,averageDeviationLiquid, averageDeviationContainer,
                containerCost, looseCost, liquidCost);

    }

    public void addTask(Task task, CargoType type)
    {
        String name;
        switch (type)
        {
            case LOOSE:
                if (!isAdded(statisticLoose, task))
                {
                    name = task.getShip().getName();
                    statisticLoose.add(task.copyForStatistic(findStartingWeight(name)));
                }
                break;
            case LIQUID:
                if (!isAdded(statisticLiquid, task))
                {
                    name = task.getShip().getName();
                    statisticLiquid.add(task.copyForStatistic(findStartingWeight(name)));
                }
                break;
            case CONTAINER:
                if (!isAdded(statisticContainer, task))
                {
                    name = task.getShip().getName();
                    statisticContainer.add(task.copyForStatistic(findStartingWeight(name)));
                }
                break;
        }
    }

    private boolean isAdded(LinkedList<Task> statistic, Task newTask)
    {
        for(Task task: statistic)
        {
            if (task.getShip().getName().equals(newTask.getShip().getName())){
                return true;
            }
            if (task.getComingTime().getTimeInSeconds() > newTask.getComingTime().getTimeInSeconds())
            {
                break;
            }
        }
        return false;
    }

    private int findStartingWeight(String name)
    {
        for(Task task: tasker)
        {
            if (task.getShip().getName().equals(name)){
                return task.getShip().getCargoWeight();
            }
        }
        return 0;
    }

    public Statistic collect(Port port)
    {
        finalTasker = new ArrayList<>();
        for (Task task: tasker)
        {
            Task unloadTask = getUnloadTask(task);
            finalTasker.add(unloadTask);
        }
        String name;
        LinkedList<Task> new_loose = new LinkedList<>();
        for (Task task: statisticLoose)
        {
            name = task.getShip().getName();
            new_loose.add(task.copyForStatistic(findStartingWeight(name)));
        }

        LinkedList<Task> new_liquid = new LinkedList<>();
        for (Task task: statisticLiquid)
        {
            name = task.getShip().getName();
            new_liquid.add(task.copyForStatistic(findStartingWeight(name)));
        }

        LinkedList<Task> new_container = new LinkedList<>();
        for (Task task: statisticContainer)
        {
            name = task.getShip().getName();
            new_container.add(task.copyForStatistic(findStartingWeight(name)));
        }

        this.totalCost = containerCost + looseCost + liquidCost;
        Time time = new Time();
        try {
            this.averageWaitingTimeContainer= time.convertSecondsToTime(averageWaitingTime(statisticContainer));
            this.averageWaitingTimeLiquid = time.convertSecondsToTime(averageWaitingTime(statisticLiquid));
            this.averageWaitingTimeLoose = time.convertSecondsToTime(averageWaitingTime(statisticLoose));

            this.maxDeviationLoose = time.convertSecondsToTime(maxDeviation(statisticLoose));
            this.maxDeviationLiquid = time.convertSecondsToTime(maxDeviation(statisticLiquid));
            this.maxDeviationContainer = time.convertSecondsToTime(maxDeviation(statisticContainer));

            this.averageDeviationLoose = time.convertSecondsToTime(averageDeviation(statisticLoose));
            this.averageDeviationLiquid = time.convertSecondsToTime(averageDeviation(statisticLiquid));
            this.averageDeviationContainer = time.convertSecondsToTime(averageDeviation(statisticContainer));
        } catch (InvalidAlgorithmParameterException invalidValue) {
            invalidValue.printStackTrace();
        }
        this.averageQueueLengthLoose = averageQueueLength(statisticLoose);
        this.averageQueueLengthLiquid = averageQueueLength(statisticLiquid);
        this.averageQueueLengthContainer = averageQueueLength(statisticContainer);

        return new Statistic(port.getCountOfLooseCrane(), port.getCountOfLiquidCranes(), port.getCountOfContainerCranes(),
                totalUnloadCount, totalCount, totalCost,
                averageQueueLengthLoose, averageQueueLengthContainer, averageQueueLengthLiquid,
                averageWaitingTimeContainer, averageWaitingTimeLiquid, averageWaitingTimeLoose,
                maxDeviationLoose, maxDeviationLiquid, maxDeviationContainer,
                averageDeviationLoose,averageDeviationLiquid, averageDeviationContainer,
                containerCost, looseCost, liquidCost, new_loose, new_liquid, new_container);
    }

    private Task search(Task currentTask, LinkedList<Task> stataList){
        for (Task task: stataList){
            if (currentTask.getShip().getName().equals(task.getShip().getName())){
                return task.copyForStatistic(findStartingWeight(task.getShip().getName()));
            }
        }
        return null;
    }

    private Task getUnloadTask(Task task){
        Task unloadTask = null;
        CargoType type = task.getShip().getCargoType();
        switch(type)
        {
            case LOOSE:
                unloadTask = search(task, statisticLoose);
                if (unloadTask != null)
                {
                    if (unloadTask.getShip().getState() == ShipState.EMPTY)
                    {
                        this.totalUnloadCount += 1;
                    }
                    this.looseCost += unloadTask.getExpenses();
                }
                break;
            case LIQUID:
                unloadTask = search(task, statisticLiquid);
                if (unloadTask != null)
                {
                    if (unloadTask.getShip().getState() == ShipState.EMPTY) {
                        this.totalUnloadCount += 1;
                    }
                    this.liquidCost += unloadTask.getExpenses();
                }
                break;
            case CONTAINER:
                unloadTask = search(task, statisticContainer);
                if (unloadTask != null)
                {
                    if (unloadTask.getShip().getState() == ShipState.EMPTY)
                    {
                        this.totalUnloadCount += 1;
                    }
                    this.containerCost += unloadTask.getExpenses();
                }
                break;
        }
        return unloadTask;
    }

    private int averageQueueLength(LinkedList<Task> tasker)
    {
        LinkedList<Integer> queue = new LinkedList<>();
        int count = tasker.size();
        int totalLength = 0;
        for ( int i = 0; i < count; i++)
        {
            int num = 0;
            for ( int j = 1; j < count-1; j++)
            {
                if (tasker.get(i).isDone())
                {
                    if(tasker.get(i).getEmptyTime().bigger(tasker.get(j).getUnloadingTimeStart()))
                    {
                        num++;
                    }
                    else{
                        break;
                    }
                }
                else {
                    num++;
                }
            }
            queue.add(num);
        }

        for (Integer num: queue)
        {
            totalLength+=num;
        }
        if (count!= 0)
        {
            return totalLength/count;
        }
        else return 0;
    }

    private int averageWaitingTime(LinkedList<Task> tasker)
    {
        int maxTime = 0;
        int count = tasker.size();
        for ( int i = 0; i < count; i++)
        {
            if (tasker.get(i).isDone())
            {
                maxTime += tasker.get(i).getDelay();
            }
        }
        if (count != 0)
        {
            return maxTime/count;
        }
        else return 0;
    }

    private int maxDeviation(LinkedList<Task> stat)
    {
        int maxDeviation = 0;
        for (Task task: stat)
        {
            int newMax = task.getShip().getDeviationFromTheSchedule().getTimeInSeconds();
            if (newMax > maxDeviation)
            {
                maxDeviation = newMax;
            }
        }
        return maxDeviation;
    }

    private int averageDeviation(LinkedList<Task> tasker)
    {
        int count = tasker.size();
        int deviation = 0;
        for (Task task: tasker)
        {
            deviation += task.getShip().getDeviationFromTheSchedule().getTimeInSeconds();
        }
        if (count != 0)
        {
            return deviation/count;
        }
        else return 0;
    }

    public int getOptimal(CargoType type, int count)
    {
        switch(type){
            case LOOSE:
                count = getOptimalCraneCount(looseCost, count);
                break;
            case LIQUID:
                count = getOptimalCraneCount(liquidCost, count);
                break;
            case CONTAINER:
                count = getOptimalCraneCount(containerCost, count);
                break;
        }
        return count;
    }


    public int getOptimalCraneCount(int cost, int count)
    {
        while (cost > CRANE_COST)
        {
            cost -= CRANE_COST;
            count++;
        }
        return count;
    }

    public boolean isOptimal()
    {
        return (liquidCost < CRANE_COST && looseCost < CRANE_COST && containerCost < CRANE_COST);
    }

    private String averageWaitingTime()
    {
        return "\n For LOOSE: " + averageWaitingTimeLoose +
                "\n For LIQUID: " + averageWaitingTimeLiquid +
                "\n For CONTAINER: " + averageWaitingTimeContainer;
    }
    private String maxDeviation()
    {
        return "\n For LOOSE: " +  maxDeviationLoose +
                "\n For LIQUID: " + maxDeviationLiquid +
                "\n For CONTAINER: "+ maxDeviationContainer;
    }

    private String averageDeviation()
    {
        return "\n For LOOSE: " +  averageDeviationLoose +
                "\n For LIQUID: " + averageDeviationLiquid +
                "\n For CONTAINER: "+ averageDeviationContainer;
    }

    private String averageQueueLength()
    {
        return "\n For LOOSE: " + averageQueueLengthLoose +
                "\n For LIQUID: " + averageQueueLengthLiquid +
                "\n For CONTAINER: "+ averageQueueLengthContainer;
    }

    private String print(LinkedList<Task> statistic)
    {
        StringBuilder str = new StringBuilder();
        for (Task task: statistic)
        {
            str.append(task.toString()).append("\n");
        }
        return str.toString();
    }

    @Override
    public String toString(){
        return "\nLOOSE output data\n" + print(statisticLoose) +
                "\nLIQUID output data\n" + print(statisticLiquid) +
                "\nCONTAINER output data\n"+ print(statisticContainer) +
                "\nЧисло разгруженных судов: " + totalUnloadCount +
                "\nЧисло судов в расписании: " + totalCount +
                "\nСредняя длина очереди на разгрузку: " + averageQueueLength() +
                "\nСреднее время ожидания в очереди: " + averageWaitingTime() +
                "\nМаксимальная задержка разгрузки: " + maxDeviation() +
                "\nСредняя задержка разгрузки: " + averageDeviation() +
                "\nОбщая сумма штрафа: " + totalCost +
                "\nШтраф LOOSE: " + looseCost + " работало: " + countOfLooseCranes +
                "\nШтраф LIQUID: " + liquidCost +" работало: " + countOfLiquidCranes +
                "\nШтраф CONTAINER: " + containerCost+" работало: " + countOfContainerCranes;
    }
}
