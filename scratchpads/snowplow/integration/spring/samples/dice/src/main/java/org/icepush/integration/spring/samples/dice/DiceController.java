package org.icepush.integration.spring.samples.dice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.util.Random;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiceController extends SimpleFormController {
    protected final Log logger = LogFactory.getLog(getClass());

    private static final String OUR_VIEW = "roll";

    private static final int TUMBLE_COUNT = 5;
    private static final long SLEEP_COUNT = 1000;
    private static final int DICE_MIN = 1;
    private static final int DICE_MAX = 6;

    private Thread rollerThread;
    private boolean rollerThreadRunning = false;
    private Random randomizer;

    private PushRequestManager pushRequestManager;
    private DiceData diceData;

    public DiceController() {
        super();

        randomizer = new Random(System.currentTimeMillis());
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
                                 Object command, BindException errors)
                                 throws Exception {
        // Ensure the push context has been set up
        if (!pushRequestManager.isInitialized()) {
            pushRequestManager.intializePushContext(request, response);
        }

        // Clean up any old dice results
        if (diceData.isDoneRolling()) {
            diceData.reset();
            pushRequestManager.requestPush();
        }

        // Stop the old thread if needed
        if (rollerThreadRunning) {
            rollerThread.interrupt();
        }

        // Setup a rolling thread
        // Basically simulate a dice tumbling around before settling on a final value
        rollerThread = new Thread(new Runnable() {
            public void run() {
                rollerThreadRunning = true;

                for (int i = 0; i < TUMBLE_COUNT; i++) {
                    // Pause before each tumble so the user can see it
                    try{
                        Thread.sleep(SLEEP_COUNT);
                    }catch (InterruptedException ie) {
                        logger.error("Interrupted dice rolling", ie);

                        diceData.setDoneRolling(false);
                        rollerThreadRunning = false;
                        
                        return;
                    }

                    // Roll 'em!
                    rollDice();
                }

                // Declare the set of rolls done, and push this info to the user
                diceData.setDoneRolling(true);
                pushRequestManager.requestPush();

                rollerThreadRunning = false;
            }
        });
        rollerThread.start();

        return new ModelAndView(OUR_VIEW, "push", pushRequestManager);
    }

    public int rollDice() {
        // Generate the current dice value
        int tumbleValue = DICE_MIN+randomizer.nextInt(DICE_MAX);

        // Recursively reroll the dice if we get the same value as we had
        // This is because the same value twice in a row looks incorrect to the user
        if (tumbleValue == diceData.getValue()) {
            return rollDice();
        }

        // Apply the value and push it to the user
        diceData.setValue(tumbleValue);
        pushRequestManager.requestPush();

        logger.info("Pushed Dice Value [" + diceData.getValue() + "]");

        return diceData.getValue();
    }

    protected Object formBackingObject(HttpServletRequest request)
                                   throws ServletException {
        return diceData;
    }    

    public PushRequestManager getPushRequestManager() {
        return pushRequestManager;
    }

    public void setPushRequestManager(PushRequestManager pushRequestManager) {
        this.pushRequestManager = pushRequestManager;
    }

    public DiceData getDiceData() {
        return diceData;
    }

    public void setDiceData(DiceData diceData) {
        this.diceData = diceData;
    }
}
