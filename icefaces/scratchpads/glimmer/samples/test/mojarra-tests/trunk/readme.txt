Mojarra Test Notes
==================

Running the tests
-----------------


Current Result Status
---------------------

The last results that I got for running Glimmer using ICEfaces pre-alpha 3 against Mojarra 2.0.2

     [exec] Result: 1
    [junit] Test com.sun.faces.composite.CompositeComponentsTestCase FAILED
    [junit] Test com.sun.faces.facelets.FaceletsTestCase FAILED
    [junit] Test com.sun.faces.systest.tags.EventTestCase FAILED
    [junit] Test com.sun.faces.systest.implicitnav.ImplicitNavigationTestCase FAILED
    [junit] Test com.sun.faces.systest.state.DynamicStateTestCase FAILED
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1
     [exec] Result: 1


These failures and other possible situations are currently all known and explained:

TestTreeVisit

Fails sporadically it seems like so it may show up as a failure or may not.  It builds a tree programmatically and then tries to visit it and harvest the ids into a string that it checks against an expected String.  Don't think we're doing anything wrong here.


messages01.jsp
messages02.jsp

These are golden file comparison tests that fail (causing the whole test run to bail out) because we add div to the messages renderering now.  Changing the "golden" text files may work but we add ids to the divs all the time which may make them just as fragile.  I've commented them out for now.  These and a few others that I didn't comment out look like they may be using JSFUnit to test.


CompositeComponentTestCase

Counting extra spans again.

<script id="form:_captureSubmit" type="text/javascript">ice.captureSubmit('form');ice.captureEnterKey('form');</script><span id="form:_captureSubmit"></span>

Seems like when Mark replaced the event listeners that used the ScriptWriter with a different class, this came back.  We can alter the tests but I don't think we need spans around these.  Mostly harmless.  The original failures for this case (before we had the fixes for h:messages and ui:debug) were far worse and have been corrected.


FaceletsTestCase

Needs to adjust some code to the test so that it can find the <ul> inside the <div> we wrapped around the messages.


EventTestCase

Extra spans around scripts just like CompositeComponentTestCase


ImplicitNavigationTestCase

All the navigation works, the failure occurs when HtmlUnit turns off redirection and then attempts to do navigation.  At this point, it expects an error to occur.  Can't test this easily with a normal browser but the test used to fail much earlier due to problems with navigation so we've improved this significantly already. I'd say it's safe to release with this one in this state.

client.setRedirectEnabled(false);


DynamicStateTestCase

This is a known "bad" test for some reason that throws a NPE the 2nd time the button is pressed.  Appears related to StateManagement

Caused by: java.lang.NullPointerException
    at com.sun.faces.application.view.StateManagementStrategyImpl$4.invokeContextCallback(StateManagementStrategyImpl.java:289)
    at javax.faces.component.UIComponent.invokeOnComponent(UIComponent.java:1253)

All the instances of
     [exec] Result: 1

are caused by the test run trying to undeploy various tests (which fails cause it isn't there yet) before it reploys it.  This is expected and okay behaviour.
