package com.jugarte.gourmet;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManagerImpl implements ThreadManager {

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 10;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue; // A queue of Runnables
    private final ThreadPoolExecutor threadPool;

    public ThreadManagerImpl() {
        workQueue = new LinkedBlockingQueue<>();
        threadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                workQueue);
    }

    @Override
    public void runOnUIThread(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("command couldn't be null");
        }

        Looper mainLooper = Looper.getMainLooper();
        new Handler(mainLooper).post(runnable);
    }

    @Override
    public void runOnBackground(Runnable runnable) {
        runOnBackground(runnable, Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    public void runOnBackground(Runnable runnable, int priority) {
        if (runnable == null) {
            throw new NullPointerException("command couldn't be null");
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new RejectedExecutionException("thread is interrupted");
        }

        try {
            Process.setThreadPriority(priority);
        } catch (SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Log.d("Task", "New thread pool request for runnable" + runnable.toString());
        threadPool.execute(runnable);
    }

    @Override
    public void cancel (Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("command couldn't be null");
        }

        threadPool.remove(runnable);
        threadPool.purge();
    }
}
