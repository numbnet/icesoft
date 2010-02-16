package org.icepush.integration.spring.samples.dice;

public class DiceData {
    private static final int DEFAULT_VALUE = 0;

    private int value = DEFAULT_VALUE;
    private boolean doneRolling = false;

    public DiceData() {
    }

    public DiceData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isDoneRolling() {
        return doneRolling;
    }

    public void setDoneRolling(boolean doneRolling) {
        this.doneRolling = doneRolling;
    }

    public void reset() {
        setValue(DEFAULT_VALUE);
        setDoneRolling(false);
    }
}
