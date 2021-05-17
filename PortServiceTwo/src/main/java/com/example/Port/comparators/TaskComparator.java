package com.example.Port.comparators;

import com.example.Port.ships.Task;

import java.util.Comparator;
public class TaskComparator implements Comparator<Task>{
    @Override
    public int compare(Task o1, Task o2) {
        return o1.getComingTime().compareTo(o2.getComingTime());
    }

}
