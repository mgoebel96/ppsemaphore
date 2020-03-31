import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PhilosophersDesk {

    static Semaphore satedPhilosophers = new Semaphore(5, true);
    static String report;

    public static void main(String[] args) {
        startProcess();
    }

    public static void startProcess() {
        Fork fork1 = new Fork();
        Fork fork2 = new Fork();
        Fork fork3 = new Fork();
        Fork fork4 = new Fork();
        Fork fork5 = new Fork();

        Philosopher platon = new Philosopher("Platon", 1, fork1, fork2);
        Philosopher aristoteles = new Philosopher("Aristoteles",2, fork2, fork3);
        Philosopher herder = new Philosopher("Herder", 3,fork3, fork4);
        Philosopher fichte = new Philosopher("Fichte", 4,fork4, fork5);
        Philosopher schlegel = new Philosopher("Schlegel",5, fork5, fork1);

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

        Runnable controller = () -> {
            if (PhilosophersDesk.satedPhilosophers.availablePermits() == 0) {
                System.out.println("Es haben alle Philosophen hunger!");
                try {
                    Logger.printOut(report + " legt seine Gabeln wieder auf den Tisch.");
                    int lastPhilosopher = Arrays.asList(philosophers).stream().filter(p -> p.name.equals(report)).findFirst().get().id-1;
                    if(Arrays.asList(philosophers).get(lastPhilosopher).right.isTaken()){
                        Arrays.asList(philosophers).get(lastPhilosopher).right.put();
                        while(PhilosophersDesk.satedPhilosophers.availablePermits() < 1 ){
                            sleep(10);
                        }
                        Arrays.asList(philosophers).get(lastPhilosopher).right.get();
                    } else {
                        Arrays.asList(philosophers).get(lastPhilosopher).left.put();
                        while(PhilosophersDesk.satedPhilosophers.availablePermits() < 1 ){
                            sleep(10);
                        }
                        Arrays.asList(philosophers).get(lastPhilosopher).left.get();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
        Thread task2Thread = new Thread(controller);
        task2Thread.start();

    }
}