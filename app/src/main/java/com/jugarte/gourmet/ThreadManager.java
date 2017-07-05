package com.jugarte.gourmet;

import java.util.concurrent.RejectedExecutionException;

public interface ThreadManager {

    /**
     * Executes the given runnable at some time in the future.  The runnable
     * may runOnBackground in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param runnable the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if runnable is null
     * @see {@link #runOnBackground(Runnable, int)}
     */
    void runOnBackground(Runnable runnable);

    /**
     * Executes the given runnable at some time in the future.  The runnable
     * may runOnBackground in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param runnable the runnable task
     * @param priority the thread priority
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if runnable is null
     * @see {@link android.os.Process} for the the priority values
     * (Ex: {@link android.os.Process.THREAD_PRIORITY_BACKGROUND})
     */
    void runOnBackground(Runnable runnable, int priority);

    /**
     * Executes the given runnable at some time in the future but in
     * the UIThread.
     * @param runnable the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if runnable is null
     */
    void runOnUIThread(Runnable runnable);

    /**
     * Cancel (if possible) the runnable execution
     * @param runnable the runnable task
     * @throws NullPointerException if runnable is null
     */
    void cancel(Runnable runnable);

}
