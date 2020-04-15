package de.netempire.classes;

import de.netempire.PhilosophersDesk;
import de.netempire.logger.MyLogger;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    Fork right, left;
    int eatingTime;
    private volatile boolean exit = false;

    public Philosopher(String name, Fork right, Fork left){
        this.name = name;
        this.right = right;
        this.left = left;
    }

    public void run() {
        int i = 30;
        while( i > 0 && !exit) {
            try {
                // Philosopher is thinking
                MyLogger.log(name + " philosphiert.");
                sleep(1000);
                MyLogger.log(name + " hat Hunger.");
                PhilosophersDesk.report = name;
                // Philosopher is hungry
                PhilosophersDesk.satedPhilosophers.acquire();
                // taking right
                right.get();
                // turn left (critical moment)
                sleep(100);
                // taking left
                left.get();
                MyLogger.log(name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep(eatingTime);
            } catch (InterruptedException e) {
                MyLogger.log(e.getMessage());
            }
            right.put();
            left.put();
            PhilosophersDesk.satedPhilosophers.release();
            i--;
        }
    }

    public void stop(){
        exit = true;
    }

    public void setEatingTime(int eatingTime) {
        this.eatingTime = eatingTime;
    }
}