import java.util.Random;

import static java.lang.Thread.sleep;

public class Philosopher implements Runnable {

    String name;
    Fork right, left;
    String state;
    Random random = new Random();


    public Philosopher(String name, Fork right, Fork left){
        this.name = name;
        this.right = right;
        this.left = left;
    }

    public void run() {
        do {
            try {
                // Philosopher is thinking
                Logger.printOut (name + " philosphiert.");
                state = "wait";
                sleep((int) (random.nextDouble()*1000));
                Logger.printOut (name + " hat Hunger.");
                PhilosophersDesk.report = name;
                // Philosopher is hungry
                state = "hungry";
                PhilosophersDesk.hungryPhilosophers.acquire();
                // taking right
                right.get();
                // turn left (critical moment)
                sleep((int) (random.nextDouble()*1000));
                // taking left
                left.get();
                state = "eating";
                Logger.printOut (name + " hat zwei Gabeln. Er kann essen.");
                // holding two forks -> can eat now
                sleep((int) (random.nextDouble()*1000));
            } catch (InterruptedException e) {
                Logger.printOut (e.getMessage());
            }
            PhilosophersDesk.hungryPhilosophers.release();
            right.put();
            left.put();
        } while (true);
    }
}