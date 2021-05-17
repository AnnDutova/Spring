package com.example.Port.comparators;


import com.example.Port.ships.Ship;
import java.util.Comparator;

public class DataComparator implements Comparator<Ship> {
    @Override
    public int compare(Ship ship1, Ship ship2) {
        return ship1.getComingTime().compareTo(ship2.getComingTime());
    }
}
