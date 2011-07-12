package org.icefaces.samples.showcase.example.compat.progress;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PortableRenderer;

import org.icefaces.samples.showcase.util.FacesUtils;

@ManagedBean(name= LongTaskManager.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LongTaskManager implements Serializable {
    public static final String BEAN_NAME = "task";
    
	public static final String PUSH_GROUP = "ourUser" + System.currentTimeMillis();
	private static final int MAX_PERCENT = 100;
	
	private Random randomizer;
	private PortableRenderer renderer; // required when PushRenderer would be used inside a thread
	private boolean taskRunning = false;
	// Used for the main, single progress bar demos
	private int progress = 0;
	// Used for the multiple progress bar demo
	private int[] progresses = new int[] { 0, 0, 0, 0 };
	private int firstComplete = -1;
	
	@PostConstruct
	private void init() {
	    // Add our session
	    PushRenderer.addCurrentSession(PUSH_GROUP);
	    
	    // Prepare the portable renderer
	    renderer = PushRenderer.getPortableRenderer(FacesContext.getCurrentInstance());
	    
	    // Prep the generator
	    randomizer = new Random(System.nanoTime());
	}
	
	@PreDestroy
	private void deinit() {
	    // Ensure our task is stopped
	    setTaskRunning(false);
        
        // Clean up the renderer
        // Not strictly necessary, but nice to do
        PushRenderer.removeCurrentSession(PUSH_GROUP);
        renderer = null;
	}
	
	public boolean getTaskRunning() { return taskRunning; }
	public int getProgress() { return progress; }
	public int[] getProgresses() { return progresses; }
	public int getFirstComplete() { return firstComplete; }
	public boolean getHasFirstComplete() { return firstComplete >= 0; }
	
	public void setTaskRunning(boolean taskRunning) { this.taskRunning = taskRunning; }
	public void setProgress(int progress) { this.progress = progress; }
	public void setProgresses(int[] progresses) { this.progresses = progresses; }
	public void setFirstComplete(int firstComplete) { this.firstComplete = firstComplete; }
	
	private void internalThreadMethod(final int minIncrease,
	                                  final int maxIncrease,
	                                  final int sleepAmount,
	                                  final boolean single) {
	    // Reset the progress / progresses if they are at the maximum
	    // Otherwise leave them alone as the user may have stopped/started the progress bar
	    //  and in that case we want it to continue from the previous percent
	    if (single) {
	        if (progress == MAX_PERCENT) {
	            progress = 0;
	        }
        }
        else {
            firstComplete = -1;
            
            for (int i = 0; i < progresses.length; i++) {
                progresses[i] = 0;
            }
        }
	    
	    // Ensure we only have one thread going at once
	    if (!taskRunning) {
	        // Use our global application wide thread pool
	        LongTaskPool pool =
	            (LongTaskPool)FacesUtils.getManagedBean(LongTaskPool.BEAN_NAME);
	            
	        // Start a new long running process to simulate a delay
            pool.getThreadPool().execute(new Runnable() {
                public void run() {
                    int completeCount = 0;
                    setTaskRunning(true);
                    
                    // Loop until a break condition inside
                    while (true) {
                        if (single) {
                            progress += minIncrease+randomizer.nextInt(maxIncrease);
                            
                            // Ensure that we don't break the max
                            // Also we can stop if we reach the top, instead of having an extra Thread.sleep
                            if (progress >= MAX_PERCENT) {
                                progress = MAX_PERCENT;
                                break;
                            }
                        }
                        else {
                            // Update each progress in our list
                            for (int i = 0; i < progresses.length; i++) {
                                if (progresses[i] < MAX_PERCENT) {
                                    progresses[i] += minIncrease+randomizer.nextInt(maxIncrease);
                                
                                    // Ensure that we don't break the max for this progress
                                    if (progresses[i] >= MAX_PERCENT) {
                                        progresses[i] = MAX_PERCENT;
                                        completeCount++;
                                        
                                        // Note which bar completed first
                                        if (completeCount == 1) {
                                            firstComplete = i;
                                        }
                                    }
                                }
                            }
                            
                            // Stop looping if we have completed all the progresses in our list
                            if (completeCount == progresses.length) {
                                break;
                            }
                        }
                        
                        // Render the updated progress
                        renderer.render(PUSH_GROUP);
                        
                        // Simulate a pause
                        try{
                            Thread.sleep(sleepAmount);
                        }catch (Exception ignored) { }
                        
                        // Ensure we're not supposed to stop
                        if (!taskRunning) {
                            break;
                        }
                    }
                    
                    // Complete the task and update the page
                    setTaskRunning(false);
                    renderer.render(PUSH_GROUP);
                }
            });
        }
    }
	
	public void startMultiThread(int minIncrease, int maxIncrease, int sleepAmount) {
	    internalThreadMethod(minIncrease, maxIncrease, sleepAmount, false);
    }
	
	public void startThread(int minIncrease, int maxIncrease, int sleepAmount) {
	    internalThreadMethod(minIncrease, maxIncrease, sleepAmount, true);
	}
	
	public void stopAndResetTask(ActionEvent event) {
	    stopTask(event);
	    resetTask(event);
	}
	
	public void stopTask(ActionEvent event) {
	    setTaskRunning(false);
	}
	
	public void resetTask(ActionEvent event) {
	    firstComplete = -1;
	    progress = 0;
	    for (int i = 0; i < progresses.length; i++) {
	        progresses[i] = 0;
	    }
	}
}
