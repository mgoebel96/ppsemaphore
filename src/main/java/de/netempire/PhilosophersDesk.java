package de.netempire;

import de.netempire.classes.Fork;
import de.netempire.classes.Philosopher;
import de.netempire.logger.ResultLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class PhilosophersDesk {

    public static final Semaphore satedPhilosophers = new Semaphore(4, true);
    public static String report;
    static Fork fork1 = new Fork();
    static Fork fork2 = new Fork();
    static Fork fork3 = new Fork();
    static Fork fork4 = new Fork();
    static Fork fork5 = new Fork();
    static Philosopher platon = new Philosopher("Platon",fork1, fork2);
    static Philosopher aristoteles = new Philosopher("Aristoteles",fork2, fork3);
    static Philosopher herder = new Philosopher("Herder", fork3, fork4);
    static Philosopher fichte = new Philosopher("Fichte", fork4, fork5);
    static Philosopher schlegel = new Philosopher("Schlegel", fork5, fork1);
    static Thread platonThread = new Thread(platon);
    static Thread aristotelesThread = new Thread(aristoteles);
    static Thread schlegelThread = new Thread(schlegel);
    static Thread fichteThread = new Thread(fichte);
    static Thread herderThread = new Thread(herder);
    static Date start = Calendar.getInstance().getTime();
    static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static Runnable finisher;

    public static void main(String[] args) {
        PhilosophersDesk.startProcess();
    }

    private static void startProcess() {
        initializePhilosophers();
        initializeFinisher();
        start();
    }

    public static void initializePhilosophers() {
        platon.setEatingTime(750);
        aristoteles.setEatingTime(1000);
        herder.setEatingTime(300);
        fichte.setEatingTime(1500);
        schlegel.setEatingTime(500);
    }

    public static void initializeFinisher(){
        finisher = () -> {
            if(!platonThread.isAlive() && !herderThread.isAlive() && !aristotelesThread.isAlive() && !fichteThread.isAlive() && !schlegelThread.isAlive()){
                platon.stop();
                herder.stop();
                platon.stop();
                aristoteles.stop();
                schlegel.stop();
                executor.shutdown();
                System.out.println("Der Abend wird beendet.");
                ResultLogger.log("Die Philosophen haben " + computeDuration(start, Calendar.getInstance().getTime()) + " Sekunden zusammen am Tisch gesessen.");
            }
        };
    }

    public static void start(){
        platonThread.start();
        aristotelesThread.start();
        schlegelThread.start();
        fichteThread.start();
        herderThread.start();
        executor.scheduleAtFixedRate(finisher, 0, 4, TimeUnit.SECONDS);
    }

    public static int computeDuration(Date to, Date from) {
        long difference = from.getTime() - to.getTime();
        return (int) (difference/1000);
    }
}