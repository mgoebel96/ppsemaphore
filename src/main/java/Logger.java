public class Logger {

    private Logger() {
        throw new IllegalStateException("Logger class");
    }

    public static synchronized void printOut(String printText){
        System.out.println(printText);
    }
}