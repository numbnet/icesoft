package com.icesoft.icefaces.tutorial.component.outputProgress.multiple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

/**
 * Class used to manage multiple progress bars
 * This will also use a thread to update the percent value of each progress bar
 */
public class MultipleBean
{
    private static final int PAUSE_AMOUNT_S = 1000; // milliseconds to pause between progress updates
    private PersistentFacesState state;
    private Random randomizer = new Random(System.currentTimeMillis());
    private ArrayList progressBarList = generateDefaultList();
    private Thread updateThread;
    private boolean isRunning = true;
    
    public MultipleBean() {
        state = PersistentFacesState.getInstance();
        
        startUpdateThread();
    }
    
    public PersistentFacesState getState() {
        return state;
    }
    
    public ArrayList getProgressBarList() {
        return progressBarList;
    }
    
    public void setState(PersistentFacesState state) {
        this.state = state;
    }
    
    public void setProgressBarList(ArrayList progressBarList) {
        this.progressBarList = progressBarList;
    }
    
    /**
     * Convience method to generate a list of data to test with
     *
     *@return the list of progress bars
     */
    private ArrayList generateDefaultList() {
        ArrayList toReturn = new ArrayList(7);
        
        toReturn.add(new ProgressBar("Calgary", randomizer));
        toReturn.add(new ProgressBar("Moscow", randomizer));
        toReturn.add(new ProgressBar("Tokyo", randomizer));
        toReturn.add(new ProgressBar("Vancouver", randomizer));
        toReturn.add(new ProgressBar("New York", randomizer));
        toReturn.add(new ProgressBar("Ashton", randomizer));
        toReturn.add(new ProgressBar("Rome", randomizer));
        
        return toReturn;
    }
    
    /**
     * Method to start the thread that will update the progress bars
     * Basically sleep for PAUSE_AMOUNT_S time, then request an update of each percent value
     */
    private void startUpdateThread() {
        updateThread = new Thread(new Runnable() {
            public void run() {
                final int size = progressBarList.size();
                
                while (isRunning) {
                    // Request an update of each progress bar
                    for (int i = 0; i < size; i++) {
                        ((ProgressBar)progressBarList.get(i)).update();
                    }
                    
                    try{
                        Thread.sleep(PAUSE_AMOUNT_S);
                    }catch (InterruptedException failedSleep) { }
                    
                    // Render the page
                    // The standard approach would be to use the RenderManager
                    // But since this is a simple tutorial, we'll instead go directly
                    //  to the PersistentFacesState
                    // If the render fails, we'll stop this thread from running
                    try{
                        state.render();
                    }catch (Exception failedRender) {
                        isRunning = false;
                    }
                }
            }
        });
        
        updateThread.start();
    }
}