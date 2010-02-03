package org.icepush.integration.spring.samples.dice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResultController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    private static final String OUR_VIEW = "result";

    private DiceData diceData;

    public ResultController() {
        super();
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws ServletException, IOException {
        return new ModelAndView(OUR_VIEW, "dice", diceData);
    }

    public DiceData getDiceData() {
        return diceData;
    }

    public void setDiceData(DiceData diceData) {
        this.diceData = diceData;
    }
}
