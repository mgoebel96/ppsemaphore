package de.netempire;

import de.netempire.classes.Fork;
import de.netempire.classes.Philosopher;
import de.netempire.logger.MyLogger;
import de.netempire.logger.ResultLogger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PhilosophersDesk {

    public static Semaphore satedPhilosophers = new Semaphore(5, true);
    public static String report;

    public static void main(String[] args) {
        PhilosophersDesk.startProcess();
    }

    private static void startProcess() {
        Date start = Calendar.getInstance().getTime();

        Fork fork1 = new Fork();
        Fork fork2 = new Fork();
        Fork fork3 = new Fork();
        Fork fork4 = new Fork();
        Fork fork5 = new Fork();

        Philosopher platon = new Philosopher("Platon", 1, fork1, fork2);
        platon.setEatingTime(750);
        Philosopher aristoteles = new Philosopher("Aristoteles",2, fork2, fork3);
        aristoteles.setEatingTime(1000);
        Philosopher herder = new Philosopher("Herder", 3,fork3, fork4);
        herder.setEatingTime(300);
        Philosopher fichte = new Philosopher("Fichte", 4,fork4, fork5);
        fichte.setEatingTime(1500);
        Philosopher schlegel = new Philosopher("Schlegel",5, fork5, fork1);
        schlegel.setEatingTime(500);

        Philosopher[] philosophers = new Philosopher[]{platon, aristoteles, herder, fichte, schlegel};


        Thread platonThread = new Thread(platon);
        platonThread.start();
        Thread aristotelesThread = new Thread(aristoteles);
        aristotelesThread.start();
        Thread schlegelThread = new Thread(schlegel);
        schlegelThread.start();
        Thread fichteThread = new Thread(fichte);
        fichteThread.start();
        Thread herderThread = new Thread(herder);
        herderThread.start();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable controller = () -> {
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
            if (PhilosophersDesk.satedPhilosophers.availablePermits() != 0) return;
            System.out.println("Es haben alle Philosophen hunger!");
            try {
                Optional<Philosopher> lastPhiloso = Arrays.stream(philosophers).filter(p -> p.name.equals(report)).findFirst();
                int idLastPhiloso = lastPhiloso.map(philosopher -> philosopher.id - 1).orElse(0);
                MyLogger.log(report + " legt seine Gabeln wieder auf den Tisch.");
                if(Arrays.asList(philosophers).get(idLastPhiloso).right.isTaken()){
                    Arrays.asList(philosophers).get(idLastPhiloso).right.put();
                    while (PhilosophersDesk.satedPhilosophers.availablePermits() < 1) {
                        sleep(100);
                    }
                    while (Arrays.asList(philosophers).get(idLastPhiloso).right.getId() != -1) {
                        sleep(100);
                    }
                    Arrays.asList(philosophers).get(idLastPhiloso).right.get();
                } else {
                    Arrays.asList(philosophers).get(idLastPhiloso).left.put();
                    while (PhilosophersDesk.satedPhilosophers.availablePermits() < 1) {
                        sleep(100);
                    }
                    while (Arrays.asList(philosophers).get(idLastPhiloso).left.getId() != -1) {
                        sleep(100);
                    }
                    Arrays.asList(philosophers).get(idLastPhiloso).left.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
        Thread task2Thread = new Thread(controller);
        task2Thread.start();
    }

    public static int computeDuration(Date to, Date from) {
        long difference = from.getTime() - to.getTime();
        return (int) (difference/1000);
    }
}