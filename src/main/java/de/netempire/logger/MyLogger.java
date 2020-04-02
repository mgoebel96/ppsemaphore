package de.netempire.logger;

public class MyLogger {

    private MyLogger() {
        throw new IllegalStateException("Logger class");
    }

    public static synchronized void log(String printText){
        System.out.println(printText);
    }
}