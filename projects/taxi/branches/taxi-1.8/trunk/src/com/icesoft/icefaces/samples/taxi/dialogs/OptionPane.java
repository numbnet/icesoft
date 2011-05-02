package com.icesoft.icefaces.samples.taxi.dialogs;

import com.icesoft.icefaces.samples.taxi.util.Command;

import javax.faces.event.ActionEvent;

public class OptionPane {
    /**
     * Creates a new dialog which will be formated with an error graphic.
     */
    public static final int ERROR_MESSAGE = 100;
    /**
     * Creates a new dialog which will be formated with an information graphic.
     */
    public static final int INFORMATION_MESSAGE = 105;
    /**
     * Creates a new dialog which will be formated with a warning graphic.
     */
    public static final int WARNING_MESSAGE = 110;
    /**
     * Creates a new dialog which will be formated with a question graphic.
     */
    public static final int QUESTION_MESSAGE = 115;
    /**
     * Creates a new dialog which will be formated with no graphic.
     */
    public static final int PLAIN_MESSAGE = 120;

    /**
     * No message formating for this type of dialog.
     */
    public static final int NO_MESSAGE = 125;

    /**
     * Creates a dialog which has just a OK button.
     */
    public static final int DEFAULT_OPTION = 1;
    /**
     * Creates a dialog which has Yes and No button.
     */
    public static final int YES_NO_OPTION = 2;
    /**
     * Creates a dialog which has a Yes, No and Cancel option.
     */
    public static final int YES_NO_CANCEL_OPTION = 3;
    /**
     * Creates a dialog which has an OK and Cancel option.
     */
    public static final int OK_CANCEL_OPTION = 4;

    /**
     * Creates a dialog with no options command buttons.
     */
    public static final int NO_OPTOINS = 5;

    // icon used when displaying dialog
    protected String icon;
    // option type
    protected int optionType;
    // title for optionPane window
    protected String title;
    // Message for optionPane body.
    protected String message;
    // Message style, determines which icon graphics will be used.
    protected int messageStyle;
    // show hide the dialog
    protected boolean rendered;

    protected String cssStyle;

    public static final String ERROR_ICON = "Error.gif";
    public static final String INFORMATION_ICON = "Inform.gif";
    public static final String WARNING_ICON = "Warn.gif";
    public static final String QUESTION_ICON = "Question.gif";
    public static final String PLAIN_ICON = "spacer.gif";

    private boolean isOkButtonVisible = true;
    private boolean isYesButtonVisible = true;
    private boolean isNoButtonVisible = true;
    private boolean isCancelButtonVisible = true;

    protected Command yesCommand;
    protected Command okCommand;
    protected Command noCommand;
    protected Command cancelCommand;

    public OptionPane() {
    }

    /**
     * Creates a new OptionPane which will not be visible.
     *
     * @param title       title used for dialog window
     * @param message     message to display to user
     * @param messageType message type
     * @param optionType  option type
     */
    public OptionPane(String title, String message, int messageType, int optionType) {
        this.title = title;
        this.message = message;
        setMessageStyle(messageType);
        setOptionType(optionType);
        rendered = false;
    }

    /**
     * Sets the command which will be executed when when the ok button is pressed.
     *
     * @param command command to execute.
     */
    public void setOKButtonCommand(Command command) {
        okCommand = command;
    }

    /**
     * Sets the command which will be executed when when the yes button is pressed.
     *
     * @param command command to execute.
     */
    public void setYesButtonCommand(Command command) {
        yesCommand = command;
    }

    /**
     * Sets the command which will be executed when when the no button is pressed.
     *
     * @param command command to execute.
     */
    public void setNoButtonCommand(Command command) {
        noCommand = command;
    }

    /**
     * Sets the command which will be executed when when the cancel button is pressed.
     *
     * @param command command to execute.
     */
    public void setCancelButtonCommand(Command command) {
        cancelCommand = command;
    }

    /**
     * Default behavior is to close the dialog when it is either manually closed
     * or the ok button has been pressed.
     *
     * @param event jsf action event.
     */
    public void okButtonAction(ActionEvent event) {
        rendered = false;
        if (okCommand != null) {
            okCommand.execute();
        }
    }


    public void yesButtonAction(ActionEvent event) {
        rendered = false;
        if (yesCommand != null) {
            yesCommand.execute();
        }
    }

    public void noButtonAction(ActionEvent event) {
        rendered = false;
        if (noCommand != null) {
            noCommand.execute();
        }
    }

    public void cancelButtonAction(ActionEvent event) {
        rendered = false;
        if (cancelCommand != null) {
            cancelCommand.execute();
        }
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
        // set up the correct icon
        if (optionType == DEFAULT_OPTION) {
            isOkButtonVisible = true;
            isYesButtonVisible = false;
            isNoButtonVisible = false;
            isCancelButtonVisible = false;
        } else if (optionType == OK_CANCEL_OPTION) {
            isOkButtonVisible = true;
            isYesButtonVisible = false;
            isNoButtonVisible = false;
            isCancelButtonVisible = true;
        } else if (optionType == YES_NO_OPTION) {
            isOkButtonVisible = false;
            isYesButtonVisible = true;
            isNoButtonVisible = true;
            isCancelButtonVisible = false;
        } else if (optionType == YES_NO_CANCEL_OPTION) {
            isOkButtonVisible = false;
            isYesButtonVisible = true;
            isNoButtonVisible = true;
            isCancelButtonVisible = true;
        } else if (optionType == NO_OPTOINS) {
            isOkButtonVisible = false;
            isYesButtonVisible = false;
            isNoButtonVisible = false;
            isCancelButtonVisible = false;
        }
    }


    public int getMessageStyle() {
        return messageStyle;
    }

    public void setMessageStyle(int messageStyle) {
        this.messageStyle = messageStyle;

        // set up appropriate icon
        if (messageStyle == ERROR_MESSAGE) {
            icon = ERROR_ICON;
        } else if (messageStyle == INFORMATION_MESSAGE) {
            icon = INFORMATION_ICON;
        } else if (messageStyle == WARNING_MESSAGE) {
            icon = WARNING_ICON;
        } else if (messageStyle == QUESTION_MESSAGE) {
            icon = QUESTION_ICON;
        } else if (messageStyle == PLAIN_MESSAGE) {
            icon = PLAIN_ICON;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isCancelButtonVisible() {
        return isCancelButtonVisible;
    }

    public void setCancelButtonVisible(boolean cancelButtonVisible) {
        isCancelButtonVisible = cancelButtonVisible;
    }

    public boolean isOkButtonVisible() {
        return isOkButtonVisible;
    }

    public void setOkButtonVisible(boolean okButtonVisible) {
        isOkButtonVisible = okButtonVisible;
    }

    public boolean isYesButtonVisible() {
        return isYesButtonVisible;
    }

    public void setYesButtonVisible(boolean yesButtonVisible) {
        isYesButtonVisible = yesButtonVisible;
    }

    public boolean isNoButtonVisible() {
        return isNoButtonVisible;
    }

    public void setNoButtonVisible(boolean noButtonVisible) {
        isNoButtonVisible = noButtonVisible;
    }


    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }
}
