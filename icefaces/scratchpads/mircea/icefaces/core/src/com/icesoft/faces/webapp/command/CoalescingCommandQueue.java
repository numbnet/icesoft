package com.icesoft.faces.webapp.command;

import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Semaphore;

public class CoalescingCommandQueue {
    private ExecutorService executor;
    private Semaphore semaphore = new Semaphore(1);
    private Command currentCommand;
    private Runnable writeListener;

    public CoalescingCommandQueue(ExecutorService executor) {
        this.executor = executor;
    }

    public void put(Command command) {
        try {
            semaphore.acquire();
            if (currentCommand == null) {
                currentCommand = command;
            } else {
                currentCommand = currentCommand.coallesceWith(command);
            }
            semaphore.release();
            executor.execute(writeListener);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Command take() {
        try {
            semaphore.acquire();
            Command command = currentCommand;
            currentCommand = null;
            semaphore.release();

            return command;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void onWrite(Runnable listener) {
        this.writeListener = listener;
    }
}
