package com.rs.cores;

/**
 * A hidden exception handler for logging silent thread death from the slow executor pool.
 * @author David O'Neill (dlo3)
 */
public final class SlowThreadHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
       // Logger.threadFatal("(" + thread.getName() + ", slow pool) - Printing trace");
        throwable.printStackTrace();
    }

}
