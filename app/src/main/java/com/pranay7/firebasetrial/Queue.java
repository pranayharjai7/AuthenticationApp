package com.pranay7.firebasetrial;

public class Queue {
    public int currentNumber;
    public int lastNumber;
    public int customersLeft;
    public String averageTime;
    public String timeOfStart;
    public String timeOfNext;

    public Queue() {
    }

    public Queue(int currentNumber, int lastNumber, int customersLeft, String averageTime, String timeOfStart, String timeOfNext) {
        this.currentNumber = currentNumber;
        this.lastNumber = lastNumber;
        this.customersLeft = customersLeft;
        this.averageTime = averageTime;
        this.timeOfStart = timeOfStart;
        this.timeOfNext = timeOfNext;
    }
}
