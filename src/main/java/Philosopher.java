import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {

    String name;

    public Philosopher(String name){
        this.name = name;
    }

    public void run() {
            try {
                PhilosophersDesk.forks.acquire();
                PhilosophersDesk.forks.acquire();
                try {
                    System.out.println("Philosoph " + name + " isst jetzt!");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("Philosoph " + name + " ist satt! Er legt seine Gabeln wieder auf den Tisch.");
                } finally {
                    PhilosophersDesk.forks.release();
                    PhilosophersDesk.forks.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
    }
}

