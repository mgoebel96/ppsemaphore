import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PhilosophersDesk {

    static Semaphore forks = new Semaphore(5);

    public static void main(String[] args) {
        startProcess();
    }

    public static void startProcess(){

        Philosopher platon = new Philosopher("Platon");
        Philosopher aristoteles = new Philosopher("Aristoteles");
        Philosopher herder = new Philosopher("Herder");
        Philosopher fichte = new Philosopher("Fichte");
        Philosopher schlegel  = new Philosopher("Schlegel ");

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> { System.out.println("Anzahl freier Gabeln: " + PhilosophersDesk.forks.availablePermits()); };

        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);

        new Thread( task ).start();

        new Thread( platon ).start();
        new Thread( aristoteles ).start();
        new Thread( schlegel ).start();
        new Thread( fichte ).start();
        new Thread( platon ).start();
        new Thread( aristoteles ).start();
        new Thread( herder ).start();
        new Thread( fichte ).start();
    }
}