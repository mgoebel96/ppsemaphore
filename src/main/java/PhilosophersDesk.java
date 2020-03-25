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
        Fork fork1 = new Fork( 1);
        Fork fork2 = new Fork(2);
        Fork fork3 = new Fork(3);
        Fork fork4 = new Fork( 4);
        Fork fork5 = new Fork( 5);

        Philosopher platon = new Philosopher("Platon",fork1 ,fork2);
        Philosopher aristoteles = new Philosopher("Aristoteles", fork2, fork3);
        Philosopher herder = new Philosopher("Herder", fork3, fork4);
        Philosopher fichte = new Philosopher("Fichte", fork4, fork5);
        Philosopher schlegel  = new Philosopher("Schlegel ", fork5, fork1);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> { System.out.println("Anzahl der Hände, die eine Gabel verlangen dürfen: " + PhilosophersDesk.forks.availablePermits()); };

        executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);

        new Thread( task ).start();

        new Thread( platon ).start();
        new Thread( aristoteles ).start();
        new Thread( schlegel ).start();
        new Thread( fichte ).start();
        new Thread( herder ).start();
    }
}