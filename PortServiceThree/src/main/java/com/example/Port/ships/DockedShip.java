package com.example.Port.ships;

import com.example.Port.enums.CargoType;
import com.example.Port.enums.ShipState;
import com.example.Port.time.Time;

public class DockedShip extends Ship
{
    private ShipFactory factory = new ShipFactory();

    private final Time deviationFromTheSchedule;
    private final Time waitingForUnloading;
    private ShipState state;

    public DockedShip(String name, CargoType type, int cargoWeight, Time comingTime)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = factory.generateShipDevastationFromTheCheadle();
        this.waitingForUnloading = factory.generateShipUnloadDevastation();
        this.state = ShipState.FLOAT;
    }

    public DockedShip(String name, CargoType type, int cargoWeight, Time comingTime,
                      Time deviationFromTheSchedule, Time waitingForUnloading)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = deviationFromTheSchedule;
        this.waitingForUnloading = waitingForUnloading;
        this.state = ShipState.FLOAT;
    }

    private DockedShip(String name, CargoType type, int cargoWeight, Time comingTime,
                       Time deviationFromTheSchedule, Time waitingForUnloading, ShipState state)
    {
        super(name, type, cargoWeight, comingTime);
        this.deviationFromTheSchedule = deviationFromTheSchedule;
        this.waitingForUnloading = waitingForUnloading;
        this.state = state;
    }

    public Time getDeviationFromTheSchedule()
    {
        return deviationFromTheSchedule;
    }

    public Time getWaitingForUnloading()
    {
        return waitingForUnloading;
    }

    public ShipState getState ()
    {
        return state;
    }

    public void setWaitingState ()
    {
        this.state = ShipState.WAIT;
    }

    public synchronized void takePeaceOfCargo(int weight)
    {
        if (super.getCargoWeight() <= 0)
        {
            state = ShipState.EMPTY;
        }
        else {
            int cargoWeight = super.getCargoWeight();
            cargoWeight -= weight;
            super.setCargoWeight(cargoWeight);
            state = weight>0? ShipState.UNLOAD: ShipState.EMPTY;
        }
    }

    public DockedShip copy()
    {
        return new DockedShip(super.getName(),
                super.getCargoType(),
                super.getCargoWeight(),
                super.getComingTime(),
                this.getDeviationFromTheSchedule(),
                this.getWaitingForUnloading());
    }

    public DockedShip copyForStatistic(int weight)
    {
        return new DockedShip(super.getName(),
                super.getCargoType(),
                weight,
                super.getComingTime(),
                this.getDeviationFromTheSchedule(),
                this.getWaitingForUnloading(),
                this.state);
    }
}
