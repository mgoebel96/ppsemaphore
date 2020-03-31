import java.util.Arrays;
import java.util.Optional;
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

    private static void startProcess() {
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
            if (PhilosophersDesk.satedPhilosophers.availablePermits() != 0) return;
            System.out.println("Es haben alle Philosophen hunger!");
            try {
                Logger.printOut(report + " legt seine Gabeln wieder auf den Tisch.");
                Optional<Philosopher> lastPhiloso = Arrays.stream(philosophers).filter(p -> p.name.equals(report)).findFirst();
                int idLastPhiloso = lastPhiloso.map(philosopher -> philosopher.id - 1).orElse(0);
                if(Arrays.asList(philosophers).get(idLastPhiloso).right.isTaken()){
                    Arrays.asList(philosophers).get(idLastPhiloso).right.put();
                    while(PhilosophersDesk.satedPhilosophers.availablePermits() < 1 ){
                        sleep(10);
                    }
                    Arrays.asList(philosophers).get(idLastPhiloso).right.get();
                } else {
                    Arrays.asList(philosophers).get(idLastPhiloso).left.put();
                    while(PhilosophersDesk.satedPhilosophers.availablePermits() < 1 ){
                        sleep(10);
                    }
                    Arrays.asList(philosophers).get(idLastPhiloso).left.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(controller, 0, 4, TimeUnit.SECONDS);
        Thread task2Thread = new Thread(controller);
        task2Thread.start();

    }
}