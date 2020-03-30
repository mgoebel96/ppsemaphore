import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PhilosophersDesk {

    static Semaphore hungryPhilosophers = new Semaphore(5, true);
    static Random random = new Random();
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

        Philosopher platon = new Philosopher("Platon", fork1, fork2);
        Philosopher aristoteles = new Philosopher("Aristoteles", fork2, fork3);
        Philosopher herder = new Philosopher("Herder", fork3, fork4);
        Philosopher fichte = new Philosopher("Fichte", fork4, fork5);
        Philosopher schlegel = new Philosopher("Schlegel", fork5, fork1);

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

        Runnable task2 = () -> {
            if (PhilosophersDesk.hungryPhilosophers.availablePermits() == 0) {
                System.out.println("Es haben alle Philosophen hunger!");
                try {
                    if (philosophers[0].state.equals("hungry") && philosophers[1].state.equals("hungry") && philosophers[2].state.equals("hungry") && philosophers[3].state.equals("hungry") && philosophers[4].state.equals("hungry")) {
                        Logger.printOut(report + " legt seine Gabeln wieder auf den Tisch.");
                        Arrays.asList(philosophers).stream().filter(p -> p.name.equals(report)).findFirst().get().right.put();
                        Arrays.asList(philosophers).stream().filter(p -> p.name.equals(report)).findFirst().get().left.put();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(task2, 0, 4, TimeUnit.SECONDS);
        Thread task2Thread = new Thread(task2);
        task2Thread.start();

    }
}