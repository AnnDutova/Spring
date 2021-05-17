package com.example.Port.threads;

public interface IObservable
{
    void addListener(Crane object);
    void removeListener(Crane object);
    void notifyObservers(int newTime);
}